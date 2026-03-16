plugins {
    id("cerofiao.android.library")
    id("cerofiao.android.compose")
}

android {
    namespace = "com.schwarckdev.cerofiao.core.ui"
}

dependencies {
    implementation(project(":core:core-model"))
    implementation(project(":core:core-designsystem"))
    implementation(project(":core:core-common"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.kotlinx.datetime)
    implementation(libs.coil.compose)
}
