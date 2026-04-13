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
package co.anitrend.common.media.ui.compose.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import co.anitrend.domain.airing.entity.AiringSchedule
import co.anitrend.domain.common.entity.shared.FuzzyDate
import co.anitrend.domain.genre.entity.Genre
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.media.entity.attribute.image.MediaImage
import co.anitrend.domain.media.entity.attribute.score.MediaScore
import co.anitrend.domain.media.entity.attribute.title.MediaTitle
import co.anitrend.domain.media.enums.MediaFormat
import co.anitrend.domain.media.enums.MediaStatus

private fun previewMedia(
    id: Long,
    preferredTitle: String,
    secondaryTitle: String,
    color: String,
    status: MediaStatus,
    year: Int,
    episodes: Int,
    genres: List<Pair<String, String?>>,
): Media =
    Media.Core.empty().copy(
        id = id,
        title =
            MediaTitle(
                userPreferred = preferredTitle,
                english = preferredTitle,
                romaji = secondaryTitle,
                native = secondaryTitle,
            ),
        status = status,
        image = MediaImage.empty().copy(color = color),
        score =
            MediaScore(
                mean = 86,
                average = 84,
                personal = null,
                popularity = 120_000,
                trending = 8_500,
            ),
        startDate = FuzzyDate(year = year, month = 10, day = 1),
        format = MediaFormat.TV,
        category =
            Media.Category.Anime(
                episodes = episodes,
                duration = 24,
                broadcast = "Sundays at 18:00",
                premiered = "Fall $year",
                schedule =
                    if (status == MediaStatus.RELEASING) {
                        AiringSchedule(
                            airingAt = 1_735_516_800L,
                            episode = 3,
                            mediaId = id,
                            timeUntilAiring = 86_400L,
                            id = id,
                        )
                    } else {
                        null
                    },
            ),
        genres =
            genres.mapIndexed { index, (name, emoji) ->
                Genre.Extended(
                    background = null,
                    name = name,
                    emoji = emoji,
                    id = index.toLong(),
                )
            },
    )

val mediaPreviewItems: List<Media> =
    listOf(
        previewMedia(
            id = 28,
            preferredTitle = "Sousou no Frieren",
            secondaryTitle = "Frieren: Beyond Journey's End",
            color = "#d4a853",
            status = MediaStatus.RELEASING,
            year = 2023,
            episodes = 28,
            genres =
                listOf(
                    "Adventure" to null,
                    "Drama" to null,
                    "Fantasy" to null,
                ),
        ),
        previewMedia(
            id = 25,
            preferredTitle = "Boku no Hero Academia 3",
            secondaryTitle = "My Hero Academia Season 3",
            color = "#e46c4e",
            status = MediaStatus.FINISHED,
            year = 2018,
            episodes = 25,
            genres =
                listOf(
                    "Action" to null,
                    "Comedy" to null,
                    "Super Power" to null,
                ),
        ),
        previewMedia(
            id = 12,
            preferredTitle = "Blue Box",
            secondaryTitle = "Ao no Hako",
            color = "#4f88e8",
            status = MediaStatus.RELEASING,
            year = 2024,
            episodes = 24,
            genres =
                listOf(
                    "Romance" to null,
                    "Sports" to null,
                    "Slice of Life" to null,
                ),
        ),
        previewMedia(
            id = 9,
            preferredTitle = "Mob Psycho 100 II",
            secondaryTitle = "Mob Psycho 100 II",
            color = "#7b5cff",
            status = MediaStatus.FINISHED,
            year = 2019,
            episodes = 13,
            genres =
                listOf(
                    "Action" to null,
                    "Comedy" to null,
                    "Psychological" to null,
                ),
        ),
    )

class MediaPreviewProvider(
    override val values: Sequence<Media> = mediaPreviewItems.asSequence(),
) : PreviewParameterProvider<Media>
