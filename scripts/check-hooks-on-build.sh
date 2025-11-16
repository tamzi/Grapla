#!/bin/bash
# Check if git hooks are installed during build
# This provides a warning if hooks are not set up properly

# Silent check - only show message if hooks are not installed
if [ ! -f ".git/hooks/pre-commit" ] || [ ! -x ".git/hooks/pre-commit" ] || \
   [ ! -f ".git/hooks/pre-push" ] || [ ! -x ".git/hooks/pre-push" ]; then
    
    echo ""
    echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
    echo "⚠️  WARNING: Git hooks are not installed!"
    echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
    echo ""
    echo "Git hooks enforce code quality and commit rules before commits/pushes."
    echo "Without them, you may commit code that violates project standards."
    echo ""
    echo "To install hooks, run:"
    echo "  ./scripts/install-git-hooks.sh"
    echo ""
    echo "To verify hooks are installed, run:"
    echo "  ./scripts/verify-hooks.sh"
    echo ""
    echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
    echo ""
fi

exit 0
