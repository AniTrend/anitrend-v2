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

package co.anitrend.data.tag.koin

import co.anitrend.data.arch.database.AniTrendStore
import co.anitrend.data.arch.extension.api
import co.anitrend.data.arch.extension.db
import co.anitrend.data.tag.datasource.MediaTagSource
import co.anitrend.data.tag.datasource.MediaTagSourceImpl
import co.anitrend.data.tag.datasource.remote.MediaTagRemoteSource
import co.anitrend.data.tag.mapper.MediaTagResponseMapper
import co.anitrend.data.tag.repository.MediaTagRepository
import co.anitrend.data.tag.usecase.MediaTagUseCaseImpl
import org.koin.dsl.module

private val sourceModule = module {
    factory<MediaTagSource> {
        MediaTagSourceImpl(
            localSource = db().mediaTagDao(),
            remoteSource = api(),
            mapper = get(),
            connectivity = get(),
            dispatchers = get()
        )
    }
}

private val mapperModule = module {
    factory {
        MediaTagResponseMapper(
            localSource = get()
        )
    }
}

private val useCaseModule = module {
    factory {
        MediaTagUseCaseImpl(
            repository = get()
        )
    }
}

private val repositoryModule = module {
    factory {
        MediaTagRepository(
            dataSource = get()
        )
    }
}

internal val mediaTagModules = listOf(
    sourceModule,
    mapperModule,
    useCaseModule,
    repositoryModule
)