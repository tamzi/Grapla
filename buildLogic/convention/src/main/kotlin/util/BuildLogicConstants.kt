package util

/**
 * Centralized build configuration constants for the project.
 * This object contains all SDK versions, build settings, and common configurations
 * used across all modules to ensure consistency.
 *
 * To modify SDK versions or other build settings, update values here rather than
 * in individual convention plugins or build files.
 */
object BuildLogicConstants {
    /**
     * Compile SDK version - the highest Android API level your app is designed to work with
     */
    const val COMPILE_SDK = 36

    /**
     * Target SDK version - the highest Android API level your app has been tested with
     */
    const val TARGET_SDK = 36

    /**
     * Minimum SDK version - the lowest Android API level your app supports
     * Set to 28 (Android 9) for broader compatibility
     */
    const val MIN_SDK = 28

    /**
     * Build tools version
     */
    const val BUILD_TOOLS = "36.0.0"

    /**
     * Java version for compilation
     */
    const val JAVA_VERSION = "21"

    /**
     * Kotlin JVM target version
     */
    const val KOTLIN_JVM_TARGET = "21"

    /**
     * Default test instrumentation runner for Android tests
     */
    const val TEST_INSTRUMENTATION_RUNNER = "androidx.test.runner.AndroidJUnitRunner"
}
