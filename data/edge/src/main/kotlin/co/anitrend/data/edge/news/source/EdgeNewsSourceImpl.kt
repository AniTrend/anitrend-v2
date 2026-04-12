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
package co.anitrend.data.edge.news.source

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingState
import co.anitrend.arch.extension.dispatchers.contract.ISupportDispatcher
import co.anitrend.arch.extension.util.DEFAULT_PAGE_SIZE
import co.anitrend.data.android.cache.model.CacheIdentity
import co.anitrend.data.android.cleaner.contract.IClearDataHelper
import co.anitrend.data.edge.news.EdgeNewsController
import co.anitrend.data.edge.news.converter.EdgeNewsEntityConverter
import co.anitrend.data.edge.news.datasource.local.EdgeNewsLocalSource
import co.anitrend.data.edge.news.datasource.remote.EdgeNewsRemoteSource
import co.anitrend.data.edge.news.source.contract.EdgeNewsSource
import co.anitrend.domain.news.entity.News
import co.anitrend.domain.news.model.NewsParam
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@OptIn(ExperimentalPagingApi::class)
internal class EdgeNewsSourceImpl(
    private val remoteSource: EdgeNewsRemoteSource,
    private val localSource: EdgeNewsLocalSource,
    private val controller: EdgeNewsController,
    private val converter: EdgeNewsEntityConverter,
    private val clearDataHelper: IClearDataHelper,
    override val dispatcher: ISupportDispatcher,
) : EdgeNewsSource.Paging() {
    override fun invoke(param: NewsParam): Flow<PagingData<News>> {
        assignQuery(param)

        val source = createPagingSource()

        return Pager(
            config =
                PagingConfig(
                    pageSize = DEFAULT_PAGE_SIZE,
                    initialLoadSize = DEFAULT_PAGE_SIZE,
                    prefetchDistance = DEFAULT_PAGE_SIZE,
                    enablePlaceholders = false,
                ),
            remoteMediator = source,
            pagingSourceFactory = source.pagingSourceFactory(),
        ).flow
    }

    override fun sync(param: NewsParam): Flow<Boolean> =
        flow {
            assignQuery(param)

            val result =
                createPagingSource().load(
                    loadType = LoadType.REFRESH,
                    state =
                        PagingState(
                            pages = emptyList(),
                            anchorPosition = null,
                            config =
                                PagingConfig(
                                    pageSize = DEFAULT_PAGE_SIZE,
                                    initialLoadSize = DEFAULT_PAGE_SIZE,
                                    prefetchDistance = DEFAULT_PAGE_SIZE,
                                    enablePlaceholders = false,
                                ),
                            leadingPlaceholderCount = 0,
                        ),
                )

            emit(result is androidx.paging.RemoteMediator.MediatorResult.Success)
        }

    private fun createPagingSource() =
        EdgeNewsPagingSource(
            cacheIdentity = NewsCacheIdentity,
            remoteSource = remoteSource,
            localSource = localSource,
            controller = controller,
            converter = converter,
            clearDataHelper = clearDataHelper,
            query = query,
            dispatcher = dispatcher,
        )

    override suspend fun clearDataSource(context: CoroutineDispatcher) {
        clearDataHelper(context = context, action = localSource::clear)
    }

    private object NewsCacheIdentity : CacheIdentity {
        override val id: Long = 0L
        override val key: String = "edge_news"
    }
}
