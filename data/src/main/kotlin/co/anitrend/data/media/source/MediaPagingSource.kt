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
package co.anitrend.data.media.source

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
import co.anitrend.data.carousel.source.contract.CarouselSource
import co.anitrend.data.common.extension.from
import co.anitrend.data.media.MediaPagedController
import co.anitrend.data.media.datasource.local.MediaLocalSource
import co.anitrend.data.media.datasource.remote.MediaRemoteSource
import co.anitrend.data.media.entity.filter.MediaQueryFilter
import co.anitrend.data.media.entity.view.MediaEntityView
import co.anitrend.data.media.model.query.MediaQuery
import co.anitrend.data.util.GraphUtil.toQueryContainerBuilder
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first

internal class MediaPagingSource(
    private val cacheIdentity: CacheIdentity,
    private val remoteSource: MediaRemoteSource,
    private val localSource: MediaLocalSource,
    private val carouselSource: CarouselSource,
    private val controller: MediaPagedController,
    private val clearDataHelper: IClearDataHelper,
    private val filter: MediaQueryFilter.Paged,
    private val query: MediaQuery.Find,
    override val dispatcher: ISupportDispatcher,
) : AbstractPagingMediator<Int, MediaEntityView.Core>() {
    fun pagingSourceFactory(): () -> PagingSource<Int, MediaEntityView.Core> =
        {
            localSource.rawPagingSource(filter.build(query.param))
        }

    private suspend fun getMedia(requestCallback: RequestCallback) {
        val deferred =
            deferred {
                val queryBuilder =
                    query.toQueryContainerBuilder(
                        supportPagingHelper,
                    )
                remoteSource.getMediaPaged(queryBuilder)
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
            block = ::getMedia,
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
            block = ::getMedia,
        )
        return InitializeAction.SKIP_INITIAL_REFRESH
    }

    override suspend fun clearDataSource(context: CoroutineDispatcher) {
        carouselSource.clearDataSource(context)
        clearDataHelper(context) {
            localSource.clear()
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MediaEntityView.Core>,
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
