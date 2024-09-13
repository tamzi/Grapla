package com.tamzi.grapla

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension

/**
 * Configure Compose-specific options:
 *
 * The configureAndroidCompose function is an internal extension function for the Project class in Gradle, designed to configure Android Compose-specific options. It takes a CommonExtension parameter, which is a generic type representing the common configuration options for Android projects.  Within the function, the commonExtension is configured to enable Compose by setting compose = true in the buildFeatures block:
 *     buildFeatures {
 *          compose = true
 *     }
 *
 * The function also adds dependencies for the Compose libraries using the add method on the dependencies block. It retrieves the Compose BOM (Bill of Materials) and UI tooling libraries from the libs VersionCatalog and adds them as implementation dependencies:
 *
 *     dependencies {
 *          val bom = libs.findLibrary("androidx-compose-bom").get()
 *          add("implementation", platform(bom))
 *          add("androidTestImplementation", platform(bom))
 *          add("implementation", libs.findLibrary("androidx-compose-ui-tooling-preview").get())
 *          add("debugImplementation", libs.findLibrary("androidx-compose-ui-tooling").get())
 *      }
 *
 * The function also configures test options for unit tests, setting isIncludeAndroidResources to true for Robolectric compatibility:
 *
 *    testOptions {
 *      unitTests {
 *            isIncludeAndroidResources = true
 *        }
 *       }
 *
 * Finally, the function configures the ComposeCompilerGradlePluginExtension, which provides additional options for the Compose compiler. It sets up properties for enabling compiler metrics and reports based on Gradle properties, and specifies the stabilityConfigurationFile and enableStrongSkippingMode options:
 *
 *      extensions.configure<ComposeCompilerGradlePluginExtension> {
 *          fun Provider<String>.onlyIfTrue() = flatMap { provider { it.takeIf(String::toBoolean) } }
 *          fun Provider<*>.relativeToRootProject(dir: String) = flatMap {
 *              rootProject.layout.buildDirectory.dir(projectDir.toRelativeString(rootDir))
 *          }.map { it.dir(dir) }
 *
 *      project.providers.gradleProperty("enableComposeCompilerMetrics").onlyIfTrue()
 *         .relativeToRootProject("compose-metrics")
 *         .let(metricsDestination::set)
 *
 *     project.providers.gradleProperty("enableComposeCompilerReports").onlyIfTrue()
 *         .relativeToRootProject("compose-reports")
 *         .let(reportsDestination::set)
 *      }
 *
 * Finally, it sets the stability configuration file and enables strong skipping mode for the Compose compiler:
 *
 *      stabilityConfigurationFile = rootProject.layout.projectDirectory.file("compose_compiler_config.conf")
 *      enableStrongSkippingMode = true
 *
 */
internal fun Project.configureAndroidCompose(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    commonExtension.apply {
        buildFeatures {
            compose = true
        }

        dependencies {
            val bom = libs.findLibrary("androidx-compose-bom").get()
            add("implementation", platform(bom))
            add("androidTestImplementation", platform(bom))
            add("implementation", libs.findLibrary("androidx-compose-ui-tooling-preview").get())
            add("debugImplementation", libs.findLibrary("androidx-compose-ui-tooling").get())
        }

        testOptions {
            unitTests {
                // For Robolectric
                isIncludeAndroidResources = true
            }
        }
    }

    extensions.configure<ComposeCompilerGradlePluginExtension> {
        fun Provider<String>.onlyIfTrue() = flatMap { provider { it.takeIf(String::toBoolean) } }

        fun Provider<*>.relativeToRootProject(dir: String) = flatMap {
            rootProject.layout.buildDirectory.dir(projectDir.toRelativeString(rootDir))
        }.map { it.dir(dir) }

        project.providers
            .gradleProperty("enableComposeCompilerMetrics").onlyIfTrue()
            .relativeToRootProject("compose-metrics")
            .let(metricsDestination::set)

        project.providers
            .gradleProperty("enableComposeCompilerReports").onlyIfTrue()
            .relativeToRootProject("compose-reports")
            .let(reportsDestination::set)

        stabilityConfigurationFile = rootProject.layout.projectDirectory.file("compose_compiler_config.conf")
    }
}
