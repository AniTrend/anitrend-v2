/*
 * Copyright (C) 2026 AniTrend
 */
package co.anitrend.data.user.mapper

import co.anitrend.data.user.datasource.local.UserLocalSource
import co.anitrend.data.user.entity.UserEntity

internal fun interface UserProfileWriterContract {
    suspend fun persist(user: UserEntity)
}

internal class UserProfileWriter(
    private val localSource: UserLocalSource,
    private val generalOptionMapper: UserMapper.GeneralOptionEmbed,
    private val mediaOptionMapper: UserMapper.MediaOptionEmbed,
    private val previousNameMapper: UserMapper.PreviousNameEmbed,
) : UserProfileWriterContract {
    override suspend fun persist(user: UserEntity) {
        localSource.upsert(user)
        generalOptionMapper.persistEmbedded()
        mediaOptionMapper.persistEmbedded()
        previousNameMapper.persistEmbedded()
    }
}
