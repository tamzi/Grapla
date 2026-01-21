package util

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension

/**
 * Configure Compose-specific options for application modules.
 */
internal fun Project.configureAndroidCompose(extension: ApplicationExtension) {
    extension.apply {
        buildFeatures {
            compose = true
        }

        testOptions {
            unitTests {
                // For Robolectric
                isIncludeAndroidResources = true
            }
        }
    }

    addComposeDependencies()
    configureComposeCompiler()
}

/**
 * Configure Compose-specific options for library modules.
 */
internal fun Project.configureAndroidCompose(extension: LibraryExtension) {
    extension.apply {
        buildFeatures {
            compose = true
        }

        testOptions {
            unitTests {
                // For Robolectric
                isIncludeAndroidResources = true
            }
        }
    }

    addComposeDependencies()
    configureComposeCompiler()
}

private fun Project.addComposeDependencies() {
    dependencies {
        val bom = libs.findLibrary("androidx-compose-bom").get()
        add("implementation", platform(bom))
        add("androidTestImplementation", platform(bom))
        add("implementation", libs.findLibrary("androidx-compose-ui-tooling-preview").get())
        add("debugImplementation", libs.findLibrary("androidx-compose-ui-tooling").get())
    }
}

private fun Project.configureComposeCompiler() {

    extensions.configure<ComposeCompilerGradlePluginExtension> {
        fun Provider<String>.onlyIfTrue() = flatMap { provider { it.takeIf(String::toBoolean) } }

        fun Provider<*>.relativeToRootProject(dir: String) =
            flatMap {
                rootProject.layout.buildDirectory.dir(projectDir.toRelativeString(rootDir))
            }.map { it.dir(dir) }

        project.providers
            .gradleProperty("enableComposeCompilerMetrics")
            .onlyIfTrue()
            .relativeToRootProject("compose-metrics")
            .let(metricsDestination::set)

        project.providers
            .gradleProperty("enableComposeCompilerReports")
            .onlyIfTrue()
            .relativeToRootProject("compose-reports")
            .let(reportsDestination::set)

        // Use stabilityConfigurationFiles instead of deprecated stabilityConfigurationFile
        stabilityConfigurationFiles.set(
            listOf(rootProject.layout.projectDirectory.file(DependencyConfig.Compose.STABILITY_CONFIG_FILE)),
        )
    }
}
