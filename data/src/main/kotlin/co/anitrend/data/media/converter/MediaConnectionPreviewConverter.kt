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
package co.anitrend.data.media.converter

import co.anitrend.data.common.extension.asFuzzyDate
import co.anitrend.data.common.extension.asFuzzyDateModel
import co.anitrend.data.common.extension.toFuzzyDateInt
import co.anitrend.data.media.entity.connection.MediaConnectionPreviewEntity
import co.anitrend.domain.airing.entity.AiringSchedule
import co.anitrend.domain.common.entity.shared.FuzzyDate
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.media.entity.attribute.image.MediaImage
import co.anitrend.domain.media.entity.attribute.score.MediaScore
import co.anitrend.domain.media.entity.attribute.title.MediaTitle
import co.anitrend.domain.media.enums.MediaType
import co.anitrend.domain.medialist.entity.MediaList
import co.anitrend.domain.medialist.entity.contract.MediaListPrivacy
import co.anitrend.domain.medialist.entity.contract.MediaListProgress
import co.anitrend.domain.medialist.enums.MediaListStatus
import kotlin.math.max

internal fun Media.toConnectionPreviewEntity(): MediaConnectionPreviewEntity {
    val animeCategory = category as? Media.Category.Anime
    val mangaCategory = category as? Media.Category.Manga

    return MediaConnectionPreviewEntity(
        id = id,
        type = category.type,
        format = format,
        status = status,
        startDate = startDate.asFuzzyDateModel().toFuzzyDateInt(),
        episodes = animeCategory?.episodes ?: 0,
        chapters = mangaCategory?.chapters ?: 0,
        volumes = mangaCategory?.volumes ?: 0,
        isFavourite = isFavourite,
        meanScore = score.mean,
        averageScore = score.average,
        personalScore = score.personal,
        nextAiringAt = animeCategory?.schedule?.airingAt,
        nextAiringEpisode = animeCategory?.schedule?.episode,
        nextAiringId = animeCategory?.schedule?.id,
        image =
            MediaConnectionPreviewEntity.Image(
                color = image.color?.toString(),
                large = image.large?.toString(),
                medium = image.medium?.toString(),
            ),
        title =
            MediaConnectionPreviewEntity.Title(
                english = title.english?.toString(),
                nativeTitle = title.native?.toString(),
                romaji = title.romaji?.toString(),
                userPreferred = title.userPreferred?.toString(),
            ),
        mediaList =
            mediaList?.let {
                MediaConnectionPreviewEntity.MediaListSummary(
                    status = it.status,
                    notes = it.privacy.notes?.toString(),
                )
            },
    )
}

private fun MediaConnectionPreviewEntity.toMediaList(): MediaList? {
    val summary = mediaList ?: return null
    return MediaList.Core.empty().copy(
        mediaId = id,
        score = personalScore ?: 0f,
        status = summary.status ?: MediaListStatus.PLANNING,
        progress =
            when (type) {
                MediaType.ANIME -> MediaListProgress.Anime.empty()
                MediaType.MANGA -> MediaListProgress.Manga.empty()
            },
        privacy = MediaListPrivacy.empty().copy(notes = summary.notes),
    )
}

private fun MediaConnectionPreviewEntity.toAiringSchedule(): AiringSchedule? {
    val airingAt = nextAiringAt ?: return null
    val episode = nextAiringEpisode ?: return null
    return AiringSchedule(
        airingAt = airingAt,
        episode = episode,
        mediaId = id,
        timeUntilAiring = max(0L, airingAt - (System.currentTimeMillis() / 1000)),
        id = nextAiringId ?: 0L,
    )
}

private fun MediaConnectionPreviewEntity.toMediaCategory(): Media.Category =
    when (type) {
        MediaType.ANIME ->
            Media.Category.Anime(
                episodes = episodes,
                duration = 0,
                broadcast = null,
                premiered = null,
                schedule = toAiringSchedule(),
            )

        MediaType.MANGA ->
            Media.Category.Manga(
                chapters = chapters,
                volumes = volumes,
            )
    }

internal fun MediaConnectionPreviewEntity.toMedia(): Media =
    Media.Core(
        externalLinks = emptyList(),
        rankings = emptyList(),
        trailer = null,
        title =
            MediaTitle(
                romaji = title.romaji,
                english = title.english,
                native = title.nativeTitle,
                userPreferred = title.userPreferred,
            ),
        image =
            MediaImage(
                color = image.color,
                extraLarge = image.large,
                large = image.large,
                medium = image.medium,
                banner = null,
            ),
        category = toMediaCategory(),
        isAdult = null,
        isFavourite = isFavourite,
        isFavouriteBlocked = false,
        format = format,
        season = null,
        status = status,
        score =
            MediaScore(
                mean = meanScore,
                average = averageScore,
                personal = personalScore,
                popularity = null,
                trending = null,
            ),
        startDate = startDate.asFuzzyDate(),
        endDate = FuzzyDate.empty(),
        mediaList = toMediaList(),
        id = id,
        countryCode = null,
        description = null,
        favourites = 0,
        genres = emptyList(),
        twitterTag = null,
        isLicensed = null,
        isLocked = null,
        isRecommendationBlocked = false,
        isReviewBlocked = false,
        siteUrl = Media.SiteUrl(),
        source = null,
        synonyms = emptyList(),
        tags = emptyList(),
    )
