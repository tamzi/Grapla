#!/bin/bash
# commit-msg hook: Validate commit message format
#
# This hook enforces the commit message rules defined in docs/agentRules/commitRules.md
#
# Installation: This is automatically installed by scripts/install-git-hooks.sh
# Or manually: cp scripts/commit-msg-hook.sh .git/hooks/commit-msg && chmod +x .git/hooks/commit-msg
#
# Rules enforced:
# 1. Use past tense (Added, Fixed, Removed, etc.)
# 2. Keep subject line concise (ideally < 50 chars, max 72)
# 3. No period at the end of subject line
# 4. No body/details (commits should be atomic and self-explanatory)
# 5. No prefixes like "feat:", "fix:", "docs:" (conventional commits style)
# 6. No WIP or generic messages

set -e

RED='\033[0;31m'
YELLOW='\033[1;33m'
GREEN='\033[0;32m'
NC='\033[0m' # No Color

# Get the commit message file path (passed as first argument)
COMMIT_MSG_FILE=$1

# Read the commit message
COMMIT_MSG=$(cat "$COMMIT_MSG_FILE")

# Get just the subject line (first line)
SUBJECT=$(echo "$COMMIT_MSG" | head -n1)

# Get the rest of the message (body)
BODY=$(echo "$COMMIT_MSG" | tail -n +2 | sed '/^#/d' | sed '/^$/d' | tr -d '\n')

VIOLATIONS=0

echo "" >&2
echo "🔍 Validating commit message format..." >&2
echo "" >&2

# Check 1: Empty message
if [ -z "$SUBJECT" ]; then
    echo -e "${RED}❌ VIOLATION: Commit message cannot be empty${NC}" >&2
    echo "" >&2
    exit 1
fi

# Check 2: No body/details allowed
if [ -n "$BODY" ]; then
    echo -e "${RED}❌ VIOLATION: Commit message has a body/details section${NC}" >&2
    echo -e "   Rule: Commits should be atomic and self-explanatory" >&2
    echo -e "   Details belong in code comments, not commit messages" >&2
    echo "" >&2
    echo -e "   Subject: $SUBJECT" >&2
    echo -e "   Body found: ${BODY:0:100}..." >&2
    echo "" >&2
    echo -e "   See: docs/agentRules/commitRules.md Rule 6" >&2
    VIOLATIONS=$((VIOLATIONS + 1))
fi

# Check 3: No period at end of subject line
if [[ "$SUBJECT" =~ \.$ ]]; then
    echo -e "${RED}❌ VIOLATION: Subject line ends with a period${NC}" >&2
    echo -e "   Subject: $SUBJECT" >&2
    echo -e "   Fix: Remove the period at the end" >&2
    echo "" >&2
    echo -e "   See: docs/agentRules/commitRules.md Rule 3" >&2
    VIOLATIONS=$((VIOLATIONS + 1))
fi

# Check 4: No conventional commit prefixes (feat:, fix:, docs:, etc.)
if [[ "$SUBJECT" =~ ^(feat|fix|docs|style|refactor|test|chore|perf|ci|build|revert)(\(.*\))?:\ .+ ]]; then
    echo -e "${RED}❌ VIOLATION: Subject uses conventional commit format (feat:, fix:, docs:, etc.)${NC}" >&2
    echo -e "   Subject: $SUBJECT" >&2
    echo -e "   Rule: Use simple past tense without prefixes" >&2
    echo "" >&2
    # Suggest a fix by removing the prefix
    SUGGESTED=$(echo "$SUBJECT" | sed -E 's/^(feat|fix|docs|style|refactor|test|chore|perf|ci|build|revert)(\(.*\))?:\ //g')
    # Capitalize first letter if needed
    SUGGESTED="$(tr '[:lower:]' '[:upper:]' <<< "${SUGGESTED:0:1}")${SUGGESTED:1}"
    echo -e "   Suggested fix: $SUGGESTED" >&2
    echo "" >&2
    echo -e "   See: docs/agentRules/commitRules.md Rule 1" >&2
    VIOLATIONS=$((VIOLATIONS + 1))
fi

# Check 5: Must use past tense
# Common non-past-tense patterns to detect:
# - Present tense: "Add", "Fix", "Remove", "Update", "Create", "Implement"
# - Gerund form: "Adding", "Fixing", "Removing", "Updating", "Creating"
if [[ "$SUBJECT" =~ ^(Add|Fix|Remove|Update|Delete|Create|Implement|Refactor|Move|Change|Merge|Revert)\ .+ ]]; then
    echo -e "${RED}❌ VIOLATION: Subject uses present tense instead of past tense${NC}" >&2
    echo -e "   Subject: $SUBJECT" >&2
    echo -e "   Rule: Use past tense (Added, Fixed, Removed, etc.)" >&2
    echo "" >&2
    # Suggest past tense
    SUGGESTED=$(echo "$SUBJECT" | sed -E 's/^Add /Added /')
    SUGGESTED=$(echo "$SUGGESTED" | sed -E 's/^Fix /Fixed /')
    SUGGESTED=$(echo "$SUGGESTED" | sed -E 's/^Remove /Removed /')
    SUGGESTED=$(echo "$SUGGESTED" | sed -E 's/^Update /Updated /')
    SUGGESTED=$(echo "$SUGGESTED" | sed -E 's/^Delete /Deleted /')
    SUGGESTED=$(echo "$SUGGESTED" | sed -E 's/^Create /Created /')
    SUGGESTED=$(echo "$SUGGESTED" | sed -E 's/^Implement /Implemented /')
    SUGGESTED=$(echo "$SUGGESTED" | sed -E 's/^Refactor /Refactored /')
    SUGGESTED=$(echo "$SUGGESTED" | sed -E 's/^Move /Moved /')
    SUGGESTED=$(echo "$SUGGESTED" | sed -E 's/^Change /Changed /')
    SUGGESTED=$(echo "$SUGGESTED" | sed -E 's/^Merge /Merged /')
    SUGGESTED=$(echo "$SUGGESTED" | sed -E 's/^Revert /Reverted /')
    echo -e "   Suggested fix: $SUGGESTED" >&2
    echo "" >&2
    echo -e "   See: docs/agentRules/commitRules.md Rule 1" >&2
    VIOLATIONS=$((VIOLATIONS + 1))
fi

if [[ "$SUBJECT" =~ ^(Adding|Fixing|Removing|Updating|Deleting|Creating|Implementing|Refactoring|Moving|Changing)\ .+ ]]; then
    echo -e "${RED}❌ VIOLATION: Subject uses gerund form (-ing) instead of past tense${NC}" >&2
    echo -e "   Subject: $SUBJECT" >&2
    echo -e "   Rule: Use past tense (Added, Fixed, Removed, etc.)" >&2
    echo "" >&2
    # Suggest past tense
    SUGGESTED=$(echo "$SUBJECT" | sed -E 's/^Adding /Added /')
    SUGGESTED=$(echo "$SUGGESTED" | sed -E 's/^Fixing /Fixed /')
    SUGGESTED=$(echo "$SUGGESTED" | sed -E 's/^Removing /Removed /')
    SUGGESTED=$(echo "$SUGGESTED" | sed -E 's/^Updating /Updated /')
    SUGGESTED=$(echo "$SUGGESTED" | sed -E 's/^Deleting /Deleted /')
    SUGGESTED=$(echo "$SUGGESTED" | sed -E 's/^Creating /Created /')
    SUGGESTED=$(echo "$SUGGESTED" | sed -E 's/^Implementing /Implemented /')
    SUGGESTED=$(echo "$SUGGESTED" | sed -E 's/^Refactoring /Refactored /')
    SUGGESTED=$(echo "$SUGGESTED" | sed -E 's/^Moving /Moved /')
    SUGGESTED=$(echo "$SUGGESTED" | sed -E 's/^Changing /Changed /')
    echo -e "   Suggested fix: $SUGGESTED" >&2
    echo "" >&2
    echo -e "   See: docs/agentRules/commitRules.md Rule 1" >&2
    VIOLATIONS=$((VIOLATIONS + 1))
fi

# Check 6: No WIP or generic messages
if [[ "$SUBJECT" =~ ^(WIP|wip|Work in progress|TODO|FIXME|TBD|temp|temporary).*$ ]] || \
   [[ "$SUBJECT" =~ ^(Updated files|Fixed stuff|Changes|Minor changes|Updates|Fixes)$ ]]; then
    echo -e "${RED}❌ VIOLATION: Generic or WIP commit message${NC}" >&2
    echo -e "   Subject: $SUBJECT" >&2
    echo -e "   Rule: Be specific about what was changed" >&2
    echo "" >&2
    echo -e "   Examples of good messages:" >&2
    echo -e "   - Added user authentication" >&2
    echo -e "   - Fixed memory leak in adapter" >&2
    echo -e "   - Removed unused dependencies" >&2
    echo "" >&2
    echo -e "   See: docs/agentRules/commitRules.md Anti-Patterns" >&2
    VIOLATIONS=$((VIOLATIONS + 1))
fi

# Check 7: Length recommendations
SUBJECT_LENGTH=${#SUBJECT}
if [ "$SUBJECT_LENGTH" -gt 72 ]; then
    echo -e "${RED}❌ VIOLATION: Subject line is too long ($SUBJECT_LENGTH chars, max: 72)${NC}" >&2
    echo -e "   Subject: $SUBJECT" >&2
    echo -e "   Rule: Keep subject concise" >&2
    echo "" >&2
    echo -e "   See: docs/agentRules/commitRules.md Rule 2" >&2
    VIOLATIONS=$((VIOLATIONS + 1))
elif [ "$SUBJECT_LENGTH" -gt 50 ]; then
    echo -e "${YELLOW}⚠️  WARNING: Subject line is longer than ideal ($SUBJECT_LENGTH chars, ideal: < 50)${NC}" >&2
    echo -e "   Subject: $SUBJECT" >&2
    echo -e "   Consider making it more concise" >&2
    echo "" >&2
fi

# If violations found, block the commit
if [ "$VIOLATIONS" -gt 0 ]; then
    echo -e "${RED}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}" >&2
    echo -e "${RED}❌ COMMIT BLOCKED: $VIOLATIONS violation(s) in commit message${NC}" >&2
    echo -e "${RED}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}" >&2
    echo "" >&2
    echo "Please fix the commit message and try again." >&2
    echo "See docs/agentRules/commitRules.md for details." >&2
    echo "" >&2
    echo "To edit your commit message, use:" >&2
    echo "  git commit --amend" >&2
    echo "" >&2
    exit 1
fi

# Success!
echo -e "${GREEN}✓${NC} Commit message format is valid" >&2
echo "" >&2

exit 0
