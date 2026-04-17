plugins {
    id("dallyrun.android.feature")
}

android {
    namespace = "com.inseong.dallyrun.feature.login"
}

dependencies {
    testImplementation(projects.core.testing)
}
