plugins {
    id("cerofiao.android.library")
}

android {
    namespace = "com.schwarckdev.cerofiao.core.model"
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.datetime)

    testImplementation(libs.junit)
}
