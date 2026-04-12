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
import co.anitrend.buildSrc.extensions.commonExtension
import co.anitrend.buildSrc.extensions.hasComposeSupport
import co.anitrend.buildSrc.extensions.hasCoroutineSupport
import co.anitrend.buildSrc.extensions.isAppModule
import co.anitrend.buildSrc.extensions.isCoreModule
import co.anitrend.buildSrc.extensions.libraryExtension
import co.anitrend.buildSrc.extensions.matchesAppModule
import co.anitrend.buildSrc.extensions.matchesTaskModule
import co.anitrend.buildSrc.extensions.props
import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile
import java.io.File

private fun Project.configureBuildFlavours() {
    applicationExtension().run {
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

private fun CommonExtension<*, *, *, *, *, *>.configureBaseAndroid(project: Project) {
    compileSdk = 36
    defaultConfig {
        minSdk = 24
        targetSdk = 36
        versionCode = project.props[PropertyTypes.CODE].toInt()
        versionName = project.props[PropertyTypes.VERSION]
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = false
            isTestCoverageEnabled = false
            proguardFiles(
                getDefaultProguardFile(
                    "proguard-android-optimize.txt"
                ),
                projectDir.resolve("proguard-rules.pro"),
                rootDir.resolve("proguard-common.pro"),
            )
        }

        getByName("debug") {
            isDebuggable = true
            isMinifyEnabled = false
            isShrinkResources = false
            isTestCoverageEnabled = true
            proguardFiles(
                getDefaultProguardFile(
                    "proguard-android-optimize.txt"
                ),
                projectDir.resolve("proguard-rules.pro"),
                rootDir.resolve("proguard-common.pro"),
            )
        }
    }

    packaging {
        resources {
            excludes.add("META-INF/NOTICE.*")
            excludes.add("META-INF/LICENSE*")
            excludes.add("META-INF/*kotlin_module")
            excludes.add("META-INF/proguard/*")
            excludes.add("META-INF/*.version")
            excludes.add("META-INF/*.properties")
            excludes.add("/*.properties")
            excludes.add("fabric/*.properties")
        }
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

private fun ApplicationExtension.applyAdditionalConfiguration(project: Project) {
    defaultConfig {
        applicationId = "co.anitrend"
    }

    buildFeatures {
        viewBinding = true
        compose = true
    }

    buildTypes {
        getByName("release") {
            if (project.file(".config/keystore.properties").exists())
                signingConfig = signingConfigs.getByName("release")
        }
    }
}

private fun LibraryExtension.applyAdditionalConfiguration(project: Project) {
    defaultConfig {
        consumerProguardFiles.add(project.file("consumer-rules.pro"))
        if (!project.matchesAppModule() && !project.matchesTaskModule()) {
            project.logger.lifecycle("Applying vector drawables configuration for module -> ${project.path}")
            vectorDrawables.useSupportLibrary = true
        }
    }

    if (!project.matchesAppModule() && !project.matchesTaskModule() && project.hasComposeSupport()) {
        project.logger.lifecycle("Applying view binding and compose build features for module -> ${project.path}")
        buildFeatures {
            viewBinding = true
            compose = true
        }
    }
}

private fun Project.configureLint() = applicationExtension().run {
    lint {
        abortOnError = false
        ignoreWarnings = false
        ignoreTestSources = true
    }
}

internal fun Project.configureAndroid(): Unit = commonExtension().run {
    configureBaseAndroid(project)
    if (isAppModule()) {
        applicationExtension().applyAdditionalConfiguration(project)
        configureLint()
        configureBuildFlavours()
        createSigningConfiguration(applicationExtension())
    }
    else {
        libraryExtension().applyAdditionalConfiguration(project)
    }

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

    // Disabling experimental language version, causing issues with KAPT + Room
    //tasks.withType(KotlinCompilationTask::class.java)
    //    .configureEach {
    //        compilerOptions
    //            .languageVersion
    //            .set(KotlinVersion.KOTLIN_1_9)
    //    }

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
