/*
 * Copyright (C) 2019  AniTrend
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

package co.anitrend.app.koin

import co.anitrend.app.navigation.NavigationController
import co.anitrend.app.ui.main.MainScreen
import co.anitrend.app.ui.main.presenter.MainPresenter
import co.anitrend.app.ui.splash.SplashScreen
import co.anitrend.app.ui.splash.presenter.SplashPresenter
import co.anitrend.core.koin.coreModules
import co.anitrend.core.navigation.INavigationController
import co.anitrend.data.koin.dataModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

private val coreModule = module {
    factory<INavigationController> {
        NavigationController()
    }
}

private val presenterModule = module {
    scope(named<SplashScreen>()) {
        scoped {
            SplashPresenter(
                context = androidContext(),
                settings = get()
            )
        }
    }
    scope(named<MainScreen>()) {
        scoped {
            MainPresenter(
                context = androidContext(),
                settings = get()
            )
        }
    }
}

val appModules = listOf(
    coreModule, presenterModule
) + coreModules + dataModules