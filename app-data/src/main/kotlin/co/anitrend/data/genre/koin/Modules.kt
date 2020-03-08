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

import co.anitrend.data.database.AniTrendStore
import co.anitrend.data.genre.datasource.MediaGenreSource
import co.anitrend.data.genre.datasource.MediaGenreSourceImpl
import co.anitrend.data.genre.datasource.remote.MediaGenreRemoteSource
import co.anitrend.data.genre.mapper.MediaGenreResponseMapper
import co.anitrend.data.genre.repository.MediaGenreRepository
import co.anitrend.data.genre.usecase.MediaGenreUseCaseImpl
import co.anitrend.domain.genre.interactors.MediaGenreUseCase
import org.koin.dsl.module

private val sourceModule = module {
    factory {
        get<AniTrendStore>().mediaGenreDao()
    }
    factory {
        MediaGenreRemoteSource.create()
    }
    factory<MediaGenreSource> {
        MediaGenreSourceImpl(
            localSource = get(),
            remoteSource = get(),
            mapper = get(),
            connectivity = get(),
            dispatchers = get()
        )
    }
}

private val mapperModule = module {
    factory {
        MediaGenreResponseMapper(
            localSource = get()
        )
    }
}

private val useCaseModule = module {
    factory {
        MediaGenreUseCaseImpl(
            repository = get()
        )
    }
}

private val repositoryModule = module {
    factory {
        MediaGenreRepository(
            dataSource = get()
        )
    }
}

internal val mediaGenreModules = listOf(
    sourceModule,
    mapperModule,
    useCaseModule,
    repositoryModule
)