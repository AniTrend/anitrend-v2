/*
 * Copyright (C) 2020 AniTrend
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

import androidx.annotation.VisibleForTesting
import co.anitrend.arch.data.converter.SupportConverter
import co.anitrend.arch.data.transformer.ISupportTransformer
import co.anitrend.data.core.extensions.koinOf
import co.anitrend.data.medialist.entity.view.CustomListCountView
import co.anitrend.data.medialist.entity.view.MediaListCountView
import co.anitrend.data.staff.converter.StaffEntityConverter
import co.anitrend.data.studio.converter.StudioEntityConverter
import co.anitrend.data.tag.converter.TagEntityConverter
import co.anitrend.data.user.entity.UserEntity
import co.anitrend.data.user.entity.option.UserGeneralOptionEntity
import co.anitrend.data.user.entity.option.UserMediaOptionEntity
import co.anitrend.data.user.entity.statistic.UserStatisticCountryEntity
import co.anitrend.data.user.entity.statistic.UserStatisticFormatEntity
import co.anitrend.data.user.entity.statistic.UserStatisticGenreEntity
import co.anitrend.data.user.entity.statistic.UserStatisticLengthEntity
import co.anitrend.data.user.entity.statistic.UserStatisticReleaseYearEntity
import co.anitrend.data.user.entity.statistic.UserStatisticScoreEntity
import co.anitrend.data.user.entity.statistic.UserStatisticStaffEntity
import co.anitrend.data.user.entity.statistic.UserStatisticStartYearEntity
import co.anitrend.data.user.entity.statistic.UserStatisticStatusEntity
import co.anitrend.data.user.entity.statistic.UserStatisticStudioEntity
import co.anitrend.data.user.entity.statistic.UserStatisticTagEntity
import co.anitrend.data.user.entity.statistic.UserStatisticVoiceActorEntity
import co.anitrend.data.user.entity.statistic.UserWithStatisticEntity
import co.anitrend.data.user.entity.view.UserEntityView
import co.anitrend.data.user.entity.view.UserStatisticEntityView
import co.anitrend.data.user.model.UserModel
import co.anitrend.domain.media.enums.MediaType
import co.anitrend.domain.medialist.enums.MediaListStatus
import co.anitrend.domain.medialist.enums.ScoreFormat
import co.anitrend.domain.user.entity.User
import co.anitrend.domain.user.entity.attribute.MediaListInfo
import co.anitrend.domain.user.entity.attribute.option.UserMediaListOption
import co.anitrend.domain.user.entity.attribute.option.UserMediaListTypeOptions
import co.anitrend.domain.user.entity.attribute.option.UserNotificationOption
import co.anitrend.domain.user.entity.attribute.option.UserProfileOption
import co.anitrend.domain.user.entity.attribute.statistic.MediaStatistic
import co.anitrend.domain.user.entity.attribute.statistic.Statistic
import co.anitrend.domain.user.entity.attribute.statistic.UserMediaStatisticType
import co.anitrend.domain.user.entity.contract.UserImage
import co.anitrend.domain.user.entity.contract.UserStatus
import co.anitrend.domain.user.enums.UserStaffNameLanguage
import co.anitrend.domain.user.enums.UserTitleLanguage

internal class UserModelConverter(
    override val fromType: (UserModel) -> UserEntity = ::transform,
    override val toType: (UserEntity) -> UserModel = { throw NotImplementedError() },
) : SupportConverter<UserModel, UserEntity>() {
    private companion object : ISupportTransformer<UserModel, UserEntity> {
        override fun transform(source: UserModel): UserEntity =
            when (source) {
                is UserModel.Core ->
                    UserEntity(
                        about =
                            UserEntity.About(
                                name = source.name,
                                bio = source.about,
                                siteUrl = source.siteUrl,
                                donatorTier = source.donatorTier,
                                donatorBadge = source.donatorBadge,
                            ),
                        status =
                            UserEntity.Status(
                                isFollowing = source.isFollowing,
                                isFollower = source.isFollower,
                                isBlocked = source.isBlocked ?: false,
                            ),
                        coverImage =
                            UserEntity.CoverImage(
                                large = source.avatar?.large,
                                medium = source.avatar?.medium,
                                banner = source.bannerImage,
                            ),
                        updatedAt = source.updatedAt,
                        createdAt = source.createdAt,
                        id = source.id,
                    )
                is UserModel.Extended ->
                    UserEntity(
                        about =
                            UserEntity.About(
                                name = source.name,
                                bio = source.about,
                                siteUrl = source.siteUrl,
                                donatorTier = source.donatorTier,
                                donatorBadge = source.donatorBadge,
                            ),
                        status =
                            UserEntity.Status(
                                isFollowing = source.isFollowing,
                                isFollower = source.isFollower,
                                isBlocked = source.isBlocked ?: false,
                            ),
                        coverImage =
                            UserEntity.CoverImage(
                                large = source.avatar?.large,
                                medium = source.avatar?.medium,
                                banner = source.bannerImage,
                            ),
                        updatedAt = source.updatedAt,
                        createdAt = source.createdAt,
                        id = source.id,
                    )
                is UserModel.Viewer ->
                    UserEntity(
                        about =
                            UserEntity.About(
                                name = source.name,
                                bio = source.about,
                                siteUrl = source.siteUrl,
                                donatorTier = source.donatorTier,
                                donatorBadge = source.donatorBadge,
                            ),
                        status =
                            UserEntity.Status(
                                isFollowing = false,
                                isFollower = false,
                                isBlocked = source.isBlocked ?: false,
                            ),
                        coverImage =
                            UserEntity.CoverImage(
                                large = source.avatar?.large,
                                medium = source.avatar?.medium,
                                banner = source.bannerImage,
                            ),
                        updatedAt = source.updatedAt,
                        createdAt = source.createdAt,
                        id = source.id,
                    )
                is UserModel.WithStatistic ->
                    UserEntity(
                        about =
                            UserEntity.About(
                                name = source.name,
                                bio = source.about,
                                siteUrl = source.siteUrl,
                                donatorTier = source.donatorTier,
                                donatorBadge = source.donatorBadge,
                            ),
                        status =
                            UserEntity.Status(
                                isFollowing = source.isFollowing,
                                isFollower = source.isFollower,
                                isBlocked = source.isBlocked ?: false,
                            ),
                        coverImage =
                            UserEntity.CoverImage(
                                large = source.avatar?.large,
                                medium = source.avatar?.medium,
                                banner = source.bannerImage,
                            ),
                        updatedAt = source.updatedAt,
                        createdAt = source.createdAt,
                        id = source.id,
                    )
                else -> error("Nothing to do with this type: $source")
            }
    }
}

internal data class UserStatisticPayload(
    val statistic: UserWithStatisticEntity,
    val countries: List<UserStatisticCountryEntity>,
    val formats: List<UserStatisticFormatEntity>,
    val genres: List<UserStatisticGenreEntity>,
    val lengths: List<UserStatisticLengthEntity>,
    val releaseYears: List<UserStatisticReleaseYearEntity>,
    val scores: List<UserStatisticScoreEntity>,
    val staff: List<UserStatisticStaffEntity>,
    val startYears: List<UserStatisticStartYearEntity>,
    val statuses: List<UserStatisticStatusEntity>,
    val studios: List<UserStatisticStudioEntity>,
    val tags: List<UserStatisticTagEntity>,
    val voiceActors: List<UserStatisticVoiceActorEntity>,
)

internal class UserStatisticModelConverter(
    override val fromType: (UserModel.WithStatistic) -> UserStatisticPayload = ::transform,
    override val toType: (UserStatisticPayload) -> UserModel.WithStatistic = { throw NotImplementedError() },
) : SupportConverter<UserModel.WithStatistic, UserStatisticPayload>() {
    private companion object : ISupportTransformer<UserModel.WithStatistic, UserStatisticPayload> {
        override fun transform(source: UserModel.WithStatistic) =
            UserStatisticPayload(
                statistic =
                    UserWithStatisticEntity(
                        statistic =
                            UserWithStatisticEntity.Statistic(
                                animeCount = source.statistics?.anime?.count,
                                animeMeanScore = source.statistics?.anime?.meanScore,
                                animeStandardDeviation = source.statistics?.anime?.standardDeviation,
                                animeMinutesWatched = source.statistics?.anime?.minutesWatched,
                                animeEpisodesWatched = source.statistics?.anime?.episodesWatched,
                                mangaCount = source.statistics?.manga?.count,
                                mangaMeanScore = source.statistics?.manga?.meanScore,
                                mangaStandardDeviation = source.statistics?.manga?.standardDeviation,
                                mangaChaptersRead = source.statistics?.manga?.chaptersRead,
                                mangaVolumesRead = source.statistics?.manga?.volumesRead,
                            ),
                        userId = source.id,
                        id = source.id,
                    ),
                countries =
                    source.statistics?.anime?.countries.orEmpty().map {
                        UserStatisticCountryEntity(it.country, source.id, MediaType.ANIME, it.count, it.meanScore, it.mediaIds, it.minutesWatched)
                    } +
                        source.statistics?.manga?.countries.orEmpty().map {
                            UserStatisticCountryEntity(it.country, source.id, MediaType.MANGA, it.count, it.meanScore, it.mediaIds, it.chaptersRead)
                        },
                formats =
                    source.statistics?.anime?.formats.orEmpty().map {
                        UserStatisticFormatEntity(it.format, source.id, MediaType.ANIME, it.count, it.meanScore, it.mediaIds, it.minutesWatched)
                    } +
                        source.statistics?.manga?.formats.orEmpty().map {
                            UserStatisticFormatEntity(it.format, source.id, MediaType.MANGA, it.count, it.meanScore, it.mediaIds, it.chaptersRead)
                        },
                genres =
                    source.statistics?.anime?.genres.orEmpty().map {
                        UserStatisticGenreEntity(it.genre, source.id, MediaType.ANIME, it.count, it.meanScore, it.mediaIds, it.minutesWatched)
                    } +
                        source.statistics?.manga?.genres.orEmpty().map {
                            UserStatisticGenreEntity(it.genre, source.id, MediaType.MANGA, it.count, it.meanScore, it.mediaIds, it.chaptersRead)
                        },
                lengths =
                    source.statistics?.anime?.lengths.orEmpty().map {
                        UserStatisticLengthEntity(it.length, source.id, MediaType.ANIME, it.count, it.meanScore, it.mediaIds, it.minutesWatched)
                    } +
                        source.statistics?.manga?.lengths.orEmpty().map {
                            UserStatisticLengthEntity(it.length, source.id, MediaType.MANGA, it.count, it.meanScore, it.mediaIds, it.chaptersRead)
                        },
                releaseYears =
                    source.statistics?.anime?.releaseYears.orEmpty().map {
                        UserStatisticReleaseYearEntity(
                            it.releaseYear,
                            source.id,
                            MediaType.ANIME,
                            it.count,
                            it.meanScore,
                            it.mediaIds,
                            it.minutesWatched,
                        )
                    } +
                        source.statistics?.manga?.releaseYears.orEmpty().map {
                            UserStatisticReleaseYearEntity(
                                it.releaseYear,
                                source.id,
                                MediaType.MANGA,
                                it.count,
                                it.meanScore,
                                it.mediaIds,
                                it.chaptersRead,
                            )
                        },
                scores =
                    source.statistics?.anime?.scores.orEmpty().map {
                        UserStatisticScoreEntity(it.score, source.id, MediaType.ANIME, it.count, it.meanScore, it.mediaIds, it.minutesWatched)
                    } +
                        source.statistics?.manga?.scores.orEmpty().map {
                            UserStatisticScoreEntity(it.score, source.id, MediaType.MANGA, it.count, it.meanScore, it.mediaIds, it.chaptersRead)
                        },
                staff =
                    source.statistics?.anime?.staff.orEmpty().mapNotNull {
                        it.staff?.id?.let { staffId ->
                            UserStatisticStaffEntity(staffId, source.id, MediaType.ANIME, it.count, it.meanScore, it.mediaIds, it.minutesWatched)
                        }
                    } +
                        source.statistics?.manga?.staff.orEmpty().mapNotNull {
                            it.staff?.id?.let { staffId ->
                                UserStatisticStaffEntity(staffId, source.id, MediaType.MANGA, it.count, it.meanScore, it.mediaIds, it.chaptersRead)
                            }
                        },
                startYears =
                    source.statistics?.anime?.startYears.orEmpty().map {
                        UserStatisticStartYearEntity(it.startYear, source.id, MediaType.ANIME, it.count, it.meanScore, it.mediaIds, it.minutesWatched)
                    } +
                        source.statistics?.manga?.startYears.orEmpty().map {
                            UserStatisticStartYearEntity(
                                it.startYear,
                                source.id,
                                MediaType.MANGA,
                                it.count,
                                it.meanScore,
                                it.mediaIds,
                                it.chaptersRead,
                            )
                        },
                statuses =
                    source.statistics?.anime?.statuses.orEmpty().map {
                        UserStatisticStatusEntity(it.status, source.id, MediaType.ANIME, it.count, it.meanScore, it.mediaIds, it.minutesWatched)
                    } +
                        source.statistics?.manga?.statuses.orEmpty().map {
                            UserStatisticStatusEntity(it.status, source.id, MediaType.MANGA, it.count, it.meanScore, it.mediaIds, it.chaptersRead)
                        },
                studios =
                    source.statistics?.anime?.studios.orEmpty().mapNotNull {
                        it.studio?.id?.let { studioId ->
                            UserStatisticStudioEntity(studioId, source.id, MediaType.ANIME, it.count, it.meanScore, it.mediaIds, it.minutesWatched)
                        }
                    } +
                        source.statistics?.manga?.studios.orEmpty().mapNotNull {
                            it.studio?.id?.let { studioId ->
                                UserStatisticStudioEntity(studioId, source.id, MediaType.MANGA, it.count, it.meanScore, it.mediaIds, it.chaptersRead)
                            }
                        },
                tags =
                    source.statistics?.anime?.tags.orEmpty().mapNotNull {
                        it.tag?.id?.let { tagId ->
                            UserStatisticTagEntity(tagId, source.id, MediaType.ANIME, it.count, it.meanScore, it.mediaIds, it.minutesWatched)
                        }
                    } +
                        source.statistics?.manga?.tags.orEmpty().mapNotNull {
                            it.tag?.id?.let { tagId ->
                                UserStatisticTagEntity(tagId, source.id, MediaType.MANGA, it.count, it.meanScore, it.mediaIds, it.chaptersRead)
                            }
                        },
                voiceActors =
                    source.statistics?.anime?.voiceActors.orEmpty().mapNotNull {
                        it.voiceActor?.id?.let { staffId ->
                            UserStatisticVoiceActorEntity(staffId, source.id, MediaType.ANIME, it.count, it.meanScore, it.mediaIds, it.minutesWatched)
                        }
                    } +
                        source.statistics?.manga?.voiceActors.orEmpty().mapNotNull {
                            it.voiceActor?.id?.let { staffId ->
                                UserStatisticVoiceActorEntity(
                                    staffId,
                                    source.id,
                                    MediaType.MANGA,
                                    it.count,
                                    it.meanScore,
                                    it.mediaIds,
                                    it.chaptersRead,
                                )
                            }
                        },
            )
    }
}

internal class UserMediaOptionModelConverter(
    override val fromType: (UserModel.WithOptions) -> UserMediaOptionEntity = ::transform,
    override val toType: (UserMediaOptionEntity) -> UserModel.WithOptions = { throw NotImplementedError() },
) : SupportConverter<UserModel.WithOptions, UserMediaOptionEntity>() {
    private companion object : ISupportTransformer<UserModel.WithOptions, UserMediaOptionEntity> {
        override fun transform(source: UserModel.WithOptions) =
            UserMediaOptionEntity(
                userId = source.id,
                scoreFormat = source.mediaListOptions?.scoreFormat ?: ScoreFormat.POINT_100,
                rowOrder = source.mediaListOptions?.rowOrder,
                anime =
                    UserMediaOptionEntity.MediaOption(
                        customLists =
                            source.mediaListOptions
                                ?.animeList
                                ?.customLists
                                .orEmpty(),
                        sectionOrder =
                            source.mediaListOptions
                                ?.animeList
                                ?.sectionOrder
                                .orEmpty(),
                        advancedScoring =
                            source.mediaListOptions
                                ?.animeList
                                ?.advancedScoring
                                .orEmpty(),
                        advancedScoringEnabled =
                            source.mediaListOptions?.animeList?.advancedScoringEnabled
                                ?: false,
                        splitCompletedSectionByFormat =
                            source.mediaListOptions?.animeList?.splitCompletedSectionByFormat
                                ?: false,
                    ),
                manga =
                    UserMediaOptionEntity.MediaOption(
                        customLists =
                            source.mediaListOptions
                                ?.mangaList
                                ?.customLists
                                .orEmpty(),
                        sectionOrder =
                            source.mediaListOptions
                                ?.mangaList
                                ?.sectionOrder
                                .orEmpty(),
                        advancedScoring =
                            source.mediaListOptions
                                ?.mangaList
                                ?.advancedScoring
                                .orEmpty(),
                        advancedScoringEnabled =
                            source.mediaListOptions?.mangaList?.advancedScoringEnabled
                                ?: false,
                        splitCompletedSectionByFormat =
                            source.mediaListOptions?.mangaList?.splitCompletedSectionByFormat
                                ?: false,
                    ),
            )
    }
}

internal class UserGeneralOptionModelConverter(
    override val fromType: (UserModel.WithOptions) -> UserGeneralOptionEntity = ::transform,
    override val toType: (UserGeneralOptionEntity) -> UserModel.WithOptions = { throw NotImplementedError() },
) : SupportConverter<UserModel.WithOptions, UserGeneralOptionEntity>() {
    private companion object : ISupportTransformer<UserModel.WithOptions, UserGeneralOptionEntity> {
        override fun transform(source: UserModel.WithOptions) =
            when (source) {
                is UserModel.Extended ->
                    UserGeneralOptionEntity(
                        userId = source.id,
                        airingNotifications = false,
                        displayAdultContent = source.options?.displayAdultContent ?: false,
                        notificationOption = emptyList(),
                        titleLanguage = source.options?.titleLanguage ?: UserTitleLanguage.ROMAJI,
                        profileColor = source.options?.profileColor,
                        timeZone = null,
                        staffNameLanguage = null,
                    )
                is UserModel.Viewer ->
                    UserGeneralOptionEntity(
                        userId = source.id,
                        airingNotifications = source.options?.airingNotifications ?: false,
                        displayAdultContent = source.options?.displayAdultContent ?: false,
                        notificationOption =
                            source.options
                                ?.notificationOptions
                                ?.mapNotNull { option ->
                                    option.notificationType?.let { notificationType ->
                                        UserGeneralOptionEntity.NotificationOption(
                                            enabled = option.enabled,
                                            notificationType = notificationType,
                                        )
                                    }
                                }.orEmpty(),
                        titleLanguage = source.options?.titleLanguage ?: UserTitleLanguage.ROMAJI,
                        profileColor = source.options?.profileColor,
                        timeZone = source.options?.timeZone,
                        staffNameLanguage = source.options?.staffNameLanguage ?: UserStaffNameLanguage.ROMAJI_WESTERN,
                    )
                else -> error("$source type does not contain any models of type UserGeneralOption")
            }
    }
}

internal class UserEntityConverter(
    override val fromType: (UserEntity) -> User = ::transform,
    override val toType: (User) -> UserEntity = { throw NotImplementedError() },
) : SupportConverter<UserEntity, User>() {
    private companion object : ISupportTransformer<UserEntity, User> {
        override fun transform(source: UserEntity) =
            User.Core(
                name = source.about.name,
                avatar =
                    UserImage(
                        large = source.coverImage.large,
                        medium = source.coverImage.medium,
                        banner = source.coverImage.banner,
                    ),
                status =
                    UserStatus(
                        about = source.about.bio,
                        donationBadge = source.about.donatorBadge,
                        donationTier = source.about.donatorTier,
                        isFollowing = source.status?.isFollowing,
                        isFollower = source.status?.isFollower,
                        isBlocked = source.status?.isBlocked,
                        pageUrl = source.about.siteUrl,
                        createdAt = source.createdAt,
                        updatedAt = source.updatedAt,
                    ),
                id = source.id,
            )
    }
}

internal class UserViewEntityConverter(
    override val fromType: (UserEntityView) -> User = ::transform,
    override val toType: (User) -> UserEntityView = { throw NotImplementedError() },
) : SupportConverter<UserEntityView, User>() {
    private companion object : ISupportTransformer<UserEntityView, User> {
        @VisibleForTesting
        fun UserMediaOptionEntity.MediaOption.listStatus(
            mediaType: MediaType,
            userEntityView: UserEntityView.WithExtended,
        ): List<MediaListInfo> {
            val mediaListInfoList =
                MediaListStatus.values().map {
                    MediaListInfo(
                        isCustomList = false,
                        mediaType = mediaType,
                        name = it.name,
                        count =
                            userEntityView.mediaListCount
                                .filter { countView ->
                                    countView.mediaType == mediaType && countView.listStatus == it
                                }.sumOf(MediaListCountView::listCount),
                    )
                }

            val customListInfoList =
                customLists.map {
                    MediaListInfo(
                        isCustomList = true,
                        mediaType = mediaType,
                        name = it,
                        count =
                            userEntityView.customListCount
                                .filter { countView ->
                                    countView.mediaType == mediaType && countView.customListName == it
                                }.sumOf(CustomListCountView::listCount),
                    )
                }

            return mediaListInfoList + customListInfoList
        }

        private fun UserStatisticEntityView.toAnimeStatistic(): Statistic.Anime? {
            val summary = statistic.statistic
            val count = summary.animeCount ?: return null
            return Statistic.Anime(
                minutesWatched = summary.animeMinutesWatched ?: 0,
                episodesWatched = summary.animeEpisodesWatched ?: 0,
                count = count,
                meanScore = summary.animeMeanScore ?: 0f,
                standardDeviation = summary.animeStandardDeviation ?: 0f,
                countries =
                    countries
                        .filter {
                            it.mediaType == MediaType.ANIME
                        }.map { MediaStatistic.Anime.Country(it.country, it.count, it.meanScore, it.mediaIds, it.trackedAmount) },
                formats =
                    formats
                        .filter {
                            it.mediaType == MediaType.ANIME
                        }.map { MediaStatistic.Anime.Format(it.format, it.count, it.meanScore, it.mediaIds, it.trackedAmount) },
                genres =
                    genres
                        .filter {
                            it.mediaType == MediaType.ANIME
                        }.map { MediaStatistic.Anime.Genre(it.genre, it.count, it.meanScore, it.mediaIds, it.trackedAmount) },
                lengths =
                    lengths
                        .filter {
                            it.mediaType == MediaType.ANIME
                        }.map { MediaStatistic.Anime.Length(it.length, it.count, it.meanScore, it.mediaIds, it.trackedAmount) },
                releaseYears =
                    releaseYears
                        .filter {
                            it.mediaType == MediaType.ANIME
                        }.map { MediaStatistic.Anime.ReleaseYear(it.releaseYear, it.count, it.meanScore, it.mediaIds, it.trackedAmount) },
                scores =
                    scores
                        .filter {
                            it.mediaType == MediaType.ANIME
                        }.map { MediaStatistic.Anime.Score(it.score, it.count, it.meanScore, it.mediaIds, it.trackedAmount) },
                staff =
                    staff
                        .filter {
                            it.connection.mediaType == MediaType.ANIME
                        }.map {
                            MediaStatistic.Anime.Staff(
                                it.staff?.let(koinOf<StaffEntityConverter>()::convertFrom),
                                it.connection.count,
                                it.connection.meanScore,
                                it.connection.mediaIds,
                                it.connection.trackedAmount,
                            )
                        },
                startYears =
                    startYears
                        .filter {
                            it.mediaType == MediaType.ANIME
                        }.map { MediaStatistic.Anime.StartYear(it.startYear, it.count, it.meanScore, it.mediaIds, it.trackedAmount) },
                statuses =
                    statuses
                        .filter {
                            it.mediaType == MediaType.ANIME
                        }.map { MediaStatistic.Anime.Status(it.status, it.count, it.meanScore, it.mediaIds, it.trackedAmount) },
                studios =
                    studios
                        .filter {
                            it.connection.mediaType == MediaType.ANIME
                        }.map {
                            MediaStatistic.Anime.Studio(
                                it.studio?.let(koinOf<StudioEntityConverter>()::convertFrom),
                                it.connection.count,
                                it.connection.meanScore,
                                it.connection.mediaIds,
                                it.connection.trackedAmount,
                            )
                        },
                tags =
                    tags
                        .filter {
                            it.connection.mediaType == MediaType.ANIME
                        }.map {
                            MediaStatistic.Anime.Tag(
                                it.tag?.let(koinOf<TagEntityConverter>()::convertFrom),
                                it.connection.count,
                                it.connection.meanScore,
                                it.connection.mediaIds,
                                it.connection.trackedAmount,
                            )
                        },
                voiceActors =
                    voiceActors
                        .filter {
                            it.connection.mediaType == MediaType.ANIME
                        }.map {
                            MediaStatistic.Anime.VoiceActor(
                                it.voiceActor?.let(koinOf<StaffEntityConverter>()::convertFrom),
                                it.connection.count,
                                it.connection.meanScore,
                                it.connection.mediaIds,
                                it.connection.trackedAmount,
                            )
                        },
            )
        }

        private fun UserStatisticEntityView.toMangaStatistic(): Statistic.Manga? {
            val summary = statistic.statistic
            val count = summary.mangaCount ?: return null
            return Statistic.Manga(
                chaptersRead = summary.mangaChaptersRead ?: 0,
                volumesRead = summary.mangaVolumesRead ?: 0,
                count = count,
                meanScore = summary.mangaMeanScore ?: 0f,
                standardDeviation = summary.mangaStandardDeviation ?: 0f,
                countries =
                    countries
                        .filter {
                            it.mediaType == MediaType.MANGA
                        }.map { MediaStatistic.Manga.Country(it.country, it.count, it.meanScore, it.mediaIds, it.trackedAmount) },
                formats =
                    formats
                        .filter {
                            it.mediaType == MediaType.MANGA
                        }.map { MediaStatistic.Manga.Format(it.format, it.count, it.meanScore, it.mediaIds, it.trackedAmount) },
                genres =
                    genres
                        .filter {
                            it.mediaType == MediaType.MANGA
                        }.map { MediaStatistic.Manga.Genre(it.genre, it.count, it.meanScore, it.mediaIds, it.trackedAmount) },
                lengths =
                    lengths
                        .filter {
                            it.mediaType == MediaType.MANGA
                        }.map { MediaStatistic.Manga.Length(it.length, it.count, it.meanScore, it.mediaIds, it.trackedAmount) },
                releaseYears =
                    releaseYears
                        .filter {
                            it.mediaType == MediaType.MANGA
                        }.map { MediaStatistic.Manga.ReleaseYear(it.releaseYear, it.count, it.meanScore, it.mediaIds, it.trackedAmount) },
                scores =
                    scores
                        .filter {
                            it.mediaType == MediaType.MANGA
                        }.map { MediaStatistic.Manga.Score(it.score, it.count, it.meanScore, it.mediaIds, it.trackedAmount) },
                staff =
                    staff
                        .filter {
                            it.connection.mediaType == MediaType.MANGA
                        }.map {
                            MediaStatistic.Manga.Staff(
                                it.staff?.let(koinOf<StaffEntityConverter>()::convertFrom),
                                it.connection.count,
                                it.connection.meanScore,
                                it.connection.mediaIds,
                                it.connection.trackedAmount,
                            )
                        },
                startYears =
                    startYears
                        .filter {
                            it.mediaType == MediaType.MANGA
                        }.map { MediaStatistic.Manga.StartYear(it.startYear, it.count, it.meanScore, it.mediaIds, it.trackedAmount) },
                statuses =
                    statuses
                        .filter {
                            it.mediaType == MediaType.MANGA
                        }.map { MediaStatistic.Manga.Status(it.status, it.count, it.meanScore, it.mediaIds, it.trackedAmount) },
                studios =
                    studios
                        .filter {
                            it.connection.mediaType == MediaType.MANGA
                        }.map {
                            MediaStatistic.Manga.Studio(
                                it.studio?.let(koinOf<StudioEntityConverter>()::convertFrom),
                                it.connection.count,
                                it.connection.meanScore,
                                it.connection.mediaIds,
                                it.connection.trackedAmount,
                            )
                        },
                tags =
                    tags
                        .filter {
                            it.connection.mediaType == MediaType.MANGA
                        }.map {
                            MediaStatistic.Manga.Tag(
                                it.tag?.let(koinOf<TagEntityConverter>()::convertFrom),
                                it.connection.count,
                                it.connection.meanScore,
                                it.connection.mediaIds,
                                it.connection.trackedAmount,
                            )
                        },
                voiceActors =
                    voiceActors
                        .filter {
                            it.connection.mediaType == MediaType.MANGA
                        }.map {
                            MediaStatistic.Manga.VoiceActor(
                                it.voiceActor?.let(koinOf<StaffEntityConverter>()::convertFrom),
                                it.connection.count,
                                it.connection.meanScore,
                                it.connection.mediaIds,
                                it.connection.trackedAmount,
                            )
                        },
            )
        }

        override fun transform(source: UserEntityView) =
            when (source) {
                is UserEntityView.WithOptions ->
                    User.Extended(
                        listOption =
                            UserMediaListOption(
                                scoreFormat = source.mediaListOption.scoreFormat,
                                rowOrder = source.mediaListOption.rowOrder,
                                animeList =
                                    UserMediaListTypeOptions(
                                        sectionOrder = source.mediaListOption.anime.sectionOrder,
                                        splitCompletedSectionByFormat = source.mediaListOption.anime.splitCompletedSectionByFormat,
                                        customLists = source.mediaListOption.anime.customLists,
                                        advancedScoring = source.mediaListOption.anime.advancedScoring,
                                        advancedScoringEnabled = source.mediaListOption.anime.advancedScoringEnabled,
                                    ),
                                mangaList =
                                    UserMediaListTypeOptions(
                                        sectionOrder = source.mediaListOption.manga.sectionOrder,
                                        splitCompletedSectionByFormat = source.mediaListOption.manga.splitCompletedSectionByFormat,
                                        customLists = source.mediaListOption.manga.customLists,
                                        advancedScoring = source.mediaListOption.manga.advancedScoring,
                                        advancedScoringEnabled = source.mediaListOption.manga.advancedScoringEnabled,
                                    ),
                            ),
                        profileOption =
                            UserProfileOption(
                                titleLanguage = source.generalOption.titleLanguage,
                                displayAdultContent = source.generalOption.displayAdultContent,
                                airingNotifications = source.generalOption.airingNotifications,
                                notificationOptions =
                                    source.generalOption.notificationOption.mapNotNull { option ->
                                        option.notificationType?.let { notificationType ->
                                            UserNotificationOption(
                                                isEnabled = option.enabled,
                                                type = notificationType,
                                            )
                                        }
                                    },
                                profileColor = source.generalOption.profileColor,
                                timeZone = source.generalOption.timeZone,
                                staffNameLanguage = source.generalOption.staffNameLanguage,
                            ),
                        name = source.user.about.name,
                        previousNames =
                            source.previousNames.map {
                                User.PreviousName(
                                    createdAt = it.createdAt,
                                    name = it.name,
                                    updatedAt = it.updatedAt,
                                )
                            },
                        avatar =
                            UserImage(
                                large = source.user.coverImage.large,
                                medium = source.user.coverImage.medium,
                                banner = source.user.coverImage.banner,
                            ),
                        status =
                            UserStatus(
                                about = source.user.about.bio,
                                donationBadge = source.user.about.donatorBadge,
                                donationTier = source.user.about.donatorTier,
                                isFollowing = source.user.status?.isFollowing,
                                isFollower = source.user.status?.isFollower,
                                isBlocked = source.user.status?.isBlocked,
                                pageUrl = source.user.about.siteUrl,
                                createdAt = source.user.createdAt,
                                updatedAt = source.user.updatedAt,
                            ),
                        mediaListInfo =
                            source.mediaListOption.anime.listStatus(MediaType.ANIME, source) +
                                source.mediaListOption.manga.listStatus(MediaType.MANGA, source),
                        id = source.user.id,
                    )
                is UserEntityView.WithStatistic ->
                    User.WithStats(
                        listOption =
                            UserMediaListOption(
                                scoreFormat = source.mediaListOption.scoreFormat,
                                rowOrder = source.mediaListOption.rowOrder,
                                animeList =
                                    UserMediaListTypeOptions(
                                        sectionOrder = source.mediaListOption.anime.sectionOrder,
                                        splitCompletedSectionByFormat = source.mediaListOption.anime.splitCompletedSectionByFormat,
                                        customLists = source.mediaListOption.anime.customLists,
                                        advancedScoring = source.mediaListOption.anime.advancedScoring,
                                        advancedScoringEnabled = source.mediaListOption.anime.advancedScoringEnabled,
                                    ),
                                mangaList =
                                    UserMediaListTypeOptions(
                                        sectionOrder = source.mediaListOption.manga.sectionOrder,
                                        splitCompletedSectionByFormat = source.mediaListOption.manga.splitCompletedSectionByFormat,
                                        customLists = source.mediaListOption.manga.customLists,
                                        advancedScoring = source.mediaListOption.manga.advancedScoring,
                                        advancedScoringEnabled = source.mediaListOption.manga.advancedScoringEnabled,
                                    ),
                            ),
                        profileOption =
                            UserProfileOption(
                                titleLanguage = source.generalOption.titleLanguage,
                                displayAdultContent = source.generalOption.displayAdultContent,
                                airingNotifications = source.generalOption.airingNotifications,
                                notificationOptions =
                                    source.generalOption.notificationOption.mapNotNull { option ->
                                        option.notificationType?.let { notificationType ->
                                            UserNotificationOption(
                                                isEnabled = option.enabled,
                                                type = notificationType,
                                            )
                                        }
                                    },
                                profileColor = source.generalOption.profileColor,
                                timeZone = source.generalOption.timeZone,
                                staffNameLanguage = source.generalOption.staffNameLanguage,
                            ),
                        statistics =
                            UserMediaStatisticType(
                                anime = source.statistic?.toAnimeStatistic(),
                                manga = source.statistic?.toMangaStatistic(),
                            ),
                        name = source.user.about.name,
                        previousNames =
                            source.previousNames.map {
                                User.PreviousName(
                                    createdAt = it.createdAt,
                                    name = it.name,
                                    updatedAt = it.updatedAt,
                                )
                            },
                        avatar =
                            UserImage(
                                large = source.user.coverImage.large,
                                medium = source.user.coverImage.medium,
                                banner = source.user.coverImage.banner,
                            ),
                        status =
                            UserStatus(
                                about = source.user.about.bio,
                                donationBadge = source.user.about.donatorBadge,
                                donationTier = source.user.about.donatorTier,
                                isFollowing = source.user.status?.isFollowing,
                                isFollower = source.user.status?.isFollower,
                                isBlocked = source.user.status?.isBlocked,
                                pageUrl = source.user.about.siteUrl,
                                createdAt = source.user.createdAt,
                                updatedAt = source.user.updatedAt,
                            ),
                        mediaListStats =
                            source.mediaListOption.anime.listStatus(MediaType.ANIME, source) +
                                source.mediaListOption.manga.listStatus(MediaType.MANGA, source),
                        id = source.user.id,
                    )
                is UserEntityView.Authenticated ->
                    User.Authenticated(
                        unreadNotifications = source.notification.unreadNotifications,
                        listOption =
                            UserMediaListOption(
                                scoreFormat = source.mediaListOption.scoreFormat,
                                rowOrder = source.mediaListOption.rowOrder,
                                animeList =
                                    UserMediaListTypeOptions(
                                        sectionOrder = source.mediaListOption.anime.sectionOrder,
                                        splitCompletedSectionByFormat = source.mediaListOption.anime.splitCompletedSectionByFormat,
                                        customLists = source.mediaListOption.anime.customLists,
                                        advancedScoring = source.mediaListOption.anime.advancedScoring,
                                        advancedScoringEnabled = source.mediaListOption.anime.advancedScoringEnabled,
                                    ),
                                mangaList =
                                    UserMediaListTypeOptions(
                                        sectionOrder = source.mediaListOption.manga.sectionOrder,
                                        splitCompletedSectionByFormat = source.mediaListOption.manga.splitCompletedSectionByFormat,
                                        customLists = source.mediaListOption.manga.customLists,
                                        advancedScoring = source.mediaListOption.manga.advancedScoring,
                                        advancedScoringEnabled = source.mediaListOption.manga.advancedScoringEnabled,
                                    ),
                            ),
                        profileOption =
                            UserProfileOption(
                                titleLanguage = source.generalOption.titleLanguage,
                                displayAdultContent = source.generalOption.displayAdultContent,
                                airingNotifications = source.generalOption.airingNotifications,
                                notificationOptions =
                                    source.generalOption.notificationOption.mapNotNull { option ->
                                        option.notificationType?.let { notificationType ->
                                            UserNotificationOption(
                                                isEnabled = option.enabled,
                                                type = notificationType,
                                            )
                                        }
                                    },
                                profileColor = source.generalOption.profileColor,
                                timeZone = source.generalOption.timeZone,
                                staffNameLanguage = source.generalOption.staffNameLanguage,
                            ),
                        name = source.user.about.name,
                        avatar =
                            UserImage(
                                large = source.user.coverImage.large,
                                medium = source.user.coverImage.medium,
                                banner = source.user.coverImage.banner,
                            ),
                        status =
                            UserStatus(
                                about = source.user.about.bio,
                                donationBadge = source.user.about.donatorBadge,
                                donationTier = source.user.about.donatorTier,
                                isFollowing = source.user.status?.isFollowing,
                                isFollower = source.user.status?.isFollower,
                                isBlocked = source.user.status?.isBlocked,
                                pageUrl = source.user.about.siteUrl,
                                createdAt = source.user.createdAt,
                                updatedAt = source.user.updatedAt,
                            ),
                        id = source.user.id,
                    )
                else -> throw NotImplementedError("Instance of $source is not supported")
            }
    }
}
