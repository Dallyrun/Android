plugins {
    id("dallyrun.android.feature")
}

android {
    namespace = "com.inseong.dallyrun.feature.signup"
}

dependencies {
    implementation(projects.core.domain)
    implementation(libs.coil.compose)
    testImplementation(projects.core.testing)
}
