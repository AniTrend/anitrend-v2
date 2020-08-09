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

private fun Project.applyFeatureModuleDependencies() {
    println("Applying shared dependencies for feature module -> $path")

    dependencies.add("implementation", Libraries.AniTrend.Arch.ui)
    dependencies.add("implementation", Libraries.AniTrend.Arch.ext)
    dependencies.add("implementation", Libraries.AniTrend.Arch.core)
    dependencies.add("implementation", Libraries.AniTrend.Arch.data)
    dependencies.add("implementation", Libraries.AniTrend.Arch.theme)
    dependencies.add("implementation", Libraries.AniTrend.Arch.domain)
    dependencies.add("implementation", Libraries.AniTrend.Arch.recycler)

    dependencies.add("implementation", Libraries.AndroidX.Core.coreKtx)
    dependencies.add("implementation", Libraries.AndroidX.Work.runtimeKtx)
    dependencies.add("implementation", Libraries.AndroidX.Paging.runtimeKtx)
    dependencies.add("implementation", Libraries.AndroidX.Activity.activityKtx)
    dependencies.add("implementation", Libraries.AndroidX.Fragment.fragmentKtx)
    dependencies.add("implementation", Libraries.AndroidX.StartUp.startUpRuntime)
    dependencies.add("implementation", Libraries.AndroidX.Collection.collectionKtx)
    dependencies.add("implementation", Libraries.AndroidX.SwipeRefresh.swipeRefreshLayout)
    dependencies.add("implementation", Libraries.AndroidX.ContraintLayout.constraintLayout)

    dependencies.add("implementation", Libraries.Google.Material.material)

    dependencies.add("implementation", project(":$core"))
    dependencies.add("implementation", project(":$data"))
    dependencies.add("implementation", project(":$domain"))
    dependencies.add("implementation", project(":$navigation"))
    dependencies.add("implementation", project(":$androidCore"))
}

private fun Project.applyAppModuleDependencies() {
    featureModules.forEach { module ->
        println("Adding runtimeOnly dependency :$module -> ${project.path}")
        dependencies.add("runtimeOnly", project(":$module"))
    }

    baseModules.forEach { module ->
        if (module != app) {
            println("Adding base module dependency :$module -> ${project.path}")
            dependencies.add("implementation", project(":$module"))
        }
    }

    androidModules.forEach { module ->
        println("Adding android core module dependency :$module -> ${project.path}")
        dependencies.add("implementation", project(":$module"))
    }

    dependencies.add("implementation", Libraries.Google.Material.material)

    dependencies.add("implementation", Libraries.AndroidX.Core.coreKtx)
    dependencies.add("implementation", Libraries.AndroidX.Work.runtimeKtx)
    dependencies.add("implementation", Libraries.AndroidX.Activity.activityKtx)
    dependencies.add("implementation", Libraries.AndroidX.Fragment.fragmentKtx)
    dependencies.add("implementation", Libraries.AndroidX.StartUp.startUpRuntime)
    dependencies.add("implementation", Libraries.AndroidX.ContraintLayout.constraintLayout)

    dependencies.add("implementation", Libraries.Coil.coil)
}

private fun Project.applyBaseModuleDependencies() {
    println("Applying base module dependencies for module -> $path")
    when (name) {
        core -> {
            dependencies.add("implementation", project(":$navigation"))
            dependencies.add("implementation", project(":$domain"))
            dependencies.add("implementation", project(":$data"))

            dependencies.add("implementation", Libraries.Google.Material.material)

            dependencies.add("implementation", Libraries.AndroidX.Core.coreKtx)
            dependencies.add("implementation", Libraries.AndroidX.Work.runtimeKtx)
            dependencies.add("implementation", Libraries.AndroidX.Activity.activityKtx)
            dependencies.add("implementation", Libraries.AndroidX.Fragment.fragmentKtx)
            dependencies.add("implementation", Libraries.AndroidX.StartUp.startUpRuntime)
            dependencies.add("implementation", Libraries.AndroidX.SwipeRefresh.swipeRefreshLayout)
            dependencies.add("implementation", Libraries.AndroidX.ContraintLayout.constraintLayout)

            dependencies.add("implementation", Libraries.AndroidX.Collection.collectionKtx)
            dependencies.add("implementation", Libraries.AndroidX.Recycler.recyclerView)
            dependencies.add("implementation", Libraries.AndroidX.Paging.runtimeKtx)

            dependencies.add("implementation", Libraries.Coil.coil)
            dependencies.add("implementation", Libraries.Coil.gif)
            dependencies.add("implementation", Libraries.Coil.svg)
            dependencies.add("implementation", Libraries.Coil.video)
            dependencies.add("implementation", Libraries.Glide.glide)
            dependencies.add("kapt", Libraries.Glide.compiler)
        }
        data -> {
            dependencies.add("implementation", project(":$domain"))

            dependencies.add("implementation", Libraries.AndroidX.Paging.common)
            dependencies.add("implementation", Libraries.AndroidX.Paging.runtime)
            dependencies.add("implementation", Libraries.AndroidX.Paging.runtimeKtx)
            dependencies.add("implementation", Libraries.AndroidX.Room.runtime)
            dependencies.add("implementation", Libraries.AndroidX.Room.ktx)
            dependencies.add("kapt", Libraries.AndroidX.Room.compiler)

            dependencies.add("implementation", Libraries.Square.Retrofit.retrofit)
            dependencies.add("implementation", Libraries.Square.Retrofit.gsonConverter)
            dependencies.add("implementation", Libraries.Square.Retrofit.xmlConverter)
            dependencies.add("implementation", Libraries.Square.OkHttp.logging)

            dependencies.add("implementation", Libraries.AniTrend.Retrofit.graphQL)
        }
        domain -> {
            dependencies.add("implementation", Libraries.AniTrend.Arch.domain)
        }
        navigation -> {
            dependencies.add("implementation", Libraries.AndroidX.Core.coreKtx)
            dependencies.add("implementation", Libraries.AndroidX.Activity.activityKtx)
            dependencies.add("implementation", Libraries.AndroidX.Collection.collectionKtx)
            dependencies.add("implementation", Libraries.AndroidX.Fragment.fragment)
        }
    }
}

private fun Project.applyAndroidCoreModuleDependencies() {
    println("Applying core feature dependencies for feature module -> $path")

    dependencies.add("implementation", Libraries.AniTrend.Arch.ui)
    dependencies.add("implementation", Libraries.AniTrend.Arch.ext)
    dependencies.add("implementation", Libraries.AniTrend.Arch.core)
    dependencies.add("implementation", Libraries.AniTrend.Arch.data)
    dependencies.add("implementation", Libraries.AniTrend.Arch.theme)
    dependencies.add("implementation", Libraries.AniTrend.Arch.domain)

    dependencies.add("implementation", Libraries.AndroidX.Core.coreKtx)
    dependencies.add("implementation", Libraries.AndroidX.Work.runtimeKtx)
    dependencies.add("implementation", Libraries.AndroidX.Paging.runtimeKtx)
    dependencies.add("implementation", Libraries.AndroidX.Activity.activityKtx)
    dependencies.add("implementation", Libraries.AndroidX.Fragment.fragmentKtx)
    dependencies.add("implementation", Libraries.AndroidX.StartUp.startUpRuntime)
    dependencies.add("implementation", Libraries.AndroidX.Collection.collectionKtx)
    dependencies.add("implementation", Libraries.AndroidX.ContraintLayout.constraintLayout)

    dependencies.add("implementation", Libraries.Google.Material.material)

    dependencies.add("implementation", Libraries.Coil.coil)

    dependencies.add("implementation", project(":$core"))
    dependencies.add("implementation", project(":$data"))
    dependencies.add("implementation", project(":$domain"))
    dependencies.add("implementation", project(":$navigation"))
    if (!isAndroidCoreModule()) {
        dependencies.add("implementation", project(":$androidCore"))
    }
}

private fun Project.applyCommonModuleDependencies() {
    println("Applying common feature dependencies for feature module -> $path")

    dependencies.add("implementation", Libraries.AniTrend.Arch.ui)
    dependencies.add("implementation", Libraries.AniTrend.Arch.ext)
    dependencies.add("implementation", Libraries.AniTrend.Arch.core)
    dependencies.add("implementation", Libraries.AniTrend.Arch.data)
    dependencies.add("implementation", Libraries.AniTrend.Arch.theme)
    dependencies.add("implementation", Libraries.AniTrend.Arch.domain)
    dependencies.add("implementation", Libraries.AniTrend.Arch.recycler)

    dependencies.add("implementation", Libraries.AndroidX.Core.coreKtx)
    dependencies.add("implementation", Libraries.AndroidX.Work.runtimeKtx)
    dependencies.add("implementation", Libraries.AndroidX.Paging.runtimeKtx)
    dependencies.add("implementation", Libraries.AndroidX.Activity.activityKtx)
    dependencies.add("implementation", Libraries.AndroidX.Fragment.fragmentKtx)
    dependencies.add("implementation", Libraries.AndroidX.StartUp.startUpRuntime)
    dependencies.add("implementation", Libraries.AndroidX.Collection.collectionKtx)
    dependencies.add("implementation", Libraries.AndroidX.ContraintLayout.constraintLayout)

    dependencies.add("implementation", Libraries.Google.Material.material)

    dependencies.add("implementation", Libraries.Coil.coil)
    dependencies.add("implementation", Libraries.Glide.glide)

    dependencies.add("implementation", project(":$androidCore"))
    dependencies.add("implementation", project(":$core"))
    dependencies.add("implementation", project(":$data"))
    dependencies.add("implementation", project(":$domain"))
    dependencies.add("implementation", project(":$navigation"))
}

internal fun Project.configureDependencies() {
    val dependencyStrategy = DependencyStrategy(project.name)
    dependencies.add("implementation",
        fileTree("libs") {
            include("*.jar")
        }
    )
    dependencyStrategy.applyDependenciesOn(dependencies)

    if (isFeatureModule()) applyFeatureModuleDependencies()
    if (isAppModule()) applyAppModuleDependencies()
    if (isBaseModule()) applyBaseModuleDependencies()
    if (isCoreFeatureModule()) applyAndroidCoreModuleDependencies()
    if (isCommonFeatureModule()) applyCommonModuleDependencies()
}