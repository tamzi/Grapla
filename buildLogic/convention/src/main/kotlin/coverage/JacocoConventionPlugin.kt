package coverage

import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.register
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension
import org.gradle.testing.jacoco.tasks.JacocoReport
import util.libs

/**
 * Convention plugin for configuring JaCoCo (Java Code Coverage).
 *
 * JaCoCo is a widely-used code coverage library that provides:
 * - Support for Java and Kotlin code
 * - Multiple report formats (XML, HTML, CSV)
 * - Integration with CI/CD tools
 * - Coverage data for unit and instrumented tests
 *
 * This plugin configures JaCoCo with:
 * - Latest JaCoCo version
 * - Combined coverage from unit and instrumented tests
 * - Exclusion of generated code and Android framework classes
 * - Unified coverage report task
 *
 * Apply this plugin to your module:
 * ```kotlin
 * plugins {
 *     id("grapla.jacoco")
 * }
 * ```
 */
class JacocoConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("jacoco")
            }

            // Configure JaCoCo version
            extensions.configure<JacocoPluginExtension> {
                toolVersion = libs.findVersion("jacoco").get().toString()
            }

            // Configure build variants for coverage
            pluginManager.withPlugin("com.android.application") {
                extensions.configure<BaseAppModuleExtension> {
                    enableJacocoCoverage()
                }
                configureJacocoTasks(isLibrary = false)
            }

            pluginManager.withPlugin("com.android.library") {
                extensions.configure<LibraryExtension> {
                    enableJacocoCoverage()
                }
                configureJacocoTasks(isLibrary = true)
            }
        }
    }

    /**
     * Enables JaCoCo coverage for build variants
     */
    private fun com.android.build.api.dsl.CommonExtension<*, *, *, *, *, *>.enableJacocoCoverage() {
        buildTypes {
            named("debug") {
                enableAndroidTestCoverage = true
                enableUnitTestCoverage = true
            }
        }
    }

    /**
     * Configures JaCoCo report tasks
     */
    private fun Project.configureJacocoTasks(isLibrary: Boolean) {
        val buildVariants = if (isLibrary) listOf("debug", "release") else listOf("debug")

        buildVariants.forEach { variant ->
            val variantCapitalized = variant.replaceFirstChar { it.uppercase() }

            tasks.register<JacocoReport>("jacoco${variantCapitalized}Report") {
                dependsOn("test${variantCapitalized}UnitTest")

                group = "Reporting"
                description = "Generate JaCoCo coverage reports for the $variant build variant"

                reports {
                    xml.required.set(true)
                    html.required.set(true)
                    csv.required.set(false)
                }

                // Source directories
                val javaTree = fileTree("${project.projectDir}/src/main/java")
                val kotlinTree = fileTree("${project.projectDir}/src/main/kotlin")
                sourceDirectories.setFrom(files(javaTree, kotlinTree))

                // Class directories
                val javaClasses =
                    fileTree("${project.layout.buildDirectory.get()}/intermediates/javac/$variant") {
                        exclude(getExclusionList())
                    }
                val kotlinClasses =
                    fileTree("${project.layout.buildDirectory.get()}/tmp/kotlin-classes/$variant") {
                        exclude(getExclusionList())
                    }
                classDirectories.setFrom(files(javaClasses, kotlinClasses))

                // Execution data
                executionData.setFrom(
                    fileTree(project.layout.buildDirectory) {
                        include(
                            "**/*.exec",
                            "**/*.ec",
                            "outputs/unit_test_code_coverage/${variant}UnitTest/*.exec",
                        )
                    },
                )
            }
        }

        // Create unified report task
        tasks.register<JacocoReport>("jacocoTestReport") {
            dependsOn(buildVariants.map { "jacoco${it.replaceFirstChar { c -> c.uppercase() }}Report" })

            group = "Reporting"
            description = "Generate unified JaCoCo coverage reports for all variants"
        }
    }

    /**
     * Returns list of classes to exclude from coverage reports
     */
    private fun getExclusionList() = listOf(
        // Android generated classes
        "**/R.class",
        "**/R$*.class",
        "**/BuildConfig.*",
        "**/Manifest*.*",
        "**/*Test*.*",
        "android/**/*.*",

        // Dagger/Hilt generated classes
        "**/*_Factory.*",
        "**/*_MembersInjector.*",
        "**/*_HiltModules*.*",
        "**/Hilt_*.*",
        "**/*_Impl.*",
        "**/*_Provide*Factory.*",
        "**/di/**",
        "dagger/hilt/internal/aggregatedroot/codegen/**",
        "hilt_aggregated_deps/**",

        // Compose generated classes
        "**/*ComposableSingletons*.*",

        // Data classes and models (optional - adjust as needed)
        "**/*\$Result.*",
        "**/*\$Companion.*",
    )
}
