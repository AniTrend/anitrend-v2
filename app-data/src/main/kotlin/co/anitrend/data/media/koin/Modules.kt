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
import co.anitrend.data.arch.extension.graphQLController
import co.anitrend.data.media.*
import co.anitrend.data.media.MediaDetailRepository
import co.anitrend.data.media.MediaNetworkRepository
import co.anitrend.data.media.MediaPagedRepository
import co.anitrend.data.media.cache.MediaCache
import co.anitrend.data.media.converter.MediaConverter
import co.anitrend.data.media.converter.MediaEntityViewConverter
import co.anitrend.data.media.converter.MediaModelConverter
import co.anitrend.data.media.mapper.MediaMapper
import co.anitrend.data.media.model.container.MediaModelContainer
import co.anitrend.data.media.repository.MediaRepository
import co.anitrend.data.media.source.MediaSourceImpl
import co.anitrend.data.media.source.contract.MediaSource
import co.anitrend.data.media.source.factory.MediaSourceFactory
import co.anitrend.data.media.usecase.MediaInteractor
import org.koin.dsl.module

private val sourceModule = module {
    factory<MediaSource.Detail> {
        MediaSourceImpl.Detail(
            remoteSource = api(EndpointType.GRAPH_QL),
            localSource = db().mediaDao(),
            controller = graphQLController(
                mapper = get<MediaMapper.Detail>()
            ),
            converter = get(),
            clearDataHelper = get(),
            cachePolicy = get<MediaCache>(),
            dispatcher = get()
        )
    }
    factory<MediaSource.Paged> {
        MediaSourceImpl.Paged(
            remoteSource = api(EndpointType.GRAPH_QL),
            localSource = db().mediaDao(),
            carouselSource = get(),
            controller = graphQLController(
                mapper = get<MediaMapper.Paged>()
            ),
            clearDataHelper = get(),
            sortOrderSettings = get(),
            converter = get(),
            dispatcher = get(),
            authentication = get()
        )
    }
    factory {
        MediaSourceFactory.Network(
            remoteSource = api(EndpointType.GRAPH_QL),
            controller = graphQLController(
                mapper = get<MediaMapper.Network>()
            ),
            sortOrderSettings = get(),
            dispatcher = get()
        )
    }
}

private val cacheModule = module {
    factory {
        MediaCache(
            localSource = db().cacheDao()
        )
    }
}

private val converterModule = module {
    factory {
        MediaModelConverter()
    }
    factory {
        MediaEntityViewConverter()
    }
    factory {
        MediaConverter()
    }
}

private val mapperModule = module {
    factory {
        MediaMapper.Detail(
            genreMapper = get(),
            tagMapper = get(),
            linkLocalSource = db().linkDao(),
            rankLocalSource = db().rankDao(),
            airingMapper = get(),
            localSource = db().mediaDao(),
            converter = get(),
            dispatcher = get()
        )
    }
    factory {
        MediaMapper.Paged(
            genreMapper = get(),
            tagMapper = get(),
            airingMapper = get(),
            localSource = db().mediaDao(),
            converter = get(),
            dispatcher = get()
        )
    }
    factory {
        MediaMapper.Network(
            converter = get()
        )
    }
}

private val useCaseModule = module {
    factory<GetDetailMediaInteractor> {
        MediaInteractor.Detail(
            repository = get()
        )
    }
    factory<GetPagedMediaInteractor> {
        MediaInteractor.Paged(
            repository = get()
        )
    }
    factory<GetNetworkMediaInteractor> {
        MediaInteractor.Network(
            repository = get()
        )
    }
}

private val repositoryModule = module {
    factory<MediaDetailRepository> {
        MediaRepository.Detail(
            source = get()
        )
    }
    factory<MediaPagedRepository> {
        MediaRepository.Paged(
            source = get()
        )
    }
    factory<MediaNetworkRepository> {
        MediaRepository.Network(
            source = get()
        )
    }
}

internal val mediaModules = listOf(
    sourceModule,
    cacheModule,
    converterModule,
    mapperModule,
    useCaseModule,
    repositoryModule
)