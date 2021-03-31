import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

plugins {
    id("com.github.ben-manes.versions")
}

buildscript {
    repositories {
        google()
        jcenter()
        mavenCentral()
    }

    dependencies {
        classpath(co.anitrend.buildSrc.Libraries.Android.Tools.buildGradle)
        classpath(co.anitrend.buildSrc.Libraries.JetBrains.Kotlin.Gradle.plugin)
        classpath(co.anitrend.buildSrc.Libraries.JetBrains.Kotlin.Serialization.serialization)

        classpath(co.anitrend.buildSrc.Libraries.Koin.Gradle.plugin)
        
        classpath(co.anitrend.buildSrc.Libraries.Google.Services.googleServices)
        classpath(co.anitrend.buildSrc.Libraries.Google.Firebase.Crashlytics.Gradle.plugin)
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        mavenCentral()
        maven {
            url = java.net.URI(co.anitrend.buildSrc.Libraries.Repositories.jitPack)
        }
        maven {
            url = java.net.URI(co.anitrend.buildSrc.Libraries.Repositories.dependencyUpdates)
        }
    }
}

plugins.apply("koin")

tasks {
    val clean by registering(Delete::class) {
        delete(rootProject.buildDir)
    }
}

tasks.named(
    "dependencyUpdates",
    DependencyUpdatesTask::class.java
).configure {
    checkForGradleUpdate = false
    outputFormatter = "json"
    outputDir = "build/dependencyUpdates"
    reportfileName = "report"
    resolutionStrategy {
        componentSelection {
            all {
                val reject = listOf("preview", "m")
                    .map { qualifier ->
                        val pattern = "(?i).*[.-]$qualifier[.\\d-]*"
                        Regex(pattern, RegexOption.IGNORE_CASE)
                    }
                    .any { it.matches(candidate.version) }
                if (reject)
                    reject("Preview releases not wanted")
            }
        }
    }
}