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

import co.anitrend.data.arch.mapper.DefaultMapper
import co.anitrend.data.arch.railway.OutCome
import co.anitrend.data.user.converter.UserGeneralOptionModelConverter
import co.anitrend.data.user.converter.UserMediaOptionModelConverter
import co.anitrend.data.user.converter.UserModelConverter
import co.anitrend.data.user.converter.UserStatisticModelConverter
import co.anitrend.data.user.datasource.local.UserLocalSource
import co.anitrend.data.user.entity.UserEntity
import co.anitrend.data.user.entity.option.UserGeneralOptionEntity
import co.anitrend.data.user.entity.option.UserMediaOptionEntity
import co.anitrend.data.user.entity.statistic.UserWithStatisticEntity
import co.anitrend.data.user.model.container.UserModelContainer

internal sealed class UserMapper<S, D> : DefaultMapper<S, D>() {

    protected abstract val localSource: UserLocalSource
    protected abstract val converter: UserModelConverter

    /**
     * Handles the persistence of [data] into a local source
     *
     * @return [OutCome.Pass] or [OutCome.Fail] of the operation
     */
    override suspend fun persistChanges(data: D): OutCome<Nothing?> {
        return runCatching {
            if (data is UserEntity)
                localSource.upsert(data)
            OutCome.Pass(null)
        }.getOrElse { OutCome.Fail(listOf(it)) }
    }

    class Paged(
        override val localSource: UserLocalSource,
        override val converter: UserModelConverter
    ) : UserMapper<UserModelContainer.Paged, List<UserEntity>>() {

        /**
         * Handles the persistence of [data] into a local source
         *
         * @return [OutCome.Pass] or [OutCome.Fail] of the operation
         */
        override suspend fun persistChanges(data: List<UserEntity>): OutCome<Nothing?> {
            return runCatching {
                localSource.upsert(data)
                OutCome.Pass(null)
            }.getOrElse { OutCome.Fail(listOf(it)) }
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
        private val generalOptionConverter: UserGeneralOptionModelConverter,
        private val mediaOptionConverter: UserMediaOptionModelConverter,
        override val localSource: UserLocalSource,
        override val converter: UserModelConverter
    ) : UserMapper<UserModelContainer.Profile, UserEntity>() {

        private var generalOption: UserGeneralOptionEntity? = null
        private var mediaOption: UserMediaOptionEntity? = null

        /**
         * Handles the persistence of [data] into a local source
         *
         * @return [OutCome.Pass] or [OutCome.Fail] of the operation
         */
        override suspend fun persistChanges(data: UserEntity): OutCome<Nothing?> {
            return runCatching {
                localSource.upsertWithOptions(data, generalOption, mediaOption)
                generalOption = null
                mediaOption = null
                OutCome.Pass(null)
            }.getOrElse { OutCome.Fail(listOf(it)) }
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
            generalOption = generalOptionConverter.convertFrom(source.user)
            mediaOption = mediaOptionConverter.convertFrom(source.user)

            return converter.convertFrom(source.user)
        }
    }

    class Statistic(
        private val localSource: UserLocalSource,
        private val converter: UserStatisticModelConverter,
        private val userConverter: UserModelConverter
    ) : DefaultMapper<UserModelContainer.WithStatistic, UserWithStatisticEntity>() {

        private var userEntity: UserEntity? = null

        /**
         * Handles the persistence of [data] into a local source
         *
         * @return [OutCome.Pass] or [OutCome.Fail] of the operation
         */
        override suspend fun persistChanges(data: UserWithStatisticEntity): OutCome<Nothing?> {
            return runCatching {
                val entity = requireNotNull(userEntity) {
                    "UserEntity should not be null at this point"
                }
                localSource.upsertWithStatistic(entity, data)
                userEntity = null
                OutCome.Pass(null)
            }.getOrElse { OutCome.Fail(listOf(it)) }
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
            userEntity = userConverter.convertFrom(source.user)
            return converter.convertFrom(source.user)
        }
    }

    class User(
        override val localSource: UserLocalSource,
        override val converter: UserModelConverter
    ) : UserMapper<UserModelContainer.User, UserEntity>() {

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
}
