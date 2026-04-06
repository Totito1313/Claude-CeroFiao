plugins {
    id("cerofiao.android.feature")
}

android {
    namespace = "com.schwarckdev.cerofiao.feature.transactions"
}

dependencies {
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
}
