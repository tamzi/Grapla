# Grapla Project Overview

> **For AI Agents:** This file provides essential context about the Grapla project

## What is Grapla?

Grapla is a modern Android design system and showcase application teplate written in Kotlin. 

## Tech Stack

| Category | Technology | Version |
|----------|-----------|---------|
| **Language** | Kotlin | 2.2.21 |
| **UI Framework** | Jetpack Compose | 2025.10.01 BOM |
| **Build System** | Gradle (Kotlin DSL) | 8.13.0 |
| **DI** | Hilt (Dagger) | 2.57.2 |
| **Async** | Coroutines + Flow | 1.10.2 |
| **Network** | Retrofit + OkHttp | 3.0.0 / 5.2.1 |
| **Database** | Room | 2.8.3 |
| **Preferences** | Proto DataStore | 1.1.7 |
| **Testing** | JUnit 5, Truth, Turbine | Various |
| **Coverage** | Kover + JaCoCo | 0.9.3 / 0.8.12 |
| **Quality** | Detekt | 1.23.8 |

## Architecture

### Reactive, Single-Activity App

- **UI:** Built entirely with Jetpack Compose, including Material 3 components and adaptive layouts
- **State Management:** Unidirectional Data Flow (UDF) using Coroutines and `Flow`s
- **ViewModel Pattern:** ViewModels expose UI state as `StateFlow`
- **Dependency Injection:** Hilt throughout the app
- **Navigation:** Jetpack Navigation 2 for Compose with type-safe routes (Kotlin Serialization)
- **Data Layer:** Repository pattern with Room + Proto DataStore + Network
    - **Room Database:** Complex data (articles, products) requiring querying
    - **Proto DataStore:** User preferences (theme, settings, bookmarks)
    - **Network:** Retrofit with fake data sources for demo, real API for prod
- **Background Processing:** WorkManager for deferrable tasks
- **Design System:** `dds` module with atomic design (atoms, molecules, organisms)

## SDK Versions

| Configuration   | Version | Android Version |
|-----------------|---------|-----------------|
| **Min SDK**     | 31      | Android 12      |
| **Target SDK**  | 36      | Android 15      |
| **Compile SDK** | 36      | Android 15      |
| **Java**        | 17      | -               |
| **Kotlin JVM**  | 17      | -               |

**Market Coverage:** Android 12+ covers 90%+ of active devices

## Module Structure

### Current Modules (3)

- **app** - Main application entry point
- **gruid** - Grapla UI Design System (atoms, theme, components)
- **gruiddemo** - Component showcase and catalog application
- **buildLogic** - Convention plugins for build configuration

### Planned Modules (18+ total)

#### Feature Modules (3)

- **feature:home** - Home feed/dashboard
- **feature:article** - Article list and detail screens
- **feature:productcatalog** - Product browsing

#### Core Modules (11)

- **core:ui** - Shared UI components
- **core:model** - Data models (pure Kotlin)
- **core:data** - Repositories
- **core:network** - API interfaces with fake/real implementations
- **core:database** - Room DAOs and entities
- **core:datastore** - Proto DataStore for preferences
- **core:domain** - Use cases (business logic)
- **core:common** - Utilities (Result, Dispatchers, extensions)
- **core:analytics** - Analytics tracking
- **core:notifications** - Push notifications
- **core:testing** - Test utilities and fakes

#### Infrastructure Modules (3)

- **sync:work** - WorkManager background sync
- **lint** - Custom lint rules
- **ui-test-hilt-manifest** - Hilt testing support

## Build Commands

### Build

```bash
./gradlew build                          # Build all
./gradlew assembleDemoDebug              # Build debug APK (demo flavor)
./gradlew assembleProdRelease            # Build release APK (prod flavor)
```

### Test

```bash
./gradlew test                           # All unit tests
./gradlew connectedAndroidTest           # All instrumented tests
./gradlew :module:test                   # Module-specific tests
```

### Code Coverage

```bash
./gradlew coverageUnit                   # Unit test coverage (Kover)
./gradlew coverageAll                    # Combined coverage (Kover + JaCoCo)
```

**Always run coverage after code changes!**

### Quality Checks

```bash
./gradlew lint                           # Android Lint
./gradlew detekt                         # Detekt static analysis
```

## Product Flavors

| Flavor | Purpose | Data Source |
|--------|---------|-------------|
| **demo** | Development, testing, showcase | Fake/mock data |
| **prod** | Production release | Real API |

## Repository Information

- **URL:** https://github.com/tamzi/grapla
- **Main Branch:** `main`
- **Workflow:** Feature branches → Pull Requests → Main
- **Package:** `com.tamzi.grapla`

## Convention Plugins

Grapla uses convention plugins for centralized build configuration:

### Key Plugins

- `grapla.android.application` - Base app setup
- `grapla.android.library` - Base library setup
- `grapla.android.feature` - Feature module conventions
- `grapla.hilt` - Hilt dependency injection
- `grapla.android.unit.test` - Unit test setup
- `grapla.android.instrumented.test` - Instrumented test setup
- `grapla.android.lint` - Lint configuration
- `grapla.detekt` - Static analysis

**Full list:** See `docs/buildLogic/conventions.md`

## Documentation Structure

```
docs/
├── agents/                          # AI agent rules (you are here!)
│   ├── projectOverview.md           # This file
│   ├── testingRules.md
│   ├── featureDevelopmentRules.md
│   └── ...
├── architecture.md                  # Complete architecture documentation
├── modularizationPlan.md            # 5-week implementation roadmap
├── implementProject.md              # NIA-style implementation guide
├── buildLogic/                      # Convention plugins documentation
├── coverage/                        # Code coverage setup
└── implementationLog/               # Historical record of work completed
```

## Current Status

**Phase:** Planning Complete - Ready for Phase 1 (Core Infrastructure)

**Next Steps:**

1. Create `core:model` module with data models
2. Create `core:common` module with utilities
3. Create `core:network` with fake data sources
4. Continue through Phase 1 of modularization plan

## Key Principles

1. **Clean Architecture** - UI → Domain → Data
2. **Offline-First** - Local data with background sync
3. **Type Safety** - Kotlin, type-safe navigation, sealed classes
4. **Testing** - 70%+ coverage target
5. **Modularization** - Feature isolation, clear boundaries
6. **Convention Over Configuration** - Convention plugins for consistency

## Dependencies Quick Reference

### Always Available (from convention plugins)

- Kotlin stdlib
- Coroutines
- Compose (if using compose plugins)
- Hilt (if using hilt plugin)
- Testing libraries (if using test plugins)

### Add Explicitly When Needed

- Room - Add room dependencies
- Proto DataStore - Add protobuf plugin
- Navigation - Add navigation-compose dependency
- Coil - For image loading

## Important Files

- **SDK Config:** `buildLogic/convention/src/main/kotlin/util/BuildLogicConstants.kt`
- **Version Catalog:** `gradle/libs.versions.toml`
- **Root Build:** `build.gradle.kts`
- **Settings:** `settings.gradle.kts`
