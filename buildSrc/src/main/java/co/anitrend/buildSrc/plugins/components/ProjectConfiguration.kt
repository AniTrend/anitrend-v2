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

import co.anitrend.buildSrc.common.Versions
import co.anitrend.buildSrc.common.hasCoroutineSupport
import co.anitrend.buildSrc.common.isAppModule
import co.anitrend.buildSrc.common.isBaseModule
import co.anitrend.buildSrc.plugins.extensions.baseAppExtension
import co.anitrend.buildSrc.plugins.extensions.baseExtension
import co.anitrend.buildSrc.plugins.extensions.libraryExtension
import com.android.build.OutputFile
import com.android.build.gradle.internal.api.BaseVariantOutputImpl
import com.android.build.gradle.internal.dsl.DefaultConfig
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinCompile
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompile
import java.io.File

private fun configureMultipleBuilds(project: Project) {
    val abiMap = mapOf(
        "armeabi" to 1,
        "armeabi-v7a" to 2,
        "arm64-v8a" to 3,
        "mips" to 5,
        "mips64" to 6,
        "x86" to 8,
        "x86_64" to 9
    )
    project.baseAppExtension().run {
        flavorDimensions("version")
        productFlavors {
            create("fdroid") {
                dimension = "version"
                applicationIdSuffix = ".fdroid"
                versionNameSuffix = "-fdroid"
                versionCode = 1000 * defaultConfig.versionCode!!
            }
        }
        splits {
            abi {
                isEnable = true
                reset()
                include(*abiMap.keys.toTypedArray())
                isUniversalApk = true
            }
        }
        applicationVariants.all {
            outputs.map { it as BaseVariantOutputImpl }.forEach { output ->
                val original = output.outputFileName
                val versionCode = defaultConfig.versionCode
                val versionName = defaultConfig.versionName
                val currentName = "app-${output.name}.apk"
                val prefix = "anitrend_v${versionName}_rc_${versionCode}"
                val outputFileName = when (output.name) {
                    "release" -> {
                        val abi = output.getFilter(OutputFile.ABI) ?: "universal"
                        original.replace(
                            currentName, "${prefix}_${abi}-${output.name}.apk"
                        )
                    }
                    else ->
                        original.replace(
                            currentName, "${prefix}-${output.name}.apk"
                        )
                }
                output.outputFileName = outputFileName
            }
        }
    }
}

@Suppress("UnstableApiUsage")
private fun DefaultConfig.applyAdditionalConfiguration(project: Project) {
    if (project.isAppModule()) {
        applicationId = "co.anitrend"
        // TODO: Configure flavours and multiple apk support
        //configureMultipleBuilds(project)
    }
    else
        consumerProguardFiles.add(File("consumer-rules.pro"))

    if (!project.isBaseModule()) {
        if (!project.isAppModule()) {
            println("Applying view binding feature for module -> ${project.path}")
            project.libraryExtension().buildFeatures {
                viewBinding = true
            }
        }

        println("Applying vector drawables configuration for module -> ${project.path}")
        vectorDrawables.useSupportLibrary = true
    }
}

internal fun Project.configureAndroid(): Unit = baseExtension().run {
    compileSdkVersion(Versions.compileSdk)
    defaultConfig {
        minSdkVersion(Versions.minSdk)
        targetSdkVersion(Versions.targetSdk)
        versionCode = Versions.versionCode
        versionName = Versions.versionName
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        applyAdditionalConfiguration(project)
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isTestCoverageEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        getByName("debug") {
            isMinifyEnabled = false
            isTestCoverageEnabled = true
        }
    }

    packagingOptions {
        exclude("META-INF/NOTICE.txt")
        exclude("META-INF/LICENSE")
        exclude("META-INF/LICENSE.txt")
    }

    sourceSets {
        map { androidSourceSet ->
            androidSourceSet.java.srcDir(
                "src/${androidSourceSet.name}/kotlin"
            )
        }
    }

    testOptions {
        unitTests.isReturnDefaultValues = true
    }

    lintOptions {
        isAbortOnError = false
        isIgnoreWarnings = false
        isIgnoreTestSources = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    tasks.withType(KotlinCompile::class.java) {
        val compilerArgumentOptions = if (hasCoroutineSupport()) {
            listOf(
                "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                "-Xopt-in=kotlinx.coroutines.FlowPreview",
                "-Xopt-in=kotlinx.coroutines.FlowPreview",
                "-Xuse-experimental=kotlin.Experimental",
                "-Xopt-in=kotlin.Experimental"
            )
        } else {
            listOf(
                "-Xuse-experimental=kotlin.Experimental",
                "-Xopt-in=kotlin.Experimental"
            )
        }
        kotlinOptions {
            allWarningsAsErrors = false
            // Filter out modules that won't be using coroutines
            freeCompilerArgs = compilerArgumentOptions
        }
    }

    tasks.withType(KotlinJvmCompile::class.java) {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }
}