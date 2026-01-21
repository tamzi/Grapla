package library

import com.android.build.api.dsl.LibraryExtension
import util.configureAndroidCompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.getByType

/**
 * Convention plugin for configuring Jetpack Compose in Android library modules.
 *
 * This plugin applies Compose-specific configurations to an Android library,
 * including Compose compiler settings and required dependencies.
 *
 * Apply this plugin to your library module:
 * ```kotlin
 * plugins {
 *     id("grapla.android.library.compose")
 * }
 * ```
 */
class AndroidLibraryComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "com.android.library")
            apply(plugin = "org.jetbrains.kotlin.plugin.compose")

            val extension = extensions.getByType<LibraryExtension>()
            configureAndroidCompose(extension)
        }
    }
}
