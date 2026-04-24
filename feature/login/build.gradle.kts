plugins {
    id("dallyrun.android.feature")
}

android {
    namespace = "com.inseong.dallyrun.feature.login"
}

dependencies {
    implementation(projects.core.domain)
    testImplementation(projects.core.testing)
}

