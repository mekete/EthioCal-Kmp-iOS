plugins {
    kotlin("multiplatform")
    id("com.android.library")
    kotlin("plugin.serialization") version "2.2.21"
    id("com.google.devtools.ksp")
    id("androidx.room") version "2.7.0-alpha10"
    id("org.jetbrains.kotlin.plugin.compose") version "2.2.21"
    id("org.jetbrains.compose") version "1.7.3"
}

kotlin {
    jvmToolchain(21)

    // Android target
    androidTarget {
        @OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
        }
    }

    // iOS targets
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        val commonMain by getting {
            dependencies {
                // Kotlin dependencies
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.0")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")

                // Room KMP
                implementation("androidx.room:room-runtime:2.7.0-alpha10")
                implementation("androidx.sqlite:sqlite-bundled:2.5.0-alpha10")

                // Koin for DI
                implementation("io.insert-koin:koin-core:3.5.6")
                implementation("io.insert-koin:koin-compose:1.1.5")

                // Compose Multiplatform
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.ui)
                implementation(compose.components.resources)
                implementation(compose.components.uiToolingPreview)

                // ViewModel for Compose
                implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")
                implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.7")

                // Ethiopian calendar library
                implementation(project(":ethiopic-chrono"))
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        val androidMain by getting {
            dependencies {
                // Android-specific dependencies
                implementation("androidx.core:core-ktx:1.15.0")

                // DataStore for preferences
                implementation("androidx.datastore:datastore-preferences:1.1.1")
            }
        }

        val iosMain by creating {
            dependsOn(commonMain)
        }

        val iosX64Main by getting {
            dependsOn(iosMain)
        }

        val iosArm64Main by getting {
            dependsOn(iosMain)
        }

        val iosSimulatorArm64Main by getting {
            dependsOn(iosMain)
        }
    }
}

android {
    namespace = "com.shalom.calendar.shared"
    compileSdk = 36

    defaultConfig {
        minSdk = 26
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}

room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    // Room KSP
    add("kspCommonMainMetadata", "androidx.room:room-compiler:2.7.0-alpha10")
    add("kspAndroid", "androidx.room:room-compiler:2.7.0-alpha10")
    add("kspIosX64", "androidx.room:room-compiler:2.7.0-alpha10")
    add("kspIosArm64", "androidx.room:room-compiler:2.7.0-alpha10")
    add("kspIosSimulatorArm64", "androidx.room:room-compiler:2.7.0-alpha10")
}
