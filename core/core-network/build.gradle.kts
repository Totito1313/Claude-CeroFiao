plugins {
    id("cerofiao.android.library")
    id("cerofiao.android.hilt")
}

android {
    namespace = "com.schwarckdev.cerofiao.core.network"
}

dependencies {
    implementation(project(":core:core-model"))
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.android)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.json)
    implementation(libs.ktor.client.logging)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.datetime)
}
