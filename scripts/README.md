# Scripts

Utility scripts for the Grapla project.

## Git Hooks

### Quick Start

**🎉 Automatic Installation**

Git hooks are **automatically installed** during your first Gradle sync!

**Manual Installation (if needed):**

```bash
./scripts/install-git-hooks.sh
```

**Verify hooks are installed:**

```bash
./scripts/verify-hooks.sh
```

### Pre-Commit Hook

Runs before every commit. 

### Commit-Msg Hook

Validates commit messages.

### Pre-Push Hook

**Phase 1:** Validates commit history
**Phase 2:** Runs `./gradlew detekt` and `./gradlew test`.

### Bypassing Hooks
**Not recommended**
See `docs/agentRules/commitRules.md` for the policy on bypassing hooks.

### Available Scripts

| Script                    | Purpose                                 |
|---------------------------|-----------------------------------------|
| `auto-install-hooks.sh`   | Auto-installs hooks if missing          |
| `install-git-hooks.sh`    | Installs all git hooks                  |
| `verify-hooks.sh`         | Verifies hooks are properly installed   |
| `check-hooks-on-build.sh` | Checks hooks during build               |
| `pre-commit-hook.sh`      | Validates staged changes                |
| `commit-msg-hook.sh`      | Validates commit message format         |
| `pre-push.sh`             | Validates commit history and runs tests |
| `pre-receive.sh`          | Server-side validation (admin only)     |
| `setup-git-aliases.sh`    | Sets up convenient git aliases          |

### Troubleshooting

**Hooks not running?**

1. Check: `./scripts/verify-hooks.sh`
2. Reinstall: `./scripts/install-git-hooks.sh`
3. Check path: `git config --get core.hooksPath`

**Permission denied errors when committing?**

If you see errors like `.git/hooks/pre-commit: line 5: ./scripts/pre-commit-hook.sh: Permission denied`, the script files need execute permissions.

**How to fix:**

1. Navigate to your project root:
   ```bash
   cd /path/to/Grapla
   ```

2. Make all scripts executable:
   ```bash
   chmod +x scripts/*.sh
   ```

3. Verify the fix worked:
   ```bash
   ls -la scripts/*.sh
   ```
   You should see `-rwxr-xr-x` (the `x` means executable)

4. Try committing again:
   ```bash
   git commit -m "your message"
   ```

**Why this happens:**
- Scripts were cloned without preserving permissions
- Scripts were copied/created manually
- File permissions were reset by your system
- Git doesn't always preserve executable bits across all platforms

**Team bypassing hooks?** See `docs/gitHookEnforcement.md`.
