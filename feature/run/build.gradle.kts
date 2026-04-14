plugins {
    id("dallyrun.android.feature")
}

android {
    namespace = "com.inseong.dallyrun.feature.run"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.core.data)
    testImplementation(projects.core.testing)
}
