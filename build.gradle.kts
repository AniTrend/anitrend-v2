import co.anitrend.buildSrc.resolver.handleConflicts
import co.anitrend.buildSrc.resolver.handleDependencySelection
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
        classpath(libs.android.gradle.plugin)
        classpath(libs.jetbrains.kotlin.gradle)
        classpath(libs.jetbrains.kotlin.serialization)

        classpath(co.anitrend.buildSrc.Libraries.Koin.Gradle.plugin)

        classpath(libs.google.gms.google.services)
        classpath(libs.google.firebase.crashlytics.gradle)
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
        handleConflicts(this@allprojects)
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
                handleDependencySelection()
            }
        }
    }
}