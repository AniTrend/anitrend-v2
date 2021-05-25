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

package co.anitrend.feed.koin

import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import co.anitrend.core.koin.helper.DynamicFeatureModuleHelper
import co.anitrend.feed.component.content.FeedContent
import co.anitrend.feed.component.screen.FeedScreen
import co.anitrend.feed.component.viewmodel.FeedViewModel
import co.anitrend.feed.component.viewmodel.state.FeedState
import co.anitrend.feed.provider.FeatureProvider
import co.anitrend.navigation.FeedRouter

private val fragmentModule = module {
    scope<FeedScreen> {
        fragment {
            FeedContent()
        }
    }
}

private val viewModelModule = module {
    viewModel {
        FeedViewModel(
            state = FeedState(
                interactor = get()
            )
        )
    }
}

private val featureModule = module {
    factory<FeedRouter.Provider> {
        FeatureProvider()
    }
}

internal val moduleHelper = DynamicFeatureModuleHelper(
    listOf(fragmentModule, viewModelModule, featureModule)
)