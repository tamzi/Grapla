#!/usr/bin/env bash
# Setup git aliases to help prevent --no-verify usage
#
# This script sets up helpful git aliases that make it easier to follow
# the commit rules and discourage bypassing pre-push hooks.

set -euo pipefail

echo "🔧 Setting up git aliases for Dashiki..."
echo ""

# Alias for safe push (default behavior, but makes intent clear)
git config alias.push-safe 'push'

# Alias for emergency push (with warning)
# shellcheck disable=SC2016  # $@ and $confirm should expand when alias runs, not now
git config alias.push-emergency '!f() { echo "⚠️  WARNING: You are about to bypass pre-push checks!"; echo "This is FORBIDDEN except for production emergencies."; echo "See docs/agentRules/commitRules.md for the policy."; echo ""; read -r -p "Are you sure? Type YES to continue: " confirm; if [ "$confirm" = "YES" ]; then git push --no-verify "$@"; else echo "Push cancelled."; fi; }; f'

# Alias for checking if you can push
# shellcheck disable=SC2016  # No variables to expand in this alias
git config alias.check-push '!f() { echo "🔍 Running pre-push checks..."; ./gradlew -q detekt test --no-daemon --max-workers=4 && echo "✅ All checks passed! You can push."; }; f'

# Alias for fixing commits
# shellcheck disable=SC2016  # $@ should expand when alias runs, not now
git config alias.fix-commits '!f() { echo "📝 Interactive rebase to fix commits..."; git rebase -i "$@"; }; f'

echo "✅ Git aliases configured:"
echo ""
echo "  git push-safe          - Standard push (same as 'git push')"
echo "  git push-emergency     - Emergency bypass (with confirmation prompt)"
echo "  git check-push         - Run pre-push checks without pushing"
echo "  git fix-commits        - Interactive rebase to fix commit violations"
echo ""
echo "💡 Recommendation: Always use 'git push' or 'git push-safe'"
echo ""
