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

import co.anitrend.arch.extension.dispatchers.contract.ISupportDispatcher
import co.anitrend.arch.request.callback.RequestCallback
import co.anitrend.data.android.cache.repository.contract.ICacheStorePolicy
import co.anitrend.data.android.cleaner.contract.IClearDataHelper
import co.anitrend.data.android.extensions.deferred
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
import co.anitrend.data.util.GraphUtil.toQueryContainerBuilder
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
                    val queryBuilder = query.toQueryContainerBuilder()
                    remoteSource.getMediaRelations(queryBuilder)
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
                    val queryBuilder = query.toQueryContainerBuilder()
                    remoteSource.getMediaRecommendations(queryBuilder)
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
}
