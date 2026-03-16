plugins {
    id("cerofiao.android.library")
    id("cerofiao.android.hilt")
}

android {
    namespace = "com.schwarckdev.cerofiao.core.datastore"
}

dependencies {
    implementation(project(":core:core-model"))
    implementation(libs.datastore.preferences)
    implementation(libs.kotlinx.coroutines.android)
}
