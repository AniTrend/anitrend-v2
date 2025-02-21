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
package co.anitrend.media.carousel.component.compose

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import co.anitrend.domain.common.entity.shared.FuzzyDate
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.media.entity.attribute.image.MediaImage
import co.anitrend.domain.media.entity.attribute.title.MediaTitle
import co.anitrend.domain.media.enums.MediaFormat
import co.anitrend.domain.media.enums.MediaStatus

internal class MediaPreviewProvider : PreviewParameterProvider<Media> {
    /**
     * [Sequence] of values of type [T] to be passed as @[Preview] parameter.
     */
    override val values: Sequence<Media>
        get() = PreviewData
}

internal val PreviewData =
    sequenceOf(
        Media.Core.empty().copy(
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
        ),
        Media.Core.empty().copy(
            title =
                MediaTitle(
                    userPreferred = "Sousou no Frieren",
                    english = "Frieren: Beyond Journey’s End",
                    romaji = "Sousou no Frieren",
                    native = "葬送のフリーレン",
                ),
            status = MediaStatus.RELEASING,
            image = MediaImage.empty().copy(color = "#d61a1a"),
            startDate = FuzzyDate.empty().copy(2023),
            format = MediaFormat.TV,
            category =
                Media.Category.Anime
                    .empty()
                    .copy(28),
        ),
    )
