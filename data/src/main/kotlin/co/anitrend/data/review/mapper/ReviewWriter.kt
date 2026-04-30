/*
 * Copyright (C) 2026 AniTrend
 */
package co.anitrend.data.review.mapper

import co.anitrend.data.android.mapper.PersistEmbedded
import co.anitrend.data.review.datasource.local.ReviewLocalSource
import co.anitrend.data.review.entity.ReviewEntity

internal interface ReviewWriterContract {
    suspend fun persist(entity: ReviewEntity)

    suspend fun persist(entities: List<ReviewEntity>)
}

internal class ReviewWriter(
    private val mediaPersistence: PersistEmbedded,
    private val userPersistence: PersistEmbedded,
    private val localSource: ReviewLocalSource,
) : ReviewWriterContract {
    override suspend fun persist(entity: ReviewEntity) {
        mediaPersistence.persistEmbedded()
        localSource.upsert(entity)
    }

    override suspend fun persist(entities: List<ReviewEntity>) {
        mediaPersistence.persistEmbedded()
        userPersistence.persistEmbedded()
        localSource.upsert(entities)
    }
}
