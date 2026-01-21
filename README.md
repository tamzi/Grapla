# Grapla

[![Build](https://github.com/tamzi/Grapla/actions/workflows/build.yml/badge.svg)](https://github.com/tamzi/Grapla/actions/workflows/build.yml)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.2.21-blue.svg?style=flat&logo=kotlin)](http://kotlinlang.org)

**Grapla** is a modern Android app template showcasing best practices for multi-module architecture
with a dedicated design system library. Perfect for jumpstarting your next Android project with
convention plugins and a scalable structure.

## 🎯 Features

- ✅ **Multi-module architecture** with clear separation of concerns
- ✅ **Design System (Gruid)** - Reusable UI components library
- ✅ **Convention Plugins** - Build logic sharing across modules
- ✅ **Jetpack Compose** - Modern declarative UI toolkit
- ✅ **Material 3** - Latest Material Design components
- ✅ **Gradle Version Catalogs** - Centralized dependency management
- ✅ **Hilt** - Dependency injection
- ✅ **Detekt** - Static code analysis and formatting
- ✅ **CI/CD** - Automated workflows with GitHub Actions

## 📦 Project Structure

```
Grapla/
├── app/              # Main application module
├── gruid/            # Design system library (Grapla UI Design)
├── gruiddemo/        # Design system showcase module
├── buildLogic/       # Convention plugins for build logic
│   └── convention/   # Shared build configuration
├── data/             # Data layer modules
├── datasources/      # Data source implementations
└── scripts/          # Git hooks and automation scripts
```

## 🏗️ Architecture

### Convention Plugins (Build Logic)

Grapla uses **convention plugins** to share build configuration across modules. This approach:

- ✅ Reduces build script duplication
- ✅ Ensures consistency across modules
- ✅ Makes build logic testable and maintainable
- ✅ Simplifies module setup

**Available Convention Plugins:**

- `grapla.android.application` - Android app modules
- `grapla.android.library` - Android library modules
- `grapla.android.library.compose` - Compose library modules
- `grapla.android.application.compose` - Compose app modules
- `grapla.hilt` - Hilt dependency injection
- `grapla.android.lint` - Lint configuration
- `grapla.android.room` - Room database setup

See the [buildLogic README](buildLogic/README.md) for more details.

### Design System (Gruid)

The **Gruid** module contains reusable UI components following atomic design principles:

- **Atoms** - Basic building blocks (colors, typography)
- **Molecules** - Simple component combinations
- **Components** - Complex, reusable UI elements
- **Theme** - App-wide theming configuration

## 🚀 Getting Started

### Prerequisites

- **Android Studio** - Latest stable version
- **Java 21** - Required for Gradle and Kotlin compilation
- **Gradle 8.7+** - Project uses Gradle wrapper

### Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/tamzi/Grapla.git
   cd Grapla
   ```

2. **Set Java 21 in Android Studio**
    - Go to: `Settings > Build, Execution, Deployment > Build Tools > Gradle`
    - Set `Gradle JDK` to `21`

3. **Install Git Hooks** ⚡ **AUTO-INSTALLED!**

   Git hooks are **automatically installed** during the first Gradle sync.

   **Manual installation (if needed):**
   ```bash
   ./scripts/install-git-hooks.sh
   ```

   **Verify installation:**
   ```bash
   ./scripts/verify-hooks.sh
   ```

   These hooks enforce code quality and commit rules automatically.

4. **Sync Gradle**
   ```bash
   ./gradlew sync
   ```

5. **Build the project**
   ```bash
   ./gradlew build
   ```

6. **Run the app**
    - Select the `app` run configuration
    - Choose your device/emulator
    - Click Run ▶️

## 🧪 Testing

```bash
# Run all tests
./gradlew test

# Run tests for specific module
./gradlew :app:test
./gradlew :gruid:test
```

## ⚙️ Common Commands

```bash
# Build all modules
./gradlew build

# Build specific module
./gradlew :app:build
./gradlew :gruid:build

# Clean build
./gradlew clean

# Run tests
./gradlew test

# Code quality checks
./gradlew detekt
./gradlew lint

# Auto-format code
./gradlew detekt --auto-correct

# Install app on device
./gradlew installDebug

# View all available tasks
./gradlew tasks
```

## 🎨 Design System Usage

To use Gruid components in your modules:

1. **Add dependency** in your module's `build.gradle.kts`:
   ```kotlin
   dependencies {
       implementation(project(":gruid"))
   }
   ```

2. **Use components**:
   ```kotlin
   @Composable
   fun MyScreen() {
       GraplaTheme {
           // Your UI here
       }
   }
   ```

3. **Explore the catalog**
    - Check out the `gruiddemo` module for component examples
    - Run the gruiddemo app to see all components in action

## 🛠️ Development

### Code Style

The project uses **Detekt** for code analysis and formatting:

```bash
# Run code analysis and formatting checks
./gradlew detekt

# Auto-format code (fixes formatting issues)
./gradlew detekt --auto-correct
```

## 🎣 Git Hooks - Automated Quality Checks

Git hooks automatically validate your code before commits and pushes, catching issues early.

### 🎉 Automatic Installation

**Good news!** Git hooks are **automatically installed** when you first sync the project in Gradle.

No manual setup needed - just clone and build!

### Manual Installation (If Needed)

If for some reason hooks aren't auto-installed, run:

```bash
./scripts/install-git-hooks.sh
```

That's it! All hooks are now installed and active.

### Verify Installation

```bash
./scripts/verify-hooks.sh
```

Expected output:

```
✅ All git hooks are properly installed
```

### What Do The Hooks Do?

**Pre-Commit Hook** (runs when you commit)

- ✅ Validates build succeeds
- ✅ Runs tests
- ✅ Checks code quality (detekt, lint)
- ✅ Enforces commit rules (file count, naming, etc.)
- ⏱️ Takes ~30-60 seconds

**Pre-Push Hook** (runs when you push)

- ✅ Validates all commits in push
- ✅ Ensures no mixed code/doc changes
- ✅ Runs detekt and tests
- ⏱️ Takes ~15-30 seconds

### Common Hook Scenarios

**✅ Successful commit:**

```bash
git add MyFile.kt
git commit -m "feat: add new feature"
# Hooks run automatically, checks pass ✓
# Commit succeeds
```

**❌ Blocked commit (too many files):**

```bash
git add file1.kt file2.kt file3.kt file4.kt file5.kt file6.kt
git commit -m "feat: big change"
# ❌ VIOLATION: 6 files staged (maximum: 5)
# Fix: Split into smaller commits
```

**🔧 How to fix:**

```bash
# Split your changes into smaller commits
git reset HEAD~1
git add file1.kt file2.kt
git commit -m "feat: add feature part 1"
git add file3.kt file4.kt
git commit -m "feat: add feature part 2"
```

### Bypassing Hooks (Emergency Only)

In rare emergencies, you can skip hooks:

```bash
git commit --no-verify   # Skip pre-commit
git push --no-verify     # Skip pre-push
```

⚠️ **Warning:** This is strongly discouraged! Violations will still be caught by:

- CI/CD checks (slower feedback)
- Server-side hooks (cannot be bypassed)
- Code review

### Troubleshooting

**Hooks not running?**

```bash
# Check status
./scripts/verify-hooks.sh

# Reinstall if needed
./scripts/install-git-hooks.sh
```

**Hooks too slow?**

- They run build and tests to prevent broken commits
- This saves time by catching issues before they enter git history
- Commit less frequently, or push more often (pre-push is faster)

**Need more details?**

- Quick guide: `docs/quickStartHooks.md`
- Full documentation: `scripts/README.md`
- Enforcement strategy: `docs/gitHookEnforcement.md`

## 🤝 Contributing

Contributions are welcome! Please read our [Contributing Guidelines](CONTRIBUTING.md)
and [Code of Conduct](CODE_OF_CONDUCT.md).

### Quick Start for Contributors

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/amazing-feature`
3. Make your changes
4. Run tests: `./gradlew test`
5. Format code: `./gradlew detekt --auto-correct`
6. Commit: `git commit -m 'feat(module): add amazing feature'`
7. Push: `git push origin feature/amazing-feature`
8. Open a Pull Request

Using gradle 9+
junit6 = "6.0.2"
kotlin = "2.3.0"
ksp = "2.3.3"

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.


## 📧 Contact

⭐ If you find this template helpful, consider giving it a star!
