import com.android.build.api.variant.LibraryAndroidComponentsExtension
import com.android.build.gradle.LibraryExtension
import com.tamzi.grapla.configureKotlinAndroid
import com.tamzi.grapla.disableUnnecessaryAndroidTests
import com.tamzi.grapla.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin


/**
 * This plugin sets up an Android library project with the necessary plugins and configurations,
 * including Kotlin support, linting, specific Android settings, and dependencies.
 *
 * The `AndroidLibraryConventionPlugin` class implements the `Plugin<Project>` interface in Gradle,
 * allowing it to be applied to a Gradle project. This plugin is designed to configure Android
 * library projects with specific settings and dependencies.
 *
 * The `apply` method is overridden to define the plugin's behavior when it is applied to a project.
 * Inside this method, the `with(target)` block is used to operate on the `Project` instance passed
 * as the `target` parameter.
 *
 * ```
 * class AndroidLibraryConventionPlugin : Plugin<Project> {
 *     override fun apply(target: Project) {
 *         with(target) {
 *             // Configuration code
 *         }
 *     }
 * }
 * ```
 *
 * Within the `with(target)` block, the `pluginManager` is used to apply several plugins to the project. These plugins include `com.android.library` for Android library support, `org.jetbrains.kotlin.android` for Kotlin support, and `grapla.android.lint` for linting.
 *
 * ```
 * with(pluginManager) {
 *     apply("com.android.library")
 *     apply("org.jetbrains.kotlin.android")
 *     apply("grapla.android.lint")
 * }
 * ```
 *
 * Next, the `extensions.configure<LibraryExtension>` method is used to configure the `LibraryExtension` instance. This extension provides access to Android-specific configuration options. The `configureKotlinAndroid` function is called to apply Kotlin-specific configurations to the Android project. The `targetSdk` version is set to 34, and the `testInstrumentationRunner` is set to `androidx.test.runner.AndroidJUnitRunner`. Animations are disabled in test options to speed up tests. Additionally, a resource prefix is derived from the module name to ensure consistent resource naming.
 *
 * ```
 * extensions.configure<LibraryExtension> {
 *     configureKotlinAndroid(this)
 *     defaultConfig.targetSdk = 34
 *     defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
 *     testOptions.animationsDisabled = true
 *     resourcePrefix = path.split("""\W""".toRegex()).drop(1).distinct().joinToString(separator = "_").lowercase() + "_"
 * }
 * ```
 *
 * The `extensions.configure<LibraryAndroidComponentsExtension>` method is used to configure the `LibraryAndroidComponentsExtension` instance. The `disableUnnecessaryAndroidTests` function is called to disable unnecessary Android tests for the project.
 *
 * ```
 * extensions.configure<LibraryAndroidComponentsExtension> {
 *     disableUnnecessaryAndroidTests(target)
 * }
 * ```
 *
 * The `dependencies` block is used to add various dependencies to the project. It includes dependencies for Android tests and an implementation dependency for `androidx.tracing.ktx`.
 *
 * ```
 * dependencies {
 *     add("androidTestImplementation", kotlin("test"))
 *     add("testImplementation", kotlin("test"))
 *     add("implementation", libs.findLibrary("androidx.tracing.ktx").get())
 * }
 * ```
 *
 * */


class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
                apply("grapla.android.lint")
            }

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = 34
                defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                testOptions.animationsDisabled = true
                // The resource prefix is derived from the module name,
                // so resources inside ":core:module1" must be prefixed with "core_module1_"
                resourcePrefix = path.split("""\W""".toRegex()).drop(1).distinct().joinToString(separator = "_").lowercase() + "_"
            }
            extensions.configure<LibraryAndroidComponentsExtension> {
                disableUnnecessaryAndroidTests(target)
            }
            dependencies {
                add("androidTestImplementation", kotlin("test"))
                add("testImplementation", kotlin("test"))

                add("implementation", libs.findLibrary("androidx.tracing.ktx").get())
            }
        }
    }
}
