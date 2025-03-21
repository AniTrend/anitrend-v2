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
    configurations.all {
        handleConflicts(this@allprojects)
    }
}

plugins.forEach {
    project.logger.lifecycle("Available plugin for module $path -> ${it.javaClass}")
}

