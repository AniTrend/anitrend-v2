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

package co.anitrend.data.feed.news.koin

import co.anitrend.data.android.extensions.cacheLocalSource
import co.anitrend.data.android.extensions.defaultController
import co.anitrend.data.feed.extensions.newsLocalSource
import co.anitrend.data.feed.extensions.remoteSource
import co.anitrend.data.feed.news.NewsPagedInteractor
import co.anitrend.data.feed.news.NewsPagedRepository
import co.anitrend.data.feed.news.NewsSyncInteractor
import co.anitrend.data.feed.news.NewsSyncRepository
import co.anitrend.data.feed.news.cache.NewsCache
import co.anitrend.data.feed.news.converter.NewsEntityConverter
import co.anitrend.data.feed.news.converter.NewsModelConverter
import co.anitrend.data.feed.news.mapper.NewsMapper
import co.anitrend.data.feed.news.repository.NewsRepository
import co.anitrend.data.feed.news.source.NewsSourceImpl
import co.anitrend.data.feed.news.source.contract.NewsSource
import co.anitrend.data.feed.news.usecase.NewsInteractor
import org.koin.dsl.module

private val sourceModule = module {
    factory<NewsSource> {
        NewsSourceImpl(
            remoteSource = remoteSource(),
            localSource = newsLocalSource(),
            clearDataHelper = get(),
            controller = defaultController(
                mapper = get<NewsMapper>()
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
            localSource = cacheLocalSource()
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
        NewsMapper(
            localSource = newsLocalSource(),
            converter = get()
        )
    }
}

private val useCaseModule = module {
    factory<NewsPagedInteractor> {
        NewsInteractor.Paged(
            repository = get()
        )
    }
    factory<NewsSyncInteractor> {
        NewsInteractor.Sync(
            repository = get()
        )
    }
}

private val repositoryModule = module {
    factory<NewsPagedRepository> {
        NewsRepository.Paged(
            source = get()
        )
    }
    factory<NewsSyncRepository> {
        NewsRepository.Sync(
            source = get()
        )
    }
}

val newsModules = listOf(
    sourceModule,
    cacheModule,
    converterModule,
    mapperModule,
    useCaseModule,
    repositoryModule
)