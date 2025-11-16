package library

import com.android.build.api.variant.LibraryAndroidComponentsExtension
import com.android.build.gradle.LibraryExtension
import util.BuildLogicConstants
import util.configureKotlinAndroid
import util.disableUnnecessaryAndroidTests
import util.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

/**
 * Convention plugin for configuring Android library modules.
 *
 * This plugin sets up an Android library project with the necessary plugins and configurations,
 * including Kotlin support, linting, specific Android settings, and dependencies.
 *
 * Apply this plugin to your library module:
 * ```kotlin
 * plugins {
 *     id("grapla.android.library")
 * }
 * ```
 */
class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
                apply("grapla.android.lint")
            }

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = BuildLogicConstants.TARGET_SDK
                defaultConfig.testInstrumentationRunner =
                    BuildLogicConstants.TEST_INSTRUMENTATION_RUNNER
                // Disable animations for faster and more reliable tests
                testOptions.animationsDisabled = true
                // Enable JUnit 6 for unit tests
                testOptions.unitTests.all {
                    it.useJUnitPlatform()
                }
                // The resource prefix is derived from the module name,
                // so resources inside ":core:module1" must be prefixed with "core_module1_"
                resourcePrefix = path
                    .split("""\W""".toRegex())
                    .drop(1)
                    .distinct()
                    .joinToString(separator = "_")
                    .lowercase() + "_"
            }
            extensions.configure<LibraryAndroidComponentsExtension> {
                disableUnnecessaryAndroidTests(target)
            }
            dependencies {
                add("androidTestImplementation", kotlin("test"))
                add("testImplementation", kotlin("test"))

                add("implementation", libs.findLibrary("androidx.tracing.ktx").get())
            }
        }
    }
}
