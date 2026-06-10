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

import co.anitrend.airing.component.viewmodel.AiringViewModel
import co.anitrend.airing.provider.AiringNavEntryProvider
import co.anitrend.airing.provider.FeatureProvider
import co.anitrend.common.navigation.FeatureNavEntryProvider
import co.anitrend.core.koin.helper.DynamicFeatureModuleHelper
import co.anitrend.navigation.AiringRouter
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

private val viewModelModule =
    module {
        viewModel { scope ->
            AiringViewModel(
                stateHandle = scope.get(),
                interactor = get(),
            )
        }
    }

private val featureModule =
    module {
        factory<AiringRouter.Provider> {
            FeatureProvider()
        }

        factory<FeatureNavEntryProvider> {
            AiringNavEntryProvider()
        }
    }

internal val moduleHelper =
    DynamicFeatureModuleHelper(
        listOf(viewModelModule, featureModule),
    )
