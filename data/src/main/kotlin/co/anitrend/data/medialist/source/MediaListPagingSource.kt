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
package co.anitrend.data.medialist.source

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
import co.anitrend.data.media.datasource.local.MediaLocalSource
import co.anitrend.data.media.entity.view.MediaEntityView
import co.anitrend.data.medialist.MediaListPagedController
import co.anitrend.data.medialist.datasource.local.MediaListLocalSource
import co.anitrend.data.medialist.datasource.remote.MediaListRemoteSource
import co.anitrend.data.medialist.entity.filter.MediaListQueryFilter
import co.anitrend.data.graphql.anilist.GetMediaListPaged
import co.anitrend.data.medialist.model.query.MediaListQuery
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first

internal class MediaListPagingSource(
    private val cacheIdentity: CacheIdentity,
    private val remoteSource: MediaListRemoteSource,
    private val localSource: MediaListLocalSource,
    private val mediaLocalSource: MediaLocalSource,
    private val controller: MediaListPagedController,
    private val filter: MediaListQueryFilter.Paged,
    private val clearDataHelper: IClearDataHelper,
    private val query: MediaListQuery.Paged,
    override val dispatcher: ISupportDispatcher,
) : AbstractPagingMediator<Int, MediaEntityView.Core>() {
    fun pagingSourceFactory(): () -> PagingSource<Int, MediaEntityView.Core> =
        {
            mediaLocalSource.rawPagingSource(filter.build(query.param))
        }

    private suspend fun getMediaList(requestCallback: RequestCallback) {
        val deferred =
            deferred {
                remoteSource.getMediaListPaged(
                    GetMediaListPaged.request(
                        compareWithAuthList = query.param.compareWithAuthList,
                        completedAt =
                            query.param.completedAt
                                ?.toString()
                                ?.toIntOrNull(),
                        completedAt_greater =
                            query.param.completedAt_greater
                                ?.toString()
                                ?.toIntOrNull(),
                        completedAt_lesser =
                            query.param.completedAt_lesser
                                ?.toString()
                                ?.toIntOrNull(),
                        completedAt_like = query.param.completedAt_like?.toString(),
                        id = query.param.id?.toInt(),
                        isFollowing = query.param.isFollowing,
                        mediaId = query.param.mediaId?.toInt(),
                        notes = query.param.notes,
                        notes_like = query.param.notes_like,
                        sort =
                            query.param.sort?.map {
                                val baseName = it.sortable.name
                                val enumName = if (it.order == co.anitrend.domain.common.sort.order.SortOrder.DESC) baseName + "_DESC" else baseName
                                co.anitrend.data.graphql.anilist.MediaListSort
                                    .valueOf(enumName)
                            },
                        startedAt =
                            query.param.startedAt
                                ?.toString()
                                ?.toIntOrNull(),
                        startedAt_greater =
                            query.param.startedAt_greater
                                ?.toString()
                                ?.toIntOrNull(),
                        startedAt_lesser =
                            query.param.startedAt_lesser
                                ?.toString()
                                ?.toIntOrNull(),
                        startedAt_like = query.param.startedAt_like?.toString(),
                        status =
                            query.param.status?.let {
                                co.anitrend.data.graphql.anilist.MediaListStatus
                                    .valueOf(it.name)
                            },
                        status_in =
                            query.param.status_in?.map {
                                co.anitrend.data.graphql.anilist.MediaListStatus
                                    .valueOf(it.name)
                            },
                        status_not =
                            query.param.status_not?.let {
                                co.anitrend.data.graphql.anilist.MediaListStatus
                                    .valueOf(it.name)
                            },
                        status_not_in =
                            query.param.status_not_in?.map {
                                co.anitrend.data.graphql.anilist.MediaListStatus
                                    .valueOf(it.name)
                            },
                        type =
                            query.param.type?.let {
                                co.anitrend.data.graphql.anilist.MediaType
                                    .valueOf(it.name)
                            },
                        userId = query.param.userId?.toInt(),
                        userName = query.param.userName,
                        userId_in = query.param.userId_in?.map { it.toInt() },
                        scoreFormat =
                            co.anitrend.data.graphql.anilist.ScoreFormat
                                .valueOf(query.param.scoreFormat.name),
                        page = supportPagingHelper.page,
                        perPage = supportPagingHelper.pageSize,
                    ),
                )
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
            block = ::getMediaList,
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
            block = ::getMediaList,
        )
        return InitializeAction.SKIP_INITIAL_REFRESH
    }

    override suspend fun clearDataSource(context: CoroutineDispatcher) {
        clearDataHelper(context) {
            if (query.param.userId != null) {
                localSource.clearByUserId(
                    requireNotNull(query.param.userId),
                )
            } else if (query.param.userName != null) {
                localSource.clearByUserName(
                    requireNotNull(query.param.userName),
                )
            }
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
