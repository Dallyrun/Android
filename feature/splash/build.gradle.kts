plugins {
    id("dallyrun.android.feature")
}

android {
    namespace = "com.inseong.dallyrun.feature.splash"
}

dependencies {
    implementation(projects.core.domain)
    testImplementation(projects.core.testing)
}
