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
package co.anitrend.data.media.converter

import co.anitrend.arch.data.converter.SupportConverter
import co.anitrend.arch.data.transformer.ISupportTransformer
import co.anitrend.data.common.extension.toFuzzyDateInt
import co.anitrend.data.media.entity.MediaEntity
import co.anitrend.data.media.model.MediaModel

internal class MediaModelConverter(
    override val fromType: (MediaModel) -> MediaEntity = ::transform,
    override val toType: (MediaEntity) -> MediaModel = { throw NotImplementedError() },
) : SupportConverter<MediaModel, MediaEntity>() {
    private companion object : ISupportTransformer<MediaModel, MediaEntity> {
        override fun transform(source: MediaModel) =
            MediaEntity(
                coverImage =
                    MediaEntity.CoverImage(
                        color = source.coverImage?.color,
                        extraLarge = source.coverImage?.extraLarge,
                        large = source.coverImage?.large,
                        medium = source.coverImage?.medium,
                        banner = source.bannerImage,
                    ),
                title =
                    MediaEntity.Title(
                        romaji = source.title?.romaji,
                        english = source.title?.english,
                        original = source.title?.native,
                        userPreferred = source.title?.userPreferred,
                    ),
                trailer =
                    source.trailer?.let {
                        MediaEntity.Trailer(
                            id = it.id.orEmpty(),
                            site = it.site,
                            thumbnail = it.thumbnail,
                        )
                    },
                nextAiringId = source.nextAiringEpisode?.id,
                averageScore = source.averageScore,
                chapters = source.chapters,
                countryOfOrigin = source.countryOfOrigin,
                description = source.description,
                duration = source.duration,
                endDate = source.endDate?.toFuzzyDateInt(),
                episodes = source.episodes,
                favourites = source.favourites,
                format = source.format,
                hashTag = source.hashTag,
                isAdult = source.isAdult,
                isFavourite = source.isFavourite,
                isFavouriteBlocked = source.isFavouriteBlocked,
                isLicensed = source.isLicensed,
                isRecommendationBlocked = source.isRecommendationBlocked,
                isReviewBlocked = source.isReviewBlocked,
                isLocked = source.isLocked,
                meanScore = source.meanScore,
                popularity = source.popularity,
                season = source.season,
                siteUrl = source.siteUrl,
                source = source.source,
                startDate = source.startDate?.toFuzzyDateInt(),
                status = source.status,
                synonyms = source.synonyms,
                trending = source.trending,
                type = source.type,
                updatedAt = source.updatedAt,
                volumes = source.volumes,
                malId = source.idMal,
                id = source.id,
            )
    }
}
