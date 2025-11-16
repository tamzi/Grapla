# Scripts

Utility scripts for the Grapla project.

## Git Hooks

### Quick Start

**🎉 Automatic Installation**

Git hooks are **automatically installed** during your first Gradle sync!

Just clone the repo and build - hooks are set up automatically.

**Manual Installation (if needed):**

```bash
./scripts/install-git-hooks.sh
```

**Verify hooks are installed:**

```bash
./scripts/verify-hooks.sh
```

### Pre-Commit Hook

Runs before every commit and checks:

**Blocking Violations:**

1. Module README size must be < 100 lines
2. Documentation files must use camelCase (except README.md)
3. Maximum 8 files per commit
4. One documentation file per commit
5. No mixed code and documentation changes

**Warnings:**

1. Large commits with more than 5 files

### Pre-Push Hook

Runs before every push and validates:

**Phase 1: Commit History Validation**

- Multiple documentation files per commit
- Mixed code and documentation
- More than 5 files per commit
- Module README size > 100 lines

**Phase 2: Code Quality Checks**

- `./gradlew detekt` - Static analysis
- `./gradlew test` - Unit tests

Total time: ~15-30 seconds.

### Bypassing Hooks

**Not recommended**, but in rare cases:

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
| `pre-push.sh`             | Validates commit history and runs tests |
| `pre-receive.sh`          | Server-side validation (admin only)     |
| `setup-git-aliases.sh`    | Sets up convenient git aliases          |

### Troubleshooting

**Hooks not running?**

1. Check installation: `./scripts/verify-hooks.sh`
2. Reinstall: `./scripts/install-git-hooks.sh`
3. Check hooks path: `git config --get core.hooksPath`

**Team members bypassing hooks?**
See `docs/gitHookEnforcement.md` for enforcement strategies.
