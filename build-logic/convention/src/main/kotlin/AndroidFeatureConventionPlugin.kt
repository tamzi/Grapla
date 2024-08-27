import com.android.build.gradle.LibraryExtension
import com.tamzi.grapla.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies


/**
 *
 *
 * The AndroidFeatureConventionPlugin class implements the Plugin<Project> interface in Gradle,
 * allowing it to be applied to a Gradle project. This plugin is designed to configure
 * Android feature modules with specific settings and dependencies.
 *
 * The apply method is overridden to define the plugin's behavior when it is applied to a project.
 * Inside this method, the with(target) block is used to operate on the Project instance passed as
 * the target parameter.
 *
 *  `class AndroidFeatureConventionPlugin : Plugin<Project> {
 *     override fun apply(target: Project) {
 *         with(target) {
 *             // Configuration code
 *         }
 *     }
 *  }`
 *
 * Within the with(target) block, the pluginManager is used to apply two custom plugins:
 * nowinandroid.android.library and nowinandroid.hilt.
 * These plugins are essential for setting up the Android library and Hilt dependency injection framework
 *
 *  `pluginManager.apply {
 *     apply("grapla.android.library")
 *     apply("grapla.hilt")
 *  }`
 *
 *
 *
 * */

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("grapla.android.library")
                apply("grapla.hilt")
            }
            extensions.configure<LibraryExtension> {
                testOptions.animationsDisabled = true
            }

            dependencies {
                add("implementation", project(":core:ui"))
                add("implementation", project(":core:designsystem"))

                add("implementation", libs.findLibrary("androidx.hilt.navigation.compose").get())
                add("implementation", libs.findLibrary("androidx.lifecycle.runtimeCompose").get())
                add("implementation", libs.findLibrary("androidx.lifecycle.viewModelCompose").get())
                add("implementation", libs.findLibrary("androidx.tracing.ktx").get())

                add("androidTestImplementation", libs.findLibrary("androidx.lifecycle.runtimeTesting").get())
            }
        }
    }
}
