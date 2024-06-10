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

package co.anitrend.buildSrc.plugins.strategy

import co.anitrend.buildSrc.extensions.androidTest
import co.anitrend.buildSrc.extensions.hasKoinAndroidSupport
import co.anitrend.buildSrc.extensions.implementation
import co.anitrend.buildSrc.extensions.isDomainModule
import co.anitrend.buildSrc.extensions.isNavigationModule
import co.anitrend.buildSrc.extensions.libs
import co.anitrend.buildSrc.extensions.test
import co.anitrend.buildSrc.module.Modules
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.DependencyHandler

internal class DependencyStrategy(private val project: Project) {

    private fun DependencyHandler.applyDefaultDependencies() {
        implementation(project.libs.jetbrains.kotlin.stdlib)
        if (!project.isDomainModule())
            implementation(project.libs.timber)

        test(project.libs.jetbrains.kotlin.test)
        test(project.libs.mockk)

        /** Work around for crashing tests when startup. initializer is not found in *.test packages */
        androidTest(project.libs.androidx.startup.runtime)
    }

    private fun DependencyHandler.applyAndroidTestDependencies() {
        androidTest(project.libs.jetbrains.kotlin.test)
        androidTest(project.libs.androidx.test.core.ktx)
        androidTest(project.libs.androidx.test.rules)
        androidTest(project.libs.androidx.test.runner)
        androidTest(project.libs.androidx.test.espresso.core)
        androidTest(project.libs.androidx.test.espresso.intents)
        androidTest(project.libs.mockk.android)
        androidTest(project.libs.androidx.test.ext.junit.ktx)
    }

    private fun DependencyHandler.applyLifeCycleDependencies() {
        implementation(project.libs.androidx.lifecycle.livedata.core.ktx)
        implementation(project.libs.androidx.lifecycle.runtime.ktx)
        implementation(project.libs.androidx.lifecycle.livedata.ktx)
        implementation(project.libs.androidx.lifecycle.extensions)
    }

    private fun DependencyHandler.applyCoroutinesDependencies() {
        implementation(project.libs.jetbrains.kotlinx.coroutines.android)
        implementation(project.libs.jetbrains.kotlinx.coroutines.core)
        implementation(project.libs.cash.copper)

        test(project.libs.cash.turbine)
        test(project.libs.jetbrains.kotlinx.coroutines.test)

        androidTest(project.libs.cash.turbine)
        androidTest(project.libs.jetbrains.kotlinx.coroutines.test)
    }

    private fun DependencyHandler.applyKoinDependencies() {
        implementation(project.libs.koin.core)
        test(project.libs.koin.test)
        test(project.libs.koin.test.junit4)
        androidTest(project.libs.koin.test)
        androidTest(project.libs.koin.test.junit4)
        if (project.hasKoinAndroidSupport()) {
            implementation(project.libs.koin.android)
            implementation(project.libs.koin.androidx.navigation)
            implementation(project.libs.koin.androidx.workManager)
        }
    }

    private fun DependencyHandler.applyOtherDependencies() {
        when (project.name) {
            Modules.App.Main.id -> {
                implementation(project.libs.anitrend.arch.analytics)
                implementation(project.libs.anitrend.arch.recycler)
                implementation(project.libs.anitrend.arch.domain)
                implementation(project.libs.anitrend.arch.theme)
                implementation(project.libs.anitrend.arch.core)
                implementation(project.libs.anitrend.arch.data)
                implementation(project.libs.anitrend.arch.extension)
                implementation(project.libs.anitrend.arch.ui)
            }
            Modules.App.Core.id -> {
                implementation(project.libs.anitrend.arch.analytics)
                implementation(project.libs.anitrend.arch.recycler.paging.legacy)
                implementation(project.libs.anitrend.arch.recycler)
                implementation(project.libs.anitrend.arch.domain)
                implementation(project.libs.anitrend.arch.theme)
                implementation(project.libs.anitrend.arch.core)
                implementation(project.libs.anitrend.arch.data)
                implementation(project.libs.anitrend.arch.extension)
                implementation(project.libs.anitrend.arch.ui)
            }
            Modules.App.Data.id -> {
                implementation(project.libs.anitrend.arch.analytics)
                implementation(project.libs.anitrend.arch.domain)
                implementation(project.libs.anitrend.arch.data)
                implementation(project.libs.anitrend.arch.request)
                implementation(project.libs.anitrend.arch.paging.legacy)
                implementation(project.libs.anitrend.arch.extension)
            }
        }
    }

    fun applyDependenciesOn(handler: DependencyHandler) {
        handler.applyDefaultDependencies()
        if (!project.isDomainModule() || !project.isNavigationModule()) {
            handler.applyLifeCycleDependencies()
            handler.applyAndroidTestDependencies()
            handler.applyCoroutinesDependencies()
            handler.applyKoinDependencies()
            handler.applyOtherDependencies()
        }
    }
}
