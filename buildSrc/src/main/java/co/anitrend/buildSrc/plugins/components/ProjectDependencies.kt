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
import co.anitrend.buildSrc.module.Modules
import co.anitrend.buildSrc.extensions.*
import co.anitrend.buildSrc.plugins.strategy.DependencyStrategy
import org.gradle.api.Project

private fun Project.applyFeatureModuleGroupDependencies() {
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
    dependencies.implementation(Libraries.AndroidX.Work.multiProcess)
    dependencies.implementation(Libraries.AndroidX.Paging.runtimeKtx)
    dependencies.implementation(Libraries.AndroidX.Activity.activityKtx)
    dependencies.implementation(Libraries.AndroidX.Fragment.fragmentKtx)
    dependencies.implementation(Libraries.AndroidX.StartUp.startUpRuntime)
    dependencies.implementation(Libraries.AndroidX.Collection.collectionKtx)
    dependencies.implementation(Libraries.AndroidX.SwipeRefresh.swipeRefreshLayout)
    dependencies.implementation(Libraries.AndroidX.ConstraintLayout.constraintLayout)

    dependencies.implementation(Libraries.CashApp.Contour.contour)

    dependencies.implementation(Libraries.Google.Material.material)

    dependencies.implementation(Libraries.AirBnB.Paris.paris)
    dependencies.implementation(Libraries.threeTenBp)

    dependencies.implementation(project(Modules.Android.Core.path()))
    dependencies.implementation(project(Modules.App.Navigation.path()))
    dependencies.implementation(project(Modules.App.Core.path()))
    dependencies.implementation(project(Modules.App.Domain.path()))
    dependencies.implementation(project(Modules.App.Data.path()))
    dependencies.implementation(project(Modules.Data.Settings.path()))
}

private fun Project.applyAppModuleDependencies() {
    Modules.Feature.values().forEach { module ->
        println("Adding runtimeOnly dependency ${module.path()} -> ${project.path}")
        dependencies.runtime(project(module.path()))
    }

    Modules.Task.values().forEach { module ->
        println("Adding runtimeOnly dependency ${module.path()} -> ${project.path}")
        dependencies.runtime(project(module.path()))
    }

    Modules.App.values().forEach { module ->
        if (module != Modules.App.Main) {
            println("Adding base module dependency ${module.path()} -> ${project.path}")
            dependencies.implementation(project(module.path()))
        }
    }

    Modules.Android.values().forEach { module ->
        println("Adding android core module dependency ${module.path()} -> ${project.path}")
        dependencies.implementation(project(module.path()))
    }

    dependencies.implementation(Libraries.Google.Material.material)

    dependencies.implementation(Libraries.AndroidX.Core.coreKtx)
    dependencies.implementation(Libraries.AndroidX.Work.runtimeKtx)
    dependencies.implementation(Libraries.AndroidX.Work.multiProcess)
    dependencies.implementation(Libraries.AndroidX.Activity.activityKtx)
    dependencies.implementation(Libraries.AndroidX.Fragment.fragmentKtx)
    dependencies.implementation(Libraries.AndroidX.StartUp.startUpRuntime)
    dependencies.implementation(Libraries.AndroidX.ConstraintLayout.constraintLayout)

    dependencies.implementation(Libraries.Coil.coil)

    dependencies.implementation(project(Modules.Data.Settings.path()))
}

private fun Project.applyAppModuleGroupDependencies() {
    println("Applying base module dependencies for module -> $path")
    when (name) {
        Modules.App.Core.id -> {
            dependencies.implementation(project(Modules.Android.Core.path()))
            dependencies.implementation(project(Modules.App.Navigation.path()))
            dependencies.implementation(project(Modules.App.Domain.path()))
            dependencies.implementation(project(Modules.App.Data.path()))

            dependencies.implementation(Libraries.Google.Material.material)

            dependencies.implementation(Libraries.AndroidX.Core.coreKtx)
            dependencies.implementation(Libraries.AndroidX.Work.runtimeKtx)
            dependencies.implementation(Libraries.AndroidX.Work.multiProcess)
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

            dependencies.implementation(project(Modules.Data.Core.path()))
            dependencies.implementation(project(Modules.Data.Android.path()))
            dependencies.implementation(project(Modules.Data.Settings.path()))
        }
        Modules.App.Data.id -> {
            dependencies.implementation(project(Modules.App.Domain.path()))

            dependencies.implementation(Libraries.AndroidX.Paging.common)
            dependencies.implementation(Libraries.AndroidX.Paging.runtime)
            dependencies.implementation(Libraries.AndroidX.Paging.runtimeKtx)
            dependencies.implementation(Libraries.AndroidX.Room.runtime)
            dependencies.implementation(Libraries.AndroidX.Room.ktx)
            dependencies.kapt(Libraries.AndroidX.Room.compiler)

            dependencies.implementation(Libraries.Square.OkHttp.logging)
            dependencies.implementation(Libraries.Square.Retrofit.retrofit)
            dependencies.implementation(Libraries.Square.Retrofit.gsonConverter)

            dependencies.implementation(Libraries.AniTrend.Retrofit.graphQL)
            dependencies.implementation(Libraries.retrofitSerializer)
            dependencies.implementation(Libraries.threeTenBp)

            dependencies.debugImplementation(Libraries.Chuncker.debug)
            dependencies.releaseImplementation(Libraries.Chuncker.release)

            dependencies.androidTest(Libraries.AndroidX.Room.test)
            dependencies.androidTest(Libraries.Square.OkHttp.mockServer)

            dependencies.compile(Libraries.Square.KotlinPoet.kotlinPoet)

            Modules.Data.values().forEach { module ->
                dependencies.implementation(project(module.path()))
            }
        }
        Modules.App.Domain.id -> {
            dependencies.implementation(Libraries.AniTrend.Arch.domain)
        }
        Modules.App.Navigation.id -> {
            dependencies.implementation(Libraries.AndroidX.Core.coreKtx)
            dependencies.implementation(Libraries.AndroidX.Activity.activityKtx)
            dependencies.implementation(Libraries.AndroidX.Collection.collectionKtx)
            dependencies.implementation(Libraries.AndroidX.Fragment.fragment)
        }
    }
}

private fun Project.applyDataModuleGroupDependencies() {
    println("Applying base module dependencies for module -> $path")
    dependencies.implementation(Libraries.AniTrend.Arch.domain)
    dependencies.implementation(Libraries.AniTrend.Arch.data)
    dependencies.implementation(Libraries.AniTrend.Arch.ext)

    dependencies.implementation(project(Modules.App.Domain.path()))

    if (name != Modules.Data.Settings.id) {
        dependencies.implementation(Libraries.AndroidX.Paging.common)
        dependencies.implementation(Libraries.AndroidX.Paging.runtime)
        dependencies.implementation(Libraries.AndroidX.Paging.runtimeKtx)

        dependencies.implementation(Libraries.AndroidX.Room.runtime)
        dependencies.implementation(Libraries.AndroidX.Room.ktx)
        dependencies.kapt(Libraries.AndroidX.Room.compiler)

        dependencies.implementation(Libraries.Square.OkHttp.logging)
        dependencies.implementation(Libraries.Square.Retrofit.retrofit)
        dependencies.implementation(Libraries.Square.Retrofit.gsonConverter)

        dependencies.implementation(Libraries.retrofitSerializer)
        dependencies.implementation(Libraries.threeTenBp)

        dependencies.debugImplementation(Libraries.Chuncker.debug)
        dependencies.releaseImplementation(Libraries.Chuncker.release)

        dependencies.androidTest(Libraries.AndroidX.Room.test)
        dependencies.androidTest(Libraries.Square.OkHttp.mockServer)
    }

    when (name) {
        Modules.Data.Android.id -> {
            dependencies.implementation(project(Modules.Data.Core.path()))
            dependencies.implementation(project(Modules.Data.Settings.path()))
        }
        Modules.Data.Settings.id -> { }
        Modules.Data.Core.id -> {
            dependencies.implementation(project(Modules.Data.Settings.path()))
        }
        else -> {
            println("Applying core and android data dependencies for module -> $path")
            dependencies.implementation(project(Modules.Data.Core.path()))
            dependencies.implementation(project(Modules.Data.Android.path()))
            dependencies.implementation(project(Modules.Data.Settings.path()))
        }
    }
}

private fun Project.applyAndroidModuleGroupDependencies() {
    println("Applying core feature dependencies for feature module -> $path")

    dependencies.implementation(Libraries.AniTrend.Arch.ui)
    dependencies.implementation(Libraries.AniTrend.Arch.ext)
    dependencies.implementation(Libraries.AniTrend.Arch.core)
    dependencies.implementation(Libraries.AniTrend.Arch.data)
    dependencies.implementation(Libraries.AniTrend.Arch.theme)
    dependencies.implementation(Libraries.AniTrend.Arch.domain)

    dependencies.implementation(Libraries.AndroidX.Core.coreKtx)
    dependencies.implementation(Libraries.AndroidX.Work.runtimeKtx)
    dependencies.implementation(Libraries.AndroidX.Work.multiProcess)
    dependencies.implementation(Libraries.AndroidX.Paging.runtimeKtx)
    dependencies.implementation(Libraries.AndroidX.Activity.activityKtx)
    dependencies.implementation(Libraries.AndroidX.Fragment.fragmentKtx)
    dependencies.implementation(Libraries.AndroidX.StartUp.startUpRuntime)
    dependencies.implementation(Libraries.AndroidX.Collection.collectionKtx)
    dependencies.implementation(Libraries.AndroidX.ConstraintLayout.constraintLayout)

    dependencies.implementation(Libraries.CashApp.Contour.contour)

    dependencies.implementation(Libraries.Google.Material.material)

    dependencies.implementation(Libraries.Coil.coil)
    dependencies.implementation(Libraries.threeTenBp)

    dependencies.implementation(project(Modules.App.Navigation.path()))
    dependencies.implementation(project(Modules.App.Domain.path()))
    dependencies.implementation(project(Modules.App.Data.path()))

    dependencies.implementation(project(Modules.Data.Settings.path()))

    if (!isAndroidCoreModule()) {
        dependencies.implementation(project(Modules.Android.Core.path()))
        dependencies.implementation(project(Modules.App.Core.path()))
    }
}

private fun Project.applyCommonModuleGroupDependencies() {
    println("Applying common feature dependencies for module -> $path")

    dependencies.implementation(Libraries.AniTrend.Arch.ui)
    dependencies.implementation(Libraries.AniTrend.Arch.ext)
    dependencies.implementation(Libraries.AniTrend.Arch.core)
    dependencies.implementation(Libraries.AniTrend.Arch.data)
    dependencies.implementation(Libraries.AniTrend.Arch.theme)
    dependencies.implementation(Libraries.AniTrend.Arch.domain)
    dependencies.implementation(Libraries.AniTrend.Arch.recycler)

    dependencies.implementation(Libraries.AndroidX.Core.coreKtx)
    dependencies.implementation(Libraries.AndroidX.Work.runtimeKtx)
    dependencies.implementation(Libraries.AndroidX.Work.multiProcess)
    dependencies.implementation(Libraries.AndroidX.Paging.runtimeKtx)
    dependencies.implementation(Libraries.AndroidX.Activity.activityKtx)
    dependencies.implementation(Libraries.AndroidX.Fragment.fragmentKtx)
    dependencies.implementation(Libraries.AndroidX.StartUp.startUpRuntime)
    dependencies.implementation(Libraries.AndroidX.Collection.collectionKtx)
    dependencies.implementation(Libraries.AndroidX.ConstraintLayout.constraintLayout)

    dependencies.implementation(Libraries.CashApp.Contour.contour)

    dependencies.implementation(Libraries.Google.Material.material)

    dependencies.implementation(Libraries.Coil.coil)
    dependencies.implementation(Libraries.Glide.glide)

    dependencies.implementation(Libraries.threeTenBp)
    dependencies.implementation(Libraries.AirBnB.Paris.paris)

    dependencies.implementation(project(Modules.Android.Core.path()))
    dependencies.implementation(project(Modules.App.Navigation.path()))
    dependencies.implementation(project(Modules.App.Domain.path()))
    dependencies.implementation(project(Modules.App.Data.path()))
    dependencies.implementation(project(Modules.App.Core.path()))
    dependencies.implementation(project(Modules.Data.Settings.path()))
}

private fun Project.applyTaskModuleGroupDependencies() {
    println("Applying task feature dependencies for module -> $path")

    dependencies.implementation(Libraries.AniTrend.Arch.ext)
    dependencies.implementation(Libraries.AniTrend.Arch.core)
    dependencies.implementation(Libraries.AniTrend.Arch.data)
    dependencies.implementation(Libraries.AniTrend.Arch.domain)

    dependencies.implementation(Libraries.AndroidX.Core.coreKtx)
    dependencies.implementation(Libraries.AndroidX.Work.runtimeKtx)
    dependencies.implementation(Libraries.AndroidX.Work.multiProcess)
    dependencies.implementation(Libraries.AndroidX.Paging.runtimeKtx)
    dependencies.implementation(Libraries.AndroidX.StartUp.startUpRuntime)
    dependencies.implementation(Libraries.AndroidX.Collection.collectionKtx)

    dependencies.implementation(Libraries.threeTenBp)

    dependencies.implementation(project(Modules.Android.Core.path()))
    dependencies.implementation(project(Modules.App.Navigation.path()))
    dependencies.implementation(project(Modules.App.Domain.path()))
    dependencies.implementation(project(Modules.App.Data.path()))
    dependencies.implementation(project(Modules.App.Core.path()))
    dependencies.implementation(project(Modules.Data.Settings.path()))

    //dependencies.androidTest(Libraries.AndroidX.Work.test)
}

private fun Project.applyComposeDependencies() {
    println("Applying compose dependencies for feature module -> $path")
    dependencies.implementation(Libraries.AndroidX.Compose.Foundation.foundation)
    dependencies.implementation(Libraries.AndroidX.Compose.Foundation.layout)
    dependencies.implementation(Libraries.AndroidX.Compose.Material.material)
    dependencies.implementation(Libraries.AndroidX.Compose.Material.Icons.core)
    dependencies.implementation(Libraries.AndroidX.Compose.Material.Icons.extended)
    dependencies.implementation(Libraries.AndroidX.Compose.Runtime.runtime)
    dependencies.implementation(Libraries.AndroidX.Compose.Runtime.liveData)
    dependencies.implementation(Libraries.AndroidX.Compose.Ui.tooling)
    dependencies.implementation(Libraries.AndroidX.Compose.Ui.ui)
    dependencies.implementation(Libraries.AndroidX.Compose.Ui.viewBinding)
    dependencies.androidTest(Libraries.AndroidX.Compose.Ui.test)

    dependencies.implementation(Libraries.AndroidX.Activity.Compose.activityCompose)
    dependencies.implementation(Libraries.AndroidX.Lifecycle.Compose.viewModelCompose)
    dependencies.implementation(Libraries.AndroidX.ConstraintLayout.Compose.constraintLayoutCompose)
    // Until I migrate to paging v3.0
    //dependencies.implementation(Libraries.AndroidX.Paging.Compose.pagingCompose)

    dependencies.implementation(Libraries.Google.Material.Compose.themeAdapter)
    dependencies.implementation(Libraries.Koin.AndroidX.compose)

    dependencies.implementation(Libraries.Google.Accompanist.pagerIndicators)
    dependencies.implementation(Libraries.Google.Accompanist.appCompatTheme)
    dependencies.implementation(Libraries.Google.Accompanist.uiController)
    dependencies.implementation(Libraries.Google.Accompanist.flowLayout)
    dependencies.implementation(Libraries.Google.Accompanist.insets)
    dependencies.implementation(Libraries.Google.Accompanist.coil)
    dependencies.implementation(Libraries.Google.Accompanist.pager)
}

internal fun Project.configureDependencies() {
    dependencies.implementation(
        fileTree("libs") {
            include("*.jar")
        }
    )
    DependencyStrategy(project).applyDependenciesOn(dependencies)

    if (isAppModule()) applyAppModuleDependencies()
    if (matchesAppModule()) applyAppModuleGroupDependencies()
    if (matchesDataModule()) applyDataModuleGroupDependencies()
    if (matchesAndroidModule()) applyAndroidModuleGroupDependencies()
    if (matchesFeatureModule()) applyFeatureModuleGroupDependencies()
    if (matchesCommonModule()) applyCommonModuleGroupDependencies()
    if (matchesTaskModule()) applyTaskModuleGroupDependencies()
    if (hasComposeSupport()) applyComposeDependencies()
}