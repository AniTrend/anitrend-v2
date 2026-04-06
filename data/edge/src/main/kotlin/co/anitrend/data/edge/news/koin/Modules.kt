/*
 * Copyright (C) 2025 AniTrend
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
package co.anitrend.data.edge.news.koin

import co.anitrend.data.android.extensions.graphQLController
import co.anitrend.data.android.extensions.offline
import co.anitrend.data.edge.core.extensions.aniTrendApi
import co.anitrend.data.edge.core.extensions.edgeStore
import co.anitrend.data.edge.news.EdgeNewsController
import co.anitrend.data.edge.news.NewsPagedInteractor
import co.anitrend.data.edge.news.NewsPagingRepository
import co.anitrend.data.edge.news.NewsSyncInteractor
import co.anitrend.data.edge.news.NewsSyncRepository
import co.anitrend.data.edge.news.converter.EdgeNewsEntityConverter
import co.anitrend.data.edge.news.converter.EdgeNewsModelConverter
import co.anitrend.data.edge.news.datasource.remote.EdgeNewsRemoteSource
import co.anitrend.data.edge.news.mapper.EdgeNewsMapper
import co.anitrend.data.edge.news.repository.EdgeNewsRepository
import co.anitrend.data.edge.news.source.EdgeNewsSourceImpl
import co.anitrend.data.edge.news.source.contract.EdgeNewsSource
import co.anitrend.data.edge.news.usecase.EdgeNewsInteractor
import org.koin.dsl.module

internal val edgeNewsModules =
    module {
        factory { EdgeNewsModelConverter() }
        factory { EdgeNewsEntityConverter() }
        factory {
            EdgeNewsMapper(
                localSource = edgeStore().edgeNewsDao(),
                converter = get(),
            )
        }
        factory<EdgeNewsController> {
            graphQLController(
                mapper = get<EdgeNewsMapper>(),
                strategy = offline(),
            )
        }
        factory<EdgeNewsSource.Paging> {
            EdgeNewsSourceImpl(
                remoteSource = aniTrendApi<EdgeNewsRemoteSource>(),
                localSource = edgeStore().edgeNewsDao(),
                controller = get(),
                converter = get(),
                clearDataHelper = get(),
                dispatcher = get(),
            )
        }
        factory<NewsPagingRepository> {
            EdgeNewsRepository.Paging(
                source = get(),
            )
        }
        factory<NewsSyncRepository> {
            EdgeNewsRepository.Sync(
                source = get(),
            )
        }
        factory<NewsPagedInteractor> {
            EdgeNewsInteractor.Paging(
                repository = get(),
            )
        }
        factory<NewsSyncInteractor> {
            EdgeNewsInteractor.Sync(
                repository = get(),
            )
        }
    }
