/*
 * Copyright (C) 2026 AniTrend
 */
package co.anitrend.data.media.mapper

import co.anitrend.data.media.datasource.local.MediaStatsLocalSource

internal fun interface MediaStatsWriterContract {
    suspend fun persist(payload: MediaStatsMapper.Payload)
}

internal class MediaStatsWriter(
    private val localSource: MediaStatsLocalSource,
) : MediaStatsWriterContract {
    override suspend fun persist(payload: MediaStatsMapper.Payload) {
        localSource.upsert(payload.stats)
        localSource.upsertScoreDistributions(payload.scoreDistribution)
        localSource.upsertStatusDistributions(payload.statusDistribution)
    }
}
