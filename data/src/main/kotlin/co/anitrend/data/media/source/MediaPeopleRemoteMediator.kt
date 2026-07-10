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
import androidx.paging.PagingState
import co.anitrend.arch.domain.entities.LoadState
import co.anitrend.arch.extension.dispatchers.contract.ISupportDispatcher
import co.anitrend.arch.request.callback.RequestCallback
import co.anitrend.arch.request.model.Request
import co.anitrend.data.android.cache.extensions.invoke
import co.anitrend.data.android.cache.model.CacheIdentity
import co.anitrend.data.android.cache.repository.contract.ICacheStorePolicy
import co.anitrend.data.android.extensions.deferred
import co.anitrend.data.android.paging.AbstractPagingMediator
import co.anitrend.data.common.extension.from
import co.anitrend.data.common.extension.seedFromLocalCount
import co.anitrend.data.graphql.anilist.GetMediaWithCharacter
import co.anitrend.data.graphql.anilist.GetMediaWithCharacterVariables
import co.anitrend.data.graphql.anilist.GetMediaWithStaff
import co.anitrend.data.graphql.anilist.GetMediaWithStaffVariables
import co.anitrend.data.media.MediaCharactersController
import co.anitrend.data.media.MediaStaffController
import co.anitrend.data.media.datasource.local.MediaLocalSource
import co.anitrend.data.media.entity.connection.MediaCharacterConnectionEntity
import co.anitrend.data.media.entity.connection.MediaStaffConnectionEntity
import co.anitrend.data.media.mapper.MediaPeopleMapper
import co.anitrend.data.media.datasource.remote.MediaRemoteSource
import co.anitrend.domain.common.sort.order.SortOrder
import co.anitrend.domain.media.entity.MediaPerson
import co.anitrend.domain.media.model.MediaParam
import co.anitrend.retrofit.graphql.model.GraphQLRequest
import kotlinx.coroutines.flow.first
import org.threeten.bp.Instant

internal sealed class MediaPeopleRemoteMediator<V : Any>(
    protected val cacheIdentity: CacheIdentity,
    protected val cachePolicy: ICacheStorePolicy,
    protected val localSource: MediaLocalSource,
    override val dispatcher: ISupportDispatcher,
) : AbstractPagingMediator<Int, V>() {
    protected suspend fun shouldRefresh(hasLocalData: Boolean): Boolean =
        !hasLocalData || cachePolicy.shouldRefresh(cacheIdentity, cacheIdentity.expiresAt)

    protected fun isCorruptPagingCache(
        itemCount: Int,
        maxSortIndex: Int?,
    ): Boolean = itemCount > 0 && maxSortIndex != itemCount - 1

    protected suspend fun awaitResult(
        requestType: Request.Type,
        block: suspend (RequestCallback) -> Unit,
    ): MediatorResult {
        cacheIdentity.invoke(
            paging = supportPagingHelper,
            requestHelper = requestHelper,
            requestType = requestType,
            block = block,
        )

        return when (
            val result =
                loadState.first {
                    it is LoadState.Success || it is LoadState.Error
                }
        ) {
            is LoadState.Success -> MediatorResult.Success(supportPagingHelper.isPagingLimit)
            is LoadState.Error -> MediatorResult.Error(result.details)
            else -> MediatorResult.Error(UnknownError("No information can be provided"))
        }
    }

    class Characters(
        cacheIdentity: CacheIdentity,
        cachePolicy: ICacheStorePolicy,
        private val query: MediaParam.Characters,
        private val remoteSource: MediaRemoteSource,
        localSource: MediaLocalSource,
        private val controller: MediaCharactersController,
        private val mapper: MediaPeopleMapper.Characters,
        dispatcher: ISupportDispatcher,
    ) : MediaPeopleRemoteMediator<MediaCharacterConnectionEntity>(
            cacheIdentity = cacheIdentity,
            cachePolicy = cachePolicy,
            localSource = localSource,
            dispatcher = dispatcher,
        ) {
        override suspend fun initialize(): InitializeAction {
            var itemCount = localSource.mediaCharactersCount(query.id)
            val maxSortIndex = localSource.mediaCharactersMaxSortIndex(query.id)

            if (isCorruptPagingCache(itemCount, maxSortIndex)) {
                localSource.clearMediaCharactersByMediaId(query.id)
                cachePolicy.invalidateLastRequest(cacheIdentity)
                itemCount = 0
            } else {
                supportPagingHelper.seedFromLocalCount(itemCount)
            }

            return if (shouldRefresh(itemCount > 0)) {
                InitializeAction.LAUNCH_INITIAL_REFRESH
            } else {
                InitializeAction.SKIP_INITIAL_REFRESH
            }
        }

        private suspend fun refreshCharacters(requestCallback: RequestCallback) {
            mapper.onRequest(
                mediaId = query.id,
                page = supportPagingHelper.page,
            )

            val deferred =
                deferred {
                    remoteSource.getMediaCharacters(
                        GraphQLRequest(
                            query = GetMediaWithCharacter.document,
                            operationName = GetMediaWithCharacter.name,
                            variables =
                                GetMediaWithCharacterVariables(
                                    id = query.id.toInt(),
                                    page = supportPagingHelper.page,
                                    perPage = supportPagingHelper.pageSize,
                                    role =
                                        query.role?.let {
                                            co.anitrend.data.graphql.anilist.CharacterRole
                                                .valueOf(it.name)
                                        },
                                    sort =
                                        query.sort?.map {
                                            val baseName = (it.sortable as Enum<*>).name
                                            val enumName = if (it.order == SortOrder.DESC) baseName + "_DESC" else baseName
                                            co.anitrend.data.graphql.anilist.CharacterSort
                                                .valueOf(enumName)
                                        },
                                ),
                        ),
                    )
                }

            controller(deferred, requestCallback) {
                supportPagingHelper.from(it.media?.characters)
                it
            }?.let {
                cachePolicy.updateLastRequest(cacheIdentity, Instant.now())
            }
        }

        override suspend fun clearDataSource(context: kotlinx.coroutines.CoroutineDispatcher) {
            localSource.clearMediaCharactersByMediaId(query.id)
            cachePolicy.invalidateLastRequest(cacheIdentity)
        }

        override suspend fun load(
            loadType: LoadType,
            state: PagingState<Int, MediaCharacterConnectionEntity>,
        ): MediatorResult =
            when (loadType) {
                LoadType.REFRESH -> {
                    supportPagingHelper.onPageRefresh()
                    awaitResult(Request.Type.INITIAL, ::refreshCharacters)
                }

                LoadType.PREPEND -> MediatorResult.Success(true)

                LoadType.APPEND -> awaitResult(Request.Type.AFTER, ::refreshCharacters)
            }
    }

    class Staff(
        cacheIdentity: CacheIdentity,
        cachePolicy: ICacheStorePolicy,
        private val query: MediaParam.Staff,
        private val remoteSource: MediaRemoteSource,
        localSource: MediaLocalSource,
        private val controller: MediaStaffController,
        private val mapper: MediaPeopleMapper.Staff,
        dispatcher: ISupportDispatcher,
    ) : MediaPeopleRemoteMediator<MediaStaffConnectionEntity>(
            cacheIdentity = cacheIdentity,
            cachePolicy = cachePolicy,
            localSource = localSource,
            dispatcher = dispatcher,
        ) {
        override suspend fun initialize(): InitializeAction {
            var itemCount = localSource.mediaStaffCount(query.id)
            val maxSortIndex = localSource.mediaStaffMaxSortIndex(query.id)

            if (isCorruptPagingCache(itemCount, maxSortIndex)) {
                localSource.clearMediaStaffByMediaId(query.id)
                cachePolicy.invalidateLastRequest(cacheIdentity)
                itemCount = 0
            } else {
                supportPagingHelper.seedFromLocalCount(itemCount)
            }

            return if (shouldRefresh(itemCount > 0)) {
                InitializeAction.LAUNCH_INITIAL_REFRESH
            } else {
                InitializeAction.SKIP_INITIAL_REFRESH
            }
        }

        private suspend fun refreshStaff(requestCallback: RequestCallback) {
            mapper.onRequest(
                mediaId = query.id,
                page = supportPagingHelper.page,
            )

            val deferred =
                deferred {
                    remoteSource.getMediaStaff(
                        GraphQLRequest(
                            query = GetMediaWithStaff.document,
                            operationName = GetMediaWithStaff.name,
                            variables =
                                GetMediaWithStaffVariables(
                                    id = query.id.toInt(),
                                    page = supportPagingHelper.page,
                                    perPage = supportPagingHelper.pageSize,
                                    sort =
                                        query.sort?.map {
                                            val baseName = (it.sortable as Enum<*>).name
                                            val enumName = if (it.order == SortOrder.DESC) baseName + "_DESC" else baseName
                                            co.anitrend.data.graphql.anilist.StaffSort
                                                .valueOf(enumName)
                                        },
                                ),
                        ),
                    )
                }

            controller(deferred, requestCallback) {
                supportPagingHelper.from(it.media?.staff)
                it
            }?.let {
                cachePolicy.updateLastRequest(cacheIdentity, Instant.now())
            }
        }

        override suspend fun clearDataSource(context: kotlinx.coroutines.CoroutineDispatcher) {
            localSource.clearMediaStaffByMediaId(query.id)
            cachePolicy.invalidateLastRequest(cacheIdentity)
        }

        override suspend fun load(
            loadType: LoadType,
            state: PagingState<Int, MediaStaffConnectionEntity>,
        ): MediatorResult =
            when (loadType) {
                LoadType.REFRESH -> {
                    supportPagingHelper.onPageRefresh()
                    awaitResult(Request.Type.INITIAL, ::refreshStaff)
                }

                LoadType.PREPEND -> MediatorResult.Success(true)

                LoadType.APPEND -> awaitResult(Request.Type.AFTER, ::refreshStaff)
            }
    }
}
