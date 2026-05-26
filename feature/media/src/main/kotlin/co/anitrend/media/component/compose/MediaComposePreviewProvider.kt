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
package co.anitrend.media.component.compose

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import co.anitrend.domain.airing.entity.AiringSchedule
import co.anitrend.domain.common.entity.shared.FuzzyDate
import co.anitrend.domain.genre.entity.Genre
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.media.entity.attribute.image.MediaImage
import co.anitrend.domain.media.entity.attribute.rank.MediaRank
import co.anitrend.domain.media.entity.attribute.score.MediaScore
import co.anitrend.domain.media.entity.attribute.theme.MediaTheme
import co.anitrend.domain.media.entity.attribute.title.MediaTitle
import co.anitrend.domain.media.enums.MediaFormat
import co.anitrend.domain.media.enums.MediaRankType
import co.anitrend.domain.media.enums.MediaSeason
import co.anitrend.domain.media.enums.MediaSource
import co.anitrend.domain.media.enums.MediaStatus
import co.anitrend.domain.medialist.entity.MediaList
import co.anitrend.domain.medialist.entity.contract.MediaListPrivacy
import co.anitrend.domain.medialist.enums.MediaListStatus
import co.anitrend.domain.tag.entity.Tag
import org.threeten.bp.Instant
import org.threeten.bp.temporal.ChronoUnit

internal data class MediaComposePreviewProvider(
    override val values: Sequence<Media.Extended> =
        sequenceOf(
            Media.Extended.empty().copy(
                title =
                    MediaTitle(
                        userPreferred = "Boku no Hero Academia 3",
                        english = "My Hero Academia Season 3",
                        romaji = "Boku no Hero Academia 3",
                        native = "僕のヒーローアカデミア 3",
                    ),
                extraInfo =
                    "Mangaka Kouhei Horikoshi has noted that American superhero comics are the inspiration " +
                        "for the series, and has based character pages on logos for Marvel and DC comic characters.",
                status = MediaStatus.FINISHED,
                image = MediaImage.empty().copy(color = "#e4a15d"),
                startDate = FuzzyDate.empty().copy(2018),
                format = MediaFormat.TV,
                category =
                    Media.Category.Anime(
                        episodes = 25,
                        duration = 24,
                        broadcast = "Saturdays 17:30 (JST)",
                        premiered = "Spring 2018",
                        schedule =
                            AiringSchedule(
                                airingAt = Instant.now().plus(2, ChronoUnit.DAYS).epochSecond,
                                episode = 23,
                                mediaId = 1,
                                timeUntilAiring = 172800,
                                id = 23,
                            ),
                        scheduleDetails =
                            Media.Category.Anime.ScheduleDetails(
                                airedEpisodes = 22,
                                firstAirDate = Instant.now().minus(120, ChronoUnit.DAYS).epochSecond,
                                lastAirDate = Instant.now().minus(5, ChronoUnit.DAYS).epochSecond,
                                nextEpisode =
                                    Media.Category.Anime.ScheduleDetails.Episode(
                                        id = 23,
                                        airDate = Instant.now().plus(2, ChronoUnit.DAYS).epochSecond,
                                        episodeNumber = 23,
                                        image = "https://cdn.example.com/mha-23.jpg",
                                        name = "Full Cowl",
                                        overview = "The raid plan hits a breaking point as the next clash comes into view.",
                                        productionCode = null,
                                        runtime = 24,
                                        seasonNumber = 3,
                                        tmdbId = null,
                                    ),
                                lastEpisode =
                                    Media.Category.Anime.ScheduleDetails.Episode(
                                        id = 22,
                                        airDate = Instant.now().minus(5, ChronoUnit.DAYS).epochSecond,
                                        episodeNumber = 22,
                                        image = "https://cdn.example.com/mha-22.jpg",
                                        name = "Bakugo's Answer",
                                        overview = "The fallout from the last duel reshapes the team's hierarchy.",
                                        productionCode = null,
                                        runtime = 24,
                                        seasonNumber = 3,
                                        tmdbId = null,
                                    ),
                                episodes =
                                    listOf(
                                        Media.Category.Anime.ScheduleDetails.Episode(
                                            id = 21,
                                            airDate = Instant.now().minus(12, ChronoUnit.DAYS).epochSecond,
                                            episodeNumber = 21,
                                            image = "https://cdn.example.com/mha-21.jpg",
                                            name = "Shiketsu High Lurking",
                                            overview = "The provisional license exam tightens across the final field.",
                                            productionCode = null,
                                            runtime = 24,
                                            seasonNumber = 3,
                                            tmdbId = null,
                                        ),
                                        Media.Category.Anime.ScheduleDetails.Episode(
                                            id = 22,
                                            airDate = Instant.now().minus(5, ChronoUnit.DAYS).epochSecond,
                                            episodeNumber = 22,
                                            image = "https://cdn.example.com/mha-22.jpg",
                                            name = "Bakugo's Answer",
                                            overview = "The fallout from the last duel reshapes the team's hierarchy.",
                                            productionCode = null,
                                            runtime = 24,
                                            seasonNumber = 3,
                                            tmdbId = null,
                                        ),
                                        Media.Category.Anime.ScheduleDetails.Episode(
                                            id = 23,
                                            airDate = Instant.now().plus(2, ChronoUnit.DAYS).epochSecond,
                                            episodeNumber = 23,
                                            image = "https://cdn.example.com/mha-23.jpg",
                                            name = "Full Cowl",
                                            overview = "The raid plan hits a breaking point as the next clash comes into view.",
                                            productionCode = null,
                                            runtime = 24,
                                            seasonNumber = 3,
                                            tmdbId = null,
                                        ),
                                    ),
                            ),
                    ),
                description =
                    "What would the world be like if 80 percent of the population manifested " +
                        "extraordinary superpowers called “Quirks” at age four? Heroes and villains would be battling it out " +
                        "everywhere! Becoming a hero would mean learning to use your power, but where would you go to study? U.A. " +
                        "High's Hero Program of course! But what would you do if you were one of the 20 percent who were born Quirkless?",
                genres =
                    listOf("Action", "Adventure", "Comedy", "Drama", "Fantasy", "Sci-Fi")
                        .mapIndexed { index, item -> Genre.Extended("", item, null, index.toLong()) },
                tags =
                    listOf("Super Power", "Super hero", "Shonen").mapIndexed { index, string ->
                        Tag.Extended(
                            id = index.toLong(),
                            name = string,
                            description = string,
                            category = "",
                            isGeneralSpoiler = false,
                            isAdult = false,
                            rank = index,
                            isMediaSpoiler = index == 1,
                            background = null,
                        )
                    },
                rankings =
                    listOf("highest rate", "top rated", "top rated").mapIndexed { index, value ->
                        MediaRank(
                            allTime = value == "highest rate" && index == 0,
                            context = value,
                            format = MediaFormat.TV,
                            rank = index,
                            season = MediaSeason.FALL,
                            type = if (value == "top rated") MediaRankType.RATED else MediaRankType.POPULAR,
                            year = 2011 + index,
                            id = index.toLong(),
                        )
                    },
                isFavourite = true,
                score =
                    MediaScore(
                        average = 69,
                        mean = 70,
                        personal = null,
                        popularity = 4_000,
                        trending = 800,
                    ),
                themes =
                    listOf(
                        MediaTheme(
                            mediaId = "1",
                            themeId = "op1",
                            name = "Odd Future",
                            audio = "https://example.com/audio.mp3",
                            video = "",
                            meta = MediaTheme.Meta(number = 1, type = "OP", version = 1),
                            variants =
                                listOf(
                                    MediaTheme.Variant(
                                        version = 1,
                                        episodes = "1-13",
                                        previews =
                                            listOf(
                                                MediaTheme.Preview(
                                                    video = "https://example.com/video-1080.webm",
                                                    audio = "https://example.com/audio.mp3",
                                                    resolution = 1080,
                                                    source = "BD",
                                                    tags = listOf("NC"),
                                                ),
                                            ),
                                    ),
                                    MediaTheme.Variant(
                                        version = 2,
                                        episodes = "14-25",
                                        previews =
                                            listOf(
                                                MediaTheme.Preview(
                                                    video = "https://example.com/video-720.webm",
                                                    audio = "https://example.com/audio-alt.mp3",
                                                    resolution = 720,
                                                    source = "WEB",
                                                ),
                                            ),
                                    ),
                                ),
                        ),
                        MediaTheme(
                            mediaId = "1",
                            themeId = "ed1",
                            name = "Update",
                            audio = null,
                            video = "https://example.com/video.mp4",
                            meta = MediaTheme.Meta(number = 1, type = "ED", version = 1),
                            variants =
                                listOf(
                                    MediaTheme.Variant(
                                        version = 1,
                                        episodes = "2-25",
                                        previews =
                                            listOf(
                                                MediaTheme.Preview(
                                                    video = "https://example.com/video.mp4",
                                                    audio = null,
                                                    resolution = 720,
                                                    source = "WEB",
                                                ),
                                            ),
                                    ),
                                ),
                        ),
                    ),
                ageRating = "PG-13",
                source = MediaSource.MANGA,
                twitterTag = "heroaca_anime",
                synonyms = listOf("My Hero Academia 3rd Season", "BNHA Season 3"),
                siteUrl = Media.SiteUrl(myAnimeList = "https://myanimelist.net/anime/36456"),
                mediaList =
                    MediaList.Core.empty().copy(
                        id = 100,
                        status = MediaListStatus.COMPLETED,
                        score = 8.3f,
                        privacy =
                            MediaListPrivacy(
                                isHidden = false,
                                isPrivate = false,
                                notes = "Good..",
                            ),
                    ),
            ),
        ),
) : PreviewParameterProvider<Media.Extended>
