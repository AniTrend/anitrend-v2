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
package co.anitrend.data.recommendation.mapper

import co.anitrend.data.android.mapper.DefaultMapper
import co.anitrend.data.media.converter.MediaConverter
import co.anitrend.data.media.converter.toConnectionPreviewEntity
import co.anitrend.data.media.model.container.MediaConnectionModelContainer
import co.anitrend.data.recommendation.datasource.local.connection.MediaRecommendationConnectionLocalSource
import co.anitrend.data.recommendation.entity.connection.MediaRecommendationConnectionEntity

internal class MediaRecommendationMapper(
    private val localSource: MediaRecommendationConnectionLocalSource,
    private val converter: MediaConverter,
) : DefaultMapper<MediaConnectionModelContainer.Recommendations, List<MediaRecommendationConnectionEntity>>() {
    private var mediaId: Long = 0L
    private var sortIndexOffset: Int = 0

    suspend fun onRequest(
        mediaId: Long,
        page: Int,
    ) {
        this.mediaId = mediaId
        sortIndexOffset =
            if (page <= 1) {
                0
            } else {
                localSource.countByMediaId(mediaId)
            }
    }

    override suspend fun persist(data: List<MediaRecommendationConnectionEntity>) {
        if (sortIndexOffset == 0) {
            localSource.clearByMediaId(mediaId)
        }

        if (data.isEmpty()) {
            return
        }

        localSource.upsertConnections(data)
    }

    override suspend fun onResponseMapFrom(source: MediaConnectionModelContainer.Recommendations): List<MediaRecommendationConnectionEntity> {
        val mediaId = source.media?.id ?: return emptyList()

        return source.media.recommendations?.edges.orEmpty().mapIndexedNotNull { index, edge ->
            val recommendation = edge.node ?: return@mapIndexedNotNull null
            val media = recommendation.mediaRecommendation ?: return@mapIndexedNotNull null

            MediaRecommendationConnectionEntity(
                mediaId = mediaId,
                entryId = recommendation.id,
                rating = recommendation.rating,
                userName = recommendation.user?.name,
                userRating = recommendation.userRating?.name,
                sortIndex = sortIndexOffset + index,
                target = converter.convertFrom(media).toConnectionPreviewEntity(),
            )
        }
    }
}
