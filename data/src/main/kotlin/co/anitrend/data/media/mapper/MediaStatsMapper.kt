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

import co.anitrend.data.android.database.common.TransactionRunner
import co.anitrend.data.android.mapper.DefaultMapper
import co.anitrend.data.media.datasource.local.MediaStatsLocalSource
import co.anitrend.data.media.entity.MediaStatsEntity
import co.anitrend.data.media.entity.stats.MediaScoreDistributionEntity
import co.anitrend.data.media.entity.stats.MediaStatusDistributionEntity
import co.anitrend.data.media.model.container.MediaSidecarModelContainer

internal class MediaStatsMapper(
    private val localSource: MediaStatsLocalSource,
    private val transactionRunner: TransactionRunner,
) : DefaultMapper<MediaSidecarModelContainer.Stats, MediaStatsMapper.Payload?>() {
    internal data class Payload(
        val stats: MediaStatsEntity,
        val scoreDistribution: List<MediaScoreDistributionEntity>,
        val statusDistribution: List<MediaStatusDistributionEntity>,
    )

    override suspend fun persist(data: Payload?) {
        if (data != null) {
            localSource.upsert(data.stats)
            localSource.upsertScoreDistributions(data.scoreDistribution)
            localSource.upsertStatusDistributions(data.statusDistribution)
        }
    }

    override suspend fun onResponseDatabaseInsert(mappedData: Payload?) {
        transactionRunner.run {
            super.onResponseDatabaseInsert(mappedData)
        }
    }

    override suspend fun onResponseMapFrom(source: MediaSidecarModelContainer.Stats): Payload? {
        val media = source.media ?: return null
        val mediaId = media.id ?: return null
        val stats = media.stats ?: return null

        return Payload(
            stats = MediaStatsEntity(id = mediaId),
            scoreDistribution =
                stats.scoreDistribution.orEmpty().mapNotNull { distribution ->
                    val amount = distribution.amount ?: return@mapNotNull null
                    val score = distribution.score ?: return@mapNotNull null

                    MediaScoreDistributionEntity(
                        amount = amount,
                        score = score,
                        mediaId = mediaId,
                    )
                },
            statusDistribution =
                stats.statusDistribution.orEmpty().mapNotNull { distribution ->
                    val amount = distribution.amount ?: return@mapNotNull null

                    MediaStatusDistributionEntity(
                        amount = amount,
                        status = distribution.status?.name,
                        mediaId = mediaId,
                    )
                },
        )
    }
}
