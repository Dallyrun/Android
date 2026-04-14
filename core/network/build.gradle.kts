plugins {
    id("dallyrun.android.library")
    id("dallyrun.android.hilt")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.inseong.dallyrun.core.network"

    buildTypes {
        debug {
            buildConfigField("String", "BASE_URL", "\"https://api.dallyrun.com/\"")
        }
        release {
            buildConfigField("String", "BASE_URL", "\"https://api.dallyrun.com/\"")
        }
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(projects.core.model)
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.kotlinx.serialization)
    implementation(libs.okhttp.core)
    implementation(libs.okhttp.logging)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.coroutines.android)
}
