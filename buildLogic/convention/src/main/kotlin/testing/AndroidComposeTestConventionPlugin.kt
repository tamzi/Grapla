package testing

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import util.libs

/**
 * Convention plugin for configuring Jetpack Compose UI tests.
 *
 * This plugin sets up Compose UI testing for Android modules, including:
 * - Compose UI test dependencies
 * - Test manifest configuration
 * - Debug tooling support
 *
 * Apply this plugin to your module with Compose:
 * ```kotlin
 * plugins {
 *     id("grapla.android.compose.test")
 * }
 * ```
 */
class AndroidComposeTestConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            // Configure Compose test options based on module type
            pluginManager.withPlugin("com.android.application") {
                extensions.configure<ApplicationExtension> {
                    buildFeatures {
                        compose = true
                    }
                    // Disable animations for faster and more reliable tests
                    testOptions {
                        animationsDisabled = true
                    }
                }
            }

            pluginManager.withPlugin("com.android.library") {
                extensions.configure<LibraryExtension> {
                    buildFeatures {
                        compose = true
                    }
                    // Disable animations for faster and more reliable tests
                    testOptions {
                        animationsDisabled = true
                    }
                }
            }

            // Add Compose test dependencies
            dependencies {
                // Compose UI Test dependencies
                add("androidTestImplementation", libs.findLibrary("androidx.compose.ui.test").get())

                // Debug dependencies for Compose testing
                add(
                    "debugImplementation",
                    libs.findLibrary("androidx.compose.ui.testManifest").get(),
                )
                add("debugImplementation", libs.findLibrary("androidx.compose.ui.tooling").get())

                // Navigation testing for Compose
                add(
                    "androidTestImplementation",
                    libs.findLibrary("androidx.navigation.testing").get(),
                )
            }
        }
    }
}
