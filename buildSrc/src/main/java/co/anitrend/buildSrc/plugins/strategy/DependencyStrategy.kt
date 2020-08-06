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
import co.anitrend.buildSrc.common.*
import co.anitrend.buildSrc.common.core
import co.anitrend.buildSrc.common.data
import co.anitrend.buildSrc.common.domain
import co.anitrend.buildSrc.common.navigation
import org.gradle.api.artifacts.dsl.DependencyHandler

internal class DependencyStrategy(
    private val module: String
) {
    private fun DependencyHandler.applyDefaultDependencies() {
        add("implementation", Libraries.JetBrains.Kotlin.stdlib)
        if (module != domain)
            add("implementation", Libraries.timber)

        add("testImplementation", Libraries.junit)
        add("testImplementation", Libraries.mockk)
    }

    private fun DependencyHandler.applyAndroidTestDependencies() {
        add("androidTestImplementation", Libraries.AndroidX.Test.coreKtx)
        add("androidTestImplementation", Libraries.AndroidX.Test.rules)
        add("androidTestImplementation", Libraries.AndroidX.Test.runner)
        add("androidTestImplementation", Libraries.AndroidX.Test.Extension.junitKtx)
    }

    private fun DependencyHandler.applyLifeCycleDependencies() {
        add("implementation", Libraries.AndroidX.Lifecycle.liveDataCoreKtx)
        add("implementation", Libraries.AndroidX.Lifecycle.runTimeKtx)
        add("implementation", Libraries.AndroidX.Lifecycle.liveDataKtx)
        add("implementation", Libraries.AndroidX.Lifecycle.extensions)
    }

    private fun DependencyHandler.applyCoroutinesDependencies() {
        add("implementation", Libraries.JetBrains.KotlinX.Coroutines.android)
        add("implementation", Libraries.JetBrains.KotlinX.Coroutines.core)
        add("testImplementation", Libraries.JetBrains.KotlinX.Coroutines.test)
    }

    private fun DependencyHandler.applyKoinDependencies() {
        add("implementation", Libraries.Koin.core)
        add("implementation", Libraries.Koin.extension)
        add("testImplementation", Libraries.Koin.test)
        if (module != data || module != core || module != androidCore) {
            add("implementation", Libraries.Koin.AndroidX.scope)
            add("implementation", Libraries.Koin.AndroidX.fragment)
            add("implementation", Libraries.Koin.AndroidX.viewmodel)
        }
    }

    private fun DependencyHandler.applyOtherDependencies() {
        when (module) {
            app -> {
                add("implementation", Libraries.AniTrend.Arch.recycler)
                add("implementation", Libraries.AniTrend.Arch.domain)
                add("implementation", Libraries.AniTrend.Arch.theme)
                add("implementation", Libraries.AniTrend.Arch.core)
                add("implementation", Libraries.AniTrend.Arch.data)
                add("implementation", Libraries.AniTrend.Arch.ext)
                add("implementation", Libraries.AniTrend.Arch.ui)
            }
            core -> {
                add("implementation", Libraries.AniTrend.Arch.recycler)
                add("implementation", Libraries.AniTrend.Arch.domain)
                add("implementation", Libraries.AniTrend.Arch.theme)
                add("implementation", Libraries.AniTrend.Arch.core)
                add("implementation", Libraries.AniTrend.Arch.data)
                add("implementation", Libraries.AniTrend.Arch.ext)
                add("implementation", Libraries.AniTrend.Arch.ui)
            }
            data -> {
                add("implementation", Libraries.AniTrend.Arch.domain)
                add("implementation", Libraries.AniTrend.Arch.data)
                add("implementation", Libraries.AniTrend.Arch.ext)
            }
        }
    }

    fun applyDependenciesOn(handler: DependencyHandler) {
        handler.applyDefaultDependencies()
        if (module != domain || module != navigation) {
            handler.applyLifeCycleDependencies()
            handler.applyAndroidTestDependencies()
            handler.applyCoroutinesDependencies()
            handler.applyKoinDependencies()
            handler.applyOtherDependencies()
        }
    }
}