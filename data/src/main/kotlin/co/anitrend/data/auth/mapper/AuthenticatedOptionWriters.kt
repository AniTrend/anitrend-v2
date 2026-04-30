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
