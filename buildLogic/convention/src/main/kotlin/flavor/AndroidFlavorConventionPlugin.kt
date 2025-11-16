package flavor

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

/**
 * Convention plugin for configuring Android build flavors.
 *
 * Build flavors allow you to create different versions of your app from a single codebase.
 * Common use cases include:
 * - Free vs. Paid versions
 * - Development vs. Production environments
 * - Different API endpoints
 * - Feature toggling
 *
 * This plugin configures three standard flavor dimensions:
 * 1. **environment** - dev, staging, production
 * 2. **version** - free, paid
 * 3. **api** - mock, real
 *
 * The resulting build variants combine flavors from each dimension with build types.
 * For example: `devFreeDebug`, `productionPaidRelease`, etc.
 *
 * Apply this plugin to your app or library module:
 * ```kotlin
 * plugins {
 *     id("grapla.android.flavor")
 * }
 * ```
 *
 * Customize flavors in your build.gradle.kts:
 * ```kotlin
 * android {
 *     flavorDimensions += listOf("environment", "version")
 *
 *     productFlavors {
 *         create("dev") {
 *             dimension = "environment"
 *             applicationIdSuffix = ".dev"
 *             versionNameSuffix = "-dev"
 *         }
 *         create("production") {
 *             dimension = "environment"
 *         }
 *     }
 * }
 * ```
 */
class AndroidFlavorConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            // Configure flavors for application modules
            pluginManager.withPlugin("com.android.application") {
                extensions.configure<ApplicationExtension> {
                    configureFlavorDimensions()
                    configureProductFlavors()
                }
            }

            // Configure flavors for library modules
            pluginManager.withPlugin("com.android.library") {
                extensions.configure<LibraryExtension> {
                    configureFlavorDimensions()
                    configureProductFlavors()
                }
            }
        }
    }

    /**
     * Configures flavor dimensions for the project
     */
    private fun com.android.build.api.dsl.CommonExtension<*, *, *, *, *, *>.configureFlavorDimensions() {
        flavorDimensions += listOf(
            DIMENSION_ENVIRONMENT,
            DIMENSION_VERSION,
        )
    }

    /**
     * Configures product flavors for each dimension
     */
    private fun com.android.build.api.dsl.CommonExtension<*, *, *, *, *, *>.configureProductFlavors() {
        productFlavors {
            // Environment flavors
            create(FLAVOR_DEV) {
                dimension = DIMENSION_ENVIRONMENT
                // Add dev-specific configuration in the app module
                // e.g., applicationIdSuffix = ".dev"
                // e.g., versionNameSuffix = "-dev"
            }

            create(FLAVOR_STAGING) {
                dimension = DIMENSION_ENVIRONMENT
                // Add staging-specific configuration in the app module
                // e.g., applicationIdSuffix = ".staging"
                // e.g., versionNameSuffix = "-staging"
            }

            create(FLAVOR_PRODUCTION) {
                dimension = DIMENSION_ENVIRONMENT
                // Production is the default, no suffix needed
            }

            // Version flavors
            create(FLAVOR_FREE) {
                dimension = DIMENSION_VERSION
                // Add free version-specific configuration
                // e.g., applicationIdSuffix = ".free"
            }

            create(FLAVOR_PAID) {
                dimension = DIMENSION_VERSION
                // Add paid version-specific configuration
            }
        }
    }

    companion object {
        // Flavor dimensions
        private const val DIMENSION_ENVIRONMENT = "environment"
        private const val DIMENSION_VERSION = "version"

        // Flavor names - Environment
        private const val FLAVOR_DEV = "dev"
        private const val FLAVOR_STAGING = "staging"
        private const val FLAVOR_PRODUCTION = "production"

        // Flavor names - Version
        private const val FLAVOR_FREE = "free"
        private const val FLAVOR_PAID = "paid"
    }
}
