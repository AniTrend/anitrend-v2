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

import co.anitrend.buildSrc.extensions.*
import co.anitrend.buildSrc.module.Modules
import co.anitrend.buildSrc.plugins.strategy.DependencyStrategy
import org.gradle.api.Project

private fun Project.applyFeatureModuleGroupDependencies() {
    logger.lifecycle("Applying shared dependencies for feature module -> $path")

    dependencies.implementation(libs.anitrend.arch.ui)
    dependencies.implementation(libs.anitrend.arch.extension)
    dependencies.implementation(libs.anitrend.arch.core)
    dependencies.implementation(libs.anitrend.arch.data)
    dependencies.implementation(libs.anitrend.arch.theme)
    dependencies.implementation(libs.anitrend.arch.domain)
    dependencies.implementation(libs.anitrend.arch.recycler)
    dependencies.implementation(libs.anitrend.arch.recycler.paging.legacy)
    dependencies.implementation(libs.anitrend.arch.paging.legacy)
    dependencies.implementation(libs.anitrend.arch.analytics)

    dependencies.implementation(libs.androidx.core.ktx)
    dependencies.implementation(libs.androidx.work.runtime.ktx)
    dependencies.implementation(libs.androidx.work.multiprocess)
    dependencies.implementation(libs.androidx.paging.runtime.ktx)
    dependencies.implementation(libs.androidx.activity.ktx)
    dependencies.implementation(libs.androidx.appcompat)
    dependencies.implementation(libs.androidx.fragment.ktx)
    dependencies.implementation(libs.androidx.startup.runtime)
    dependencies.implementation(libs.androidx.collection.ktx)
    dependencies.implementation(libs.androidx.swiperefreshlayout)
    dependencies.implementation(libs.androidx.constraintlayout)

    dependencies.implementation(libs.saket.cascade)
    dependencies.implementation(libs.cash.contour)

    dependencies.implementation(libs.google.android.material)

    dependencies.implementation(libs.airbnb.paris)
    dependencies.implementation(libs.threeTenBp)

    dependencies.implementation(project(Modules.Android.Core.path()))
    dependencies.implementation(project(Modules.App.Navigation.path()))
    dependencies.implementation(project(Modules.App.Core.path()))
    dependencies.implementation(project(Modules.App.Domain.path()))
    dependencies.implementation(project(Modules.App.Data.path()))
    dependencies.implementation(project(Modules.Data.Settings.path()))
}

private fun Project.applyAppModuleDependencies() {
    Modules.Feature.values().forEach { module ->
        logger.lifecycle("Adding runtimeOnly dependency ${module.path()} -> ${project.path}")
        dependencies.runtime(project(module.path()))
    }

    Modules.Task.values().forEach { module ->
        logger.lifecycle("Adding runtimeOnly dependency ${module.path()} -> ${project.path}")
        dependencies.runtime(project(module.path()))
    }

    Modules.App.values().forEach { module ->
        if (module != Modules.App.Main) {
            logger.lifecycle("Adding base module dependency ${module.path()} -> ${project.path}")
            dependencies.implementation(project(module.path()))
        }
    }

    Modules.Android.values().forEach { module ->
        logger.lifecycle("Adding android core module dependency ${module.path()} -> ${project.path}")
        dependencies.implementation(project(module.path()))
    }

    dependencies.implementation(libs.google.android.material)

    dependencies.implementation(libs.androidx.core.ktx)
    dependencies.implementation(libs.androidx.work.runtime.ktx)
    dependencies.implementation(libs.androidx.work.multiprocess)
    dependencies.implementation(libs.androidx.activity.ktx)
    dependencies.implementation(libs.androidx.appcompat)
    dependencies.implementation(libs.androidx.fragment.ktx)
    dependencies.implementation(libs.androidx.startup.runtime)
    dependencies.implementation(libs.androidx.constraintlayout)

    dependencies.implementation(libs.coil)
    dependencies.implementation(libs.saket.cascade)

    dependencies.implementation(project(Modules.Data.Settings.path()))

    dependencies.googleImplementation(libs.google.firebase.analytics.ktx)
    dependencies.googleImplementation(libs.google.firebase.crashlytics)

    /** debugImplementation because LeakCanary should only run in debug builds. */
    dependencies.debugImplementation(libs.square.leakcanary.android)
}

private fun Project.applyAppModuleGroupDependencies() {
    logger.lifecycle("Applying base module dependencies for module -> $path")
    when (name) {
        Modules.App.Core.id -> {
            dependencies.implementation(project(Modules.Android.Core.path()))
            dependencies.implementation(project(Modules.App.Navigation.path()))
            dependencies.implementation(project(Modules.App.Domain.path()))
            dependencies.implementation(project(Modules.App.Data.path()))

            dependencies.implementation(libs.google.android.material)

            dependencies.implementation(libs.androidx.core.ktx)
            dependencies.implementation(libs.androidx.work.runtime.ktx)
            dependencies.implementation(libs.androidx.work.multiprocess)
            dependencies.implementation(libs.androidx.activity.ktx)
            dependencies.implementation(libs.androidx.appcompat)
            dependencies.implementation(libs.androidx.fragment.ktx)
            dependencies.implementation(libs.androidx.startup.runtime)
            dependencies.implementation(libs.androidx.swiperefreshlayout)
            dependencies.implementation(libs.androidx.constraintlayout)

            dependencies.implementation(libs.androidx.collection.ktx)
            dependencies.implementation(libs.androidx.recyclerview)
            dependencies.implementation(libs.androidx.paging.runtime.ktx)

            dependencies.implementation(libs.coil)
            dependencies.implementation(libs.coil.gif)
            dependencies.implementation(libs.coil.svg)
            dependencies.implementation(libs.coil.video)

            dependencies.implementation(project(Modules.Data.Core.path()))
            dependencies.implementation(project(Modules.Data.Android.path()))
            dependencies.implementation(project(Modules.Data.Settings.path()))
        }
        Modules.App.Data.id -> {
            dependencies.implementation(project(Modules.App.Domain.path()))

            dependencies.implementation(libs.androidx.paging.common)
            dependencies.implementation(libs.androidx.paging.runtime)
            dependencies.implementation(libs.androidx.paging.runtime.ktx)
            dependencies.implementation(libs.androidx.room.runtime)
            dependencies.implementation(libs.androidx.room.ktx)
            dependencies.kapt(libs.androidx.room.compiler)

            dependencies.implementation(libs.square.okhttp.logging)
            dependencies.implementation(libs.square.retrofit)
            dependencies.implementation(libs.square.retrofit.converter.gson)

            dependencies.implementation(libs.anitrend.retrofit.graphql)
            dependencies.implementation(libs.retrofitSerializer)
            dependencies.implementation(libs.threeTenBp)

            dependencies.debugImplementation(libs.chuncker.debug)
            dependencies.releaseImplementation(libs.chuncker.release)

            dependencies.androidTest(libs.androidx.room.testing)
            dependencies.androidTest(libs.square.okhttp.mockwebserver)

            dependencies.compile(libs.square.kotlinpoet)

            Modules.Data.values().forEach { module ->
                dependencies.implementation(project(module.path()))
            }
        }
        Modules.App.Domain.id -> {
            dependencies.implementation(libs.anitrend.arch.domain)
        }
        Modules.App.Navigation.id -> {
            dependencies.implementation(libs.androidx.core.ktx)
            dependencies.implementation(libs.androidx.activity.ktx)
            dependencies.implementation(libs.androidx.collection.ktx)
            dependencies.implementation(libs.androidx.fragment.ktx)
        }
    }
}

private fun Project.applyDataModuleGroupDependencies() {
    logger.lifecycle("Applying base module dependencies for module -> $path")
    dependencies.implementation(libs.anitrend.arch.analytics)
    dependencies.implementation(libs.anitrend.arch.domain)
    dependencies.implementation(libs.anitrend.arch.data)
    dependencies.implementation(libs.anitrend.arch.request)
    dependencies.implementation(libs.anitrend.arch.paging.legacy)
    dependencies.implementation(libs.anitrend.arch.extension)

    dependencies.implementation(project(Modules.App.Domain.path()))

    if (name != Modules.Data.Settings.id) {
        dependencies.implementation(libs.androidx.paging.common)
        dependencies.implementation(libs.androidx.paging.runtime)
        dependencies.implementation(libs.androidx.paging.runtime.ktx)

        dependencies.implementation(libs.androidx.room.runtime)
        dependencies.implementation(libs.androidx.room.ktx)
        dependencies.kapt(libs.androidx.room.compiler)

        dependencies.implementation(libs.square.okhttp.logging)
        dependencies.implementation(libs.square.retrofit)
        dependencies.implementation(libs.square.retrofit.converter.gson)

        dependencies.implementation(libs.retrofitSerializer)
        dependencies.implementation(libs.threeTenBp)

        dependencies.debugImplementation(libs.chuncker.debug)
        dependencies.releaseImplementation(libs.chuncker.release)

        dependencies.androidTest(libs.androidx.room.testing)
        dependencies.androidTest(libs.square.okhttp.mockwebserver)
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
            logger.lifecycle("Applying core and android data dependencies for module -> $path")
            dependencies.implementation(project(Modules.Data.Core.path()))
            dependencies.implementation(project(Modules.Data.Android.path()))
            dependencies.implementation(project(Modules.Data.Settings.path()))
        }
    }
}

private fun Project.applyAndroidModuleGroupDependencies() {
    logger.lifecycle("Applying core feature dependencies for feature module -> $path")

    dependencies.implementation(libs.anitrend.arch.ui)
    dependencies.implementation(libs.anitrend.arch.extension)
    dependencies.implementation(libs.anitrend.arch.core)
    dependencies.implementation(libs.anitrend.arch.data)
    dependencies.implementation(libs.anitrend.arch.theme)
    dependencies.implementation(libs.anitrend.arch.domain)
    dependencies.implementation(libs.anitrend.arch.analytics)

    dependencies.implementation(libs.androidx.core.ktx)
    dependencies.implementation(libs.androidx.work.runtime.ktx)
    dependencies.implementation(libs.androidx.work.multiprocess)
    dependencies.implementation(libs.androidx.paging.runtime.ktx)
    dependencies.implementation(libs.androidx.activity.ktx)
    dependencies.implementation(libs.androidx.appcompat)
    dependencies.implementation(libs.androidx.fragment.ktx)
    dependencies.implementation(libs.androidx.startup.runtime)
    dependencies.implementation(libs.androidx.collection.ktx)
    dependencies.implementation(libs.androidx.constraintlayout)

    dependencies.implementation(libs.cash.contour)
    dependencies.implementation(libs.saket.cascade)

    dependencies.implementation(libs.google.android.material)

    dependencies.implementation(libs.coil)
    dependencies.implementation(libs.threeTenBp)

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
    logger.lifecycle("Applying common feature dependencies for module -> $path")

    dependencies.implementation(libs.anitrend.arch.ui)
    dependencies.implementation(libs.anitrend.arch.extension)
    dependencies.implementation(libs.anitrend.arch.core)
    dependencies.implementation(libs.anitrend.arch.data)
    dependencies.implementation(libs.anitrend.arch.theme)
    dependencies.implementation(libs.anitrend.arch.domain)
    dependencies.implementation(libs.anitrend.arch.recycler)
    dependencies.implementation(libs.anitrend.arch.recycler.paging.legacy)
    dependencies.implementation(libs.anitrend.arch.paging.legacy)
    dependencies.implementation(libs.anitrend.arch.analytics)

    dependencies.implementation(libs.androidx.core.ktx)
    dependencies.implementation(libs.androidx.work.runtime.ktx)
    dependencies.implementation(libs.androidx.work.multiprocess)
    dependencies.implementation(libs.androidx.paging.runtime.ktx)
    dependencies.implementation(libs.androidx.activity.ktx)
    dependencies.implementation(libs.androidx.fragment.ktx)
    dependencies.implementation(libs.androidx.startup.runtime)
    dependencies.implementation(libs.androidx.collection.ktx)
    dependencies.implementation(libs.androidx.constraintlayout)

    dependencies.implementation(libs.cash.contour)

    dependencies.implementation(libs.google.android.material)

    dependencies.implementation(libs.coil)

    dependencies.implementation(libs.threeTenBp)
    dependencies.implementation(libs.airbnb.paris)

    dependencies.implementation(project(Modules.Android.Core.path()))
    dependencies.implementation(project(Modules.App.Navigation.path()))
    dependencies.implementation(project(Modules.App.Domain.path()))
    dependencies.implementation(project(Modules.App.Data.path()))
    dependencies.implementation(project(Modules.App.Core.path()))
    dependencies.implementation(project(Modules.Data.Settings.path()))
}

private fun Project.applyTaskModuleGroupDependencies() {
    logger.lifecycle("Applying task feature dependencies for module -> $path")

    dependencies.implementation(libs.anitrend.arch.extension)
    dependencies.implementation(libs.anitrend.arch.core)
    dependencies.implementation(libs.anitrend.arch.data)
    dependencies.implementation(libs.anitrend.arch.domain)

    dependencies.implementation(libs.androidx.core.ktx)
    dependencies.implementation(libs.androidx.work.runtime.ktx)
    dependencies.implementation(libs.androidx.work.multiprocess)
    dependencies.implementation(libs.androidx.paging.runtime.ktx)
    dependencies.implementation(libs.androidx.startup.runtime)
    dependencies.implementation(libs.androidx.collection.ktx)

    dependencies.implementation(libs.threeTenBp)

    dependencies.implementation(project(Modules.Android.Core.path()))
    dependencies.implementation(project(Modules.App.Navigation.path()))
    dependencies.implementation(project(Modules.App.Domain.path()))
    dependencies.implementation(project(Modules.App.Data.path()))
    dependencies.implementation(project(Modules.App.Core.path()))
    dependencies.implementation(project(Modules.Data.Settings.path()))

    //dependencies.androidTest(libs.androidx.work.test)
}

private fun Project.applyComposeDependencies() {
    logger.lifecycle("Applying compose dependencies for feature module -> $path")
    dependencies.implementation(libs.androidx.compose.foundation)
    dependencies.implementation(libs.androidx.compose.foundation.layout)
    dependencies.implementation(libs.androidx.compose.material)
    dependencies.implementation(libs.androidx.compose.material.icons.core)
    dependencies.implementation(libs.androidx.compose.material.icons.extended)
    dependencies.implementation(libs.androidx.compose.runtime)
    dependencies.implementation(libs.androidx.compose.runtime.livedata)
    dependencies.implementation(libs.androidx.compose.ui.viewbinding)
    dependencies.debugImplementation(libs.androidx.compose.ui.tooling)
    dependencies.implementation(libs.androidx.compose.ui.tooling.preview)
    dependencies.androidTest(libs.androidx.compose.ui.test)
    dependencies.implementation(libs.androidx.compose.ui)

    dependencies.implementation(libs.androidx.activity.compose)
    dependencies.implementation(libs.androidx.lifecycle.viewmodel.compose)
    dependencies.implementation(libs.androidx.constraintlayout.compose)
    // Until I migrate to paging v3.0
    //dependencies.implementation(libs.androidx.paging.Compose.pagingCompose)

    dependencies.implementation(libs.google.android.material.compose.theme.adapter)
    dependencies.implementation(libs.koin.androidx.compose)

    dependencies.implementation(libs.google.accompanist.pager.indicators)
    dependencies.implementation(libs.google.accompanist.appcompat.theme)
    dependencies.implementation(libs.google.accompanist.systemuicontroller)
    dependencies.implementation(libs.google.accompanist.flowlayout)
    dependencies.implementation(libs.google.accompanist.insets)
    dependencies.implementation(libs.google.accompanist.pager)
    dependencies.implementation(libs.google.accompanist.drawablepainter)

    dependencies.implementation(libs.coil.compose)
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
