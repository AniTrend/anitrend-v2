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
import co.anitrend.data.media.datasource.local.MediaStatsLocalSource
import co.anitrend.data.media.entity.MediaStatsEntity
import co.anitrend.data.media.model.container.MediaSidecarModelContainer

internal class MediaStatsMapper(
    private val localSource: MediaStatsLocalSource,
) : DefaultMapper<MediaSidecarModelContainer.Stats, MediaStatsEntity?>() {
    override suspend fun persist(data: MediaStatsEntity?) {
        if (data != null) {
            localSource.upsert(data)
        }
    }

    override suspend fun onResponseMapFrom(source: MediaSidecarModelContainer.Stats): MediaStatsEntity? {
        val media = source.media ?: return null
        val mediaId = media.id ?: return null
        val stats = media.stats ?: return null

        return MediaStatsEntity(
            id = mediaId,
            scoreDistribution =
                stats.scoreDistribution.orEmpty().mapNotNull { distribution ->
                    val amount = distribution.amount ?: return@mapNotNull null
                    val score = distribution.score ?: return@mapNotNull null

                    MediaStatsEntity.ScoreDistribution(
                        amount = amount,
                        score = score,
                    )
                },
            statusDistribution =
                stats.statusDistribution.orEmpty().mapNotNull { distribution ->
                    val amount = distribution.amount ?: return@mapNotNull null

                    MediaStatsEntity.StatusDistribution(
                        amount = amount,
                        status = distribution.status?.name,
                    )
                },
        )
    }
}
