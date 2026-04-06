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
package co.anitrend.data.review.source

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
import co.anitrend.data.review.ReviewPagedController
import co.anitrend.data.review.converter.ReviewEntityViewConverter
import co.anitrend.data.review.datasource.local.ReviewLocalSource
import co.anitrend.data.review.datasource.remote.ReviewRemoteSource
import co.anitrend.data.review.entity.filter.ReviewQueryFilter
import co.anitrend.data.review.model.query.ReviewQuery
import co.anitrend.data.util.GraphUtil.toQueryContainerBuilder
import co.anitrend.domain.review.entity.Review
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first

internal class ReviewPagingSource(
    private val cacheIdentity: CacheIdentity,
    private val remoteSource: ReviewRemoteSource,
    private val localSource: ReviewLocalSource,
    private val controller: ReviewPagedController,
    private val filter: ReviewQueryFilter.Paged,
    private val converter: ReviewEntityViewConverter,
    private val clearDataHelper: IClearDataHelper,
    private val query: ReviewQuery.Paged,
    override val dispatcher: ISupportDispatcher,
) : AbstractPagingMediator<Int, Review>() {
    fun pagingSourceFactory(): () -> PagingSource<Int, Review> =
        localSource
            .rawFactory(filter.build(query.param))
            .map(converter::convertFrom)
            .asPagingSourceFactory()

    private suspend fun getReview(requestCallback: RequestCallback) {
        val deferred =
            deferred {
                val queryBuilder =
                    query.toQueryContainerBuilder(
                        supportPagingHelper,
                    )
                remoteSource.getReviewPaged(queryBuilder)
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
            block = ::getReview,
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
            block = ::getReview,
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
        state: PagingState<Int, Review>,
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
