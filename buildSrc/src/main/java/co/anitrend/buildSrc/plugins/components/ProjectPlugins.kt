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

import co.anitrend.buildSrc.common.app
import co.anitrend.buildSrc.common.data
import org.gradle.api.Project
import org.gradle.api.plugins.PluginContainer

private fun addAndroidPlugin(name: String, pluginContainer: PluginContainer) {
    if (name == app)
        pluginContainer.apply("com.android.application")
    else pluginContainer.apply("com.android.library")
}

private fun addAnnotationProcessor(name: String, pluginContainer: PluginContainer) {
    if (name == data || name == app)
        pluginContainer.apply("kotlin-kapt")
}

internal fun Project.configurePlugins() {
    addAndroidPlugin(project.name, plugins)
    plugins.apply("kotlin-android")
    plugins.apply("kotlin-android-extensions")
    addAnnotationProcessor(project.name, plugins)
}