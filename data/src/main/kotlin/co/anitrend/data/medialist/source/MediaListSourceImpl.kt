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
package co.anitrend.data.medialist.source

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import co.anitrend.arch.extension.dispatchers.contract.ISupportDispatcher
import co.anitrend.arch.extension.util.DEFAULT_PAGE_SIZE
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
import co.anitrend.data.graphql.anilist.DeleteCustomList
import co.anitrend.data.graphql.anilist.DeleteMediaListItem
import co.anitrend.data.graphql.anilist.FuzzyDateInput
import co.anitrend.data.graphql.anilist.GetMediaListCollection
import co.anitrend.data.graphql.anilist.GetMediaListEntry
import co.anitrend.data.graphql.anilist.SaveMediaListEntries
import co.anitrend.data.graphql.anilist.SaveMediaListEntry
import co.anitrend.data.medialist.source.contract.MediaListSource
import co.anitrend.data.user.source.contract.UserSource
import co.anitrend.domain.common.sort.order.SortOrder
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.medialist.model.MediaListParam
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
        override val dispatcher: ISupportDispatcher,
    ) : MediaListSource.Sync() {
        override val observable: MutableStateFlow<Boolean?> = MutableStateFlow(null)

        override suspend fun getMediaList(requestCallback: RequestCallback) {
            val deferred =
                deferred {
                    remoteSource.getMediaListCollection(
                        GetMediaListCollection.request(
                            chunk = query.chunk,
                            completedAt =
                                query.completedAt
                                    ?.toString()
                                    ?.toIntOrNull(),
                            completedAt_greater =
                                query.completedAt_greater
                                    ?.toString()
                                    ?.toIntOrNull(),
                            completedAt_lesser =
                                query.completedAt_lesser
                                    ?.toString()
                                    ?.toIntOrNull(),
                            completedAt_like = query.completedAt_like?.toString(),
                            forceSingleCompletedList = query.forceSingleCompletedList,
                            notes = query.notes,
                            notes_like = query.notes_like,
                            perChunk = query.perChunk,
                            sort =
                                query.sort?.map {
                                    val baseName = it.sortable.name
                                    val enumName = if (it.order == SortOrder.DESC) baseName + "_DESC" else baseName
                                    co.anitrend.data.graphql.anilist.MediaListSort
                                        .valueOf(enumName)
                                },
                            startedAt =
                                query.startedAt
                                    ?.toString()
                                    ?.toIntOrNull(),
                            startedAt_greater =
                                query.startedAt_greater
                                    ?.toString()
                                    ?.toIntOrNull(),
                            startedAt_lesser =
                                query.startedAt_lesser
                                    ?.toString()
                                    ?.toIntOrNull(),
                            startedAt_like = query.startedAt_like?.toString(),
                            status =
                                query.status?.let {
                                    co.anitrend.data.graphql.anilist.MediaListStatus
                                        .valueOf(it.name)
                                },
                            status_in =
                                query.status_in?.map {
                                    co.anitrend.data.graphql.anilist.MediaListStatus
                                        .valueOf(it.name)
                                },
                            status_not =
                                query.status_not?.let {
                                    co.anitrend.data.graphql.anilist.MediaListStatus
                                        .valueOf(it.name)
                                },
                            status_not_in =
                                query.status_not_in?.map {
                                    co.anitrend.data.graphql.anilist.MediaListStatus
                                        .valueOf(it.name)
                                },
                            type =
                                co.anitrend.data.graphql.anilist.MediaType
                                    .valueOf(query.type.name),
                            userId = query.userId?.toInt(),
                            userName = query.userName,
                            scoreFormat =
                                co.anitrend.data.graphql.anilist.ScoreFormat
                                    .valueOf(query.scoreFormat.name),
                        ),
                    )
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
        override val cachePolicy: ICacheStorePolicy,
    ) : MediaListSource.Entry() {
        override fun observable(): Flow<Media> =
            mediaLocalSource
                .rawFlow(
                    filter.build(query),
                ).flowOn(dispatcher.io)
                .filterNotNull()
                .map(converter::convertFrom)
                .distinctUntilChanged()
                .flowOn(dispatcher.computation)

        override suspend fun getEntry(requestCallback: RequestCallback): Boolean {
            val deferred =
                deferred {
                    remoteSource.getMediaListEntry(
                        GetMediaListEntry.request(
                            scoreFormat =
                                co.anitrend.data.graphql.anilist.ScoreFormat
                                    .valueOf(query.scoreFormat.name),
                            mediaId = query.mediaId.toInt(),
                            userId = query.userId.toInt(),
                        ),
                    )
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
                    mediaId = query.mediaId,
                    userId = query.userId,
                )
                cachePolicy.invalidateLastRequest(cacheIdentity)
            }
        }
    }

    class Paging(
        private val remoteSource: MediaListRemoteSource,
        private val localSource: MediaListLocalSource,
        private val mediaLocalSource: MediaLocalSource,
        private val controller: MediaListPagedController,
        private val converter: MediaEntityViewConverter,
        private val filter: MediaListQueryFilter.Paged,
        private val clearDataHelper: IClearDataHelper,
        private val dispatcher: ISupportDispatcher,
    ) : MediaListSource.Paging() {
        override fun invoke(param: MediaListParam.Paged): Flow<PagingData<Media>> {
            assignQuery(param)

            val source =
                MediaListPagingSource(
                    cacheIdentity = MediaListCache.Identity.Paged(param),
                    remoteSource = remoteSource,
                    localSource = localSource,
                    mediaLocalSource = mediaLocalSource,
                    controller = controller,
                    filter = filter,
                    clearDataHelper = clearDataHelper,
                    query = query,
                    dispatcher = dispatcher,
                )

            return Pager(
                config =
                    PagingConfig(
                        pageSize = DEFAULT_PAGE_SIZE,
                        initialLoadSize = DEFAULT_PAGE_SIZE,
                        prefetchDistance = DEFAULT_PAGE_SIZE,
                        enablePlaceholders = false,
                    ),
                remoteMediator = source,
                pagingSourceFactory = source.pagingSourceFactory(),
            ).flow.map { pagingData -> pagingData.map { entity -> converter.convertFrom(entity) } }
        }
    }

    class SaveEntry(
        private val remoteSource: MediaListRemoteSource,
        private val controller: MediaListSaveEntryController,
        override val dispatcher: ISupportDispatcher,
    ) : MediaListSource.SaveEntry() {
        override val observable: MutableStateFlow<Boolean?> = MutableStateFlow(null)

        override suspend fun saveEntry(requestCallback: RequestCallback) {
            val deferred =
                deferred {
                    remoteSource.saveMediaListEntry(
                        SaveMediaListEntry.request(
                            id = mutation.id?.toInt(),
                            mediaId = mutation.mediaId.toInt(),
                            status =
                                co.anitrend.data.graphql.anilist.MediaListStatus
                                    .valueOf(mutation.status.name),
                            score = mutation.score?.toDouble(),
                            scoreRaw = mutation.scoreRaw,
                            progress = mutation.progress,
                            progressVolumes = mutation.progressVolumes,
                            repeat = mutation.repeat,
                            priority = mutation.priority,
                            isPrivate = mutation.private,
                            notes = mutation.notes,
                            hiddenFromStatusLists = mutation.hiddenFromStatusLists,
                            customLists = mutation.customLists,
                            advancedScores = mutation.advancedScores?.map { it.toDouble() },
                            startedAt = mutation.startedAt?.let { FuzzyDateInput(day = it.day, month = it.month, year = it.year) },
                            completedAt = mutation.completedAt?.let { FuzzyDateInput(day = it.day, month = it.month, year = it.year) },
                            scoreFormat =
                                co.anitrend.data.graphql.anilist.ScoreFormat
                                    .valueOf(mutation.scoreFormat.name),
                        ),
                    )
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
        override val dispatcher: ISupportDispatcher,
    ) : MediaListSource.SaveEntries() {
        override val observable: MutableStateFlow<Boolean?> = MutableStateFlow(null)

        override suspend fun saveEntries(requestCallback: RequestCallback) {
            val deferred =
                deferred {
                    remoteSource.saveMediaListEntries(
                        SaveMediaListEntries.request(
                            status =
                                co.anitrend.data.graphql.anilist.MediaListStatus
                                    .valueOf(mutation.status.name),
                            score = mutation.score?.toDouble(),
                            scoreRaw = mutation.scoreRaw,
                            progress = mutation.progress,
                            progressVolumes = mutation.progressVolumes,
                            repeat = mutation.repeat,
                            priority = mutation.priority,
                            isPrivate = mutation.private,
                            notes = mutation.notes,
                            hiddenFromStatusLists = mutation.hiddenFromStatusLists,
                            advancedScores = mutation.advancedScores?.map { it.toDouble() },
                            startedAt = mutation.startedAt?.let { FuzzyDateInput(day = it.day, month = it.month, year = it.year) },
                            completedAt = mutation.completedAt?.let { FuzzyDateInput(day = it.day, month = it.month, year = it.year) },
                            ids = mutation.ids.map { it.toInt() },
                            scoreFormat =
                                co.anitrend.data.graphql.anilist.ScoreFormat
                                    .valueOf(mutation.scoreFormat.name),
                        ),
                    )
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
        override val dispatcher: ISupportDispatcher,
    ) : MediaListSource.DeleteEntry() {
        override val observable: MutableStateFlow<Boolean?> = MutableStateFlow(null)

        override suspend fun deleteEntry(requestCallback: RequestCallback) {
            val deferred =
                deferred {
                    remoteSource.deleteMediaListEntry(
                        DeleteMediaListItem.request(
                            id = mutation.id.toInt(),
                        ),
                    )
                }

            val result = controller(deferred, requestCallback)

            if (result == true) {
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
                localSource.clearById(
                    id = mutation.id,
                    userId = settings.authenticatedUserId.value,
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
        override val dispatcher: ISupportDispatcher,
    ) : MediaListSource.DeleteCustomList() {
        override val observable: MutableStateFlow<Boolean?> = MutableStateFlow(null)

        override suspend fun deleteCustomList(requestCallback: RequestCallback) {
            val deferred =
                deferred {
                    remoteSource.deleteCustomList(
                        co.anitrend.data.graphql.anilist.DeleteCustomList.request(
                            customList = mutation.customList,
                            type =
                                co.anitrend.data.graphql.anilist.MediaType
                                    .valueOf(mutation.type.name),
                        ),
                    )
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
                    listName = mutation.customList,
                    userId = userId,
                )
            }
        }
    }
}
