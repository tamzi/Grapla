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

Runs before every commit. **Blocking violations:** Module README size < 100 lines, camelCase docs (
except README.md), max 5 files per commit, one doc file per commit, no mixed code/docs, no
Kotlin/Java examples in markdown (except README.md). **Warnings:** Large commits (>3 files).

### Commit-Msg Hook

Validates commit messages. **Blocking violations:** No conventional prefixes (`feat:`, `fix:`,
etc.), must use past tense (`Added` not `Add`), no period at end, no body section (atomic commits
only), no WIP/generic messages, subject ≤ 72 chars. **Warnings:** Subject line 50-72 chars.

### Pre-Push Hook

**Phase 1:** Validates commit history (multiple docs per commit, mixed code/docs, >5 files, README
size >100 lines, no Kotlin/Java examples, commit message format).

**Phase 2:** Runs `./gradlew detekt` and `./gradlew test`. Total time: ~15-30 seconds.

### Bypassing Hooks

**Not recommended**, but available:

```bash
git commit --no-verify
git push --no-verify
```

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

**Team bypassing hooks?** See `docs/gitHookEnforcement.md`.
