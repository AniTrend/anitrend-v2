/*
 * Copyright (C) 2021  AniTrend
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

package co.anitrend.data.review.source.contract

import androidx.paging.PagedList
import co.anitrend.arch.data.source.core.SupportCoreDataSource
import co.anitrend.arch.paging.legacy.source.SupportPagingDataSource
import co.anitrend.arch.request.callback.RequestCallback
import co.anitrend.arch.request.model.Request
import co.anitrend.data.android.cache.extensions.invoke
import co.anitrend.data.android.cache.model.CacheIdentity
import co.anitrend.data.android.cache.repository.contract.ICacheStorePolicy
import co.anitrend.data.android.extensions.invoke
import co.anitrend.data.review.cache.ReviewCache
import co.anitrend.data.review.model.mutation.ReviewMutation
import co.anitrend.data.review.model.query.ReviewQuery
import co.anitrend.domain.review.entity.Review
import co.anitrend.domain.review.model.ReviewParam
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull

internal class ReviewSource {

    abstract class Entry : SupportCoreDataSource() {

        protected lateinit var query: ReviewQuery.Entry

        protected lateinit var cacheIdentity: CacheIdentity

        protected abstract val cachePolicy: ICacheStorePolicy

        protected abstract fun observable(): Flow<Review>

        protected abstract suspend fun getEntry(
            requestCallback: RequestCallback
        ): Boolean

        operator fun invoke(param: ReviewParam.Entry): Flow<Review> {
            query = ReviewQuery.Entry(param)
            cacheIdentity = ReviewCache.Identity.Entry(param)
            cachePolicy(
                scope = scope,
                requestHelper = requestHelper,
                cacheIdentity = cacheIdentity,
                block = ::getEntry,
            )
            return observable()
        }
    }

    abstract class Rate : SupportCoreDataSource() {

        protected lateinit var mutation: ReviewMutation.Rate

        protected abstract val observable: Flow<Boolean?>

        protected abstract suspend fun rateEntry(
            requestCallback: RequestCallback
        )

        operator fun invoke(param: ReviewParam.Rate): Flow<Boolean> {
            mutation = ReviewMutation.Rate(param)
            invoke(block = ::rateEntry)
            return observable.filterNotNull()
        }
    }

    abstract class Delete : SupportCoreDataSource() {

        protected lateinit var mutation: ReviewMutation.Delete

        protected abstract val observable: Flow<Boolean?>

        protected abstract suspend fun deleteEntry(
            requestCallback: RequestCallback
        )

        operator fun invoke(param: ReviewParam.Delete): Flow<Boolean> {
            mutation = ReviewMutation.Delete(param)
            invoke(block = ::deleteEntry)
            return observable.filterNotNull()
        }
    }

    abstract class Save : SupportCoreDataSource() {

        protected lateinit var mutation: ReviewMutation.Save

        protected abstract val observable: Flow<Boolean?>

        protected abstract suspend fun deleteEntry(
            requestCallback: RequestCallback
        )

        operator fun invoke(param: ReviewParam.Save): Flow<Boolean> {
            mutation = ReviewMutation.Save(param)
            invoke(block = ::deleteEntry)
            return observable.filterNotNull()
        }
    }

    abstract class Paged : SupportPagingDataSource<Review>() {

        protected lateinit var query: ReviewQuery.Paged

        protected abstract val cacheIdentity: CacheIdentity

        protected abstract fun observable(): Flow<PagedList<Review>>

        protected abstract suspend fun getReview(
            requestCallback: RequestCallback
        )

        operator fun invoke(param: ReviewParam.Paged): Flow<PagedList<Review>> {
            query = ReviewQuery.Paged(param)
            return observable()
        }

        /**
         * Called when the item at the end of the PagedList has been loaded, and access has
         * occurred within [Config.prefetchDistance] of it.
         *
         * No more data will be appended to the PagedList after this item.
         *
         * @param itemAtEnd The first item of PagedList
         */
        override fun onItemAtEndLoaded(itemAtEnd: Review) {
            cacheIdentity(
                scope = scope,
                paging = supportPagingHelper,
                requestHelper = requestHelper,
                requestType = Request.Type.AFTER,
                block = ::getReview
            )
        }

        /**
         * Called when the item at the front of the PagedList has been loaded, and access has
         * occurred within [Config.prefetchDistance] of it.
         *
         * No more data will be prepended to the PagedList before this item.
         *
         * @param itemAtFront The first item of PagedList
         */
        override fun onItemAtFrontLoaded(itemAtFront: Review) {
            super.onItemAtFrontLoaded(itemAtFront)
        }

        /**
         * Called when zero items are returned from an initial load of the PagedList's data source.
         */
        override fun onZeroItemsLoaded() {
            cacheIdentity(
                scope = scope,
                paging = supportPagingHelper,
                requestHelper = requestHelper,
                block = ::getReview
            )
        }
    }
}
