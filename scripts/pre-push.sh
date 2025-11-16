#\!/usr/bin/env bash
# Pre-push hook: Validate commit rules and run code quality checks
#
# WHY THIS EXISTS:
# - Ensures all commits follow the documented commit rules
# - Catches bugs and code quality issues before they reach the remote repository
# - Prevents breaking CI builds that waste time and resources
# - Provides faster feedback (seconds locally vs minutes waiting for CI)
# - Reduces "fix CI" commits that clutter git history
# - Acts as a safety net for all developers
#
# PHILOSOPHY: "Shift left" - find issues as early as possible in the dev cycle
#
# WHEN IT RUNS: Automatically before every `git push`
# BYPASSING: Using `git push --no-verify` is FORBIDDEN
#            See docs/agentRules/commitRules.md for the strict policy
#            Fix your commits instead of bypassing the checks

set -euo pipefail

RED='\033[0;31m'
YELLOW='\033[1;33m'
GREEN='\033[0;32m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo "" >&2
echo "🔍 Running pre-push checks..." >&2
echo "" >&2

# Read all input from stdin at once to avoid blocking git
# Git passes: <local ref> <local sha> <remote ref> <remote sha>
input=$(cat)

if [ -z "$input" ]; then
    echo "No refs to push" >&2
    exit 0
fi

# Check if this is a new branch being pushed for the first time
# Extract branch info from the input
while IFS= read -r line; do
    read -r local_ref local_sha remote_ref remote_sha <<< "$line"
    
    if [ "$local_sha" = "0000000000000000000000000000000000000000" ]; then
        # Handle delete - no commits to check
        continue
    fi
    
    # Extract branch name from refs/heads/branch-name
    branch_name="${local_ref#refs/heads/}"
    remote_name="${remote_ref%%/*}"  # Extract remote name (usually 'origin')
    
    # Check if remote branch exists
    if [ "$remote_sha" = "0000000000000000000000000000000000000000" ]; then
        echo -e "${BLUE}ℹ️  New branch detected: ${branch_name}${NC}" >&2
        echo "" >&2
        echo "This branch doesn't exist on the remote yet." >&2
        echo "" >&2
        echo -e "${YELLOW}💡 TIP: For first-time push, you typically need the -u flag:${NC}" >&2
        echo "   git push -u ${remote_name} ${branch_name}" >&2
        echo "" >&2
        echo "This command will:" >&2
        echo "  • Push your branch to the remote" >&2
        echo "  • Set up tracking so future pushes can use just 'git push'" >&2
        echo "" >&2
        echo "Proceeding with validation checks..." >&2
        echo "" >&2
    fi
done <<< "$input"

# Process each line for validation
# Use process substitution to avoid subshell and allow exit to work properly
while IFS= read -r line; do
    read -r local_ref local_sha remote_ref remote_sha <<< "$line"
    
    if [ "$local_sha" = "0000000000000000000000000000000000000000" ]; then
        # Handle delete - no commits to check
        continue
    fi
    
    if [ "$remote_sha" = "0000000000000000000000000000000000000000" ]; then
        # New branch - check all commits
        if git rev-parse --verify origin/main >/dev/null 2>&1; then
            range="origin/main..$local_sha"
        else
            # If origin/main doesn't exist, check last 10 commits
            range="$local_sha~10..$local_sha"
        fi
    else
        # Existing branch - check commits since last push
        range="$remote_sha..$local_sha"
    fi
    
    # Validate commit rules
    echo "📋 Validating commit rules for commits being pushed..." >&2
    echo "" >&2
    
    VIOLATIONS=0
    
    # Get list of commits in the range
    commits=$(git rev-list "$range" 2>/dev/null || echo "")
    
    if [ -z "$commits" ]; then
        echo -e "${GREEN}✓${NC} No new commits to validate" >&2
        echo "" >&2
    else
        for commit in $commits; do
            # Get commit message (first line only)
            msg=$(git log -1 --pretty=%B "$commit" | head -1)
            
            # Get files changed in this commit
            files=$(git diff-tree --no-commit-id --name-only -r "$commit")
            file_count=$(echo "$files" | wc -l | tr -d ' ')
            
            # Count documentation files
            doc_count=$(echo "$files" | grep -c '\.md$' || true)
            
            # Count code files (add || true to handle when all files are .md)
            code_count=$(echo "$files" | grep -cv '\.md$' || true)
            
            # Check 1: Multiple documentation files per commit
            if [ "$doc_count" -gt 1 ]; then
                echo -e "${RED}❌ VIOLATION in commit ${commit:0:7}:${NC}" >&2
                echo -e "   Multiple documentation files in one commit ($doc_count files)" >&2
                echo -e "   Rule: Each documentation file MUST be in its own commit" >&2
                echo -e "   Commit message: $msg" >&2
                echo "" >&2
                VIOLATIONS=$((VIOLATIONS + 1))
            fi
            
            # Check 2: Mixed code and documentation
            if [ "$code_count" -gt 0 ] && [ "$doc_count" -gt 0 ]; then
                echo -e "${RED}❌ VIOLATION in commit ${commit:0:7}:${NC}" >&2
                echo -e "   Mixing code and documentation changes" >&2
                echo -e "   Code files: $code_count | Documentation files: $doc_count" >&2
                echo -e "   Rule: Documentation changes MUST be in separate commits" >&2
                echo -e "   Commit message: $msg" >&2
                echo "" >&2
                VIOLATIONS=$((VIOLATIONS + 1))
            fi
            
            # Check 3: Too many files per commit
            if [ "$file_count" -gt 5 ]; then
                echo -e "${RED}❌ VIOLATION in commit ${commit:0:7}:${NC}" >&2
                echo -e "   Too many files in commit ($file_count files, maximum: 5)" >&2
                echo -e "   Rule: Keep commits focused (ideal: 1-3 files)" >&2
                echo -e "   Commit message: $msg" >&2
                echo "" >&2
                VIOLATIONS=$((VIOLATIONS + 1))
            elif [ "$file_count" -gt 3 ]; then
                echo -e "${YELLOW}⚠️  WARNING in commit ${commit:0:7}:${NC}" >&2
                echo -e "   Large commit ($file_count files, ideal: 1-3)" >&2
                echo -e "   Consider splitting by layer if unrelated" >&2
                echo -e "   Commit message: $msg" >&2
                echo "" >&2
            fi
            
            # Check 4: Module README size (if any module READMEs were changed)
            for file in $files; do
                if [[ "$file" == */README.md ]] && [[ "$file" != docs/* ]]; then
                    if [ -f "$file" ]; then
                        line_count=$(wc -l < "$file" | tr -d ' ')
                        if [ "$line_count" -gt 100 ]; then
                            echo -e "${RED}❌ VIOLATION in commit ${commit:0:7}:${NC}" >&2
                            echo -e "   Module README exceeds size limit: $file ($line_count lines, max: 100)" >&2
                            echo -e "   Rule: Module READMEs must be < 100 lines" >&2
                            echo -e "   Commit message: $msg" >&2
                            echo "" >&2
                            VIOLATIONS=$((VIOLATIONS + 1))
                        fi
                    fi
                fi
            done
            
            # Check 5: No Kotlin/Java code examples in markdown files
            for file in $files; do
                if [[ "$file" == *.md ]] && [[ "$(basename "$file")" != "README.md" ]]; then
                    # Get file content at this commit and check for code blocks
                    content=$(git show "$commit:$file" 2>/dev/null || echo "")
                    if echo "$content" | grep -q "\`\`\`kotlin\|\`\`\`java"; then
                        echo -e "${RED}❌ VIOLATION in commit ${commit:0:7}:${NC}" >&2
                        echo -e "   File contains Kotlin/Java code examples: $file" >&2
                        echo -e "   Rule: Code examples belong in KDoc, not markdown files" >&2
                        echo -e "   Exception: README.md files may have brief usage examples" >&2
                        echo -e "   Commit message: $msg" >&2
                        echo "" >&2
                        VIOLATIONS=$((VIOLATIONS + 1))
                    fi
                fi
            done
        done
        
        if [ "$VIOLATIONS" -gt 0 ]; then
            echo -e "${RED}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}" >&2
            echo -e "${RED}❌ PUSH BLOCKED: $VIOLATIONS commit rule violation(s) found${NC}" >&2
            echo -e "${RED}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}" >&2
            echo "" >&2
            echo "Your commits violate the rules in docs/agents/commitRules.md" >&2
            echo "" >&2
            echo "To fix:" >&2
            echo "  1. Use 'git rebase -i' to edit the problematic commits" >&2
            echo "  2. Split large commits into smaller, focused ones" >&2
            echo "  3. Separate documentation changes from code changes" >&2
            echo "  4. Ensure each doc file has its own commit" >&2
            echo "" >&2
            echo "Need help? Run: ./gradlew detekt test" >&2
            echo "to see detailed error messages" >&2
            echo "" >&2
            echo -e "${YELLOW}⚠️  NOTE: Using 'git push --no-verify' is FORBIDDEN${NC}" >&2
            echo "See docs/agents/commitRules.md for the policy" >&2
            echo "" >&2
            exit 1
        fi
        
        echo -e "${GREEN}✓${NC} All commits follow the rules" >&2
        echo "" >&2
    fi
done <<< "$input"

# Fast checks before allowing a push
echo "🔧 Running code quality checks..." >&2
echo "" >&2

# Note: Using --no-daemon to ensure gradle doesn't leave a daemon running
# Using --max-workers=4 to limit resource usage
./gradlew -q \
  detekt \
  test \
  --no-daemon --max-workers=4

# WHY THESE SPECIFIC CHECKS:
#
# 1. detekt - Static analysis for Kotlin code
#    - Catches potential bugs, code smells, and style violations
#    - Enforces consistent code patterns across the team
#    - Fast to run, high signal-to-noise ratio
#
# 2. test - All unit tests
#    - Verifies functionality isn't broken
#    - Ensures new changes don't break existing features
#    - Unit tests are fast (run in seconds)
#
# WHAT'S NOT INCLUDED (intentionally):
# - Instrumented tests (too slow, better suited for CI)
# - Full builds (checked by CI, too time-consuming for every push)
# - Coverage reports (generated in CI, not needed locally every time)
#
# PERFORMANCE TUNING:
# --no-daemon    : Prevents gradle daemon from lingering after checks
# --max-workers=4: Limits parallel tasks to avoid overwhelming your machine
# -q             : Quiet mode, only shows warnings/errors for faster output

echo "" >&2
echo -e "${GREEN}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}" >&2
echo -e "${GREEN}✅ All pre-push checks passed\!${NC}" >&2
echo -e "${GREEN}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}" >&2
echo "" >&2
