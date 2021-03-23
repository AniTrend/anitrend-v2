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
import co.anitrend.data.media.converter.MediaEntityViewConverter.Companion.createMediaList
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
import co.anitrend.domain.media.enums.*
import co.anitrend.domain.medialist.entity.MediaList
import co.anitrend.domain.medialist.entity.contract.MediaListPrivacy
import co.anitrend.domain.medialist.entity.contract.MediaListProgress
import co.anitrend.domain.medialist.enums.MediaListStatus
import co.anitrend.domain.tag.entity.Tag
import java.util.*

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
                favourites = source.favourites,
                genres = source.genres.map {
                    Genre.Extended(
                        id = it.hashCode().toLong(),
                        name = it,
                        emoji = null,
                        background = source.coverImage?.color
                    )
                },
                twitterTag = null,
                isRecommendationBlocked = false,
                isLicensed = source.isLicensed,
                isLocked = source.isLocked,
                siteUrl = source.siteUrl,
                source = source.source,
                synonyms = source.synonyms,
                tags = source.tags.map {
                    Tag.Extended(
                        id = it.id,
                        name = it.name,
                        description = it.description,
                        category = it.category,
                        rank = it.rank ?: 0,
                        isGeneralSpoiler = it.isGeneralSpoiler ?: false,
                        isMediaSpoiler = it.isMediaSpoiler ?: false,
                        isAdult = it.isAdult ?: false,
                        background = source.coverImage?.color
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
                trailer = source.trailer?.let {
                    MediaTrailer(
                        id = it.id.orEmpty(),
                        site = it.site,
                        thumbnail = it.thumbnail
                    )
                },
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
                        broadcast = null,
                        premiered = null,
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
                favourites = source.favourites,
                genres = source.genres.map {
                   Genre.Extended(
                       id = it.hashCode().toLong(),
                       name = it,
                       emoji = null,
                       background = source.coverImage?.color
                   )
                },
                twitterTag = source.hashTag,
                isRecommendationBlocked = false,
                isLicensed = source.isLicensed,
                isLocked = source.isLocked,
                siteUrl = source.siteUrl,
                source = source.source,
                synonyms = source.synonyms,
                tags = source.tags.map {
                    Tag.Extended(
                        id = it.id,
                        name = it.name,
                        description = it.description,
                        category = it.category,
                        rank = it.rank ?: 0,
                        isGeneralSpoiler = it.isGeneralSpoiler ?: false,
                        isMediaSpoiler = it.isMediaSpoiler ?: false,
                        isAdult = it.isAdult ?: false,
                        background = source.coverImage?.color
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
                trailer = source.trailer?.let {
                    MediaTrailer(
                        id = it.id.orEmpty(),
                        site = it.site,
                        thumbnail = it.thumbnail
                    )
                },
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
                        broadcast = null,
                        premiered = null,
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
                background = null,
                ageRating = null,
                extraInfo = null,
                openingThemes = emptyList(),
                endingThemes = emptyList(),
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
                favourites = source.favourites,
                genres = source.genres.map  {
                    Genre.Extended(
                        id = it.hashCode().toLong(),
                        name = it,
                        emoji = null,
                        background = source.coverImage?.color
                    )
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
                    Tag.Extended(
                        id = it.id,
                        name = it.name,
                        description = it.description,
                        category = it.category,
                        rank = it.rank ?: 0,
                        isGeneralSpoiler = it.isGeneralSpoiler ?: false,
                        isMediaSpoiler = it.isMediaSpoiler ?: false,
                        isAdult = it.isAdult ?: false,
                        background = source.coverImage?.color
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
                        broadcast = null,
                        premiered = null,
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
                original = source.title?.native,
                userPreferred = source.title?.userPreferred
            ),
            trailer = source.trailer?.let {
                MediaEntity.Trailer(
                    id = it.id.orEmpty(),
                    site = it.site,
                    thumbnail = it.thumbnail
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
            isLicensed = source.isLicensed,
            isRecommendationBlocked = source.isRecommendationBlocked,
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
            id = source.id
        )
    }
}

internal class MediaEntityViewConverter(
    override val fromType: (MediaEntityView) -> Media = ::transform,
    override val toType: (Media) -> MediaEntityView = { throw NotImplementedError() }
) : SupportConverter<MediaEntityView, Media>() {
    private companion object : ISupportTransformer<MediaEntityView, Media> {

        private fun MediaListEntity.createMediaList(): MediaList {
            return MediaListEntityConverter().convertFrom(this)
        }

        private fun MediaEntityView.createMedia(): Media {
            return Media.Core(
                sourceId = MediaSourceId.empty().copy(
                    anilist = media.id,
                    mal = media.malId ?: moe?.mal,
                    aniDb = moe?.aniDb,
                    trakt = 0, // TODO provide trakt Id?
                    kitsu = moe?.kitsu,
                ),
                countryCode = media.countryOfOrigin,
                description = media.description ?: jikan?.synopsis,
                favourites = media.favourites,
                genres = genres.map {
                    Genre.Extended(
                        id = it.id,
                        name = it.genre,
                        emoji = it.emoji,
                        background = media.coverImage.color
                    )
                },
                twitterTag = media.hashTag,
                isLicensed = media.isLicensed,
                isLocked = media.isLocked,
                isRecommendationBlocked = media.isRecommendationBlocked,
                siteUrl = media.siteUrl,
                source = media.source ?: jikan?.source?.let {
                    runCatching {
                        MediaSource.valueOf(it.toUpperCase(Locale.ROOT))
                    }.getOrNull()
                } ?: jikan?.type?.let {
                    runCatching {
                        MediaSource.valueOf(it.toUpperCase(Locale.ROOT))
                    }.getOrNull()
                },
                synonyms = media.synonyms.let {
                    if (it.isEmpty())
                        jikan?.title?.synonyms.orEmpty()
                    else it
                },
                tags = tags.map {
                    Tag.Extended(
                        name = it.name,
                        description = it.description,
                        category = it.category,
                        rank = it.rank,
                        isGeneralSpoiler = it.isGeneralSpoiler,
                        isMediaSpoiler = it.isMediaSpoiler,
                        isAdult = it.isAdult,
                        id = it.id,
                        background = media.coverImage.color
                    )
                },
                trailer = media.trailer?.let {
                    MediaTrailer(
                        id = it.id,
                        site = it.site,
                        thumbnail = it.thumbnail
                    )
                },
                format = media.format,
                season = media.season,
                status = media.status,
                meanScore = media.meanScore ?: 0,
                averageScore = media.averageScore ?: 0,
                startDate = media.startDate.toFuzzyDateModel().asFuzzyDate(),
                endDate = media.endDate.toFuzzyDateModel().asFuzzyDate(),
                title = MediaTitle(
                    romaji = media.title.romaji ?: jikan?.title?.japanese,
                    english = media.title.english ?: jikan?.title?.english,
                    native = media.title.original,
                    userPreferred = media.title.userPreferred ?: jikan?.title?.preferred
                ),
                image = MediaImage(
                    color = media.coverImage.color,
                    extraLarge = media.coverImage.extraLarge ?: jikan?.imageUrl,
                    large = media.coverImage.large ?: jikan?.imageUrl,
                    medium = media.coverImage.medium ?: jikan?.imageUrl,
                    banner = media.coverImage.banner,
                ),
                category = when (media.type) {
                    MediaType.ANIME -> Media.Category.Anime(
                        media.episodes ?: jikan?.episodes ?: 0,
                        media.duration ?: 0,
                        broadcast = jikan?.broadcast,
                        premiered = jikan?.premiered,
                        nextAiring?.let {
                            AiringSchedule(
                                airingAt = it.airingAt,
                                episode = it.episode,
                                mediaId = media.id,
                                timeUntilAiring = it.timeUntilAiring,
                                id = it.id
                            )
                        }
                    )
                    MediaType.MANGA -> Media.Category.Manga(
                        media.chapters ?: jikan?.chapters ?: 0,
                        media.volumes ?: jikan?.volumes ?: 0
                    )
                },
                isAdult = media.isAdult,
                isFavourite = media.isFavourite,
                id = media.id,
                mediaList = mediaList?.createMediaList()
            )
        }

        override fun transform(source: MediaEntityView) = when (source) {
            is MediaEntityView.Core -> source.createMedia()
            is MediaEntityView.Extended -> source.createMedia().let { media ->
                Media.Extended(
                    sourceId = media.sourceId,
                    background = source.jikan?.background,
                    ageRating = source.jikan?.rating,
                    extraInfo = source.jikan?.info,
                    openingThemes = source.jikan?.openingThemes.orEmpty(),
                    endingThemes = source.jikan?.endingThemes.orEmpty(),
                    countryCode = media.countryCode,
                    description = media.description,
                    externalLinks = source.links.map {
                        MediaExternalLink(
                            site = it.site,
                            url = it.url,
                            id = it.id
                        )
                    },
                    favourites = media.favourites,
                    genres = media.genres,
                    twitterTag = media.twitterTag,
                    isLicensed = media.isLicensed,
                    isLocked = media.isLocked,
                    isRecommendationBlocked = media.isRecommendationBlocked,
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
                    siteUrl = media.siteUrl,
                    source = media.source,
                    synonyms = media.synonyms,
                    tags = media.tags,
                    trailer = media.trailer,
                    format = media.format,
                    season = media.season,
                    status = media.status,
                    meanScore = media.meanScore,
                    averageScore = media.averageScore,
                    startDate = media.startDate,
                    endDate = media.endDate,
                    title = media.title,
                    image = media.image,
                    category = media.category,
                    isAdult = media.isAdult,
                    isFavourite = media.isFavourite,
                    id = media.id,
                    mediaList = media.mediaList
                )
            }
        }
    }
}