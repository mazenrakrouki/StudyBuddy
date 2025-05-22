plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.studybuddy"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.studybuddy"
        minSdk = 27
        targetSdk = 35
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
}

dependencies {
    // build.gradle.kts (Module)
        implementation ("com.google.firebase:firebase-database:20.3.0")
        implementation ("androidx.recyclerview:recyclerview:1.3.2")
        implementation(platform("com.google.firebase:firebase-bom:32.7.0"))
        implementation("com.google.firebase:firebase-auth")
        implementation("com.google.firebase:firebase-database")
        implementation("com.google.android.material:material:1.11.0")


    // Firebase Authentication
    implementation("com.google.firebase:firebase-auth:22.3.0")

// Firebase Realtime Database (ou Firestore si tu préfères)
    implementation("com.google.firebase:firebase-database:20.3.0")

// RecyclerView
    implementation("androidx.recyclerview:recyclerview:1.3.1")

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}