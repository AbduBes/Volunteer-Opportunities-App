plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.example.volunteer"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.volunteer"
        minSdk = 24
        targetSdk = 36
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    // Required for viewModelScope
//    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.3")
//
//    // Optional: for lifecycleScope in Activities/Fragments
//    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.0")
//    implementation("com.google.dagger:hilt-android:2.56.2")
//    ksp("com.google.dagger:hilt-android-compiler:2.56.2")
//
//    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")
//
//    implementation("androidx.compose.material:material-icons-extended")
//    implementation("io.coil-kt.coil3:coil-compose:3.1.0")
//    implementation("io.coil-kt.coil3:coil-network-okhttp:3.1.0")
//    implementation("androidx.navigation:navigation-compose:2.9.7")
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.navigation.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material3.adaptive.navigation.suite)
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    implementation("com.squareup.retrofit2:retrofit:2.11.0")

// Gson converter
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")

// OkHttp - the underlying HTTP engine
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

// Logging interceptor - prints requests/responses in Logcat
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
}