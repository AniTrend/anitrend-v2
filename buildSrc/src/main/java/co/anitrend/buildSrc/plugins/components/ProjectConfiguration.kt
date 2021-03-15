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

import co.anitrend.buildSrc.Libraries
import co.anitrend.buildSrc.common.Versions
import co.anitrend.buildSrc.extensions.isAppModule
import co.anitrend.buildSrc.extensions.isDataModule
import co.anitrend.buildSrc.extensions.isCoreModule
import co.anitrend.buildSrc.extensions.hasComposeSupport
import co.anitrend.buildSrc.extensions.isNavigationModule
import co.anitrend.buildSrc.extensions.hasCoroutineSupport
import co.anitrend.buildSrc.extensions.matchesAppModule
import co.anitrend.buildSrc.extensions.matchesTaskModule
import co.anitrend.buildSrc.extensions.matchesCommonModule
import co.anitrend.buildSrc.extensions.matchesFeatureModule
import co.anitrend.buildSrc.extensions.baseAppExtension
import co.anitrend.buildSrc.extensions.baseExtension
import co.anitrend.buildSrc.extensions.libraryExtension
import com.android.build.gradle.internal.api.BaseVariantOutputImpl
import com.android.build.gradle.internal.dsl.DefaultConfig
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByName
import org.jetbrains.kotlin.gradle.dsl.KotlinCompile
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompile
import java.io.File

private fun configureBuildFlavours(project: Project) {
    project.baseAppExtension().run {
        flavorDimensions("version")
        productFlavors {
            create("fdroid") {
                dimension = "version"
                applicationIdSuffix = ".fdroid"
                versionNameSuffix = "-fdroid"
                versionCode = defaultConfig.versionCode!!.times(10)
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
                        val abi = output.getFilter("ABI") ?: "universal"
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
        project.baseAppExtension().buildFeatures {
            viewBinding = true
            compose = true
        }
        // TODO: Configure build flavours
        // this might be deprecated in favour of app bundles
        //configureBuildFlavours(project)
    }
    else
        consumerProguardFiles.add(File("consumer-rules.pro"))

    if (!project.matchesAppModule() && !project.matchesTaskModule()) {
        // checking app module again since the group for app modules is
        // `app-` while the main app is just `app`
        if (!project.isAppModule()) {
            println("Applying view binding feature for module -> ${project.path}")
            project.libraryExtension().buildFeatures {
                viewBinding = true
                if (project.hasComposeSupport())
                    compose = true
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
            isShrinkResources = false
            isTestCoverageEnabled = false
            proguardFiles(
                getDefaultProguardFile(
                    "proguard-android-optimize.txt"
                ),
                "proguard-rules.pro"
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
                "proguard-rules.pro"
            )
        }
    }

    packagingOptions {
        excludes.add("META-INF/NOTICE.txt")
        excludes.add("META-INF/LICENSE")
        excludes.add("META-INF/LICENSE.txt")
        // Exclude potential duplicate kotlin_module files
        excludes.add("META-INF/*kotlin_module")
        // Exclude consumer proguard files
        excludes.add("META-INF/proguard/*")
        // Exclude AndroidX version files
        excludes.add("META-INF/*.version")
        // Exclude the Firebase/Fabric/other random properties files
        excludes.add("META-INF/*.properties")
        excludes.add("/*.properties")
        excludes.add("fabric/*.properties")
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

    tasks.withType(KotlinJvmCompile::class.java) {
        kotlinOptions {
            jvmTarget = "1.8"
            // https://blog.jetbrains.com/kotlin/2021/02/the-jvm-backend-is-in-beta-let-s-make-it-stable-together/
            useIR
        }
    }

    tasks.withType(KotlinCompile::class.java) {
        val compilerArgumentOptions = mutableListOf(
            "-Xuse-experimental=kotlin.Experimental",
            "-Xopt-in=kotlin.ExperimentalStdlibApi",
            "-Xopt-in=kotlin.Experimental"
        )

        if (hasCoroutineSupport()) {
            compilerArgumentOptions.add("-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi")
            compilerArgumentOptions.add("-Xopt-in=kotlinx.coroutines.FlowPreview")
        }

        if (hasComposeSupport()) {
            compilerArgumentOptions.add("-Xopt-in=androidx.compose.foundation.ExperimentalFoundationApi")
            compilerArgumentOptions.add("-Xopt-in=androidx.compose.material.ExperimentalMaterialApi")
        }
        
        if (isAppModule() || isCoreModule() || isNavigationModule())
			compilerArgumentOptions.apply {
				add("-Xopt-in=org.koin.core.component.KoinExperimentalAPI")
				add("-Xopt-in=org.koin.core.component.KoinApiExtension")
				add("-Xopt-in=org.koin.core.KoinExperimentalAPI")
			}
		
        kotlinOptions {
            allWarningsAsErrors = false
            // Filter out modules that won't be using coroutines
            freeCompilerArgs = compilerArgumentOptions
        }
    }

    if (project.hasComposeSupport()) {
        composeOptions {
            kotlinCompilerExtensionVersion = Libraries.AndroidX.Compose.version
            kotlinCompilerVersion = Libraries.JetBrains.Kotlin.version
        }
    }
}
