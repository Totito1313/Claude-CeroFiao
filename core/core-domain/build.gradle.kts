plugins {
    id("cerofiao.android.library")
    id("cerofiao.android.hilt")
}

android {
    namespace = "com.schwarckdev.cerofiao.core.domain"
}

dependencies {
    implementation(project(":core:core-model"))
    implementation(project(":core:core-common"))
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.datetime)
}
