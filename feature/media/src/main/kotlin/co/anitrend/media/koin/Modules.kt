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
package co.anitrend.media.koin

import co.anitrend.core.koin.helper.DynamicFeatureModuleHelper
import co.anitrend.media.component.viewmodel.MediaCommunityViewModel
import co.anitrend.media.component.viewmodel.MediaCharactersViewModel
import co.anitrend.media.component.viewmodel.MediaStatsViewModel
import co.anitrend.media.component.viewmodel.MediaRecommendationsViewModel
import co.anitrend.media.component.viewmodel.MediaRelationsViewModel
import co.anitrend.media.component.viewmodel.MediaStaffViewModel
import co.anitrend.media.component.viewmodel.MediaStudiosViewModel
import co.anitrend.media.component.viewmodel.MediaViewModel
import co.anitrend.media.component.viewmodel.MediaScheduleViewModel
import co.anitrend.media.provider.FeatureProvider
import co.anitrend.media.provider.PeopleFeatureProvider
import co.anitrend.media.provider.RecommendationsFeatureProvider
import co.anitrend.media.provider.RelationsFeatureProvider
import co.anitrend.media.provider.StudiosFeatureProvider
import co.anitrend.navigation.MediaPeopleRouter
import co.anitrend.navigation.MediaRecommendationsRouter
import co.anitrend.navigation.MediaRelationsRouter
import co.anitrend.navigation.MediaRouter
import co.anitrend.navigation.MediaStudiosRouter
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

private val viewModelModule =
    module {
        viewModel {
            MediaViewModel(
                interactor = get(),
                settings = get(),
            )
        }
        viewModel {
            MediaScheduleViewModel(
                interactor = get(),
            )
        }
        viewModel {
            MediaRelationsViewModel(
                interactor = get(),
            )
        }
        viewModel {
            MediaRecommendationsViewModel(
                interactor = get(),
            )
        }
        viewModel {
            MediaCommunityViewModel(
                interactor = get(),
            )
        }
        viewModel {
            MediaStudiosViewModel(
                interactor = get(),
            )
        }
        viewModel {
            MediaStatsViewModel(
                interactor = get(),
            )
        }
        viewModel {
            MediaCharactersViewModel(
                interactor = get(),
            )
        }
        viewModel {
            MediaStaffViewModel(
                interactor = get(),
            )
        }
    }

private val featureModule =
    module {
        factory<MediaRouter.Provider> {
            FeatureProvider()
        }
        factory<MediaPeopleRouter.Provider> {
            PeopleFeatureProvider()
        }
        factory<MediaRelationsRouter.Provider> {
            RelationsFeatureProvider()
        }
        factory<MediaRecommendationsRouter.Provider> {
            RecommendationsFeatureProvider()
        }
        factory<MediaStudiosRouter.Provider> {
            StudiosFeatureProvider()
        }
    }

internal val moduleHelper =
    DynamicFeatureModuleHelper(
        listOf(viewModelModule, featureModule),
    )
