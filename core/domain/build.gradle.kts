plugins {
    id("dallyrun.jvm.library")
}

dependencies {
    implementation(projects.core.model)
    implementation(libs.kotlinx.coroutines.core)
}
