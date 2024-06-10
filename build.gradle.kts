import co.anitrend.buildSrc.Libraries.Repositories
import co.anitrend.buildSrc.resolver.handleConflicts

plugins {
    id("org.jetbrains.kotlin.plugin.compose")
}

buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath(libs.android.gradle.plugin)
        classpath(libs.jetbrains.kotlin.gradle)
        classpath(libs.jetbrains.kotlin.serialization)

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
            setUrl(Repositories.jitPack)
        }
        maven {
            setUrl(Repositories.sonatypeReleases)
        }
    }

    configurations.all {
        handleConflicts(this@allprojects)
    }
}

tasks.create("clean", Delete::class) {
    delete(rootProject.layout.buildDirectory)
}
