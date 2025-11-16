# AI Agent Rules & Guidelines for Grapla

> **Purpose:** Centralized rules and guidelines for AI agents working on the Grapla project

---

## ⚠️ CRITICAL: Read the Detailed Rules

**This document is a QUICK REFERENCE ONLY.**

For comprehensive, enforceable rules that you **MUST** follow, see:

📁 **[docs/agentRules/](docs/agentRules/)** - Detailed agent rules directory

**Start here:**

- **[docs/agentRules/README.md](docs/agentRules/README.md)** - Complete index and navigation
- **[docs/agentRules/commitRules.md](docs/agentRules/commitRules.md)** - MANDATORY pre-commit
  checklist
- **[docs/agentRules/documentationRules.md](docs/agentRules/documentationRules.md)** - Documentation
  requirements

**The detailed rules include:**

- ✅ Specific checklists you MUST follow before every commit
- ✅ Architecture patterns with code examples
- ✅ Testing requirements and utilities
- ✅ Common mistakes and how to avoid them
- ✅ Enforcement criteria for all standards

---

## Overview

This document provides essential context, conventions, and guidelines for AI agents (like Claude,
GitHub Copilot, etc.) contributing to the Grapla codebase. Grapla is an Android design system
library built with Jetpack Compose, featuring atomic design principles and modern Android
development practices.

## Project Context

### What is Grapla?

Grapla is a modern Android design system library written in Kotlin and Jetpack Compose. It provides
reusable UI components following atomic design principles (atoms, molecules, organisms) with a
showcase application demonstrating the design system in action.

**Key Characteristics:**

- Multi-module architecture with convention plugins
- Design system library (`gruid`) with atomic design components
- Demo application (`gruiddemo`) showcasing components
- Modern Android development with Compose and Material 3
- Built with Gradle Kotlin DSL and convention plugins

### Tech Stack

| Category | Technology | Notes |
|----------|-----------|-------|
| **Language** | Kotlin | Primary language |
| **UI Framework** | Jetpack Compose | Material 3 components |
| **Build System** | Gradle (Kotlin DSL) | With convention plugins |
| **DI** | Hilt (Dagger) | Dependency injection |
| **Quality** | Detekt, KtLint | Code quality tools |
| **Testing** | JUnit, Roborazzi | Unit and screenshot tests |

### SDK Versions

| Configuration   | Version | Android Version |
|-----------------|---------|-----------------|
| **Min SDK**     | TBD     | Android 12+     |
| **Target SDK**  | 36      | Android 15      |
| **Compile SDK** | 36      | Android 15      |
| **Java**        | 21      | Java 21         |
| **Kotlin JVM**  | 17/21   | Target JVM      |

**Important:** Uses Java 21 - must be configured in Android Studio:

- Settings > Build, Execution, Deployment > Build Tools > Gradle > Gradle JDK > 21

## Module Structure

```
Grapla/
├── app/                    # Main application entry point
├── gruid/                  # Grapla UI Design system library (atomic design)
│   ├── atoms/              # Basic UI elements (buttons, text, icons)
│   ├── molecules/          # Simple component combinations
│   ├── components/         # Reusable components
│   └── theme/              # Theme and styling
├── gruiddemo/              # Design system showcase application
├── buildLogic/             # Convention plugins for build configuration
│   └── convention/         # Convention plugin implementations
└── docs/                   # Documentation
    └── agentRules/         # Detailed AI agent rules
```

### Module Responsibilities

**app:**

- Main application entry point
- Demonstrates integration of gruid design system
- Single Activity architecture with Compose
- Uses Hilt for dependency injection

**gruid (Grapla UI Design):**

- Core design system library
- Atomic design components (atoms, molecules, organisms)
- Theme and styling system
- Exported components for consumption by apps
- Material 3 foundation

**gruiddemo:**

- Component catalog and showcase
- Interactive component explorer
- Design system documentation
- Testing ground for new components

**buildLogic:**

- Convention plugins for consistent build configuration
- Shared build logic across modules
- Gradle configuration reusability

## Architecture Principles

### 1. Atomic Design

Components are organized following atomic design methodology:

- **Atoms:** Basic building blocks (buttons, text fields, icons)
- **Molecules:** Simple combinations of atoms (search bar, card header)
- **Organisms:** Complex components (navigation bar, full cards)
- **Templates:** Page-level layouts (if applicable)

### 2. Compose-First

- All UI built with Jetpack Compose
- No XML layouts
- Composable functions for all components
- Preview annotations for design-time rendering

### 3. Convention Plugins

Build configuration is managed through convention plugins:

- `grapla.android.application` - Application module setup
- `grapla.android.library` - Library module setup
- `grapla.android.library.compose` - Compose library configuration
- `grapla.hilt` - Hilt dependency injection setup
- `grapla.android.lint` - Lint configuration

### 4. Clean Separation

- Design system (`gruid`) is independent and reusable
- Demo app depends on design system, not vice versa
- Theme and styling centralized in gruid
- Clear module boundaries

## Development Guidelines

### 1. Coding Standards

**Kotlin Style:**

- Follow official Kotlin coding conventions
- Use KtLint for formatting (configured in project)
- Write clear, concise code comments
- Prefer immutable data structures

**Naming Conventions:**

- Composables: PascalCase (e.g., `ButtonPrimary`, `CardHeader`)
- Functions: camelCase (e.g., `calculateSpacing`, `formatText`)
- Constants: UPPER_SNAKE_CASE (e.g., `MAX_ITEMS`, `DEFAULT_TIMEOUT`)
- Files: Match primary type name (e.g., `ButtonPrimary.kt`)

**Documentation:**

```kotlin
/**
 * A primary button component following Grapla design system.
 *
 * @param text Button label text
 * @param onClick Callback invoked when button is clicked
 * @param modifier Optional modifier for customization
 * @param enabled Whether button is enabled (default: true)
 */
@Composable
fun ButtonPrimary(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    // Implementation
}
```

### 2. Component Development

**Creating New Components:**

1. **Determine atomic level:** Is it an atom, molecule, or organism?
2. **Place in correct directory:**
   `gruid/src/main/java/com/tamzi/gruid/{atoms|molecules|components}/`
3. **Follow naming pattern:** Descriptive + Component Type (e.g., `ButtonPrimary`, `CardElevated`)
4. **Include previews:** Add `@Preview` annotations for design-time rendering
5. **Document parameters:** Use KDoc for all public composables
6. **Make configurable:** Use `Modifier` parameter for flexibility

**Example Structure:**

```kotlin
// File: gruid/src/main/java/com/tamzi/gruid/atoms/ComponentName.kt

/**
 * Documentation here
 */
@Composable
fun ComponentName(
    // Required parameters first
    modifier: Modifier = Modifier, // Modifier last in parameters
    // Optional parameters with defaults
) {
    // Implementation
}

@Preview(showBackground = true)
@Composable
private fun ComponentNamePreview() {
    GraplaTheme {
        ComponentName()
    }
}
```

### 3. Theme and Styling

**Theme Usage:**

- All components should use `GraplaTheme` for theming
- Access colors via `MaterialTheme.colorScheme`
- Access typography via `MaterialTheme.typography`
- Access spacing/dimensions from theme extensions

**Consistent Styling:**

- Don't hardcode colors - use theme colors
- Don't hardcode dimensions - use theme spacing
- Support both light and dark themes
- Test components in both theme modes

### 4. Testing

**Unit Tests:**

- Place in `src/test/java` directory
- Test component behavior and logic
- Use JUnit for test framework
- Name: `{ComponentName}Test.kt`

**Screenshot Tests:**

- Use Roborazzi for screenshot testing
- Test visual appearance and layout
- Include different states and configurations
- Place in appropriate test directory

**Preview Tests:**

- Every component should have `@Preview` annotations
- Include multiple previews for different states
- Preview both light and dark themes when relevant

### 5. Build Commands

```bash
# Build entire project
./gradlew build

# Build specific module
./gradlew :gruid:build
./gradlew :app:build

# Run tests
./gradlew test
./gradlew :gruid:test

# Run lint checks
./gradlew lint
./gradlew detekt
./gradlew ktlintCheck

# Format code
./gradlew ktlintFormat

# Clean build
./gradlew clean build
```

## Commit Guidelines

### Commit Message Format

Use conventional commits format:

```
<type>(<scope>): <description>

[optional body]

[optional footer]
```

**Types:**

- `feat` - New feature or component
- `fix` - Bug fix
- `refactor` - Code refactoring
- `docs` - Documentation changes
- `style` - Code style changes (formatting, naming)
- `test` - Adding or updating tests
- `chore` - Build process, dependencies, tooling
- `perf` - Performance improvements

**Scopes:**

- `gruid` - Design system library changes
- `app` - Main app changes
- `gruiddemo` - Demo app changes
- `buildLogic` - Build configuration changes
- `docs` - Documentation changes

**Examples:**

```
feat(gruid): add ButtonPrimary component with Material 3 styling

refactor(gruid): reorganize atoms directory structure

fix(app): resolve navigation crash on back press

docs(agents): update component development guidelines

chore(buildLogic): upgrade Gradle to 8.7

test(gruid): add screenshot tests for button components
```

### Commit Best Practices

1. **Atomic commits** - One logical change per commit
2. **Clear descriptions** - Explain what and why, not how
3. **Test before committing** - Ensure tests pass
4. **Lint before committing** - Run ktlintCheck and fix issues
5. **Reference issues** - Link to issues in footer if applicable

## Quality Standards

### Code Quality

- **Detekt:** Static code analysis must pass
- **KtLint:** Code formatting must be consistent
- **No warnings:** Address all compiler warnings
- **No hardcoded values:** Use constants, theme values, or resources

### Performance

- **Compose efficiency:** Avoid unnecessary recomposition
- **Use remember:** Cache computed values appropriately
- **Stable parameters:** Mark data classes with `@Immutable` or `@Stable` when appropriate
- **Lazy composition:** Use `LazyColumn`, `LazyRow` for lists

### Accessibility

- **Content descriptions:** Provide for all interactive elements
- **Semantic properties:** Use appropriate semantics modifiers
- **Touch targets:** Minimum 48dp for interactive elements
- **Color contrast:** Ensure sufficient contrast ratios

## Project-Specific Patterns

### 1. Theme Access Pattern

```kotlin
@Composable
fun MyComponent() {
    // Colors
    val backgroundColor = MaterialTheme.colorScheme.surface
    val contentColor = MaterialTheme.colorScheme.onSurface
    
    // Typography
    val textStyle = MaterialTheme.typography.bodyLarge
    
    // Apply to UI
}
```

### 2. Modifier Parameter Pattern

```kotlin
@Composable
fun MyComponent(
    // Other parameters...
    modifier: Modifier = Modifier, // Always last with default
) {
    // Apply modifier to root composable
    Surface(modifier = modifier) {
        // Content
    }
}
```

### 3. Preview Pattern

```kotlin
@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun MyComponentPreview() {
    GraplaTheme {
        MyComponent()
    }
}
```

### 4. State Management Pattern

```kotlin
@Composable
fun MyComponent(
    // State parameters as function parameters (for hoisting)
    isSelected: Boolean,
    onSelectionChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    // Use passed state
}
```

## Documentation Standards

### 1. Code Documentation

- **Public APIs:** Must have KDoc comments
- **Complex logic:** Inline comments explaining why, not what
- **TODOs:** Include issue reference or explanation

### 2. Component Documentation

Each component should document:

- Purpose and use case
- Parameters and their effects
- Any special considerations
- Example usage (if complex)

### 3. Markdown Documentation

- Use camelCase for markdown files (e.g., `agentRules.md`)
- Include table of contents for long documents
- Use code blocks with language specification
- Keep line length reasonable (80-120 chars)

## Important Files and Directories

```
Grapla/
├── gradle/libs.versions.toml    # Version catalog for dependencies
├── build.gradle.kts              # Root build configuration
├── settings.gradle.kts           # Module and repository configuration
├── buildLogic/                   # Convention plugin source code
├── docs/agentRules/              # Detailed agent rules (reference this!)
│   ├── projectOverview.md        # Detailed project context
│   ├── codingStandards.md        # Comprehensive coding standards
│   ├── commitRules.md            # Git commit guidelines
│   └── ...                       # Other specific rule files
└── scripts/                      # Git hooks and automation scripts
```

## Related Documentation

⚠️ **IMPORTANT:** This document provides a high-level overview. For detailed, comprehensive rules
that **MUST** be followed, see the `docs/agentRules/` directory:

### Essential Reading (READ THESE FIRST!)

- **[README.md](docs/agentRules/README.md)** - Index and quick reference for all agent rules
- **[workflowRules.md](docs/agentRules/workflowRules.md)** - **CRITICAL:** Work planning,
  self-review, implementation summaries, library versions, and documentation preferences
- **[commitRules.md](docs/agentRules/commitRules.md)** - **CRITICAL:** Git commit message
  guidelines, pre-commit checklist, and atomic commit rules
- **[documentationRules.md](docs/agentRules/documentationRules.md)** - **CRITICAL:** Documentation
  location, size limits, and naming conventions

### Architecture & Design

- **[projectOverview.md](docs/agentRules/projectOverview.md)** - Detailed project information, tech
  stack, and SDK versions
- **[architectureRules.md](docs/agentRules/architectureRules.md)** - Clean Architecture layers,
  module dependencies, and data flow patterns

### Development Standards

- **[codingStandards.md](docs/agentRules/codingStandards.md)** - Comprehensive Kotlin code style
  guide, naming conventions, and documentation requirements
- **[testingRules.md](docs/agentRules/testingRules.md)** - Testing frameworks, patterns, coverage
  requirements (70%+), and test utilities
- **[featureDevelopmentRules.md](docs/agentRules/featureDevelopmentRules.md)** - Feature module
  structure, ViewModel patterns, navigation, and state management

### Why These Rules Matter

These detailed rules provide:

1. **Specific enforcement criteria** - Clear do's and don'ts with examples
2. **Pre-commit checklists** - Mandatory steps before creating any commit
3. **Architecture patterns** - Proven patterns for Clean Architecture implementation
4. **Testing strategies** - Comprehensive testing approaches with code examples
5. **Common mistakes** - Anti-patterns to avoid with corrections

**Before making any commit, you MUST:**

- Review the relevant sections in `docs/agentRules/`
- Follow the pre-commit checklist in `commitRules.md`
- Verify documentation compliance per `documentationRules.md`
- Ensure code quality per `codingStandards.md`
- Check test coverage per `testingRules.md`

## Quick Reference Checklist

Before completing any task, verify:

### Work Planning & Review (see workflowRules.md)

- [ ] Discussion before implementation (for questions/suggestions)
- [ ] Work planned before execution
- [ ] Self-review checklist completed
- [ ] Implementation summary provided
- [ ] Only stable library versions (no alpha/beta)
- [ ] Inline documentation preferred over README code snippets

### Code Quality

- [ ] Code follows Kotlin coding conventions
- [ ] KtLint formatting is applied (`./gradlew ktlintFormat`)
- [ ] Detekt checks pass (`./gradlew detekt`)
- [ ] Tests are written and passing (`./gradlew test`)
- [ ] Components have `@Preview` annotations
- [ ] Public APIs have KDoc documentation
- [ ] Theme values used instead of hardcoded values

### Commit Standards
- [ ] Commit message follows conventional format
- [ ] Changes are in correct module (gruid vs app vs gruiddemo)
- [ ] No TODO comments without explanation or issue reference

## Getting Help

When uncertain about:

- **Architecture decisions:** Check `docs/agentRules/architectureRules.md`
- **Code style:** Check `docs/agentRules/codingStandards.md`
- **Component patterns:** Look at existing components in `gruid/`
- **Build setup:** Check convention plugins in `buildLogic/`
- **Testing:** Check `docs/agentRules/testingRules.md`

## Project Philosophy

1. **Reusability First** - Components should be flexible and reusable
2. **Consistency** - Follow established patterns and conventions
3. **Quality** - Never compromise on code quality or testing
4. **Documentation** - Well-documented code is maintainable code
5. **Simplicity** - Prefer simple, clear solutions over clever ones
6. **Performance** - Write efficient Compose code
7. **Accessibility** - Build inclusive user interfaces

## Notes for AI Agents

- This project uses **convention plugins** extensively - check `buildLogic/` before adding plugins
- The design system (`gruid`) should remain **independent** - no app-specific code
- Always **test in both light and dark themes**
- Use **Material 3** components and patterns
- Follow **atomic design** principles when organizing components
- **Preview every component** - previews are essential for development
- **Document public APIs** - gruid is a library, documentation is critical
- When in doubt, **look at existing code** for patterns and conventions

---

**Repository:** [Grapla on GitHub](https://github.com/tamzi/grapla)
