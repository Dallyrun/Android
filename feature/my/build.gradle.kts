plugins {
    id("dallyrun.android.feature")
}

android {
    namespace = "com.inseong.dallyrun.feature.my"
}

dependencies {
    testImplementation(projects.core.testing)
}
