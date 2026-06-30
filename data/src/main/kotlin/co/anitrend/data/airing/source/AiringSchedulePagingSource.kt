/*
 * Copyright (C) 2025 AniTrend
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
package co.anitrend.data.airing.source

import androidx.paging.LoadType
import androidx.paging.LoadType.*
import androidx.paging.PagingSource
import androidx.paging.PagingState
import co.anitrend.arch.domain.entities.LoadState
import co.anitrend.arch.extension.dispatchers.contract.ISupportDispatcher
import co.anitrend.arch.request.callback.RequestCallback
import co.anitrend.arch.request.model.Request
import co.anitrend.data.airing.AiringSchedulePagedController
import co.anitrend.data.airing.datasource.local.AiringLocalSource
import co.anitrend.data.airing.datasource.remote.AiringRemoteSource
import co.anitrend.data.airing.entity.filter.AiringQueryFilter
import co.anitrend.data.airing.model.query.AiringScheduleQuery
import co.anitrend.data.android.cache.extensions.invoke
import co.anitrend.data.android.cache.model.CacheIdentity
import co.anitrend.data.android.cleaner.contract.IClearDataHelper
import co.anitrend.data.android.extensions.deferred
import co.anitrend.data.android.paging.AbstractPagingMediator
import co.anitrend.data.common.extension.from
import co.anitrend.data.graphql.anilist.GetAiringPaged
import co.anitrend.data.graphql.anilist.GetAiringPagedVariables
import co.anitrend.data.media.datasource.local.MediaLocalSource
import co.anitrend.data.media.entity.view.MediaEntityView
import co.anitrend.domain.common.sort.order.SortOrder
import co.anitrend.retrofit.graphql.model.GraphQLRequest
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first

internal class AiringSchedulePagingSource(
    private val cacheIdentity: CacheIdentity,
    private val remoteSource: AiringRemoteSource,
    private val localSource: AiringLocalSource,
    private val mediaLocalSource: MediaLocalSource,
    private val controller: AiringSchedulePagedController,
    private val clearDataHelper: IClearDataHelper,
    private val filter: AiringQueryFilter.Paged,
    private val query: AiringScheduleQuery,
    override val dispatcher: ISupportDispatcher,
) : AbstractPagingMediator<Int, MediaEntityView.Core>() {
    fun pagingSourceFactory(): () -> PagingSource<Int, MediaEntityView.Core> =
        {
            mediaLocalSource.rawPagingSource(filter.build(query.param))
        }

    private suspend fun getAiringSchedule(requestCallback: RequestCallback) {
        val deferred =
            deferred {
                remoteSource.getAiringPaged(
                    GraphQLRequest(
                        query = GetAiringPaged.document,
                        operationName = GetAiringPaged.name,
                        variables =
                            GetAiringPagedVariables(
                                page = supportPagingHelper.page,
                                perPage = supportPagingHelper.pageSize,
                                airingAt = query.param.airingAt,
                                airingAt_greater = query.param.airingAt_greater,
                                airingAt_lesser = query.param.airingAt_lesser,
                                episode = query.param.episode,
                                episode_greater = query.param.episode_greater,
                                episode_in = query.param.episode_in?.toList(),
                                episode_lesser = query.param.episode_lesser,
                                episode_not = query.param.episode_not,
                                episode_not_in = query.param.episode_not_in?.toList(),
                                id = query.param.id?.toInt(),
                                id_in = query.param.id_in?.map { it.toInt() },
                                id_not = query.param.id_not?.toInt(),
                                id_not_in = query.param.id_not_in?.map { it.toInt() },
                                mediaId = query.param.mediaId?.toInt(),
                                mediaId_in = query.param.mediaId_in?.map { it.toInt() },
                                mediaId_not = query.param.mediaId_not?.toInt(),
                                mediaId_not_in = query.param.mediaId_not_in?.map { it.toInt() },
                                notYetAired = query.param.notYetAired,
                                sort =
                                    query.param.sort?.map {
                                        val baseName = (it.sortable as Enum<*>).name
                                        val enumName = if (it.order == SortOrder.DESC) baseName + "_DESC" else baseName
                                        co.anitrend.data.graphql.anilist.AiringSort
                                            .valueOf(enumName)
                                    },
                            ),
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
            block = ::getAiringSchedule,
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
            block = ::getAiringSchedule,
        )
        return InitializeAction.SKIP_INITIAL_REFRESH
    }

    /**
     * Clears data sources (databases, preferences, e.t.c)
     *
     * @param context Dispatcher context to run in
     */
    override suspend fun clearDataSource(context: CoroutineDispatcher) {
        clearDataHelper(context = context, action = localSource::clear)
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MediaEntityView.Core>,
    ): MediatorResult {
        return when (loadType) {
            REFRESH -> {
                clearDataSource(dispatcher.io)
                return invoke(requestType = Request.Type.INITIAL)
            }
            PREPEND -> MediatorResult.Success(true)
            APPEND -> {
                return invoke(requestType = Request.Type.AFTER)
            }
        }
    }
}
