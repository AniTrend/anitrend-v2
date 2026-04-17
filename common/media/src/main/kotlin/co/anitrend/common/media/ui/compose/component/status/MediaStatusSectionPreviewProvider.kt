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
package co.anitrend.common.media.ui.compose.component.status

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import co.anitrend.domain.airing.entity.AiringSchedule
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.media.entity.contract.IMedia
import co.anitrend.domain.media.enums.MediaStatus
import org.threeten.bp.Instant
import org.threeten.bp.temporal.ChronoUnit

data class MediaStatusSectionPreviewProvider(
    override val values: Sequence<IMedia> =
        sequenceOf(
            Media.Extended.empty().copy(
                status = MediaStatus.FINISHED,
                category =
                    Media.Category.Manga(
                        chapters = 64,
                        volumes = 8,
                    ),
            ),
            Media.Extended.empty().copy(
                status = MediaStatus.RELEASING,
                category =
                    Media.Category.Anime(
                        episodes = 12,
                        duration = 24,
                        broadcast = "Fridays 17:00 (JST)",
                        premiered = "Spring 2025",
                        schedule =
                            AiringSchedule(
                                airingAt = Instant.now().plus(2, ChronoUnit.HOURS).epochSecond,
                                episode = 10,
                                mediaId = 1,
                                timeUntilAiring = 62811,
                                id = 1,
                            ),
                        scheduleDetails =
                            Media.Category.Anime.ScheduleDetails(
                                airedEpisodes = 9,
                                firstAirDate = Instant.now().minus(70, ChronoUnit.DAYS).epochSecond,
                                lastAirDate = Instant.now().minus(5, ChronoUnit.DAYS).epochSecond,
                                nextEpisode =
                                    Media.Category.Anime.ScheduleDetails.Episode(
                                        id = 10,
                                        airDate = Instant.now().plus(2, ChronoUnit.DAYS).epochSecond,
                                        episodeNumber = 10,
                                        image = "https://cdn.example.com/episode-10.jpg",
                                        name = "The Last Bell",
                                        overview = "Class 1-A scrambles to regroup before the next raid begins.",
                                        productionCode = null,
                                        runtime = 24,
                                        seasonNumber = 1,
                                        tmdbId = null,
                                    ),
                                lastEpisode =
                                    Media.Category.Anime.ScheduleDetails.Episode(
                                        id = 9,
                                        airDate = Instant.now().minus(5, ChronoUnit.DAYS).epochSecond,
                                        episodeNumber = 9,
                                        image = "https://cdn.example.com/episode-9.jpg",
                                        name = "A New Resolve",
                                        overview = "The team regroups after the evacuation order fractures their plan.",
                                        productionCode = null,
                                        runtime = 24,
                                        seasonNumber = 1,
                                        tmdbId = null,
                                    ),
                                episodes =
                                    listOf(
                                        Media.Category.Anime.ScheduleDetails.Episode(
                                            id = 8,
                                            airDate = Instant.now().minus(12, ChronoUnit.DAYS).epochSecond,
                                            episodeNumber = 8,
                                            image = "https://cdn.example.com/episode-8.jpg",
                                            name = "Through the Static",
                                            overview = "A rescue run forces the squad to split across the city grid.",
                                            productionCode = null,
                                            runtime = 24,
                                            seasonNumber = 1,
                                            tmdbId = null,
                                        ),
                                        Media.Category.Anime.ScheduleDetails.Episode(
                                            id = 9,
                                            airDate = Instant.now().minus(5, ChronoUnit.DAYS).epochSecond,
                                            episodeNumber = 9,
                                            image = "https://cdn.example.com/episode-9.jpg",
                                            name = "A New Resolve",
                                            overview = "The team regroups after the evacuation order fractures their plan.",
                                            productionCode = null,
                                            runtime = 24,
                                            seasonNumber = 1,
                                            tmdbId = null,
                                        ),
                                        Media.Category.Anime.ScheduleDetails.Episode(
                                            id = 10,
                                            airDate = Instant.now().plus(2, ChronoUnit.DAYS).epochSecond,
                                            episodeNumber = 10,
                                            image = "https://cdn.example.com/episode-10.jpg",
                                            name = "The Last Bell",
                                            overview = "Class 1-A scrambles to regroup before the next raid begins.",
                                            productionCode = null,
                                            runtime = 24,
                                            seasonNumber = 1,
                                            tmdbId = null,
                                        ),
                                    ),
                            ),
                    ),
            ),
            Media.Extended.empty().copy(
                status = MediaStatus.HIATUS,
                category =
                    Media.Category.Anime(
                        episodes = 12,
                        duration = 24,
                        broadcast = "",
                        premiered = "",
                        schedule = null,
                        scheduleDetails =
                            Media.Category.Anime.ScheduleDetails(
                                airedEpisodes = 7,
                                firstAirDate = Instant.now().minus(90, ChronoUnit.DAYS).epochSecond,
                                lastAirDate = Instant.now().minus(14, ChronoUnit.DAYS).epochSecond,
                                lastEpisode =
                                    Media.Category.Anime.ScheduleDetails.Episode(
                                        id = 7,
                                        airDate = Instant.now().minus(14, ChronoUnit.DAYS).epochSecond,
                                        episodeNumber = 7,
                                        image = "https://cdn.example.com/hiatus-7.jpg",
                                        name = "After the Rain",
                                        overview = "The mid-season cliffhanger lands just as the production pause begins.",
                                        productionCode = null,
                                        runtime = 24,
                                        seasonNumber = 1,
                                        tmdbId = null,
                                    ),
                                episodes =
                                    listOf(
                                        Media.Category.Anime.ScheduleDetails.Episode(
                                            id = 7,
                                            airDate = Instant.now().minus(14, ChronoUnit.DAYS).epochSecond,
                                            episodeNumber = 7,
                                            image = "https://cdn.example.com/hiatus-7.jpg",
                                            name = "After the Rain",
                                            overview = "The mid-season cliffhanger lands just as the production pause begins.",
                                            productionCode = null,
                                            runtime = 24,
                                            seasonNumber = 1,
                                            tmdbId = null,
                                        ),
                                    ),
                            ),
                    ),
            ),
            Media.Extended.empty().copy(
                status = MediaStatus.RELEASING,
                category =
                    Media.Category.Anime(
                        episodes = 0,
                        duration = 24,
                        broadcast = "Sundays 21:00 (JST)",
                        premiered = "Winter 2025",
                        schedule = null,
                        scheduleDetails =
                            Media.Category.Anime.ScheduleDetails(
                                airedEpisodes = 3,
                                firstAirDate = Instant.now().minus(28, ChronoUnit.DAYS).epochSecond,
                                lastAirDate = Instant.now().minus(4, ChronoUnit.DAYS).epochSecond,
                                lastEpisode =
                                    Media.Category.Anime.ScheduleDetails.Episode(
                                        id = 3,
                                        airDate = Instant.now().minus(4, ChronoUnit.DAYS).epochSecond,
                                        episodeNumber = 3,
                                        image = null,
                                        name = "Signal Lost",
                                        overview = null,
                                        productionCode = null,
                                        runtime = 24,
                                        seasonNumber = 1,
                                        tmdbId = null,
                                    ),
                                episodes =
                                    listOf(
                                        Media.Category.Anime.ScheduleDetails.Episode(
                                            id = 1,
                                            airDate = Instant.now().minus(18, ChronoUnit.DAYS).epochSecond,
                                            episodeNumber = 1,
                                            image = null,
                                            name = "Wake",
                                            overview = null,
                                            productionCode = null,
                                            runtime = 24,
                                            seasonNumber = 1,
                                            tmdbId = null,
                                        ),
                                        Media.Category.Anime.ScheduleDetails.Episode(
                                            id = 2,
                                            airDate = Instant.now().minus(11, ChronoUnit.DAYS).epochSecond,
                                            episodeNumber = 2,
                                            image = null,
                                            name = "The Split",
                                            overview = null,
                                            productionCode = null,
                                            runtime = 24,
                                            seasonNumber = 1,
                                            tmdbId = null,
                                        ),
                                        Media.Category.Anime.ScheduleDetails.Episode(
                                            id = 3,
                                            airDate = Instant.now().minus(4, ChronoUnit.DAYS).epochSecond,
                                            episodeNumber = 3,
                                            image = null,
                                            name = "Signal Lost",
                                            overview = null,
                                            productionCode = null,
                                            runtime = 24,
                                            seasonNumber = 1,
                                            tmdbId = null,
                                        ),
                                    ),
                            ),
                    ),
            ),
            Media.Extended.empty().copy(
                status = MediaStatus.CANCELLED,
                category =
                    Media.Category.Anime(
                        episodes = 12,
                        duration = 24,
                        broadcast = "",
                        premiered = "",
                        schedule = null,
                    ),
            ),
        ),
) : PreviewParameterProvider<IMedia>
