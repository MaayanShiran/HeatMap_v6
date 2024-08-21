plugins {
    alias(libs.plugins.android.application)

}

android {
    namespace = "com.classy.heatmap"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.classy.heatmap"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation(libs.heatmaplibrary)
    implementation(libs.appcompat)
   // implementation("com.github.MaayanShiran:HeatMap_v5:1.00.06")

   // implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
 //   implementation(project(":heatmaplibrary"))
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}