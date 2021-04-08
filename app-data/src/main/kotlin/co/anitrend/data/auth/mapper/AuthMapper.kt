/*
 * Copyright (C) 2020  AniTrend
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

import co.anitrend.data.android.extensions.runInTransaction
import co.anitrend.data.android.mapper.DefaultMapper
import co.anitrend.data.user.converter.UserModelConverter
import co.anitrend.data.user.datasource.local.UserLocalSource
import co.anitrend.data.user.entity.UserEntity
import co.anitrend.data.user.mapper.UserMapper
import co.anitrend.data.user.model.container.UserModelContainer
import co.anitrend.data.user.settings.IUserSettings

internal class AuthMapper(
    private val settings: IUserSettings,
    private val generalOptionMapper: UserMapper.GeneralOptionEmbed,
    private val mediaOptionMapper: UserMapper.MediaOptionEmbed,
    private val localSource: UserLocalSource,
    private val converter: UserModelConverter
) : DefaultMapper<UserModelContainer.Viewer, UserEntity>() {

    /**
     * Save [data] into your desired local source
     */
    override suspend fun persist(data: UserEntity) {
        runInTransaction {
            localSource.upsert(data)
            generalOptionMapper.persistEmbedded(settings)
            mediaOptionMapper.persistEmbedded(settings)
        }
    }

    /**
     * Creates mapped objects and handles the database operations which may be required to map various objects,
     *
     * @param source the incoming data source type
     * @return mapped object that will be consumed by [onResponseDatabaseInsert]
     */
    override suspend fun onResponseMapFrom(
        source: UserModelContainer.Viewer
    ): UserEntity {
        generalOptionMapper.onEmbedded(source.user)
        mediaOptionMapper.onEmbedded(source.user)
        return converter.convertFrom(source.user)
    }
}