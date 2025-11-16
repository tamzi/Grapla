# Contributing to Grapla

Thank you for your interest in contributing to Grapla! This document provides guidelines and
instructions for contributing.

## Getting Started

1. **Fork the repository** on GitHub
2. **Clone your fork** locally
   ```bash
   git clone https://github.com/your-username/Grapla.git
   cd Grapla
   ```
3. **Set up your development environment**
    - Install Android Studio
    - Set up Java 21 (see README.md for instructions)
    - Sync Gradle dependencies

## Development Workflow

### Before You Start

1. Check existing [issues](https://github.com/tamzi/Grapla/issues) to see if your idea is already
   being discussed
2. Create a new issue if one doesn't exist
3. Wait for maintainer feedback before starting significant work

### Making Changes

1. **Create a feature branch** from `main`
   ```bash
   git checkout -b feature/your-feature-name
   ```

2. **Write clear, concise code**
    - Follow Kotlin coding conventions
    - Add clear comments where necessary
    - Maintain consistency with existing code style

3. **Test your changes**
    - Write unit tests for new functionality
    - Ensure all existing tests pass
    - Run the app and verify your changes work as expected

4. **Commit your changes**
    - Write clear, descriptive commit messages
    - Follow the format: `type(scope): description`
    - Examples:
        - `feat(gruid): add new button component`
        - `fix(app): resolve navigation issue`
        - `docs(readme): update setup instructions`

### Submitting Changes

1. **Push your branch** to your fork
   ```bash
   git push origin feature/your-feature-name
   ```

2. **Create a Pull Request**
    - Provide a clear title and description
    - Reference any related issues
    - Explain what changes you made and why
    - Include screenshots for UI changes

3. **Wait for review**
    - Maintainers will review your PR
    - Address any feedback or requested changes
    - Once approved, your PR will be merged

## Code Style

- Follow [Kotlin coding conventions](https://kotlinlang.org/docs/coding-conventions.html)
- Use Detekt for code analysis and formatting
- Run `./gradlew detekt --auto-correct` before committing

## Building the Project

```bash
# Build all modules
./gradlew build

# Run tests
./gradlew test

# Run code analysis and formatting checks
./gradlew detekt

# Auto-format code
./gradlew detekt --auto-correct
```

## Design System (Gruid)

When contributing to the design system:

1. Follow atomic design principles (atoms, molecules, organisms)
2. Create reusable, composable components
3. Document component usage with examples
4. Add preview composables for easy visualization
5. Update the gruiddemo module with your new component

## Questions?

Feel free to open an issue for:

- Questions about the project
- Clarification on contribution guidelines
- Discussion about potential features

Thank you for contributing! 🎉
