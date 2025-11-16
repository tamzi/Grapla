# Commit Message Rules

> **For AI Agents:** Git commit message guidelines for Grapla

## 🛑 STOP AND VERIFY

**When user says "commit", you MUST do this FIRST:**

1. **Run `git status` - What actually needs committing?**
2. **For EACH file, run the checklist below**
3. **Fix violations BEFORE staging anything**
4. **Only after ALL checks pass, then commit**

### Quick Checklist (Do this for EVERY file):

**For Documentation Files (.md):**

- [ ] `wc -l <file>` - Module READMEs must be < 100 lines
- [ ] Check filename - Must use camelCase (except README.md)
- [ ] No multiple doc files in one commit
- [ ] Read the file - verify no duplications, hallucinations

**For Code Files:**

- [ ] Read the file - verify what changed
- [ ] Check it compiles mentally
- [ ] Check dependencies are correct

**ONLY commit after all checks pass.**

## 🚫 CRITICAL: NEVER Use `git push --no-verify`

**ABSOLUTE RULE: Using `git push --no-verify` is FORBIDDEN.**

**Why this rule exists:**

The pre-push hook exists to protect code quality and prevent broken code from reaching the
repository. Bypassing it:

- ❌ **Breaks the build** - Pushes code that fails tests or quality checks
- ❌ **Wastes team time** - Forces others to deal with your broken code
- ❌ **Violates commit rules** - Allows improperly structured commits into history
- ❌ **Defeats the purpose** - Makes the entire quality system meaningless
- ❌ **Creates technical debt** - Problems compound and become harder to fix

**What to do instead:**

If the pre-push hook blocks your push, there's a REASON:

1. ✅ **Read the error message** - It tells you exactly what's wrong
2. ✅ **Fix the violations** - Follow the guidance provided
3. ✅ **Use `git rebase -i`** - To split or fix commits if needed
4. ✅ **Run checks manually** - `./gradlew detekt test` to debug issues
5. ✅ **Ask for help** - If you're stuck, ask the team

**The ONLY exception:**

In true emergency situations (production down, critical hotfix needed immediately), you may use
`--no-verify` BUT:

1. You MUST immediately create a follow-up branch to fix the violations
2. You MUST notify the team about what was bypassed and why
3. You MUST ensure the follow-up fixes are merged within 24 hours

**For AI Agents specifically:**

- ❌ **NEVER suggest `git push --no-verify`** to the user
- ❌ **NEVER use `git push --no-verify`** in your commands
- ✅ **ALWAYS fix the root cause** of any pre-push failures
- ✅ **ALWAYS help the user** understand and resolve violations

**Remember:** If you're tempted to use `--no-verify`, it means something is wrong with your commits.
Fix the commits, don't bypass the safety checks.

## Format

```
<Subject: past tense, concise, no period>

Why is this change necessary? (optional, only if not obvious)
```

##  🛑 Critical Rules for AI Agents

### ALWAYS Review Work Before Committing

**MANDATORY PRE-COMMIT CHECKLIST:**

Before creating ANY commit, you MUST:

1. **✅ Read back all changes made**
    - Use `grep_search` or `read_file` to verify what you actually changed
    - Don't trust your memory - verify the actual file contents

2. **✅ Check for repetitions**
    - Look for duplicated code blocks
    - Check for repeated sections in documentation
    - Verify no copy-paste errors

3. **✅ Check for hallucinations**
    - Verify all file paths you reference actually exist
    - Confirm all function/class names match the codebase
    - Double-check version numbers and dates
    - Ensure all links point to real files

4. **✅ Verify consistency**
    - Check that naming follows project conventions (camelCase for docs)
    - Ensure coding style matches existing code
    - Verify imports and dependencies are correct

5. **✅ Test your changes mentally**
    - Would this code compile?
    - Are all referenced files in place?
    - Did you update all necessary references?

6. **✅ ALWAYS Verify documentation compliance** (when committing docs)
    - **REQUIRED:** Run `wc -l <file>` for module READMEs - MUST be < 100 lines
    - **REQUIRED:** Check filename uses camelCase (except README.md)
    - **REQUIRED:** If violations found, FIX THEM before committing
   - See [documentationRules.md](./documentationRules.md) for the full checklist AND MAKE APPROPRIATE CHANGES

**Example Review Process:**

```bash
# After making changes, BEFORE committing:

# 1. Review what you actually changed
git diff --staged

# 2. Read back the modified files
# Use read_file tool to verify contents

# 3. Search for any potential issues
# Use grep_search to find references that need updating

# 4. Only THEN commit
git commit -m "Added feature X"
```

**Why this matters:**

- Prevents broken code from being committed
- Avoids "fixing" commits that clutter history
- Ensures quality and correctness
- Saves time debugging later

### NEVER Push Commits

**AI agents must NEVER run `git push` or any push commands.**

- ✅ **Do:** Create commits with `git commit`
- ✅ **Do:** Stage files with `git add`
- ✅ **Do:** Show commit log with `git log`
- ❌ **NEVER:** Run `git push`
- ❌ **NEVER:** Push to remote repositories
- ❌ **NEVER:** Force push with `git push -f`

**Reason:** Pushing requires authentication and user approval. Always let the user push their own
commits.

**After committing:**

```bash
# ✅ Show what was committed
git log --oneline -5

# ✅ Tell user to push manually
# User will run: git push
```

### ⚠️ ONE Documentation File Per Commit

**CRITICAL RULE:** Each documentation file MUST have its own separate commit. NEVER bundle multiple
documentation files together.

**Before committing any documentation file:**

1. ✅ **Verify it follows [documentationRules.md](./documentationRules.md)**
   - Location, size, naming, content rules
   - See documentationRules.md for full checklist

2. ✅ **One file per commit**
   - Never bundle multiple docs together

**❌ WRONG - Multiple docs in one commit:**

```bash
git add docs/agentRules/commitRules.md docs/agentRules/README.md
git commit -m "Updated agent documentation"  # NEVER DO THIS!
```

**✅ CORRECT - One commit per doc file:**

```bash
# First commit
git add docs/agentRules/commitRules.md
git commit -m "Updated commit rules documentation"

# Second commit
git add docs/agentRules/README.md
git commit -m "Updated agents README"

# Third commit
git add docs/agentRules.md
git commit -m "Updated agents guide"
```

**Why this rule exists:**

- Makes changes easy to track per document
- Allows reverting specific document changes
- Keeps commit history clean and focused
- Each document has its own change history

**Remember:** If you modify 5 documentation files, you create 5 separate commits!

### ⚠️ Split Commits by Layer and Dependency

**CRITICAL RULE:** When implementing a feature, split commits by logical layers following dependency
order. Each layer should be its own commit.

**❌ WRONG - Everything in one commit:**

```bash
# One big commit with 8 files
git add strings.xml TopLevelDestination.kt GraplaApp.kt GraplaAppState.kt \
    MainActivity.kt HomeNavigation.kt StoriesNavigation.kt ProductNavigation.kt
git commit -m "Implemented app navigation with bottom bar"  # TOO BIG!
```

**✅ CORRECT - Split by logical layers:**

```bash
# Commit 1: Resources first (no dependencies)
git add app/src/main/res/values/strings.xml
git commit -m "Added bottom navigation string resources"

# Commit 2: Data structures (depends on resources)
git add app/.../navigation/TopLevelDestination.kt
git commit -m "Created TopLevelDestination enum"

# Commit 3: Feature helpers (depends on data structures)
git add feature/home/navigation/HomeNavigation.kt \
    feature/stories/navigation/StoriesNavigation.kt \
    feature/productcatalog/navigation/ProductCatalogNavigation.kt
git commit -m "Added navigation helper functions to features"

# Commit 4: State management (depends on helpers)
git add app/.../ui/GraplaAppState.kt
git commit -m "Created GraplaAppState for navigation"

# Commit 5: UI components (depends on state)
git add app/.../ui/GraplaApp.kt
git commit -m "Created GraplaApp with bottom navigation"

# Commit 6: Integration (depends on UI)
git add app/.../GraplaAppMainActivity.kt
git commit -m "Updated MainActivity to use GraplaApp"
```

**Why this rule exists:**

- **Easier to review** - Each commit tells one clear story
- **Easier to revert** - Can undo specific parts without breaking everything
- **Follows dependency order** - Lower layers first, higher layers later
- **Better git bisect** - Easier to find which commit introduced an issue
- **Clearer intent** - Each commit has a single, obvious purpose

**Guideline: One commit should ideally change 1-3 files, maximum 5 files**

**Layers to separate (in order):**

1. **Resources** (strings, drawables, etc.) - No dependencies
2. **Data models/enums** - Depends on resources
3. **Utilities/helpers** - Depends on models
4. **State management** - Depends on helpers
5. **UI components** - Depends on state
6. **Integration/wiring** - Depends on UI

**Remember:** If a commit touches 8+ files, it's probably too big! Split it.

## Rules

### 1. Use Past Tense

- **Do:** Added, Fixed, Removed, Updated, Renamed, Implemented, Refactored
- **Don't:** Add, Fix, Remove, Adding, Fixing

### 2. Be Concise

- One line is enough
- Keep it under 50 characters
- If you need multiple paragraphs, the commit is too big

### 3. No Period at the End

- **Do:** `Added user authentication`
- **Don't:** `Added user authentication.`

### 4. One Documentation File Per Commit

- ✅ **Do:** One commit per documentation file
- ❌ **NEVER:** Bundle multiple documentation files in one commit

**Example:**

```bash
# ✅ Correct - Separate commits
git add docs/agentRules/commitRules.md
git commit -m "Updated commit rules documentation"

git add docs/agentRules/README.md
git commit -m "Updated agents README"

# ❌ Wrong - Multiple docs in one commit
git add docs/agentRules/commitRules.md docs/agentRules/README.md
git commit -m "Updated agent documentation"  # NO!
```

**Why:** This makes it easy to track changes to individual documents and revert specific changes.

### 5. Atomic Commits

- One logical change per commit
- If explaining needs paragraphs = commit too big
- Split large changes into smaller, focused commits
- **Rule of thumb:** 1-3 files per commit is ideal, 8+ files is too big
- Follow dependency order when splitting (see "Split Commits by Layer")

**When to split a commit:**

- Changes touch multiple architectural layers (resources, models, UI, integration)
- More than 5-8 files modified
- Multiple unrelated concerns mixed together
- Would be hard to review as one change

**Example of splitting:**

```bash
# ❌ Too big - 8 files in one commit
git commit -m "Implemented navigation"  # strings, models, state, UI, integration

# ✅ Just right - Split into 6 focused commits
git commit -m "Added bottom navigation string resources"     # 1 file
git commit -m "Created TopLevelDestination enum"             # 1 file
git commit -m "Added navigation helper functions to features" # 3 files
git commit -m "Created GraplaAppState for navigation"       # 1 file
git commit -m "Created GraplaApp with bottom navigation"    # 1 file
git commit -m "Updated MainActivity to use GraplaApp"       # 1 file
```

### 6. Details Belong Elsewhere

- Code comments explain **how** and **why**
- Commit messages explain **what** changed
- Don't repeat what's obvious from the diff
- Commits should NOT have Body/Details

### 7. Separate Documentation Commits

- Documentation changes should be in separate commits
- Don't mix code changes with documentation updates

## Examples

### Good Commits ✓

```
Added user authentication
Fixed memory leak in adapter
Removed unused dependencies
Updated navigation to type-safe routes
Renamed Printing to DebugTools
Added centralized build configuration
Fixed deprecated Compose APIs
Created core:model module
Implemented article repository
Configured Hilt dependency injection
Updated Kotlin to 2.2.21
Refactored repository to use Flow
```

### Bad Commits ✗

```
feat(build-logic): add comprehensive utilities documentation
WIP
WIP - still working
Updated files
Fixed stuff
Changes
Added feature X, fixed bug Y, updated docs, refactored Z
```

## Commit Templates by Type

### Feature Addition

```
Added <feature>
Implemented <functionality>
Created <module>
```

### Bug Fix

```
Fixed <issue>
Resolved <problem>
```

### Removal

```
Removed <item>
Deleted <unused code>
```

### Update/Modification

```
Updated <component>
Modified <behavior>
Refactored <code>
```

### Rename

```
Renamed <old> to <new>
```

### Configuration

```
Configured <setting>
Upgraded <dependency> to <version>
```

## Setup Commit Template (Optional)

Save this as `.gitmessage` in project root:

```bash
# <Subject: past tense, concise, no period>

# Why? (optional, only if not obvious)


# ──────────────────────────────────────────────────────────────
# Rules: past tense, concise, no period, <50 chars
# Details belong in code comments, not commits
```

Configure git to use it:

```bash
git config commit.template .gitmessage
```

## Anti-Patterns to Avoid

### ❌ WIP commits

```
WIP
WIP - still working
Almost done
```

### ❌ Generic messages

```
Updated files
Fixed stuff
Changes
```

### ❌ Commit novel syndrome

```
feat(core): comprehensive refactoring of entire data layer

This commit introduces a complete overhaul of...
[50 more lines]
```

### ❌ Mixed concerns

```
Added feature X, fixed bug Y, updated docs, refactored Z
```

## Remember

> **Atomic commits = one logical change**
>
> If explaining needs paragraphs = commit too big
>
> Documentation changes should be separate, not mixed with code
>
> **Each documentation file gets its own commit**

## Commit Workflow

** 🛑 START HERE when user says "commit":**

0. **🛑 GO TO "STOP AND VERIFY" SECTION AT TOP OF THIS FILE**
1. **Run all checks - FIX violations FIRST**
2. **Make atomic changes** - One logical change at a time
3. **Stage related files** - `git add` only files for this change
4. **Write clear message** - Follow the rules above
5. **Review before commit** - `git diff --staged`
6. **Commit** - `git commit`
7. **Verify** - Check the commit with `git show HEAD`

**If you skip step 0-1, you WILL create bad commits.**

## Helpful Git Aliases

To make it easier to follow these rules, run the setup script:

```bash
./scripts/setup-git-aliases.sh
```

This configures helpful aliases:

- `git push-safe` - Standard push (same as `git push`)
- `git check-push` - Run pre-push checks without pushing
- `git fix-commits` - Interactive rebase to fix commit violations
- `git push-emergency` - Emergency bypass (requires typing "YES" to confirm)

**Recommendation:** Use `git check-push` before pushing to catch issues early.

