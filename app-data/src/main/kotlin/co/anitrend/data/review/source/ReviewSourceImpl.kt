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

package co.anitrend.data.review.source

import androidx.paging.PagedList
import co.anitrend.arch.data.paging.FlowPagedListBuilder
import co.anitrend.arch.data.request.callback.RequestCallback
import co.anitrend.arch.data.util.PAGING_CONFIGURATION
import co.anitrend.arch.extension.dispatchers.contract.ISupportDispatcher
import co.anitrend.data.android.cache.model.CacheIdentity
import co.anitrend.data.android.cache.repository.contract.ICacheStorePolicy
import co.anitrend.data.android.cleaner.contract.IClearDataHelper
import co.anitrend.data.common.extension.from
import co.anitrend.data.review.*
import co.anitrend.data.review.cache.ReviewCache
import co.anitrend.data.review.converter.ReviewEntityViewConverter
import co.anitrend.data.review.datasource.local.ReviewLocalSource
import co.anitrend.data.review.datasource.remote.ReviewRemoteSource
import co.anitrend.data.review.entity.filter.ReviewQueryFilter
import co.anitrend.data.review.source.contract.ReviewSource
import co.anitrend.data.util.GraphUtil.toQueryContainerBuilder
import co.anitrend.domain.review.entity.Review
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*

internal sealed class ReviewSourceImpl {

    class Entry(
        private val remoteSource: ReviewRemoteSource,
        private val localSource: ReviewLocalSource,
        private val controller: ReviewEntryController,
        private val converter: ReviewEntityViewConverter,
        private val filter: ReviewQueryFilter.Entry,
        private val clearDataHelper: IClearDataHelper,
        override val cachePolicy: ICacheStorePolicy,
        override val dispatcher: ISupportDispatcher
    ) : ReviewSource.Entry() {

        override fun observable(): Flow<Review> {
            return localSource.rawFlow(
                filter.build(query.param)
            ).flowOn(dispatcher.io)
                .filterNotNull()
                .map(converter::convertFrom)
                .distinctUntilChanged()
                .flowOn(dispatcher.computation)
        }

        override suspend fun getEntry(requestCallback: RequestCallback): Boolean {
            val deferred = async {
                val queryBuilder = query.toQueryContainerBuilder()
                remoteSource.getReview(queryBuilder)
            }

            val result = controller(deferred, requestCallback)

            return result != null
        }

        /**
         * Clears data sources (databases, preferences, e.t.c)
         *
         * @param context Dispatcher context to run in
         */
        override suspend fun clearDataSource(context: CoroutineDispatcher) {
            clearDataHelper(context) {
                cachePolicy.invalidateLastRequest(cacheIdentity)
                localSource.clearById(cacheIdentity.id)
            }
        }
    }

    class Rate(
        private val remoteSource: ReviewRemoteSource,
        private val controller: ReviewRateController,
        override val dispatcher: ISupportDispatcher
    ) : ReviewSource.Rate() {

        override val observable: MutableStateFlow<Boolean?> = MutableStateFlow(null)

        override suspend fun rateEntry(requestCallback: RequestCallback) {
            val deferred = async {
                val queryBuilder = mutation.toQueryContainerBuilder()
                remoteSource.rateReview(queryBuilder)
            }

            val result = controller(deferred, requestCallback)
            observable.value = result != null
        }

        /**
         * Clears data sources (databases, preferences, e.t.c)
         *
         * @param context Dispatcher context to run in
         */
        override suspend fun clearDataSource(context: CoroutineDispatcher) {

        }
    }

    class Delete(
        private val remoteSource: ReviewRemoteSource,
        private val localSource: ReviewLocalSource,
        private val controller: ReviewDeleteController,
        private val clearDataHelper: IClearDataHelper,
        override val dispatcher: ISupportDispatcher
    ) : ReviewSource.Delete() {

        override val observable: MutableStateFlow<Boolean?> = MutableStateFlow(null)

        override suspend fun deleteEntry(requestCallback: RequestCallback) {
            val deferred = async {
                val queryBuilder = mutation.toQueryContainerBuilder()
                remoteSource.deleteReview(queryBuilder)
            }

            val result = controller(deferred, requestCallback)
            observable.value = result != null
        }

        /**
         * Clears data sources (databases, preferences, e.t.c)
         *
         * @param context Dispatcher context to run in
         */
        override suspend fun clearDataSource(context: CoroutineDispatcher) {
            clearDataHelper(context) {
                localSource.clearById(mutation.param.id)
            }
        }
    }

    class Save(
        private val remoteSource: ReviewRemoteSource,
        private val controller: ReviewSaveController,
        override val dispatcher: ISupportDispatcher
    ) : ReviewSource.Save() {
        
        override val observable: MutableStateFlow<Boolean?> = MutableStateFlow(null)

        override suspend fun deleteEntry(requestCallback: RequestCallback) {
            val deferred = async {
                val queryBuilder = mutation.toQueryContainerBuilder()
                remoteSource.saveReview(queryBuilder)
            }

            val result = controller(deferred, requestCallback)
            observable.value = result != null
        }

        /**
         * Clears data sources (databases, preferences, e.t.c)
         *
         * @param context Dispatcher context to run in
         */
        override suspend fun clearDataSource(context: CoroutineDispatcher) {

        }
    }

    class Paged(
        private val remoteSource: ReviewRemoteSource,
        private val localSource: ReviewLocalSource,
        private val controller: ReviewPagedController,
        private val filter: ReviewQueryFilter.Paged,
        private val converter: ReviewEntityViewConverter,
        private val clearDataHelper: IClearDataHelper,
        override val dispatcher: ISupportDispatcher,
    ) : ReviewSource.Paged() {

        override val cacheIdentity: CacheIdentity = ReviewCache.Identity.Paged()

        override fun observable(): Flow<PagedList<Review>> {
            val dataSourceFactory = localSource
                .rawFactory(filter.build(query.param))
                .map(converter::convertFrom)

            return FlowPagedListBuilder(
                dataSourceFactory,
                PAGING_CONFIGURATION,
                null,
                this
            ).buildFlow()
        }

        override suspend fun getReview(requestCallback: RequestCallback) {
            val deferred = async {
                val queryBuilder = query.toQueryContainerBuilder(
                    supportPagingHelper
                )
                remoteSource.getReviewPaged(queryBuilder)
            }

            controller(deferred, requestCallback) {
                supportPagingHelper.from(it.page)
                it
            }
        }

        /**
         * Clears data sources (databases, preferences, e.t.c)
         *
         * @param context Dispatcher context to run in
         */
        override suspend fun clearDataSource(context: CoroutineDispatcher) {
            clearDataHelper(context) {
                localSource.clear()
            }
        }
    }
}