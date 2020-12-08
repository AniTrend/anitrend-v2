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

import co.anitrend.data.arch.mapper.DefaultMapper
import co.anitrend.data.arch.railway.OutCome
import co.anitrend.data.arch.railway.extension.evaluate
import co.anitrend.data.arch.railway.extension.otherwise
import co.anitrend.data.arch.railway.extension.then
import co.anitrend.data.user.converter.UserGeneralOptionModelConverter
import co.anitrend.data.user.converter.UserMediaOptionModelConverter
import co.anitrend.data.user.converter.UserModelConverter
import co.anitrend.data.user.datasource.local.UserLocalSource
import co.anitrend.data.user.entity.UserEntity
import co.anitrend.data.user.entity.option.UserGeneralOptionEntity
import co.anitrend.data.user.entity.option.UserMediaOptionEntity
import co.anitrend.data.user.model.remote.container.UserViewerModelContainer
import co.anitrend.data.user.settings.IUserSettings

internal class AuthMapper(
    private val settings: IUserSettings,
    private val userLocalSource: UserLocalSource,
    private val converter: UserModelConverter,
    private val generalOptionConverter: UserGeneralOptionModelConverter,
    private val mediaOptionConverter: UserMediaOptionModelConverter,
) : DefaultMapper<UserViewerModelContainer, UserEntity>() {

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
     * Handles the persistence of [data] into a local source
     *
     * @return [OutCome.Pass] or [OutCome.Fail] of the operation
     */
    override suspend fun persistChanges(data: UserEntity): OutCome<Nothing?> {
        return runCatching {
            userLocalSource.upsertWithOptions(data, generalOption, mediaOption)
            persistUserSettings()
            OutCome.Pass(null)
        }.getOrElse { OutCome.Fail(listOf(it)) }
    }

    /**
     * Inserts the given object into the implemented room database,
     *
     * @param mappedData mapped object from [onResponseMapFrom] to insert into the database
     */
    override suspend fun onResponseDatabaseInsert(mappedData: UserEntity) {
        mappedData evaluate
                ::checkValidity then
                ::persistChanges otherwise
                ::handleException
    }

    /**
     * Creates mapped objects and handles the database operations which may be required to map various objects,
     *
     * @param source the incoming data source type
     * @return mapped object that will be consumed by [onResponseDatabaseInsert]
     */
    override suspend fun onResponseMapFrom(
        source: UserViewerModelContainer
    ): UserEntity {
        generalOption = generalOptionConverter.convertFrom(source.user)
        mediaOption = mediaOptionConverter.convertFrom(source.user)

        return converter.convertFrom(source.user)
    }
}