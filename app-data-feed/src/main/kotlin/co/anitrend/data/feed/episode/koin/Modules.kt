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

package co.anitrend.data.feed.episode.koin

import co.anitrend.data.android.extensions.cacheLocalSource
import co.anitrend.data.android.extensions.defaultController
import co.anitrend.data.feed.episode.*
import co.anitrend.data.feed.episode.EpisodeDetailRepository
import co.anitrend.data.feed.episode.EpisodePagedRepository
import co.anitrend.data.feed.episode.cache.EpisodeCache
import co.anitrend.data.feed.episode.converter.EpisodeEntityConverter
import co.anitrend.data.feed.episode.converter.EpisodeModelConverter
import co.anitrend.data.feed.episode.mapper.EpisodeMapper
import co.anitrend.data.feed.episode.repository.EpisodeRepository
import co.anitrend.data.feed.episode.source.EpisodeSourceImpl
import co.anitrend.data.feed.episode.source.contract.EpisodeSource
import co.anitrend.data.feed.episode.usecase.EpisodeInteractor
import co.anitrend.data.feed.extensions.episodeLocalSource
import co.anitrend.data.feed.extensions.remoteSource
import org.koin.dsl.module

private val sourceModule = module {
    factory<EpisodeSource.Detail> {
        EpisodeSourceImpl.Detail(
            localSource = episodeLocalSource(),
            converter = get(),
            dispatcher = get(),
        )
    }
    factory<EpisodeSource.Paged> {
        EpisodeSourceImpl.Paged(
            remoteSource = remoteSource(),
            localSource = episodeLocalSource(),
            clearDataHelper = get(),
            controller = defaultController(
                mapper = get<EpisodeMapper>()
            ),
            converter = get(),
            cachePolicy = get<EpisodeCache>(),
            dispatcher = get(),
        )
    }
}

private val cacheModule = module {
    factory {
        EpisodeCache(
            localSource = cacheLocalSource()
        )
    }
}

private val converterModule = module {
    factory {
        EpisodeModelConverter()
    }
    factory {
        EpisodeEntityConverter()
    }
}

private val mapperModule = module {
    factory {
        EpisodeMapper(
            localSource = episodeLocalSource(),
            converter = get()
        )
    }
}

private val useCaseModule = module {
    factory<EpisodeDetailInteractor> {
        EpisodeInteractor.Detail(
            repository = get()
        )
    }
    factory<EpisodePagedInteractor> {
        EpisodeInteractor.Paged(
            repository = get()
        )
    }
    factory<EpisodeSyncInteractor> {
        EpisodeInteractor.Sync(
            repository = get()
        )
    }
}

private val repositoryModule = module {
    factory<EpisodeDetailRepository> {
        EpisodeRepository.Detail(
            source = get()
        )
    }
    factory<EpisodePagedRepository> {
        EpisodeRepository.Paged(
            source = get()
        )
    }
    factory<EpisodeSyncRepository> {
        EpisodeRepository.Sync(
            source = get()
        )
    }
}

internal val episodeModules = listOf(
    sourceModule,
    cacheModule,
    converterModule,
    mapperModule,
    useCaseModule,
    repositoryModule
)