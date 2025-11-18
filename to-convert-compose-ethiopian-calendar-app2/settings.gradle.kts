
pluginManagement {
    repositories {
        google()
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
    }
    // This part correctly declares the plugin's version
    plugins {
        id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0" // or a more recent version
    }
}


dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://company/com/maven2")
        }
        mavenLocal()
        flatDir {
            dirs("libs")
        }
    }
}

rootProject.name = "Compose-Ethiopian-Calendar"
include(":app")
include(":datepicker")
