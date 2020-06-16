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
import co.anitrend.buildSrc.common.*
import co.anitrend.buildSrc.common.core
import co.anitrend.buildSrc.common.data
import co.anitrend.buildSrc.common.domain
import co.anitrend.buildSrc.common.navigation
import co.anitrend.buildSrc.plugins.strategy.DependencyStrategy
import org.gradle.api.Project

private fun Project.applyPlatformDependencies() {
    println("Applying common dependencies for feature module -> $path")

    dependencies.add("implementation", Libraries.AniTrend.Arch.ui)
    dependencies.add("implementation", Libraries.AniTrend.Arch.ext)
    dependencies.add("implementation", Libraries.AniTrend.Arch.core)
    dependencies.add("implementation", Libraries.AniTrend.Arch.data)
    dependencies.add("implementation", Libraries.AniTrend.Arch.theme)
    dependencies.add("implementation", Libraries.AniTrend.Arch.domain)
    dependencies.add("implementation", Libraries.AniTrend.Arch.recycler)

    dependencies.add("implementation", Libraries.AndroidX.Collection.collectionKtx)

    dependencies.add("implementation", Libraries.AndroidX.Paging.runtime)
    dependencies.add("implementation", Libraries.AndroidX.Paging.runtimeKtx)

    dependencies.add("implementation", Libraries.Google.Material.material)

    dependencies.add("implementation", project(":$core"))
    dependencies.add("implementation", project(":$data"))
    dependencies.add("implementation", project(":$domain"))
    dependencies.add("implementation", project(":$navigation"))
}

private fun Project.applyFeatureModules() {
    featureModules.forEach { module ->
        println("Adding runtimeOnly dependency :$module -> ${project.path}")
        dependencies.add("runtimeOnly", project(":$module"))
    }
    dependencies.add("implementation", project(":$navigation"))
    dependencies.add("implementation", project(":$domain"))
    dependencies.add("implementation", project(":$data"))
    dependencies.add("implementation", project(":$core"))
}

internal fun Project.configureDependencies() {
    val dependencyStrategy = DependencyStrategy(project.name)
    dependencies.add("implementation",
        fileTree("libs") {
            include("*.jar")
        }
    )
    dependencyStrategy.applyDependenciesOn(dependencies)

    if (isFeatureModule()) applyPlatformDependencies()
    if (isAppModule()) applyFeatureModules()
}