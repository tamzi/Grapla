plugins {
    alias(libs.plugins.grapla.android.library)
    alias(libs.plugins.grapla.android.library.compose)
    alias(libs.plugins.grapla.android.lint)
    alias(libs.plugins.grapla.detekt)
    alias(libs.plugins.grapla.android.unit.test)
    alias(libs.plugins.grapla.android.instrumented.test)
    alias(libs.plugins.grapla.android.compose.test)
    alias(libs.plugins.grapla.jacoco)
}

android {
    namespace = "com.tamzi.gruid"

    defaultConfig {

        consumerProguardFiles("consumer-rules.pro")
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}

dependencies {

    api(libs.androidx.compose.foundation)
    api(libs.androidx.compose.foundation.layout)
    api(libs.androidx.compose.material.iconsExtended)
    api(libs.androidx.compose.material3)
    api(libs.androidx.compose.material3.adaptive)
    api(libs.androidx.compose.material3.navigationSuite)
    api(libs.androidx.compose.runtime)
    api(libs.androidx.compose.ui.util)

    implementation(libs.coil.kt.compose)
}
