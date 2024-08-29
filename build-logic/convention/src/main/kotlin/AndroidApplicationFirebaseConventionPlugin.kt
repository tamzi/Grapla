import com.android.build.api.dsl.ApplicationExtension
import com.google.firebase.crashlytics.buildtools.gradle.CrashlyticsExtension
import com.tamzi.grapla.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
/**
 *
 * This plugin sets up an Android application project with the necessary Firebase plugins
 * and dependencies, and configures specific settings for Firebase services.
 *
 * The `AndroidApplicationFirebaseConventionPlugin` class implements the `Plugin<Project>`
 * interface in Gradle, allowing it to be applied to a Gradle project.
 *
 * This plugin is designed to configure an Android application project with Firebase services.
 * The `apply` method is overridden to define the plugin's behavior when applied to a project.
 * Inside this method, the `with(target)` block is used to operate on the `Project` instance passed
 * as the `target` parameter.
 *
 *  `class AndroidApplicationFirebaseConventionPlugin : Plugin<Project> {
 *     override fun apply(target: Project) {
 *         with(target) {
 *             // Configuration code
 *         }
 *     }
 *  }`
 *
 *  Within the `with(target)` block, the `pluginManager` is used to apply several Firebase-related
 *  plugins to the project. These plugins include `com.google.gms.google-services` for Google services,
 *  `com.google.firebase.firebase-perf` for Firebase Performance Monitoring,
 *  and `com.google.firebase.crashlytics` for Firebase Crashlytics.
 *
 *  `with(pluginManager) {
 *     apply("com.google.gms.google-services")
 *     apply("com.google.firebase.firebase-perf")
 *     apply("com.google.firebase.crashlytics")
 *  }`
 *
 * Next, the `dependencies` block is used to add Firebase dependencies to the project.
 * The Firebase Bill of Materials (BOM) is added to ensure consistent versions of Firebase libraries.
 *
 * Specific Firebase services such as Analytics, Performance Monitoring, and Crashlytics are also
 * added as dependencies.
 *
 *  `dependencies {
 *     val bom = libs.findLibrary("firebase-bom").get()
 *     add("implementation", platform(bom))
 *     "implementation"(libs.findLibrary("firebase.analytics").get())
 *     "implementation"(libs.findLibrary("firebase.performance").get())
 *     "implementation"(libs.findLibrary("firebase.crashlytics").get())
 *  }`
 *
 *  The `extensions.configure<ApplicationExtension>` method is used to configure the
 *  `ApplicationExtension` instance. Within this block, the `buildTypes.configureEach` method is called
 *  to configure each build type. Specifically, it disables the Crashlytics mapping file
 *  upload by setting `mappingFileUploadEnabled` to `false` in the `CrashlyticsExtension` configuration.
 *  This feature should only be enabled if a Firebase backend is available and configured
 *  in google-services.json.
 *
 *  `extensions.configure<ApplicationExtension> {
 *     buildTypes.configureEach {
 *         configure<CrashlyticsExtension> {
 *             mappingFileUploadEnabled = false
 *         }
 *     }
 *  }`
 *
 *
 *
 * */

class AndroidApplicationFirebaseConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.google.gms.google-services")
                apply("com.google.firebase.firebase-perf")
                apply("com.google.firebase.crashlytics")
            }

            dependencies {
                val bom = libs.findLibrary("firebase-bom").get()
                add("implementation", platform(bom))
                "implementation"(libs.findLibrary("firebase.analytics").get())
                "implementation"(libs.findLibrary("firebase.performance").get())
                "implementation"(libs.findLibrary("firebase.crashlytics").get())
            }

            extensions.configure<ApplicationExtension> {
                buildTypes.configureEach {
                    // Disable the Crashlytics mapping file upload. This feature should only be
                    // enabled if a Firebase backend is available and configured in
                    // google-services.json.
                    configure<CrashlyticsExtension> {
                        mappingFileUploadEnabled = false
                    }
                }
            }
        }
    }
}
