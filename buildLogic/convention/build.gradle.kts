import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
}

group = "com.tamzi.grapla.buildlogic"

// Configure the build-logic plugins to target JDK 21
// This matches the JDK used to build the project, and is not related to what is running on device.
java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_21
    }
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.android.tools.common)
    compileOnly(libs.compose.gradlePlugin)
    compileOnly(libs.firebase.crashlytics.gradlePlugin)
    compileOnly(libs.firebase.performance.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
    compileOnly(libs.room.gradlePlugin)
    compileOnly(libs.kover.gradlePlugin)
    compileOnly(libs.detekt.gradlePlugin)
    implementation(libs.truth)
}

tasks {
    validatePlugins {
        enableStricterValidation = true
        failOnWarning = true
    }
}

gradlePlugin {
    plugins {
        // Application plugins
        register("androidApplicationCompose") {
            id = "grapla.android.application.compose"
            implementationClass = "application.AndroidApplicationComposeConventionPlugin"
        }
        register("androidApplication") {
            id = "grapla.android.application"
            implementationClass = "application.AndroidApplicationConventionPlugin"
        }
        register("androidApplicationFirebase") {
            id = "grapla.android.application.firebase"
            implementationClass = "application.AndroidApplicationFirebaseConventionPlugin"
        }

        // Library plugins
        register("androidLibraryCompose") {
            id = "grapla.android.library.compose"
            implementationClass = "library.AndroidLibraryComposeConventionPlugin"
        }
        register("androidLibrary") {
            id = "grapla.android.library"
            implementationClass = "library.AndroidLibraryConventionPlugin"
        }

        // Feature plugin
        register("androidFeature") {
            id = "grapla.android.feature"
            implementationClass = "feature.AndroidFeatureConventionPlugin"
        }

        // Test plugin
        register("androidTest") {
            id = "grapla.android.test"
            implementationClass = "test.AndroidTestConventionPlugin"
        }

        // Testing plugins
        register("androidUnitTest") {
            id = "grapla.android.unit.test"
            implementationClass = "testing.AndroidUnitTestConventionPlugin"
        }
        register("androidInstrumentedTest") {
            id = "grapla.android.instrumented.test"
            implementationClass = "testing.AndroidInstrumentedTestConventionPlugin"
        }
        register("androidComposeTest") {
            id = "grapla.android.compose.test"
            implementationClass = "testing.AndroidComposeTestConventionPlugin"
        }

        // Coverage plugins
        register("kover") {
            id = "grapla.kover"
            implementationClass = "coverage.KoverConventionPlugin"
        }
        register("jacoco") {
            id = "grapla.jacoco"
            implementationClass = "coverage.JacocoConventionPlugin"
        }

        // Quality plugins
        register("detekt") {
            id = "grapla.detekt"
            implementationClass = "quality.DetektConventionPlugin"
        }

        // Flavor configuration plugin
        register("androidFlavor") {
            id = "grapla.android.flavor"
            implementationClass = "flavor.AndroidFlavorConventionPlugin"
        }

        // Infrastructure plugins
        register("hilt") {
            id = "grapla.hilt"
            implementationClass = "hilt.HiltConventionPlugin"
        }
        register("androidRoom") {
            id = "grapla.android.room"
            implementationClass = "android.AndroidRoomConventionPlugin"
        }
        register("androidLint") {
            id = "grapla.android.lint"
            implementationClass = "android.AndroidLintConventionPlugin"
        }

        // JVM plugin
        register("jvmLibrary") {
            id = "grapla.jvm.library"
            implementationClass = "jvm.JvmLibraryConventionPlugin"
        }
    }
}
