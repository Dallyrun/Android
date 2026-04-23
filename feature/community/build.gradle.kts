plugins {
    id("dallyrun.android.feature")
}

android {
    namespace = "com.inseong.dallyrun.feature.community"
}

dependencies {
    testImplementation(projects.core.testing)
}
