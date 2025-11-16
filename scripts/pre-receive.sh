#!/usr/bin/env bash
# Server-side pre-receive hook: Enforce commit rules on the remote
#
# INSTALLATION (on remote repository):
#   cp scripts/pre-receive /path/to/remote/repo.git/hooks/pre-receive
#   chmod +x /path/to/remote/repo.git/hooks/pre-receive
#
# PURPOSE:
# This is the ULTIMATE DEFENSE against --no-verify bypasses.
# Client-side hooks (pre-commit, pre-push) can be bypassed with --no-verify,
# but server-side hooks CANNOT be bypassed - they run on the server.
#
# WHEN IT RUNS: On the server, after receiving push but before updating refs
# CANNOT BE BYPASSED: Users cannot use --no-verify to skip this
#
# This validates ALL commits being pushed follow the rules in docs/agentRules/commitRules.md

set -euo pipefail

RED='\033[0;31m'
GREEN='\033[0;32m'
NC='\033[0m' # No Color

echo "" >&2
echo "🛡️  Server-side validation: Checking commit rules..." >&2
echo "" >&2

VIOLATIONS=0

# Read from stdin: <old-value> <new-value> <ref-name>
while read -r oldrev newrev refname; do
    # Skip branch deletions
    if [ "$newrev" = "0000000000000000000000000000000000000000" ]; then
        continue
    fi
    
    # Determine range of commits to check
    if [ "$oldrev" = "0000000000000000000000000000000000000000" ]; then
        # New branch - check last 50 commits
        range="$newrev~50..$newrev"
    else
        # Existing branch - check new commits
        range="$oldrev..$newrev"
    fi
    
    echo "📋 Validating commits in $refname..." >&2
    echo "" >&2
    
    # Get list of commits in the range
    commits=$(git rev-list "$range" 2>/dev/null || echo "")
    
    if [ -z "$commits" ]; then
        echo -e "${GREEN}✓${NC} No new commits to validate" >&2
        continue
    fi
    
    for commit in $commits; do
        # Get commit message (first line only)
        msg=$(git log -1 --pretty=%B "$commit" | head -1)
        commit_short="${commit:0:7}"
        
        # Get files changed in this commit
        files=$(git diff-tree --no-commit-id --name-only -r "$commit")
        file_count=$(echo "$files" | wc -l | tr -d ' ')
        
        # Count documentation files
        doc_count=$(echo "$files" | grep -c '\.md$' || true)
        
        # Count code files
        code_count=$(echo "$files" | grep -cv '\.md$' || true)
        
        # Check 1: Multiple documentation files per commit
        if [ "$doc_count" -gt 1 ]; then
            echo -e "${RED}❌ VIOLATION in commit $commit_short:${NC}" >&2
            echo -e "   Multiple documentation files in one commit ($doc_count files)" >&2
            echo -e "   Rule: Each documentation file MUST be in its own commit" >&2
            echo -e "   Message: $msg" >&2
            echo "" >&2
            VIOLATIONS=$((VIOLATIONS + 1))
        fi
        
        # Check 2: Mixed code and documentation
        if [ "$code_count" -gt 0 ] && [ "$doc_count" -gt 0 ]; then
            echo -e "${RED}❌ VIOLATION in commit $commit_short:${NC}" >&2
            echo -e "   Mixing code and documentation changes" >&2
            echo -e "   Code files: $code_count | Documentation files: $doc_count" >&2
            echo -e "   Rule: Documentation changes MUST be in separate commits" >&2
            echo -e "   Message: $msg" >&2
            echo "" >&2
            VIOLATIONS=$((VIOLATIONS + 1))
        fi
        
        # Check 3: Too many files per commit
        if [ "$file_count" -gt 5 ]; then
            echo -e "${RED}❌ VIOLATION in commit $commit_short:${NC}" >&2
            echo -e "   Too many files in commit ($file_count files, maximum: 5)" >&2
            echo -e "   Rule: Keep commits focused (ideal: 1-3 files)" >&2
            echo -e "   Message: $msg" >&2
            echo "" >&2
            VIOLATIONS=$((VIOLATIONS + 1))
        fi
        
        # Check 4: Module README size (if any module READMEs were changed)
        for file in $files; do
            if [[ "$file" == */README.md ]] && \
               [[ "$file" != docs/* ]] && \
               [[ "$file" != scripts/* ]] && \
               [[ "$file" != buildLogic/* ]] && \
               [[ "$file" != gradle/* ]]; then
                # Get file content at this commit
                content=$(git show "$commit:$file" 2>/dev/null || echo "")
                if [ -n "$content" ]; then
                    line_count=$(echo "$content" | wc -l | tr -d ' ')
                    if [ "$line_count" -gt 100 ]; then
                        echo -e "${RED}❌ VIOLATION in commit $commit_short:${NC}" >&2
                        echo -e "   Module README exceeds size limit: $file ($line_count lines, max: 100)" >&2
                        echo -e "   Rule: Module READMEs must be < 100 lines" >&2
                        echo -e "   Message: $msg" >&2
                        echo "" >&2
                        VIOLATIONS=$((VIOLATIONS + 1))
                    fi
                fi
            fi
        done
        
        # Check 5: File naming convention (camelCase for .md files)
        for file in $files; do
            if [[ "$file" == *.md ]]; then
                filename=$(basename "$file")
                # Skip README.md (allowed to be uppercase)
                if [[ "$filename" != "README.md" ]]; then
                    # Check if filename starts with uppercase or has underscores
                    if [[ "$filename" =~ ^[A-Z] ]] || [[ "$filename" =~ _ ]]; then
                        echo -e "${RED}❌ VIOLATION in commit $commit_short:${NC}" >&2
                        echo -e "   Wrong file naming: $file" >&2
                        echo -e "   Rule: .md files must use camelCase (start lowercase, no underscores)" >&2
                        echo -e "   Message: $msg" >&2
                        echo "" >&2
                        VIOLATIONS=$((VIOLATIONS + 1))
                    fi
                fi
            fi
        done
        
        # Check 6: No Kotlin/Java code examples in markdown files
        for file in $files; do
            if [[ "$file" == *.md ]] && [[ "$(basename "$file")" != "README.md" ]]; then
                # Get file content at this commit and check for code blocks
                content=$(git show "$commit:$file" 2>/dev/null || echo "")
                if echo "$content" | grep -q "\`\`\`kotlin\|\`\`\`java"; then
                    echo -e "${RED}❌ VIOLATION in commit $commit_short:${NC}" >&2
                    echo -e "   File contains Kotlin/Java code examples: $file" >&2
                    echo -e "   Rule: Code examples belong in KDoc, not markdown files" >&2
                    echo -e "   Exception: README.md files may have brief usage examples" >&2
                    echo -e "   Message: $msg" >&2
                    echo "" >&2
                    VIOLATIONS=$((VIOLATIONS + 1))
                fi
            fi
        done
    done
done

if [ "$VIOLATIONS" -gt 0 ]; then
    echo -e "${RED}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}" >&2
    echo -e "${RED}🚫 PUSH REJECTED: $VIOLATIONS commit rule violation(s) found${NC}" >&2
    echo -e "${RED}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}" >&2
    echo "" >&2
    echo "Your commits violate the rules in docs/agents/commitRules.md" >&2
    echo "" >&2
    echo "⚠️  You CANNOT bypass this check with --no-verify" >&2
    echo "    This is a server-side hook that runs on the remote repository" >&2
    echo "" >&2
    echo "To fix:" >&2
    echo "  1. Use 'git rebase -i origin/main' to edit the problematic commits" >&2
    echo "  2. Split large commits into smaller, focused ones" >&2
    echo "  3. Separate documentation changes from code changes" >&2
    echo "  4. Ensure each doc file has its own commit" >&2
    echo "  5. Push again after fixing" >&2
    echo "" >&2
    exit 1
fi

echo -e "${GREEN}✓${NC} All commits follow the rules" >&2
echo "" >&2

exit 0
