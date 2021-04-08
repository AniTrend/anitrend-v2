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

package co.anitrend.data.media.source.contract

import androidx.paging.PagedList
import co.anitrend.arch.data.request.callback.RequestCallback
import co.anitrend.arch.data.request.model.Request
import co.anitrend.arch.data.source.core.SupportCoreDataSource
import co.anitrend.arch.data.source.live.SupportPagingLiveDataSource
import co.anitrend.arch.data.source.paging.SupportPagingDataSource
import co.anitrend.data.android.cache.extensions.invoke
import co.anitrend.data.android.cache.model.CacheIdentity
import co.anitrend.data.android.cache.repository.contract.ICacheStorePolicy
import co.anitrend.data.media.cache.MediaCache
import co.anitrend.data.media.model.query.MediaQuery
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.media.model.MediaParam
import kotlinx.coroutines.flow.Flow

internal class MediaSource {

    abstract class Detail : SupportCoreDataSource() {

        protected lateinit var query: MediaQuery.Detail

        protected lateinit var cacheIdentity: CacheIdentity

        protected abstract val cachePolicy: ICacheStorePolicy

        protected abstract fun observable(): Flow<Media>

        protected abstract suspend fun getMedia(requestCallback: RequestCallback): Boolean

        operator fun invoke(param: MediaParam.Detail): Flow<Media> {
            query = MediaQuery.Detail(param)
            cacheIdentity = MediaCache.Identity.Detail(param)
            cachePolicy(
                scope = scope,
                requestHelper = requestHelper,
                cacheIdentity = cacheIdentity,
                block = ::getMedia,
            )
            return observable()
        }
    }

    abstract class Paged : SupportPagingDataSource<Media>() {

        protected lateinit var query: MediaQuery.Find

        protected abstract val cacheIdentity: CacheIdentity

        protected abstract fun observable(): Flow<PagedList<Media>>

        protected abstract suspend fun getMedia(requestCallback: RequestCallback)

        operator fun invoke(param: MediaParam.Find): Flow<PagedList<Media>> {
            query = MediaQuery.Find(param)
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
        override fun onItemAtEndLoaded(itemAtEnd: Media) {
            cacheIdentity(
                scope = scope,
                paging = supportPagingHelper,
                requestHelper = requestHelper,
                requestType = Request.Type.AFTER,
                block = ::getMedia
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
        override fun onItemAtFrontLoaded(itemAtFront: Media) {
            cacheIdentity(
                scope = scope,
                paging = supportPagingHelper,
                requestHelper = requestHelper,
                requestType = Request.Type.BEFORE,
                block = ::getMedia
            )
        }

        /**
         * Called when zero items are returned from an initial load of the PagedList's data source.
         */
        override fun onZeroItemsLoaded() {
            cacheIdentity(
                scope = scope,
                paging = supportPagingHelper,
                requestHelper = requestHelper,
                block = ::getMedia
            )
        }
    }

    abstract class Network : SupportPagingLiveDataSource<MediaParam.Find, Media>() {

        protected abstract val cacheIdentity: CacheIdentity

        protected abstract val initialKey: MediaParam.Find

        protected abstract suspend fun getMedia(
            param: MediaParam.Find,
            callback: RequestCallback
        ) : List<Media>

        /**
         * Load initial data.
         *
         * This method is called first to initialize a PagedList with data. If it's possible to count
         * the items that can be loaded by the DataSource, it's recommended to pass the loaded data to
         * the callback via the three-parameter
         * [LoadInitialCallback.onResult]. This enables PagedLists
         * presenting data from this source to display placeholders to represent unloaded items.
         *
         * [LoadInitialParams.requestedLoadSize] is a hint, not a requirement, so it may be may be
         * altered or ignored.
         *
         * @param params Parameters for initial load, including requested load size.
         * @param callback Callback that receives initial load data.
         */
        override fun loadInitial(
            params: LoadInitialParams<MediaParam.Find>,
            callback: LoadInitialCallback<MediaParam.Find, Media>
        ) {
            cacheIdentity(
                scope = scope,
                paging = supportPagingHelper,
                requestHelper = requestHelper
            ) {
                val result = getMedia(initialKey, it)
                callback.onResult(result, null, initialKey)
            }
        }

        /**
         * Append page with the key specified by [LoadParams.key].
         *
         * It's valid to return a different list size than the page size if it's easier, e.g. if your
         * backend defines page sizes. It is generally safer to increase the number loaded than reduce.
         *
         * Data may be passed synchronously during the load method, or deferred and called at a
         * later time. Further loads going down will be blocked until the callback is called.
         *
         * If data cannot be loaded (for example, if the request is invalid, or the data would be stale
         * and inconsistent, it is valid to call [.invalidate] to invalidate the data source,
         * and prevent further loading.
         *
         * @param params Parameters for the load, including the key for the new page, and requested load
         * size.
         * @param callback Callback that receives loaded data.
         */
        override fun loadAfter(
            params: LoadParams<MediaParam.Find>,
            callback: LoadCallback<MediaParam.Find, Media>
        ) {
            cacheIdentity(
                scope = scope,
                paging = supportPagingHelper,
                requestHelper = requestHelper,
                requestType = Request.Type.AFTER
            ) {
                val result = getMedia(params.key, it)
                callback.onResult(result, params.key)
            }
        }

        /**
         * Prepend page with the key specified by [LoadParams.key].
         *
         * It's valid to return a different list size than the page size if it's easier, e.g. if your
         * backend defines page sizes. It is generally safer to increase the number loaded than reduce.
         *
         * Data may be passed synchronously during the load method, or deferred and called at a
         * later time. Further loads going down will be blocked until the callback is called.
         *
         * If data cannot be loaded (for example, if the request is invalid, or the data would be stale
         * and inconsistent, it is valid to call [.invalidate] to invalidate the data source,
         * and prevent further loading.
         *
         * @param params Parameters for the load, including the key for the new page, and requested load
         * size.
         * @param callback Callback that receives loaded data.
         */
        override fun loadBefore(
            params: LoadParams<MediaParam.Find>,
            callback: LoadCallback<MediaParam.Find, Media>
        ) {
            cacheIdentity(
                scope = scope,
                paging = supportPagingHelper,
                requestHelper = requestHelper,
                requestType = Request.Type.BEFORE
            ) {
                val result = getMedia(params.key, it)
                callback.onResult(result, params.key)
            }

        }
    }
}
