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

package co.anitrend.data.medialist.koin

import co.anitrend.arch.extension.dispatchers.contract.ISupportDispatcher
import co.anitrend.data.api.contract.EndpointType
import co.anitrend.data.arch.extension.api
import co.anitrend.data.arch.extension.db
import co.anitrend.data.arch.extension.graphQLController
import co.anitrend.data.medialist.MediaListInteractor
import co.anitrend.data.medialist.converter.MediaListEntityConverter
import co.anitrend.data.medialist.converter.MediaListModelConverter
import co.anitrend.data.medialist.mapper.MediaListCollectionMapper
import co.anitrend.data.medialist.mapper.MediaListPagedMapper
import co.anitrend.data.medialist.repository.MediaListRepositoryImpl
import co.anitrend.data.medialist.source.paged.MediaListPagedSourceImpl
import co.anitrend.data.medialist.source.paged.contract.MediaListPagedSource
import co.anitrend.data.medialist.usecase.MediaListUseCaseImpl
import org.koin.dsl.module

private val sourceModule = module {
    factory<MediaListPagedSource> {
        MediaListPagedSourceImpl(
            remoteSource = api(EndpointType.GRAPH_QL),
            localSource = db().mediaListDao(),
            clearDataHelper = get(),
            controller = graphQLController(
                mapper = get<MediaListPagedMapper>()
            ),
            sortOrderSettings = get(),
            converter = get(),
            dispatcher = get()
        )
    }
}

private val mapperModule = module {
    factory {
        MediaListPagedMapper(
            localSource = db().mediaListDao(),
            converter = get(),
            mediaLocalSource = db().mediaDao(),
            mediaConverter = get(),
            context = get<ISupportDispatcher>().io
        )
    }
    factory {
        MediaListCollectionMapper(
            localSource = db().mediaListDao(),
            converter = get(),
            userLocalSource = db().userDao(),
            userConverter = get(),
            context = get<ISupportDispatcher>().io
        )
    }
}

private val converterModule = module {
    factory {
        MediaListModelConverter()
    }
    factory {
        MediaListEntityConverter()
    }
}

private val useCaseModule = module {
    factory<MediaListInteractor> {
        MediaListUseCaseImpl(
            repository = get()
        )
    }
}

private val repositoryModule = module {
    factory {
        MediaListRepositoryImpl(
            source = get()
        )
    }
}

internal val mediaListModules = listOf(
    sourceModule,
    mapperModule,
    converterModule,
    useCaseModule,
    repositoryModule
)