plugins {
id("com.android.application")
id("org.jetbrains.kotlin.android")
}

android {
namespace = "com.guregram.app"
compileSdk = 35

defaultConfig {
    applicationId = "com.guregram.app"
    minSdk = 23
    targetSdk = 35
    versionCode = 1
    versionName = "0.2.0"
}

buildFeatures {
    compose = true
}

composeOptions {
    kotlinCompilerExtensionVersion = "1.5.15"
}

kotlinOptions {
    jvmTarget = "17"
}

}

dependencies {
implementation("androidx.core:core-ktx:1.15.0")
implementation("androidx.activity:activity-compose:1.9.3")
implementation(platform("androidx.compose:compose-bom:2024.10.01"))
implementation("androidx.compose.ui:ui")
implementation("androidx.compose.ui:ui-graphics")
implementation("androidx.compose.ui:ui-tooling-preview")
implementation("androidx.compose.material3:material3")
implementation("androidx.compose.material:material-icons-extended")
debugImplementation("androidx.compose.ui:ui-tooling")
}
