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
package co.anitrend.data.media.mapper

import co.anitrend.data.android.mapper.DefaultMapper
import co.anitrend.data.media.converter.MediaConverter
import co.anitrend.data.media.model.container.MediaConnectionModelContainer
import co.anitrend.domain.media.entity.MediaRecommendationEntry
import co.anitrend.domain.media.entity.MediaRelationEntry

internal sealed class MediaConnectionMapper<S, D> : DefaultMapper<S, D>() {
    class Relations(
        private val converter: MediaConverter,
    ) : MediaConnectionMapper<MediaConnectionModelContainer.Relations, List<MediaRelationEntry>>() {
        override suspend fun persist(data: List<MediaRelationEntry>) {
        }

        override suspend fun onResponseMapFrom(source: MediaConnectionModelContainer.Relations) =
            source
                .media
                ?.relations
                ?.edges
                .orEmpty()
                .mapNotNull { edge ->
                    edge.node?.let { media ->
                        MediaRelationEntry(
                            relation = edge.mediaRelation,
                            media = converter.convertFrom(media),
                            id = edge.id,
                        )
                    }
                }
    }

    class Recommendations(
        private val converter: MediaConverter,
    ) : MediaConnectionMapper<MediaConnectionModelContainer.Recommendations, List<MediaRecommendationEntry>>() {
        override suspend fun persist(data: List<MediaRecommendationEntry>) {
        }

        override suspend fun onResponseMapFrom(source: MediaConnectionModelContainer.Recommendations) =
            source
                .media
                ?.recommendations
                ?.edges
                .orEmpty()
                .mapNotNull { edge ->
                    val recommendation = edge.node ?: return@mapNotNull null
                    val media = recommendation.mediaRecommendation ?: return@mapNotNull null

                    MediaRecommendationEntry(
                        media = converter.convertFrom(media),
                        rating = recommendation.rating,
                        userName = recommendation.user?.name,
                        userRating = recommendation.userRating,
                        id = recommendation.id,
                    )
                }
    }
}
