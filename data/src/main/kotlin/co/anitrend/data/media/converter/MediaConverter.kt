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
import co.anitrend.data.common.extension.asFuzzyDate
import co.anitrend.data.media.model.MediaModel
import co.anitrend.data.medialist.model.MediaListModel
import co.anitrend.domain.airing.entity.AiringSchedule
import co.anitrend.domain.genre.entity.Genre
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.media.entity.attribute.image.MediaImage
import co.anitrend.domain.media.entity.attribute.link.MediaExternalLink
import co.anitrend.domain.media.entity.attribute.origin.MediaSourceId
import co.anitrend.domain.media.entity.attribute.rank.MediaRank
import co.anitrend.domain.media.entity.attribute.score.MediaScore
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
    override val toType: (Media) -> MediaModel = { throw NotImplementedError() },
) : SupportConverter<MediaModel, Media>() {
    private companion object : ISupportTransformer<MediaModel, Media> {
        private fun createMediaListProgress(source: MediaListModel.Core): MediaListProgress =
            when (source.category.type) {
                MediaType.ANIME ->
                    MediaListProgress.Anime(
                        episodeProgress = source.progress ?: 0,
                        repeatedCount = source.repeat ?: 0,
                    )
                MediaType.MANGA ->
                    MediaListProgress.Manga(
                        chapterProgress = source.progress ?: 0,
                        volumeProgress = source.progressVolumes ?: 0,
                        repeatedCount = source.repeat ?: 0,
                    )
            }

        private fun MediaModel.createSiteUrl(): Media.SiteUrl =
            Media.SiteUrl(
                aniList = siteUrl,
                myAnimeList =
                    idMal?.let {
                        "https://myanimelist.net/${type.name.lowercase()}/$it"
                    },
            )

        private fun convertMediaListEntry(model: MediaListModel.Core?): MediaList =
            model?.let {
                MediaList.Core(
                    advancedScores =
                        it.advancedScores
                            ?.map { entry ->
                                MediaList.AdvancedScore(
                                    name = entry.key,
                                    score = entry.value,
                                )
                            }.orEmpty(),
                    customLists =
                        it.customLists
                            ?.map { custom ->
                                MediaList.CustomList(
                                    name = custom.name,
                                    enabled = custom.enabled,
                                )
                            }.orEmpty(),
                    userId = it.user.id,
                    priority = it.priority,
                    createdOn = it.createdAt,
                    startedOn = it.startedAt.asFuzzyDate(),
                    finishedOn = it.completedAt.asFuzzyDate(),
                    mediaId = it.mediaId,
                    score = it.score ?: 0f,
                    status = it.status ?: MediaListStatus.PLANNING,
                    progress = createMediaListProgress(it),
                    privacy =
                        MediaListPrivacy(
                            isPrivate = it.private ?: false,
                            isHidden = it.hiddenFromStatusLists ?: false,
                            notes = it.notes,
                        ),
                    id = it.id,
                )
            } ?: MediaList.Core.empty()

        private fun MediaModel.createMediaList(): MediaList =
            when (this) {
                is MediaModel.Media -> MediaList.Core.empty()
                is MediaModel.Core -> convertMediaListEntry(mediaListEntry)
                is MediaModel.Extended -> convertMediaListEntry(mediaListEntry)
            }

        override fun transform(source: MediaModel) =
            when (source) {
                is MediaModel.Media ->
                    Media.Core(
                        countryCode = source.countryOfOrigin,
                        description = source.description,
                        externalLinks =
                            source.externalLinks.map {
                                MediaExternalLink(
                                    color = it.color,
                                    icon = it.icon,
                                    isDisabled = it.isDisabled,
                                    language = it.language,
                                    notes = it.notes,
                                    siteId = it.siteId,
                                    linkType = it.linkType,
                                    site = it.site,
                                    url = it.url,
                                    id = it.id,
                                )
                            },
                        favourites = source.favourites,
                        genres =
                            source.genres.map {
                                Genre.Extended(
                                    id = it.hashCode().toLong(),
                                    name = it,
                                    emoji = null,
                                    background = source.coverImage?.color,
                                )
                            },
                        twitterTag = source.hashTag,
                        isRecommendationBlocked = false,
                        isReviewBlocked = false,
                        rankings =
                            source.rankings.map {
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
                        isLicensed = source.isLicensed,
                        isLocked = source.isLocked,
                        siteUrl = source.createSiteUrl(),
                        source = source.source,
                        synonyms = source.synonyms,
                        tags =
                            source.tags.map {
                                Tag.Extended(
                                    id = it.id,
                                    name = it.name,
                                    description = it.description,
                                    category = it.category,
                                    rank = it.rank ?: 0,
                                    isGeneralSpoiler = it.isGeneralSpoiler ?: false,
                                    isMediaSpoiler = it.isMediaSpoiler ?: false,
                                    isAdult = it.isAdult ?: false,
                                    background = source.coverImage?.color,
                                )
                            },
                        format = source.format,
                        season = source.season,
                        status = source.status,
                        score =
                            MediaScore(
                                mean = source.meanScore ?: 0,
                                average = source.averageScore ?: 0,
                                personal = null,
                                popularity = source.popularity,
                                trending = source.trending,
                            ),
                        startDate = source.startDate.asFuzzyDate(),
                        endDate = source.endDate.asFuzzyDate(),
                        title =
                            MediaTitle(
                                romaji = source.title?.romaji,
                                english = source.title?.english,
                                native = source.title?.native,
                                userPreferred = source.title?.userPreferred,
                            ),
                        trailer =
                            source.trailer?.let {
                                MediaTrailer(
                                    id = it.id.orEmpty(),
                                    site = it.site,
                                    thumbnail = it.thumbnail,
                                )
                            },
                        image =
                            MediaImage(
                                color = source.coverImage?.color,
                                extraLarge = source.coverImage?.extraLarge,
                                large = source.coverImage?.large,
                                medium = source.coverImage?.medium,
                                banner = source.bannerImage,
                            ),
                        category =
                            when (source.type) {
                                MediaType.ANIME ->
                                    Media.Category.Anime(
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
                                                id = it.id,
                                            )
                                        },
                                    )

                                else ->
                                    Media.Category.Manga(
                                        source.chapters ?: 0,
                                        source.volumes ?: 0,
                                    )
                            },
                        isAdult = source.isAdult,
                        isFavourite = source.isFavourite,
                        isFavouriteBlocked = source.isFavouriteBlocked,
                        id = source.id,
                        mediaList = null,
                    )
                is MediaModel.Core ->
                    Media.Core(
                        countryCode = source.countryOfOrigin,
                        description = source.description,
                        externalLinks =
                            source.externalLinks.map {
                                MediaExternalLink(
                                    color = it.color,
                                    icon = it.icon,
                                    isDisabled = it.isDisabled,
                                    language = it.language,
                                    notes = it.notes,
                                    siteId = it.siteId,
                                    linkType = it.linkType,
                                    site = it.site,
                                    url = it.url,
                                    id = it.id,
                                )
                            },
                        favourites = source.favourites,
                        genres =
                            source.genres.map {
                                Genre.Extended(
                                    id = it.hashCode().toLong(),
                                    name = it,
                                    emoji = null,
                                    background = source.coverImage?.color,
                                )
                            },
                        twitterTag = source.hashTag,
                        isRecommendationBlocked = false,
                        isReviewBlocked = false,
                        rankings =
                            source.rankings.map {
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
                        isLicensed = source.isLicensed,
                        isLocked = source.isLocked,
                        siteUrl = source.createSiteUrl(),
                        source = source.source,
                        synonyms = source.synonyms,
                        tags =
                            source.tags.map {
                                Tag.Extended(
                                    id = it.id,
                                    name = it.name,
                                    description = it.description,
                                    category = it.category,
                                    rank = it.rank ?: 0,
                                    isGeneralSpoiler = it.isGeneralSpoiler ?: false,
                                    isMediaSpoiler = it.isMediaSpoiler ?: false,
                                    isAdult = it.isAdult ?: false,
                                    background = source.coverImage?.color,
                                )
                            },
                        format = source.format,
                        season = source.season,
                        status = source.status,
                        score =
                            MediaScore(
                                mean = source.meanScore ?: 0,
                                average = source.averageScore ?: 0,
                                personal = source.mediaListEntry?.score,
                                popularity = source.popularity,
                                trending = source.trending,
                            ),
                        startDate = source.startDate.asFuzzyDate(),
                        endDate = source.endDate.asFuzzyDate(),
                        title =
                            MediaTitle(
                                romaji = source.title?.romaji,
                                english = source.title?.english,
                                native = source.title?.native,
                                userPreferred = source.title?.userPreferred,
                            ),
                        trailer =
                            source.trailer?.let {
                                MediaTrailer(
                                    id = it.id.orEmpty(),
                                    site = it.site,
                                    thumbnail = it.thumbnail,
                                )
                            },
                        image =
                            MediaImage(
                                color = source.coverImage?.color,
                                extraLarge = source.coverImage?.extraLarge,
                                large = source.coverImage?.large,
                                medium = source.coverImage?.medium,
                                banner = source.bannerImage,
                            ),
                        category =
                            when (source.type) {
                                MediaType.ANIME ->
                                    Media.Category.Anime(
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
                                                id = it.id,
                                            )
                                        },
                                    )
                                else ->
                                    Media.Category.Manga(
                                        source.chapters ?: 0,
                                        source.volumes ?: 0,
                                    )
                            },
                        isAdult = source.isAdult,
                        isFavourite = source.isFavourite,
                        isFavouriteBlocked = source.isFavouriteBlocked,
                        id = source.id,
                        mediaList = source.createMediaList(),
                    )
                is MediaModel.Extended ->
                    Media.Extended(
                        background = null,
                        ageRating = null,
                        extraInfo = null,
                        themes = emptyList(),
                        sourceId =
                            MediaSourceId.empty().copy(
                                myAnimeList = source.idMal,
                                aniList = source.id,
                            ),
                        countryCode = source.countryOfOrigin,
                        description = source.description,
                        externalLinks =
                            source.externalLinks.map {
                                MediaExternalLink(
                                    color = it.color,
                                    icon = it.icon,
                                    isDisabled = it.isDisabled,
                                    language = it.language,
                                    notes = it.notes,
                                    siteId = it.siteId,
                                    linkType = it.linkType,
                                    site = it.site,
                                    url = it.url,
                                    id = it.id,
                                )
                            },
                        favourites = source.favourites,
                        genres =
                            source.genres.map {
                                Genre.Extended(
                                    id = it.hashCode().toLong(),
                                    name = it,
                                    emoji = null,
                                    background = source.coverImage?.color,
                                )
                            },
                        twitterTag = null,
                        isLicensed = source.isLicensed,
                        isLocked = source.isLocked,
                        isRecommendationBlocked = false,
                        isReviewBlocked = false,
                        rankings =
                            source.rankings.map {
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
                        siteUrl =
                            Media.SiteUrl(
                                aniList = source.siteUrl,
                                myAnimeList = source.idMal?.let { "" },
                            ),
                        source = source.source,
                        synonyms = source.synonyms,
                        tags =
                            source.tags.map {
                                Tag.Extended(
                                    id = it.id,
                                    name = it.name,
                                    description = it.description,
                                    category = it.category,
                                    rank = it.rank ?: 0,
                                    isGeneralSpoiler = it.isGeneralSpoiler ?: false,
                                    isMediaSpoiler = it.isMediaSpoiler ?: false,
                                    isAdult = it.isAdult ?: false,
                                    background = source.coverImage?.color,
                                )
                            },
                        trailer =
                            MediaTrailer(
                                source.trailer?.id,
                                source.trailer?.site,
                                source.trailer?.thumbnail,
                            ),
                        format = source.format,
                        season = source.season,
                        status = source.status,
                        score =
                            MediaScore(
                                mean = source.meanScore ?: 0,
                                average = source.averageScore ?: 0,
                                personal = source.mediaListEntry?.score,
                                popularity = source.popularity,
                                trending = source.trending,
                            ),
                        startDate = source.startDate.asFuzzyDate(),
                        endDate = source.endDate.asFuzzyDate(),
                        title =
                            MediaTitle(
                                romaji = source.title?.romaji,
                                english = source.title?.english,
                                native = source.title?.native,
                                userPreferred = source.title?.userPreferred,
                            ),
                        image =
                            MediaImage(
                                color = source.coverImage?.color,
                                extraLarge = source.coverImage?.extraLarge,
                                large = source.coverImage?.large,
                                medium = source.coverImage?.medium,
                                banner = source.bannerImage,
                            ),
                        category =
                            when (source.type) {
                                MediaType.ANIME ->
                                    Media.Category.Anime(
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
                                                id = it.id,
                                            )
                                        },
                                    )
                                else ->
                                    Media.Category.Manga(
                                        source.chapters ?: 0,
                                        source.volumes ?: 0,
                                    )
                            },
                        isAdult = source.isAdult,
                        isFavourite = source.isFavourite,
                        isFavouriteBlocked = source.isFavouriteBlocked,
                        id = source.id,
                        mediaList = source.createMediaList(),
                    )
            }
    }
}
