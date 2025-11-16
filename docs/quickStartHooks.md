# Git Hooks Quick Start Guide

## TL;DR

🎉 **Hooks are automatically installed!** Just build the project.

**Verify installation:**

```bash
./scripts/verify-hooks.sh
```

**Manual install (if needed):**

```bash
./scripts/install-git-hooks.sh
```

That's it! Your commits and pushes will now be automatically validated.

## What Do These Hooks Do?

### Pre-Commit Hook

Runs before you commit. Checks:

- ✅ Build succeeds
- ✅ Tests pass
- ✅ Code quality (detekt, lint)
- ✅ Commit follows size and structure rules
- ✅ File naming conventions

**Time:** ~30-60 seconds (only runs when committing)

### Pre-Push Hook

Runs before you push. Validates:

- ✅ All commits in push follow the rules
- ✅ No mixed code/documentation commits
- ✅ Detekt passes
- ✅ All tests pass

**Time:** ~15-30 seconds (only runs when pushing)

### Commit-Msg Hook

Validates commit message format.

**Time:** < 1 second

## Common Questions

### Q: The hooks are too slow!

The hooks run build and tests to ensure you don't commit broken code. This saves time by:

- Catching issues before they enter git history
- Preventing failed CI builds
- Reducing "fix tests" commits

If speed is critical, commit less frequently but push more often (pre-push is faster than
pre-commit).

### Q: Can I bypass the hooks?

Technically yes, using `--no-verify`:

```bash
git commit --no-verify
git push --no-verify
```

**But you shouldn't!** Here's why:

- Violations will be caught by CI anyway (slower feedback)
- You'll have to fix them eventually (harder after pushing)
- It creates technical debt
- It wastes CI resources

**Use `--no-verify` only for genuine emergencies.**

### Q: The hook blocked my commit. Now what?

**Read the error message carefully.** It tells you exactly what's wrong and how to fix it.

Common issues:

1. **"Too many files in commit"**
   ```bash
   # Split your commit into smaller ones
   git reset HEAD~1
   git add file1.kt file2.kt
   git commit -m "feat: add feature part 1"
   git add file3.kt file4.kt
   git commit -m "feat: add feature part 2"
   ```

2. **"Mixing code and documentation"**
   ```bash
   # Commit code and docs separately
   git reset HEAD~1
   git add *.kt
   git commit -m "feat: implement feature"
   git add *.md
   git commit -m "docs: document feature"
   ```

3. **"Build failed" or "Tests failed"**
   ```bash
   # Fix the issue first, then commit
   ./gradlew build test
   # Fix issues...
   git commit
   ```

### Q: I need to rebase but hooks keep running!

During interactive rebase, hooks run for each commit being edited. To temporarily disable:

```bash
# Disable hooks temporarily
git config core.hooksPath /dev/null

# Do your rebase
git rebase -i origin/main

# Re-enable hooks
git config --unset core.hooksPath
```

**Remember to re-enable afterward!**

### Q: Hooks aren't running at all!

Check if they're installed:

```bash
./scripts/verify-hooks.sh
```

If not installed:

```bash
./scripts/install-git-hooks.sh
```

### Q: I'm getting a "permission denied" error

Scripts need to be executable:

```bash
chmod +x scripts/*.sh
./scripts/install-git-hooks.sh
```

### Q: Do I need to reinstall hooks after pulling changes?

Only if the hook scripts themselves changed. To be safe, run:

```bash
./scripts/install-git-hooks.sh
```

It's safe to run multiple times.

## For CI/CD and Advanced Enforcement

The hooks can be bypassed locally, but violations will be caught by:

1. **CI checks** - Run automatically on every push
2. **Server-side hooks** - Cannot be bypassed (requires server setup)
3. **Code review** - Human reviewers check for violations

See `docs/gitHookEnforcement.md` for the complete enforcement strategy.

## Need Help?

- **Full documentation:** `scripts/README.md`
- **Enforcement details:** `docs/gitHookEnforcement.md`
- **Troubleshooting:** `./scripts/verify-hooks.sh`

## Script Reference

| Command | Purpose |
|---------|---------|
| `./scripts/install-git-hooks.sh` | Install all hooks |
| `./scripts/verify-hooks.sh` | Check if hooks are installed |
| `./scripts/check-hooks-on-build.sh` | Check during build (automatic) |
| `git commit` | Runs pre-commit hook automatically |
| `git push` | Runs pre-push hook automatically |
| `git commit --no-verify` | Skip pre-commit (not recommended) |
| `git push --no-verify` | Skip pre-push (not recommended) |

---

**Remember:** Hooks are here to help you catch issues early. Work with them, not against them! 🚀
