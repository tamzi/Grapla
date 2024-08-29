package com.tamzi.grapla

import com.android.build.api.variant.LibraryAndroidComponentsExtension
import org.gradle.api.Project

/**
 * Disable unnecessary Android instrumented tests for the [project] if there is no `androidTest` folder.
 * Otherwise, these projects would be compiled, packaged, installed and ran only
 * to end-up with the following message:
 *
 * > Starting 0 tests on AVD
 *
 * Note: this could be improved by checking other potential sourceSets based on buildTypes and flavors.
 *
 *
 * The disableUnnecessaryAndroidTests function is an internal extension function for
 * the LibraryAndroidComponentsExtension class in Gradle. It is designed to disable
 * unnecessary Android instrumented tests for a given project if there is no androidTest folder present.
 * The function takes a Project parameter and uses the beforeVariants method to configure the
 * variants before they are built. Inside the beforeVariants block, it checks if
 * the androidTest folder exists in the project's directory.
 * This is done using the project.projectDir.resolve("src/androidTest").exists() expression.
 *
 * `project.projectDir.resolve("src/androidTest").exists()`
 * The above expression resolves the
 * path to the androidTest folder in the project directory and checks if it exists.
 *
 * If the androidTest folder does not exist, the enableAndroidTest property of
 * the variant is set to false, effectively disabling the Android instrumented tests for that variant.
 * If the folder exists, the enableAndroidTest property remains unchanged.
 *
 * `it.enableAndroidTest = it.enableAndroidTest && project.projectDir.resolve("src/androidTest").exists()`
 *
 * The above line of code sets the enableAndroidTest property of the variant to false if the
 * androidTest folder does not exist in the project directory.
 *
 * This approach ensures that unnecessary Android instrumented tests are not compiled,
 * packaged, installed, and run, which would otherwise result in a message indicating that
 * no tests were started on the Android Virtual Device (AVD).
 *
 * @param project : The project for which unnecessary Android instrumented tests should be disabled.
 *
 *
 * `LibraryAndroidComponentsExtension` :  The LibraryAndroidComponentsExtension class is a Gradle extension
 * that allows configuration of Android components for library projects.
 *
 * `beforeVariants`  The beforeVariants method is used to configure the variants before they are built.
 *
 * `exists` : The exists method checks if a file or directory exists at the specified path.
 *
 * `enableAndroidTest` : The enableAndroidTest property is used to enable or disable Android instrumented tests for a variant.
 *
 * `projectDir` :  The projectDir property provides the directory path of the project.
 *
 * `resolve The resolve method resolves the path to a file or directory based on the specified path.
 *
 */

internal fun LibraryAndroidComponentsExtension.disableUnnecessaryAndroidTests(
    project: Project,
) = beforeVariants {
    it.enableAndroidTest = it.enableAndroidTest
        && project.projectDir.resolve("src/androidTest").exists()
}
