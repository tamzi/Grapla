package quality

import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType
import util.BuildLogicConstants
import util.libs

/**
 * Convention plugin for configuring Detekt (Kotlin static code analysis).
 *
 * Detekt is a static code analysis tool for Kotlin that provides:
 * - Code smell detection
 * - Code complexity analysis
 * - Customizable rule sets
 * - Automatic code formatting with detekt-formatting
 * - IDE integration
 *
 * This plugin applies Detekt with recommended defaults.
 * Configure it further in your build.gradle.kts:
 * ```kotlin
 * detekt {
 *     parallel = true
 *     buildUponDefaultConfig = true
 *     autoCorrect = true
 *     config.setFrom(files("$rootDir/config/detekt/detekt-config.yml"))
 * }
 *
 * tasks.withType<Detekt>().configureEach {
 *     reports {
 *         xml.required.set(true)
 *         html.required.set(true)
 *         txt.required.set(true)
 *         sarif.required.set(true)
 *         md.required.set(true)
 *     }
 *     jvmTarget = "21"
 * }
 * ```
 *
 * Apply this plugin to your module:
 * ```kotlin
 * plugins {
 *     id("grapla.detekt")
 * }
 * ```
 */
class DetektConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("io.gitlab.arturbosch.detekt")
            }

            // Add Detekt formatting plugin for additional style checks
            dependencies {
                add("detektPlugins", libs.findLibrary("detekt.formatting").get())
            }

            // Configure Detekt extension
            extensions.configure<DetektExtension> {
                // Build upon the default configuration
                buildUponDefaultConfig = true
                // Run detekt in parallel
                parallel = true
                // Use the project-wide config file
                val configFile = rootProject.file("config/detekt/detekt.yml")
                if (configFile.exists()) {
                    config.setFrom(configFile)
                }
            }

            // Configure Detekt tasks to use the correct JVM target
            tasks.withType<Detekt>().configureEach {
                jvmTarget = BuildLogicConstants.KOTLIN_JVM_TARGET
                // Generate all types of reports
                reports {
                    html.required.set(true)
                    xml.required.set(true)
                    txt.required.set(true)
                    sarif.required.set(true)
                    md.required.set(false)
                }
            }

            tasks.withType<DetektCreateBaselineTask>().configureEach {
                jvmTarget = BuildLogicConstants.KOTLIN_JVM_TARGET
            }
        }
    }
}
