/*
 * Copyright (C) 2021  AniTrend
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

import co.anitrend.data.android.mapper.DefaultMapper
import co.anitrend.data.android.mapper.EmbedMapper
import co.anitrend.data.user.converter.UserGeneralOptionModelConverter
import co.anitrend.data.user.converter.UserMediaOptionModelConverter
import co.anitrend.data.user.converter.UserModelConverter
import co.anitrend.data.user.converter.UserStatisticModelConverter
import co.anitrend.data.user.datasource.local.UserLocalSource
import co.anitrend.data.user.datasource.local.option.UserGeneralOptionLocalSource
import co.anitrend.data.user.datasource.local.option.UserMediaOptionLocalSource
import co.anitrend.data.user.datasource.local.statistic.UserStatisticLocalSource
import co.anitrend.data.user.entity.UserEntity
import co.anitrend.data.user.entity.option.UserGeneralOptionEntity
import co.anitrend.data.user.entity.option.UserMediaOptionEntity
import co.anitrend.data.user.entity.statistic.UserWithStatisticEntity
import co.anitrend.data.user.model.UserModel
import co.anitrend.data.user.model.container.UserModelContainer
import co.anitrend.data.user.settings.IUserSettings

internal sealed class UserMapper<S, D> : DefaultMapper<S, D>() {

    protected abstract val localSource: UserLocalSource
    protected abstract val converter: UserModelConverter

    class Paged(
        override val localSource: UserLocalSource,
        override val converter: UserModelConverter
    ) : UserMapper<UserModelContainer.Paged, List<UserEntity>>() {

        /**
         * Save [data] into your desired local source
         */
        override suspend fun persist(data: List<UserEntity>) {
            localSource.upsert(data)
        }

        /**
         * Creates mapped objects and handles the database operations which may be required to map various objects,
         *
         * @param source the incoming data source type
         * @return mapped object that will be consumed by [onResponseDatabaseInsert]
         */
        override suspend fun onResponseMapFrom(
            source: UserModelContainer.Paged
        ) = converter.convertFrom(source.page.userList)
    }

    class Profile(
        private val generalOptionMapper: GeneralOptionEmbed,
        private val mediaOptionMapper: MediaOptionEmbed,
        override val localSource: UserLocalSource,
        override val converter: UserModelConverter
    ) : UserMapper<UserModelContainer.Profile, UserEntity>() {


        /**
         * Save [data] into your desired local source
         */
        override suspend fun persist(data: UserEntity) {
            localSource.upsert(data)
            generalOptionMapper.persistEmbedded()
            mediaOptionMapper.persistEmbedded()
        }

        /**
         * Creates mapped objects and handles the database operations which may be required to map various objects,
         *
         * @param source the incoming data source type
         * @return mapped object that will be consumed by [onResponseDatabaseInsert]
         */
        override suspend fun onResponseMapFrom(
            source: UserModelContainer.Profile
        ): UserEntity {
            generalOptionMapper.onEmbedded(source.user)
            mediaOptionMapper.onEmbedded(source.user)
            return converter.convertFrom(source.user)
        }
    }

    class Statistic(
        private val userMapper: Embed,
        private val localSource: UserStatisticLocalSource,
        private val converter: UserStatisticModelConverter,
    ) : DefaultMapper<UserModelContainer.WithStatistic, UserWithStatisticEntity>() {


        /**
         * Save [data] into your desired local source
         */
        override suspend fun persist(data: UserWithStatisticEntity) {
            userMapper.persistEmbedded()
            localSource.upsert(data)
        }

        /**
         * Creates mapped objects and handles the database operations which may be required to map various objects,
         *
         * @param source the incoming data source type
         * @return mapped object that will be consumed by [onResponseDatabaseInsert]
         */
        override suspend fun onResponseMapFrom(
            source: UserModelContainer.WithStatistic
        ): UserWithStatisticEntity {
            userMapper.onEmbedded(source.user)
            return converter.convertFrom(source.user)
        }
    }

    class User(
        override val localSource: UserLocalSource,
        override val converter: UserModelConverter
    ) : UserMapper<UserModelContainer.User, UserEntity>() {

        /**
         * Save [data] into your desired local source
         */
        override suspend fun persist(data: UserEntity) {
            localSource.upsert(data)
        }

        /**
         * Creates mapped objects and handles the database operations which may be required to map various objects,
         *
         * @param source the incoming data source type
         * @return mapped object that will be consumed by [onResponseDatabaseInsert]
         */
        override suspend fun onResponseMapFrom(
            source: UserModelContainer.User
        ) = converter.convertFrom(source.user)
    }

    class Embed(
        override val localSource: UserLocalSource,
        override val converter: UserModelConverter
    ) : EmbedMapper<UserModel, UserEntity>()

    class MediaOptionEmbed(
        override val localSource: UserMediaOptionLocalSource,
        override val converter: UserMediaOptionModelConverter
    ) : EmbedMapper<UserModel.WithOptions, UserMediaOptionEntity>() {
        suspend fun persistEmbedded(settings: IUserSettings) {
            onResponseDatabaseInsert(entities.orEmpty())
            entities?.firstOrNull()?.also {
                settings.scoreFormat.value = it.scoreFormat
            }
            entities = null
        }
    }

    class GeneralOptionEmbed(
        override val localSource: UserGeneralOptionLocalSource,
        override val converter: UserGeneralOptionModelConverter
    ) : EmbedMapper<UserModel.WithOptions, UserGeneralOptionEntity>() {
        suspend fun persistEmbedded(settings: IUserSettings) {
            onResponseDatabaseInsert(entities.orEmpty())
            entities?.firstOrNull()?.also {
                settings.titleLanguage.value = it.titleLanguage
            }
            entities = null
        }
    }
}
