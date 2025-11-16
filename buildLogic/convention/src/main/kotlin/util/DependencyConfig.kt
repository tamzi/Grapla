package util

/**
 * Centralized build configuration
 *
 * This object contains configuration for build behavior that is actually used by convention plugins.
 *
 * **Philosophy:**
 * - Keep it simple: Only configure what needs to be configurable
 * - Core dependencies are always included (not optional)
 * - Focus on compiler settings and optional features
 */
object DependencyConfig {

    /**
     * Compose compiler configuration
     */
    object Compose {
        /**
         * Path to Compose stability configuration file
         *
         * This file helps the Compose compiler understand which classes are stable,
         * allowing for better recomposition optimizations.
         *
         * **Used by:** `AndroidCompose.kt`
         * **Default:** "compose_compiler_config.conf"
         */
        const val STABILITY_CONFIG_FILE = "compose_compiler_config.conf"

        // Note: Strong skipping mode is enabled by default in newer Compose versions
        // No need to configure it explicitly
    }
}
