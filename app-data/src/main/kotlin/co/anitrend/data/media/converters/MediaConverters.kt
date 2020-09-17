/*
 * Copyright (C) 2020  AniTrend
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

package co.anitrend.data.media.converters

import co.anitrend.arch.data.converter.SupportConverter
import co.anitrend.arch.data.mapper.contract.ISupportMapperHelper
import co.anitrend.data.media.entity.MediaEntity
import co.anitrend.data.media.model.MediaModelExtended
import co.anitrend.domain.airing.entity.AiringSchedule
import co.anitrend.domain.common.entity.shared.FuzzyDate.Companion.orEmpty
import co.anitrend.domain.common.extension.toFuzzyDate
import co.anitrend.domain.common.extension.toFuzzyDateInt
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.media.entity.attribute.image.MediaImage
import co.anitrend.domain.media.entity.attribute.link.MediaExternalLink
import co.anitrend.domain.media.entity.attribute.origin.MediaSourceId
import co.anitrend.domain.media.entity.attribute.rank.MediaRank
import co.anitrend.domain.media.entity.attribute.title.MediaTitle
import co.anitrend.domain.media.entity.attribute.trailer.MediaTrailer
import co.anitrend.domain.media.entity.contract.MediaCategory
import co.anitrend.domain.media.enums.MediaType
import co.anitrend.domain.medialist.entity.MediaList
import co.anitrend.domain.tag.entity.Tag

internal class MediaEntityConverter(
    override val fromType: (MediaEntity) -> Media = { from().transform(it) },
    override val toType: (Media) -> MediaEntity = { throw NotImplementedError() }
) : SupportConverter<MediaEntity, Media>() {
    companion object {
        private fun from() =
            object : ISupportMapperHelper<MediaEntity, Media> {
                override fun transform(source: MediaEntity): Media {
                    return Media(
                        sourceId = MediaSourceId.empty().copy(
                            anilist = source.id
                        ),
                        origin = source.countryOfOrigin,
                        description = source.description,
                        externalLinks = emptyList(),//source.externalLinks
                        favouritesCount = source.id,
                        genres = emptyList(),//source.genres,
                        twitterTag = null,
                        isLicensed = source.isLicensed,
                        isLocked = source.isLocked,
                        isRecommendationBlocked = false,
                        rankings = emptyList(),//source.rankings
                        siteUrl = source.siteUrl,
                        source = source.source,
                        synonyms = source.synonyms,
                        tags = emptyList(),//source.id
                        trailer = null,//source.trailer
                        format = source.format,
                        season = source.season,
                        status = source.status,
                        scoreFormat = null,// TODO: use user object to retrieve score format
                        meanScore = source.meanScore ?: 0,
                        averageScore = source.averageScore ?: 0,
                        startDate = source.startDate.toFuzzyDate(),
                        endDate = source.endDate.toFuzzyDate(),
                        title = MediaTitle(
                            romaji = source.title.romaji,
                            english = source.title.english,
                            native = source.title.native,
                            userPreferred = source.title.userPreferred
                        ),
                        image = MediaImage(
                            color = source.coverImage.color,
                            extraLarge = source.coverImage.extraLarge,
                            large = source.coverImage.large,
                            medium = source.coverImage.medium,
                            banner = source.coverImage.banner,
                        ),
                        category = when (source.type) {
                            MediaType.ANIME -> MediaCategory.Anime(
                                source.episodes ?: 0,
                                source.duration ?: 0,
                                null//source.nextAiringEpisode
                            )
                            MediaType.MANGA -> MediaCategory.Manga(
                                source.chapters ?: 0,
                                source.volumes ?: 0
                            )
                        },
                        isAdult = source.isAdult,
                        isFavourite = source.isFavourite,
                        id = source.id,
                        mediaListEntry = MediaList.empty()
                    )
                }
            }
    }
}

internal class MediaModelConverter(
    override val fromType: (MediaModelExtended) -> MediaEntity = { from().transform(it) },
    override val toType: (MediaEntity) -> MediaModelExtended = { throw NotImplementedError() }
) : SupportConverter<MediaModelExtended, MediaEntity>() {
    companion object {
        private fun from() =
            object : ISupportMapperHelper<MediaModelExtended, MediaEntity> {
                override fun transform(source: MediaModelExtended): MediaEntity {
                    throw NotImplementedError()
                }
            }
    }
}

internal class MediaConverter(
    override val fromType: (MediaModelExtended) -> Media = { from().transform(it) },
    override val toType: (Media) -> MediaModelExtended = { throw NotImplementedError() }
) : SupportConverter<MediaModelExtended, Media>() {
    companion object {
        private fun from() =
            object : ISupportMapperHelper<MediaModelExtended, Media> {
                override fun transform(source: MediaModelExtended) = Media(
                    sourceId = MediaSourceId.empty().copy(
                        mal = source.idMal,
                        anilist = source.id
                    ),
                    origin = source.countryOfOrigin,
                    description = source.description,
                    externalLinks = source.externalLinks?.map {
                        MediaExternalLink(
                            site = it.site,
                            url = it.url,
                            id = it.id,
                        )
                    }.orEmpty(),
                    favouritesCount = source.id,
                    genres = source.genres.orEmpty(),
                    twitterTag = null,
                    isLicensed = source.isLicensed,
                    isLocked = source.isLocked,
                    isRecommendationBlocked = false,
                    rankings = source.rankings?.map {
                        MediaRank(
                            allTime = it.allTime,
                            context = it.context,
                            format = it.format,
                            rank = it.rank,
                            season = it.season,
                            type = it.type,
                            year = it.year,
                            id = it.id,
                        )
                    }.orEmpty(),
                    siteUrl = source.siteUrl,
                    source = source.source,
                    synonyms = source.synonyms.orEmpty(),
                    tags = source.tags?.map {
                        Tag(
                            id = it.id,
                            name = it.name,
                            description = it.description,
                            category = it.category,
                            rank = it.rank ?: 0,
                            isGeneralSpoiler = it.isGeneralSpoiler ?: false,
                            isMediaSpoiler = it.isMediaSpoiler ?: false,
                            isAdult = it.isAdult ?: false,
                        )
                    }.orEmpty(),
                    trailer = MediaTrailer(
                        source.trailer?.id,
                        source.trailer?.site,
                        source.trailer?.thumbnail
                    ),
                    format = source.format,
                    season = source.season,
                    status = source.status,
                    scoreFormat = null,// TODO: use user object to retrieve score format
                    meanScore = source.meanScore ?: 0,
                    averageScore = source.averageScore ?: 0,
                    startDate = source.startDate.orEmpty(),
                    endDate = source.endDate.orEmpty(),
                    title = MediaTitle(
                        romaji = source.title?.romaji,
                        english = source.title?.english,
                        native = source.title?.native,
                        userPreferred = source.title?.userPreferred
                    ),
                    image = MediaImage(
                        color = source.coverImage?.color,
                        extraLarge = source.coverImage?.extraLarge,
                        large = source.coverImage?.large,
                        medium = source.coverImage?.medium,
                        banner = source.bannerImage,
                    ),
                    category = when (source.type) {
                        MediaType.ANIME -> MediaCategory.Anime(
                            source.episodes ?: 0,
                            source.duration ?: 0,
                            source.nextAiringEpisode?.let {
                                AiringSchedule(
                                    airingAt = it.airingAt,
                                    episode = it.episode,
                                    mediaId = it.mediaId,
                                    timeUntilAiring = it.timeUntilAiring,
                                    id = it.id
                                )
                            }
                        )
                        MediaType.MANGA -> MediaCategory.Manga(
                            source.chapters ?: 0,
                            source.volumes ?: 0
                        )
                    },
                    isAdult = source.isAdult,
                    isFavourite = source.isFavourite,
                    id = source.id,
                    mediaListEntry = source.mediaListEntry?.let {
                        // TODO: build media list object
                        MediaList.empty()
                    } ?: MediaList.empty()
                )
            }
    }
}