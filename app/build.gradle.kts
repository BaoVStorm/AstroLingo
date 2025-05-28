plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.astrolingo"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.astrolingo"
        minSdk = 24
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

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    implementation(libs.glide)
    implementation(libs.core)
    implementation(libs.annotations) // Glide library
    annotationProcessor(libs.glide.compiler) // Glide compiler
    implementation(libs.glide.okhttp3)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation(libs.volley)
    implementation(libs.circleimageview)
    implementation(libs.picasso)

//    implementation(libs.credentials)
//    implementation(libs.credentials.play.services.auth)
//    implementation(libs.googleid)
    implementation(libs.play.services.auth)

    implementation(libs.viewpager2)

    implementation(libs.pinview)

    implementation(libs.material.v1110)

    implementation(libs.flexbox)
}