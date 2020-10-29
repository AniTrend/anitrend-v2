/*
 * Copyright (C) 2020  AniTrend
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

import androidx.recyclerview.widget.RecyclerView
import co.anitrend.common.media.ui.adapter.MediaCarouselAdapter
import co.anitrend.common.media.ui.adapter.MediaPagedAdapter
import co.anitrend.core.koin.helper.DynamicFeatureModuleHelper
import co.anitrend.media.component.carousel.content.CarouselContent
import co.anitrend.media.component.carousel.viewmodel.CarouselViewModel
import co.anitrend.media.component.discover.ui.DiscoverContent
import co.anitrend.media.component.discover.viewmodel.DiscoverViewModel
import co.anitrend.media.provider.FeatureProvider
import co.anitrend.navigation.MediaRouter
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

private val fragmentModule = module {
    fragment {
        DiscoverContent(
            stateConfig = get(),
            supportViewAdapter = MediaPagedAdapter(
                settings = get(),
                resources = androidContext().resources,
                stateConfiguration = get()
            )
        )
    }
    fragment {
        CarouselContent(
            dateHelper = get(),
            stateConfig = get(),
            supportViewAdapter = MediaCarouselAdapter(
                viewPool = RecyclerView.RecycledViewPool(),
                resources = androidContext().resources,
                stateConfiguration = get()
            )
        )
    }
}

private val viewModelModule = module {
    viewModel {
        DiscoverViewModel(
            useCase = get()
        )
    }
    viewModel {
        CarouselViewModel(
            useCase = get()
        )
    }
}

private val featureModule = module {
    factory<MediaRouter.Provider> {
        FeatureProvider()
    }
}

internal val moduleHelper = DynamicFeatureModuleHelper(
    listOf(fragmentModule, viewModelModule, featureModule)
)