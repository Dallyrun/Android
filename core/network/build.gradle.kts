import java.util.Properties

plugins {
    id("dallyrun.android.library")
    id("dallyrun.android.hilt")
    alias(libs.plugins.kotlin.serialization)
}

val localProperties = Properties().apply {
    val file = rootProject.file("local.properties")
    if (file.exists()) {
        file.inputStream().use { load(it) }
    }
}
val baseUrl: String = localProperties.getProperty("BASE_URL")?.takeIf { it.isNotBlank() }
    ?: error("BASE_URL is missing. Copy local.properties.example to local.properties and set BASE_URL.")

android {
    namespace = "com.inseong.dallyrun.core.network"

    buildTypes {
        debug {
            buildConfigField("String", "BASE_URL", "\"$baseUrl\"")
        }
        release {
            buildConfigField("String", "BASE_URL", "\"$baseUrl\"")
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
    api(libs.okhttp.core)
    implementation(libs.okhttp.logging)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.coroutines.android)
    testImplementation(projects.core.testing)
}
