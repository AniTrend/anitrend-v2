/*
 * Copyright (C) 2026 AniTrend
 */
package co.anitrend.data.airing.mapper

import co.anitrend.data.airing.datasource.local.AiringLocalSource
import co.anitrend.data.airing.entity.AiringScheduleEntity
import co.anitrend.data.android.mapper.PersistEmbedded

internal fun interface AiringWriterContract {
    suspend fun persist(entities: List<AiringScheduleEntity>)
}

internal class AiringWriter(
    private val mediaPersistence: PersistEmbedded,
    private val localSource: AiringLocalSource,
) : AiringWriterContract {
    override suspend fun persist(entities: List<AiringScheduleEntity>) {
        mediaPersistence.persistEmbedded()
        localSource.upsert(entities)
    }
}
