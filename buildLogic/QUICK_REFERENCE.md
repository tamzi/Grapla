# Convention Plugins Quick Reference

A quick lookup guide for all Grapla convention plugins.

## 🚀 Plugin ID Reference

| Plugin ID | Category | Purpose |
|-----------|----------|---------|
| `grapla.android.application` | App | Base Android app setup |
| `grapla.android.application.compose` | App | Compose for apps |
| `grapla.android.application.firebase` | App | Firebase integration |
| `grapla.android.library` | Library | Base Android library |
| `grapla.android.library.compose` | Library | Compose for libraries |
| `grapla.android.feature` | Feature | Feature module (lib + hilt) |
| `grapla.android.test` | Testing | Test module setup |
| `grapla.android.unit.test` 🆕 | Testing | Unit tests (JUnit 6) |
| `grapla.android.instrumented.test` 🆕 | Testing | Device tests (Espresso) |
| `grapla.android.compose.test` 🆕 | Testing | Compose UI tests |
| `grapla.kover` 🆕 | Coverage | Kotlin coverage |
| `grapla.jacoco` 🆕 | Coverage | Java/Kotlin coverage |
| `grapla.detekt` 🆕 | Quality | Static analysis |
| `grapla.android.flavor` 🆕 | Flavor | Build variants |
| `grapla.hilt` | Infra | Dependency injection |
| `grapla.android.room` | Infra | Database |
| `grapla.android.lint` | Infra | Lint rules |
| `grapla.jvm.library` | JVM | Pure Kotlin library |

🆕 = New plugin

## ⚡ Quick Commands

### Testing

```bash
./gradlew test                          # Run all unit tests
./gradlew connectedAndroidTest          # Run instrumented tests
```

### Coverage

```bash
# Kover
./gradlew koverHtmlReport               # HTML report
./gradlew koverXmlReport                # XML report
./gradlew koverVerify                   # Check thresholds

# JaCoCo
./gradlew jacocoTestReport              # All variants
./gradlew jacocoDebugReport             # Debug only
```

### Quality

```bash
./gradlew detekt                        # Run analysis
./gradlew detektBaseline                # Create baseline
./gradlew detekt --auto-correct         # Auto-fix issues
```

### Combined

```bash
./gradlew clean detekt test koverVerify # Full quality check
```

## 📋 Common Plugin Combos

### App Module

```kotlin
plugins {
    id("grapla.android.application")
    id("grapla.android.application.compose")
    id("grapla.hilt")
    id("grapla.android.flavor")         // 🆕 Build variants
    id("grapla.kover")                  // 🆕 Coverage
    id("grapla.detekt")                 // 🆕 Quality
}
```

### Feature Module

```kotlin
plugins {
    id("grapla.android.feature")
    id("grapla.android.library.compose")
    id("grapla.android.unit.test")      // 🆕 Unit tests
    id("grapla.android.compose.test")   // 🆕 UI tests
    id("grapla.kover")                  // 🆕 Coverage
    id("grapla.detekt")                 // 🆕 Quality
}
```

### Data Module

```kotlin
plugins {
    id("grapla.android.library")
    id("grapla.android.room")
    id("grapla.hilt")
    id("grapla.android.unit.test")              // 🆕 Unit tests
    id("grapla.android.instrumented.test")      // 🆕 DB tests
    id("grapla.jacoco")                         // 🆕 Coverage
    id("grapla.detekt")                         // 🆕 Quality
}
```

### UI Module

```kotlin
plugins {
    id("grapla.android.library")
    id("grapla.android.library.compose")
    id("grapla.android.unit.test")      // 🆕 Unit tests
    id("grapla.android.compose.test")   // 🆕 UI tests
    id("grapla.kover")                  // 🆕 Coverage
}
```

### Domain Module

```kotlin
plugins {
    id("grapla.jvm.library")
    id("grapla.android.unit.test")      // 🆕 Unit tests
    id("grapla.kover")                  // 🆕 Coverage
    id("grapla.detekt")                 // 🆕 Quality
}
```

## 🎯 Plugin Dependencies

### Must Be Combined

- `grapla.android.application.compose` requires `grapla.android.application`
- `grapla.android.library.compose` requires `grapla.android.library`
- Testing plugins work with application or library plugins

### Auto-Applied

- `grapla.android.feature` includes `grapla.android.library` + `grapla.hilt`
- `grapla.android.application` includes `grapla.android.lint`
- `grapla.android.library` includes `grapla.android.lint`

## 📝 Test Dependencies Added

### Unit Test Plugin

- JUnit 6 (Jupiter)
- Kotlin Test
- Coroutines Test
- Truth
- Turbine
- Robolectric

### Instrumented Test Plugin

- AndroidX Test
- Espresso
- UI Automator
- JUnit 4
- Truth
- Hilt Testing (if Hilt applied)

### Compose Test Plugin

- Compose UI Test
- Test Manifest
- UI Tooling
- Navigation Testing

## 🔧 Configuration Examples

### Kover Coverage

```kotlin
kover {
    reports {
        filters {
            excludes {
                classes("*_Factory", "*.BuildConfig")
            }
        }
        total {
            xml { onCheck = true }
            html { onCheck = true }
        }
        verify {
            rule { minBound(80) }
        }
    }
}
```

### Detekt Quality

```kotlin
detekt {
    parallel = true
    buildUponDefaultConfig = true
    autoCorrect = true
    config.setFrom(files("$rootDir/config/detekt/detekt-config.yml"))
}
```

### Build Flavors

```kotlin
android {
    productFlavors {
        getByName("dev") {
            applicationIdSuffix = ".dev"
            versionNameSuffix = "-dev"
        }
        getByName("production") {
            // Production config
        }
    }
}
```

## 📊 Report Locations

### Kover

- HTML: `build/reports/kover/html/index.html`
- XML: `build/reports/kover/report.xml`

### JaCoCo

- HTML: `build/reports/jacoco/jacocoTestReport/html/index.html`
- XML: `build/reports/jacoco/jacocoTestReport/jacocoTestReport.xml`

### Detekt

- HTML: `build/reports/detekt/detekt.html`
- XML: `build/reports/detekt/detekt.xml`
- TXT: `build/reports/detekt/detekt.txt`

### Tests

- Unit: `build/reports/tests/testDebugUnitTest/index.html`
- Instrumented: `build/reports/androidTests/connected/index.html`

## 🎨 Flavor Variants

### Default Dimensions

1. **environment**: dev, staging, production
2. **version**: free, paid

### Generated Variants

- devFreeDebug / devFreeRelease
- devPaidDebug / devPaidRelease
- stagingFreeDebug / stagingFreeRelease
- stagingPaidDebug / stagingPaidRelease
- productionFreeDebug / productionFreeRelease
- productionPaidDebug / productionPaidRelease

## 🚦 Adoption Path

### Level 1: Start Simple

```kotlin
plugins {
    id("grapla.android.library")
}
```

### Level 2: Add Testing

```kotlin
plugins {
    id("grapla.android.library")
    id("grapla.android.unit.test")          // 🆕
}
```

### Level 3: Add Coverage

```kotlin
plugins {
    id("grapla.android.library")
    id("grapla.android.unit.test")          // 🆕
    id("grapla.kover")                      // 🆕
}
```

### Level 4: Full Quality

```kotlin
plugins {
    id("grapla.android.library")
    id("grapla.android.unit.test")          // 🆕
    id("grapla.kover")                      // 🆕
    id("grapla.detekt")                     // 🆕
}
```

## 📖 More Info

- [README.md](./README.md) - Overview
- [PLUGIN_GUIDE.md](./PLUGIN_GUIDE.md) - Detailed usage
- [PLUGINS_OVERVIEW.md](./PLUGINS_OVERVIEW.md) - Visual guide
- [STRUCTURE.md](./STRUCTURE.md) - Technical details

---

**Quick Tip**: Start with unit tests and coverage, then add quality checks as your project matures.
