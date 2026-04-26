/*
 * Copyright (C) 2019 AniTrend
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
package co.anitrend.data.android.database

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import co.anitrend.data.airing.entity.AiringScheduleEntity
import co.anitrend.data.android.cache.entity.CacheEntity
import co.anitrend.data.android.database.common.IAniTrendStore
import co.anitrend.data.android.database.converter.TypeConverterEnum
import co.anitrend.data.android.database.converter.TypeConverterObject
import co.anitrend.data.android.database.migration.MIGRATIONS
import co.anitrend.data.android.database.migration.UserProfileSidecarMigrationSpec
import co.anitrend.data.auth.entity.AuthEntity
import co.anitrend.data.character.entity.CharacterEntity
import co.anitrend.data.character.entity.fts.CharacterFtsEntity
import co.anitrend.data.customlist.entity.CustomListEntity
import co.anitrend.data.customscore.entity.CustomScoreEntity
import co.anitrend.data.edge.config.entity.EdgeConfigEntity
import co.anitrend.data.edge.episode.entity.EdgeEpisodeEntity
import co.anitrend.data.edge.genre.entity.EdgeGenreEntity
import co.anitrend.data.edge.image.entity.EdgeMediaImageEntity
import co.anitrend.data.edge.media.entity.EdgeMediaEntity
import co.anitrend.data.edge.navigation.entity.EdgeNavigationEntity
import co.anitrend.data.edge.network.entity.EdgeNetworkEntity
import co.anitrend.data.edge.news.entity.EdgeNewsEntity
import co.anitrend.data.edge.season.entity.EdgeSeasonEntity
import co.anitrend.data.edge.theme.entity.EdgeThemeEntity
import co.anitrend.data.edge.trailer.entity.EdgeTrailerEntity
import co.anitrend.data.feed.episode.entity.EpisodeEntity
import co.anitrend.data.feed.episode.entity.fts.EpisodeFtsEntity
import co.anitrend.data.feed.news.entity.NewsEntity
import co.anitrend.data.feed.news.entity.fts.NewsFtsEntity
import co.anitrend.data.genre.entity.GenreEntity
import co.anitrend.data.genre.entity.connection.GenreConnectionEntity
import co.anitrend.data.link.entity.LinkEntity
import co.anitrend.data.media.entity.MediaEntity
import co.anitrend.data.media.entity.MediaStatsEntity
import co.anitrend.data.media.entity.connection.MediaCharacterConnectionEntity
import co.anitrend.data.media.entity.connection.MediaRelationConnectionEntity
import co.anitrend.data.media.entity.connection.MediaStaffConnectionEntity
import co.anitrend.data.media.entity.fts.MediaFtsEntity
import co.anitrend.data.media.entity.stats.MediaScoreDistributionEntity
import co.anitrend.data.media.entity.stats.MediaStatusDistributionEntity
import co.anitrend.data.medialist.entity.MediaListEntity
import co.anitrend.data.medialist.entity.view.CustomListCountView
import co.anitrend.data.medialist.entity.view.MediaListCountView
import co.anitrend.data.rank.entity.RankEntity
import co.anitrend.data.recommendation.entity.connection.MediaRecommendationConnectionEntity
import co.anitrend.data.review.entity.ReviewEntity
import co.anitrend.data.staff.entity.StaffEntity
import co.anitrend.data.staff.entity.fts.StaffFtsEntity
import co.anitrend.data.studio.entity.StudioEntity
import co.anitrend.data.studio.entity.connection.MediaStudioConnectionEntity
import co.anitrend.data.studio.entity.fts.StudioFtsEntity
import co.anitrend.data.tag.entity.TagEntity
import co.anitrend.data.tag.entity.connection.TagConnectionEntity
import co.anitrend.data.user.entity.UserEntity
import co.anitrend.data.user.entity.fts.UserFtsEntity
import co.anitrend.data.user.entity.name.UserPreviousNameEntity
import co.anitrend.data.user.entity.notification.UserNotificationEntity
import co.anitrend.data.user.entity.option.UserGeneralOptionEntity
import co.anitrend.data.user.entity.option.UserMediaOptionEntity
import co.anitrend.data.status.entity.StatusEntity
import co.anitrend.data.user.entity.connection.UserProfileFavouriteMediaEntity
import co.anitrend.data.user.entity.connection.UserProfileReviewEntity
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

@Database(
    entities = [
        CacheEntity::class,
        AuthEntity::class, TagEntity::class, TagConnectionEntity::class,
        GenreEntity::class, GenreConnectionEntity::class,
        MediaEntity::class, MediaFtsEntity::class, AiringScheduleEntity::class,
        UserEntity::class, UserFtsEntity::class, UserGeneralOptionEntity::class,
        UserMediaOptionEntity::class, UserWithStatisticEntity::class,
        UserStatisticCountryEntity::class, UserStatisticFormatEntity::class,
        UserStatisticGenreEntity::class, UserStatisticLengthEntity::class,
        UserStatisticReleaseYearEntity::class, UserStatisticScoreEntity::class,
        UserStatisticStaffEntity::class, UserStatisticStartYearEntity::class,
        UserStatisticStatusEntity::class, UserStatisticStudioEntity::class,
        UserStatisticTagEntity::class, UserStatisticVoiceActorEntity::class,
        UserProfileFavouriteMediaEntity::class, StatusEntity.ListStatus::class,
        UserProfileReviewEntity::class, MediaListEntity::class,
        NewsEntity::class, NewsFtsEntity::class, EpisodeEntity::class, EpisodeFtsEntity::class,
        CharacterEntity::class, CharacterFtsEntity::class, StudioEntity::class, StudioFtsEntity::class,
        StaffEntity::class, StaffFtsEntity::class, LinkEntity::class, RankEntity::class,
        MediaCharacterConnectionEntity::class, MediaStaffConnectionEntity::class,
        MediaStudioConnectionEntity::class, MediaRelationConnectionEntity::class,
        MediaRecommendationConnectionEntity::class, MediaStatsEntity::class,
        MediaScoreDistributionEntity::class, MediaStatusDistributionEntity::class,
        CustomListEntity::class, CustomScoreEntity::class,
        UserPreviousNameEntity::class, ReviewEntity::class,
        UserNotificationEntity::class,
        EdgeConfigEntity::class, EdgeEpisodeEntity::class,
        EdgeGenreEntity::class, EdgeMediaImageEntity::class,
        EdgeMediaEntity::class, EdgeNavigationEntity::class,
        EdgeNetworkEntity::class, EdgeNewsEntity::class,
        EdgeSeasonEntity::class, EdgeThemeEntity::class,
        EdgeTrailerEntity::class,
    ],
    views = [MediaListCountView::class, CustomListCountView::class],
    version = AniTrendStore.DATABASE_SCHEMA_VERSION,
    autoMigrations = [
        AutoMigration(from = 6, to = 8),
        AutoMigration(from = 8, to = 9),
        AutoMigration(from = 13, to = 14),
        AutoMigration(from = 14, to = 15),
        AutoMigration(from = 15, to = 16),
        AutoMigration(from = 16, to = 17),
        AutoMigration(from = 17, to = 18),
        AutoMigration(from = 18, to = 19, spec = UserProfileSidecarMigrationSpec::class),
    ],
)
@TypeConverters(
    value = [
        TypeConverterObject::class,
        TypeConverterEnum::class,
    ],
)
internal abstract class AniTrendStore :
    RoomDatabase(),
    IAniTrendStore {
    companion object {
        const val DATABASE_SCHEMA_VERSION = 21

        internal fun create(applicationContext: Context): IAniTrendStore =
            Room
                .databaseBuilder(
                    applicationContext,
                    AniTrendStore::class.java,
                    "anitrend-db",
                ).fallbackToDestructiveMigration(false)
                .addMigrations(*MIGRATIONS)
                .build()
    }
}
