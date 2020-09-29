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

import co.anitrend.data.api.contract.EndpointType
import co.anitrend.data.arch.extension.api
import co.anitrend.data.arch.extension.db
import co.anitrend.data.arch.extension.online
import co.anitrend.data.carousel.cache.CarouselCache
import co.anitrend.data.carousel.mapper.CarouselAnimeMapper
import co.anitrend.data.carousel.mapper.CarouselMangaMapper
import co.anitrend.data.carousel.repository.CarouselRepositoryImpl
import co.anitrend.data.carousel.source.CarouselSourceImpl
import co.anitrend.data.carousel.source.contract.CarouselSource
import co.anitrend.data.carousel.usecase.CarouselUseCaseContract
import co.anitrend.data.carousel.usecase.CarouselUseCaseImpl
import org.koin.dsl.module

private val sourceModule = module {
    factory<CarouselSource> {
        CarouselSourceImpl(
            remoteSource = api(EndpointType.GRAPH_QL),
            localSource = db().carouselDao(),
            cachePolicy = CarouselCache(
                db().cacheDao()
            ),
            animeMapper = get(),
            mangaMapper = get(),
            strategy = online(),
            clearDataHelper = get(),
            dispatchers = get()
        )
    }
}

private val mapperModule = module {
    factory {
        CarouselAnimeMapper(
            combinedMapper = get(),
            airingMapper = get()
        )
    }
    factory {
        CarouselMangaMapper(
            combinedMapper = get()
        )
    }
}

private val useCaseModule = module {
    factory<CarouselUseCaseContract> {
        CarouselUseCaseImpl(
            repository = get()
        )
    }
}

private val repositoryModule = module {
    factory {
        CarouselRepositoryImpl(
            source = get()
        )
    }
}

internal val carouselModules = listOf(
    sourceModule,
    mapperModule,
    useCaseModule,
    repositoryModule
)