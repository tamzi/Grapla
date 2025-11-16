#!/bin/bash
# Automatically install git hooks if not already installed
# This script is called by Gradle during sync to ensure hooks are set up
# It's non-intrusive - only installs if hooks are missing

set -e

# Silent check - only act if hooks are not installed
if [ ! -f ".git/hooks/pre-commit" ] || [ ! -x ".git/hooks/pre-commit" ] || \
   [ ! -f ".git/hooks/pre-push" ] || [ ! -x ".git/hooks/pre-push" ]; then
    
    echo ""
    echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
    echo "🎣 Installing Git Hooks (First Time Setup)"
    echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
    echo ""
    echo "Git hooks enforce code quality and commit rules."
    echo "This is a one-time setup that takes a few seconds..."
    echo ""
    
    # Run the installation script
    if [ -f "scripts/install-git-hooks.sh" ]; then
        ./scripts/install-git-hooks.sh
        echo ""
        echo "✅ Hooks installed successfully!"
        echo ""
        echo "To verify: ./scripts/verify-hooks.sh"
        echo ""
    else
        echo "❌ Error: scripts/install-git-hooks.sh not found"
        echo "   Please run manually from project root."
        echo ""
    fi
else
    # Hooks already installed - silent success
    exit 0
fi

exit 0
