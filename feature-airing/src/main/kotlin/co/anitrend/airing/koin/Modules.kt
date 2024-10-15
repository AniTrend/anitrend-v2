/*
 * Copyright (C) 2020 AniTrend
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
package co.anitrend.airing.koin

import androidx.recyclerview.widget.RecyclerView
import co.anitrend.airing.component.content.AiringContent
import co.anitrend.airing.component.viewmodel.AiringViewModel
import co.anitrend.airing.component.viewmodel.state.AiringState
import co.anitrend.airing.provider.FeatureProvider
import co.anitrend.common.media.ui.adapter.MediaPagedAdapter
import co.anitrend.core.android.settings.Settings
import co.anitrend.core.koin.helper.DynamicFeatureModuleHelper
import co.anitrend.navigation.AiringRouter
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

private val fragmentModule =
    module {
        fragment {
            val settings = get<Settings>()
            AiringContent(
                settings = settings,
                dateHelper = get(),
                stateConfig = get(),
                supportViewAdapter =
                    MediaPagedAdapter(
                        settings = settings,
                        viewPool = RecyclerView.RecycledViewPool(),
                        resources = androidContext().resources,
                        stateConfiguration = get(),
                    ),
            )
        }
    }

private val viewModelModule =
    module {
        viewModel { scope ->
            AiringViewModel(
                state =
                    AiringState(
                        interactor = get(),
                    ),
                stateHandle = scope.get(),
            )
        }
    }

private val featureModule =
    module {
        factory<AiringRouter.Provider> {
            FeatureProvider()
        }
    }

internal val moduleHelper =
    DynamicFeatureModuleHelper(
        listOf(fragmentModule, viewModelModule, featureModule),
    )
