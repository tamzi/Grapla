# Documentation Rules

> **For AI Agents:** Documentation organization and structure guidelines for Grapla

## Critical Rules

### ⚠️ Rule 0: Be Crisp and Specific

Documentation must be concise. Avoid repetition and "AI slop."

**Bad:** "As we can see from the verification below, the module README is compliant..."  
**Good:** "Module README: 59 lines (limit: 100) ✅"

### ⚠️ Rule 1: Location

ALL documentation in `docs/` except small module READMEs.

*Why: Centralizes docs in one place for easy discovery and maintenance.*

### ⚠️ Rule 2: Module README Size

Module READMEs MUST be < 100 lines.

*Why: Keeps module READMEs as quick-reference only, forcing detailed content into proper docs.*

### ⚠️ Rule 3: Detailed Docs Location

Detailed module docs go in `docs/architecture/{moduleName}/`

*Why: Separates detailed documentation from code, maintains clean module structure.*

### ⚠️ Rule 4: File Naming

Use camelCase for all files except `README.md` (capital letters).

*Why: Consistent naming convention across all docs (README.md is the only exception).*

**Wrong:** `IMPLEMENTATION.md`, `TESTING_GUIDE.md`  
**Correct:** `implementation.md`, `testingGuide.md`

### ⚠️ Rule 5: Index Updates

Update `docs/README.md` when adding new documentation.

*Why: Maintains discoverability - all docs must be indexed in the main documentation index.*

### Rule 6: No "Last Updated" Dates or Maintainers

Do not include "Last Updated" dates or references to maintainers/maintained by in documentation.

*Why: Automatically updated dates can be misleading and are not necessary for documentation, and
maintainer information is not relevant to documentation content.*

### ⚠️ Rule 7: Prefer KDoc Over Code Examples in Documentation

Do NOT put Kotlin/Java code examples in `.md` files. Code documentation belongs in KDoc comments
within actual code files.

*Why: Code examples in markdown files become outdated, aren't type-checked, and duplicate
documentation. KDoc keeps code documentation close to implementation, making it easier to maintain
and accessible via IDE.*

**❌ FORBIDDEN in .md files:**

- Class/interface/function definitions and examples
- API usage examples with Kotlin/Java code
- Data class definitions
- Sealed interface hierarchies
- Implementation examples
- Mapper functions
- Repository patterns with code

**✅ ALLOWED in .md files:**

- Shell commands (`./gradlew build`, `git commit`, etc.)
- Configuration snippets (TOML, JSON, XML)
- Architecture diagrams (ASCII or mermaid)
- High-level concepts and explanations
- Directory structure examples
- Build configuration examples
- File path references

**Use KDoc for:**

- Class/interface/function purposes and usage
- Parameter descriptions
- Return value documentation
- Code examples and usage snippets
- Exceptions and edge cases

**Use documentation files for:**

- Module architecture and design decisions (conceptual, not code)
- Integration guides and tutorials (steps, not code examples)
- Cross-cutting concerns
- Migration guides (process, not code samples)
- High-level overviews
- Commands and build instructions

---

# Implementation Guidelines

The following sections provide detailed guidance on how to apply the rules above.

## Documentation Locations
docs should be stored in: 

| Document Type  | Location                      | Size Limit  | Purpose             |
|----------------|-------------------------------|-------------|---------------------|
| Module README  | `{module}/README.md`          | < 100 lines | Quick overview      |
| Docs Index     | `docs/{category}/README.md`   | < 150 lines | Category navigation |
| Architecture   | `docs/architecture/{module}/` | Unlimited   | Detailed docs       |
| Build          | `docs/buildLogic/`            | Unlimited   | Build configuration |
| Agents         | `docs/agents/`                | Unlimited   | AI guidelines       |
| Modularization | `docs/modularization/`        | Unlimited   | Planning/tracking   |

---

## Module README Structure

Module READMEs are quick-reference documents. They provide just enough info to understand and use
the module, with links to detailed docs.

### Required Content (< 100 lines)

- One-paragraph purpose (2-3 sentences)
- Key components list (5-10 items)
- Quick start code (< 20 lines)
- Dependencies list (3-10 items)
- Links to detailed docs

### Excluded Content

Move these to `docs/architecture/{moduleName}/` instead:

- Detailed implementation
- Architecture diagrams
- Complete testing guides
- Performance details
- Migration guides

**Note:** API documentation belongs in KDoc comments, not in separate markdown files (Rule 7).

### Example

```markdown
# Feature: Stories

> News articles in full-screen story format

## Purpose

Immersive reading experience with vertical, full-screen format and gesture navigation.

## Key Components

- `StoriesHeadlinesScreen` - Main UI
- `StoriesHeadlinesViewModel` - State management
- `StoriesConfig` - Configuration

## Architecture

See [docs/architecture/stories/](../../docs/architecture/stories/) for details.

## Quick Start

```kotlin
navController.navigate(StoriesRoute)
```

## Dependencies

- `core:model` - Data models
- `core:ui` - UI components
- `core:data` - Data layer

---

```

**Line count: ~40 lines** ✅

---

## When Module README > 100 Lines

Create `docs/architecture/{moduleName}/` with:

```

docs/architecture/{moduleName}/
├── README.md # Architecture overview
├── implementation.md # Detailed implementation
├── testing.md # Testing strategy
└── decisions.md # ADRs (if needed)

```

**Note:** API documentation should be in KDoc comments in code, not in separate files (Rule 7).

Update module README to link to these files.

---

## Common Mistakes

### 1. Documentation in Module Directory

**❌ Wrong:**
```
feature/stories/
├── docs/
│ └── architecture.md
└── README.md
```

**✅ Correct:**
```
feature/stories/
└── README.md (< 100 lines)

docs/architecture/stories/
└── README.md
```

### 2. Oversized Module README

**❌ Wrong:** `feature/stories/README.md` (350 lines)  
**✅ Correct:** `feature/stories/README.md` (75 lines) + links to `docs/architecture/stories/`

### 3. Wrong File Naming

**❌ Wrong:** `IMPLEMENTATION.md`, `INDEX.md`, `STATUS.md`  
**✅ Correct:** `implementation.md`, `index.md`, `status.md`

Exception: `README.md` must use capital letters.

### 4. Multiple Doc Files in Module

**❌ Wrong:**
```
feature/stories/
├── README.md
├── ARCHITECTURE.md
└── TESTING.md
```

**✅ Correct:**
```
feature/stories/
└── README.md

docs/architecture/stories/
├── README.md
└── testing.md

```

**Note:** API docs should be KDoc in code, not `apiReference.md` (Rule 7).

---

## Enforcement Checklist

Before committing documentation:

- [ ] All docs in `docs/` (except module READMEs)
- [ ] Module READMEs < 100 lines
- [ ] Files use camelCase (except README.md)
- [ ] `docs/README.md` index updated
- [ ] Cross-references correct
- [ ] No architecture diagrams in module READMEs
- [ ] Detailed docs in `docs/architecture/{module}/`
- [ ] No "Last Updated" dates in documentation
- [ ] No references to maintainers/maintained by in documentation
- [ ] No Kotlin/Java code examples in .md files (except README.md)
- [ ] API documentation in KDoc (not separate files)

---

## Summary

**The Rules:**

**Core Rules (0-7):**
0. ✅ Be crisp and specific. Always re-check for duplication. (no AI slop)
1. ✅ All docs in `docs/` (except module READMEs)
2. ✅ Module READMEs < 100 lines
3. ✅ Detailed docs in `docs/architecture/{moduleName}/`
4. ✅ camelCase naming (except README.md)
5. ✅ Update `docs/README.md` index
6. ✅ No "Last Updated" dates or maintainers in documentation
7. ✅ No Kotlin/Java code examples in .md files (use KDoc instead)

**Enforcement (what NOT to do):**
- ❌ NO docs in module dirs (except README.md)
- ❌ NO oversized module READMEs
- ❌ NO multiple doc files in module

---

**For detailed migration guide, see:** [documentationMigrationGuide.md](./documentationMigrationGuide.md)

---


**See also:**
- [Coding Standards](./codingStandards.md)
- [Commit Rules](./commitRules.md)
- [Testing Rules](./testingRules.md)
