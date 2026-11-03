
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.jarvis.ai"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.jarvis.ai"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner =
            "com.jarvis.ai.JarvisTestRunner"
    }
}

dependencies {
    androidTestImplementation(
        "com.google.dagger:hilt-android-testing:2.52"
    )

    kspAndroidTest(
        "com.google.dagger:hilt-compiler:2.52"
    )
}
