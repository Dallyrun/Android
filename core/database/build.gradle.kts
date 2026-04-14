plugins {
    id("dallyrun.android.library")
    id("dallyrun.android.hilt")
    alias(libs.plugins.room)
}

android {
    namespace = "com.inseong.dallyrun.core.database"
}

room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    implementation(projects.core.model)
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)
    implementation(libs.kotlinx.coroutines.android)
}
