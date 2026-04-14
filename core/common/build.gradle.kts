plugins {
    id("dallyrun.android.library")
    id("dallyrun.android.hilt")
}

android {
    namespace = "com.inseong.dallyrun.core.common"
}

dependencies {
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
}
