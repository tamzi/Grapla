# Convention Plugin Usage Guide

This guide provides detailed information on how to use each convention plugin in the Grapla project.

## Table of Contents

- [Testing Plugins](#testing-plugins)
- [Coverage Plugins](#coverage-plugins)
- [Quality Plugins](#quality-plugins)
- [Flavor Configuration](#flavor-configuration)
- [Best Practices](#best-practices)

---

## Testing Plugins

### Unit Test Plugin (`grapla.android.unit.test`)

Configures unit testing with JUnit 6 (Jupiter), Kotlin test support, and modern testing libraries.

**Apply to your module:**

```kotlin
plugins {
    id("grapla.android.library")
    id("grapla.android.unit.test")
}
```

**Included dependencies:**

- JUnit 6 (Jupiter) - Modern testing framework
- Kotlin test support
- Coroutines test library
- Truth - Fluent assertions
- Turbine - Flow testing
- Robolectric - Android framework testing

**Example test:**

```kotlin
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import kotlinx.coroutines.test.runTest

class MyViewModelTest {
    @Test
    fun `test loading state`() = runTest {
        val viewModel = MyViewModel()
        assertEquals(LoadingState.Loading, viewModel.state.value)
    }
}
```

---

### Instrumented Test Plugin (`grapla.android.instrumented.test`)

Configures instrumented (on-device) tests with Espresso and AndroidX Test libraries.

**Apply to your module:**

```kotlin
plugins {
    id("grapla.android.library")
    id("grapla.android.instrumented.test")
}
```

**Included dependencies:**

- AndroidX Test framework
- Espresso UI testing
- UI Automator for complex interactions
- Hilt testing support (when Hilt is applied)

**Example test:**

```kotlin
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MyFragmentTest {
    @Test
    fun testButtonIsDisplayed() {
        onView(withId(R.id.button))
            .check(matches(isDisplayed()))
    }
}
```

---

### Compose Test Plugin (`grapla.android.compose.test`)

Configures Compose UI testing with test dependencies and debug tooling.

**Apply to your module:**

```kotlin
plugins {
    id("grapla.android.library")
    id("grapla.android.library.compose")
    id("grapla.android.compose.test")
}
```

**Included dependencies:**

- Compose UI Test
- Compose Test Manifest
- Compose UI Tooling
- Navigation Testing

**Example test:**

```kotlin
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test

class MyComposableTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testButtonClick() {
        composeTestRule.setContent {
            MyButton(text = "Click me")
        }
        
        composeTestRule.onNodeWithText("Click me").performClick()
    }
}
```

---

## Coverage Plugins

### Kover Plugin (`grapla.kover`)

Kotlin-first code coverage tool with better performance for Kotlin code.

**Apply to your module:**

```kotlin
plugins {
    id("grapla.android.library")
    id("grapla.kover")
}
```

**Configuration example:**

```kotlin
kover {
    reports {
        filters {
            excludes {
                classes("*_Factory", "*_HiltModules*", "*.BuildConfig")
                packages("*.di", "*.di.*")
                annotatedBy("dagger.Module")
            }
        }
        total {
            xml { onCheck = true }
            html { onCheck = true }
        }
        verify {
            rule {
                minBound(80) // 80% minimum coverage
            }
        }
    }
}
```

**Generate reports:**

```bash
./gradlew koverHtmlReport
./gradlew koverXmlReport
./gradlew koverVerify
```

---

### JaCoCo Plugin (`grapla.jacoco`)

Industry-standard code coverage tool with support for Java and Kotlin.

**Apply to your module:**

```kotlin
plugins {
    id("grapla.android.library")
    id("grapla.jacoco")
}
```

**Generate reports:**

```bash
./gradlew jacocoTestReport      # Unified report for all variants
./gradlew jacocoDebugReport     # Debug variant only
./gradlew jacocoReleaseReport   # Release variant only
```

**Report locations:**

- XML: `build/reports/jacoco/jacoco<Variant>Report/jacoco<Variant>Report.xml`
- HTML: `build/reports/jacoco/jacoco<Variant>Report/html/index.html`

---

## Quality Plugins

### Detekt Plugin (`grapla.detekt`)

Static code analysis tool for Kotlin that detects code smells and complexity issues.

**Apply to your module:**

```kotlin
plugins {
    id("grapla.android.library")
    id("grapla.detekt")
}
```

**Configuration example:**

```kotlin
detekt {
    parallel = true
    buildUponDefaultConfig = true
    autoCorrect = true
    config.setFrom(files("$rootDir/config/detekt/detekt-config.yml"))
}

tasks.withType<Detekt>().configureEach {
    reports {
        xml.required.set(true)
        html.required.set(true)
        txt.required.set(true)
        sarif.required.set(true)
        md.required.set(true)
    }
    jvmTarget = "21"
}
```

**Run analysis:**

```bash
./gradlew detekt              # Run analysis
./gradlew detektBaseline      # Create baseline for existing issues
```

**Custom configuration file** (`config/detekt/detekt-config.yml`):

```yaml
build:
  maxIssues: 0

complexity:
  ComplexMethod:
    threshold: 15
  LongMethod:
    threshold: 60
    
style:
  MaxLineLength:
    maxLineLength: 120
```

---

## Flavor Configuration

### Flavor Plugin (`grapla.android.flavor`)

Configures product flavors for different app variants (dev, staging, production, free, paid).

**Apply to your module:**

```kotlin
plugins {
    id("grapla.android.application")
    id("grapla.android.flavor")
}
```

**Default flavor dimensions:**

1. **environment**: dev, staging, production
2. **version**: free, paid

**Customize in your build.gradle.kts:**

```kotlin
android {
    productFlavors {
        getByName("dev") {
            applicationIdSuffix = ".dev"
            versionNameSuffix = "-dev"
            buildConfigField("String", "API_URL", "\"https://dev-api.example.com\"")
        }
        
        getByName("staging") {
            applicationIdSuffix = ".staging"
            versionNameSuffix = "-staging"
            buildConfigField("String", "API_URL", "\"https://staging-api.example.com\"")
        }
        
        getByName("production") {
            buildConfigField("String", "API_URL", "\"https://api.example.com\"")
        }
        
        getByName("free") {
            applicationIdSuffix = ".free"
            buildConfigField("Boolean", "PREMIUM_FEATURES", "false")
        }
        
        getByName("paid") {
            buildConfigField("Boolean", "PREMIUM_FEATURES", "true")
        }
    }
}
```

**Build variants created:**

- `devFreeDebug`, `devFreeRelease`
- `devPaidDebug`, `devPaidRelease`
- `stagingFreeDebug`, `stagingFreeRelease`
- `stagingPaidDebug`, `stagingPaidRelease`
- `productionFreeDebug`, `productionFreeRelease`
- `productionPaidDebug`, `productionPaidRelease`

**Build specific variant:**

```bash
./gradlew assembleDevFreeDebug
./gradlew installProductionPaidRelease
```

---

## Best Practices

### Testing Strategy

**Layer-based approach:**

1. **Unit Tests** (`grapla.android.unit.test`)
    - ViewModels
    - Use cases
    - Repositories
    - Data mappers

2. **Instrumented Tests** (`grapla.android.instrumented.test`)
    - Database operations (Room)
    - Complex UI interactions
    - Integration tests

3. **Compose Tests** (`grapla.android.compose.test`)
    - Compose UI components
    - Screen-level tests
    - Navigation flows

### Coverage Goals

- **Minimum coverage**: 80% overall
- **Critical modules**: 90%+ (repositories, use cases)
- **UI modules**: 70%+ (compose screens, fragments)

### Quality Checks

**Run before committing:**

```bash
./gradlew detekt                    # Code quality
./gradlew test                      # Unit tests
./gradlew koverVerify              # Coverage check
```

**CI/CD pipeline:**

```bash
./gradlew clean build               # Full build
./gradlew detekt                    # Static analysis
./gradlew test                      # All tests
./gradlew koverXmlReport           # Coverage report
./gradlew connectedAndroidTest     # Instrumented tests
```

### Plugin Combinations

**Feature module (recommended):**

```kotlin
plugins {
    id("grapla.android.feature")
    id("grapla.android.library.compose")
    id("grapla.android.unit.test")
    id("grapla.android.compose.test")
    id("grapla.kover")
    id("grapla.detekt")
}
```

**Data module:**

```kotlin
plugins {
    id("grapla.android.library")
    id("grapla.android.room")
    id("grapla.hilt")
    id("grapla.android.unit.test")
    id("grapla.android.instrumented.test")
    id("grapla.jacoco")
    id("grapla.detekt")
}
```

**UI module:**

```kotlin
plugins {
    id("grapla.android.library")
    id("grapla.android.library.compose")
    id("grapla.android.unit.test")
    id("grapla.android.compose.test")
    id("grapla.kover")
}
```

---

## Troubleshooting

### Kover Issues

**Issue**: Kover reports 0% coverage
**Solution**: Ensure tests are actually running and instrumentation is not disabled

```kotlin
kover {
    currentProject {
        instrumentation {
            disableForAll = false  // Make sure this is false
        }
    }
}
```

### JaCoCo Issues

**Issue**: JaCoCo execution data not found
**Solution**: Run tests before generating report

```bash
./gradlew test jacocoTestReport
```

### Detekt Issues

**Issue**: Too many issues reported
**Solution**: Create a baseline for existing issues

```bash
./gradlew detektBaseline
```

### Flavor Issues

**Issue**: Too many build variants
**Solution**: Reduce flavor dimensions or use variant filters

```kotlin
android {
    variantFilter {
        if (name.contains("stagingPaid")) {
            ignore = true
        }
    }
}
```

---

## Additional Resources

- [Kover Documentation](https://kotlin.github.io/kotlinx-kover/)
- [JaCoCo Documentation](https://www.jacoco.org/jacoco/trunk/doc/)
- [Detekt Documentation](https://detekt.dev/)
- [Android Testing Guide](https://developer.android.com/training/testing)
- [Compose Testing](https://developer.android.com/jetpack/compose/testing)
