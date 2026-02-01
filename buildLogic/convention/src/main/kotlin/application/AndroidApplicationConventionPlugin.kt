package application

import com.android.build.api.dsl.ApplicationExtension
import util.BuildLogicConstants
import util.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

/**
 * Convention plugin for configuring Android application modules.
 *
 * This plugin sets up an Android application project with the necessary plugins and configurations,
 * including Kotlin support, dependency management, and specific Android settings.
 *
 * The `AndroidApplicationConventionPlugin` class implements the `Plugin<Project>` interface in Gradle,
 * which allows it to be applied to a Gradle project.
 *
 * This plugin is designed to configure Android application projects with specific settings and plugins.
 *
 * Apply this plugin to your app module:
 * ```kotlin
 * plugins {
 *     id("grapla.android.application")
 * }
 * ```
 */
class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("com.dropbox.dependency-guard")
                apply("grapla.android.lint")
            }

            extensions.configure<ApplicationExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = BuildLogicConstants.TARGET_SDK
                defaultConfig.minSdk = BuildLogicConstants.MIN_SDK
                // Disable animations for faster and more reliable tests
                testOptions.animationsDisabled = true
                // Enable JUnit 6 for unit tests
                testOptions.unitTests.all {
                    it.useJUnitPlatform()
                }
            }
        }
    }
}
