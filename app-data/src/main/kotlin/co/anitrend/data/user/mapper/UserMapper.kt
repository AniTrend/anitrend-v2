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

import co.anitrend.arch.data.converter.SupportConverter
import co.anitrend.data.android.mapper.DefaultMapper
import co.anitrend.data.android.mapper.EmbedMapper
import co.anitrend.data.android.source.local.AbstractLocalSource
import co.anitrend.data.user.converter.*
import co.anitrend.data.user.datasource.local.UserLocalSource
import co.anitrend.data.user.datasource.local.option.UserGeneralOptionLocalSource
import co.anitrend.data.user.datasource.local.option.UserMediaOptionLocalSource
import co.anitrend.data.user.datasource.local.statistic.UserStatisticLocalSource
import co.anitrend.data.user.entity.UserEntity
import co.anitrend.data.user.entity.name.UserPreviousNameEntity
import co.anitrend.data.user.entity.notification.UserNotificationEntity
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
        private val previousNameMapper: PreviousNameEmbed,
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
            previousNameMapper.persistEmbedded()
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
            previousNameMapper.onEmbedded(
                PreviousNameEmbed.asItem(source.user)
            )
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

    class PreviousNameEmbed(
        override val localSource: AbstractLocalSource<UserPreviousNameEntity>
    ) : EmbedMapper<PreviousNameEmbed.Item, UserPreviousNameEntity>() {

        override val converter = object : SupportConverter<Item, UserPreviousNameEntity>() {
            /**
             * Function reference from converting from [M] to [E] which will
             * be called by [convertFrom]
             */
            override val fromType: (Item) -> UserPreviousNameEntity = {
                UserPreviousNameEntity(
                    userId = it.userId,
                    createdAt = it.previousName.createdAt,
                    name = it.previousName.name,
                    updatedAt = it.previousName.updatedAt,
                )
            }

            /**
             * Function reference from converting from [E] to [M] which will
             * be called by [convertTo]
             */
            override val toType: (UserPreviousNameEntity) -> Item
                get() = throw NotImplementedError()
        }

        data class Item(
            val userId: Long,
            val previousName: UserModel.UserPreviousName
        )

        companion object {
            fun asItem(source: UserModel.WithOptions) =
                source.previousNames.map {
                    Item(
                        userId = source.id,
                        previousName = it
                    )
                }

            fun asItem(source: List<UserModel.WithOptions>) =
                source.flatMap { user ->
                    asItem(user)
                }
        }
    }

    class NotificationEmbed(
        override val localSource: AbstractLocalSource<UserNotificationEntity>
    ) : EmbedMapper<NotificationEmbed.Item, UserNotificationEntity>() {

        override val converter = object : SupportConverter<Item, UserNotificationEntity>() {
            /**
             * Function reference from converting from [M] to [E] which will
             * be called by [convertFrom]
             */
            override val fromType: (Item) -> UserNotificationEntity = {
                UserNotificationEntity(
                    userId = it.userId,
                    unreadNotifications = it.unreadNotifications
                )
            }

            /**
             * Function reference from converting from [E] to [M] which will
             * be called by [convertTo]
             */
            override val toType: (UserNotificationEntity) -> Item
                get() = throw NotImplementedError()

        }

        data class Item(
            val userId: Long,
            val unreadNotifications: Int
        )


        companion object {
            fun asItem(source: UserModel.Viewer) = Item(
                userId = source.id,
                unreadNotifications = source.unreadNotificationCount ?: 0
            )
        }
    }
}
