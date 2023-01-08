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

package co.anitrend.data.android.database.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import org.intellij.lang.annotations.Language
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
        val tableName = "media_list"
        @Language("sql") val createQuery = """
            CREATE TABLE `${tableName}_temp`(
                `media_type` TEXT NOT NULL, 
                `completed_at` TEXT, 
                `created_at` INTEGER, 
                `hidden_from_status` INTEGER NOT NULL, 
                `media_id` INTEGER NOT NULL, 
                `notes` TEXT, 
                `priority` INTEGER, 
                `hidden` INTEGER NOT NULL, 
                `progress` INTEGER NOT NULL, 
                `progress_volumes` INTEGER NOT NULL, 
                `repeat_count` INTEGER NOT NULL, 
                `score` REAL NOT NULL, 
                `started_at` TEXT, 
                `status` TEXT NOT NULL, 
                `updated_at` INTEGER, 
                `user_id` INTEGER NOT NULL, 
                `id` INTEGER NOT NULL, 
                PRIMARY KEY(`id`)
            );
            
            INSERT INTO `${tableName}_temp` SELECT media_type, completed_at, created_at, 
            hidden_from_status, media_id, notes, priority, hidden, progress, progress_volumes, 
            repeat_count, score, started_at, status, updated_at, user_id, id
            FROM `${tableName}`;
            
            DROP TABLE `${tableName}`;
            
            ALTER TABLE `${tableName}_temp` RENAME TO `${tableName}`;
        """.trimIndent()
        database.usingTransaction("MIGRATION_1_2", createQuery)
    }
}

internal val migrations = arrayOf(MIGRATION_1_2)