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
        mavenLocal()
        google()
        jcenter()
        mavenCentral()
        maven {
            setUrl(co.anitrend.buildSrc.Libraries.Repositories.jitPack)
        }
        maven {
            setUrl(co.anitrend.buildSrc.Libraries.Repositories.sonatypeReleases)
        }
    }

    configurations.all {
        resolutionStrategy.eachDependency {
            when (requested.group) {
                "org.jetbrains.kotlin" -> {
                    when (requested.name) {
                        "kotlin-reflect",
                        "kotlin-stdlib",
                        "kotlin-stdlib-common",
                        "kotlin-stdlib-jdk8",
                        "kotlin-stdlib-jdk7" -> useVersion("1.4.32")
                    }
                }
            }
            if (requested.group == "com.google.android.material") {
                useTarget(co.anitrend.buildSrc.Libraries.Google.Material.material)
            }
            if (requested.group == "com.jakewharton.timber") {
                useTarget(co.anitrend.buildSrc.Libraries.timber)
            }
        }
    }
}

plugins.apply("koin")

tasks.create("clean", Delete::class) {
    delete(rootProject.buildDir)
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