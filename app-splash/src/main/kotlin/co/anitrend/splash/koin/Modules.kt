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

package co.anitrend.splash.koin

import co.anitrend.core.koin.helper.DynamicFeatureModuleHelper
import co.anitrend.splash.presenter.SplashPresenter
import co.anitrend.splash.ui.fragment.SplashContent
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

private val presenterModule = module {
    scope(named<SplashContent>()) {
        scoped {
            SplashPresenter(
                context = androidContext(),
                settings = get()
            )
        }
    }
}

internal val moduleHelper by lazy {
    DynamicFeatureModuleHelper(
        listOf(presenterModule)
    )
}