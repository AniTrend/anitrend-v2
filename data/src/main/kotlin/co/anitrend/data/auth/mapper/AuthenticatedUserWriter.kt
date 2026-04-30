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
