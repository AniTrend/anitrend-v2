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

import co.anitrend.buildSrc.common.Configuration
import co.anitrend.buildSrc.extensions.isDataModule
import co.anitrend.buildSrc.extensions.isCoreModule
import co.anitrend.buildSrc.extensions.isAndroidCoreModule
import co.anitrend.buildSrc.extensions.matchesDataModule
import co.anitrend.buildSrc.extensions.libraryExtension
import com.android.build.gradle.BaseExtension
import com.android.build.api.dsl.LibraryBuildType
import com.android.build.api.dsl.LibraryDefaultConfig
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import java.util.*

private fun Properties.applyToBuildConfigFor(buildType: LibraryBuildType) {
    forEach { propEntry ->
        val key = propEntry.key as String
        val value = propEntry.value as String
        println("Adding build config field property -> key: $key value: $value")
        buildType.buildConfigField("String", key, value)
    }
}

private fun NamedDomainObjectContainer<LibraryBuildType>.applyVersionProperties() {
    asMap.forEach { buildTypeEntry ->
        println("Adding version build configuration fields -> ${buildTypeEntry.key}")
        val buildType = buildTypeEntry.value

        buildType.buildConfigField("String", "versionName", "\"${Configuration.versionName}\"")
        buildType.buildConfigField("int", "versionCode", Configuration.versionCode.toString())
    }
}

private fun NamedDomainObjectContainer<LibraryBuildType>.applyConfigurationProperties(project: Project) {
    asMap.forEach { buildTypeEntry ->
        println("Configuring build type -> ${buildTypeEntry.key}")
        val buildType = buildTypeEntry.value

        buildType.buildConfigField("String", "versionName", "\"${Configuration.versionName}\"")
        buildType.buildConfigField("int", "versionCode", Configuration.versionCode.toString())

        val secretsFile = project.file(".config/secrets.properties")
        if (secretsFile.exists())
            secretsFile.inputStream().use { fis ->
                Properties().run {
                    load(fis); applyToBuildConfigFor(buildType)
                }
            }

        val configurationFile = project.file(".config/configuration.properties")
        if (configurationFile.exists())
            configurationFile.inputStream().use { fis ->
                Properties().run {
                    load(fis); applyToBuildConfigFor(buildType)
                }
            }
    }
}

private fun LibraryDefaultConfig.applyRoomCompilerOptions(project: Project) {
    println("Adding java compiler options for room on module-> ${project.path}")
    javaCompileOptions {
        annotationProcessorOptions {
            arguments(
                mapOf(
                    "room.schemaLocation" to "${project.projectDir}/schemas",
                    "room.expandingProjections" to "true",
                    "room.incremental" to "true"
                )
            )
        }
    }
}

internal fun Project.createSigningConfiguration(extension: BaseExtension) {
    var properties: Properties? = null
    val keyStoreFile = project.file(".config/keystore.properties")
    if (keyStoreFile.exists())
        keyStoreFile.inputStream().use { fis ->
            Properties().run {
                load(fis);
                properties = this
            }
        }
    else println("${keyStoreFile.absolutePath} could not be found, automated releases may not be singed")
    properties?.also {
        extension.signingConfigs {
            create("release") {
                storeFile(file(it["STORE_FILE"] as String))
                storePassword(it["STORE_PASSWORD"] as String)
                keyAlias(it["STORE_KEY_ALIAS"] as String)
                keyPassword(it["STORE_KEY_PASSWORD"] as String)
            }
        }
    }
}

internal fun Project.configureOptions() {
    if (isDataModule() || matchesDataModule()) {
        libraryExtension().run {
            if (isDataModule())
                defaultConfig {
                    applyRoomCompilerOptions(this@configureOptions)
                }
            buildTypes {
                applyConfigurationProperties(this@configureOptions)
            }
        }
    }

    if (isCoreModule() || isAndroidCoreModule()) {
        libraryExtension().run {
            buildTypes {
                applyVersionProperties()
            }
        }
    }
}