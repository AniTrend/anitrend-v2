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
import co.anitrend.data.user.entity.statistic.UserWithStatisticEntity
import co.anitrend.data.user.entity.view.UserEntityView
import co.anitrend.data.user.model.UserModel
import co.anitrend.domain.common.entity.shared.CoverImage
import co.anitrend.domain.common.entity.shared.CoverName
import co.anitrend.domain.medialist.enums.ScoreFormat
import co.anitrend.domain.staff.entity.Staff
import co.anitrend.domain.studio.entity.Studio
import co.anitrend.domain.tag.entity.Tag
import co.anitrend.domain.user.entity.User
import co.anitrend.domain.user.entity.attribute.option.UserMediaListOption
import co.anitrend.domain.user.entity.attribute.option.UserMediaListTypeOptions
import co.anitrend.domain.user.entity.attribute.option.UserNotificationOption
import co.anitrend.domain.user.entity.attribute.option.UserProfileOption
import co.anitrend.domain.user.entity.attribute.statistic.MediaStatistic
import co.anitrend.domain.user.entity.attribute.statistic.Statistic
import co.anitrend.domain.user.entity.attribute.statistic.UserMediaStatisticType
import co.anitrend.domain.user.entity.contract.UserImage
import co.anitrend.domain.user.entity.contract.UserStatus
import co.anitrend.domain.user.enums.UserTitleLanguage

internal class UserModelConverter(
    override val fromType: (UserModel) -> UserEntity = ::transform,
    override val toType: (UserEntity) -> UserModel = { throw NotImplementedError() }
) : SupportConverter<UserModel, UserEntity>() {
    private companion object : ISupportTransformer<UserModel, UserEntity> {
        override fun transform(source: UserModel): UserEntity = when (source) {
            is UserModel.Core -> UserEntity(
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
            is UserModel.WithStatistic -> UserEntity(
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
                unreadNotification = null,
                updatedAt = source.updatedAt,
                id = source.id
            )
        }
    }
}

internal class UserStatisticModelConverter(
    override val fromType: (UserModel.WithStatistic) -> UserWithStatisticEntity = ::transform,
    override val toType: (UserWithStatisticEntity) -> UserModel.WithStatistic = { throw NotImplementedError() }
) : SupportConverter<UserModel.WithStatistic, UserWithStatisticEntity>() {
    private companion object : ISupportTransformer<UserModel.WithStatistic, UserWithStatisticEntity> {
        override fun transform(source: UserModel.WithStatistic) = UserWithStatisticEntity(
            statistic = UserWithStatisticEntity.Statistic(
                anime = source.statistics?.anime,
                manga = source.statistics?.manga
            ),
            userId = source.id
        )
    }
}

internal class UserMediaOptionModelConverter(
    override val fromType: (UserModel.Extended) -> UserMediaOptionEntity = ::transform,
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
    override val fromType: (UserModel.Extended) -> UserGeneralOptionEntity = ::transform,
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
    override val fromType: (UserEntity) -> User = ::transform,
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
    override val fromType: (UserEntityView) -> User = ::transform,
    override val toType: (User) -> UserEntityView = { throw NotImplementedError() }
) : SupportConverter<UserEntityView, User>() {
    private companion object : ISupportTransformer<UserEntityView, User> {
        override fun transform(source: UserEntityView) = when (source) {
            is UserEntityView.WithOptions -> User.Extended(
                unreadNotifications = source.user.unreadNotification ?: 0,
                listOption = UserMediaListOption(
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
            is UserEntityView.WithStatistic -> User.WithStats(
                unreadNotifications = source.user.unreadNotification ?: 0,
                listOption = UserMediaListOption(
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
                statistics = UserMediaStatisticType(
                    anime = source.statistic.statistic.anime?.let { entity ->
                        Statistic.Anime(
                            minutesWatched = entity.minutesWatched,
                            episodesWatched = entity.episodesWatched,
                            count = entity.count,
                            meanScore = entity.meanScore,
                            standardDeviation = entity.standardDeviation,
                            countries = entity.countries?.map {
                                MediaStatistic.Anime.Country(
                                    country = it.country,
                                    count = it.count,
                                    meanScore = it.meanScore,
                                    mediaIds = it.mediaIds,
                                    minutesWatched = it.minutesWatched,
                                )
                            },
                            formats = entity.formats?.map {
                                MediaStatistic.Anime.Format(
                                    format = it.format,
                                    count = it.count,
                                    meanScore = it.meanScore,
                                    mediaIds = it.mediaIds,
                                    minutesWatched = it.minutesWatched,
                                )
                            },
                            genres = entity.genres?.map {
                                MediaStatistic.Anime.Genre(
                                    genre = it.genre,
                                    count = it.count,
                                    meanScore = it.meanScore,
                                    mediaIds = it.mediaIds,
                                    minutesWatched = it.minutesWatched,
                                )
                            },
                            lengths = entity.lengths?.map {
                                MediaStatistic.Anime.Length(
                                    length = it.length,
                                    count = it.count,
                                    meanScore = it.meanScore,
                                    mediaIds = it.mediaIds,
                                    minutesWatched = it.minutesWatched,
                                )
                            },
                            releaseYears = entity.releaseYears?.map {
                                MediaStatistic.Anime.ReleaseYear(
                                    releaseYear = it.releaseYear,
                                    count = it.count,
                                    meanScore = it.meanScore,
                                    mediaIds = it.mediaIds,
                                    minutesWatched = it.minutesWatched,
                                )
                            },
                            scores = entity.scores?.map {
                                MediaStatistic.Anime.Score(
                                    score = it.score,
                                    count = it.count,
                                    meanScore = it.meanScore,
                                    mediaIds = it.mediaIds,
                                    minutesWatched = it.minutesWatched,
                                )
                            },
                            staff = entity.staff?.map {
                                MediaStatistic.Anime.Staff(
                                    staff = it.staff?.let { staff ->
                                        Staff.Core(
                                            description = staff.description,
                                            favourites = staff.favourites,
                                            image = staff.image?.let { image ->
                                                CoverImage(
                                                    large = image.large,
                                                    medium = image.medium
                                                )
                                            },
                                            isFavourite = staff.isFavourite,
                                            language = staff.language,
                                            name = staff.name?.let { name ->
                                                CoverName(
                                                    alternative = name.alternative.orEmpty(),
                                                    first = name.first,
                                                    full = name.full,
                                                    last = name.last,
                                                    native = name.native,
                                                )
                                            },
                                            siteUrl = staff.siteUrl,
                                            updatedAt = staff.updatedAt,
                                            id = staff.id
                                        )
                                    },
                                    count = it.count,
                                    meanScore = it.meanScore,
                                    mediaIds = it.mediaIds,
                                    minutesWatched = it.minutesWatched,
                                )
                            },
                            startYears = entity.startYears?.map {
                                MediaStatistic.Anime.StartYear(
                                    startYear = it.startYear,
                                    count = it.count,
                                    meanScore = it.meanScore,
                                    mediaIds = it.mediaIds,
                                    minutesWatched = it.minutesWatched,
                                )
                            },
                            statuses = entity.statuses?.map {
                                MediaStatistic.Anime.Status(
                                    status = it.status,
                                    count = it.count,
                                    meanScore = it.meanScore,
                                    mediaIds = it.mediaIds,
                                    minutesWatched = it.minutesWatched,
                                )
                            },
                            studios = entity.studios?.map {
                                MediaStatistic.Anime.Studio(
                                    studio = it.studio?.let { studio ->
                                        Studio.Core(
                                            favourites = studio.favourites ?: 0,
                                            isAnimationStudio = studio.isAnimationStudio,
                                            isFavourite = studio.isFavourite,
                                            name = studio.name,
                                            siteUrl = studio.siteUrl,
                                            id = studio.id
                                        )
                                    },
                                    count = it.count,
                                    meanScore = it.meanScore,
                                    mediaIds = it.mediaIds,
                                    minutesWatched = it.minutesWatched,
                                )
                            },
                            tags = entity.tags?.map {
                                MediaStatistic.Anime.Tag(
                                    tag = it.tag?.let { tag ->
                                        Tag.Core(
                                            name = tag.name,
                                            description = tag.description,
                                            category = tag.category,
                                            isGeneralSpoiler = tag.isGeneralSpoiler ?: false,
                                            isAdult = tag.isAdult ?: false,
                                            id = tag.id,
                                        )
                                    },
                                    count = it.count,
                                    meanScore = it.meanScore,
                                    mediaIds = it.mediaIds,
                                    minutesWatched = it.minutesWatched,
                                )
                            },
                            voiceActors = entity.voiceActors?.map {
                                MediaStatistic.Anime.VoiceActor(
                                    voiceActor = it.voiceActor?.let { staff ->
                                        Staff.Core(
                                            description = staff.description,
                                            favourites = staff.favourites,
                                            image = staff.image?.let { image ->
                                                CoverImage(
                                                    large = image.large,
                                                    medium = image.medium
                                                )
                                            },
                                            isFavourite = staff.isFavourite,
                                            language = staff.language,
                                            name = staff.name?.let { name ->
                                                CoverName(
                                                    alternative = name.alternative.orEmpty(),
                                                    first = name.first,
                                                    full = name.full,
                                                    last = name.last,
                                                    native = name.native,
                                                )
                                            },
                                            siteUrl = staff.siteUrl,
                                            updatedAt = staff.updatedAt,
                                            id = staff.id
                                        )
                                    },
                                    count = it.count,
                                    meanScore = it.meanScore,
                                    mediaIds = it.mediaIds,
                                    minutesWatched = it.minutesWatched,
                                )
                            },
                        )
                    },
                    manga = source.statistic.statistic.manga?.let { entity ->
                        Statistic.Manga(
                            chaptersRead = entity.chaptersRead,
                            volumesRead = entity.volumesRead,
                            count = entity.count,
                            meanScore = entity.meanScore,
                            standardDeviation = entity.standardDeviation,
                            countries = entity.countries?.map {
                                 MediaStatistic.Manga.Country(
                                     country = it.country,
                                     count = it.count,
                                     meanScore = it.meanScore,
                                     mediaIds = it.mediaIds,
                                     chaptersRead = it.chaptersRead,
                                 )
                            },
                            formats = entity.formats?.map {
                                 MediaStatistic.Manga.Format(
                                     format = it.format,
                                     count = it.count,
                                     meanScore = it.meanScore,
                                     mediaIds = it.mediaIds,
                                     chaptersRead = it.chaptersRead,
                                 )
                            },
                            genres = entity.genres?.map {
                                 MediaStatistic.Manga.Genre(
                                     genre = it.genre,
                                     count = it.count,
                                     meanScore = it.meanScore,
                                     mediaIds = it.mediaIds,
                                     chaptersRead = it.chaptersRead,
                                 )
                            },
                            lengths = entity.lengths?.map {
                                 MediaStatistic.Manga.Length(
                                     length = it.length,
                                     count = it.count,
                                     meanScore = it.meanScore,
                                     mediaIds = it.mediaIds,
                                     chaptersRead = it.chaptersRead,
                                 )
                            },
                            releaseYears = entity.releaseYears?.map {
                                 MediaStatistic.Manga.ReleaseYear(
                                     releaseYear = it.releaseYear,
                                     count = it.count,
                                     meanScore = it.meanScore,
                                     mediaIds = it.mediaIds,
                                     chaptersRead = it.chaptersRead,
                                 )
                            },
                            scores = entity.scores?.map {
                                 MediaStatistic.Manga.Score(
                                     score = it.score,
                                     count = it.count,
                                     meanScore = it.meanScore,
                                     mediaIds = it.mediaIds,
                                     chaptersRead = it.chaptersRead,
                                 )
                            },
                            staff = entity.staff?.map {
                                 MediaStatistic.Manga.Staff(
                                     staff = it.staff?.let { staff ->
                                         Staff.Core(
                                             description = staff.description,
                                             favourites = staff.favourites,
                                             image = staff.image?.let { image ->
                                                 CoverImage(
                                                     large = image.large,
                                                     medium = image.medium
                                                 )
                                             },
                                             isFavourite = staff.isFavourite,
                                             language = staff.language,
                                             name = staff.name?.let { name ->
                                                 CoverName(
                                                     alternative = name.alternative.orEmpty(),
                                                     first = name.first,
                                                     full = name.full,
                                                     last = name.last,
                                                     native = name.native,
                                                 )
                                             },
                                             siteUrl = staff.siteUrl,
                                             updatedAt = staff.updatedAt,
                                             id = staff.id
                                         )
                                     },
                                     count = it.count,
                                     meanScore = it.meanScore,
                                     mediaIds = it.mediaIds,
                                     chaptersRead = it.chaptersRead,
                                 )
                            },
                            startYears = entity.startYears?.map {
                                 MediaStatistic.Manga.StartYear(
                                     startYear = it.startYear,
                                     count = it.count,
                                     meanScore = it.meanScore,
                                     mediaIds = it.mediaIds,
                                     chaptersRead = it.chaptersRead,
                                 )
                            },
                            statuses = entity.statuses?.map {
                                 MediaStatistic.Manga.Status(
                                     status = it.status,
                                     count = it.count,
                                     meanScore = it.meanScore,
                                     mediaIds = it.mediaIds,
                                     chaptersRead = it.chaptersRead,
                                 )
                            },
                            studios = entity.studios?.map {
                                 MediaStatistic.Manga.Studio(
                                     studio = it.studio?.let { studio ->
                                         Studio.Core(
                                             favourites = studio.favourites ?: 0,
                                             isAnimationStudio = studio.isAnimationStudio,
                                             isFavourite = studio.isFavourite,
                                             name = studio.name,
                                             siteUrl = studio.siteUrl,
                                             id = studio.id
                                         )
                                     },
                                     count = it.count,
                                     meanScore = it.meanScore,
                                     mediaIds = it.mediaIds,
                                     chaptersRead = it.chaptersRead,
                                 )
                            },
                            tags = entity.tags?.map {
                                 MediaStatistic.Manga.Tag(
                                     tag = it.tag?.let { tag ->
                                         Tag.Core(
                                             name = tag.name,
                                             description = tag.description,
                                             category = tag.category,
                                             isGeneralSpoiler = tag.isGeneralSpoiler ?: false,
                                             isAdult = tag.isAdult ?: false,
                                             id = tag.id,
                                         )
                                     },
                                     count = it.count,
                                     meanScore = it.meanScore,
                                     mediaIds = it.mediaIds,
                                     chaptersRead = it.chaptersRead,
                                 )
                            },
                            voiceActors = entity.voiceActors?.map {
                                 MediaStatistic.Manga.VoiceActor(
                                     voiceActor = it.voiceActor?.let { staff ->
                                         Staff.Core(
                                             description = staff.description,
                                             favourites = staff.favourites,
                                             image = staff.image?.let { image ->
                                                 CoverImage(
                                                     large = image.large,
                                                     medium = image.medium
                                                 )
                                             },
                                             isFavourite = staff.isFavourite,
                                             language = staff.language,
                                             name = staff.name?.let { name ->
                                                 CoverName(
                                                     alternative = name.alternative.orEmpty(),
                                                     first = name.first,
                                                     full = name.full,
                                                     last = name.last,
                                                     native = name.native,
                                                 )
                                             },
                                             siteUrl = staff.siteUrl,
                                             updatedAt = staff.updatedAt,
                                             id = staff.id
                                         )
                                     },
                                     count = it.count,
                                     meanScore = it.meanScore,
                                     mediaIds = it.mediaIds,
                                     chaptersRead = it.chaptersRead,
                                 )
                            },
                        )
                    }
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
        }
    }
}