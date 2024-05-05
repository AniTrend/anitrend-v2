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
package co.anitrend.review.koin

import co.anitrend.core.koin.helper.DynamicFeatureModuleHelper
import co.anitrend.navigation.ReviewRouter
import co.anitrend.review.component.content.ReviewContent
import co.anitrend.review.component.screen.ReviewScreen
import co.anitrend.review.component.viewmodel.ReviewViewModel
import co.anitrend.review.component.viewmodel.state.ReviewState
import co.anitrend.review.provider.FeatureProvider
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

private val fragmentModule =
    module {
        scope<ReviewScreen> {
            fragment {
                ReviewContent()
            }
        }
    }

private val viewModelModule = module {
    viewModel {
        ReviewViewModel(
            state = ReviewState()
        )
    }
}

private val featureModule =
    module {
        factory<ReviewRouter.Provider> {
            FeatureProvider()
        }
    }

internal val moduleHelper = DynamicFeatureModuleHelper(
    listOf(fragmentModule, viewModelModule, featureModule)
)
