/*
 * Copyright (C) 2026 AniTrend
 */
package co.anitrend.data.user.mapper

import co.anitrend.data.android.mapper.PersistEmbedded
import co.anitrend.data.user.converter.UserStatisticPayload

internal fun interface UserStatisticPersistenceWriterContract {
    suspend fun persist(payload: UserStatisticPayload)
}

internal class UserStatisticPersistenceWriter(
    private val userPersistence: PersistEmbedded,
    private val statisticWriter: UserStatisticWriterContract,
) : UserStatisticPersistenceWriterContract {
    override suspend fun persist(payload: UserStatisticPayload) {
        userPersistence.persistEmbedded()
        statisticWriter.persist(payload)
    }
}
