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

package co.anitrend.data.media.converter

import co.anitrend.arch.data.converter.SupportConverter
import co.anitrend.arch.data.transformer.ISupportTransformer
import co.anitrend.data.arch.extension.asFuzzyDate
import co.anitrend.data.arch.extension.toFuzzyDateInt
import co.anitrend.data.arch.extension.toFuzzyDateModel
import co.anitrend.data.media.entity.MediaEntity
import co.anitrend.data.media.entity.view.MediaEntityView
import co.anitrend.data.media.model.MediaModel
import co.anitrend.data.medialist.converter.MediaListEntityConverter
import co.anitrend.data.medialist.entity.MediaListEntity
import co.anitrend.data.medialist.model.MediaListModel
import co.anitrend.domain.airing.entity.AiringSchedule
import co.anitrend.domain.genre.entity.Genre
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.media.entity.attribute.image.MediaImage
import co.anitrend.domain.media.entity.attribute.link.MediaExternalLink
import co.anitrend.domain.media.entity.attribute.origin.MediaSourceId
import co.anitrend.domain.media.entity.attribute.rank.MediaRank
import co.anitrend.domain.media.entity.attribute.title.MediaTitle
import co.anitrend.domain.media.entity.attribute.trailer.MediaTrailer
import co.anitrend.domain.media.enums.MediaType
import co.anitrend.domain.medialist.entity.MediaList
import co.anitrend.domain.medialist.entity.contract.MediaListPrivacy
import co.anitrend.domain.medialist.entity.contract.MediaListProgress
import co.anitrend.domain.medialist.enums.MediaListStatus
import co.anitrend.domain.tag.entity.Tag

internal class MediaConverter(
    override val fromType: (MediaModel) -> Media = ::transform,
    override val toType: (Media) -> MediaModel = { throw NotImplementedError() }
) : SupportConverter<MediaModel, Media>() {
    private companion object : ISupportTransformer<MediaModel, Media> {

        private fun createMediaListProgress(source: MediaListModel.Core): MediaListProgress {
            return when (source.category.type) {
                MediaType.ANIME -> MediaListProgress.Anime(
                    episodeProgress = source.progress ?: 0,
                    repeatedCount = source.repeat ?: 0
                )
                MediaType.MANGA -> MediaListProgress.Manga(
                    chapterProgress = source.progress ?: 0,
                    volumeProgress = source.progressVolumes ?: 0,
                    repeatedCount = source.repeat ?: 0
                )
            }
        }

        private fun convertMediaListEntry(model: MediaListModel.Core?): MediaList {
            return model?.let {
                MediaList.Core(
                    advancedScores = it.advancedScores?.map { entry ->
                        MediaList.AdvancedScore(
                            name = entry.key,
                            score = entry.value
                        )
                    }.orEmpty(),
                    customLists = it.customLists?.map { custom ->
                        MediaList.CustomList(
                            name = custom.name,
                            enabled = custom.enabled
                        )
                    }.orEmpty(),
                    userId = it.userId,
                    priority = it.priority,
                    createdOn = it.createdAt,
                    startedOn = it.startedAt.asFuzzyDate(),
                    finishedOn = it.completedAt.asFuzzyDate(),
                    mediaId = it.mediaId,
                    score = it.score ?: 0f,
                    status = it.status ?: MediaListStatus.PLANNING,
                    progress = createMediaListProgress(it),
                    privacy = MediaListPrivacy(
                        isPrivate = it.private ?: false,
                        isHidden = it.hiddenFromStatusLists ?: false,
                        notes = it.notes
                    ),
                    id = it.id,
                )
            } ?: MediaList.Core.empty()
        }

        private fun MediaModel.createMediaList(): MediaList {
            return when (this) {
                is MediaModel.Media -> MediaList.Core.empty()
                is MediaModel.Core -> convertMediaListEntry(mediaListEntry)
                is MediaModel.Extended -> convertMediaListEntry(mediaListEntry)
            }
        }

        override fun transform(source: MediaModel) = when (source) {
            is MediaModel.Media -> Media.Core(
                sourceId = MediaSourceId.empty().copy(
                    mal = source.idMal,
                    anilist = source.id
                ),
                countryCode = source.countryOfOrigin,
                description = source.description,
                favouritesCount = source.id,
                genres = source.genres.map {
                    Genre(it)
                },
                twitterTag = null,
                isRecommendationBlocked = false,
                isLicensed = source.isLicensed,
                isLocked = source.isLocked,
                siteUrl = source.siteUrl,
                source = source.source,
                synonyms = source.synonyms,
                tags = source.tags.map {
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
                },
                format = source.format,
                season = source.season,
                status = source.status,
                meanScore = source.meanScore ?: 0,
                averageScore = source.averageScore ?: 0,
                startDate = source.startDate.asFuzzyDate(),
                endDate = source.endDate.asFuzzyDate(),
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
                    MediaType.ANIME -> Media.Category.Anime(
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
                    else -> Media.Category.Manga(
                        source.chapters ?: 0,
                        source.volumes ?: 0
                    )
                },
                isAdult = source.isAdult,
                isFavourite = source.isFavourite,
                id = source.id,
                mediaList = null
            )
            is MediaModel.Core -> Media.Core(
                sourceId = MediaSourceId.empty().copy(
                    mal = source.idMal,
                    anilist = source.id
                ),
                countryCode = source.countryOfOrigin,
                description = source.description,
                favouritesCount = source.id,
                genres = source.genres.map {
                    Genre(it)
                },
                twitterTag = null,
                isRecommendationBlocked = false,
                isLicensed = source.isLicensed,
                isLocked = source.isLocked,
                siteUrl = source.siteUrl,
                source = source.source,
                synonyms = source.synonyms,
                tags = source.tags.map {
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
                },
                format = source.format,
                season = source.season,
                status = source.status,
                meanScore = source.meanScore ?: 0,
                averageScore = source.averageScore ?: 0,
                startDate = source.startDate.asFuzzyDate(),
                endDate = source.endDate.asFuzzyDate(),
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
                    MediaType.ANIME -> Media.Category.Anime(
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
                    else -> Media.Category.Manga(
                        source.chapters ?: 0,
                        source.volumes ?: 0
                    )
                },
                isAdult = source.isAdult,
                isFavourite = source.isFavourite,
                id = source.id,
                mediaList = source.createMediaList()
            )
            is MediaModel.Extended -> Media.Extended(
                sourceId = MediaSourceId.empty().copy(
                    mal = source.idMal,
                    anilist = source.id
                ),
                countryCode = source.countryOfOrigin,
                description = source.description,
                externalLinks = source.externalLinks.map {
                    MediaExternalLink(
                        site = it.site,
                        url = it.url,
                        id = it.id,
                    )
                },
                favouritesCount = source.id,
                genres = source.genres.map {
                    Genre(it)
                },
                twitterTag = null,
                isLicensed = source.isLicensed,
                isLocked = source.isLocked,
                isRecommendationBlocked = false,
                rankings = source.rankings.map {
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
                },
                siteUrl = source.siteUrl,
                source = source.source,
                synonyms = source.synonyms,
                tags = source.tags.map {
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
                },
                trailer = MediaTrailer(
                    source.trailer?.id,
                    source.trailer?.site,
                    source.trailer?.thumbnail
                ),
                format = source.format,
                season = source.season,
                status = source.status,
                meanScore = source.meanScore ?: 0,
                averageScore = source.averageScore ?: 0,
                startDate = source.startDate.asFuzzyDate(),
                endDate = source.endDate.asFuzzyDate(),
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
                    MediaType.ANIME -> Media.Category.Anime(
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
                    else -> Media.Category.Manga(
                        source.chapters ?: 0,
                        source.volumes ?: 0
                    )
                },
                isAdult = source.isAdult,
                isFavourite = source.isFavourite,
                id = source.id,
                mediaList = source.createMediaList()
            )
        }
    }
}

internal class MediaModelConverter(
    override val fromType: (MediaModel) -> MediaEntity = ::transform,
    override val toType: (MediaEntity) -> MediaModel = { throw NotImplementedError() }
) : SupportConverter<MediaModel, MediaEntity>() {
    private companion object : ISupportTransformer<MediaModel, MediaEntity> {
        override fun transform(source: MediaModel) = MediaEntity(
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
            trailer = when (source) {
                is MediaModel.Extended -> source.trailer?.let {
                    MediaEntity.Trailer(
                        id = it.id.orEmpty(),
                        site = it.site,
                        thumbnail = it.thumbnail
                    )
                }
                else -> null
            },
            nextAiringId = source.nextAiringEpisode?.id,
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
            links = when (source) {
                is MediaModel.Extended -> source.externalLinks.map {
                    MediaEntity.Link(
                        site = it.site,
                        url = it.url,
                        id = it.id
                    )
                }
                else -> emptyList()
            },
            ranks = when (source) {
                is MediaModel.Extended -> source.rankings.map {
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
                }
                else -> emptyList()
            },
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
            synonyms = source.synonyms,
            trending = source.trending,
            type = source.type,
            updatedAt = source.updatedAt,
            volumes = source.volumes,
            id = source.id
        )
    }
}

internal class MediaEntityViewConverter(
    override val fromType: (MediaEntityView) -> Media = { transform(it) },
    override val toType: (Media) -> MediaEntityView = { throw NotImplementedError() }
) : SupportConverter<MediaEntityView, Media>() {
    private companion object : ISupportTransformer<MediaEntityView, Media> {

        private fun MediaListEntity.createMediaList(): MediaList {
            return MediaListEntityConverter().convertFrom(this)
        }

        override fun transform(source: MediaEntityView) = when (source) {
            is MediaEntityView.Core -> Media.Core(
                sourceId = MediaSourceId.empty().copy(
                    anilist = source.media.id
                ),
                countryCode = source.media.countryOfOrigin,
                description = source.media.description,
                favouritesCount = source.media.id,
                genres = source.media.genres.map {
                    Genre(it.name)
                },
                twitterTag = source.media.hashTag,
                isLicensed = source.media.isLicensed,
                isLocked = source.media.isLocked,
                isRecommendationBlocked = false,
                siteUrl = source.media.siteUrl,
                source = source.media.source,
                synonyms = source.media.synonyms,
                tags = source.media.tags.map {
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
                format = source.media.format,
                season = source.media.season,
                status = source.media.status,
                meanScore = source.media.meanScore ?: 0,
                averageScore = source.media.averageScore ?: 0,
                startDate = source.media.startDate.toFuzzyDateModel().asFuzzyDate(),
                endDate = source.media.endDate.toFuzzyDateModel().asFuzzyDate(),
                title = MediaTitle(
                    romaji = source.media.title.romaji,
                    english = source.media.title.english,
                    native = source.media.title.native,
                    userPreferred = source.media.title.userPreferred
                ),
                image = MediaImage(
                    color = source.media.coverImage.color,
                    extraLarge = source.media.coverImage.extraLarge,
                    large = source.media.coverImage.large,
                    medium = source.media.coverImage.medium,
                    banner = source.media.coverImage.banner,
                ),
                category = when (source.media.type) {
                    MediaType.ANIME -> Media.Category.Anime(
                        source.media.episodes ?: 0,
                        source.media.duration ?: 0,
                        source.nextAiring?.let {
                            AiringSchedule(
                                airingAt = it.airingAt,
                                episode = it.episode,
                                mediaId = source.media.id,
                                timeUntilAiring = it.timeUntilAiring,
                                id = it.id
                            )
                        }
                    )
                    MediaType.MANGA -> Media.Category.Manga(
                        source.media.chapters ?: 0,
                        source.media.volumes ?: 0
                    )
                },
                isAdult = source.media.isAdult,
                isFavourite = source.media.isFavourite,
                id = source.media.id,
                mediaList = null
            )
            is MediaEntityView.WithMediaList -> Media.Extended(
                sourceId = MediaSourceId.empty().copy(
                    anilist = source.media.id
                ),
                countryCode = source.media.countryOfOrigin,
                description = source.media.description,
                externalLinks = source.media.links.map {
                    MediaExternalLink(
                        site = it.site,
                        url = it.url,
                        id = it.id
                    )
                },
                favouritesCount = source.media.id,
                genres = source.media.genres.map {
                    Genre(it.name)
                },
                twitterTag = source.media.hashTag,
                isLicensed = source.media.isLicensed,
                isLocked = source.media.isLocked,
                isRecommendationBlocked = false,
                rankings = source.media.ranks.map {
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
                siteUrl = source.media.siteUrl,
                source = source.media.source,
                synonyms = source.media.synonyms,
                tags = source.media.tags.map {
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
                trailer = source.media.trailer?.let {
                    MediaTrailer(
                        id = it.id,
                        site = it.site,
                        thumbnail = it.thumbnail
                    )
                },
                format = source.media.format,
                season = source.media.season,
                status = source.media.status,
                meanScore = source.media.meanScore ?: 0,
                averageScore = source.media.averageScore ?: 0,
                startDate = source.media.startDate.toFuzzyDateModel().asFuzzyDate(),
                endDate = source.media.endDate.toFuzzyDateModel().asFuzzyDate(),
                title = MediaTitle(
                    romaji = source.media.title.romaji,
                    english = source.media.title.english,
                    native = source.media.title.native,
                    userPreferred = source.media.title.userPreferred
                ),
                image = MediaImage(
                    color = source.media.coverImage.color,
                    extraLarge = source.media.coverImage.extraLarge,
                    large = source.media.coverImage.large,
                    medium = source.media.coverImage.medium,
                    banner = source.media.coverImage.banner,
                ),
                category = when (source.media.type) {
                    MediaType.ANIME -> Media.Category.Anime(
                        source.media.episodes ?: 0,
                        source.media.duration ?: 0,
                        source.nextAiring?.let {
                            AiringSchedule(
                                airingAt = it.airingAt,
                                episode = it.episode,
                                mediaId = source.media.id,
                                timeUntilAiring = it.timeUntilAiring,
                                id = it.id
                            )
                        }
                    )
                    MediaType.MANGA -> Media.Category.Manga(
                        source.media.chapters ?: 0,
                        source.media.volumes ?: 0
                    )
                },
                isAdult = source.media.isAdult,
                isFavourite = source.media.isFavourite,
                id = source.media.id,
                mediaList = source.mediaList?.createMediaList()
            )
        }
    }
}