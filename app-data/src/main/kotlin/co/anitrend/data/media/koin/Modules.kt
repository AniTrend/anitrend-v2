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

package co.anitrend.data.media.koin

import co.anitrend.data.api.contract.EndpointType
import co.anitrend.data.arch.extension.api
import co.anitrend.data.arch.extension.db
import co.anitrend.data.arch.extension.offline
import co.anitrend.data.arch.extension.online
import co.anitrend.data.media.mapper.carousel.MediaCarouselAnimeMapper
import co.anitrend.data.media.mapper.carousel.MediaCarouselMangaMapper
import co.anitrend.data.media.mapper.paged.MediaPagedCombinedMapper
import co.anitrend.data.media.mapper.paged.MediaPagedNetworkMapper
import co.anitrend.data.media.repository.MediaCarouselRepositoryImpl
import co.anitrend.data.media.repository.MediaRepositoryImpl
import co.anitrend.data.media.source.carousel.MediaCarouselSourceImpl
import co.anitrend.data.media.source.carousel.contract.MediaCarouselSource
import co.anitrend.data.media.source.paged.combined.MediaPagedSourceImpl
import co.anitrend.data.media.source.paged.combined.contract.MediaPagedSource
import co.anitrend.data.media.source.paged.network.factory.MediaPagedNetworkSourceFactory
import co.anitrend.data.media.usecase.MediaCarouselUseCaseContract
import co.anitrend.data.media.usecase.MediaCarouselUseCaseImpl
import co.anitrend.data.media.usecase.MediaUseCaseContract
import co.anitrend.data.media.usecase.MediaUseCaseImpl
import org.koin.dsl.module

private val sourceModule = module {
    factory {
        MediaPagedNetworkSourceFactory(
            remoteSource = api(EndpointType.GRAPH_QL),
            mapper = get(),
            strategy = online(),
            sortOrderSettings = get(),
            dispatchers = get()
        )
    }
    factory<MediaPagedSource> {
        MediaPagedSourceImpl(
            remoteSource = api(EndpointType.GRAPH_QL),
            localSource = db().mediaDao(),
            clearDataHelper = get(),
            strategy = offline(),
            mapper = get(),
            sortOrderSettings = get(),
            dispatchers = get()
        )
    }
    factory<MediaCarouselSource> {
        MediaCarouselSourceImpl(
            remoteSource = api(EndpointType.GRAPH_QL),
            localSource = db().carouselDao(),
            animeMapper = get(),
            mangaMapper = get(),
            strategy = online(),
            dispatchers = get()
        )
    }
}

private val mapperModule = module {
    factory {
        MediaPagedCombinedMapper(
            localSource = db().mediaDao(),
            scheduleMapper = get()
        )
    }
    factory {
        MediaPagedNetworkMapper()
    }
    factory {
        MediaCarouselAnimeMapper(
            combinedMapper = get(),
            airingMapper = get()
        )
    }
    factory {
        MediaCarouselMangaMapper(
            combinedMapper = get()
        )
    }
}

private val useCaseModule = module {
    factory<MediaUseCaseContract> {
        MediaUseCaseImpl(
            repository = get()
        )
    }
    factory<MediaCarouselUseCaseContract> {
        MediaCarouselUseCaseImpl(
            repository = get()
        )
    }
}

private val repositoryModule = module {
    factory {
        MediaRepositoryImpl(
            source = get(),
            sourceFactory = get()
        )
    }
    factory {
        MediaCarouselRepositoryImpl(
            source = get()
        )
    }
}

internal val mediaModules = listOf(
    sourceModule,
    mapperModule,
    useCaseModule,
    repositoryModule
)