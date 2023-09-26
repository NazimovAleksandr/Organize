plugins {
    alias(libs.plugins.com.android.library)
    alias(libs.plugins.kotlinAndroid)
}

android {
    namespace = "com.dialog.newtask"
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

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
}

dependencies {
    implementation(project(mapOf("path" to ":res")))
    implementation(project(mapOf("path" to ":core")))
    implementation(project(mapOf("path" to ":data")))
    implementation(project(mapOf("path" to ":entity")))

    implementation(project(mapOf("path" to ":dialog:date")))
    implementation(project(mapOf("path" to ":dialog:time")))
    implementation(project(mapOf("path" to ":dialog:priority")))
    implementation(project(mapOf("path" to ":dialog:category")))

    implementation(libs.kotlinx.datetime)
}