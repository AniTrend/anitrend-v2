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

import co.anitrend.arch.data.converter.SupportConverter
import co.anitrend.arch.data.transformer.ISupportTransformer
import co.anitrend.data.media.entity.view.MediaStatsEntityView
import co.anitrend.domain.media.entity.MediaStats
import co.anitrend.domain.medialist.enums.MediaListStatus

internal class MediaStatsEntityConverter(
    override val fromType: (MediaStatsEntityView) -> MediaStats = ::transform,
    override val toType: (MediaStats) -> MediaStatsEntityView = { throw NotImplementedError() },
) : SupportConverter<MediaStatsEntityView, MediaStats>() {
    private companion object : ISupportTransformer<MediaStatsEntityView, MediaStats> {
        override fun transform(source: MediaStatsEntityView) =
            MediaStats(
                scoreDistribution =
                    source.scoreDistribution.map { distribution ->
                        MediaStats.ScoreDistribution(
                            amount = distribution.amount,
                            score = distribution.score,
                        )
                    },
                statusDistribution =
                    source.statusDistribution.map { distribution ->
                        MediaStats.StatusDistribution(
                            amount = distribution.amount,
                            status =
                                distribution.status?.let { value ->
                                    runCatching { MediaListStatus.valueOf(value) }.getOrNull()
                                },
                        )
                    },
            )
    }
}
