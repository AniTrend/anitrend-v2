/*
 * Copyright (C) 2019  AniTrend
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
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import co.anitrend.data.airing.entity.AiringScheduleEntity
import co.anitrend.data.android.database.common.IAniTrendStore
import co.anitrend.data.android.database.converter.TypeConverterEnum
import co.anitrend.data.android.database.converter.TypeConverterObject
import co.anitrend.data.android.database.migration.migrations
import co.anitrend.data.auth.entity.AuthEntity
import co.anitrend.data.android.cache.entity.CacheEntity
import co.anitrend.data.character.entity.CharacterEntity
import co.anitrend.data.character.entity.fts.CharacterFtsEntity
import co.anitrend.data.feed.episode.entity.EpisodeEntity
import co.anitrend.data.feed.episode.entity.fts.EpisodeFtsEntity
import co.anitrend.data.genre.entity.GenreEntity
import co.anitrend.data.genre.entity.connection.GenreConnectionEntity
import co.anitrend.data.jikan.media.entity.JikanEntity
import co.anitrend.data.link.entity.LinkEntity
import co.anitrend.data.media.entity.MediaEntity
import co.anitrend.data.media.entity.fts.MediaFtsEntity
import co.anitrend.data.medialist.entity.MediaListEntity
import co.anitrend.data.relation.entity.RelationEntity
import co.anitrend.data.feed.news.entity.NewsEntity
import co.anitrend.data.feed.news.entity.fts.NewsFtsEntity
import co.anitrend.data.rank.entity.RankEntity
import co.anitrend.data.staff.entity.StaffEntity
import co.anitrend.data.staff.entity.fts.StaffFtsEntity
import co.anitrend.data.studio.entity.StudioEntity
import co.anitrend.data.studio.entity.fts.StudioFtsEntity
import co.anitrend.data.tag.entity.TagEntity
import co.anitrend.data.tag.entity.connection.TagConnectionEntity
import co.anitrend.data.user.entity.UserEntity
import co.anitrend.data.user.entity.fts.UserFtsEntity
import co.anitrend.data.user.entity.option.UserGeneralOptionEntity
import co.anitrend.data.user.entity.option.UserMediaOptionEntity
import co.anitrend.data.user.entity.statistic.UserWithStatisticEntity
import org.jetbrains.annotations.TestOnly

@Database(
    entities = [
        CacheEntity::class, RelationEntity::class,
        AuthEntity::class, TagEntity::class , TagConnectionEntity::class,
        GenreEntity::class, GenreConnectionEntity::class,
        MediaEntity::class, MediaFtsEntity::class, AiringScheduleEntity::class,
        UserEntity::class, UserFtsEntity::class, UserGeneralOptionEntity::class,
        UserMediaOptionEntity::class, UserWithStatisticEntity::class, MediaListEntity::class,
        NewsEntity::class, NewsFtsEntity::class, EpisodeEntity::class, EpisodeFtsEntity::class,
        CharacterEntity::class, CharacterFtsEntity::class, StudioEntity::class, StudioFtsEntity::class,
        StaffEntity::class, StaffFtsEntity::class, LinkEntity::class, RankEntity::class,
        JikanEntity::class, JikanEntity.StudioEntity::class, JikanEntity.LicensorEntity::class,
        JikanEntity.ProducerEntity::class, JikanEntity.AuthorEntity::class
    ],
    version = AniTrendStore.DATABASE_SCHEMA_VERSION,
    views = [TagEntity.Extended::class, GenreEntity.Extended::class]
)
@TypeConverters(
    value = [
        TypeConverterObject::class,
        TypeConverterEnum::class
    ]
)
internal abstract class AniTrendStore: RoomDatabase(), IAniTrendStore {

    companion object {
        const val DATABASE_SCHEMA_VERSION = 1

        internal fun create(applicationContext: Context): IAniTrendStore {
            return Room.databaseBuilder(
                applicationContext,
                AniTrendStore::class.java,
                "anitrend-db"
            ).fallbackToDestructiveMigration()
                .addMigrations(*migrations)
                .build()
        }

        @TestOnly
        internal fun create(
            applicationContext: Context,
            migrations: Array<Migration>
        ): IAniTrendStore {
            return Room.inMemoryDatabaseBuilder(
                applicationContext,
                AniTrendStore::class.java,
            ).allowMainThreadQueries()
                .addMigrations(*migrations)
                .build()
        }
    }
}
