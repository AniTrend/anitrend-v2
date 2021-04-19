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

package co.anitrend.data.medialist.source.contract

import androidx.paging.PagedList
import co.anitrend.arch.data.request.callback.RequestCallback
import co.anitrend.arch.data.request.model.Request
import co.anitrend.arch.data.source.core.SupportCoreDataSource
import co.anitrend.arch.data.source.paging.SupportPagingDataSource
import co.anitrend.data.android.cache.extensions.invoke
import co.anitrend.data.android.cache.model.CacheIdentity
import co.anitrend.data.android.cache.repository.contract.ICacheStorePolicy
import co.anitrend.data.android.extensions.invoke
import co.anitrend.data.medialist.cache.MediaListCache
import co.anitrend.data.medialist.model.mutation.MediaListMutation
import co.anitrend.data.medialist.model.query.MediaListQuery
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.medialist.entity.MediaList
import co.anitrend.domain.medialist.model.MediaListParam
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull

internal class MediaListSource {

    abstract class Sync : SupportCoreDataSource() {

        protected lateinit var query: MediaListQuery.Collection

        protected abstract val observable: Flow<Boolean?>

        protected abstract suspend fun getMediaList(
            requestCallback: RequestCallback
        )

        operator fun invoke(param: MediaListParam.Collection): Flow<Boolean> {
            query = MediaListQuery.Collection(param)
            invoke(block = ::getMediaList)
            return observable.filterNotNull()
        }
    }

    abstract class Entry : SupportCoreDataSource() {

        protected lateinit var query: MediaListQuery.Entry

        protected lateinit var cacheIdentity: CacheIdentity

        protected abstract val cachePolicy: ICacheStorePolicy

        protected abstract fun observable(): Flow<MediaList>

        protected abstract suspend fun getEntry(
            requestCallback: RequestCallback
        ): Boolean

        operator fun invoke(param: MediaListParam.Entry): Flow<MediaList> {
            query = MediaListQuery.Entry(param)
            cacheIdentity = MediaListCache.Identity.Entry(param)
            cachePolicy(
                scope = scope,
                requestHelper = requestHelper,
                cacheIdentity = cacheIdentity,
                block = ::getEntry,
            )
            return observable()
        }
    }

    abstract class Paged : SupportPagingDataSource<Media>() {

        protected lateinit var query: MediaListQuery.Paged

        protected abstract val cacheIdentity: CacheIdentity

        protected abstract fun observable(): Flow<PagedList<Media>>

        protected abstract suspend fun getMediaList(
            requestCallback: RequestCallback
        )

        operator fun invoke(param: MediaListParam.Paged): Flow<PagedList<Media>> {
            query = MediaListQuery.Paged(param)
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
                block = ::getMediaList
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
                block = ::getMediaList
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
                block = ::getMediaList
            )
        }
    }

    abstract class Collection : SupportPagingDataSource<Media>() {

        protected lateinit var query: MediaListQuery.Collection

        protected abstract val cacheIdentity: CacheIdentity

        protected abstract fun observable(): Flow<PagedList<Media>>

        protected abstract suspend fun getMediaList(
            requestCallback: RequestCallback
        )

        operator fun invoke(param: MediaListParam.Collection): Flow<PagedList<Media>> {
            query = MediaListQuery.Collection(param)
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
                block = ::getMediaList
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
                block = ::getMediaList
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
                block = ::getMediaList
            )
        }
    }

    abstract class SaveEntry : SupportCoreDataSource() {

        protected lateinit var mutation: MediaListMutation.SaveEntry

        protected abstract val observable: Flow<Boolean?>

        protected abstract suspend fun saveEntry(
            requestCallback: RequestCallback
        )

        operator fun invoke(param: MediaListParam.SaveEntry): Flow<Boolean> {
            mutation = MediaListMutation.SaveEntry(param)
            invoke(block = ::saveEntry)
            return observable.filterNotNull()
        }
    }

    abstract class SaveEntries : SupportCoreDataSource() {

        protected lateinit var mutation: MediaListMutation.SaveEntries

        protected abstract val observable: Flow<Boolean?>

        protected abstract suspend fun saveEntries(
            requestCallback: RequestCallback
        )

        operator fun invoke(param: MediaListParam.SaveEntries): Flow<Boolean> {
            mutation = MediaListMutation.SaveEntries(param)
            invoke(block = ::saveEntries)
            return observable.filterNotNull()
        }
    }

    abstract class DeleteEntry : SupportCoreDataSource() {

        protected lateinit var mutation: MediaListMutation.DeleteEntry

        protected abstract val observable: Flow<Boolean?>

        protected abstract suspend fun deleteEntry(
            requestCallback: RequestCallback
        )

        operator fun invoke(param: MediaListParam.DeleteEntry): Flow<Boolean> {
            mutation = MediaListMutation.DeleteEntry(param)
            invoke(block = ::deleteEntry)
            return observable.filterNotNull()
        }
    }

    abstract class DeleteCustomList : SupportCoreDataSource() {

        protected lateinit var mutation: MediaListMutation.DeleteCustomList

        protected abstract val observable: Flow<Boolean?>

        protected abstract suspend fun deleteCustomList(
            requestCallback: RequestCallback
        )

        operator fun invoke(param: MediaListParam.DeleteCustomList): Flow<Boolean> {
            mutation = MediaListMutation.DeleteCustomList(param)
            invoke(block = ::deleteCustomList)
            return observable.filterNotNull()
        }
    }
}