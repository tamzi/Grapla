# Grapla Architecture

## Overview

Grapla follows a **multi-module architecture** with clear separation of concerns, enabling:

- Independent module development and testing
- Faster build times with Gradle's build cache
- Clear dependency boundaries
- Scalable codebase as the project grows

## Module Structure

```
┌─────────────────────────────────────────────────────────┐
│                          app                            │
│                  (Main Application)                     │
└──────────────────┬──────────────────────────────────────┘
                   │
                   │ depends on
                   │
┌──────────────────▼──────────────────────────────────────┐
│                        gruid                            │
│              (Design System Library)                    │
│  - Theme, Colors, Typography                            │
│  - Reusable UI Components                               │
│  - Atomic Design Structure                              │
└──────────────────┬──────────────────────────────────────┘
                   │
                   │ showcased by
                   │
┌──────────────────▼──────────────────────────────────────┐
│                      gruiddemo                          │
│           (Design System Catalog)                       │
│  - Component previews & examples                        │
└─────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────┐
│                      buildLogic                         │
│                 (Convention Plugins)                    │
│  - Shared build configuration                           │
│  - Gradle convention plugins                            │
└─────────────────────────────────────────────────────────┘
```

## Core Modules

### 1. `app` - Application Module

The main Android application module that users install.

**Responsibilities:**

- Application entry point (`MainActivity`)
- Navigation graph
- Dependency injection setup (Hilt)
- Feature integration

**Dependencies:**

- `gruid` - For design system components
- Various AndroidX libraries
- Hilt for DI

**Build Configuration:**

```kotlin
plugins {
    alias(libs.plugins.grapla.android.application)
    alias(libs.plugins.grapla.android.application.compose)
    alias(libs.plugins.grapla.hilt)
}
```

### 2. `gruid` - Design System Library

A standalone Android library module containing all reusable UI components.

**Responsibilities:**

- Design tokens (colors, typography, spacing)
- Atomic design components:
    - **Atoms**: Basic elements (Color, Type)
    - **Molecules**: Simple combinations
    - **Components**: Complex composables
- Theme configuration
- Material 3 implementation

**Structure:**

```
gruid/
├── atoms/
│   ├── color/
│   │   └── Color.kt
│   └── type/
│       └── Type.kt
├── molecules/
│   └── (Simple component combinations)
├── components/
│   └── (Complex reusable components)
└── theme/
    ├── Theme.kt
    └── Type.kt
```

**Design Principles:**

- **Reusable**: Components work across different features
- **Composable**: Built with Jetpack Compose
- **Themeable**: Supports Material 3 theming
- **Testable**: Each component has preview composables

### 3. `gruiddemo` - Design System Catalog

A dedicated module to showcase and test design system components.

**Responsibilities:**

- Component gallery/catalog
- Usage examples
- Interactive previews
- Documentation through code

**Benefits:**

- Designers can see all components
- Developers can test components in isolation
- QA can verify component behavior
- Living documentation

### 4. `buildLogic` - Convention Plugins

Contains composite build with convention plugins for shared build configuration.

**Structure:**

```
buildLogic/
└── convention/
    ├── src/main/kotlin/
    │   ├── AndroidApplicationComposeConventionPlugin.kt
    │   ├── AndroidApplicationConventionPlugin.kt
    │   ├── AndroidLibraryComposeConventionPlugin.kt
    │   ├── AndroidLibraryConventionPlugin.kt
    │   ├── AndroidLintConventionPlugin.kt
    │   ├── HiltConventionPlugin.kt
    │   └── ... (other convention plugins)
    └── build.gradle.kts
```

**Convention Plugins Available:**

| Plugin | Purpose |
|--------|---------|
| `grapla.android.application` | Android app module setup |
| `grapla.android.library` | Android library module setup |
| `grapla.android.application.compose` | Compose configuration for apps |
| `grapla.android.library.compose` | Compose configuration for libraries |
| `grapla.hilt` | Hilt dependency injection |
| `grapla.android.lint` | Lint checks configuration |
| `grapla.android.room` | Room database setup |
| `grapla.android.test` | Testing configuration |

**Why Convention Plugins?**

Traditional approach (repeated in every module):

```kotlin
// ❌ Repeated in every module's build.gradle.kts
android {
    compileSdk = 36
    defaultConfig {
        minSdk = 24
        // ... lots of configuration
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    // ... more configuration
}
```

Convention plugin approach:

```kotlin
// ✅ Simple and consistent
plugins {
    alias(libs.plugins.grapla.android.library)
}
```

**Benefits:**

- **DRY (Don't Repeat Yourself)**: Write configuration once
- **Consistency**: All modules use same configuration
- **Maintainability**: Update once, affects all modules
- **Type Safety**: Kotlin-based, compile-time checked
- **Testable**: Convention plugins are regular Kotlin code

## Data Flow

```
┌─────────────┐
│    User     │
└──────┬──────┘
       │
       │ interacts with
       ▼
┌─────────────┐
│   app/UI    │ ◄──uses── ┌─────────────┐
└──────┬──────┘           │    gruid    │
       │                  │ (Components)│
       │                  └─────────────┘
       │ triggers
       ▼
┌─────────────┐
│  ViewModel  │
└──────┬──────┘
       │
       │ requests
       ▼
┌─────────────┐
│ Repository  │
└──────┬──────┘
       │
       │ fetches
       ▼
┌─────────────┐
│ Data Source │
└─────────────┘
```

## Technology Stack

### UI Layer

- **Jetpack Compose** - Modern declarative UI
- **Material 3** - Latest Material Design
- **Navigation Compose** - Type-safe navigation
- **Hilt** - Dependency injection

### Build System

- **Gradle 8.7+** with Kotlin DSL
- **Version Catalogs** - Centralized dependencies
- **Convention Plugins** - Shared build logic
- **KSP** - Kotlin Symbol Processing

### Testing

- **JUnit 4** - Unit testing
- **Robolectric** - Android unit tests
- **Compose Testing** - UI testing
- **Hilt Testing** - DI testing

### Code Quality

- **ktlint** - Code formatting
- **Lint** - Static analysis
- **Dependency Guard** - Dependency validation

## Best Practices

### 1. Module Independence

- Each module should be self-contained
- Minimize cross-module dependencies
- Use interfaces for abstraction

### 2. Convention Plugins

- Create convention plugins for repeated configuration
- Keep plugins focused on single responsibility
- Document plugin usage

### 3. Design System

- All UI components go in `gruid`
- Use atomic design principles
- Provide preview composables
- Document component usage

### 4. Dependency Management

- Use version catalog (`libs.versions.toml`)
- Group related dependencies
- Keep versions up-to-date with Renovate

### 5. Testing

- Write unit tests for business logic
- Use Robolectric for Android-specific tests
- Test composables with Compose Testing library

## Scaling Guidelines

### Adding a New Feature Module

1. Create module structure
   ```bash
   mkdir -p feature/featurename/src/main/kotlin
   ```

2. Add `build.gradle.kts`:
   ```kotlin
   plugins {
       alias(libs.plugins.grapla.android.library)
       alias(libs.plugins.grapla.android.library.compose)
       alias(libs.plugins.grapla.hilt)
   }
   
   dependencies {
       implementation(project(":gruid"))
       // ... other dependencies
   }
   ```

3. Register in `settings.gradle.kts`:
   ```kotlin
   include(":feature:featurename")
   ```

### Adding to Design System

1. Identify the atomic level (atom/molecule/component)
2. Create in appropriate package in `gruid`
3. Add preview composable
4. Add to `gruiddemo` for showcase

## References

- [Now in Android](https://github.com/android/nowinandroid) - Architecture inspiration
- [Guide to app architecture](https://developer.android.com/topic/architecture)
- [Atomic Design](https://atomicdesign.bradfrost.com/)
- [Gradle Convention Plugins](https://docs.gradle.org/current/samples/sample_convention_plugins.html)
