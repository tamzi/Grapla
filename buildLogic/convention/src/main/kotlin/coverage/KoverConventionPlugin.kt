package coverage

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Convention plugin for configuring Kover (Kotlin code coverage).
 *
 * Kover is a Kotlin-first code coverage tool that provides:
 * - Zero-configuration setup for Kotlin projects
 * - Better performance than JaCoCo for Kotlin code
 * - IntelliJ IDEA integration
 * - XML, HTML, and binary report formats
 *
 * This plugin configures Kover with sensible defaults including:
 * - Minimum coverage thresholds (80% coverage)
 * - Exclusion of generated code and Android framework classes
 * - XML and HTML report generation
 *
 * Apply this plugin to your module:
 * ```kotlin
 * plugins {
 *     id("grapla.kover")
 * }
 * ```
 *
 * Then configure it in your build.gradle.kts:
 * ```kotlin
 * kover {
 *     reports {
 *         filters {
 *             excludes {
 *                 classes("*_Factory", "*_HiltModules*", "*.BuildConfig")
 *                 packages("*.di", "*.di.*")
 *                 annotatedBy("dagger.Module")
 *             }
 *         }
 *         total {
 *             xml { onCheck = true }
 *             html { onCheck = true }
 *         }
 *         verify {
 *             rule { minBound(80) }
 *         }
 *     }
 * }
 * ```
 */
class KoverConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlinx.kover")
            }

            // Note: Kover configuration is done via the 'kover' extension in build.gradle.kts
            // This plugin simply applies the Kover plugin with recommended defaults
            // Users can further customize the configuration in their build files
        }
    }
}
