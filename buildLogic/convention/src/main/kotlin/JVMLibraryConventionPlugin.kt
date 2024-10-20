import com.tamzi.grapla.configureKotlinJvm
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 *
 * The `JvmLibraryConventionPlugin` class implements the `Plugin<Project>` interface in Gradle,
 * allowing it to be applied to a Gradle project. This plugin is designed to configure JVM library
 * projects with specific settings and dependencies.
 *
 * The `apply` method is overridden to define the plugin's behavior when it is applied to a project.
 * Inside this method, the `with(target)` block is used to operate on the `Project` instance passed as the `target` parameter.
 *
 * ```
 * class JvmLibraryConventionPlugin : Plugin<Project> {
 *     override fun apply(target: Project) {
 *         with(target) {
 *             // Configuration code
 *         }
 *     }
 * }
 * ```
 *
 * Within the `with(target)` block, the `pluginManager` is used to apply two plugins:
 * `org.jetbrains.kotlin.jvm` for Kotlin JVM support and `grapla.android.lint` for linting.
 *
 * ```
 * with(pluginManager) {
 *     apply("org.jetbrains.kotlin.jvm")
 *     apply("grapla.android.lint")
 * }
 * ```
 *
 * Finally, the `configureKotlinJvm` function is called to apply Kotlin-specific configurations to
 * the JVM project.
 *
 * ```
 * configureKotlinJvm()
 * ```
 *
 * In summary, this plugin sets up a JVM library project with the necessary plugins and configurations,
 * including Kotlin JVM support and linting.
 *
 *
 * */

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
