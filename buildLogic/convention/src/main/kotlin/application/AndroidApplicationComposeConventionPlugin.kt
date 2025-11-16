package application

import com.android.build.api.dsl.ApplicationExtension
import util.configureAndroidCompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.getByType

/**
 * Convention plugin for configuring Jetpack Compose in Android application modules.
 *
 * This plugin applies Compose-specific configurations to an Android application,
 * including Compose compiler settings and required dependencies.
 *
 * Apply this plugin to your app module:
 * ```kotlin
 * plugins {
 *     id("grapla.android.application.compose")
 * }
 * ```
 */
class AndroidApplicationComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "com.android.application")
            apply(plugin = "org.jetbrains.kotlin.plugin.compose")

            val extension = extensions.getByType<ApplicationExtension>()
            configureAndroidCompose(extension)
        }
    }
}
