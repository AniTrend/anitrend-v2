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

package co.anitrend.data.medialist.source

import androidx.paging.PagedList
import co.anitrend.arch.extension.dispatchers.contract.ISupportDispatcher
import co.anitrend.arch.paging.legacy.FlowPagedListBuilder
import co.anitrend.arch.paging.legacy.util.PAGING_CONFIGURATION
import co.anitrend.arch.request.callback.RequestCallback
import co.anitrend.data.android.cache.model.CacheIdentity
import co.anitrend.data.android.cache.repository.contract.ICacheStorePolicy
import co.anitrend.data.android.cleaner.contract.IClearDataHelper
import co.anitrend.data.android.extensions.deferred
import co.anitrend.data.auth.settings.IAuthenticationSettings
import co.anitrend.data.common.extension.from
import co.anitrend.data.customlist.datasource.CustomListLocalSource
import co.anitrend.data.media.converter.MediaEntityViewConverter
import co.anitrend.data.media.datasource.local.MediaLocalSource
import co.anitrend.data.medialist.DeleteCustomListController
import co.anitrend.data.medialist.MediaListCollectionController
import co.anitrend.data.medialist.MediaListDeleteEntryController
import co.anitrend.data.medialist.MediaListEntryController
import co.anitrend.data.medialist.MediaListPagedController
import co.anitrend.data.medialist.MediaListSaveEntriesController
import co.anitrend.data.medialist.MediaListSaveEntryController
import co.anitrend.data.medialist.cache.MediaListCache
import co.anitrend.data.medialist.datasource.local.MediaListLocalSource
import co.anitrend.data.medialist.datasource.remote.MediaListRemoteSource
import co.anitrend.data.medialist.entity.filter.MediaListQueryFilter
import co.anitrend.data.medialist.source.contract.MediaListSource
import co.anitrend.data.user.source.contract.UserSource
import co.anitrend.data.util.GraphUtil.toQueryContainerBuilder
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.user.model.UserParam
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

internal class MediaListSourceImpl {

    class Sync(
        private val remoteSource: MediaListRemoteSource,
        private val controller: MediaListCollectionController,
        override val dispatcher: ISupportDispatcher
    ) : MediaListSource.Sync() {

        override val observable: MutableStateFlow<Boolean?> = MutableStateFlow(null)

        override suspend fun getMediaList(requestCallback: RequestCallback) {
            val deferred = deferred {
                val queryBuilder = query.toQueryContainerBuilder()
                remoteSource.getMediaListCollection(queryBuilder)
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

    class Entry(
        private val remoteSource: MediaListRemoteSource,
        private val localSource: MediaListLocalSource,
        private val mediaLocalSource: MediaLocalSource,
        private val controller: MediaListEntryController,
        private val filter: MediaListQueryFilter.Entry,
        private val converter: MediaEntityViewConverter,
        private val clearDataHelper: IClearDataHelper,
        override val dispatcher: ISupportDispatcher,
        override val cachePolicy: ICacheStorePolicy
    ) : MediaListSource.Entry() {

        override fun observable(): Flow<Media> {
            return mediaLocalSource.rawFlow(
                filter.build(query.param)
            ).flowOn(dispatcher.io)
                .filterNotNull()
                .map(converter::convertFrom)
                .distinctUntilChanged()
                .flowOn(dispatcher.computation)
        }

        override suspend fun getEntry(requestCallback: RequestCallback): Boolean {
            val deferred = deferred {
                val queryBuilder = query.toQueryContainerBuilder()
                remoteSource.getMediaListEntry(queryBuilder)
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
                localSource.clearByMediaId(
                    mediaId = query.param.mediaId,
                    userId = query.param.userId
                )
                cachePolicy.invalidateLastRequest(cacheIdentity)
            }
        }
    }

    class Paged(
        private val remoteSource: MediaListRemoteSource,
        private val localSource: MediaListLocalSource,
        private val mediaLocalSource: MediaLocalSource,
        private val controller: MediaListPagedController,
        private val converter: MediaEntityViewConverter,
        private val filter: MediaListQueryFilter.Paged,
        private val clearDataHelper: IClearDataHelper,
        override val dispatcher: ISupportDispatcher
    ) : MediaListSource.Paged() {

        override val cacheIdentity: CacheIdentity = MediaListCache.Identity.Paged()

        override fun observable(): Flow<PagedList<Media>> {
            val dataSourceFactory = mediaLocalSource
                .rawFactory(filter.build(query.param))
                .map(converter::convertFrom)

            return FlowPagedListBuilder(
                dataSourceFactory,
                PAGING_CONFIGURATION,
                null,
                this
            ).buildFlow()
        }

        override suspend fun getMediaList(requestCallback: RequestCallback) {
            val deferred = deferred {
                val queryBuilder = query.toQueryContainerBuilder(
                    supportPagingHelper
                )
                remoteSource.getMediaListPaged(queryBuilder)
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
                if (query.param.userId != null)
                    localSource.clearByUserId(
                        requireNotNull(query.param.userId)
                    )
                else if (query.param.userName != null)
                    localSource.clearByUserName(
                        requireNotNull(query.param.userName)
                    )
            }
        }
    }

    class Collection(
        private val remoteSource: MediaListRemoteSource,
        private val localSource: MediaListLocalSource,
        private val mediaLocalSource: MediaLocalSource,
        private val controller: MediaListCollectionController,
        private val converter: MediaEntityViewConverter,
        private val filter: MediaListQueryFilter.Collection,
        private val clearDataHelper: IClearDataHelper,
        override val dispatcher: ISupportDispatcher
    ) : MediaListSource.Collection() {

        override val cacheIdentity: CacheIdentity = MediaListCache.Identity.Collection()

        override fun observable(): Flow<PagedList<Media>> {
            val dataSourceFactory = mediaLocalSource
                .rawFactory(filter.build(query.param))
                .map(converter::convertFrom)

            return FlowPagedListBuilder(
                dataSourceFactory,
                PAGING_CONFIGURATION,
                null,
                this
            ).buildFlow()
        }

        override suspend fun getMediaList(requestCallback: RequestCallback) {
            val deferred = deferred {
                val queryBuilder = query.toQueryContainerBuilder(
                    supportPagingHelper
                )
                remoteSource.getMediaListCollection(queryBuilder)
            }

            controller(deferred, requestCallback)
        }

        /**
         * Clears data sources (databases, preferences, e.t.c)
         *
         * @param context Dispatcher context to run in
         */
        override suspend fun clearDataSource(context: CoroutineDispatcher) {
            clearDataHelper(context) {
                if (query.param.userId != null)
                    localSource.clearByUserId(
                        requireNotNull(query.param.userId)
                    )
                else if (query.param.userName != null)
                    localSource.clearByUserName(
                        requireNotNull(query.param.userName)
                    )
            }
        }
    }

    class SaveEntry(
        private val remoteSource: MediaListRemoteSource,
        private val controller: MediaListSaveEntryController,
        override val dispatcher: ISupportDispatcher
    ) : MediaListSource.SaveEntry() {

        override val observable: MutableStateFlow<Boolean?> = MutableStateFlow(null)

        override suspend fun saveEntry(requestCallback: RequestCallback) {
            val deferred = deferred {
                val queryBuilder = mutation.toQueryContainerBuilder()
                remoteSource.saveMediaListEntry(queryBuilder)
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

    class SaveEntries(
        private val remoteSource: MediaListRemoteSource,
        private val controller: MediaListSaveEntriesController,
        override val dispatcher: ISupportDispatcher
    ) : MediaListSource.SaveEntries() {

        override val observable: MutableStateFlow<Boolean?> = MutableStateFlow(null)

        override suspend fun saveEntries(requestCallback: RequestCallback) {
            val deferred = deferred {
                val queryBuilder = mutation.toQueryContainerBuilder()
                remoteSource.saveMediaListEntries(queryBuilder)
            }

            val result = controller(deferred, requestCallback)
            observable.value = !result.isNullOrEmpty()
        }

        /**
         * Clears data sources (databases, preferences, e.t.c)
         *
         * @param context Dispatcher context to run in
         */
        override suspend fun clearDataSource(context: CoroutineDispatcher) {

        }
    }

    class DeleteEntry(
        private val remoteSource: MediaListRemoteSource,
        private val localSource: MediaListLocalSource,
        private val controller: MediaListDeleteEntryController,
        private val settings: IAuthenticationSettings,
        private val clearDataHelper: IClearDataHelper,
        override val dispatcher: ISupportDispatcher
    ) : MediaListSource.DeleteEntry() {

        override val observable: MutableStateFlow<Boolean?> = MutableStateFlow(null)

        override suspend fun deleteEntry(requestCallback: RequestCallback) {
            val deferred = deferred {
                val queryBuilder = mutation.toQueryContainerBuilder()
                remoteSource.deleteMediaListEntry(queryBuilder)
            }

            val result = controller(deferred, requestCallback)

            if (result == true)
                clearDataSource(dispatcher.io)

            observable.value = result
        }

        /**
         * Clears data sources (databases, preferences, e.t.c)
         *
         * @param context Dispatcher context to run in
         */
        override suspend fun clearDataSource(context: CoroutineDispatcher) {
            clearDataHelper(context) {
                localSource.clearById(
                    id = mutation.param.id,
                    userId = settings.authenticatedUserId.value
                )
            }
        }
    }

    class DeleteCustomList(
        private val remoteSource: MediaListRemoteSource,
        private val localSource: CustomListLocalSource,
        private val userSource: UserSource.Profile,
        private val controller: DeleteCustomListController,
        private val settings: IAuthenticationSettings,
        private val clearDataHelper: IClearDataHelper,
        override val dispatcher: ISupportDispatcher
    ) : MediaListSource.DeleteCustomList() {

        override val observable: MutableStateFlow<Boolean?> = MutableStateFlow(null)

        override suspend fun deleteCustomList(requestCallback: RequestCallback) {
            val deferred = deferred {
                val queryBuilder = mutation.toQueryContainerBuilder()
                remoteSource.deleteCustomList(queryBuilder)
            }

            val result = controller(deferred, requestCallback)

            if (result == true) {
                val userId = settings.authenticatedUserId.value
                userSource(UserParam.Profile(userId))
                clearDataSource(dispatcher.io)
            }

            observable.value = result
        }

        /**
         * Clears data sources (databases, preferences, e.t.c)
         *
         * @param context Dispatcher context to run in
         */
        override suspend fun clearDataSource(context: CoroutineDispatcher) {
            clearDataHelper(context) {
                val userId = settings.authenticatedUserId.value
                localSource.clear(
                    listName = mutation.param.customList,
                    userId = userId
                )
            }
        }
    }
}
