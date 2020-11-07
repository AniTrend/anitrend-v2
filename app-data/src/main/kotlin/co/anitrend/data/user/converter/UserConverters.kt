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

package co.anitrend.data.user.converter

import co.anitrend.arch.data.converter.SupportConverter
import co.anitrend.arch.data.transformer.ISupportTransformer
import co.anitrend.data.user.entity.UserEntity
import co.anitrend.data.user.entity.option.UserGeneralOptionEntity
import co.anitrend.data.user.entity.option.UserMediaOptionEntity
import co.anitrend.data.user.entity.view.UserEntityView
import co.anitrend.data.user.model.remote.UserModel
import co.anitrend.domain.medialist.enums.ScoreFormat
import co.anitrend.domain.user.entity.User
import co.anitrend.domain.user.entity.attribute.option.UserMediaListOption
import co.anitrend.domain.user.entity.attribute.option.UserMediaListTypeOptions
import co.anitrend.domain.user.entity.attribute.option.UserNotificationOption
import co.anitrend.domain.user.entity.attribute.option.UserProfileOption
import co.anitrend.domain.user.entity.contract.UserImage
import co.anitrend.domain.user.entity.contract.UserStatus
import co.anitrend.domain.user.enums.UserTitleLanguage

internal class UserModelConverter(
    override val fromType: (UserModel) -> UserEntity = { transform(it) },
    override val toType: (UserEntity) -> UserModel = { throw NotImplementedError() }
) : SupportConverter<UserModel, UserEntity>() {
    private companion object : ISupportTransformer<UserModel, UserEntity> {
        override fun transform(source: UserModel) = when (source) {
            is UserModel.Core -> UserEntity(
                about = UserEntity.About(
                    name = source.name,
                    bio = source.about,
                    siteUrl = source.siteUrl,
                    donatorTier = source.donatorTier,
                    donatorBadge = source.donatorBadge,
                ),
                status = UserEntity.Status(),
                coverImage = UserEntity.CoverImage(
                    large = source.avatar?.large,
                    medium = source.avatar?.medium,
                    banner = source.bannerImage
                ),
                unreadNotification = null,
                updatedAt = source.updatedAt,
                id = source.id
            )
            is UserModel.Extended -> UserEntity(
                about = UserEntity.About(
                    name = source.name,
                    bio = source.about,
                    siteUrl = source.siteUrl,
                    donatorTier = source.donatorTier,
                    donatorBadge = source.donatorBadge,
                ),
                status = UserEntity.Status(
                    isFollowing = source.isFollowing,
                    isFollower = source.isFollower,
                    isBlocked = source.isBlocked ?: false
                ),
                coverImage = UserEntity.CoverImage(
                    large = source.avatar?.large,
                    medium = source.avatar?.medium,
                    banner = source.bannerImage
                ),
                unreadNotification = source.unreadNotificationCount,
                updatedAt = source.updatedAt,
                id = source.id
            )
            is UserModel.WithStatistic -> TODO("Pending implementation")
        }
    }
}

internal class UserMediaOptionModelConverter(
    override val fromType: (UserModel.Extended) -> UserMediaOptionEntity = { transform(it) },
    override val toType: (UserMediaOptionEntity) -> UserModel.Extended = { throw NotImplementedError() }
) : SupportConverter<UserModel.Extended, UserMediaOptionEntity>() {
    private companion object : ISupportTransformer<UserModel.Extended, UserMediaOptionEntity> {
        override fun transform(source: UserModel.Extended) = UserMediaOptionEntity(
            userId = source.id,
            scoreFormat = source.mediaListOptions?.scoreFormat ?: ScoreFormat.POINT_100,
            rowOrder = source.mediaListOptions?.rowOrder,
            anime = UserMediaOptionEntity.MediaOption(
                customLists = source.mediaListOptions?.animeList?.customLists.orEmpty(),
                sectionOrder = source.mediaListOptions?.animeList?.sectionOrder.orEmpty(),
                advancedScoring = source.mediaListOptions?.animeList?.advancedScoring.orEmpty(),
                advancedScoringEnabled = source.mediaListOptions?.animeList?.advancedScoringEnabled ?: false,
                splitCompletedSectionByFormat = source.mediaListOptions?.animeList?.splitCompletedSectionByFormat ?: false
            ),
            manga = UserMediaOptionEntity.MediaOption(
                customLists = source.mediaListOptions?.mangaList?.customLists.orEmpty(),
                sectionOrder = source.mediaListOptions?.mangaList?.sectionOrder.orEmpty(),
                advancedScoring = source.mediaListOptions?.mangaList?.advancedScoring.orEmpty(),
                advancedScoringEnabled = source.mediaListOptions?.mangaList?.advancedScoringEnabled ?: false,
                splitCompletedSectionByFormat = source.mediaListOptions?.mangaList?.splitCompletedSectionByFormat ?: false
            )
        )
    }
}

internal class UserGeneralOptionModelConverter(
    override val fromType: (UserModel.Extended) -> UserGeneralOptionEntity = { transform(it) },
    override val toType: (UserGeneralOptionEntity) -> UserModel.Extended = { throw NotImplementedError() }
) : SupportConverter<UserModel.Extended, UserGeneralOptionEntity>() {
    private companion object : ISupportTransformer<UserModel.Extended, UserGeneralOptionEntity> {
        override fun transform(source: UserModel.Extended) = UserGeneralOptionEntity(
            userId = source.id,
            airingNotifications = source.options?.airingNotifications ?: false,
            displayAdultContent = source.options?.displayAdultContent ?: false,
            notificationOption = source.options?.notificationOptions?.map { option ->
                UserGeneralOptionEntity.NotificationOption(
                    enabled = option.enabled,
                    notificationType = option.notificationType
                )
            }.orEmpty(),
            titleLanguage = source.options?.titleLanguage ?: UserTitleLanguage.ROMAJI,
            profileColor = source.options?.profileColor,
        )
    }
}

internal class UserEntityConverter(
    override val fromType: (UserEntity) -> User = { transform(it) },
    override val toType: (User) -> UserEntity = { throw NotImplementedError() }
) : SupportConverter<UserEntity, User>() {
    private companion object : ISupportTransformer<UserEntity, User> {
                override fun transform(source: UserEntity): User {
                    return User.Core(
                        name = source.about.name,
                        avatar = UserImage(
                            large = source.coverImage.large,
                            medium = source.coverImage.medium,
                            banner = source.coverImage.banner
                        ),
                        status = UserStatus(
                            about = source.about.bio,
                            donationBadge = source.about.donatorBadge,
                            donationTier = source.about.donatorTier,
                            isFollowing = source.status?.isFollowing,
                            isFollower = source.status?.isFollower,
                            isBlocked = source.status?.isBlocked,
                            pageUrl = source.about.siteUrl,
                        ),
                        id = source.id
                    )
                }
            }
}

internal class UserViewEntityConverter(
    override val fromType: (UserEntityView) -> User = { transform(it) },
    override val toType: (User) -> UserEntityView = { throw NotImplementedError() }
) : SupportConverter<UserEntityView, User>() {
    private companion object : ISupportTransformer<UserEntityView, User> {
        override fun transform(source: UserEntityView) = when (source) {
            is UserEntityView.WithOptions -> User.Extended(
                unreadNotifications = source.user.unreadNotification ?: 0,
                listOptions = UserMediaListOption(
                    scoreFormat = source.mediaListOption.scoreFormat,
                    rowOrder = source.mediaListOption.rowOrder,
                    animeList = UserMediaListTypeOptions(
                        sectionOrder = source.mediaListOption.anime.sectionOrder,
                        splitCompletedSectionByFormat = source.mediaListOption.anime.splitCompletedSectionByFormat,
                        customLists = source.mediaListOption.anime.customLists,
                        advancedScoring = source.mediaListOption.anime.advancedScoring,
                        advancedScoringEnabled = source.mediaListOption.anime.advancedScoringEnabled
                    ),
                    mangaList = UserMediaListTypeOptions(
                        sectionOrder = source.mediaListOption.manga.sectionOrder,
                        splitCompletedSectionByFormat = source.mediaListOption.manga.splitCompletedSectionByFormat,
                        customLists = source.mediaListOption.manga.customLists,
                        advancedScoring = source.mediaListOption.manga.advancedScoring,
                        advancedScoringEnabled = source.mediaListOption.manga.advancedScoringEnabled
                    ),
                ),
                profileOption = UserProfileOption(
                    titleLanguage = source.generalOption.titleLanguage,
                    displayAdultContent = source.generalOption.displayAdultContent,
                    airingNotifications = source.generalOption.airingNotifications,
                    notificationOptions = source.generalOption.notificationOption.map { option ->
                        UserNotificationOption(
                            isEnabled = option.enabled,
                            type = option.notificationType
                        )
                    },
                    profileColor = source.generalOption.profileColor,
                ),
                name = source.user.about.name,
                avatar = UserImage(
                    large = source.user.coverImage.large,
                    medium = source.user.coverImage.medium,
                    banner = source.user.coverImage.banner
                ),
                status = UserStatus(
                    about = source.user.about.bio,
                    donationBadge = source.user.about.donatorBadge,
                    donationTier = source.user.about.donatorTier,
                    isFollowing = source.user.status?.isFollowing,
                    isFollower = source.user.status?.isFollower,
                    isBlocked = source.user.status?.isBlocked,
                    pageUrl = source.user.about.siteUrl,
                ),
                id = source.user.id
            )
            is UserEntityView.WithStatistic -> TODO("Pending implementation")
        }
    }
}