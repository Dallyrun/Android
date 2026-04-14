plugins {
    id("dallyrun.android.feature")
}

android {
    namespace = "com.inseong.dallyrun.feature.history"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.core.data)
    testImplementation(projects.core.testing)
}
