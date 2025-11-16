package testing

import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin
import util.libs

/**
 * Convention plugin for configuring Android unit tests.
 *
 * This plugin sets up unit testing for Android modules, including:
 * - JUnit 6 configuration (modern testing framework)
 * - Kotlin test dependencies
 * - Coroutines test support
 * - Truth assertion library
 * - Turbine for Flow testing
 *
 * Note: This configures UNIT tests (testImplementation) to use JUnit 6.
 * Instrumented tests (androidTestImplementation) still require JUnit 4.
 *
 * Apply this plugin to your module:
 * ```kotlin
 * plugins {
 *     id("grapla.android.unit.test")
 * }
 * ```
 */
class AndroidUnitTestConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            // Configure unit test options based on module type
            pluginManager.withPlugin("com.android.application") {
                extensions.configure<BaseAppModuleExtension> {
                    configureUnitTests()
                }
            }

            pluginManager.withPlugin("com.android.library") {
                extensions.configure<LibraryExtension> {
                    configureUnitTests()
                }
            }

            // Add unit test dependencies
            dependencies {
                // JUnit 6 - modern testing framework (released Oct 2025)
                add("testImplementation", libs.findLibrary("junit6").get())
                add("testRuntimeOnly", libs.findLibrary("junit.platform.launcher").get())

                // Kotlin test support
                add("testImplementation", kotlin("test"))

                // Coroutines test support
                add("testImplementation", libs.findLibrary("kotlinx.coroutines.test").get())

                // Truth - fluent assertion library
                add("testImplementation", libs.findLibrary("truth").get())

                // Turbine - Flow testing library
                add("testImplementation", libs.findLibrary("turbine").get())

                // Robolectric - Android framework testing
                add("testImplementation", libs.findLibrary("robolectric").get())
            }
        }
    }

    /**
     * Configures unit test options for Android modules
     */
    private fun com.android.build.api.dsl.CommonExtension<*, *, *, *, *, *>.configureUnitTests() {
        testOptions {
            // Enable JUnit 6 for unit tests
            unitTests.all {
                it.useJUnitPlatform()
            }

            // Include Android framework classes for unit tests
            unitTests.isIncludeAndroidResources = true

            // Return default values for all resources instead of throwing exceptions
            unitTests.isReturnDefaultValues = true
        }
    }
}
