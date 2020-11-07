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

package co.anitrend.data.arch.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import co.anitrend.data.airing.entity.AiringScheduleEntity
import co.anitrend.data.arch.database.common.IAniTrendStore
import co.anitrend.data.arch.database.converter.TypeConverterEnum
import co.anitrend.data.arch.database.converter.TypeConverterObject
import co.anitrend.data.arch.database.migration.migrations
import co.anitrend.data.auth.entity.JwtEntity
import co.anitrend.data.cache.entity.CacheEntity
import co.anitrend.data.genre.entity.GenreEntity
import co.anitrend.data.media.entity.MediaEntity
import co.anitrend.data.moe.entity.MoeEntity
import co.anitrend.data.tag.entity.TagEntity
import org.jetbrains.annotations.TestOnly

@Database(
    entities = [
        JwtEntity::class, TagEntity::class, GenreEntity::class, MoeEntity::class,
        MediaEntity::class, AiringScheduleEntity::class, CacheEntity::class
    ],
    version = AniTrendStore.DATABASE_SCHEMA_VERSION
)
@TypeConverters(
    value = [
        TypeConverterObject::class,
        TypeConverterEnum::class
    ]
)
internal abstract class AniTrendStore: RoomDatabase(), IAniTrendStore {

    companion object {
        const val DATABASE_SCHEMA_VERSION = 8

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
