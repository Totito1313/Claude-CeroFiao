plugins {
    id("cerofiao.android.library")
    id("cerofiao.android.hilt")
}

android {
    namespace = "com.schwarckdev.cerofiao.core.common"
}

dependencies {
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.datetime)
}
