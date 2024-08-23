plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("androidx.navigation.safeargs.kotlin")
    alias(libs.plugins.google.gms.google.services)
    id ("kotlin-kapt")

}

android {
    namespace = "com.example.healthcare"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.healthcare"
        minSdk = 26
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.auth)
    implementation(libs.androidx.annotation)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.play.services.maps)
    implementation(libs.play.services.location)
    implementation(libs.android.maps.utils)
    implementation(libs.firebase.database)
    implementation(libs.firebase.storage)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation("com.google.android.material:material:1.9.0")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    // Retrofit Gson converter
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    //Google play services Auth
    implementation("com.google.android.gms:play-services-auth:21.2.0")
    //Nav
    val nav_version = "2.7.7"
    implementation("androidx.navigation:navigation-fragment-ktx:$nav_version")
    implementation("androidx.navigation:navigation-ui-ktx:$nav_version")
    // LiveData
    val lifecycle_version = "2.8.4"
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")
    // Dependency Injection
    implementation ("com.google.dagger:dagger:2.48.1")
    kapt ("com.google.dagger:dagger-compiler:2.48.1")
    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9")



}