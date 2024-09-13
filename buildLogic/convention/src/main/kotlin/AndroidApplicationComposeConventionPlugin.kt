import com.android.build.api.dsl.ApplicationExtension
import com.tamzi.grapla.configureAndroidCompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.getByType

/**
 * This plugin is specifically designed to configure Android applications that use Jetpack Compose.
 *
 * This plugin sets up an Android application project with the necessary plugins and configurations
 * to support Jetpack Compose, making it easier to develop modern Android UIs.
 *
 * The `AndroidApplicationComposeConventionPlugin` class implements
 * the `Plugin<Project>` interface in Gradle, which allows it to be applied to a Gradle project.
 *
 * The `apply` method is overridden to define the plugin's behavior when it is applied to a project.
 * Inside this method, the `with(target)` block is used to operate on the `Project` instance
 * passed as the `target` parameter.
 *
 *  `class AndroidApplicationComposeConventionPlugin : Plugin<Project> {
 *     override fun apply(target: Project) {
 *         with(target) {
 *             // Plugin application and configuration code
 *          }
 *         }
 *      }`
 *
 *   Within the `with(target)` block, the `apply` method is called twice to apply
 *   the `com.android.application` and `org.jetbrains.kotlin.plugin.compose` plugins to the project.
 *   These plugins are essential for building Android applications and enabling
 *   Jetpack Compose support, respectively.
 *
 *   `apply(plugin = "com.android.application")
 *    apply(plugin = "org.jetbrains.kotlin.plugin.compose")`
 *
 *  Next, the `extensions.getByType<ApplicationExtension>()` method is used to retrieve
 *  the `ApplicationExtension` instance from the project's extensions.
 *  This extension provides access to Android-specific configuration options.
 *
 *  `val extension = extensions.getByType<ApplicationExtension>()`
 *
 *  Finally, the `configureAndroidCompose(extension)` function is called with the retrieved
 *  `ApplicationExtension` instance. This function is responsible for configuring the Android
 *  application to support Jetpack Compose, although its implementation is not
 *  shown in the provided code.
 *
 *  `configureAndroidCompose(extension)`
 *
 * */

class AndroidApplicationComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "com.android.application")
            apply(plugin = "org.jetbrains.kotlin.plugin.compose")
            apply(plugin = "org.jetbrains.kotlin.android")

            val extension = extensions.getByType<ApplicationExtension>()
            configureAndroidCompose(extension)
        }
    }
}
