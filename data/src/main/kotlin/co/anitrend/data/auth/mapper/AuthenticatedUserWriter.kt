/*
 * Copyright (C) 2026 AniTrend
 */
package co.anitrend.data.auth.mapper

import co.anitrend.data.user.entity.UserEntity
import co.anitrend.data.user.datasource.local.UserLocalSource

internal fun interface AuthenticatedUserWriterContract {
    suspend fun persist(user: UserEntity)
}

internal class AuthenticatedUserWriter(
    private val generalOptionWriter: UserGeneralOptionWriterContract,
    private val mediaOptionWriter: UserMediaOptionWriterContract,
    private val notificationMapper: NotificationPersistence,
    private val localSource: UserLocalSource,
) : AuthenticatedUserWriterContract {
    override suspend fun persist(user: UserEntity) {
        localSource.upsert(user)
        generalOptionWriter.persist()
        mediaOptionWriter.persist()
        notificationMapper.persistEmbedded()
    }
}
