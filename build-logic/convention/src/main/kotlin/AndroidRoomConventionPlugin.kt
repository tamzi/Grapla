import androidx.room.gradle.RoomExtension
import com.google.devtools.ksp.gradle.KspExtension
import com.tamzi.grapla.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies




/**
 *
 * This plugin sets up an Android project with the necessary Room database
 * plugins and configurations, including KSP support, schema directory configuration,
 * and Room dependencies.
 *
 * The `AndroidRoomConventionPlugin` class implements the `Plugin<Project>` interface in Gradle,
 * allowing it to be applied to a Gradle project. This plugin is designed to configure Android
 * projects with Room database settings and dependencies.
 *
 * The `apply` method is overridden to define the plugin's behavior when it is applied to a project.
 * Inside this method, the `with(target)` block is used to operate on the `Project` instance passed
 * as the `target` parameter.
 *
 * ```
 * class AndroidRoomConventionPlugin : Plugin<Project> {
 *     override fun apply(target: Project) {
 *         with(target) {
 *             // Configuration code
 *         }
 *     }
 * }
 * ```
 *
 * Within the `with(target)` block, the `pluginManager` is used to apply the `androidx.room` plugin
 * for Room database support and the `com.google.devtools.ksp` plugin for
 * Kotlin Symbol Processing (KSP).
 *
 * ```
 * pluginManager.apply("androidx.room")
 * pluginManager.apply("com.google.devtools.ksp")
 * ```
 *
 * Next, the `extensions.configure<KspExtension>` method is used to configure the `KspExtension`
 * instance. This configuration sets the `room.generateKotlin` argument to `true`, enabling Room
 * to generate Kotlin code.
 *
 * ```
 * extensions.configure<KspExtension> {
 *     arg("room.generateKotlin", "true")
 * }
 * ```
 *
 * The `extensions.configure<RoomExtension>` method is used to configure the `RoomExtension` instance.
 * This configuration sets the `schemaDirectory` to a directory named `schemas` within the
 * project directory. The schemas directory contains a schema file for each version of the
 * Room database, which is required to enable Room auto migrations.
 *
 * ```
 * extensions.configure<RoomExtension> {
 *     schemaDirectory("$projectDir/schemas")
 * }
 * ```
 *
 * The `dependencies` block is used to add various Room dependencies to the project.
 * It includes the Room runtime library, the Room Kotlin extensions (KTX) library, and
 * the Room compiler for KSP.
 *
 * ```
 * dependencies {
 *     add("implementation", libs.findLibrary("room.runtime").get())
 *     add("implementation", libs.findLibrary("room.ktx").get())
 *     add("ksp", libs.findLibrary("room.compiler").get())
 * }
 * ```
 *
 *
 * */
class AndroidRoomConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("androidx.room")
            pluginManager.apply("com.google.devtools.ksp")

            extensions.configure<KspExtension> {
                arg("room.generateKotlin", "true")
            }

            extensions.configure<RoomExtension> {
                // The schemas directory contains a schema file for each version of the Room database.
                // This is required to enable Room auto migrations.
                // See https://developer.android.com/reference/kotlin/androidx/room/AutoMigration.
                schemaDirectory("$projectDir/schemas")
            }

            dependencies {
                add("implementation", libs.findLibrary("room.runtime").get())
                add("implementation", libs.findLibrary("room.ktx").get())
                add("ksp", libs.findLibrary("room.compiler").get())
            }
        }
    }
}