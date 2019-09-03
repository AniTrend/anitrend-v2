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

package co.anitrend.data.dao.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 by lazy {
    object : Migration(1, 2) {
        /**
         * Should run the necessary migrations.
         *
         * This class cannot access any generated Dao in this method.
         *
         * This method is already called inside a transaction and that transaction might actually be a
         * composite transaction of all necessary `Migration`s.
         *
         * @param database The database instance
         */
        override fun migrate(database: SupportSQLiteDatabase) {
            database.apply {

            }
        }
    }
}