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
import co.anitrend.data.arch.database.common.IAniTrendStore
import co.anitrend.data.arch.database.migration.migrations
import co.anitrend.data.auth.model.JsonWebToken
import co.anitrend.data.genre.entity.GenreEntity
import co.anitrend.data.tag.entity.TagEntity

@Database(
    entities = [
        JsonWebToken::class, TagEntity::class, GenreEntity::class
    ],
    version = AniTrendStore.DATABASE_SCHEMA_VERSION
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
    }
}
