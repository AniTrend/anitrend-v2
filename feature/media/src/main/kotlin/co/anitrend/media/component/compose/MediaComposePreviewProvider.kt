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
import co.anitrend.domain.common.entity.shared.FuzzyDate
import co.anitrend.domain.genre.entity.Genre
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.media.entity.attribute.image.MediaImage
import co.anitrend.domain.media.entity.attribute.rank.MediaRank
import co.anitrend.domain.media.entity.attribute.score.MediaScore
import co.anitrend.domain.media.entity.attribute.title.MediaTitle
import co.anitrend.domain.media.entity.contract.IMedia
import co.anitrend.domain.media.enums.MediaFormat
import co.anitrend.domain.media.enums.MediaRankType
import co.anitrend.domain.media.enums.MediaSeason
import co.anitrend.domain.media.enums.MediaStatus
import co.anitrend.domain.medialist.entity.MediaList
import co.anitrend.domain.medialist.entity.contract.MediaListPrivacy
import co.anitrend.domain.medialist.enums.MediaListStatus
import co.anitrend.domain.tag.entity.Tag

internal data class MediaComposePreviewProvider(
    override val values: Sequence<IMedia> =
        sequenceOf(
            Media.Extended.empty().copy(
                title =
                    MediaTitle(
                        userPreferred = "Boku no Hero Academia 3",
                        english = "My Hero Academia Season 3",
                        romaji = "Boku no Hero Academia 3",
                        native = "僕のヒーローアカデミア 3",
                    ),
                status = MediaStatus.FINISHED,
                image = MediaImage.empty().copy(color = "#e4a15d"),
                startDate = FuzzyDate.empty().copy(2018),
                format = MediaFormat.TV,
                category =
                    Media.Category.Anime
                        .empty()
                        .copy(25),
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
) : PreviewParameterProvider<IMedia>
