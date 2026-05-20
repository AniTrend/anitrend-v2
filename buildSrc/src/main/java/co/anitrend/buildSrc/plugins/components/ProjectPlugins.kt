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

import co.anitrend.buildSrc.extensions.androidComponents
import co.anitrend.buildSrc.extensions.hasComposeSupport
import co.anitrend.buildSrc.extensions.hasParcelizeSupport
import co.anitrend.buildSrc.extensions.hasKspSupport
import co.anitrend.buildSrc.extensions.isAppModule
import org.gradle.api.Project
import org.gradle.api.plugins.PluginContainer

private fun addAndroidPlugin(project: Project, pluginContainer: PluginContainer) {
    if (project.isAppModule()) pluginContainer.apply("com.android.application")
    else pluginContainer.apply("com.android.library")
}

private fun addCoreAndroidPlugins(pluginContainer: PluginContainer) {
    pluginContainer.apply("com.diffplug.spotless")
}

private fun addParcelizePlugin(project: Project, pluginContainer: PluginContainer) {
    if (project.hasParcelizeSupport())
        pluginContainer.apply("org.jetbrains.kotlin.plugin.parcelize")
}

private fun addSymbolProcessingPlugin(project: Project, pluginContainer: PluginContainer) {
    if (project.hasKspSupport())
        pluginContainer.apply("com.google.devtools.ksp")
}

internal fun Project.configurePlugins() {
    addAndroidPlugin(project, plugins)
    addCoreAndroidPlugins(plugins)
    addParcelizePlugin(project, plugins)
    addSymbolProcessingPlugin(project, plugins)

    if (project.hasComposeSupport()) {
        plugins.apply("org.jetbrains.kotlin.plugin.compose")
    }
}

internal fun Project.configureAdditionalPlugins() {
    if (isAppModule()) {
        androidComponents().beforeVariants {
            logger.lifecycle("VariantFilter { name: ${it.name}, flavor: ${it.flavorName}, module: $path }")
            if (it.flavorName == "google") {
                logger.lifecycle("Applying additional google plugins on -> module: $path | type: ${it.name}")
                if (file("google-services.json").exists()) {
                    plugins.apply("com.google.gms.google-services")
                    plugins.apply("com.google.firebase.crashlytics")
                } else logger.lifecycle("google-services.json cannot be found and will not be using any of the google plugins")
            }
        }
    }
}
