/*
 * Copyright (C) 2021 AniTrend
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
package co.anitrend.review.discover.koin

import co.anitrend.common.navigation.FeatureNavEntryProvider
import co.anitrend.core.koin.helper.DynamicFeatureModuleHelper
import co.anitrend.navigation.ReviewDiscoverRouter
import co.anitrend.review.discover.component.content.viewmodel.ReviewDiscoverViewModel
import co.anitrend.review.discover.provider.FeatureProvider
import co.anitrend.review.discover.provider.ReviewDiscoverNavEntryProvider
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

private val viewModelModule =
    module {
        viewModel {
            ReviewDiscoverViewModel(
                interactor = get(),
                savedStateHandle = get(),
                settings = get(),
            )
        }
    }

private val featureModule =
    module {
        factory<ReviewDiscoverRouter.Provider> {
            FeatureProvider()
        }
    }

private val nav3Module =
    module {
        factory {
            ReviewDiscoverNavEntryProvider()
        } bind FeatureNavEntryProvider::class
    }

internal val moduleHelper =
    DynamicFeatureModuleHelper(
        listOf(viewModelModule, featureModule, nav3Module),
    )
