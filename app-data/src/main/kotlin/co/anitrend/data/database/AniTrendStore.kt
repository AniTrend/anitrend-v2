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

package co.anitrend.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import co.anitrend.data.BuildConfig
import co.anitrend.data.auth.model.JsonWebToken
import co.anitrend.data.database.migration.MIGRATION_1_2
import co.anitrend.data.auth.datasource.local.JsonWebTokenDao
import co.anitrend.data.genre.datasource.local.MediaGenreLocalSource
import co.anitrend.data.tag.datasource.local.MediaTagLocalSource
import co.anitrend.data.media.model.remote.MediaGenre
import co.anitrend.data.media.model.remote.MediaTag

@Database(
    entities = [
        JsonWebToken::class, MediaTag::class, MediaGenre::class
    ],
    version = BuildConfig.DATABASE_SCHEMA_VERSION
)
abstract class AniTrendStore: RoomDatabase() {

    abstract fun jsonWebTokenDao(): JsonWebTokenDao
    abstract fun mediaTagDao(): MediaTagLocalSource
    abstract fun mediaGenreDao(): MediaGenreLocalSource

    companion object {

        fun create(applicationContext: Context): AniTrendStore {
            return Room.databaseBuilder(
                applicationContext,
                AniTrendStore::class.java,
                "anitrend-data"
            ).fallbackToDestructiveMigration()
                .addMigrations(MIGRATION_1_2)
                .build()
        }
    }
}
