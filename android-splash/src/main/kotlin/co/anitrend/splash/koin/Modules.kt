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
import co.anitrend.navigation.SplashRouter
import co.anitrend.splash.component.presenter.SplashPresenter
import co.anitrend.splash.provider.FeatureProvider
import co.anitrend.splash.component.content.SplashContent
import co.anitrend.splash.component.screen.SplashScreen
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.fragment.dsl.fragment
import org.koin.dsl.module

private val presenterModule = module {
    scope<SplashContent> {
        scoped {
            SplashPresenter(
                context = androidContext(),
                settings = get()
            )
        }
    }
}

private val fragmentModule = module {
    scope<SplashScreen> {
        fragment {
            SplashContent()
        }
    }
}

private val featureModule = module {
    factory<SplashRouter.Provider> {
        FeatureProvider()
    }
}

internal val moduleHelper = DynamicFeatureModuleHelper(
    listOf(presenterModule, fragmentModule, featureModule)
)