package util

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/**
 * This file defines functions to configure Kotlin settings for both Android and JVM
 * projects within a Gradle build script.
 *
 * These configurations ensure that the projects adhere to specific standards and use
 * the appropriate versions of Java and Kotlin.
 *
 * Configure base Kotlin with Android options.
 * This setup ensures that the project is configured with the necessary Kotlin and Android options,
 * including support for Java APIs and core library desugaring.
 */
internal fun Project.configureKotlinAndroid(commonExtension: CommonExtension<*, *, *, *, *, *>) {
    commonExtension.apply {
        compileSdk = BuildLogicConstants.COMPILE_SDK

        defaultConfig {
            minSdk = BuildLogicConstants.MIN_SDK
        }

        compileOptions {
            // Up to Java 11 APIs are available through desugaring
            // https://developer.android.com/studio/write/java11-minimal-support-table
            sourceCompatibility = JavaVersion.toVersion(BuildLogicConstants.JAVA_VERSION)
            targetCompatibility = JavaVersion.toVersion(BuildLogicConstants.JAVA_VERSION)
            isCoreLibraryDesugaringEnabled = true
        }
    }

    // Configure Kotlin options via tasks
    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.fromTarget(BuildLogicConstants.KOTLIN_JVM_TARGET))
            val warningsAsErrors: String? by project
            allWarningsAsErrors.set(warningsAsErrors.toBoolean())
            freeCompilerArgs.add("-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi")
        }
    }

    dependencies {
        add(
            "coreLibraryDesugaring",
            project.extensions.getByType<VersionCatalogsExtension>().named("libs")
                .findLibrary("android.desugarJdkLibs").get(),
        )

        // Core Android KTX dependency (used in virtually all Android modules)
        add(
            "implementation",
            project.extensions.getByType<VersionCatalogsExtension>().named("libs")
                .findLibrary("androidx.core.ktx").get(),
        )
    }
}

/**
 * Configure base Kotlin options for JVM (non-Android)
 *
 * This function configures Kotlin options for JVM (non-Android) projects within a Gradle build script.
 * This setup ensures that the project is configured with the necessary Kotlin and Java options.
 */
internal fun Project.configureKotlinJvm() {
    extensions.configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.toVersion(BuildLogicConstants.JAVA_VERSION)
        targetCompatibility = JavaVersion.toVersion(BuildLogicConstants.JAVA_VERSION)
    }

    // Configure Kotlin options via tasks
    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.fromTarget(BuildLogicConstants.KOTLIN_JVM_TARGET))
            val warningsAsErrors: String? by project
            allWarningsAsErrors.set(warningsAsErrors.toBoolean())
            freeCompilerArgs.add("-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi")
        }
    }
}
