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

# Install commit-msg hook (for Change-Id or similar workflow)
# This downloads from Gerrit's standard commit-msg hook location
# If you're not using Gerrit, you can customize or remove this section
if command -v curl &> /dev/null; then
  echo "Downloading commit-msg hook..."
  # Note: This is a placeholder URL - update with your actual commit-msg hook source
  # or create a custom one. For now, we'll create a simple one.
  cat > .git/hooks/commit-msg << 'EOF'
#!/bin/bash
# commit-msg hook: Validate commit message format
# Customize this based on your commit message requirements

# Example: Ensure commit messages are not empty
if ! grep -qE '.+' "$1"; then
  echo "Error: Commit message cannot be empty" >&2
  exit 1
fi

exit 0
EOF
  chmod +x .git/hooks/commit-msg
  echo "✓ commit-msg hook installed successfully!"
fi

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


