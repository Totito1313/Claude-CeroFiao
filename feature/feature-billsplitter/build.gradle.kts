plugins {
    id("cerofiao.android.feature")
}

android {
    namespace = "com.schwarckdev.cerofiao.feature.billsplitter"
}

dependencies {
    implementation(project(":core:core-ui"))
    implementation(project(":core:core-designsystem"))
    implementation(project(":core:core-model"))
    implementation(project(":core:core-domain"))
    implementation(project(":core:core-common"))
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    
    // Core Compose and Navigation dependencies are provided by the convention plugin cerofiao.android.feature
}
