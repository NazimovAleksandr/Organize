plugins {
    alias(libs.plugins.com.android.library)
    alias(libs.plugins.kotlinAndroid)
}

android {
    namespace = "com.core"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
}

dependencies {
    implementation(project(mapOf("path" to ":res")))
    implementation(project(mapOf("path" to ":entity")))

    api(libs.core.ktx)

    api(libs.lifecycle.runtime.ktx)
    api(libs.lifecycle.runtime.compose)
    api(libs.lifecycle.viewmodel.ktx)
    api(libs.lifecycle.viewmodel.compose)

    api(platform(libs.compose.bom))
    api(libs.foundation)
    api(libs.foundation.layout)
    api(libs.ui)
    api(libs.ui.graphics)
    api(libs.ui.tooling.preview)
    api(libs.material3)

    androidTestApi(platform(libs.compose.bom))
    debugApi(libs.ui.tooling)

    api(libs.activity.compose)
    api(libs.navigation.compose)

    api(libs.koin.core)
    api(libs.koin.android)
    api(libs.koin.compose)
}