plugins {
    kotlin("multiplatform")
    id("com.android.library")
    kotlin("plugin.serialization")
    id("com.google.devtools.ksp") version "2.0.21-1.0.27"
    id("androidx.room") version "2.7.0-alpha10"
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
