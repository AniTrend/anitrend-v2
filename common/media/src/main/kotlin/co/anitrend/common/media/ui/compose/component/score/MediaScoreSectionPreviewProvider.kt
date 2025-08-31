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
package co.anitrend.common.media.ui.compose.component.score

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import co.anitrend.domain.media.entity.attribute.score.IMediaScore
import co.anitrend.domain.media.entity.attribute.score.MediaScore

internal data class MediaScoreSectionPreviewProvider(
    override val values: Sequence<IMediaScore> =
        sequenceOf(
            MediaScore(mean = 85, average = 86, personal = 9f, popularity = 4_700_000, trending = 9_000),
            MediaScore(mean = 39, average = 46, personal = 2.5f, popularity = 300_000, trending = 2_300),
            MediaScore(mean = 85, average = 86, personal = null, popularity = 20_000, trending = 2_300),
            MediaScore(mean = 20, average = 40, personal = null, popularity = null, trending = 2_300),
            MediaScore(mean = 5, average = 6, personal = null, popularity = null, trending = null),
        ),
) : PreviewParameterProvider<IMediaScore>
