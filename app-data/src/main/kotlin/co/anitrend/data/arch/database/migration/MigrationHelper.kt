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

package co.anitrend.data.arch.database.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import timber.log.Timber

private fun SupportSQLiteDatabase.usingTransaction(tag: String, query: String) {
    beginTransaction()
    runCatching {
        execSQL(query)
    }.onSuccess {
        setTransactionSuccessful()
    }.onFailure {
        Timber.tag(tag).e(it)
    }
    endTransaction()
}

internal val MIGRATION_1_2 = object : Migration(1, 2) {
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
        val tableName = "source_entity"
        val createQuery = """
            CREATE TABLE IF NOT EXISTS `$tableName` (
                    `anilist` INTEGER NOT NULL, 
                    `anidb` INTEGER, 
                    `kitsu` INTEGER, 
                    `mal` INTEGER,
                    PRIMARY KEY(`anilist`)
                )
        """.trimIndent()
        database.usingTransaction("MIGRATION_1_2", createQuery)
    }
}

internal val migrations = arrayOf(MIGRATION_1_2)