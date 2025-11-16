package feature

import com.android.build.gradle.LibraryExtension
import util.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

/**
 * Convention plugin for configuring Android feature modules.
 *
 * Feature modules are specialized library modules that contain
 * specific features or functionality of the app. They typically
 * depend on core modules and use Hilt for dependency injection.
 *
 * Apply this plugin to your feature module:
 * ```kotlin
 * plugins {
 *     id("grapla.android.feature")
 * }
 * ```
 */
class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("grapla.android.library")
                apply("grapla.hilt")
            }
            extensions.configure<LibraryExtension> {
                // Disable animations for faster and more reliable tests
                testOptions.animationsDisabled = true
            }

            dependencies {
                add("implementation", project(":core:ui"))
                add("implementation", project(":gruid"))

                add("implementation", libs.findLibrary("androidx.hilt.navigation.compose").get())
                add("implementation", libs.findLibrary("androidx.lifecycle.runtimeCompose").get())
                add("implementation", libs.findLibrary("androidx.lifecycle.viewModelCompose").get())
                add("implementation", libs.findLibrary("androidx.tracing.ktx").get())

                add(
                    "androidTestImplementation",
                    libs.findLibrary("androidx.lifecycle.runtimeTesting").get(),
                )
            }
        }
    }
}
