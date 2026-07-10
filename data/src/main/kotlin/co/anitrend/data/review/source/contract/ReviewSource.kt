/*
 * Copyright (C) 2021 AniTrend
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

import androidx.paging.PagingData
import co.anitrend.arch.request.callback.RequestCallback
import co.anitrend.arch.request.model.Request
import co.anitrend.data.android.cache.extensions.invoke
import co.anitrend.data.android.cache.model.CacheIdentity
import co.anitrend.data.android.cache.repository.contract.ICacheStorePolicy
import co.anitrend.data.android.extensions.invoke
import co.anitrend.data.android.source.AbstractCoreDataSource
import co.anitrend.data.review.cache.ReviewCache
import co.anitrend.domain.review.entity.Review
import co.anitrend.domain.review.model.ReviewParam
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull

internal class ReviewSource {
    abstract class Entry : AbstractCoreDataSource() {
        protected lateinit var query: ReviewParam.Entry

        protected lateinit var cacheIdentity: CacheIdentity

        protected abstract val cachePolicy: ICacheStorePolicy

        protected abstract fun observable(): Flow<Review>

        protected abstract suspend fun getEntry(requestCallback: RequestCallback): Boolean

        operator fun invoke(param: ReviewParam.Entry): Flow<Review> {
            query = param
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

    abstract class Rate : AbstractCoreDataSource() {
        protected lateinit var mutation: ReviewParam.Rate

        protected abstract val observable: Flow<Boolean?>

        protected abstract suspend fun rateEntry(requestCallback: RequestCallback)

        operator fun invoke(param: ReviewParam.Rate): Flow<Boolean> {
            mutation = param
            invoke(block = ::rateEntry)
            return observable.filterNotNull()
        }
    }

    abstract class Delete : AbstractCoreDataSource() {
        protected lateinit var mutation: ReviewParam.Delete

        protected abstract val observable: Flow<Boolean?>

        protected abstract suspend fun deleteEntry(requestCallback: RequestCallback)

        operator fun invoke(param: ReviewParam.Delete): Flow<Boolean> {
            mutation = param
            invoke(block = ::deleteEntry)
            return observable.filterNotNull()
        }
    }

    abstract class Save : AbstractCoreDataSource() {
        protected lateinit var mutation: ReviewParam.Save

        protected abstract val observable: Flow<Boolean?>

        protected abstract suspend fun deleteEntry(requestCallback: RequestCallback)

        operator fun invoke(param: ReviewParam.Save): Flow<Boolean> {
            mutation = param
            invoke(block = ::deleteEntry)
            return observable.filterNotNull()
        }
    }

    abstract class Paging {
        protected lateinit var query: ReviewParam.Paged

        abstract operator fun invoke(param: ReviewParam.Paged): Flow<PagingData<Review>>

        protected fun assignQuery(param: ReviewParam.Paged) {
            query = param
        }
    }
}
