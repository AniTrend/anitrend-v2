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
package co.anitrend.common.media.ui.compose.component.rank

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import co.anitrend.domain.media.entity.attribute.rank.IMediaRank
import co.anitrend.domain.media.entity.attribute.rank.MediaRank
import co.anitrend.domain.media.enums.MediaFormat
import co.anitrend.domain.media.enums.MediaRankType
import co.anitrend.domain.media.enums.MediaSeason

internal data class MediaRankPreviewProvider(
    override val values: Sequence<IMediaRank> =
        sequenceOf(
            MediaRank(
                id = 1L,
                allTime = true,
                context = "highest rated",
                format = MediaFormat.TV,
                rank = 16,
                season = null,
                type = MediaRankType.RATED,
                year = 2025,
            ),
            MediaRank(
                id = 2L,
                allTime = null,
                context = "most popular",
                format = MediaFormat.TV,
                rank = 36,
                season = MediaSeason.SUMMER,
                type = MediaRankType.POPULAR,
                year = 2025,
            ),
            MediaRank(
                id = 3L,
                allTime = null,
                context = "highest rated",
                format = MediaFormat.MUSIC,
                rank = 48,
                season = null,
                type = MediaRankType.RATED,
                year = null,
            ),
        ),
) : PreviewParameterProvider<IMediaRank>
