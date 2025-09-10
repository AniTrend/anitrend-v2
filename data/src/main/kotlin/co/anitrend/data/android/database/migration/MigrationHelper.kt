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
package co.anitrend.data.android.database.migration

import androidx.room.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.db.SupportSQLiteDatabase
import timber.log.Timber

private fun SupportSQLiteDatabase.usingTransaction(
    tag: String,
    query: String,
) {
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

private fun migrationOf(from: Int, to: Int, block: () -> String) =
    object : Migration(from, to) {
        /**
         * Should run the necessary migrations.
         *
         * The Migration class cannot access any generated Dao in this method.
         *
         * This method is already called inside a transaction and that transaction might actually be a
         * composite transaction of all necessary `Migration`s.
         *
         * This function is only called when Room is configured without a driver. If a driver is set
         * using [androidx.room.RoomDatabase.Builder.setDriver], then only the version that receives a
         * [SQLiteConnection] is called.
         *
         * @param db The database instance
         * @throws NotImplementedError if migrate(SQLiteConnection) is not overridden.
         */
        override fun migrate(db: SupportSQLiteDatabase) {
            db.usingTransaction("MIGRATION_${from}_${to}", block())
        }
    }

internal val MIGRATIONS = arrayOf(
    migrationOf(1, 2, {
        val tableName = "media_list"
        """
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
                FROM `$tableName`;

                DROP TABLE `$tableName`;

                ALTER TABLE `${tableName}_temp` RENAME TO `$tableName`;
                """.trimIndent()
    }),
    migrationOf(9, 10,
        {
            """
            Drop table relation;
            Drop table jikan;
            """.trimIndent()
        })
)
