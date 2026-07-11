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
import androidx.paging.PagingSource
import androidx.paging.PagingState
import co.anitrend.arch.domain.entities.LoadState
import co.anitrend.arch.extension.dispatchers.contract.ISupportDispatcher
import co.anitrend.arch.request.callback.RequestCallback
import co.anitrend.arch.request.model.Request
import co.anitrend.data.android.cache.extensions.invoke
import co.anitrend.data.android.cache.model.CacheIdentity
import co.anitrend.data.android.cleaner.contract.IClearDataHelper
import co.anitrend.data.android.extensions.deferred
import co.anitrend.data.android.paging.AbstractPagingMediator
import co.anitrend.data.edge.graphql.NewsConnection
import co.anitrend.data.edge.news.EdgeNewsController
import co.anitrend.data.edge.news.converter.EdgeNewsEntityConverter
import co.anitrend.data.edge.news.datasource.local.EdgeNewsLocalSource
import co.anitrend.data.edge.news.datasource.remote.EdgeNewsRemoteSource
import co.anitrend.domain.news.entity.News
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first

@OptIn(ExperimentalPagingApi::class)
internal class EdgeNewsPagingSource(
    private val cacheIdentity: CacheIdentity,
    private val remoteSource: EdgeNewsRemoteSource,
    private val localSource: EdgeNewsLocalSource,
    private val controller: EdgeNewsController,
    private val converter: EdgeNewsEntityConverter,
    private val clearDataHelper: IClearDataHelper,
    override val dispatcher: ISupportDispatcher,
) : AbstractPagingMediator<Int, News>() {
    private var nextCursor: String? = null

    fun pagingSourceFactory(factoryQuery: androidx.sqlite.db.SupportSQLiteQuery): () -> PagingSource<Int, News> =
        localSource
            .rawFactory(factoryQuery)
            .map { entity -> converter.convertFrom(entity) }
            .asPagingSourceFactory()

    fun pagingSourceFactory(): () -> PagingSource<Int, News> =
        localSource
            .entryFactory()
            .map { entity -> converter.convertFrom(entity) }
            .asPagingSourceFactory()

    private suspend fun getNews(requestCallback: RequestCallback) {
        val deferred =
            deferred {
                remoteSource.getNewsConnection(
                    NewsConnection.request(
                        after = nextCursor,
                        limit = supportPagingHelper.pageSize.toDouble(),
                    ),
                )
            }
        controller(deferred, requestCallback) {
            // Determine paging completion based on returned data size and/or "last" cursor
            val items = it.news.data
            supportPagingHelper.isPagingLimit = items.size < supportPagingHelper.pageSize
            nextCursor = it.news.last
            it
        }
    }

    private suspend operator fun invoke(requestType: Request.Type = Request.Type.INITIAL): MediatorResult {
        cacheIdentity.invoke(
            paging = supportPagingHelper,
            requestHelper = requestHelper,
            requestType = requestType,
            block = ::getNews,
        )
        val result = loadState.first { it is LoadState.Success || it is LoadState.Error }
        return when (result) {
            is LoadState.Success -> MediatorResult.Success(supportPagingHelper.isPagingLimit)
            is LoadState.Error -> MediatorResult.Error(result.details)
            else -> MediatorResult.Error(UnknownError("No information can be provided"))
        }
    }

    override suspend fun initialize(): InitializeAction {
        // Defer initial to Paging when it calls load(REFRESH)
        nextCursor = null
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun clearDataSource(context: CoroutineDispatcher) {
        clearDataHelper(context = context, action = localSource::clear)
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, co.anitrend.domain.news.entity.News>,
    ): MediatorResult =
        when (loadType) {
            LoadType.REFRESH -> {
                nextCursor = null
                clearDataSource(dispatcher.io)
                invoke(requestType = Request.Type.INITIAL)
            }
            LoadType.PREPEND -> MediatorResult.Success(true)
            LoadType.APPEND -> invoke(requestType = Request.Type.AFTER)
        }
}
