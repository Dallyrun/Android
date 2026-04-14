plugins {
    id("dallyrun.android.library")
    id("dallyrun.android.hilt")
}

android {
    namespace = "com.inseong.dallyrun.core.testing"
}

dependencies {
    implementation(projects.core.model)
    api(libs.junit)
    api(libs.kotlinx.coroutines.test)
    api(libs.turbine)
    api(libs.mockk)
    api(libs.hilt.android.testing)
    api(libs.androidx.junit)
    implementation(libs.kotlinx.coroutines.android)
}
