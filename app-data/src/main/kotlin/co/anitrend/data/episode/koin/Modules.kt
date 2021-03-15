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

package co.anitrend.data.episode.koin

import co.anitrend.data.api.contract.EndpointType
import co.anitrend.data.arch.extension.api
import co.anitrend.data.arch.extension.db
import co.anitrend.data.arch.extension.defaultController
import co.anitrend.data.episode.EpisodeDetailInteractor
import co.anitrend.data.episode.EpisodeDetailRepository
import co.anitrend.data.episode.EpisodePagedInteractor
import co.anitrend.data.episode.EpisodePagedRepository
import co.anitrend.data.episode.cache.EpisodeCache
import co.anitrend.data.episode.converter.EpisodeEntityConverter
import co.anitrend.data.episode.converter.EpisodeModelConverter
import co.anitrend.data.episode.mapper.EpisodeResponseMapper
import co.anitrend.data.episode.repository.EpisodeRepository
import co.anitrend.data.episode.source.EpisodeSourceImpl
import co.anitrend.data.episode.source.contract.EpisodeSource
import co.anitrend.data.episode.usecase.EpisodeUseCaseImpl
import org.koin.dsl.module

private val sourceModule = module {
    factory<EpisodeSource.Detail> {
        EpisodeSourceImpl.Detail(
            localSource = db().episodeDao(),
            converter = get(),
            dispatcher = get(),
        )
    }
    factory<EpisodeSource.Paged> {
        EpisodeSourceImpl.Paged(
            remoteSource = api(EndpointType.MEDIA_RSS),
            localSource = db().episodeDao(),
            clearDataHelper = get(),
            controller = defaultController(
                mapper = get<EpisodeResponseMapper>()
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
            localSource = db().cacheDao()
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
        EpisodeResponseMapper(
            localSource = db().episodeDao(),
            converter = get()
        )
    }
}

private val useCaseModule = module {
    factory<EpisodeDetailInteractor> {
        EpisodeUseCaseImpl.Detail(
            repository = get()
        )
    }
    factory<EpisodePagedInteractor> {
        EpisodeUseCaseImpl.Paged(
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
}

internal val episodeModules = listOf(
    sourceModule,
    cacheModule,
    converterModule,
    mapperModule,
    useCaseModule,
    repositoryModule
)