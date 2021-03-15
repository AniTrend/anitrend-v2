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

package co.anitrend.data.carousel.koin

import co.anitrend.arch.extension.dispatchers.contract.ISupportDispatcher
import co.anitrend.data.api.contract.EndpointType
import co.anitrend.data.arch.extension.api
import co.anitrend.data.arch.extension.db
import co.anitrend.data.arch.extension.graphQLController
import co.anitrend.data.carousel.GetCarouselInteractor
import co.anitrend.data.carousel.MediaCarouselListRepository
import co.anitrend.data.carousel.cache.CarouselCache
import co.anitrend.data.carousel.mapper.CarouselMapper
import co.anitrend.data.carousel.repository.MediaCarouselRepository
import co.anitrend.data.carousel.source.CarouselSourceImpl
import co.anitrend.data.carousel.source.contract.CarouselSource
import co.anitrend.data.carousel.usecase.MediaCarouselUseCaseImpl
import org.koin.dsl.module

private val sourceModule = module {
    factory<CarouselSource> {
        CarouselSourceImpl(
            remoteSource = api(EndpointType.GRAPH_QL),
            localSource = db().carouselDao(),
            controller = graphQLController(
                mapper = get<CarouselMapper>()
            ),
            cachePolicy = get<CarouselCache>(),
            clearDataHelper = get(),
            converter = get(),
            dispatcher = get()
        )
    }
}

private val cacheModule = module {
    factory {
        CarouselCache(
            localSource = db().cacheDao()
        )
    }
}

private val mapperModule = module {
    factory {
        CarouselMapper(
            mapper = get()
        )
    }
}

private val useCaseModule = module {
    factory<GetCarouselInteractor> {
        MediaCarouselUseCaseImpl(
            repository = get()
        )
    }
}

private val repositoryModule = module {
    factory<MediaCarouselListRepository> {
        MediaCarouselRepository(
            source = get()
        )
    }
}

internal val carouselModules = listOf(
    sourceModule,
    cacheModule,
    mapperModule,
    useCaseModule,
    repositoryModule
)