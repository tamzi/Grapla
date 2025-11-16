package jvm

import util.configureKotlinJvm
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Convention plugin for configuring JVM library modules.
 *
 * This plugin is designed to configure JVM library projects with
 * specific settings and dependencies.
 *
 * Apply this plugin to your JVM library module:
 * ```kotlin
 * plugins {
 *     id("grapla.jvm.library")
 * }
 * ```
 */
class JvmLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.jvm")
                apply("grapla.android.lint")
            }
            configureKotlinJvm()
        }
    }
}
