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
package co.anitrend.data.recommendation.converter

import co.anitrend.arch.data.converter.SupportConverter
import co.anitrend.arch.data.transformer.ISupportTransformer
import co.anitrend.data.media.converter.toMedia
import co.anitrend.data.recommendation.entity.connection.MediaRecommendationConnectionEntity
import co.anitrend.domain.media.entity.MediaRecommendationEntry
import co.anitrend.domain.recommendation.enums.RecommendationRating

private fun String?.toRecommendationRating(): RecommendationRating? =
    this?.let { value ->
        runCatching { RecommendationRating.valueOf(value) }.getOrNull()
    }

internal class MediaRecommendationConnectionEntityConverter(
    override val fromType: (MediaRecommendationConnectionEntity) -> MediaRecommendationEntry = ::transform,
    override val toType: (MediaRecommendationEntry) -> MediaRecommendationConnectionEntity = { throw NotImplementedError() },
) : SupportConverter<MediaRecommendationConnectionEntity, MediaRecommendationEntry>() {
    private companion object : ISupportTransformer<MediaRecommendationConnectionEntity, MediaRecommendationEntry> {
        override fun transform(source: MediaRecommendationConnectionEntity) =
            MediaRecommendationEntry(
                media = source.target.toMedia(),
                rating = source.rating,
                userName = source.userName,
                userRating = source.userRating.toRecommendationRating(),
                id = source.entryId,
            )
    }
}
