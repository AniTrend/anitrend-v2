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

import co.anitrend.buildSrc.extensions.androidTest
import co.anitrend.buildSrc.extensions.androidTestPlatform
import co.anitrend.buildSrc.extensions.compile
import co.anitrend.buildSrc.extensions.debugImplementation
import co.anitrend.buildSrc.extensions.debugImplementationPlatform
import co.anitrend.buildSrc.extensions.googleImplementation
import co.anitrend.buildSrc.extensions.hasComposeSupport
import co.anitrend.buildSrc.extensions.implementation
import co.anitrend.buildSrc.extensions.implementationPlatform
import co.anitrend.buildSrc.extensions.isAndroidCoreModule
import co.anitrend.buildSrc.extensions.isAppModule
import co.anitrend.buildSrc.extensions.isDataModule
import co.anitrend.buildSrc.extensions.isDomainModule
import co.anitrend.buildSrc.extensions.ksp
import co.anitrend.buildSrc.extensions.libs
import co.anitrend.buildSrc.extensions.matchesAndroidModule
import co.anitrend.buildSrc.extensions.matchesAppModule
import co.anitrend.buildSrc.extensions.matchesCommonModule
import co.anitrend.buildSrc.extensions.matchesDataModule
import co.anitrend.buildSrc.extensions.matchesDomainModule
import co.anitrend.buildSrc.extensions.matchesFeatureModule
import co.anitrend.buildSrc.extensions.matchesTaskModule
import co.anitrend.buildSrc.extensions.releaseImplementation
import co.anitrend.buildSrc.extensions.runtime
import co.anitrend.buildSrc.extensions.test
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

    dependencies.implementation(libs.google.android.material)

    dependencies.implementation(libs.threeTenBp)

    dependencies.implementation(project(Modules.Android.Core.path))
    dependencies.implementation(project(Modules.App.Navigation.path))
    dependencies.implementation(project(Modules.App.Core.path))
    dependencies.implementation(project(Modules.Domain.Common.path))
    dependencies.implementation(project(Modules.Data.Common.path))
    dependencies.implementation(project(Modules.Data.Settings.path))
}

private fun Project.applyAppModuleDependencies() {
    Modules.Domain.values().forEach { module ->
        logger.lifecycle("Adding implementation dependency ${module.path} -> ${project.path}")
        dependencies.implementation(project(module.path))
    }

    Modules.Data.values().forEach { module ->
        if (module != Modules.Data.Settings) {
            logger.lifecycle("Adding implementation dependency ${module.path} -> ${project.path}")
            dependencies.implementation(project(module.path))
        }
        else {
            logger.lifecycle("Adding runtimeOnly dependency ${module.path} -> ${project.path}")
            dependencies.runtime(project(module.path))
        }
    }

    Modules.Feature.values().forEach { module ->
        logger.lifecycle("Adding runtimeOnly dependency ${module.path} -> ${project.path}")
        dependencies.runtime(project(module.path))
    }

    Modules.Task.values().forEach { module ->
        logger.lifecycle("Adding runtimeOnly dependency ${module.path} -> ${project.path}")
        dependencies.runtime(project(module.path))
    }

    Modules.App.values().filter { it != Modules.App.Main }.forEach { module ->
        logger.lifecycle("Adding app sub-module dependency ${module.path} -> ${project.path}")
        dependencies.implementation(project(module.path))
    }

    Modules.Android.values().forEach { module ->
        logger.lifecycle("Adding android module dependency ${module.path} -> ${project.path}")
        dependencies.implementation(project(module.path))
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

    dependencies.implementation(project(Modules.Data.Settings.path))

    dependencies.googleImplementation(libs.google.firebase.analytics.ktx)
    dependencies.googleImplementation(libs.google.firebase.crashlytics)

    /** debugImplementation because LeakCanary should only run in debug builds. */
    dependencies.debugImplementation(libs.square.leakcanary.android)
}

private fun Project.applyAppModuleGroupDependencies() {
    logger.lifecycle("Applying base module dependencies for module -> $path")
    when (path) {
        Modules.App.Core.path -> {
            dependencies.implementation(project(Modules.Android.Core.path))
            dependencies.implementation(project(Modules.App.Navigation.path))
            // TODO: Review the need for some of these dependencies
            dependencies.implementation(project(Modules.Domain.Common.path))
            dependencies.implementation(project(Modules.Data.Common.path))

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

            dependencies.implementation(project(Modules.Data.Core.path))
            dependencies.implementation(project(Modules.Data.Android.path))
            dependencies.implementation(project(Modules.Data.Settings.path))
        }
        Modules.App.Navigation.path -> {
            dependencies.implementation(libs.androidx.core.ktx)
            dependencies.implementation(libs.androidx.activity.ktx)
            dependencies.implementation(libs.androidx.collection.ktx)
            dependencies.implementation(libs.androidx.fragment.ktx)
        }
    }
}

private fun Project.applyDomainModuleDependencies() {
    logger.lifecycle("Applying domain module dependencies for module -> $path")
    dependencies.implementation(libs.anitrend.arch.domain)
}

private fun Project.applyRoomCompilerDependency() {
    dependencies.ksp(libs.androidx.room.compiler)
}

private fun Project.applyDomainModuleGroupDependencies() {
    logger.lifecycle("Applying domain module group dependencies for module -> $path")
    dependencies.implementation(libs.anitrend.arch.domain)
}

private fun Project.applyDataModuleDependencies() {
    logger.lifecycle("Applying base module dependencies for module -> $path")
    dependencies.implementation(project(Modules.Domain.Common.path))

    dependencies.implementation(libs.androidx.paging.common)
    dependencies.implementation(libs.androidx.paging.runtime)
    dependencies.implementation(libs.androidx.paging.runtime.ktx)
    dependencies.implementation(libs.androidx.room.runtime)
    dependencies.implementation(libs.androidx.room.ktx)
    applyRoomCompilerDependency()

    dependencies.implementation(libs.square.okhttp.logging)
    dependencies.implementation(libs.square.retrofit)
    dependencies.implementation(libs.square.retrofit.converter.gson)

    dependencies.implementation(libs.anitrend.retrofit.graphql.runtime)
    dependencies.implementation(libs.anitrend.retrofit.graphql.api)
    dependencies.implementation(libs.anitrend.retrofit.graphql.annotations)
    dependencies.implementation(libs.retrofitSerializer)
    dependencies.implementation(libs.threeTenBp)

    dependencies.debugImplementation(libs.chuncker.debug)
    dependencies.releaseImplementation(libs.chuncker.release)

    dependencies.androidTest(libs.androidx.room.testing)
    dependencies.androidTest(libs.square.okhttp.mockwebserver)

    dependencies.compile(libs.square.kotlinpoet)

    Modules.Data.values().filter { it != Modules.Data.Common }.forEach { module ->
        dependencies.implementation(project(module.path))
    }
}

private fun Project.applyDataModuleGroupDependencies() {
    logger.lifecycle("Applying data module group dependencies for module -> $path")
    dependencies.implementation(libs.anitrend.arch.analytics)
    dependencies.implementation(libs.anitrend.arch.domain)
    dependencies.implementation(libs.anitrend.arch.data)
    dependencies.implementation(libs.anitrend.arch.request)
    dependencies.implementation(libs.anitrend.arch.extension)

    dependencies.implementation(project(Modules.Domain.Common.path))

    if (path != Modules.Data.Settings.path) {
        dependencies.implementation(libs.androidx.paging.common)
        dependencies.implementation(libs.androidx.paging.runtime)
        dependencies.implementation(libs.androidx.paging.runtime.ktx)

        dependencies.implementation(libs.androidx.room.runtime)
        dependencies.implementation(libs.androidx.room.ktx)
        applyRoomCompilerDependency()

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

    when (path) {
        Modules.Data.Android.path -> {
            dependencies.implementation(project(Modules.Data.Core.path))
            dependencies.implementation(project(Modules.Data.Settings.path))
        }
        Modules.Data.Settings.path -> { }
        Modules.Data.Core.path -> {
            dependencies.implementation(project(Modules.Data.Settings.path))
        }
        else -> {
            logger.lifecycle("Applying core and android data dependencies for module -> $path")
            dependencies.implementation(project(Modules.Data.Core.path))
            dependencies.implementation(project(Modules.Data.Android.path))
            dependencies.implementation(project(Modules.Data.Settings.path))
        }
    }
}

private fun Project.applyAndroidModuleGroupDependencies() {
    logger.lifecycle("Applying android group dependencies for feature module -> $path")

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

    dependencies.implementation(libs.google.android.material)

    dependencies.implementation(libs.coil)
    dependencies.implementation(libs.threeTenBp)

    dependencies.implementation(project(Modules.App.Navigation.path))
    dependencies.implementation(project(Modules.Domain.Common.path))
    dependencies.implementation(project(Modules.Data.Common.path))

    dependencies.implementation(project(Modules.Data.Settings.path))

    if (!isAndroidCoreModule()) {
        dependencies.implementation(project(Modules.Android.Core.path))
        dependencies.implementation(project(Modules.App.Core.path))
    }
}

private fun Project.applyCommonModuleGroupDependencies() {
    logger.lifecycle("Applying common feature group dependencies for module -> $path")

    dependencies.implementation(libs.anitrend.arch.ui)
    dependencies.implementation(libs.anitrend.arch.extension)
    dependencies.implementation(libs.anitrend.arch.core)
    dependencies.implementation(libs.anitrend.arch.data)
    dependencies.implementation(libs.anitrend.arch.theme)
    dependencies.implementation(libs.anitrend.arch.domain)
    dependencies.implementation(libs.anitrend.arch.recycler)
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

    dependencies.implementation(libs.google.android.material)

    dependencies.implementation(libs.coil)

    dependencies.implementation(libs.threeTenBp)

    dependencies.implementation(project(Modules.Android.Core.path))
    dependencies.implementation(project(Modules.App.Navigation.path))
    dependencies.implementation(project(Modules.Domain.Common.path))
    dependencies.implementation(project(Modules.Data.Common.path))
    dependencies.implementation(project(Modules.App.Core.path))
    dependencies.implementation(project(Modules.Data.Settings.path))
}

private fun Project.applyTaskModuleGroupDependencies() {
    logger.lifecycle("Applying task module group dependencies for module -> $path")

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

    dependencies.implementation(project(Modules.Android.Core.path))
    dependencies.implementation(project(Modules.App.Navigation.path))
    dependencies.implementation(project(Modules.Domain.Common.path))
    dependencies.implementation(project(Modules.Data.Common.path))
    dependencies.implementation(project(Modules.App.Core.path))
    dependencies.implementation(project(Modules.Data.Settings.path))

    //dependencies.androidTest(libs.androidx.work.test)
}

private fun Project.applyComposeDependencies() {
    logger.lifecycle("Applying compose dependencies for module -> $path")
    dependencies.implementationPlatform(libs.androidx.compose.bom)
    dependencies.debugImplementationPlatform(libs.androidx.compose.bom)
    dependencies.androidTestPlatform(libs.androidx.compose.bom)
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
    dependencies.implementation(libs.androidx.navigation.compose)

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
    dependencies.test(libs.jetbrains.kotlin.test.junit5)
    DependencyStrategy(project).applyDependenciesOn(dependencies)

    if (isAppModule()) applyAppModuleDependencies()
    if (isDataModule()) applyDataModuleDependencies()
    if (isDomainModule()) applyDomainModuleDependencies()
    if (matchesAppModule()) applyAppModuleGroupDependencies()
    if (matchesDataModule()) applyDataModuleGroupDependencies()
    if (matchesDomainModule()) applyDomainModuleGroupDependencies()
    if (matchesAndroidModule()) applyAndroidModuleGroupDependencies()
    if (matchesFeatureModule()) applyFeatureModuleGroupDependencies()
    if (matchesCommonModule()) applyCommonModuleGroupDependencies()
    if (matchesTaskModule()) applyTaskModuleGroupDependencies()
    if (hasComposeSupport()) applyComposeDependencies()
}
