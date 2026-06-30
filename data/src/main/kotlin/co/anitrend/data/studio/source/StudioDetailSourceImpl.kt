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
package co.anitrend.data.studio.source

import co.anitrend.arch.extension.dispatchers.contract.ISupportDispatcher
import co.anitrend.arch.request.callback.RequestCallback
import co.anitrend.data.android.cache.repository.contract.ICacheStorePolicy
import co.anitrend.data.android.cleaner.contract.IClearDataHelper
import co.anitrend.data.android.extensions.deferred
import co.anitrend.data.edge.network.datasource.EdgeNetworkLocalSource
import co.anitrend.data.graphql.anilist.GetStudioDetail
import co.anitrend.data.graphql.anilist.GetStudioDetailVariables
import co.anitrend.data.studio.MediaStudioDetailController
import co.anitrend.data.studio.converter.MediaStudioEntryEnricher
import co.anitrend.data.studio.converter.StudioEntityConverter
import co.anitrend.data.studio.datasource.local.StudioLocalSource
import co.anitrend.data.studio.datasource.local.connection.MediaStudioConnectionLocalSource
import co.anitrend.data.studio.datasource.remote.StudioRemoteSource
import co.anitrend.data.studio.source.contract.StudioDetailSource
import co.anitrend.domain.common.entity.shared.CoverImage
import co.anitrend.domain.media.entity.MediaStudioEntry
import co.anitrend.domain.media.enums.MediaFormat
import co.anitrend.domain.medialist.enums.ScoreFormat
import co.anitrend.domain.studio.entity.StudioDetailData
import co.anitrend.retrofit.graphql.model.GraphQLRequest
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

internal class StudioDetailSourceImpl(
    private val remoteSource: StudioRemoteSource,
    private val localSource: StudioLocalSource,
    private val connectionLocalSource: MediaStudioConnectionLocalSource,
    private val edgeNetworkLocalSource: EdgeNetworkLocalSource,
    private val controller: MediaStudioDetailController,
    private val clearDataHelper: IClearDataHelper,
    private val entityConverter: StudioEntityConverter,
    private val enricher: MediaStudioEntryEnricher,
    override val cachePolicy: ICacheStorePolicy,
    override val dispatcher: ISupportDispatcher,
) : StudioDetailSource() {
    override fun observable(): Flow<StudioDetailData> =
        combine(
            localSource.studioByIdFlow(param.id).filterNotNull(),
            connectionLocalSource.entriesByStudioIdFlow(param.id),
        ) { studioEntity, connections ->
            studioEntity to connections
        }.flatMapLatest { (studioEntity, connections) ->
            val mediaIds = connections.map { it.mediaId.toString() }.distinct()
            val networksFlow =
                if (mediaIds.isEmpty()) {
                    flowOf(emptyList())
                } else {
                    edgeNetworkLocalSource.byMediaIdsFlow(mediaIds)
                }

            networksFlow.map { networks ->
                val studio = entityConverter.convertFrom(studioEntity)

                val mediaEntries =
                    connections.map { connection ->
                        MediaStudioEntry(
                            studio = studio,
                            mediaTitle = connection.mediaTitle.orEmpty(),
                            mediaCoverImage =
                                if (connection.mediaCoverImageLarge != null || connection.mediaCoverImageMedium != null) {
                                    CoverImage(
                                        large = connection.mediaCoverImageLarge,
                                        medium = connection.mediaCoverImageMedium,
                                    )
                                } else {
                                    null
                                },
                            mediaFormat = connection.mediaFormat?.let { runCatching { MediaFormat.valueOf(it) }.getOrNull() },
                            mediaStartYear = connection.mediaStartYear,
                            mediaAverageScore = connection.mediaAverageScore,
                            isMain = connection.isMain,
                            id = connection.entryId,
                        )
                    }

                val enriched = enricher.enrich(mediaEntries, networks)
                val networkLogoPath = enriched.firstNotNullOfOrNull { it.networkMatch?.logoPath }

                StudioDetailData(
                    studio = studio,
                    mediaEntries = enriched,
                    networkLogo = networkLogoPath?.let { CoverImage(large = it, medium = it) },
                    id = studio.id,
                )
            }
        }.flowOn(dispatcher.io)

    override suspend fun getStudio(callback: RequestCallback): Boolean {
        val deferred =
            deferred {
                remoteSource.getStudioDetail(
                    GraphQLRequest(
                        query = GetStudioDetail.document,
                        operationName = GetStudioDetail.name,
                        variables =
                            GetStudioDetailVariables(
                                id = param.id.toInt(),
                                scoreFormat =
                                    co.anitrend.data.graphql.anilist.ScoreFormat
                                        .valueOf(ScoreFormat.POINT_100.name),
                            ),
                    ),
                )
            }
        val result = controller(deferred, callback)

        return result != null
    }

    override suspend fun clearDataSource(context: CoroutineDispatcher) {
        clearDataHelper(context) {
            cachePolicy.invalidateLastRequest(cacheIdentity)
            localSource.clearById(param.id)
            connectionLocalSource.clearByStudioId(param.id)
        }
    }
}
