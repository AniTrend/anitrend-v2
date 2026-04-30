/*
 * Copyright (C) 2026 AniTrend
 */
package co.anitrend.data.auth.mapper

import co.anitrend.data.android.mapper.PersistEmbedded
import co.anitrend.data.user.mapper.UserMapper
import co.anitrend.data.user.settings.IUserSettings

internal fun interface UserGeneralOptionWriterContract {
    suspend fun persist()
}

internal fun interface UserMediaOptionWriterContract {
    suspend fun persist()
}

internal typealias NotificationPersistence = PersistEmbedded

internal class UserGeneralOptionWriter(
    private val settings: IUserSettings,
    private val mapper: UserMapper.GeneralOptionEmbed,
) : UserGeneralOptionWriterContract {
    override suspend fun persist() {
        mapper.persistEmbedded(settings)
    }
}

internal class UserMediaOptionWriter(
    private val settings: IUserSettings,
    private val mapper: UserMapper.MediaOptionEmbed,
) : UserMediaOptionWriterContract {
    override suspend fun persist() {
        mapper.persistEmbedded(settings)
    }
}
