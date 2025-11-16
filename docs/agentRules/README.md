# AI Agent Rules & Guidelines

> **Purpose:** Centralized rules and guidelines for AI agents working on the Grapla project

## Overview

This directory contains specific rules and guidelines for AI agents (like Claude, GitHub Copilot,
etc.) to follow when contributing to the Grapla codebase. Each file focuses on a specific aspect of
development.

## Structure

```
docs/agentRules/
├── README.md                    # This file - index and overview
├── projectOverview.md           # High-level project information
├── workflowRules.md             # Work planning, review, and summary
├── commitRules.md               # Git commit message guidelines
├── testingRules.md              # Testing standards and practices
├── featureDevelopmentRules.md   # Feature development workflow
├── codingStandards.md           # Code style and conventions
├── documentationRules.md        # Documentation organization and structure
└── architectureRules.md         # Architecture patterns and decisions
```

## Quick Reference

### For General Development

→ Start with [`projectOverview.md`](./projectOverview.md) - Project context, tech stack, SDK
versions

### For Work Planning and Review

→ See [`workflowRules.md`](./workflowRules.md) - **CRITICAL:** Planning, self-review, and
implementation summaries

### For Committing Code

→ See [`commitRules.md`](./commitRules.md) - Commit message format and examples

### For Writing Tests

→ See [`testingRules.md`](./testingRules.md) - Testing frameworks, patterns, and coverage
requirements

### For Building Features

→ See [`featureDevelopmentRules.md`](./featureDevelopmentRules.md) - Feature module structure,
ViewModels, navigation

### For Code Quality

→ See [`codingStandards.md`](./codingStandards.md) - Kotlin style, naming conventions, documentation

### For Architecture Decisions

→ See [`architectureRules.md`](./architectureRules.md) - Module dependencies, Clean Architecture,
patterns

### For Documentation

→ See [`documentationRules.md`](./documentationRules.md) - Documentation organization and structure

## Usage Guidelines

### For AI Agents

## Key Guidelines for AI Agents

1. **Discuss before implementing** - Engage in discussion for questions/suggestions (see
   `workflowRules.md`)
2. **Follow commit rules** - Past tense, concise, no period (see `commitRules.md`)
3. **⚠️ ONE documentation file per commit** - NEVER bundle multiple docs in one commit
4. **Atomic commits** - One logical change per commit
5. **Test before committing** - Run relevant tests
6. **Update documentation** - When changing code behavior
7. **NEVER push** - Always let the user push commits

### For Human Developers

These rules serve as:

- **Onboarding documentation** for new developers
- **Reference guide** during development
- **Code review checklist** for maintainers
- **Convention documentation** for the team

## Rule Categories

### 1. Project Context (projectOverview.md)

- Tech stack and dependencies
- Module structure
- SDK versions
- Build commands
- Repository information

### 2. Workflow Guidelines (workflowRules.md)

- Discussion before implementation (for questions/suggestions)
- Work planning before execution
- Self-review checklist
- Implementation summaries
- Library version policy (no alpha/beta)
- Documentation preferences (inline over README)

### 3. Commit Guidelines (commitRules.md)

- Commit message format
- Examples (good and bad)
- Templates by type
- Anti-patterns to avoid

### 4. Testing Rules (testingRules.md)

- Testing frameworks (JUnit 5, Truth, Turbine)
- Unit test patterns
- Instrumented test patterns
- Coverage requirements (70%+ target)
- Test organization

### 5. Feature Development (featureDevelopmentRules.md)

- Feature module structure
- ViewModel patterns with StateFlow
- Navigation setup (type-safe)
- Hilt dependency injection
- Repository usage
- UI state management

### 6. Coding Standards (codingStandards.md)

- Kotlin style guide
- Naming conventions
- Documentation requirements (KDoc)
- Code organization
- Import ordering
- Comment guidelines

### 7. Documentation Rules (documentationRules.md)

- Documentation organization
- Structure and naming conventions
- Writing style and tone
- Example usage and code snippets

### 8. Architecture Rules (architectureRules.md)

- Clean Architecture layers
- Module dependency rules
- Data flow patterns
- Repository pattern
- Use case pattern
- Module boundaries

## ⚠️ CRITICAL: Pre-Commit Review Required

**Before EVERY commit, you MUST:**

1. ✅ **Read back all changes** - Use tools to verify actual file contents
2. ✅ **Check for repetitions** - No duplicated code or documentation
3. ✅ **Check for hallucinations** - All paths, names, and references are real
4. ✅ **Verify consistency** - Follows all naming and style conventions
5. ✅ **Test mentally** - Would this code actually work?

**See:** [`commitRules.md`](./commitRules.md) for complete pre-commit checklist

## Enforcement

### Automated Checks

- **Lint:** `./gradlew lint` - Catches style violations
- **Detekt:** `./gradlew detekt` - Static code analysis
- **Tests:** `./gradlew test` - Unit test execution
- **Coverage:** `./gradlew coverageAll` - Coverage reporting

### Manual Review

- Code reviews verify adherence to these rules
- Implementation logs track what was built
- Documentation updates reflect new patterns

## Updating Rules

When updating rules:

1. **Make updates in the relevant file** (don't duplicate)
2. **Keep examples current** with actual codebase
3. **Update this README** if adding new rule files
4. **Document the reason** for rule changes
5. **Notify the team** of significant changes

## Related Documentation

- **Architecture:** `docs/architecture.md` - Complete architecture overview
- **Modularization Plan:** `docs/modularizationPlan.md` - Implementation roadmap
- **Build Logic:** `docs/buildLogic/` - Convention plugins and build system
- **Coverage:** `docs/coverage/` - Code coverage setup
- **Implementation Logs:** `docs/implementationLog/` - What has been built

## Priority Order

When rules conflict or you're unsure:

1. **Architecture Rules** - Core structure takes precedence
2. **Testing Rules** - Quality is non-negotiable
3. **Commit Rules** - Clean history is important
4. **Coding Standards** - Readability and consistency
5. **Feature Development** - Implementation details
6. **Documentation Rules** - Clarity and organization

## Notes

- Rules are **prescriptive, not descriptive** - they define how things should be done
- Examples should be **from actual code** when possible
- Keep rules **concise and actionable**
- Focus on **common patterns**, not edge cases
- **Update regularly** as the project evolves

## Documentation Standards

All documentation must follow these standards:

1. **Naming:** Use camelCase for all markdown files (see `codingStandards.md`)
2. **Examples:** Provide code examples where appropriate
3. **Links:** Keep cross-references updated
