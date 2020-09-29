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
import co.anitrend.domain.genre.entity.Genre
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
import co.anitrend.domain.medialist.enums.ScoreFormat
import co.anitrend.domain.tag.entity.Tag

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
                    genres = source.genres?.map {
                        Genre(it)
                    }.orEmpty(),
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
                    scoreFormat = ScoreFormat.POINT_10_DECIMAL,// TODO: use user object to retrieve score format
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

                        MediaList.empty()
                    } ?: MediaList.empty() // TODO: build media list object
                )
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
                override fun transform(source: MediaModelExtended) = MediaEntity(
                    coverImage = MediaEntity.CoverImage(
                        color = source.coverImage?.color,
                        extraLarge = source.coverImage?.extraLarge,
                        large = source.coverImage?.large,
                        medium = source.coverImage?.medium,
                        banner = source.bannerImage,
                    ),
                    title = MediaEntity.Title(
                        romaji = source.title?.romaji,
                        english = source.title?.english,
                        native = source.title?.native,
                        userPreferred = source.title?.userPreferred
                    ),
                    trailer = source.trailer?.let {
                        MediaEntity.Trailer(
                            id = it.id.orEmpty(),
                            site = it.site,
                            thumbnail = it.thumbnail
                        )
                    },
                    nextAiring = source.nextAiringEpisode?.let {
                        MediaEntity.Airing(
                            airingAt = it.airingAt,
                            episode = it.episode,
                            airingId = it.id,
                            timeUntilAiring = it.timeUntilAiring,
                        )
                    },
                    genres = source.genres?.map {
                        MediaEntity.Genre(it)
                    }.orEmpty(),
                    tags = source.tags?.map {
                        MediaEntity.Tag(
                            name = it.name,
                            description = it.description,
                            category = it.category,
                            rank = it.rank,
                            isGeneralSpoiler = it.isGeneralSpoiler,
                            isMediaSpoiler = it.isMediaSpoiler,
                            isAdult = it.isAdult,
                            id = it.id
                        )
                    }.orEmpty(),
                    links = source.externalLinks?.map {
                        MediaEntity.Link(
                            site = it.site,
                            url = it.url,
                            id = it.id
                        )
                    }.orEmpty(),
                    ranks = source.rankings?.map {
                        MediaEntity.Rank(
                            allTime = it.allTime,
                            context = it.context,
                            format = it.format,
                            rank = it.rank,
                            season = it.season,
                            type = it.type,
                            year = it.year,
                            id = it.id
                        )
                    }.orEmpty(),
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
                    isLicensed = source.isLicensed,
                    isLocked = source.isLocked,
                    meanScore = source.meanScore,
                    popularity = source.popularity,
                    season = source.season,
                    siteUrl = source.siteUrl,
                    source = source.source,
                    startDate = source.startDate?.toFuzzyDateInt(),
                    status = source.status,
                    synonyms = source.synonyms.orEmpty(),
                    trending = source.trending,
                    type = source.type,
                    updatedAt = source.updatedAt,
                    volumes = source.volumes,
                    id = source.id
                )
            }
    }
}

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
                        externalLinks = source.links.map {
                            MediaExternalLink(
                                site = it.site,
                                url = it.url,
                                id = it.id
                            )
                        },
                        favouritesCount = source.id,
                        genres = source.genres.map {
                            Genre(
                                it.name
                            )
                        },
                        twitterTag = null,
                        isLicensed = source.isLicensed,
                        isLocked = source.isLocked,
                        isRecommendationBlocked = false,
                        rankings = source.ranks.map {
                            MediaRank(
                                allTime = it.allTime,
                                context = it.context,
                                format = it.format,
                                rank = it.rank,
                                season = it.season,
                                type = it.type,
                                year = it.year,
                                id = it.id
                            )
                        },
                        siteUrl = source.siteUrl,
                        source = source.source,
                        synonyms = source.synonyms,
                        tags = source.tags.map {
                            Tag(
                                name = it.name,
                                description = it.description,
                                category = it.category,
                                rank = it.rank ?: 0,
                                isGeneralSpoiler = it.isGeneralSpoiler ?: false,
                                isMediaSpoiler = it.isMediaSpoiler ?: false,
                                isAdult = it.isAdult ?: false,
                                id = it.id
                            )
                        },
                        trailer = source.trailer?.let {
                            MediaTrailer(
                                id = it.id,
                                site = it.site,
                                thumbnail = it.thumbnail
                            )
                        },
                        format = source.format,
                        season = source.season,
                        status = source.status,
                        scoreFormat = ScoreFormat.POINT_10_DECIMAL,// TODO: use user object to retrieve score format
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
                                source.nextAiring?.let {
                                    AiringSchedule(
                                        airingAt = it.airingAt,
                                        episode = it.episode,
                                        mediaId = source.id,
                                        timeUntilAiring = it.timeUntilAiring,
                                        id = it.airingId
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
                        mediaListEntry = MediaList.empty() // TODO: build media list object
                    )
                }
            }
    }
}