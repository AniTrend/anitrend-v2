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

import co.anitrend.buildSrc.Libraries
import co.anitrend.buildSrc.extensions.*
import co.anitrend.buildSrc.module.Modules
import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.DependencyHandler

internal class DependencyStrategy(private val project: Project) {

    private fun DependencyHandler.applyDefaultDependencies() {
        implementation(project.libs.jetbrains.kotlin.stdlib.jdk8)
        if (!project.isDomainModule())
            implementation(project.libs.timber)

        test(project.libs.junit)
        test(project.libs.mockk)

        /** Work around for crashing tests when startup. initializer is not found in *.test packages */
        androidTest(project.libs.androidx.startup.runtime)
    }

    private fun DependencyHandler.applyAndroidTestDependencies() {
        androidTest(project.libs.androidx.test.core.ktx)
        androidTest(project.libs.androidx.test.rules)
        androidTest(project.libs.androidx.test.runner)
        androidTest(project.libs.androidx.test.espresso.core)
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
    }

    private fun DependencyHandler.applyKoinDependencies() {
        implementation(Libraries.Koin.core)
        test(Libraries.Koin.Test.test)
        test(Libraries.Koin.Test.testJUnit4)
        if (project.hasKoinAndroidSupport()) {
            implementation(Libraries.Koin.android)
            implementation(Libraries.Koin.AndroidX.workManager)
        }
    }

    private fun DependencyHandler.applyOtherDependencies() {
        when (project.name) {
            Modules.App.Main.id -> {
                implementation(project.libs.anitrend.arch.recycler)
                implementation(project.libs.anitrend.arch.domain)
                implementation(project.libs.anitrend.arch.theme)
                implementation(project.libs.anitrend.arch.core)
                implementation(project.libs.anitrend.arch.data)
                implementation(project.libs.anitrend.arch.ext)
                implementation(project.libs.anitrend.arch.ui)
            }
            Modules.App.Core.id -> {
                implementation(project.libs.anitrend.arch.recycler)
                implementation(project.libs.anitrend.arch.domain)
                implementation(project.libs.anitrend.arch.theme)
                implementation(project.libs.anitrend.arch.core)
                implementation(project.libs.anitrend.arch.data)
                implementation(project.libs.anitrend.arch.ext)
                implementation(project.libs.anitrend.arch.ui)
            }
            Modules.App.Data.id -> {
                implementation(project.libs.anitrend.arch.domain)
                implementation(project.libs.anitrend.arch.data)
                implementation(project.libs.anitrend.arch.ext)
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