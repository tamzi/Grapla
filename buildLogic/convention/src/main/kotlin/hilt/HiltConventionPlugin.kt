package hilt

import util.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * Convention plugin for configuring Hilt dependency injection.
 *
 * This plugin sets up a project with the necessary Hilt plugins and dependencies,
 * including KSP support and specific configurations for Android modules.
 *
 * Apply this plugin to enable Hilt:
 * ```kotlin
 * plugins {
 *     id("grapla.hilt")
 * }
 * ```
 */
class HiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.google.devtools.ksp")
            dependencies {
                add("ksp", libs.findLibrary("hilt.compiler").get())
                add("implementation", libs.findLibrary("hilt.core").get())
            }

            /** Add support for Android modules */
            pluginManager.withPlugin("com.android.base") {
                pluginManager.apply("dagger.hilt.android.plugin")
                dependencies {
                    add("implementation", libs.findLibrary("hilt.android").get())
                }
            }
        }
    }
}
