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
package co.anitrend.media.discover.filter.koin

import co.anitrend.common.genre.ui.adapter.GenreListAdapter
import co.anitrend.common.tag.ui.adpter.TagListAdapter
import co.anitrend.core.android.recycler.selection.DefaultSelectionMode
import co.anitrend.core.koin.helper.DynamicFeatureModuleHelper
import co.anitrend.media.discover.filter.component.content.GeneralContent
import co.anitrend.media.discover.filter.component.content.GenreContent
import co.anitrend.media.discover.filter.component.content.SortingContent
import co.anitrend.media.discover.filter.component.content.TagContent
import co.anitrend.media.discover.filter.component.viewmodel.genre.GenreViewModel
import co.anitrend.media.discover.filter.component.viewmodel.genre.state.GenreViewModelState
import co.anitrend.media.discover.filter.component.viewmodel.tag.TagViewModel
import co.anitrend.media.discover.filter.component.viewmodel.tag.state.TagViewModelState
import co.anitrend.media.discover.filter.provider.FeatureProvider
import co.anitrend.navigation.MediaDiscoverFilterRouter
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

private val fragmentModule =
    module {
        fragment {
            SortingContent()
        }
        fragment {
            GeneralContent(
                dateHelper = get(),
            )
        }
        fragment {
            GenreContent(
                stateConfig = get(),
                supportViewAdapter =
                    GenreListAdapter(
                        stateConfiguration = get(),
                        resources = androidContext().resources,
                        supportAction = DefaultSelectionMode(),
                    ),
            )
        }
        fragment {
            TagContent(
                stateConfig = get(),
                supportViewAdapter =
                    TagListAdapter(
                        stateConfiguration = get(),
                        resources = androidContext().resources,
                        supportAction = DefaultSelectionMode(),
                    ),
            )
        }
    }

private val viewModelModule =
    module {
        viewModel {
            GenreViewModel(
                state =
                    GenreViewModelState(
                        interactor = get(),
                    ),
            )
        }
        viewModel {
            TagViewModel(
                state =
                    TagViewModelState(
                        interactor = get(),
                    ),
            )
        }
    }

private val featureModule =
    module {
        factory<MediaDiscoverFilterRouter.Provider> {
            FeatureProvider()
        }
    }

internal val moduleHelper =
    DynamicFeatureModuleHelper(
        listOf(fragmentModule, viewModelModule, featureModule),
    )
