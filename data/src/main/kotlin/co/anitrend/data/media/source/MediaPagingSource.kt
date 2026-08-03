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
import co.anitrend.data.carousel.source.contract.CarouselSource
import co.anitrend.data.common.extension.from
import co.anitrend.data.graphql.anilist.GetMediaPaged
import co.anitrend.data.graphql.anilist.GetMediaPagedVariables
import co.anitrend.data.media.MediaPagedController
import co.anitrend.data.media.datasource.local.MediaLocalSource
import co.anitrend.data.media.datasource.remote.MediaRemoteSource
import co.anitrend.data.media.entity.filter.MediaQueryFilter
import co.anitrend.data.media.entity.view.MediaEntityView
import co.anitrend.domain.media.model.MediaParam
import co.anitrend.domain.common.sort.order.SortOrder
import co.anitrend.retrofit.graphql.model.request.GraphQLOperationRequest
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first

internal class MediaPagingSource(
    private val cacheIdentity: CacheIdentity,
    private val remoteSource: MediaRemoteSource,
    private val localSource: MediaLocalSource,
    private val carouselSource: CarouselSource,
    private val controller: MediaPagedController,
    private val clearDataHelper: IClearDataHelper,
    private val filter: MediaQueryFilter.Paged,
    private val query: MediaParam.Find,
    override val dispatcher: ISupportDispatcher,
) : AbstractPagingMediator<Int, MediaEntityView.Core>() {
    fun pagingSourceFactory(): () -> PagingSource<Int, MediaEntityView.Core> =
        {
            localSource.rawPagingSource(filter.build(query))
        }

    private suspend fun getMedia(requestCallback: RequestCallback) {
        val deferred =
            deferred {
                remoteSource.getMediaPaged(
                    GraphQLOperationRequest(
                        query = GetMediaPaged.document,
                        operationName = GetMediaPaged.name,
                        variables =
                            GetMediaPagedVariables(
                                page = supportPagingHelper.page,
                                perPage = supportPagingHelper.pageSize,
                                id = query.id?.toInt(),
                                idMal = query.idMal?.toInt(),
                                idMal_in = query.idMal_in?.map { it.toInt() },
                                idMal_not = query.idMal_not?.toInt(),
                                idMal_not_in = query.idMal_not_in?.map { it.toInt() },
                                id_in = query.id_in?.map { it.toInt() },
                                id_not = query.id_not?.toInt(),
                                id_not_in = query.id_not_in?.map { it.toInt() },
                                isAdult = query.isAdult,
                                type =
                                    query.type?.let {
                                        co.anitrend.data.graphql.anilist.MediaType
                                            .valueOf(it.name)
                                    },
                                format =
                                    query.format?.let {
                                        co.anitrend.data.graphql.anilist.MediaFormat
                                            .valueOf(it.name)
                                    },
                                format_in =
                                    query.format_in?.map {
                                        co.anitrend.data.graphql.anilist.MediaFormat
                                            .valueOf(it.name)
                                    },
                                format_not =
                                    query.format_not?.let {
                                        co.anitrend.data.graphql.anilist.MediaFormat
                                            .valueOf(it.name)
                                    },
                                format_not_in =
                                    query.format_not_in?.map {
                                        co.anitrend.data.graphql.anilist.MediaFormat
                                            .valueOf(it.name)
                                    },
                                status =
                                    query.status?.let {
                                        co.anitrend.data.graphql.anilist.MediaStatus
                                            .valueOf(it.name)
                                    },
                                status_in =
                                    query.status_in?.map {
                                        co.anitrend.data.graphql.anilist.MediaStatus
                                            .valueOf(it.name)
                                    },
                                status_not =
                                    query.status_not?.let {
                                        co.anitrend.data.graphql.anilist.MediaStatus
                                            .valueOf(it.name)
                                    },
                                status_not_in =
                                    query.status_not_in?.map {
                                        co.anitrend.data.graphql.anilist.MediaStatus
                                            .valueOf(it.name)
                                    },
                                season =
                                    query.season?.let {
                                        co.anitrend.data.graphql.anilist.MediaSeason
                                            .valueOf(it.name)
                                    },
                                seasonYear = query.seasonYear,
                                search = query.search,
                                onList = query.onList,
                                averageScore = query.averageScore,
                                averageScore_greater = query.averageScore_greater,
                                averageScore_lesser = query.averageScore_lesser,
                                averageScore_not = query.averageScore_not,
                                popularity = query.popularity,
                                popularity_greater = query.popularity_greater,
                                popularity_lesser = query.popularity_lesser,
                                popularity_not = query.popularity_not,
                                chapters = query.chapters,
                                chapters_greater = query.chapters_greater,
                                chapters_lesser = query.chapters_lesser,
                                duration = query.duration,
                                duration_greater = query.duration_greater,
                                duration_lesser = query.duration_lesser,
                                episodes = query.episodes,
                                episodes_greater = query.episodes_greater,
                                episodes_lesser = query.episodes_lesser,
                                volumes = query.volumes,
                                volumes_greater = query.volumes_greater,
                                volumes_lesser = query.volumes_lesser,
                                source =
                                    query.source?.let {
                                        co.anitrend.data.graphql.anilist.MediaSource
                                            .valueOf(it.name)
                                    },
                                source_in =
                                    query.source_in?.map {
                                        co.anitrend.data.graphql.anilist.MediaSource
                                            .valueOf(it.name)
                                    },
                                startDate =
                                    query.startDate
                                        ?.toString()
                                        ?.toIntOrNull(),
                                startDate_greater =
                                    query.startDate_greater
                                        ?.toString()
                                        ?.toIntOrNull(),
                                startDate_lesser =
                                    query.startDate_lesser
                                        ?.toString()
                                        ?.toIntOrNull(),
                                startDate_like = query.startDate_like?.toString(),
                                endDate =
                                    query.endDate
                                        ?.toString()
                                        ?.toIntOrNull(),
                                endDate_greater =
                                    query.endDate_greater
                                        ?.toString()
                                        ?.toIntOrNull(),
                                endDate_lesser =
                                    query.endDate_lesser
                                        ?.toString()
                                        ?.toIntOrNull(),
                                endDate_like = query.endDate_like?.toString(),
                                countryOfOrigin = query.countryOfOrigin?.toString(),
                                genre = query.genre,
                                genre_in = query.genre_in,
                                genre_not_in = query.genre_not_in,
                                tag = query.tag,
                                tag_in = query.tag_in,
                                tag_not_in = query.tag_not_in,
                                tagCategory = query.tagCategory,
                                tagCategory_in = query.tagCategory_in,
                                tagCategory_not_in = query.tagCategory_not_in,
                                minimumTagRank = query.minimumTagRank,
                                licensedBy =
                                    query.licensedBy
                                        ?.alias
                                        ?.toString(),
                                licensedById = query.licensedById,
                                licensedById_in = query.licensedById_in,
                                licensedBy_in = query.licensedBy_in?.map { it.alias.toString() },
                                sort =
                                    query.sort?.map {
                                        val baseName = (it.sortable as Enum<*>).name
                                        val enumName =
                                            if (it.order == SortOrder.DESC && baseName != "SEARCH_MATCH" && baseName != "RELEVANCE") {
                                                baseName + "_DESC"
                                            } else {
                                                baseName
                                            }
                                        co.anitrend.data.graphql.anilist.MediaSort
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
            block = ::getMedia,
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
            block = ::getMedia,
        )
        return InitializeAction.SKIP_INITIAL_REFRESH
    }

    override suspend fun clearDataSource(context: CoroutineDispatcher) {
        carouselSource.clearDataSource(context)
        clearDataHelper(context) {
            localSource.clear()
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
