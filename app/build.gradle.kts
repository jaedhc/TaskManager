plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    // Implementing Google Services
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    namespace = "com.example.taskmanager"
    compileSdk = 34

    buildFeatures{
        viewBinding = true
    }

    defaultConfig {
        applicationId = "com.example.taskmanager"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    //ZEGOCLOUD
    implementation("com.github.ZEGOCLOUD:zego_uikit_prebuilt_call_android:+")
    //AUTH
    implementation("com.google.firebase:firebase-auth:22.3.1")
    //REALTIME DATABASE
    implementation("com.google.firebase:firebase-database:20.3.0")
    implementation("com.google.firebase:firebase-firestore:24.10.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    //GSON TO PASS OBJECTS FROM SHAREDPREF
    implementation("com.google.code.gson:gson:2.10.1")
    //Implementing ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    //Implementing LiveData
    implementation("androidx.lifecycle:lifecycle-livedata-core-ktx:2.7.0")
    //Implementing Fragments
    implementation("androidx.fragment:fragment-ktx:1.6.2")
    //Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:32.7.1"))
    //TODO: add the dependencies for firebase products (crashlytics, etc)
    //IF using BoM don't specify the version of Firebase dependencies
    implementation("com.google.firebase:firebase-analytics")
    //implementation("com.google.firebase:firebase-auth")
    //Analytics and crashlytics dependencies
    implementation("com.google.firebase:firebase-crashlytics-ktx")
    //Android SignIn
    implementation("com.google.android.gms:play-services-auth:20.7.0")
    //Glide Implementation
    implementation("com.github.bumptech.glide:glide:4.13.0")
}