# Scripts

Utility scripts for the Grapla project.

## Git Hooks

### Installation

```bash
./scripts/install-git-hooks.sh
```

This installs the pre-commit and pre-push hooks that enforce commit rules from
`docs/agents/commitRules.md` and run code quality checks.

### Pre-Commit Hook

The pre-commit hook (`pre-commit-hook.sh`) runs before every commit and checks:

#### ❌ Blocking Violations (Commit will be rejected)

1. **Module README size** - Module READMEs must be < 100 lines
2. **File naming** - Documentation files must use camelCase (except README.md)
3. **Commit size** - Maximum 8 files per commit
4. **Multiple documentation files** - Each doc file should have its own commit
5. **Mixed concerns** - Code and documentation changes together

#### Warnings (Commit allowed but discouraged)

1. **Large commits** - More than 5 files (ideal: 1-3)

### Pre-Push Hook

The pre-push hook (`pre-push`) automatically runs before every push and checks:

#### Phase 1: Commit History Validation

Validates all commits being pushed follow the rules from `docs/agents/commitRules.md`:

**Blocking Violations (Prevents Push):**

1. **Multiple documentation files per commit**
    - Checks each commit in the push
    - Rule: One `.md` file per commit
    - Fix: Use `git rebase -i` to split commits

2. **Mixed code and documentation**
    - Checks each commit in the push
    - Rule: Code and docs must be separate
    - Fix: Use `git rebase -i` to separate commits

3. **More than 5 files per commit**
    - Checks each commit in the push
    - Rule: Keep commits focused
    - Fix: Use `git rebase -i` to split commits

4. **Module README size > 100 lines**
    - Checks any module READMEs changed
    - Fix: Reduce README size and rebase

**Warnings (Allows Push):**

1. **4-5 files per commit**
    - Suggests splitting if unrelated

#### Phase 2: Code Quality Checks

After validating commit history, runs:

1. **`./gradlew detekt`** - Static analysis (5-10s)
2. **`./gradlew test`** - Unit tests (10-20s)

**Total time:** ~15-30 seconds. Fast validation first (<1s), then expensive quality checks only if
commits are valid.

### Bypassing the Hooks

**Not recommended**, but in rare cases:

```bash
git commit --no-verify
git push --no-verify
```

**Note:** Using `--no-verify` is strongly discouraged. See `docs/agents/commitRules.md` for the
policy.

## Creating New Scripts

When adding new scripts:

1. Make them executable: `chmod +x scripts/your-script.sh`
2. Use `#!/bin/bash` shebang
3. Add usage documentation here
4. Follow shell script best practices
