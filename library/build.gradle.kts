plugins {
    id("com.android.library")
    id("kotlin-android")
    id("maven-publish")        
}

android {
    namespace = "com.looper.android.support"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
        multiDexEnabled = true
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }    
               
    buildTypes {
        getByName("release") {
            // Disables code shrinking, obfuscation, and optimization.
            isMinifyEnabled = false
            
            // Includes the default ProGuard rules files.
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {
    implementation(libs.androidx.activity)
    implementation(libs.androidx.appcompat)    
    implementation(libs.androidx.core)
    implementation(libs.androidx.preference)
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)
    implementation(libs.material)
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])
                groupId = "com.github.iamlooper"
                artifactId = "android-support"
                version = "2.6.0"
            }
        }
    }
}