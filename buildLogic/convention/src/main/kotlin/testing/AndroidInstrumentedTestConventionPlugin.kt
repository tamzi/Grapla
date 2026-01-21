package testing

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin
import util.BuildLogicConstants
import util.libs

/**
 * Convention plugin for configuring Android instrumented tests.
 *
 * This plugin sets up instrumented testing for Android modules, including:
 * - AndroidJUnit4 test runner (JUnit 4 - required by Android, not JUnit 6)
 * - Espresso for UI testing
 * - AndroidX Test libraries
 * - Hilt testing support
 * - UI Automator for complex UI interactions
 *
 * Note: Android instrumented tests must use JUnit 4 via AndroidJUnitRunner.
 * JUnit 5/6 is not yet officially supported by AndroidX for instrumented tests.
 * Unit tests (testImplementation) use JUnit 6.
 *
 * Apply this plugin to your module:
 * ```kotlin
 * plugins {
 *     id("grapla.android.instrumented.test")
 * }
 * ```
 */
class AndroidInstrumentedTestConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            // Configure instrumented test options based on module type
            pluginManager.withPlugin("com.android.application") {
                extensions.configure<ApplicationExtension> {
                    defaultConfig {
                        testInstrumentationRunner = BuildLogicConstants.TEST_INSTRUMENTATION_RUNNER
                    }
                    testOptions {
                        // Disable animations for faster and more reliable tests
                        animationsDisabled = true
                        // Emulator snapshots for faster test execution
                        execution = "ANDROIDX_TEST_ORCHESTRATOR"
                    }
                }
            }

            pluginManager.withPlugin("com.android.library") {
                extensions.configure<LibraryExtension> {
                    defaultConfig {
                        testInstrumentationRunner = BuildLogicConstants.TEST_INSTRUMENTATION_RUNNER
                    }
                    testOptions {
                        // Disable animations for faster and more reliable tests
                        animationsDisabled = true
                        // Emulator snapshots for faster test execution
                        execution = "ANDROIDX_TEST_ORCHESTRATOR"
                    }
                }
            }

            // Add instrumented test dependencies
            dependencies {
                // AndroidX Test - Core testing framework
                add("androidTestImplementation", libs.findLibrary("androidx.test.core").get())
                add("androidTestImplementation", libs.findLibrary("androidx.test.runner").get())
                add("androidTestImplementation", libs.findLibrary("androidx.test.rules").get())
                add("androidTestImplementation", libs.findLibrary("androidx.test.ext").get())

                // Kotlin test support
                add("androidTestImplementation", kotlin("test"))

                // JUnit 4 (required for Android instrumented tests - JUnit 6 not supported yet)
                add("androidTestImplementation", libs.findLibrary("androidx.junit").get())

                // Espresso - UI testing
                add(
                    "androidTestImplementation",
                    libs.findLibrary("androidx.test.espresso.core").get(),
                )

                // Coroutines test support
                add("androidTestImplementation", libs.findLibrary("kotlinx.coroutines.test").get())

                // Truth - fluent assertion library
                add("androidTestImplementation", libs.findLibrary("truth").get())

                // UI Automator - for complex UI interactions
                add(
                    "androidTestImplementation",
                    libs.findLibrary("androidx.test.uiautomator").get(),
                )

                // Hilt testing support (if Hilt plugin is applied)
                pluginManager.withPlugin("grapla.hilt") {
                    add("androidTestImplementation", libs.findLibrary("hilt.android.testing").get())
                    // Note: hilt-compiler should already be added by the hilt plugin
                }
            }
        }
    }
}
