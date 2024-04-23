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

import co.anitrend.data.android.extensions.graphQLController
import co.anitrend.data.android.extensions.offline
import co.anitrend.data.core.extensions.aniListApi
import co.anitrend.data.core.extensions.store
import co.anitrend.data.media.GetDetailMediaInteractor
import co.anitrend.data.media.GetNetworkMediaInteractor
import co.anitrend.data.media.GetPagedMediaInteractor
import co.anitrend.data.media.MediaDetailRepository
import co.anitrend.data.media.MediaNetworkRepository
import co.anitrend.data.media.MediaPagedRepository
import co.anitrend.data.media.cache.MediaCache
import co.anitrend.data.media.converter.MediaConverter
import co.anitrend.data.media.converter.MediaEntityViewConverter
import co.anitrend.data.media.converter.MediaModelConverter
import co.anitrend.data.media.entity.filter.MediaQueryFilter
import co.anitrend.data.media.mapper.MediaMapper
import co.anitrend.data.media.repository.MediaRepository
import co.anitrend.data.media.source.MediaSourceImpl
import co.anitrend.data.media.source.contract.MediaSource
import co.anitrend.data.media.source.factory.MediaSourceFactory
import co.anitrend.data.media.usecase.MediaInteractor
import org.koin.dsl.module

private val sourceModule = module {
    factory<MediaSource.Detail> {
        MediaSourceImpl.Detail(
            remoteSource = aniListApi(),
            localSource = store().mediaDao(),
            controller = graphQLController(
                mapper = get<MediaMapper.Detail>(),
                strategy = offline(),
            ),
            converter = get(),
            clearDataHelper = get(),
            jikanSource = get(),
            moeSource = get(),
            cachePolicy = get<MediaCache>(),
            dispatcher = get()
        )
    }
    factory<MediaSource.Paged> {
        MediaSourceImpl.Paged(
            remoteSource = aniListApi(),
            localSource = store().mediaDao(),
            carouselSource = get(),
            controller = graphQLController(
                mapper = get<MediaMapper.Paged>(),
                strategy = offline(),
            ),
            clearDataHelper = get(),
            converter = get(),
            filter = get(),
            dispatcher = get()
        )
    }
    factory {
        MediaSourceFactory.Network(
            remoteSource = aniListApi(),
            controller = graphQLController(
                mapper = get<MediaMapper.Network>()
            ),
            dispatcher = get()
        )
    }
}

private val filterModule = module {
    factory {
        MediaQueryFilter.Paged(
            authentication = get()
        )
    }
}

private val cacheModule = module {
    factory {
        MediaCache(
            localSource = store().cacheDao()
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
            mediaListMapper = get(),
            genreMapper = get(),
            tagMapper = get(),
            linkMapper = get(),
            rankMapper = get(),
            airingMapper = get(),
            localSource = store().mediaDao(),
            converter = get()
        )
    }
    factory {
        MediaMapper.Paged(
            mediaListMapper = get(),
            genreMapper = get(),
            tagMapper = get(),
            linkMapper = get(),
            rankMapper = get(),
            airingMapper = get(),
            localSource = store().mediaDao(),
            converter = get()
        )
    }
    factory {
        MediaMapper.Network(
            converter = get()
        )
    }
    factory {
        MediaMapper.Embed(
            genreMapper = get(),
            tagMapper = get(),
            linkMapper = get(),
            rankMapper = get(),
            localSource = store().mediaDao(),
            converter = get()
        )
    }
    factory {
        MediaMapper.EmbedWithAiring(
            airingMapper = get(),
            genreMapper = get(),
            tagMapper = get(),
            linkMapper = get(),
            rankMapper = get(),
            localSource = store().mediaDao(),
            converter = get()
        )
    }
    factory {
        MediaMapper.EmbedWithMediaList(
            mediaListMapper = get(),
            airingMapper = get(),
            genreMapper = get(),
            tagMapper = get(),
            linkMapper = get(),
            rankMapper = get(),
            localSource = store().mediaDao(),
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

internal val mediaModules = module {
    includes(
        sourceModule,
        filterModule,
        cacheModule,
        converterModule,
        mapperModule,
        useCaseModule,
        repositoryModule
    )
}
