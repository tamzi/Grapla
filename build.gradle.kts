// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.android.test) apply false
    alias(libs.plugins.baselineprofile) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlinx.serialization) apply false
    alias(libs.plugins.dependencyGuard) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
    alias(libs.plugins.firebase.perf) apply false
    alias(libs.plugins.gms) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.roborazzi) apply false
    alias(libs.plugins.secrets) apply false
    alias(libs.plugins.room) apply false
    alias(libs.plugins.module.graph) apply true
    // Kover plugin must be declared here with "apply false" so convention plugin can apply it
    alias(libs.plugins.kover) apply false
    // Detekt plugin - apply at root for project-wide static analysis
    alias(libs.plugins.detekt) apply true
    // Apply Kover convention plugin to enable coverage tasks
    alias(libs.plugins.grapla.kover)
}

// Task to automatically install git hooks if not present
// This runs during Gradle sync to ensure hooks are always set up
tasks.register<Exec>("autoInstallGitHooks") {
    group = "setup"
    description = "Automatically installs git hooks if they're not present"
    commandLine("bash", "./scripts/auto-install-hooks.sh")

    // Don't fail the build, just warn
    isIgnoreExitValue = true
}

// Task to check if git hooks are installed
// This provides a warning during build if hooks are not set up
tasks.register<Exec>("checkGitHooks") {
    group = "verification"
    description = "Verifies that git hooks are properly installed"
    commandLine("bash", "./scripts/check-hooks-on-build.sh")

    // Don't fail the build, just warn
    isIgnoreExitValue = true

    // Run after auto-install
    dependsOn("autoInstallGitHooks")
}

// Run the check in subprojects when building
subprojects {
    tasks.configureEach {
        if (name == "preBuild") {
            dependsOn(":checkGitHooks")
        }
    }
}

// Also make it easy to run manually
tasks.register("checkHooks") {
    group = "verification"
    description = "Alias for checkGitHooks"
    dependsOn("checkGitHooks")
}