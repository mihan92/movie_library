plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp") version "1.9.20-1.0.14"
}

android {
    namespace = "com.mihan.hdrezka"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.mihan.hdrezka"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "0.1"
        vectorDrawables {
            useSupportLibrary = true
        }

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
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

kapt {
    correctErrorTypes = true
}

dependencies {

    val hilt_version = "2.48"
    val navigation_version = "1.9.55"

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.tv:tv-foundation:1.0.0-alpha07")
    implementation("androidx.tv:tv-material:1.0.0-alpha07")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.1")

    //DI
    implementation("com.google.dagger:hilt-android:$hilt_version")
    kapt ("com.google.dagger:hilt-compiler:$hilt_version")

    //Navigation
    implementation("io.github.raamcosta.compose-destinations:core:$navigation_version")
    ksp("io.github.raamcosta.compose-destinations:ksp:$navigation_version")

    //Tests
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}