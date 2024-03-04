plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.hilt)

    kotlin("android")
    alias(libs.plugins.google.devtools.ksp)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.kotlin.serialize)
}

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

android {
    namespace = "com.barleytea.fetchcodingexercise"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.barleytea.fetchcodingexercise"
        minSdk = 30
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.androidxComposeCompiler.get()
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

hilt {
    enableAggregatingTask = false
    enableExperimentalClasspathAggregation = true
}

ksp {
    arg("skipPrivatePreviews", "true")
}

dependencies {

    implementation(libs.androidx.core.ktx)

    implementation(project(path = ":networking"))

    // compose
    implementation(libs.androidx.compose.compiler)
    implementation(libs.androidx.lifecycle.viewModelCompose)
    val composeBom = platform(libs.androidx.compose.bom)
    implementation(composeBom)
    androidTestImplementation(composeBom)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui.tooling)


    implementation(libs.androidx.activity.compose)

    // hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    testImplementation(libs.hilt.android.testing)
    kspTest(libs.hilt.android.compiler)

    // hiltx
    implementation(libs.androidx.hilt.work)
    ksp(libs.androidx.hilt.compiler)


    // kotlin
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.retrofit.kotlinx.serialization)

    // lifecycle
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.runtimeCompose)
    implementation(libs.androidx.lifecycle.livedata.ktx)

    // moshi
    implementation(libs.moshi)
    implementation(libs.moshi.adapters)
    implementation(libs.moshi.kotlin)
    ksp(libs.moshi.kotlin.codegen)

    // okhttp
    implementation(libs.okhttp3.logging.interceptor)
    implementation(libs.okhttp3)
    implementation(libs.okhttp3.mockwebserver)

    // retrofit
    implementation(libs.retrofit.adapter.rxjava)
    implementation(libs.retrofit.converter.moshi)
    implementation(libs.retrofit)

    // workmanager
    implementation(libs.androidx.work.runtime.ktx)

    implementation(libs.material)

    // Logging
    implementation(libs.timber)

    // D8 Core Library Desugaring for JavaTime without uppping minSDK
    coreLibraryDesugaring(libs.android.desugarJdkLibs)
}