/*
 * Copyright (C) 2019  AniTrend
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

package co.anitrend.data.genre.koin

import co.anitrend.data.api.contract.EndpointType
import co.anitrend.data.arch.extension.api
import co.anitrend.data.arch.extension.db
import co.anitrend.data.arch.extension.graphQLController
import co.anitrend.data.cache.repository.contract.ICacheStorePolicy
import co.anitrend.data.genre.MediaGenreInteractor
import co.anitrend.data.genre.cache.GenreCache
import co.anitrend.data.genre.converters.GenreEntityConverter
import co.anitrend.data.genre.mapper.MediaGenreResponseMapper
import co.anitrend.data.genre.repository.MediaGenreRepository
import co.anitrend.data.genre.source.MediaGenreSourceImpl
import co.anitrend.data.genre.source.contract.MediaGenreSource
import co.anitrend.data.genre.usecase.MediaGenreUseCaseImpl
import org.koin.dsl.module

private val sourceModule = module {
    factory<MediaGenreSource> {
        MediaGenreSourceImpl(
            localSource = db().mediaGenreDao(),
            remoteSource = api(EndpointType.GRAPH_QL),
            controller = graphQLController(
                mapper = get<MediaGenreResponseMapper>()
            ),
            cachePolicy = get<GenreCache>(),
            clearDataHelper = get(),
            converter = get(),
            dispatcher = get()
        )
    }
}

private val cacheModule = module {
    factory {
        GenreCache(
            localSource = db().cacheDao()
        )
    }
}

private val converterModule = module {
    factory {
        GenreEntityConverter()
    }
}

private val mapperModule = module {
    factory {
        MediaGenreResponseMapper(
            localSource = db().mediaGenreDao()
        )
    }
}

private val useCaseModule = module {
    factory<MediaGenreInteractor> {
        MediaGenreUseCaseImpl(
            repository = get()
        )
    }
}

private val repositoryModule = module {
    factory {
        MediaGenreRepository(
            source = get()
        )
    }
}

internal val mediaGenreModules = listOf(
    converterModule,
    cacheModule,
    sourceModule,
    mapperModule,
    useCaseModule,
    repositoryModule
)