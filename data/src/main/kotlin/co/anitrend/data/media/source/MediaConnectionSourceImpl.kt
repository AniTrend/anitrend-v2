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

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import co.anitrend.arch.extension.dispatchers.contract.ISupportDispatcher
import co.anitrend.arch.request.callback.RequestCallback
import co.anitrend.data.android.cache.repository.contract.ICacheStorePolicy
import co.anitrend.data.android.cleaner.contract.IClearDataHelper
import co.anitrend.data.android.extensions.deferred
import co.anitrend.data.graphql.anilist.GetMediaWithRelation
import co.anitrend.data.graphql.anilist.GetMediaWithSuggestion
import co.anitrend.data.media.MediaRecommendationsController
import co.anitrend.data.media.MediaRelationsController
import co.anitrend.data.media.converter.MediaRelationConnectionEntityConverter
import co.anitrend.data.media.datasource.local.connection.MediaRelationConnectionLocalSource
import co.anitrend.data.media.datasource.remote.MediaRemoteSource
import co.anitrend.data.media.mapper.MediaRelationMapper
import co.anitrend.data.media.source.contract.MediaConnectionSource
import co.anitrend.data.recommendation.converter.MediaRecommendationConnectionEntityConverter
import co.anitrend.data.recommendation.datasource.local.connection.MediaRecommendationConnectionLocalSource
import co.anitrend.data.recommendation.mapper.MediaRecommendationMapper
import co.anitrend.domain.common.sort.order.SortOrder
import co.anitrend.domain.media.entity.MediaRecommendationEntry
import co.anitrend.domain.media.entity.MediaRelationEntry
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

internal sealed class MediaConnectionSourceImpl {
    class Relations(
        private val remoteSource: MediaRemoteSource,
        private val localSource: MediaRelationConnectionLocalSource,
        private val controller: MediaRelationsController,
        private val mapper: MediaRelationMapper,
        private val converter: MediaRelationConnectionEntityConverter,
        private val clearDataHelper: IClearDataHelper,
        override val cachePolicy: ICacheStorePolicy,
        override val dispatcher: ISupportDispatcher,
    ) : MediaConnectionSource.Relations() {
        override fun observable(): Flow<List<MediaRelationEntry>> =
            localSource
                .entriesByMediaIdFlow(query.param.id)
                .flowOn(dispatcher.io)
                .map { entries ->
                    entries.map(converter::convertFrom)
                }.distinctUntilChanged()
                .flowOn(dispatcher.computation)

        override suspend fun refreshRelations(requestCallback: RequestCallback): Boolean {
            val deferred =
                deferred {
                    remoteSource.getMediaRelations(
                        GetMediaWithRelation.request(
                            id = query.param.id.toInt(),
                            scoreFormat =
                                co.anitrend.data.graphql.anilist.ScoreFormat
                                    .valueOf(query.param.scoreFormat.name),
                        ),
                    )
                }

            val result = controller(deferred, requestCallback)
            return result != null
        }

        override suspend fun clearDataSource(context: CoroutineDispatcher) {
            clearDataHelper(context) {
                cachePolicy.invalidateLastRequest(cacheIdentity)
                localSource.clearByMediaId(cacheIdentity.id)
            }
        }
    }

    class Recommendations(
        private val remoteSource: MediaRemoteSource,
        private val localSource: MediaRecommendationConnectionLocalSource,
        private val controller: MediaRecommendationsController,
        private val mapper: MediaRecommendationMapper,
        private val converter: MediaRecommendationConnectionEntityConverter,
        private val clearDataHelper: IClearDataHelper,
        override val cachePolicy: ICacheStorePolicy,
        override val dispatcher: ISupportDispatcher,
    ) : MediaConnectionSource.Recommendations() {
        override fun observable(): Flow<List<MediaRecommendationEntry>> =
            localSource
                .entriesByMediaIdFlow(query.param.id)
                .flowOn(dispatcher.io)
                .map { entries ->
                    entries.map(converter::convertFrom)
                }.distinctUntilChanged()
                .flowOn(dispatcher.computation)

        override suspend fun refreshRecommendations(requestCallback: RequestCallback): Boolean {
            val deferred =
                deferred {
                    remoteSource.getMediaRecommendations(
                        GetMediaWithSuggestion.request(
                            page = 1,
                            perPage = 25,
                            id = query.param.id.toInt(),
                            scoreFormat =
                                co.anitrend.data.graphql.anilist.ScoreFormat
                                    .valueOf(query.param.scoreFormat.name),
                            sort =
                                query.param.sort?.map {
                                    val baseName = it.name
                                    co.anitrend.data.graphql.anilist.RecommendationSort
                                        .valueOf(baseName)
                                },
                        ),
                    )
                }

            val result = controller(deferred, requestCallback)
            return result != null
        }

        override suspend fun clearDataSource(context: CoroutineDispatcher) {
            clearDataHelper(context) {
                cachePolicy.invalidateLastRequest(cacheIdentity)
                localSource.clearByMediaId(cacheIdentity.id)
            }
        }
    }

    class RecommendationsPaged(
        private val remoteSource: MediaRemoteSource,
        private val localSource: MediaRecommendationConnectionLocalSource,
        private val controller: MediaRecommendationsController,
        private val mapper: MediaRecommendationMapper,
        private val converter: MediaRecommendationConnectionEntityConverter,
        override val cachePolicy: ICacheStorePolicy,
        override val dispatcher: ISupportDispatcher,
    ) : MediaConnectionSource.RecommendationsPaged() {
        override fun observable(): Flow<PagingData<MediaRecommendationEntry>> =
            Pager(
                config =
                    PagingConfig(
                        pageSize = query.param.perPage,
                        initialLoadSize = query.param.perPage,
                        prefetchDistance = query.param.perPage,
                        enablePlaceholders = false,
                    ),
                remoteMediator =
                    MediaRecommendationsRemoteMediator(
                        cacheIdentity = cacheIdentity,
                        cachePolicy = cachePolicy,
                        query = query,
                        remoteSource = remoteSource,
                        localSource = localSource,
                        controller = controller,
                        mapper = mapper,
                        dispatcher = dispatcher,
                    ),
                pagingSourceFactory = { localSource.entriesByMediaIdPagingSource(query.param.id) },
            ).flow.map { pagingData ->
                pagingData.map(converter::convertFrom)
            }

        override suspend fun clearDataSource(context: CoroutineDispatcher) {
            localSource.clearByMediaId(query.param.id)
            cachePolicy.invalidateLastRequest(cacheIdentity)
        }
    }
}
