import com.android.build.gradle.internal.api.BaseVariantOutputImpl
import java.text.SimpleDateFormat
import java.util.Date

// Git SHA and build metadata
val gitSha = Runtime.getRuntime()
    .exec(arrayOf("git", "rev-parse", "--short", "HEAD"), emptyArray(), project.rootDir)
    .inputStream.bufferedReader().use { it.readText() }.trim()

val appName = "Ethio-Cal"
val playStoreCode = 108
val majorVersion = "3"
val subVersion = 0

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
    id("com.google.devtools.ksp")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.google.firebase.crashlytics")
    kotlin("plugin.serialization") version "2.0.21"

}

android {
    namespace = "com.shalom.calendar"
    compileSdk = 36
    ndkVersion = "27.0.12077973"

    defaultConfig {
        applicationId = "com.shalom.calendar"
        minSdk = 26
        targetSdk = 36
        versionCode = playStoreCode
        versionName = "${majorVersion}.${subVersion}.${playStoreCode}"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        buildConfigField("int", "VERSION_CODE", versionCode.toString())
        buildConfigField("String", "VERSION_NAME", "\"${versionName}\"")
        buildConfigField("String", "BUILD_TIME", "\"${SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())}\"")
        buildConfigField("String", "GIT_SHA", "\"${gitSha}\"")

    }
    bundle {
        language {
            //not to split language resources when building App Bundle.
            enableSplit = false
        }
    }
    ksp {
        arg("room.schemaLocation", "$projectDir/schemas")
    }
    buildFeatures {
        buildConfig = true  // Make sure this is enabled
    }
    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
            resValue ("string", "app_name", "Ethio Cal-D")

        }

        release {
            resValue ("string", "app_name", "Ethio Calendar")
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }


    kotlin {
        jvmToolchain(21)
        compilerOptions {
            freeCompilerArgs.addAll(
                listOf("-Xannotation-default-target=param-property")
            )
        }
    }
    buildFeatures {
        compose = true
    }
    @Suppress("UnstableApiUsage")
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.4"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }




    // Custom APK naming and ProGuard mapping file organization
    applicationVariants.all {
        val variant = this
        if (variant.buildType.isMinifyEnabled){
            outputs.all {
                // Custom APK naming with version (requires cast to BaseVariantOutputImpl)
                (this as BaseVariantOutputImpl).outputFileName = "${appName}-${variant.name}-${variant.versionName}.apk"

            }

            // Handle mapping file renaming
            assembleProvider.configure {
                doLast {
                    val mappingFileCollection = variant.mappingFileProvider.orNull
                    if (mappingFileCollection != null && !mappingFileCollection.isEmpty) {
                        val mapping = mappingFileCollection.singleFile // Get the single file from the collection
                        if (mapping.exists()) {
                            val outputDir = variant.outputs.first().outputFile.parentFile
                            val newMappingFile = File(outputDir, "mapping-${variant.versionName}.txt")
                            mapping.copyTo(newMappingFile, overwrite = true)
                        }
                    }
                }
            }
        }
    }
}


dependencies {
    // Ethiopian DatePicker Library
    implementation(project(":ethiopic-chrono"))
    implementation(project(":datepicker"))

    // Core Android
    implementation("androidx.core:core-ktx:1.17.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.9.4")
    implementation("androidx.activity:activity-compose:1.12.0-rc01")

    // Compose
    implementation(platform("androidx.compose:compose-bom:2025.11.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")

    // Glance (App Widgets)
    implementation("androidx.glance:glance-appwidget:1.1.1")
    implementation("androidx.glance:glance-material3:1.1.1")

//    implementation("com.google.dagger:dagger-compiler:2.57.2")
    ksp("com.google.dagger:dagger-compiler:2.57.2")

    // Navigation
    implementation("androidx.navigation:navigation-compose:2.9.6")

    // Hilt
    implementation("com.google.dagger:hilt-android:2.57.2")
    ksp("com.google.dagger:hilt-compiler:2.57.2")
    implementation("androidx.hilt:hilt-navigation-compose:1.3.0")
    implementation("androidx.hilt:hilt-work:1.3.0")
    ksp("androidx.hilt:hilt-compiler:1.3.0")

    // Room
    implementation("androidx.room:room-runtime:2.8.3")
    implementation("androidx.room:room-ktx:2.8.3")
    ksp("androidx.room:room-compiler:2.8.3")

    // DataStore
    implementation("androidx.datastore:datastore-preferences:1.1.7")
    implementation("androidx.datastore:datastore-core:1.1.7")

    // Kotlinx Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")

    implementation("com.google.accompanist:accompanist-systemuicontroller:0.36.0")
    // WorkManager
    implementation("androidx.work:work-runtime-ktx:2.11.0")

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:34.6.0"))
    implementation("com.google.firebase:firebase-messaging")
    implementation("com.google.firebase:firebase-config")
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-crashlytics")

    // ThreeTenBP for Ethiopian Calendar
//    implementation("org.threeten:threeten-extra:1.8.0")
    implementation("com.airbnb.android:lottie-compose:6.7.1")
    // Timber
    implementation("com.jakewharton.timber:timber:5.0.1")

    // Accompanist
    implementation("com.google.accompanist:accompanist-pager:0.36.0")
    implementation("com.google.accompanist:accompanist-permissions:0.37.3")


    implementation("androidx.compose.foundation:foundation:1.9.4")

    // Testing
    testImplementation("junit:junit:4.13.2")
    testImplementation("com.google.truth:truth:1.4.5")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.2")

    androidTestImplementation("androidx.test:runner:1.6.2")
    androidTestImplementation("androidx.test.ext:junit:1.3.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.7.0")
    androidTestImplementation("androidx.test.uiautomator:uiautomator:2.3.0")
    androidTestImplementation("androidx.test:rules:1.6.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2025.11.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")

    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}
