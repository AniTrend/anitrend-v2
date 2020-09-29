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
import co.anitrend.buildSrc.plugins.extensions.androidTest
import co.anitrend.buildSrc.plugins.extensions.implementation
import co.anitrend.buildSrc.plugins.extensions.kapt
import co.anitrend.buildSrc.plugins.extensions.runtime
import co.anitrend.buildSrc.plugins.strategy.DependencyStrategy
import org.gradle.api.Project

private fun Project.applyFeatureModuleDependencies() {
    println("Applying shared dependencies for feature module -> $path")

    dependencies.implementation(Libraries.AniTrend.Arch.ui)
    dependencies.implementation(Libraries.AniTrend.Arch.ext)
    dependencies.implementation(Libraries.AniTrend.Arch.core)
    dependencies.implementation(Libraries.AniTrend.Arch.data)
    dependencies.implementation(Libraries.AniTrend.Arch.theme)
    dependencies.implementation(Libraries.AniTrend.Arch.domain)
    dependencies.implementation(Libraries.AniTrend.Arch.recycler)

    dependencies.implementation(Libraries.AndroidX.Core.coreKtx)
    dependencies.implementation(Libraries.AndroidX.Work.runtimeKtx)
    dependencies.implementation(Libraries.AndroidX.Paging.runtimeKtx)
    dependencies.implementation(Libraries.AndroidX.Activity.activityKtx)
    dependencies.implementation(Libraries.AndroidX.Fragment.fragmentKtx)
    dependencies.implementation(Libraries.AndroidX.StartUp.startUpRuntime)
    dependencies.implementation(Libraries.AndroidX.Collection.collectionKtx)
    dependencies.implementation(Libraries.AndroidX.SwipeRefresh.swipeRefreshLayout)
    dependencies.implementation(Libraries.AndroidX.ConstraintLayout.constraintLayout)

    dependencies.implementation(Libraries.Google.Material.material)

    dependencies.implementation(Libraries.threeTenBp)

    dependencies.implementation(project(":$core"))
    dependencies.implementation(project(":$data"))
    dependencies.implementation(project(":$domain"))
    dependencies.implementation(project(":$navigation"))
    dependencies.implementation(project(":$androidCore"))
}

private fun Project.applyAppModuleDependencies() {
    featureModules.forEach { module ->
        println("Adding runtimeOnly dependency :$module -> ${project.path}")
        dependencies.runtime(project(":$module"))
    }

    baseModules.forEach { module ->
        if (module != app) {
            println("Adding base module dependency :$module -> ${project.path}")
            dependencies.implementation(project(":$module"))
        }
    }

    androidModules.forEach { module ->
        println("Adding android core module dependency :$module -> ${project.path}")
        dependencies.implementation(project(":$module"))
    }

    dependencies.implementation(Libraries.Google.Material.material)

    dependencies.implementation(Libraries.AndroidX.Core.coreKtx)
    dependencies.implementation(Libraries.AndroidX.Work.runtimeKtx)
    dependencies.implementation(Libraries.AndroidX.Activity.activityKtx)
    dependencies.implementation(Libraries.AndroidX.Fragment.fragmentKtx)
    dependencies.implementation(Libraries.AndroidX.StartUp.startUpRuntime)
    dependencies.implementation(Libraries.AndroidX.ConstraintLayout.constraintLayout)

    dependencies.implementation(Libraries.Coil.coil)
}

private fun Project.applyBaseModuleDependencies() {
    println("Applying base module dependencies for module -> $path")
    when (name) {
        core -> {
            dependencies.implementation(project(":$androidCore"))
            dependencies.implementation(project(":$navigation"))
            dependencies.implementation(project(":$domain"))
            dependencies.implementation(project(":$data"))

            dependencies.implementation(Libraries.Google.Material.material)

            dependencies.implementation(Libraries.AndroidX.Core.coreKtx)
            dependencies.implementation(Libraries.AndroidX.Work.runtimeKtx)
            dependencies.implementation(Libraries.AndroidX.Activity.activityKtx)
            dependencies.implementation(Libraries.AndroidX.Fragment.fragmentKtx)
            dependencies.implementation(Libraries.AndroidX.StartUp.startUpRuntime)
            dependencies.implementation(Libraries.AndroidX.SwipeRefresh.swipeRefreshLayout)
            dependencies.implementation(Libraries.AndroidX.ConstraintLayout.constraintLayout)

            dependencies.implementation(Libraries.AndroidX.Collection.collectionKtx)
            dependencies.implementation(Libraries.AndroidX.Recycler.recyclerView)
            dependencies.implementation(Libraries.AndroidX.Paging.runtimeKtx)

            dependencies.implementation(Libraries.Coil.coil)
            dependencies.implementation(Libraries.Coil.gif)
            dependencies.implementation(Libraries.Coil.svg)
            dependencies.implementation(Libraries.Coil.video)
            dependencies.implementation(Libraries.Glide.glide)
            dependencies.kapt(Libraries.Glide.compiler)
        }
        data -> {
            dependencies.implementation(project(":$domain"))

            dependencies.implementation(Libraries.AndroidX.Paging.common)
            dependencies.implementation(Libraries.AndroidX.Paging.runtime)
            dependencies.implementation(Libraries.AndroidX.Paging.runtimeKtx)
            dependencies.implementation(Libraries.AndroidX.Room.runtime)
            dependencies.implementation(Libraries.AndroidX.Room.ktx)
            dependencies.kapt(Libraries.AndroidX.Room.compiler)

            dependencies.implementation(Libraries.Square.Retrofit.retrofit)
            dependencies.implementation(Libraries.Square.Retrofit.gsonConverter)
            dependencies.implementation(Libraries.Square.Retrofit.xmlConverter)
            dependencies.implementation(Libraries.Square.OkHttp.logging)

            dependencies.implementation(Libraries.AniTrend.Retrofit.graphQL)
            dependencies.implementation(Libraries.threeTenBp)
        }
        domain -> {
            dependencies.implementation(Libraries.AniTrend.Arch.domain)
        }
        navigation -> {
            dependencies.implementation(Libraries.AndroidX.Core.coreKtx)
            dependencies.implementation(Libraries.AndroidX.Activity.activityKtx)
            dependencies.implementation(Libraries.AndroidX.Collection.collectionKtx)
            dependencies.implementation(Libraries.AndroidX.Fragment.fragment)
        }
    }
}

private fun Project.applyAndroidCoreModuleDependencies() {
    println("Applying core feature dependencies for feature module -> $path")

    dependencies.implementation(Libraries.AniTrend.Arch.ui)
    dependencies.implementation(Libraries.AniTrend.Arch.ext)
    dependencies.implementation(Libraries.AniTrend.Arch.core)
    dependencies.implementation(Libraries.AniTrend.Arch.data)
    dependencies.implementation(Libraries.AniTrend.Arch.theme)
    dependencies.implementation(Libraries.AniTrend.Arch.domain)

    dependencies.implementation(Libraries.AndroidX.Core.coreKtx)
    dependencies.implementation(Libraries.AndroidX.Work.runtimeKtx)
    dependencies.implementation(Libraries.AndroidX.Paging.runtimeKtx)
    dependencies.implementation(Libraries.AndroidX.Activity.activityKtx)
    dependencies.implementation(Libraries.AndroidX.Fragment.fragmentKtx)
    dependencies.implementation(Libraries.AndroidX.StartUp.startUpRuntime)
    dependencies.implementation(Libraries.AndroidX.Collection.collectionKtx)
    dependencies.implementation(Libraries.AndroidX.ConstraintLayout.constraintLayout)

    dependencies.implementation(Libraries.Google.Material.material)

    dependencies.implementation(Libraries.Coil.coil)
    dependencies.implementation(Libraries.threeTenBp)

    dependencies.implementation(project(":$data"))
    dependencies.implementation(project(":$domain"))
    dependencies.implementation(project(":$navigation"))
    if (!isAndroidCoreModule()) {
        dependencies.implementation(project(":$core"))
        dependencies.implementation(project(":$androidCore"))
    }
}

private fun Project.applyCommonModuleDependencies() {
    println("Applying common feature dependencies for feature module -> $path")

    dependencies.implementation(Libraries.AniTrend.Arch.ui)
    dependencies.implementation(Libraries.AniTrend.Arch.ext)
    dependencies.implementation(Libraries.AniTrend.Arch.core)
    dependencies.implementation(Libraries.AniTrend.Arch.data)
    dependencies.implementation(Libraries.AniTrend.Arch.theme)
    dependencies.implementation(Libraries.AniTrend.Arch.domain)
    dependencies.implementation(Libraries.AniTrend.Arch.recycler)

    dependencies.implementation(Libraries.AndroidX.Core.coreKtx)
    dependencies.implementation(Libraries.AndroidX.Work.runtimeKtx)
    dependencies.implementation(Libraries.AndroidX.Paging.runtimeKtx)
    dependencies.implementation(Libraries.AndroidX.Activity.activityKtx)
    dependencies.implementation(Libraries.AndroidX.Fragment.fragmentKtx)
    dependencies.implementation(Libraries.AndroidX.StartUp.startUpRuntime)
    dependencies.implementation(Libraries.AndroidX.Collection.collectionKtx)
    dependencies.implementation(Libraries.AndroidX.ConstraintLayout.constraintLayout)

    dependencies.implementation(Libraries.Google.Material.material)

    dependencies.implementation(Libraries.Coil.coil)
    dependencies.implementation(Libraries.Glide.glide)

    dependencies.implementation(Libraries.threeTenBp)

    dependencies.implementation(project(":$androidCore"))
    dependencies.implementation(project(":$core"))
    dependencies.implementation(project(":$data"))
    dependencies.implementation(project(":$domain"))
    dependencies.implementation(project(":$navigation"))
}

private fun Project.applyComposeDependencies() {
    println("Applying compose dependencies for feature module -> $path")

    dependencies.implementation(Libraries.AndroidX.Compose.Foundation.foundation)
    dependencies.implementation(Libraries.AndroidX.Compose.Material.material)
    dependencies.implementation(Libraries.AndroidX.Compose.Material.Icons.core)
    dependencies.implementation(Libraries.AndroidX.Compose.Material.Icons.extended)
    dependencies.implementation(Libraries.AndroidX.Compose.Runtime.liveData)
    dependencies.implementation(Libraries.AndroidX.Compose.Ui.tooling)
    dependencies.implementation(Libraries.AndroidX.Compose.Ui.ui)
    dependencies.androidTest(Libraries.AndroidX.Compose.Test.test)
}

internal fun Project.configureDependencies() {
    val dependencyStrategy = DependencyStrategy(project.name)
    dependencies.implementation(
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
    /*if (hasComposeSupport()) applyComposeDependencies()*/
}