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

import co.anitrend.data.api.contract.EndpointType
import co.anitrend.data.arch.extension.api
import co.anitrend.data.arch.extension.db
import co.anitrend.data.arch.extension.graphQLController
import co.anitrend.data.cache.repository.contract.ICacheStorePolicy
import co.anitrend.data.tag.mapper.MediaTagResponseMapper
import co.anitrend.data.tag.repository.MediaTagRepository
import co.anitrend.data.tag.source.MediaTagSourceImpl
import co.anitrend.data.tag.source.contract.MediaTagSource
import co.anitrend.data.tag.usecase.MediaTagUseCaseImpl
import co.anitrend.data.tag.MediaTagInteractor
import co.anitrend.data.tag.cache.TagCache
import co.anitrend.data.tag.converter.TagEntityConverter
import org.koin.dsl.module

private val sourceModule = module {
    factory<MediaTagSource> {
        MediaTagSourceImpl(
            localSource = db().mediaTagDao(),
            remoteSource = api(EndpointType.GRAPH_QL),
            controller = graphQLController(
                mapper = get<MediaTagResponseMapper>()
            ),
            cachePolicy = get<TagCache>(),
            clearDataHelper = get(),
            converter = get(),
            dispatcher = get()
        )
    }
}

private val cacheModule = module {
    factory {
        TagCache(
            localSource = db().cacheDao()
        )
    }
}

private val converterModule = module {
    factory {
        TagEntityConverter()
    }
}

private val mapperModule = module {
    factory {
        MediaTagResponseMapper(
            localSource = db().mediaTagDao()
        )
    }
}

private val useCaseModule = module {
    factory<MediaTagInteractor> {
        MediaTagUseCaseImpl(
            repository = get()
        )
    }
}

private val repositoryModule = module {
    factory {
        MediaTagRepository(
            source = get()
        )
    }
}

internal val mediaTagModules = listOf(
    sourceModule,
    cacheModule,
    converterModule,
    mapperModule,
    useCaseModule,
    repositoryModule
)