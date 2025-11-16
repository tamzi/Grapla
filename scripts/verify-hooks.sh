#!/bin/bash
# Script to verify git hooks are properly installed
# Run this to check if your hooks are active

set -e

RED='\033[0;31m'
YELLOW='\033[1;33m'
GREEN='\033[0;32m'
NC='\033[0m' # No Color

echo "🔍 Verifying Git Hooks Installation..."
echo ""

ISSUES=0

# Check if pre-commit hook exists and is executable
if [ -f ".git/hooks/pre-commit" ] && [ -x ".git/hooks/pre-commit" ]; then
    echo -e "${GREEN}✓${NC} pre-commit hook is installed and executable"
else
    echo -e "${RED}❌${NC} pre-commit hook is missing or not executable"
    ISSUES=$((ISSUES + 1))
fi

# Check if pre-push hook exists and is executable
if [ -f ".git/hooks/pre-push" ] && [ -x ".git/hooks/pre-push" ]; then
    echo -e "${GREEN}✓${NC} pre-push hook is installed and executable"
else
    echo -e "${RED}❌${NC} pre-push hook is missing or not executable"
    ISSUES=$((ISSUES + 1))
fi

# Check if commit-msg hook exists and is executable
if [ -f ".git/hooks/commit-msg" ] && [ -x ".git/hooks/commit-msg" ]; then
    echo -e "${GREEN}✓${NC} commit-msg hook is installed and executable"
else
    echo -e "${RED}❌${NC} commit-msg hook is missing or not executable"
    ISSUES=$((ISSUES + 1))
fi

# Check if the validation scripts exist and are executable
if [ -f "scripts/pre-commit-hook.sh" ] && [ -x "scripts/pre-commit-hook.sh" ]; then
    echo -e "${GREEN}✓${NC} pre-commit validation script exists and is executable"
else
    echo -e "${RED}❌${NC} scripts/pre-commit-hook.sh is missing or not executable"
    ISSUES=$((ISSUES + 1))
fi

if [ -f "scripts/pre-push.sh" ] && [ -x "scripts/pre-push.sh" ]; then
    echo -e "${GREEN}✓${NC} pre-push validation script exists and is executable"
else
    echo -e "${RED}❌${NC} scripts/pre-push.sh is missing or not executable"
    ISSUES=$((ISSUES + 1))
fi

echo ""

# Check if core.hooksPath is set (which could redirect hooks)
HOOKS_PATH=$(git config --get core.hooksPath || echo "")
if [ -n "$HOOKS_PATH" ]; then
    echo -e "${YELLOW}⚠️${NC}  Custom hooks path detected: $HOOKS_PATH"
    echo "   This might prevent hooks from running from .git/hooks/"
    ISSUES=$((ISSUES + 1))
else
    echo -e "${GREEN}✓${NC} No custom hooks path configured"
fi

echo ""

if [ "$ISSUES" -gt 0 ]; then
    echo -e "${RED}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
    echo -e "${RED}❌ Issues found with git hooks setup${NC}"
    echo -e "${RED}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
    echo ""
    echo "To fix, run:"
    echo "  ./scripts/install-git-hooks.sh"
    echo ""
    exit 1
else
    echo -e "${GREEN}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
    echo -e "${GREEN}✅ All git hooks are properly installed${NC}"
    echo -e "${GREEN}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
    echo ""
    echo "Your commits will be validated before committing and pushing."
    echo ""
    echo -e "${YELLOW}⚠️  Note: Hooks can still be bypassed with --no-verify${NC}"
    echo "   Using --no-verify is strongly discouraged and violations"
    echo "   may still be caught by server-side hooks or CI."
    echo ""
fi

exit 0
