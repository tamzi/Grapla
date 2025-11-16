#!/bin/bash
# Script to install Git hooks for the Dashiki project
#
# PURPOSE:
# This installer sets up automated quality gates that run at specific points
# in your git workflow to catch issues early and maintain code quality.
#
# WHY WE USE GIT HOOKS:
# 1. Catch issues locally before they reach CI (faster feedback)
# 2. Prevent broken builds from blocking other developers
# 3. Enforce consistent commit message standards
# 4. Reduce code review noise by catching style/quality issues early
# 5. Validate commit history follows project rules
#
# HOOKS INSTALLED:
# - pre-commit:  Enforces commit rules
# - commit-msg:  Validates commit message format
# - pre-push:    Validates commit history and runs detekt/tests
#
# USAGE:
#   ./scripts/install-git-hooks.sh
#
# SAFETY:
# - Idempotent: Safe to run multiple times
# - Non-destructive: Overwrites existing hooks (they're git-ignored anyway)
# - Can be bypassed: Use --no-verify flag in emergencies

set -e

echo "Installing Git hooks..."

# Create hooks directory if it doesn't exist
mkdir -p .git/hooks

# Create pre-commit hook
cat > .git/hooks/pre-commit << 'EOF'
#!/bin/bash
# Pre-commit hook: Enforce commit rules

# Run the validation hook
./scripts/pre-commit-hook.sh

# Exit with the script's exit code
exit $?
EOF

# Make the pre-commit hook executable
chmod +x .git/hooks/pre-commit

# Install commit-msg hook
cat > .git/hooks/commit-msg << 'EOF'
#!/bin/bash
# commit-msg hook: Validate commit message format

# Run the validation hook
./scripts/commit-msg-hook.sh "$1"

# Exit with the script's exit code
exit $?
EOF

chmod +x .git/hooks/commit-msg
echo "✓ commit-msg hook installed successfully!"

# Install pre-push hook
if [ -f "scripts/pre-push.sh" ]; then
  cp scripts/pre-push.sh .git/hooks/pre-push
  chmod +x .git/hooks/pre-push
  echo "✓ Pre-push hook installed successfully!"
else
  echo "⚠ Warning: scripts/pre-push.sh not found. Skipping pre-push hook installation."
fi

# Install pre-receive hook (for server-side validation)
# Note: This is typically installed on the git server, not locally
if [ -f "scripts/pre-receive.sh" ]; then
  echo "ℹ️  Note: pre-receive hook found at scripts/pre-receive.sh"
  echo "   This hook should be installed on your git server, not locally."
fi

echo ""
echo "✅ Git hooks installed successfully!"
echo ""
echo "Installed hooks:"
echo "  - pre-commit: Enforces commit rules"
echo "  - commit-msg: Validates commit message format"
echo "  - pre-push: Validates commit history and runs detekt/tests"
echo ""
echo "To temporarily skip any hook, use: git commit/push --no-verify"


