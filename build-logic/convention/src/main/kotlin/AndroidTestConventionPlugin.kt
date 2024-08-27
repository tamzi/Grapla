import com.android.build.gradle.TestExtension
import com.tamzi.grapla.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure


/**
 *
 * The `AndroidTestConventionPlugin` class implements the `Plugin<Project>` interface in Gradle,
 * allowing it to be applied to a Gradle project. This plugin is designed to configure Android test
 * projects with specific settings and dependencies.
 *
 * The `apply` method is overridden to define the plugin's behavior when it is applied to a project.
 * Inside this method, the `with(target)` block is used to operate on the `Project` instance passed
 * as the `target` parameter.
 *
 * ```
 * class AndroidTestConventionPlugin : Plugin<Project> {
 *     override fun apply(target: Project) {
 *         with(target) {
 *             // Configuration code
 *         }
 *     }
 * }
 * ```
 *
 * Within the `with(target)` block, the `pluginManager` is used to apply two plugins:
 * `com.android.test` for Android test support and `org.jetbrains.kotlin.android` for Kotlin support.
 *
 * ```
 * with(pluginManager) {
 *     apply("com.android.test")
 *     apply("org.jetbrains.kotlin.android")
 * }
 * ```
 *
 * Next, the `extensions.configure<TestExtension>` method is used to configure the `TestExtension`
 * instance. This configuration calls the `configureKotlinAndroid` function to apply Kotlin-specific
 * configurations to the Android test project. Additionally, the `targetSdk` version is set to 34.
 *
 * ```
 * extensions.configure<TestExtension> {
 *     configureKotlinAndroid(this)
 *     defaultConfig.targetSdk = 34
 * }
 * ```
 *
 * In summary, this plugin sets up an Android test project with the necessary plugins and
 * configurations, including Kotlin support and specific Android settings.
 *
 *
 * */

class AndroidTestConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.test")
                apply("org.jetbrains.kotlin.android")
            }

            extensions.configure<TestExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = 34
            }
        }
    }

}
