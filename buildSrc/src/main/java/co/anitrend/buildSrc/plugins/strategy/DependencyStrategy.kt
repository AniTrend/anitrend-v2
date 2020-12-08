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
import co.anitrend.buildSrc.plugins.extensions.implementation
import co.anitrend.buildSrc.plugins.extensions.test
import co.anitrend.buildSrc.plugins.extensions.androidTest
import org.gradle.api.artifacts.dsl.DependencyHandler

internal class DependencyStrategy(
    private val module: String
) {
    private fun DependencyHandler.applyDefaultDependencies() {
        implementation(Libraries.JetBrains.Kotlin.stdlib)
        if (module != domain)
            implementation(Libraries.timber)

        test(Libraries.junit)
        test(Libraries.mockk)

        /** Work around for crashing tests when startup. initializer is not found in *.test packages */
        androidTest(Libraries.AndroidX.StartUp.startUpRuntime)
    }

    private fun DependencyHandler.applyAndroidTestDependencies() {
        androidTest(Libraries.AndroidX.Test.coreKtx)
        androidTest(Libraries.AndroidX.Test.rules)
        androidTest(Libraries.AndroidX.Test.runner)
        androidTest(Libraries.AndroidX.Test.Espresso.core)
        androidTest(Libraries.AndroidX.Test.Extension.junitKtx)
        androidTest(Libraries.mockk)
    }

    private fun DependencyHandler.applyLifeCycleDependencies() {
        implementation(Libraries.AndroidX.Lifecycle.liveDataCoreKtx)
        implementation(Libraries.AndroidX.Lifecycle.runTimeKtx)
        implementation(Libraries.AndroidX.Lifecycle.liveDataKtx)
        implementation(Libraries.AndroidX.Lifecycle.extensions)
    }

    private fun DependencyHandler.applyCoroutinesDependencies() {
        implementation(Libraries.JetBrains.KotlinX.Coroutines.android)
        implementation(Libraries.JetBrains.KotlinX.Coroutines.core)
        implementation(Libraries.CashApp.Copper.copper)

        test(Libraries.JetBrains.KotlinX.Coroutines.test)
        androidTest(Libraries.CashApp.Turbine.turbine)
    }

    private fun DependencyHandler.applyKoinDependencies() {
        implementation(Libraries.Koin.core)
        implementation(Libraries.Koin.core)
        implementation(Libraries.Koin.extension)
        androidTest(Libraries.Koin.test)
        if (module != data || module != core || module != androidCore) {
            implementation(Libraries.Koin.AndroidX.scope)
            implementation(Libraries.Koin.AndroidX.fragment)
            implementation(Libraries.Koin.AndroidX.viewModel)
            implementation(Libraries.Koin.AndroidX.workManager)
        }
    }

    private fun DependencyHandler.applyOtherDependencies() {
        when (module) {
            app -> {
                implementation(Libraries.AniTrend.Arch.recycler)
                implementation(Libraries.AniTrend.Arch.domain)
                implementation(Libraries.AniTrend.Arch.theme)
                implementation(Libraries.AniTrend.Arch.core)
                implementation(Libraries.AniTrend.Arch.data)
                implementation(Libraries.AniTrend.Arch.ext)
                implementation(Libraries.AniTrend.Arch.ui)
            }
            core -> {
                implementation(Libraries.AniTrend.Arch.recycler)
                implementation(Libraries.AniTrend.Arch.domain)
                implementation(Libraries.AniTrend.Arch.theme)
                implementation(Libraries.AniTrend.Arch.core)
                implementation(Libraries.AniTrend.Arch.data)
                implementation(Libraries.AniTrend.Arch.ext)
                implementation(Libraries.AniTrend.Arch.ui)
            }
            data -> {
                implementation(Libraries.AniTrend.Arch.domain)
                implementation(Libraries.AniTrend.Arch.data)
                implementation(Libraries.AniTrend.Arch.ext)
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