/*
 * Copyright (C) 2020  AniTrend
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package co.anitrend.buildSrc.plugins.components

import co.anitrend.buildSrc.extensions.applicationExtension
import co.anitrend.buildSrc.extensions.hasComposeSupport
import co.anitrend.buildSrc.extensions.hasCoroutineSupport
import co.anitrend.buildSrc.extensions.isAppModule
import co.anitrend.buildSrc.extensions.isCoreModule
import co.anitrend.buildSrc.extensions.libraryExtension
import co.anitrend.buildSrc.extensions.matchesAppModule
import co.anitrend.buildSrc.extensions.matchesTaskModule
import co.anitrend.buildSrc.extensions.props
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile
import java.io.File

private fun Project.configureBuildFlavours() {
    applicationExtension().apply {
        flavorDimensions.add("default")
        productFlavors {
            create("google") {
                dimension = "default"
                isDefault = true
            }
            create("github") {
                dimension = "default"
                versionNameSuffix = "-github"
            }
        }
    }
}

private fun Project.configureLint() = applicationExtension().lint {
    abortOnError = false
    ignoreWarnings = false
    ignoreTestSources = true
}

private fun Project.configureAppAndroid() {
    val ext = applicationExtension()
    createSigningConfiguration(ext)
    configureLint()
    configureBuildFlavours()
    ext.apply {
        compileSdk = 36
        defaultConfig {
            applicationId = "co.anitrend"
            minSdk = 24
            targetSdk = 36
            versionCode = props[PropertyTypes.CODE].toInt()
            versionName = props[PropertyTypes.VERSION]
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
        buildFeatures {
            viewBinding = true
            compose = true
        }
        buildTypes {
            getByName("release") {
                isMinifyEnabled = true
                isShrinkResources = false
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    projectDir.resolve("proguard-rules.pro"),
                    rootDir.resolve("proguard-common.pro"),
                )
                if (project.file(".config/keystore.properties").exists())
                    signingConfig = signingConfigs.getByName("release")
            }
            getByName("debug") {
                isDebuggable = true
                isMinifyEnabled = false
                isShrinkResources = false
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    projectDir.resolve("proguard-rules.pro"),
                    rootDir.resolve("proguard-common.pro"),
                )
            }
        }
        packaging {
            resources.excludes.add("META-INF/NOTICE.*")
            resources.excludes.add("META-INF/LICENSE*")
            resources.excludes.add("META-INF/*kotlin_module")
            resources.excludes.add("META-INF/proguard/*")
            resources.excludes.add("META-INF/*.version")
            resources.excludes.add("META-INF/*.properties")
            resources.excludes.add("/*.properties")
            resources.excludes.add("fabric/*.properties")
        }
        sourceSets {
            map { androidSourceSet ->
                androidSourceSet.java.srcDir(
                    "src/${androidSourceSet.name}/kotlin"
                )
            }
        }
        testOptions {
            unitTests {
                isReturnDefaultValues = true
                isIncludeAndroidResources = true
            }
        }
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_21
            targetCompatibility = JavaVersion.VERSION_21
        }
    }
}

private fun Project.configureLibraryAndroid() = libraryExtension().apply {
    compileSdk = 36
    defaultConfig {
        minSdk = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles.add(File("consumer-rules.pro"))
        vectorDrawables.useSupportLibrary = true
    }
    if (!matchesAppModule() && !matchesTaskModule() && hasComposeSupport()) {
        logger.lifecycle("Applying view binding and compose build features for module -> $path")
        buildFeatures {
            viewBinding = true
            compose = true
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                projectDir.resolve("proguard-rules.pro"),
                rootDir.resolve("proguard-common.pro"),
            )
        }
        getByName("debug") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                projectDir.resolve("proguard-rules.pro"),
                rootDir.resolve("proguard-common.pro"),
            )
        }
    }
    packaging {
        resources.excludes.add("META-INF/NOTICE.*")
        resources.excludes.add("META-INF/LICENSE*")
        resources.excludes.add("META-INF/*kotlin_module")
        resources.excludes.add("META-INF/proguard/*")
        resources.excludes.add("META-INF/*.version")
        resources.excludes.add("META-INF/*.properties")
        resources.excludes.add("/*.properties")
        resources.excludes.add("fabric/*.properties")
    }
    sourceSets {
        map { androidSourceSet ->
            androidSourceSet.java.srcDir(
                "src/${androidSourceSet.name}/kotlin"
            )
        }
    }
    testOptions {
        unitTests {
            isReturnDefaultValues = true
            isIncludeAndroidResources = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}

internal fun Project.configureAndroid() {
    if (isAppModule()) configureAppAndroid()
    else configureLibraryAndroid()

    tasks.withType(KotlinJvmCompile::class.java) {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }

    tasks.withType(KotlinCompilationTask::class.java) {
        val compilerArgumentOptions = mutableListOf(
            "-opt-in=kotlin.ExperimentalStdlibApi",
        )

        if (hasCoroutineSupport()) {
            compilerArgumentOptions.add("-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi")
            compilerArgumentOptions.add("-opt-in=kotlinx.coroutines.FlowPreview")
        }

        if (hasComposeSupport()) {
            compilerArgumentOptions.add("-opt-in=androidx.compose.foundation.ExperimentalFoundationApi")
            compilerArgumentOptions.add("-opt-in=androidx.compose.material.ExperimentalMaterialApi")
        }

        if (isAppModule() || isCoreModule()) {
            compilerArgumentOptions.apply {
                add("-opt-in=org.koin.core.component.KoinApiExtension")
                add("-opt-in=org.koin.viewmodel.KoinInternalApi")
                add("-opt-in=org.koin.core.KoinExperimentalAPI")
            }
        }

        compilerOptions {
            allWarningsAsErrors.set(false)
            // Filter out modules that won't be using coroutines
            freeCompilerArgs.addAll(compilerArgumentOptions)
        }
    }

    tasks.withType(Test::class.java) {
        useJUnitPlatform()

        // Gradle 9 fails test tasks when no tests are discovered, even when generated
        // Android unit-test classes are present in modules without real test cases.
        failOnNoDiscoveredTests.set(false)

        maxHeapSize = "1G"

        testLogging {
            events("passed", "skipped", "failed")
            exceptionFormat = TestExceptionFormat.FULL
            showStandardStreams = true
        }
    }

    tasks.register("makeProguard") {
        val projectDirectory = project.layout.projectDirectory
        val buildDirectory = project.layout.buildDirectory
        val proguardFile = projectDirectory.file("proguard-rules.pro").asFile
        val missingRules = buildDirectory.file("outputs/mapping/release/missing_rules.txt").get().asFile
        if (proguardFile.exists()) {
            if (missingRules.exists() && missingRules.length() > 0) {
                val contents = missingRules.readText()
                val existing = proguardFile.readText()
                if (proguardFile.length() < 1) {
                    logger.lifecycle("Creating proguard-rules.pro $proguardFile with contents from $missingRules")
                    proguardFile.writeText(contents)
                } else {
                    if (contents != existing) {
                        logger.lifecycle("Updating proguard-rules.pro $proguardFile with contents from $missingRules")
                        proguardFile.appendText(contents)
                    } else {
                        logger.lifecycle("$proguardFile contents are identical to $missingRules")
                    }
                }
            }
        } else {
            logger.lifecycle("Creating new proguard-rules.pro file in: $proguardFile")
            proguardFile.createNewFile()
        }
    }
}
