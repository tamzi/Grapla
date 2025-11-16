# Centralized Build Configuration

## Overview

All build configuration constants are now centralized in `BuildLogicConstants.kt` to ensure
consistency across the project and make updates easier.

## Location

**File:** `buildLogic/convention/src/main/kotlin/util/BuildLogicConstants.kt`

## Available Constants

### SDK Versions

- **`COMPILE_SDK`** (36): The highest Android API level your app is designed to work with
- **`TARGET_SDK`** (36): The highest Android API level your app has been tested with
- **`MIN_SDK`** (28): The lowest Android API level your app supports (Android 9+)
- **`BUILD_TOOLS`** ("36.0.0"): Build tools version

### Java/Kotlin Configuration

- **`JAVA_VERSION`** ("21"): Java version for compilation
- **`KOTLIN_JVM_TARGET`** ("21"): Kotlin JVM target version

### Test Configuration

- **`TEST_INSTRUMENTATION_RUNNER`** ("androidx.test.runner.AndroidJUnitRunner"): Default test runner
  for Android instrumented tests

**Note:** Test animations are always disabled (`animationsDisabled = true`) across all test
configurations for faster and more reliable test execution. This is hardcoded directly in each
plugin rather than as a constant since it should always be `true` and doesn't need to be
configurable.

## Usage

All convention plugins now reference these constants instead of hardcoded values:

```kotlin
import util.BuildLogicConstants

// In convention plugins
defaultConfig.targetSdk = BuildLogicConstants.TARGET_SDK
defaultConfig.minSdk = BuildLogicConstants.MIN_SDK
testInstrumentationRunner = BuildLogicConstants.TEST_INSTRUMENTATION_RUNNER
// Animations always disabled for tests
testOptions.animationsDisabled = true
```

## Updated Convention Plugins

The following convention plugins now use centralized constants:

1. **AndroidApplicationConventionPlugin** - Uses `TARGET_SDK`, `MIN_SDK`
2. **AndroidLibraryConventionPlugin** - Uses `TARGET_SDK`, `TEST_INSTRUMENTATION_RUNNER`
3. **AndroidTestConventionPlugin** - Uses `TARGET_SDK`
4. **AndroidFeatureConventionPlugin** - Disables test animations (hardcoded `true`)
5. **AndroidInstrumentedTestConventionPlugin** - Uses `TEST_INSTRUMENTATION_RUNNER`
6. **AndroidComposeTestConventionPlugin** - Disables test animations (hardcoded `true`)
7. **KotlinAndroid.kt** - Uses `COMPILE_SDK`, `MIN_SDK`, `JAVA_VERSION`, `KOTLIN_JVM_TARGET`

## Benefits

✅ **Single Source of Truth**: All SDK versions and build settings in one place  
✅ **Easy Updates**: Change SDK versions once, applies everywhere  
✅ **Consistency**: No risk of mismatched versions across modules  
✅ **Documentation**: Clear comments explain each constant's purpose  
✅ **Type Safety**: Compile-time checks ensure correctness

## How to Update SDK Versions

To update SDK versions for the entire project:

1. Open `buildLogic/convention/src/main/kotlin/util/BuildLogicConstants.kt`
2. Update the desired constant(s)
3. Rebuild the project

That's it! All modules will automatically use the new values.

## Example: Updating to Android API 37

```kotlin
object BuildLogicConstants {
    const val COMPILE_SDK = 37  // Changed from 36
    const val TARGET_SDK = 37   // Changed from 36
    const val MIN_SDK = 28      // Unchanged
    // ... rest of constants
}
```

## Migration Complete

All hardcoded SDK versions and test configurations have been replaced with references to
`BuildLogicConstants`. No more scattered references throughout the codebase!
