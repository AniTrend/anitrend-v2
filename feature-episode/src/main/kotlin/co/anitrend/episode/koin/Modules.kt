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

package co.anitrend.episode.koin

import co.anitrend.common.episode.ui.adapter.EpisodePagedAdapter
import co.anitrend.core.android.koin.MarkdownFlavour
import co.anitrend.core.koin.helper.DynamicFeatureModuleHelper
import co.anitrend.episode.component.content.EpisodeContent
import co.anitrend.episode.component.content.viewmodel.EpisodeContentViewModel
import co.anitrend.episode.component.content.viewmodel.state.EpisodeContentState
import co.anitrend.episode.component.sheet.EpisodeSheet
import co.anitrend.episode.component.sheet.viewmodel.EpisodeSheetViewModel
import co.anitrend.episode.component.sheet.viewmodel.state.EpisodeSheetState
import co.anitrend.episode.provider.FeatureProvider
import co.anitrend.navigation.EpisodeRouter
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

private val fragmentModule = module {
    fragment {
        EpisodeContent(
            settings = get(),
            stateConfig = get(),
            supportViewAdapter = EpisodePagedAdapter(
                resources = androidContext().resources,
                stateConfiguration = get()
            )
        )
    }
    fragment {
        EpisodeSheet(
            markwon = get(named(MarkdownFlavour.STANDARD))
        )
    }
}

private val viewModelModule = module {
    viewModel {
        EpisodeContentViewModel(
            state = EpisodeContentState(
                interactor = get()
            )
        )
    }
    viewModel {
        EpisodeSheetViewModel(
            state = EpisodeSheetState(
                interactor = get()
            )
        )
    }
}

private val featureModule = module {
    factory<EpisodeRouter.Provider> {
        FeatureProvider()
    }
}

internal val moduleHelper = DynamicFeatureModuleHelper(
    listOf(fragmentModule, viewModelModule, featureModule)
)