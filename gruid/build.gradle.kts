plugins {
    alias(libs.plugins.grapla.android.library)
    alias(libs.plugins.grapla.android.library.compose)
    alias(libs.plugins.grapla.android.lint)
    alias(libs.plugins.ktlint)
}

android {
    namespace = "com.tamzi.gruid"

    defaultConfig {

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    kotlinOptions {
        jvmTarget = "21"
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

    testImplementation(libs.androidx.compose.ui.test)
    testImplementation(libs.androidx.compose.ui.testManifest)
    testImplementation(libs.junit)
    testImplementation(libs.hilt.android.testing)
    testImplementation(libs.robolectric)

    androidTestImplementation(libs.bundles.androidx.compose.ui.test)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
