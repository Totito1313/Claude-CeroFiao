plugins {
    id("cerofiao.android.library")
    id("cerofiao.android.hilt")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.schwarckdev.cerofiao.core.data"
}

dependencies {
    implementation(project(":core:core-model"))
    implementation(project(":core:core-database"))
    implementation(project(":core:core-network"))
    implementation(project(":core:core-datastore"))
    implementation(project(":core:core-domain"))
    implementation(project(":core:core-common"))
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.opencsv)
}
