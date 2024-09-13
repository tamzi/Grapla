import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import com.android.build.api.dsl.Lint
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure


/**
 *
 * This plugin configures linting for Android projects by checking which Android plugin is applied
 * and setting up the appropriate lint configuration.
 * It ensures that lint reports are generated in XML format and that dependencies are checked.
 *
 * The `AndroidLintConventionPlugin` class implements the `Plugin<Project>` interface in Gradle,
 * allowing it to be applied to a Gradle project. This plugin is designed to configure linting for
 * Android projects, whether they are application or library modules.
 *
 * The `apply` method is overridden to define the plugin's behavior when it is applied to a project.
 * Inside this method, the `with(target)` block is used to operate on the `Project` instance passed
 * as the `target` parameter. The `when` expression is used to check which Android plugin is
 * applied to the project.
 *
 * ```
 * class AndroidLintConventionPlugin : Plugin<Project> {
 *     override fun apply(target: Project) {
 *         with(target) {
 *             when {
 *                 pluginManager.hasPlugin("com.android.application") ->
 *                     configure<ApplicationExtension> { lint(Lint::configure) }
 *
 *                 pluginManager.hasPlugin("com.android.library") ->
 *                     configure<LibraryExtension> { lint(Lint::configure) }
 *
 *                 else -> {
 *                     pluginManager.apply("com.android.lint")
 *                     configure<Lint>(Lint::configure)
 *                 }
 *             }
 *         }
 *     }
 * }
 * ```
 *
 * If the `com.android.application` plugin is applied, the `ApplicationExtension` is
 * configured to use the custom lint configuration. Similarly, if the `com.android.library` plugin
 * is applied, the `LibraryExtension` is configured. If neither plugin is present,
 * the `com.android.lint` plugin is applied, and the `Lint` configuration is set up.
 *
 * The `Lint.configure` extension function is defined to set specific lint options.
 * It enables XML reporting and checks dependencies.
 *
 * ```
 * private fun Lint.configure() {
 *     xmlReport = true
 *     checkDependencies = true
 * }
 * ```
 *
 * */

class AndroidLintConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            when {
                pluginManager.hasPlugin("com.android.application") ->
                    configure<ApplicationExtension> { lint(Lint::configure) }

                pluginManager.hasPlugin("com.android.library") ->
                    configure<LibraryExtension> { lint(Lint::configure) }

                else -> {
                    pluginManager.apply("com.android.lint")
                    configure<Lint>(Lint::configure)
                }
            }
        }
    }
}

private fun Lint.configure() {
    xmlReport = true
    checkDependencies = true
}

