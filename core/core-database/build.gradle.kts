plugins {
    id("cerofiao.android.library")
    id("cerofiao.android.hilt")
    id("cerofiao.android.room")
}

android {
    namespace = "com.schwarckdev.cerofiao.core.database"
}

dependencies {
    implementation(project(":core:core-model"))
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.datetime)
}
