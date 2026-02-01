plugins {
    alias(libs.plugins.grapla.android.application)
    alias(libs.plugins.grapla.android.application.compose)
    alias(libs.plugins.grapla.hilt)
    alias(libs.plugins.grapla.detekt)
    alias(libs.plugins.grapla.android.unit.test)
    alias(libs.plugins.grapla.android.instrumented.test)
    alias(libs.plugins.grapla.android.compose.test)
    alias(libs.plugins.grapla.jacoco)
    alias(libs.plugins.roborazzi)
}

android {
    namespace = "com.tamzi.grapla"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.tamzi.grapla"
        versionCode = 1
        versionName = "0.0.01" // X.Y.ZZ; X = Major, Y = minor, Z = Patch level can be many digits

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(project(":gruid"))

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material3.adaptive)
    implementation(libs.androidx.compose.material3.adaptive.layout)
    implementation(libs.androidx.compose.material3.adaptive.navigation)
    implementation(libs.androidx.compose.material3.windowSizeClass)
    implementation(libs.androidx.compose.runtime.tracing)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.lifecycle.runtimeCompose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.profileinstaller)
    implementation(libs.androidx.tracing.ktx)
    implementation(libs.androidx.window.core)
    implementation(libs.kotlinx.coroutines.guava)
    implementation(libs.coil.kt)

    ksp(libs.hilt.compiler)
    kspTest(libs.hilt.compiler)
}

// Configure Dependency Guard plugin
dependencyGuard {
    configuration("releaseRuntimeClasspath")
}
