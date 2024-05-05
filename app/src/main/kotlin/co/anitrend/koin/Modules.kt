/*
 * Copyright (C) 2019 AniTrend
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

import co.anitrend.analytics.AnalyticsTree
import co.anitrend.arch.analytics.contract.ISupportAnalytics
import co.anitrend.component.presenter.MainPresenter
import co.anitrend.component.screen.MainScreen
import co.anitrend.component.viewmodel.MainScreenViewModel
import co.anitrend.core.koin.helper.DynamicFeatureModuleHelper
import co.anitrend.navigation.MainRouter
import co.anitrend.provider.FeatureProvider
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

private val analyticsModule =
    module {
        single<ISupportAnalytics> {
            AnalyticsTree(
                context = androidContext(),
                settings = get(),
            )
        }
    }

private val presenterModule =
    module {
        scope<MainScreen> {
            scoped {
                MainPresenter(
                    context = androidContext(),
                    settings = get(),
                )
            }
        }
    }

private val viewModelModule =
    module {
        viewModel {
            MainScreenViewModel(
                savedStateHandle = get(),
            )
        }
    }

private val featureModule =
    module {
        factory<MainRouter.Provider> {
            FeatureProvider()
        }
    }

internal val appModules =
    DynamicFeatureModuleHelper(
        listOf(analyticsModule, presenterModule, viewModelModule, featureModule),
    )
