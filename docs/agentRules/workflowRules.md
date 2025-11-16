# Workflow Rules

> **For AI Agents:** Work planning, execution, review, and documentation standards

## Core Workflow Principles

### 1. Discuss Before Implementation

**When user presents a question or suggestion, engage in discussion first:**

- Ask clarifying questions about requirements
- Discuss potential approaches and tradeoffs
- Confirm understanding before coding
- Suggest alternatives if applicable
- Get user buy-in on approach

**Do NOT start coding immediately when:**

- Requirements are unclear or ambiguous
- Multiple approaches are possible
- User asks a question about feasibility
- User suggests an idea or feature
- Significant architectural decisions are needed

**Example discussion:**

```
User: "I want to add a search feature"

Agent: "I understand you'd like to add search. Before implementing, 
let me clarify a few things:

1. What should be searchable? (articles, titles only, content, etc.)
2. Should search be local or server-side?
3. Do you want live search as user types, or search on submit?
4. Any specific UI requirements?

Based on your answers, I can propose the best approach."
```

### 2. Plan Before Execution

**After discussion, plan work before starting implementation:**

- Analyze requirements and scope
- Identify affected modules and files
- List necessary changes in order
- Consider dependencies and side effects
- Verify approach aligns with architecture

### 3. Check and Review Work

**Always review your work before completing:**

- Verify all changes were applied correctly
- Check for unintended side effects
- Confirm no code duplication exists
- Validate imports and dependencies
- Test mentally for correctness

### 3. Provide Implementation Summary

**Always summarize what was implemented:**

- List all files changed/created
- Describe key changes made
- Note any decisions or tradeoffs
- Identify any follow-up needed

## Work Planning

### Before Starting Any Task

**Create a mental or written plan:**

```markdown
## Task: [Brief description]

### Analysis
- Requirement: [What needs to be done]
- Affected modules: [List modules]
- Dependencies: [What depends on this]

### Approach
1. [Step 1]
2. [Step 2]
3. [Step 3]

### Files to Modify
- `path/to/file1.kt` - [What changes]
- `path/to/file2.kt` - [What changes]

### Verification
- [ ] Tests pass
- [ ] No lint errors
- [ ] Documentation updated
```

### Planning Checklist

- [ ] **Understand requirements** - Clear on what needs to be done
- [ ] **Identify affected areas** - Know what files/modules to touch
- [ ] **Check dependencies** - Understand what depends on changes
- [ ] **Consider architecture** - Ensure approach fits design
- [ ] **Plan verification** - Know how to validate success

## Work Execution

### During Implementation

**As you work:**

1. **Follow the plan** - Stick to identified changes
2. **Track progress** - Note what's completed
3. **Adapt if needed** - Update plan if requirements change
4. **Stay focused** - One logical change at a time
5. **Document decisions** - Add comments for non-obvious choices

### Execution Best Practices

- Make smallest change that solves problem
- Keep changes focused and atomic
- Add comments for complex logic
- Update tests as you go
- Run checks incrementally

## Work Review

### Self-Review Checklist

**Before marking work complete, verify:**

#### Code Quality

- [ ] All changes are intentional and correct
- [ ] No duplicated code or logic
- [ ] No unused imports or variables
- [ ] Follows coding standards
- [ ] Comments explain "why", not "what"

#### Functionality

- [ ] Logic is correct and handles edge cases
- [ ] Error handling is in place
- [ ] State management is proper
- [ ] Dependencies are injected correctly
- [ ] No hardcoded values

#### Testing

- [ ] Existing tests still pass
- [ ] New tests added for new functionality
- [ ] Edge cases are covered
- [ ] Test coverage meets 70% minimum

#### Documentation

- [ ] Public APIs have KDoc
- [ ] README updated if needed
- [ ] Complex logic is commented
- [ ] No code snippets in markdown files

#### Architecture

- [ ] Changes follow Clean Architecture
- [ ] Module boundaries respected
- [ ] Dependencies flow correctly
- [ ] No circular dependencies

#### Build and Lint

- [ ] Project builds successfully
- [ ] No lint errors
- [ ] No compiler warnings
- [ ] Detekt checks pass

### Review Process

**Step-by-step review:**

1. **Read back changes** - Use file read tools to verify
2. **Check for duplication** - No repeated code
3. **Verify consistency** - Naming, style, patterns
4. **Test mentally** - Would this actually work?
5. **Check side effects** - What else might be affected?

## Implementation Summary

### After Completing Work

**Provide a structured summary:**

```markdown
## Implementation Summary

### Changes Made
- **Module**: [module name]
  - Modified: `path/to/file.kt`
    - Added [feature/function]
    - Updated [component]
  - Created: `path/to/new/file.kt`
    - Purpose: [brief description]

### Key Decisions
- [Decision 1]: Reasoning
- [Decision 2]: Reasoning

### Testing
- Added [X] unit tests
- Updated [Y] existing tests
- Coverage: [percentage]%

### Documentation
- Updated: [list files]
- Added inline documentation to [components]

### Verification
- ✅ All tests pass
- ✅ Lint checks pass
- ✅ Builds successfully
- ✅ No warnings
```

### Summary Requirements

**Every implementation summary must include:**

1. **Files changed** - Complete list with paths
2. **What changed** - Brief description per file
3. **Why changed** - Reasoning for major decisions
4. **Tests updated** - What tests were added/modified
5. **Verification status** - Confirm all checks pass

### Summary Examples

#### Good Summary ✅

```markdown
## Implementation Summary

### Changes Made
- **feature/home**
  - Modified: `HomeViewModel.kt`
    - Added article filtering by category
    - Implemented search functionality with debounce
  - Modified: `HomeScreen.kt`
    - Added search bar UI
    - Added category filter chips

### Key Decisions
- Used 300ms debounce for search to reduce API calls
- Filtered articles locally instead of server-side for better UX

### Testing
- Added 4 unit tests for filtering logic
- Added 2 UI tests for search interaction
- Coverage increased from 72% to 78%

### Documentation
- Added KDoc to new public functions
- Updated feature/home/README.md with search usage

### Verification
- ✅ All 147 tests pass
- ✅ Lint checks pass (0 errors)
- ✅ Builds successfully
- ✅ No compiler warnings
```

#### Poor Summary ❌

```markdown
Added search feature to home screen.
Changed a few files.
Tests pass.
```

## Library Version Policy

### Version Selection Rules

**When adding or updating dependencies:**

1. **Use latest stable versions** - Check official sources
2. **NO alpha versions** - Only stable releases
3. **NO beta versions** - Only stable releases
4. **NO RC versions** - Only stable releases
5. **Verify compatibility** - Check with other dependencies

### Version Catalog Format

**All versions in `gradle/libs.versions.toml`:**

```toml
[versions]
kotlin = "2.0.0"          # ✅ Stable
compose = "1.6.0"         # ✅ Stable
hilt = "2.50"             # ✅ Stable

# ❌ Bad examples (DO NOT USE):
# kotlin = "2.1.0-alpha01"  # NO alpha
# compose = "1.7.0-beta02"  # NO beta
# hilt = "2.51-RC1"         # NO RC
```

### Checking Version Stability

**Before adding/updating a library:**

1. Check official release page
2. Verify it's marked as "stable" or "release"
3. Check for known issues
4. Test in project before committing

### Version Update Process

```bash
# 1. Check current versions
cat gradle/libs.versions.toml

# 2. Research latest stable versions
# - Visit official documentation
# - Check GitHub releases
# - Verify stability

# 3. Update in version catalog
# Edit gradle/libs.versions.toml

# 4. Sync and test
./gradlew clean build
./gradlew test

# 5. Verify no issues
./gradlew detekt
```

## Documentation Standards

### Inline Documentation Priority

**Prefer inline documentation over README snippets:**

```kotlin
// ✅ Good - Documentation in code
/**
 * Fetches articles with optional category filter.
 *
 * Example usage:
 * ```

* val articles = repository.getArticles(category = "tech")
* ```
*
* @param category Optional category filter
* @return Flow of filtered articles
  */
  fun getArticles(category: String? = null): Flow<List<Article>>

```

**In README files:**

```markdown
## Usage

```bash
# Build the project
./gradlew build

# Run tests
./gradlew test
```

See [API Documentation](path/to/docs) for detailed usage examples.

```

### README Content Rules

**README files should contain:**

- ✅ Brief overview
- ✅ Installation/setup commands
- ✅ Build commands
- ✅ Links to detailed docs
- ✅ Quick start with terminal commands

**README files should NOT contain:**

- ❌ Code snippets (put in KDoc instead)
- ❌ API usage examples (put in KDoc instead)
- ❌ Detailed implementation (put in separate docs)
- ❌ Long code blocks (use inline docs)

### Markdown File Guidelines

**Keep README concise:**

```markdown
# Module Name

Brief description.

## Quick Start

```bash
./gradlew :module:build
```

## Documentation

- [Architecture](../../docs/architecture/module/)
- [API Reference](../../docs/architecture/module/api.md)

## Commands

```bash
# Test
./gradlew :module:test

# Lint
./gradlew :module:lint
```

```

**Exception:** Commands and CLI examples are preferred in markdown.

## Workflow Anti-Patterns

### Don't Do These ❌

1. **Start coding without discussion**
   - Engage in discussion first when user asks questions or suggests ideas
   - Clarify requirements before implementing

2. **Start coding without planning**
   - Always plan first, code second

3. **Skip work review**
   - Always review before completing

3. **No implementation summary**
   - Always summarize what was done

4. **Use alpha/beta versions**
   - Only stable library versions

5. **Code snippets in README**
   - Use inline documentation instead

6. **Make changes without verification**
   - Always verify changes work

7. **Skip testing**
   - Always add/update tests

8. **Ignore lint errors**
   - Fix all lint issues before completing

## Complete Workflow Example

### Task: Add article bookmarking feature

#### 1. Planning Phase

```markdown
## Task: Add article bookmarking feature

### Analysis
- Requirement: Users can bookmark articles for later reading
- Affected modules: core:data, core:database, feature:home
- Dependencies: None block this, but affects feature:bookmarks

### Approach
1. Add bookmark field to Article model
2. Add bookmark methods to repository
3. Add bookmark button to UI
4. Add bookmark state to ViewModel
5. Update tests

### Files to Modify
- `core/model/src/.../Article.kt` - Add isBookmarked field
- `core/database/src/.../ArticleEntity.kt` - Add bookmark column
- `core/data/src/.../ArticleRepository.kt` - Add bookmark methods
- `feature/home/src/.../HomeViewModel.kt` - Add bookmark action
- `feature/home/src/.../HomeScreen.kt` - Add bookmark button

### Verification
- [ ] Unit tests for repository methods
- [ ] ViewModel tests for bookmark action
- [ ] UI tests for bookmark button
- [ ] Lint checks pass
```

#### 2. Execution Phase

- Follow planned order of changes
- Add inline documentation as you go
- Write tests alongside implementation
- Run incremental checks

#### 3. Review Phase

```markdown
### Self-Review Checklist
- [x] All changes are intentional
- [x] No duplicated code
- [x] Follows coding standards
- [x] Tests added and passing
- [x] Documentation updated
- [x] Lint checks pass
- [x] Builds successfully
```

#### 4. Summary Phase

```markdown
## Implementation Summary

### Changes Made
- **core/model**
  - Modified: `Article.kt`
    - Added `isBookmarked: Boolean` field
    - Updated documentation

- **core/database**
  - Modified: `ArticleEntity.kt`
    - Added `isBookmarked` column
  - Modified: `ArticleDao.kt`
    - Added `updateBookmarkStatus()` method

- **core/data**
  - Modified: `ArticleRepository.kt`
    - Added `bookmarkArticle(id: String)` method
    - Added `unbookmarkArticle(id: String)` method

- **feature/home**
  - Modified: `HomeViewModel.kt`
    - Added `onBookmarkClick()` action handler
  - Modified: `HomeScreen.kt`
    - Added bookmark icon button to article cards

### Key Decisions
- Bookmark state stored in local database for offline access
- Bookmark toggle uses optimistic update for better UX
- Repository handles sync with server in background

### Testing
- Added 6 unit tests for repository bookmark methods
- Added 4 ViewModel tests for bookmark actions
- Added 2 UI tests for bookmark button interaction
- Coverage: 76% (above 70% target)

### Documentation
- Added KDoc to new repository methods
- Added inline comments for optimistic update logic
- Updated feature/home/README.md

### Verification
- ✅ All 153 tests pass
- ✅ Detekt checks pass (0 issues)
- ✅ Builds successfully
- ✅ No compiler warnings

### Libraries Used
- All existing dependencies (no new libraries added)
```

## Enforcement

### Automated Checks

- Build success: `./gradlew build`
- Test pass: `./gradlew test`
- Lint pass: `./gradlew detekt`

### Manual Verification

- Review implementation summary completeness
- Check for proper planning documentation
- Verify inline documentation over README snippets
- Confirm stable library versions only

## Priority Checklist

Before completing ANY task:

1. [ ] **Discussed approach** - Clarified requirements with user when needed
2. [ ] **Planned work** - Clear plan documented
3. [ ] **Reviewed changes** - Self-review completed
4. [ ] **Tested thoroughly** - Tests added and passing
5. [ ] **Documented inline** - KDoc for public APIs
6. [ ] **Summary provided** - Complete implementation summary
7. [ ] **Stable versions** - No alpha/beta dependencies
8. [ ] **No code in README** - Commands only
9. [ ] **Lint clean** - No errors or warnings
10. [ ] **Builds successfully** - Project compiles
11. [ ] **Architecture aligned** - Follows project patterns

## Related Documentation

- [Coding Standards](codingStandards.md) - Code style and quality
- [Testing Rules](testingRules.md) - Testing requirements
- [Commit Rules](commitRules.md) - Git commit guidelines
- [Documentation Rules](documentationRules.md) - Doc organization
- [Architecture Rules](architectureRules.md) - Architecture patterns
