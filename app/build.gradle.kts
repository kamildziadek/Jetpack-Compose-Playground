plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
}

android {
    compileSdkVersion(29)
    defaultConfig {
        applicationId("com.dziadek.compose")
        minSdkVersion(21)
        targetSdkVersion(29)

        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
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
        kotlinCompilerExtensionVersion = Versions.AndroidX.compose
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}")

    implementation("androidx.core:core-ktx:${Versions.AndroidX.coreKtx}")
    implementation("androidx.appcompat:appcompat:${Versions.AndroidX.appcompat}")
    implementation("com.google.android.material:material:${Versions.Google.material}")

    implementation("androidx.compose:compose-runtime:${Versions.AndroidX.compose}")
    implementation("androidx.ui:ui-framework:${Versions.AndroidX.compose}")
    implementation("androidx.ui:ui-layout:${Versions.AndroidX.compose}")
    implementation("androidx.ui:ui-material:${Versions.AndroidX.compose}")
    implementation("androidx.ui:ui-foundation:${Versions.AndroidX.compose}")
    implementation("androidx.ui:ui-animation:${Versions.AndroidX.compose}")
    implementation("androidx.ui:ui-tooling:${Versions.AndroidX.compose}")


    androidTestImplementation("junit:junit:${Versions.junit}")
    androidTestImplementation("androidx.test:rules:${Versions.AndroidX.test}")
    androidTestImplementation("androidx.test:runner:${Versions.AndroidX.test}")
    androidTestImplementation("androidx.ui:ui-platform:${Versions.AndroidX.compose}")
    androidTestImplementation("androidx.ui:ui-test:${Versions.AndroidX.compose}")
}