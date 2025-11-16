# Build Logic - Convention Plugins

This directory contains Gradle convention plugins that centralize build configuration across the
Grapla project. The structure is inspired by [dashiki](https://github.com/tamzi/dashiki) and follows
best practices for organizing build logic.

## What Are Convention Plugins?

Instead of repeating the same Gradle configuration in every module, you define it once in a plugin
and apply it where needed. This keeps your build scripts DRY (Don't Repeat Yourself) and makes
configuration changes easier to manage.

## 📦 Available Plugins

### Application

- `grapla.android.application` - Android app configuration
- `grapla.android.application.compose` - Compose support for apps
- `grapla.android.application.firebase` - Firebase integration

### Library

- `grapla.android.library` - Android library configuration
- `grapla.android.library.compose` - Compose support for libraries
- `grapla.android.feature` - Feature module setup

### Infrastructure

- `grapla.hilt` - Dependency injection (Hilt/Dagger)
- `grapla.android.room` - Room database setup
- `grapla.android.lint` - Linting configuration

### Testing

- `grapla.android.test` - Android test modules
- `grapla.android.unit.test` - Unit test configuration (JUnit 6, Robolectric)
- `grapla.android.instrumented.test` - Instrumented test setup (Espresso, UI Automator)
- `grapla.android.compose.test` - Compose UI test configuration

### Coverage

- `grapla.kover` - Kover coverage (Kotlin-first)
- `grapla.jacoco` - JaCoCo coverage (Java/Kotlin)

### Quality

- `grapla.detekt` - Static code analysis (Detekt)

### Flavor Configuration

- `grapla.android.flavor` - Product flavor setup (environment, version dimensions)

### JVM

- `grapla.jvm.library` - Pure Kotlin/JVM libraries

## 🏗️ Project Structure

```
buildLogic/
├── settings.gradle.kts          # Build logic settings
├── gradle.properties            # Build logic properties
├── README.md                    # This file
└── convention/
    ├── build.gradle.kts         # Plugin registration
    └── src/main/kotlin/
        ├── application/         # App-level plugins
        │   ├── AndroidApplicationConventionPlugin.kt
        │   ├── AndroidApplicationComposeConventionPlugin.kt
        │   └── AndroidApplicationFirebaseConventionPlugin.kt
        ├── library/             # Library-level plugins
        │   ├── AndroidLibraryConventionPlugin.kt
        │   └── AndroidLibraryComposeConventionPlugin.kt
        ├── feature/             # Feature module plugins
        │   └── AndroidFeatureConventionPlugin.kt
        ├── hilt/                # Dependency injection
        │   └── HiltConventionPlugin.kt
        ├── android/             # Android-specific plugins
        │   ├── AndroidRoomConventionPlugin.kt
        │   └── AndroidLintConventionPlugin.kt
        ├── test/                # Testing plugins
        │   └── AndroidTestConventionPlugin.kt
        ├── jvm/                 # JVM plugins
        │   └── JvmLibraryConventionPlugin.kt
        └── util/                # Shared utilities
            ├── BuildLogicConstants.kt
            ├── ProjectExtensions.kt
            ├── DependencyConfig.kt
            ├── KotlinAndroid.kt
            ├── AndroidCompose.kt
            └── AndroidInstrumentedTests.kt
```

## 🎯 Design Principles

1. **Single Responsibility** - Each plugin has one clear purpose
2. **Composability** - Plugins can be combined (e.g., library + compose + hilt)
3. **Consistency** - All modules of the same type have identical configuration
4. **Maintainability** - Change once, apply everywhere
5. **Type Safety** - Use the version catalog with type-safe accessors

## 📚 Usage Examples

### Android Application with Compose

```kotlin
// app/build.gradle.kts
plugins {
    id("grapla.android.application")
    id("grapla.android.application.compose")
    id("grapla.hilt")
    id("grapla.android.flavor")
    id("grapla.kover")
    id("grapla.detekt")
}

dependencies {
    // Your app dependencies
}
```

### Android Library with Compose and Testing

```kotlin
// core/ui/build.gradle.kts
plugins {
    id("grapla.android.library")
    id("grapla.android.library.compose")
    id("grapla.android.unit.test")
    id("grapla.android.compose.test")
}

dependencies {
    // Your library dependencies
}
```

### Feature Module with Full Testing and Coverage

```kotlin
// feature/home/build.gradle.kts
plugins {
    id("grapla.android.feature")
    id("grapla.android.library.compose")
    id("grapla.android.unit.test")
    id("grapla.android.instrumented.test")
    id("grapla.android.compose.test")
    id("grapla.jacoco")
}

dependencies {
    // Feature-specific dependencies
    // Common feature dependencies are already included by the feature plugin
}
```

### Library with Code Quality and Coverage

```kotlin
// core/data/build.gradle.kts
plugins {
    id("grapla.android.library")
    id("grapla.android.room")
    id("grapla.hilt")
    id("grapla.android.unit.test")
    id("grapla.kover")
    id("grapla.detekt")
}

dependencies {
    // Your data layer dependencies
}
```

## 🔧 Configuration

### Build Constants

All SDK versions and other constants are centralized in `util/BuildLogicConstants.kt`:

```kotlin
object BuildLogicConstants {
    const val COMPILE_SDK = 36
    const val TARGET_SDK = 36
    const val MIN_SDK = 28
    const val JAVA_VERSION = "21"
    const val KOTLIN_JVM_TARGET = "21"
}
```

### Version Catalog Access

Plugins can access the version catalog using the `libs` extension:

```kotlin
dependencies {
    add("implementation", libs.findLibrary("androidx.core.ktx").get())
}
```

## 🚀 Adding a New Plugin

1. Create a new Kotlin file in the appropriate package (e.g., `quality/DetektConventionPlugin.kt`)
2. Implement the `Plugin<Project>` interface
3. Register the plugin in `convention/build.gradle.kts`:

```kotlin
register("detekt") {
    id = "grapla.detekt"
    implementationClass = "quality.DetektConventionPlugin"
}
```

4. Use it in your modules:

```kotlin
plugins {
    id("grapla.detekt")
}
```

## 📖 References

- [Sharing Build Logic](https://docs.gradle.org/current/userguide/sharing_build_logic_between_subprojects.html)
- [Convention Plugins](https://docs.gradle.org/current/samples/sample_convention_plugins.html)
- [Now in Android buildLogic](https://github.com/android/nowinandroid/tree/main/build-logic)
- [Dashiki buildLogic](https://github.com/tamzi/dashiki/tree/main/buildLogic)
