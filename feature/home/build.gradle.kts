plugins {
    id("dallyrun.android.feature")
}

android {
    namespace = "com.inseong.dallyrun.feature.home"
}

dependencies {
    testImplementation(projects.core.testing)
}
