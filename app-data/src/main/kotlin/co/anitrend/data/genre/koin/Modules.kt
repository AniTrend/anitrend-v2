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

import co.anitrend.data.android.extensions.graphQLController
import co.anitrend.data.core.extensions.graphApi
import co.anitrend.data.core.extensions.store
import co.anitrend.data.genre.GenreInteractor
import co.anitrend.data.genre.GenreListRepository
import co.anitrend.data.genre.cache.GenreCache
import co.anitrend.data.genre.converters.GenreEntityConverter
import co.anitrend.data.genre.converters.GenreModelConverter
import co.anitrend.data.genre.entity.filter.GenreQueryFilter
import co.anitrend.data.genre.mapper.GenreMapper
import co.anitrend.data.genre.repository.GenreRepository
import co.anitrend.data.genre.source.GenreSourceImpl
import co.anitrend.data.genre.source.contract.GenreSource
import co.anitrend.data.genre.usecase.GenreUseCaseImpl
import org.koin.dsl.module

private val sourceModule = module {
    factory<GenreSource> {
        GenreSourceImpl(
            localSource = store().genreDao(),
            remoteSource = graphApi(),
            controller = graphQLController(
                mapper = get<GenreMapper.Core>()
            ),
            cachePolicy = get<GenreCache>(),
            clearDataHelper = get(),
            filter = get(),
            converter = get(),
            dispatcher = get()
        )
    }
}

private val filterModule = module {
    factory {
        GenreQueryFilter()
    }
}

private val cacheModule = module {
    factory {
        GenreCache(
            localSource = store().cacheDao()
        )
    }
}

private val converterModule = module {
    factory {
        GenreEntityConverter()
    }
    factory {
        GenreModelConverter(
            emojiManager = get()
        )
    }
}

private val mapperModule = module {
    factory {
        GenreMapper.Core(
            localSource = store().genreDao(),
            converter = get()
        )
    }
    factory {
        GenreMapper.Embed(
            localSource = store().genreConnectionDao()
        )
    }
}

private val useCaseModule = module {
    factory<GenreInteractor> {
        GenreUseCaseImpl(
            repository = get()
        )
    }
}

private val repositoryModule = module {
    factory<GenreListRepository> {
        GenreRepository(
            source = get()
        )
    }
}

internal val genreModules = module {
    includes(
        converterModule,
        filterModule,
        cacheModule,
        sourceModule,
        mapperModule,
        useCaseModule,
        repositoryModule
    )
}
