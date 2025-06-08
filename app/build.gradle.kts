plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.test1"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.test1"
        minSdk = 34
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildFeatures {
        viewBinding = true
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

// In your app/build.gradle.kts file

dependencies {
    // --- Default Core Libraries ---
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // --- ViewModel & Lifecycle (for modern architecture) ---
    // For viewModelScope
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.2")
    // For lifecycleScope
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.2")
    // For the "by viewModels()" delegate
    implementation("androidx.activity:activity-ktx:1.9.0")

    // --- Networking (Retrofit for API calls) ---
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    // For logging network requests (very useful for debugging)
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")

    // --- Image Loading (Glide) ---
    implementation("com.github.bumptech.glide:glide:4.16.0")

    // --- UI Components ---
    // For pull-to-refresh
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    // --- Testing Libraries (usually included by default) ---
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}