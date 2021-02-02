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

package co.anitrend.data.news.koin

import co.anitrend.data.api.contract.EndpointType
import co.anitrend.data.arch.extension.api
import co.anitrend.data.arch.extension.db
import co.anitrend.data.arch.extension.defaultController
import co.anitrend.data.news.NewsPagedRepository
import co.anitrend.data.news.NewsInteractor
import co.anitrend.data.news.cache.NewsCache
import co.anitrend.data.news.converter.NewsEntityConverter
import co.anitrend.data.news.converter.NewsModelConverter
import co.anitrend.data.news.mapper.NewsResponseMapper
import co.anitrend.data.news.repository.NewsRepositoryImpl
import co.anitrend.data.news.source.NewsSourceImpl
import co.anitrend.data.news.source.contract.NewsSource
import co.anitrend.data.news.usecase.NewsUseCaseImpl
import org.koin.dsl.module

private val sourceModule = module {
    factory<NewsSource> {
        NewsSourceImpl(
            remoteSource = api(EndpointType.NEWS_RSS),
            localSource = db().newsDao(),
            clearDataHelper = get(),
            controller = defaultController(
                mapper = get<NewsResponseMapper>()
            ),
            converter = get(),
            cachePolicy = get<NewsCache>(),
            dispatcher = get()
        )
    }
}

private val cacheModule = module {
    factory {
        NewsCache(
            localSource = db().cacheDao()
        )
    }
}

private val converterModule =  module {
    factory {
        NewsEntityConverter()
    }
    factory {
        NewsModelConverter()
    }
}

private val mapperModule = module {
    factory {
        NewsResponseMapper(
            localSource = db().newsDao(),
            converter = get()
        )
    }
}

private val useCaseModule = module {
    factory<NewsInteractor> {
        NewsUseCaseImpl(
            repository = get()
        )
    }
}

private val repositoryModule = module {
    factory<NewsPagedRepository> {
        NewsRepositoryImpl(
            source = get()
        )
    }
}

internal val newsModules = listOf(
    sourceModule,
    cacheModule,
    converterModule,
    mapperModule,
    useCaseModule,
    repositoryModule
)