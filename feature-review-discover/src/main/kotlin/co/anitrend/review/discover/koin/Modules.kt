/*
 * Copyright (C) 2021  AniTrend
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

import co.anitrend.common.review.ui.adapter.ReviewPagedAdapter
import co.anitrend.core.android.settings.common.IConfigurationSettings
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import co.anitrend.core.koin.helper.DynamicFeatureModuleHelper
import co.anitrend.data.settings.customize.common.PreferredViewMode
import co.anitrend.review.discover.component.content.ReviewDiscoverContent
import co.anitrend.review.discover.component.content.viewmodel.ReviewDiscoverViewModel
import co.anitrend.review.discover.component.content.viewmodel.state.ReviewDiscoverState
import co.anitrend.review.discover.provider.FeatureProvider
import co.anitrend.navigation.ReviewDiscoverRouter
import org.koin.android.ext.koin.androidContext

private val fragmentModule = module {
    fragment {
        ReviewDiscoverContent(
            stateConfig = get(),
            supportViewAdapter = ReviewPagedAdapter(
                settings = get(),
                preferredViewMode = PreferredViewMode.DETAILED,
                resources = androidContext().resources,
                stateConfiguration = get(),
            )
        )
    }
}

private val viewModelModule = module {
    viewModel {
        ReviewDiscoverViewModel(
            state = ReviewDiscoverState(
                interactor = get(),
                savedStateHandle = get(),
                settings = get()
            )
        )
    }
}

private val featureModule = module {
    factory<ReviewDiscoverRouter.Provider> {
        FeatureProvider()
    }
}

internal val moduleHelper = DynamicFeatureModuleHelper(
    listOf(fragmentModule, viewModelModule, featureModule)
)