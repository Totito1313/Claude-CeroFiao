plugins {
    id("cerofiao.android.library")
    id("cerofiao.android.compose")
}

android {
    namespace = "com.schwarckdev.cerofiao.core.designsystem"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.compose.material.icons.extended)
    api(libs.lucide.icons)
    api(libs.oneui.icons)
}
