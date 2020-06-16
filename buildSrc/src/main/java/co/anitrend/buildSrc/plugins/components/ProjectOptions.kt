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

import co.anitrend.buildSrc.common.isDataModule
import co.anitrend.buildSrc.common.isFeatureModule
import co.anitrend.buildSrc.plugins.extensions.androidExtensionsExtension
import co.anitrend.buildSrc.plugins.extensions.libraryExtension
import com.android.build.gradle.internal.dsl.BuildType
import com.android.build.gradle.internal.dsl.DefaultConfig
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import java.util.*

private fun Properties.applyToBuildConfigForBuild(buildType: BuildType) {
    forEach { propEntry ->
        val key = propEntry.key as String
        val value = propEntry.value as String
        println("Adding build config field property -> key: $key value: $value")
        buildType.buildConfigField("String", key, value)
    }
}

private fun NamedDomainObjectContainer<BuildType>.applyConfiguration(project: Project) {
    asMap.forEach { buildTypeEntry ->
        println("Configuring build type -> ${buildTypeEntry.key}")
        val buildType = buildTypeEntry.value

        val secretsFile = project.file(".config/secrets.properties")
        if (secretsFile.exists())
            secretsFile.inputStream().use { fis ->
                Properties().run {
                    load(fis); applyToBuildConfigForBuild(buildType)
                }
            }

        val configurationFile = project.file(".config/configuration.properties")
        if (configurationFile.exists())
            configurationFile.inputStream().use { fis ->
                Properties().run {
                    load(fis); applyToBuildConfigForBuild(buildType)
                }
            }
    }
}

private fun DefaultConfig.applyCompilerOptions(project: Project) {
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

internal fun Project.configureOptions() {
    if (isDataModule()) {
        libraryExtension().run {
            defaultConfig {
                applyCompilerOptions(this@configureOptions)
            }
            buildTypes {
                applyConfiguration(this@configureOptions)
            }
        }
    }

    if (!isFeatureModule()) return

    println("Applying extension options for feature module -> ${project.path}")
    androidExtensionsExtension().isExperimental = true
}