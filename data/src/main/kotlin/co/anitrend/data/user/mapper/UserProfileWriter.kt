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
