import com.android.build.gradle.LibraryExtension
import com.tamzi.grapla.configureAndroidCompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.getByType

/**
 *
 * This custom plugin simplifies the setup of an Android library project that uses Jetpack Compose
 * by applying the necessary plugins and configuring the project through the
 * `configureAndroidCompose` function.
 *
 * The provided code defines a custom Gradle plugin for an Android library project
 * that uses Jetpack Compose.
 * The plugin is implemented in the `AndroidLibraryComposeConventionPlugin` class, which implements
 * the `Plugin<Project>` interface. This interface requires the implementation of the `apply` method,
 * which is called when the plugin is applied to a project.
 * Within the `apply` method,
 * the `with(target)` block is used to configure the target project.
 *
 * The first two lines within this block apply the `com.android.library` and
 * `org.jetbrains.kotlin.plugin.compose` plugins to the project:
 *
 * `apply(plugin = "com.android.library")
 *  apply(plugin = "org.jetbrains.kotlin.plugin.compose")`
 *
 *  These plugins are essential for setting up an Android library module and enabling Jetpack
 *  Compose support, respectively.  Next, the code retrieves the LibraryExtension from the
 *  project's extensions.
 *  The LibraryExtension is a specialized extension for configuring Android library projects:
 *
 *  `val extension = extensions.getByType<LibraryExtension>()`
 *
 *  Finally, the configureAndroidCompose function is called with the LibraryExtension instance
 *  as an argument. This function is responsible for configuring the Android library project
 *  to support Jetpack Compose:
 *
 *  `configureAndroidCompose(extension)`
 *
 * */

class AndroidLibraryComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "com.android.library")
            apply(plugin = "org.jetbrains.kotlin.plugin.compose")

            val extension = extensions.getByType<LibraryExtension>()
            configureAndroidCompose(extension)
        }
    }

}
