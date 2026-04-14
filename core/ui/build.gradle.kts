plugins {
    id("dallyrun.android.library.compose")
}

android {
    namespace = "com.inseong.dallyrun.core.ui"
}

dependencies {
    implementation(projects.core.model)
    implementation(projects.core.designsystem)
    api(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling.preview)
    debugImplementation(libs.androidx.compose.ui.tooling)
}
