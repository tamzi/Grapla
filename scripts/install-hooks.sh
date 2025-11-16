#!/bin/bash
# Install git hooks for commit validation

echo "Installing git hooks..."

# Make the pre-commit hook executable
chmod +x scripts/pre-commit-hook.sh

# Copy to .git/hooks/
cp scripts/pre-commit-hook.sh .git/hooks/pre-commit
chmod +x .git/hooks/pre-commit

echo "✅ Pre-commit hook installed successfully!"
echo ""
echo "The hook will now:"
echo "  - ❌ BLOCK module READMEs > 100 lines"
echo "  - ❌ BLOCK multiple documentation files per commit"
echo "  - ❌ BLOCK mixing code and documentation changes"
echo "  - ❌ BLOCK commits with > 5 files"
echo "  - ❌ BLOCK Kotlin/Java code examples in .md files (except README.md)"
echo "  - ⚠️  WARN about commits with 4-5 files"
echo "  - ✅ ENFORCE camelCase naming for documentation files"
echo ""
echo "To bypass the hook (not recommended):"
echo "  git commit --no-verify"
