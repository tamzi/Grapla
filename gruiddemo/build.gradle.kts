plugins {
    alias(libs.plugins.grapla.android.application)
    alias(libs.plugins.grapla.android.application.compose)
    alias(libs.plugins.grapla.detekt)
    alias(libs.plugins.grapla.android.unit.test)
    alias(libs.plugins.grapla.android.instrumented.test)
    alias(libs.plugins.grapla.android.compose.test)
    alias(libs.plugins.grapla.jacoco)
}

android {
    namespace = "com.tamzi.gruiddemo"

    defaultConfig {
        applicationId = "com.tamzi.gruiddemo"
        versionCode = 1
        versionName = "0.0.01" // X.Y.ZZ; X = Major, Y = minor, Z = Patch level can be many digits
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
}

dependencyGuard {
    configuration("releaseRuntimeClasspath")
}
