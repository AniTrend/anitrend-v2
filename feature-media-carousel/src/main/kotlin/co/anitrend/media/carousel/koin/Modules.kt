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

package co.anitrend.media.carousel.koin

import androidx.recyclerview.widget.RecyclerView
import co.anitrend.common.media.ui.adapter.MediaCarouselAdapter
import co.anitrend.common.media.ui.databinding.MediaCarouselItemBinding
import co.anitrend.core.koin.helper.DynamicFeatureModuleHelper
import co.anitrend.media.carousel.R
import co.anitrend.media.carousel.component.content.CarouselContent
import co.anitrend.media.carousel.component.content.controller.CarouselContentController
import co.anitrend.media.carousel.component.viewmodel.CarouselViewModel
import co.anitrend.media.carousel.component.viewmodel.state.CarouselState
import co.anitrend.media.carousel.provider.FeatureProvider
import co.anitrend.navigation.MediaCarouselRouter
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.getScopeId
import org.koin.dsl.module

private val coreModule = module {
    factory {
        RecyclerView.RecycledViewPool().apply {
            setMaxRecycledViews(co.anitrend.common.media.ui.R.layout.media_carousel_item, 12)
            setMaxRecycledViews(co.anitrend.common.media.ui.R.layout.media_comfortable_item, 26)
        }
    }
}

private val controllerModule = module {
    factory {
        CarouselContentController(
            dateHelper = get()
        )
    }
}

private val fragmentModule = module {
    fragment {
        CarouselContent(
            controller = get(),
            stateConfig = get(),
            supportViewAdapter = MediaCarouselAdapter(
                settings = get(),
                viewPool = get(),
                resources = androidContext().resources,
                stateConfiguration = get()
            )
        )
    }
}

private val viewModelModule = module {
    viewModel {
        CarouselViewModel(
            state = CarouselState(
                interactor = get()
            )
        )
    }
}

private val featureModule = module {
	factory<MediaCarouselRouter.Provider> {
        FeatureProvider()
    }
}

internal val moduleHelper = DynamicFeatureModuleHelper(
    listOf(coreModule, controllerModule, fragmentModule, viewModelModule, featureModule)
)
