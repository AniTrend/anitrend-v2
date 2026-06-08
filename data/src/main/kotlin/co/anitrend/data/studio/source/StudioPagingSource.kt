/*
 * Copyright (C) 2026 AniTrend
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
package co.anitrend.data.studio.source

import androidx.paging.LoadType
import androidx.paging.LoadType.APPEND
import androidx.paging.LoadType.PREPEND
import androidx.paging.LoadType.REFRESH
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
import co.anitrend.data.common.extension.from
import co.anitrend.data.studio.StudioPagedController
import co.anitrend.data.studio.datasource.local.StudioLocalSource
import co.anitrend.data.studio.datasource.remote.StudioRemoteSource
import co.anitrend.data.studio.entity.StudioEntity
import co.anitrend.data.studio.entity.filter.StudioQueryFilter
import co.anitrend.data.studio.model.query.StudioQuery
import co.anitrend.data.util.GraphUtil.toQueryContainerBuilder
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first

internal class StudioPagingSource(
    private val cacheIdentity: CacheIdentity,
    private val remoteSource: StudioRemoteSource,
    private val localSource: StudioLocalSource,
    private val controller: StudioPagedController,
    private val clearDataHelper: IClearDataHelper,
    private val filter: StudioQueryFilter.Search,
    private val query: StudioQuery,
    override val dispatcher: ISupportDispatcher,
) : AbstractPagingMediator<Int, StudioEntity>() {
    fun pagingSourceFactory(): () -> PagingSource<Int, StudioEntity> =
        {
            localSource.rawPagingSource(filter.build(query.param))
        }

    private suspend fun getStudio(requestCallback: RequestCallback) {
        val deferred =
            deferred {
                val queryBuilder =
                    query.toQueryContainerBuilder(
                        supportPagingHelper,
                    )
                remoteSource.getStudioPaged(queryBuilder)
            }

        controller(deferred, requestCallback) {
            supportPagingHelper.from(it.page)
            it
        }
    }

    private suspend operator fun invoke(requestType: Request.Type = Request.Type.INITIAL): MediatorResult {
        cacheIdentity.invoke(
            paging = supportPagingHelper,
            requestHelper = requestHelper,
            requestType = requestType,
            block = ::getStudio,
        )

        val result =
            loadState.first {
                it is LoadState.Success || it is LoadState.Error
            }

        return when (result) {
            is LoadState.Success -> MediatorResult.Success(supportPagingHelper.isPagingLimit)
            is LoadState.Error -> MediatorResult.Error(result.details)
            else -> MediatorResult.Error(UnknownError("No information can be provided"))
        }
    }

    override suspend fun initialize(): InitializeAction {
        cacheIdentity.invoke(
            paging = supportPagingHelper,
            requestHelper = requestHelper,
            requestType = Request.Type.INITIAL,
            block = ::getStudio,
        )
        return InitializeAction.SKIP_INITIAL_REFRESH
    }

    override suspend fun clearDataSource(context: CoroutineDispatcher) {
        clearDataHelper(context) {
            localSource.clear()
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, StudioEntity>,
    ): MediatorResult =
        when (loadType) {
            REFRESH -> {
                clearDataSource(dispatcher.io)
                invoke(requestType = Request.Type.INITIAL)
            }

            PREPEND -> MediatorResult.Success(true)

            APPEND -> invoke(requestType = Request.Type.AFTER)
        }
}
