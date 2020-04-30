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

package co.anitrend.koin

import co.anitrend.ui.activity.MainScreen
import co.anitrend.presenter.MainPresenter
import co.anitrend.core.koin.coreModules
import co.anitrend.data.arch.di.dataModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

private val presenterModule = module {
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
    presenterModule
) + coreModules + dataModules