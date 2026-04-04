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
package co.anitrend.data.media.mapper

import co.anitrend.data.android.mapper.DefaultMapper
import co.anitrend.data.media.model.container.MediaSidecarModelContainer
import co.anitrend.data.studio.converter.StudioConverter
import co.anitrend.domain.media.entity.MediaStats
import co.anitrend.domain.media.entity.MediaStudioEntry

internal sealed class MediaSidecarMapper<S, D> : DefaultMapper<S, D>() {
    class Studios(
        private val converter: StudioConverter,
    ) : MediaSidecarMapper<MediaSidecarModelContainer.Studios, List<MediaStudioEntry>>() {
        override suspend fun persist(data: List<MediaStudioEntry>) {
        }

        override suspend fun onResponseMapFrom(source: MediaSidecarModelContainer.Studios): List<MediaStudioEntry> =
            source
                .media
                ?.studios
                ?.edges
                .orEmpty()
                .mapNotNull { edge ->
                    val studio = edge.node ?: return@mapNotNull null

                    MediaStudioEntry(
                        studio = converter.convertFrom(studio),
                        isMain = edge.isMain,
                        id = edge.id.toLong(),
                    )
                }
    }

    class Stats : MediaSidecarMapper<MediaSidecarModelContainer.Stats, MediaStats>() {
        override suspend fun persist(data: MediaStats) {
        }

        override suspend fun onResponseMapFrom(source: MediaSidecarModelContainer.Stats): MediaStats {
            val stats = source.media?.stats ?: return MediaStats.empty()

            return MediaStats(
                scoreDistribution =
                    stats.scoreDistribution
                        .orEmpty()
                        .mapNotNull { distribution ->
                            val amount = distribution.amount ?: return@mapNotNull null
                            val score = distribution.score ?: return@mapNotNull null

                            MediaStats.ScoreDistribution(
                                amount = amount,
                                score = score,
                            )
                        },
                statusDistribution =
                    stats.statusDistribution
                        .orEmpty()
                        .mapNotNull { distribution ->
                            val amount = distribution.amount ?: return@mapNotNull null

                            MediaStats.StatusDistribution(
                                amount = amount,
                                status = distribution.status,
                            )
                        },
            )
        }
    }
}
