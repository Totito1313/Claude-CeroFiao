plugins {
    id("cerofiao.android.application")
    id("cerofiao.android.compose")
    id("cerofiao.android.hilt")
}

android {
    namespace = "com.SchwarckDev.CeroFiao"

    defaultConfig {
        applicationId = "com.SchwarckDev.CeroFiao"
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("debug")
        }
    }
}

dependencies {
    // Core modules
    implementation(project(":core:core-model"))
    implementation(project(":core:core-data"))
    implementation(project(":core:core-domain"))
    implementation(project(":core:core-common"))
    implementation(project(":core:core-designsystem"))
    implementation(project(":core:core-ui"))

    // Feature modules
    implementation(project(":feature:feature-dashboard"))
    implementation(project(":feature:feature-transactions"))
    implementation(project(":feature:feature-accounts"))
    implementation(project(":feature:feature-categories"))
    implementation(project(":feature:feature-exchange-rates"))
    implementation(project(":feature:feature-settings"))
    implementation(project(":feature:feature-onboarding"))
    implementation(project(":feature:feature-budget"))
    implementation(project(":feature:feature-debt"))

    // AndroidX
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.work.runtime.ktx)
    implementation(libs.hilt.work)
    ksp(libs.hilt.work.compiler)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
}
