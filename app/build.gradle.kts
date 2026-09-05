import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.nutricionbn.insulator"
    compileSdk {
        version = release(37) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.nutricionbn.insulator"
        minSdk = 26
        targetSdk = 37
        versionCode = 1
        versionName = "0.0.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            val properties = Properties()
            properties.load(project.rootProject.file("local.properties").inputStream())

            val clientId = properties.getProperty("MY_GOOGLE_ID")
            val xAppHeader = properties.getProperty("X_APP_HEADER")
            val workerUrl = properties.getProperty("WORKER_URL")
            val authRoute = properties.getProperty("AUTH_ROUTE")
            val patientsRoute = properties.getProperty("PATIENTS_ROUTE")
            val medicRoute = properties.getProperty("MEDICS_ROUTE")

            buildConfigField("String", "GOOGLE_CLIENT_ID", "\"$clientId\"")
            buildConfigField("String", "X_APP_HEADER", "\"$xAppHeader\"")
            buildConfigField("String", "WORKER_URL", "\"$workerUrl\"")
            buildConfigField("String", "AUTH_ROUTE", "\"$authRoute\"")
            buildConfigField("String", "PATIENTS_ROUTE", "\"$patientsRoute\"")
            buildConfigField("String", "MEDICS_ROUTE", "\"$medicRoute\"")
        }

        debug {
            val properties = Properties()
            properties.load(project.rootProject.file("local.properties").inputStream())

            val clientId = properties.getProperty("MY_GOOGLE_ID")
            val xAppHeader = properties.getProperty("X_APP_HEADER")
            val workerUrl = properties.getProperty("WORKER_URL")
            val authRoute = properties.getProperty("AUTH_ROUTE")
            val patientsRoute = properties.getProperty("PATIENTS_ROUTE")
            val medicRoute = properties.getProperty("MEDICS_ROUTE")

            buildConfigField("String", "GOOGLE_CLIENT_ID", "\"$clientId\"")
            buildConfigField("String", "X_APP_HEADER", "\"$xAppHeader\"")
            buildConfigField("String", "WORKER_URL", "\"$workerUrl\"")
            buildConfigField("String", "AUTH_ROUTE", "\"$authRoute\"")
            buildConfigField("String", "PATIENTS_ROUTE", "\"$patientsRoute\"")
            buildConfigField("String", "MEDICS_ROUTE", "\"$medicRoute\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    // TODO : use catalog
    //DataStore
    implementation("androidx.datastore:datastore-preferences:1.2.1")
    //Google Auth
    implementation("androidx.credentials:credentials:1.6.0")
    implementation("androidx.credentials:credentials-play-services-auth:1.6.0")
    implementation("com.google.android.libraries.identity.googleid:googleid:1.2.0")
    // Koin
    implementation("io.insert-koin:koin-android:4.2.2")
    implementation("io.insert-koin:koin-androidx-compose:4.2.2")
    //Ktor
    implementation("io.ktor:ktor-client-android:3.5.2")
    implementation("io.ktor:ktor-client-content-negotiation:3.5.2")
    implementation("io.ktor:ktor-serialization-kotlinx-json:3.5.2")
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.runtime)
    //Navigation
    implementation("androidx.navigation:navigation-compose:2.10.0")
    implementation("androidx.navigation:navigation-fragment:2.10.0")
    implementation("androidx.navigation:navigation-ui:2.10.0")
    // Feature module support for Fragments
    implementation("androidx.navigation:navigation-dynamic-features-fragment:2.10.0")
    // Testing Navigation
    androidTestImplementation("androidx.navigation:navigation-testing:2.10.0")
    // JSON serialization library, works with the Kotlin serialization plugin
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.11.0")
    //Coil
    implementation("io.coil-kt.coil3:coil-compose:3.6.0")
    implementation("io.coil-kt.coil3:coil-network-okhttp:3.6.0")
    //////////////////////////////////////////////////
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.animation)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.material3)
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.1")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.1")
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}