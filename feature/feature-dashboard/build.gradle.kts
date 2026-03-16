plugins {
    id("cerofiao.android.feature")
}

android {
    namespace = "com.schwarckdev.cerofiao.feature.dashboard"
}

dependencies {
    implementation(libs.vico.compose.m3)
}
