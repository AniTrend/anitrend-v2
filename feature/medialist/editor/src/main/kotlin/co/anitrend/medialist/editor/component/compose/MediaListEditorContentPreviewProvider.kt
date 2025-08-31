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
package co.anitrend.medialist.editor.component.compose

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import co.anitrend.domain.common.entity.shared.FuzzyDate
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.media.entity.Media.SiteUrl
import co.anitrend.domain.media.entity.attribute.image.MediaImage
import co.anitrend.domain.media.entity.attribute.score.MediaScore
import co.anitrend.domain.media.entity.attribute.title.MediaTitle
import co.anitrend.domain.media.entity.attribute.trailer.MediaTrailer
import co.anitrend.domain.media.enums.MediaFormat
import co.anitrend.domain.media.enums.MediaSeason
import co.anitrend.domain.media.enums.MediaSource
import co.anitrend.domain.media.enums.MediaStatus
import co.anitrend.domain.medialist.entity.MediaList
import co.anitrend.domain.medialist.entity.contract.MediaListPrivacy
import co.anitrend.domain.medialist.entity.contract.MediaListProgress
import co.anitrend.domain.medialist.enums.MediaListStatus
import org.threeten.bp.Instant
import org.threeten.bp.temporal.ChronoUnit

private val data =
    Media.Extended.empty().copy(
        title = MediaTitle("Attack on Titan", "Shingeki no Kyojin", "進撃の巨人", "Attack on Titan"),
        image = MediaImage("url_banner", "url_extra_large", "url_large", "url_medium", "#FFC107"),
        category =
            Media.Category.Anime(
                episodes = 87,
                duration = 24,
                broadcast = "Sundays at 00:10 (JST)",
                premiered = "Spring 2013",
                schedule = null,
            ),
        status = MediaStatus.RELEASING,
        score = MediaScore(88, 89, 9.5f, 100000, 5000),
        format = MediaFormat.TV,
        countryCode = "JP",
        description = "Centuries ago...",
        externalLinks = emptyList(),
        favourites = 12000,
        genres = emptyList(),
        twitterTag = "#shingeki",
        isRecommendationBlocked = false,
        isReviewBlocked = false,
        rankings = emptyList(),
        isLicensed = true,
        isLocked = false,
        siteUrl = SiteUrl("anilist.co/anime/123", "myanimelist.net/anime/123"),
        source = MediaSource.ANIME,
        synonyms = emptyList(),
        tags = emptyList(),
        season = MediaSeason.SUMMER,
        startDate = FuzzyDate(2013, 4, 7),
        endDate = FuzzyDate.empty(),
        trailer = MediaTrailer("trailer_id", "youtube", "thumbnail_url"),
        isAdult = false,
        isFavourite = false,
        isFavouriteBlocked = false,
        mediaList = null,
        id = 1L,
    )

data class MediaListEditorContentPreviewProvider(
    override val values: Sequence<Media> =
        sequenceOf(
            data.copy(
                mediaList =
                    MediaList.Core(
                        id = 1L,
                        mediaId = 123L,
                        userId = 1L,
                        status = MediaListStatus.CURRENT,
                        score = 9f,
                        progress =
                            MediaListProgress.Anime(
                                episodeProgress = 60,
                                repeatedCount = 0,
                            ),
                        startedOn = FuzzyDate(2020, 1, 15),
                        finishedOn = FuzzyDate.empty(),
                        privacy =
                            MediaListPrivacy(
                                isPrivate = false,
                                notes = "Best anime ever!",
                                isHidden = false,
                            ),
                        customLists = listOf(MediaList.CustomList("Favorites", true), MediaList.CustomList("To Discuss", false)),
                        advancedScores =
                            listOf(
                                MediaList.AdvancedScore("Story", 6f),
                            ),
                        priority = 0,
                        createdOn = System.currentTimeMillis(),
                    ),
            ),
            data.copy(
                title = MediaTitle("One Piece", "One Piece", "One Piece", "One Piece"),
                category =
                    Media.Category.Manga(
                        chapters = 100,
                        volumes = 10,
                    ),
                mediaList =
                    MediaList.Core(
                        id = 1L,
                        mediaId = 123L,
                        userId = 1L,
                        status = MediaListStatus.CURRENT,
                        score = 6f,
                        progress =
                            MediaListProgress.Manga(
                                chapterProgress = 60,
                                volumeProgress = 2,
                                repeatedCount = 1,
                            ),
                        startedOn = FuzzyDate(2024, 12, 15),
                        finishedOn = FuzzyDate.empty(),
                        privacy =
                            MediaListPrivacy(
                                isPrivate = true,
                                notes = "Kind of hard to say how I felt about this show tbh",
                                isHidden = false,
                            ),
                        customLists = listOf(MediaList.CustomList("Binge", true)),
                        advancedScores = emptyList(),
                        priority = 0,
                        createdOn = Instant.now().minus(3, ChronoUnit.DAYS).toEpochMilli(),
                    ),
            ),
        ),
) : PreviewParameterProvider<Media>
