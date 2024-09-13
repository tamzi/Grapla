import com.android.build.gradle.api.AndroidBasePlugin
import com.tamzi.grapla.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies



/**
 *
 * This plugin sets up a project with the necessary Hilt plugins and dependencies,
 * including KSP support and specific configurations for Android modules.
 *
 * The `HiltConventionPlugin` class implements the `Plugin<Project>` interface in Gradle,
 * allowing it to be applied to a Gradle project. This plugin is designed to configure projects
 * with Hilt dependency injection settings and dependencies.
 *
 * The `apply` method is overridden to define the plugin's behavior when it is applied to a project.
 * Inside this method, the `with(target)` block is used to operate on the `Project` instance passed
 * as the `target` parameter.
 *
 * ```
 * class HiltConventionPlugin : Plugin<Project> {
 *     override fun apply(target: Project) {
 *         with(target) {
 *             // Configuration code
 *         }
 *     }
 * }
 * ```
 *
 * Within the `with(target)` block, the `pluginManager` is used to apply the `com.google.devtools.ksp`
 * plugin for Kotlin Symbol Processing (KSP). This is necessary for Hilt's annotation processing.
 *
 * ```
 * pluginManager.apply("com.google.devtools.ksp")
 * ```
 *
 * The `dependencies` block is used to add various Hilt dependencies to the project.
 * It includes the Hilt compiler for KSP and the core Hilt library.
 *
 * ```
 * dependencies {
 *     add("ksp", libs.findLibrary("hilt.compiler").get())
 *     add("implementation", libs.findLibrary("hilt.core").get())
 * }
 * ```
 *
 * Additionally, the `pluginManager.withPlugin("com.android.base")` block checks if the
 * `com.android.base` plugin is applied to the project. If it is, the `dagger.hilt.android.plugin`
 * is applied, and the Hilt Android library is added as a dependency.
 *
 * ```
 * pluginManager.withPlugin("com.android.base") {
 *     pluginManager.apply("dagger.hilt.android.plugin")
 *     dependencies {
 *         add("implementation", libs.findLibrary("hilt.android").get())
 *     }
 * }
 * ```
 *
 * */

class HiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.google.devtools.ksp")
            dependencies {
                add("ksp", libs.findLibrary("hilt.compiler").get())
                add("implementation", libs.findLibrary("hilt.core").get())
            }

            /** Add support for Android modules, based on [AndroidBasePlugin] */
            pluginManager.withPlugin("com.android.base") {
                pluginManager.apply("dagger.hilt.android.plugin")
                dependencies {
                    add("implementation", libs.findLibrary("hilt.android").get())
                }
            }
        }
    }
}
