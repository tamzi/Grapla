# BuildLogic Structure Overview

## Directory Layout

```
buildLogic/
│
├── settings.gradle.kts              # BuildLogic settings, includes :convention
├── gradle.properties                # BuildLogic properties
├── README.md                        # Comprehensive documentation
├── MIGRATION_SUMMARY.md            # Migration details
└── STRUCTURE.md                     # This file
│
└── convention/                      # Convention plugins module
    │
    ├── build.gradle.kts            # Plugin definitions and registration
    │
    └── src/main/kotlin/
        │
        ├── application/            # Application-level plugins
        │   ├── AndroidApplicationConventionPlugin.kt
        │   ├── AndroidApplicationComposeConventionPlugin.kt
        │   └── AndroidApplicationFirebaseConventionPlugin.kt
        │
        ├── library/                # Library-level plugins
        │   ├── AndroidLibraryConventionPlugin.kt
        │   └── AndroidLibraryComposeConventionPlugin.kt
        │
        ├── feature/                # Feature module plugins
        │   └── AndroidFeatureConventionPlugin.kt
        │
        ├── hilt/                   # Dependency injection
        │   └── HiltConventionPlugin.kt
        │
        ├── android/                # Android infrastructure
        │   ├── AndroidRoomConventionPlugin.kt
        │   └── AndroidLintConventionPlugin.kt
        │
        ├── test/                   # Testing plugins
        │   └── AndroidTestConventionPlugin.kt
        │
        ├── testing/                # Advanced testing plugins
        │   ├── AndroidUnitTestConventionPlugin.kt
        │   ├── AndroidInstrumentedTestConventionPlugin.kt
        │   └── AndroidComposeTestConventionPlugin.kt
        │
        ├── coverage/               # Coverage plugins
        │   ├── KoverConventionPlugin.kt
        │   └── JacocoConventionPlugin.kt
        │
        ├── quality/                # Code quality plugins
        │   └── DetektConventionPlugin.kt
        │
        ├── flavor/                 # Build flavor plugins
        │   └── AndroidFlavorConventionPlugin.kt
        │
        ├── jvm/                    # JVM plugins
        │   └── JvmLibraryConventionPlugin.kt
        │
        └── util/                   # Shared utilities
            ├── BuildLogicConstants.kt          # SDK versions & constants
            ├── ProjectExtensions.kt            # Version catalog access
            ├── DependencyConfig.kt             # Build configuration
            ├── KotlinAndroid.kt                # Kotlin/Android setup
            ├── AndroidCompose.kt               # Compose configuration
            └── AndroidInstrumentedTests.kt     # Test management
```

## Plugin Organization

### By Category

#### 🚀 Application (3 plugins)

Plugins for Android application modules (`:app`)

| Plugin | File | Purpose |
|--------|------|---------|
| `grapla.android.application` | `application/AndroidApplicationConventionPlugin.kt` | Base Android app setup |
| `grapla.android.application.compose` | `application/AndroidApplicationComposeConventionPlugin.kt` | Jetpack Compose for apps |
| `grapla.android.application.firebase` | `application/AndroidApplicationFirebaseConventionPlugin.kt` | Firebase services |

#### 📚 Library (2 plugins)

Plugins for Android library modules

| Plugin | File | Purpose |
|--------|------|---------|
| `grapla.android.library` | `library/AndroidLibraryConventionPlugin.kt` | Base Android library setup |
| `grapla.android.library.compose` | `library/AndroidLibraryComposeConventionPlugin.kt` | Jetpack Compose for libraries |

#### ⭐ Feature (1 plugin)

Specialized plugins for feature modules

| Plugin | File | Purpose |
|--------|------|---------|
| `grapla.android.feature` | `feature/AndroidFeatureConventionPlugin.kt` | Feature module with UI & DI |

#### 🔧 Infrastructure (3 plugins)

Plugins for common infrastructure needs

| Plugin | File | Purpose |
|--------|------|---------|
| `grapla.hilt` | `hilt/HiltConventionPlugin.kt` | Dependency injection |
| `grapla.android.room` | `android/AndroidRoomConventionPlugin.kt` | Room database |
| `grapla.android.lint` | `android/AndroidLintConventionPlugin.kt` | Lint configuration |

#### 🧪 Testing (4 plugins)

Plugins for test modules

| Plugin                             | File                                                 | Purpose                       |
|------------------------------------|------------------------------------------------------|-------------------------------|
| `grapla.android.test`              | `test/AndroidTestConventionPlugin.kt`                | Android test module setup     |
| `grapla.android.unit.test`         | `testing/AndroidUnitTestConventionPlugin.kt`         | Unit test configuration       |
| `grapla.android.instrumented.test` | `testing/AndroidInstrumentedTestConventionPlugin.kt` | Instrumented test setup       |
| `grapla.android.compose.test`      | `testing/AndroidComposeTestConventionPlugin.kt`      | Compose UI test configuration |

#### 📊 Coverage (2 plugins)

Plugins for code coverage analysis

| Plugin          | File                                 | Purpose                       |
|-----------------|--------------------------------------|-------------------------------|
| `grapla.kover`  | `coverage/KoverConventionPlugin.kt`  | Kover coverage (Kotlin-first) |
| `grapla.jacoco` | `coverage/JacocoConventionPlugin.kt` | JaCoCo coverage (Java/Kotlin) |

#### 🔍 Quality (1 plugin)

Plugins for code quality analysis

| Plugin          | File                                | Purpose              |
|-----------------|-------------------------------------|----------------------|
| `grapla.detekt` | `quality/DetektConventionPlugin.kt` | Static code analysis |

#### 🎨 Flavor (1 plugin)

Plugins for build variant configuration

| Plugin                  | File                                      | Purpose              |
|-------------------------|-------------------------------------------|----------------------|
| `grapla.android.flavor` | `flavor/AndroidFlavorConventionPlugin.kt` | Product flavor setup |

#### ☕ JVM (1 plugin)

Plugins for pure Kotlin/JVM modules

| Plugin | File | Purpose |
|--------|------|---------|
| `grapla.jvm.library` | `jvm/JvmLibraryConventionPlugin.kt` | Pure Kotlin library |

### Utility Files

#### Core Configuration

| File | Purpose | Used By |
|------|---------|---------|
| `BuildLogicConstants.kt` | SDK versions, Java/Kotlin versions | All plugins |
| `DependencyConfig.kt` | Build behavior configuration | Compose plugins |

#### Extensions & Helpers

| File | Purpose | Used By |
|------|---------|---------|
| `ProjectExtensions.kt` | Version catalog access (`libs`) | All plugins |
| `KotlinAndroid.kt` | Kotlin/Android setup functions | Android plugins |
| `AndroidCompose.kt` | Compose setup functions | Compose plugins |
| `AndroidInstrumentedTests.kt` | Test management | Library plugins |

## Common Usage Patterns

### Basic Android App

```kotlin
plugins {
    id("grapla.android.application")
    id("grapla.android.application.compose")
    id("grapla.hilt")
}
```

### Android Library with Compose

```kotlin
plugins {
    id("grapla.android.library")
    id("grapla.android.library.compose")
}
```

### Feature Module

```kotlin
plugins {
    id("grapla.android.feature")  // Includes library + hilt
    id("grapla.android.library.compose")
}
```

### Library with Database

```kotlin
plugins {
    id("grapla.android.library")
    id("grapla.android.room")
    id("grapla.hilt")
}
```

## Plugin Dependencies

```
grapla.android.feature
├── grapla.android.library
│   ├── grapla.android.lint
│   └── com.android.library
└── grapla.hilt
    └── com.google.devtools.ksp

grapla.android.application.compose
└── grapla.android.application
    ├── grapla.android.lint
    └── com.android.application

grapla.android.library.compose
└── grapla.android.library
    ├── grapla.android.lint
    └── com.android.library
```

## Statistics

- **Total Plugins**: 20
- **Total Packages**: 11 (application, library, feature, hilt, android, test, testing, coverage,
  quality, flavor, jvm)
- **Total Utility Files**: 6
- **Total Kotlin Files**: 26
- **Lines of Code**: ~4,500 (including documentation)

## Key Features

### ✅ Type Safety

- All plugins use version catalog with type-safe accessors
- Compile-time checking of configurations

### ✅ Centralized Configuration

- Single source of truth for SDK versions
- Easy to update across entire project

### ✅ Composability

- Plugins can be combined
- Single responsibility principle
- No duplicate configuration

### ✅ Consistency

- Same configuration for same module types
- Predictable behavior

### ✅ Maintainability

- Well-organized structure
- Clear naming conventions
- Comprehensive documentation

## Related Documentation

- [buildLogic/README.md](./README.md) - Main documentation

---
