package test

import com.android.build.gradle.TestExtension
import util.BuildLogicConstants
import util.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

/**
 * Convention plugin for configuring Android test modules.
 *
 * This plugin is designed to configure Android test projects with
 * specific settings and dependencies.
 *
 * Apply this plugin to your test module:
 * ```kotlin
 * plugins {
 *     id("grapla.android.test")
 * }
 * ```
 */
class AndroidTestConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.test")
                apply("org.jetbrains.kotlin.android")
            }

            extensions.configure<TestExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = BuildLogicConstants.TARGET_SDK
            }
        }
    }
}
