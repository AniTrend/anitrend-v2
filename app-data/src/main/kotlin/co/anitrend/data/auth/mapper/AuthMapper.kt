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

import  co.anitrend.data.android.mapper.DefaultMapper
import co.anitrend.data.user.converter.UserGeneralOptionModelConverter
import co.anitrend.data.user.converter.UserMediaOptionModelConverter
import co.anitrend.data.user.converter.UserModelConverter
import co.anitrend.data.user.datasource.local.UserLocalSource
import co.anitrend.data.user.entity.UserEntity
import co.anitrend.data.user.entity.option.UserGeneralOptionEntity
import co.anitrend.data.user.entity.option.UserMediaOptionEntity
import co.anitrend.data.user.model.container.UserModelContainer
import co.anitrend.data.user.settings.IUserSettings

internal class AuthMapper(
    private val settings: IUserSettings,
    private val userLocalSource: UserLocalSource,
    private val converter: UserModelConverter,
    private val generalOptionConverter: UserGeneralOptionModelConverter,
    private val mediaOptionConverter: UserMediaOptionModelConverter,
) : DefaultMapper<UserModelContainer.Viewer, UserEntity>() {

    private var generalOption: UserGeneralOptionEntity? = null
    private var mediaOption: UserMediaOptionEntity? = null

    private fun persistUserSettings() {
        generalOption?.also {
            settings.titleLanguage.value = it.titleLanguage
        }
        mediaOption?.also {
            settings.scoreFormat.value = it.scoreFormat
        }
        generalOption = null
        mediaOption = null
    }

    /**
     * Save [data] into your desired local source
     */
    override suspend fun persist(data: UserEntity) {
        userLocalSource.upsertWithOptions(data, generalOption, mediaOption)
        persistUserSettings()
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
        generalOption = generalOptionConverter.convertFrom(source.user)
        mediaOption = mediaOptionConverter.convertFrom(source.user)

        return converter.convertFrom(source.user)
    }
}