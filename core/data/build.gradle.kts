plugins {
    id("dallyrun.android.library")
    id("dallyrun.android.hilt")
}

android {
    namespace = "com.inseong.dallyrun.core.data"
}

dependencies {
    implementation(projects.core.model)
    implementation(projects.core.domain)
    implementation(projects.core.database)
    implementation(projects.core.network)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.androidx.datastore.preferences)
    testImplementation(projects.core.testing)
}
