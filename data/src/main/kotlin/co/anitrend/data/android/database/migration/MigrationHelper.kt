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

private fun SupportSQLiteDatabase.usingTransactionStatements(
    tag: String,
    vararg queries: String,
) {
    beginTransaction()
    runCatching {
        queries.forEach { execSQL(it) }
    }.onSuccess {
        setTransactionSuccessful()
    }.onFailure {
        Timber.tag(tag).e(it)
    }
    endTransaction()
}

private fun migrationOf(
    from: Int,
    to: Int,
    block: () -> String,
) = object : Migration(from, to) {
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
        db.usingTransaction("MIGRATION_${from}_$to", block())
    }
}

private fun migrationOfStatements(
    from: Int,
    to: Int,
    block: () -> Array<String>,
) = object : Migration(from, to) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.usingTransactionStatements("MIGRATION_${from}_$to", *block())
    }
}

internal val MIGRATIONS =
    arrayOf(
        migrationOf(1, 2) {
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
        },
        migrationOf(9, 11) {
            """
            DROP TABLE IF EXISTS relation;
            DROP TABLE IF EXISTS jikan;
            """.trimIndent()
        },
        migrationOf(11, 12) {
            """
            ALTER TABLE edge_media ADD COLUMN broadcast TEXT;
            ALTER TABLE edge_media ADD COLUMN kind TEXT;
            ALTER TABLE edge_media ADD COLUMN chapters INTEGER;
            ALTER TABLE edge_media ADD COLUMN volumes INTEGER;
            ALTER TABLE edge_media ADD COLUMN more_info TEXT;
            ALTER TABLE edge_media ADD COLUMN published_from INTEGER;
            ALTER TABLE edge_media ADD COLUMN published_to INTEGER;
            ALTER TABLE edge_media ADD COLUMN schedule_next_episode_detail_id INTEGER;
            ALTER TABLE edge_media ADD COLUMN schedule_next_episode_detail_air_date INTEGER;
            ALTER TABLE edge_media ADD COLUMN schedule_next_episode_detail_episode_number INTEGER;
            ALTER TABLE edge_media ADD COLUMN schedule_next_episode_detail_image TEXT;
            ALTER TABLE edge_media ADD COLUMN schedule_next_episode_detail_name TEXT;
            ALTER TABLE edge_media ADD COLUMN schedule_next_episode_detail_overview TEXT;
            ALTER TABLE edge_media ADD COLUMN schedule_next_episode_detail_production_code TEXT;
            ALTER TABLE edge_media ADD COLUMN schedule_next_episode_detail_runtime INTEGER;
            ALTER TABLE edge_media ADD COLUMN schedule_next_episode_detail_season_number INTEGER;
            ALTER TABLE edge_media ADD COLUMN schedule_next_episode_detail_tmdb_id INTEGER;
            ALTER TABLE edge_media ADD COLUMN schedule_last_episode_detail_id INTEGER;
            ALTER TABLE edge_media ADD COLUMN schedule_last_episode_detail_air_date INTEGER;
            ALTER TABLE edge_media ADD COLUMN schedule_last_episode_detail_episode_number INTEGER;
            ALTER TABLE edge_media ADD COLUMN schedule_last_episode_detail_image TEXT;
            ALTER TABLE edge_media ADD COLUMN schedule_last_episode_detail_name TEXT;
            ALTER TABLE edge_media ADD COLUMN schedule_last_episode_detail_overview TEXT;
            ALTER TABLE edge_media ADD COLUMN schedule_last_episode_detail_production_code TEXT;
            ALTER TABLE edge_media ADD COLUMN schedule_last_episode_detail_runtime INTEGER;
            ALTER TABLE edge_media ADD COLUMN schedule_last_episode_detail_season_number INTEGER;
            ALTER TABLE edge_media ADD COLUMN schedule_last_episode_detail_tmdb_id INTEGER;
            """.trimIndent()
        },
        migrationOf(12, 13) {
            """
            CREATE TABLE IF NOT EXISTS `media_character_connection` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT,
                `media_id` INTEGER NOT NULL,
                `character_id` INTEGER NOT NULL,
                `role` TEXT,
                `media_role_name` TEXT,
                `sort_index` INTEGER NOT NULL,
                `image_large` TEXT,
                `image_medium` TEXT,
                `name_first` TEXT,
                `name_full` TEXT,
                `name_last` TEXT,
                `name_middle` TEXT,
                `name_native` TEXT,
                `name_user_preferred` TEXT,
                `name_alternative` TEXT NOT NULL,
                `name_alternative_spoiler` TEXT NOT NULL,
                `site_url` TEXT,
                `voice_actor_id` INTEGER,
                `voice_actor_name_full` TEXT,
                `voice_actor_name_user_preferred` TEXT
            );
            CREATE UNIQUE INDEX IF NOT EXISTS `index_media_character_connection_media_id_character_id`
            ON `media_character_connection` (`media_id`, `character_id`);
            CREATE INDEX IF NOT EXISTS `index_media_character_connection_media_id_sort_index`
            ON `media_character_connection` (`media_id`, `sort_index`);

            CREATE TABLE IF NOT EXISTS `media_staff_connection` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT,
                `media_id` INTEGER NOT NULL,
                `staff_id` INTEGER NOT NULL,
                `role` TEXT,
                `language` TEXT,
                `sort_index` INTEGER NOT NULL,
                `image_large` TEXT,
                `image_medium` TEXT,
                `name_first` TEXT,
                `name_full` TEXT,
                `name_last` TEXT,
                `name_middle` TEXT,
                `name_native` TEXT,
                `name_user_preferred` TEXT,
                `name_alternative` TEXT NOT NULL,
                `name_alternative_spoiler` TEXT NOT NULL,
                `site_url` TEXT
            );
            CREATE UNIQUE INDEX IF NOT EXISTS `index_media_staff_connection_media_id_staff_id`
            ON `media_staff_connection` (`media_id`, `staff_id`);
            CREATE INDEX IF NOT EXISTS `index_media_staff_connection_media_id_sort_index`
            ON `media_staff_connection` (`media_id`, `sort_index`);
            """.trimIndent()
        },
        migrationOfStatements(19, 20) {
            arrayOf(
                "DROP TABLE IF EXISTS `user_profile_activity`",
                "DROP TABLE IF EXISTS `user_profile_favourite_media`",
                "DROP TABLE IF EXISTS `user_profile_review`",
                "CREATE TABLE IF NOT EXISTS `user_profile_favourite_media` (`user_id` INTEGER NOT NULL, `media_id` INTEGER NOT NULL, `category` TEXT NOT NULL, `sort_index` INTEGER NOT NULL, `title_romaji` TEXT, `title_english` TEXT, `title_native` TEXT, `title_user_preferred` TEXT, `cover_color` TEXT, `cover_large` TEXT, `cover_medium` TEXT, `media_type` TEXT, `media_format` TEXT, `media_status` TEXT, `episodes` INTEGER, `chapters` INTEGER, `volumes` INTEGER, `is_favourite` INTEGER, `mean_score` INTEGER, `average_score` INTEGER, `site_url` TEXT, `media_list_status` TEXT, `media_list_progress` INTEGER, `media_list_volume_progress` INTEGER, `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, FOREIGN KEY(`user_id`) REFERENCES `user`(`id`) ON UPDATE CASCADE ON DELETE CASCADE )",
                "CREATE UNIQUE INDEX IF NOT EXISTS `index_user_profile_favourite_media_user_id_media_id_category` ON `user_profile_favourite_media` (`user_id`, `media_id`, `category`)",
                "CREATE INDEX IF NOT EXISTS `index_user_profile_favourite_media_user_id_sort_index` ON `user_profile_favourite_media` (`user_id`, `sort_index`)",
                "CREATE TABLE IF NOT EXISTS `user_profile_review` (`user_id` INTEGER NOT NULL, `review_id` INTEGER NOT NULL, `sort_index` INTEGER NOT NULL, `summary` TEXT, `score` INTEGER, `rating` INTEGER, `rating_amount` INTEGER, `site_url` TEXT, `created_at` INTEGER NOT NULL, `updated_at` INTEGER NOT NULL, `review_media_id` INTEGER NOT NULL, `review_media_type` TEXT, `media_title_romaji` TEXT, `media_title_english` TEXT, `media_title_native` TEXT, `media_title_user_preferred` TEXT, `media_cover_color` TEXT, `media_cover_large` TEXT, `media_cover_medium` TEXT, `media_type` TEXT, `media_format` TEXT, `media_status` TEXT, `media_episodes` INTEGER, `media_chapters` INTEGER, `media_volumes` INTEGER, `media_is_favourite` INTEGER, `media_mean_score` INTEGER, `media_average_score` INTEGER, `media_site_url` TEXT, `media_list_status` TEXT, `media_list_progress` INTEGER, `media_list_volume_progress` INTEGER, `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, FOREIGN KEY(`user_id`) REFERENCES `user`(`id`) ON UPDATE CASCADE ON DELETE CASCADE )",
                "CREATE UNIQUE INDEX IF NOT EXISTS `index_user_profile_review_user_id_review_id` ON `user_profile_review` (`user_id`, `review_id`)",
                "CREATE INDEX IF NOT EXISTS `index_user_profile_review_user_id_sort_index` ON `user_profile_review` (`user_id`, `sort_index`)",
                "CREATE TABLE IF NOT EXISTS `list_status` (`user_id` INTEGER NOT NULL, `created_at` INTEGER NOT NULL, `sort_index` INTEGER NOT NULL, `activity_status` TEXT, `progress` TEXT, `site_url` TEXT, `activity_type` TEXT, `media_id` INTEGER, `media_title_romaji` TEXT, `media_title_english` TEXT, `media_title_native` TEXT, `media_title_user_preferred` TEXT, `media_cover_color` TEXT, `media_cover_large` TEXT, `media_cover_medium` TEXT, `media_type` TEXT, `media_format` TEXT, `media_status` TEXT, `media_episodes` INTEGER, `media_chapters` INTEGER, `media_volumes` INTEGER, `media_is_favourite` INTEGER, `media_mean_score` INTEGER, `media_average_score` INTEGER, `media_site_url` TEXT, `media_list_status` TEXT, `media_list_progress` INTEGER, `media_list_volume_progress` INTEGER, `id` INTEGER NOT NULL, PRIMARY KEY(`id`), FOREIGN KEY(`user_id`) REFERENCES `user`(`id`) ON UPDATE CASCADE ON DELETE CASCADE )",
                "CREATE INDEX IF NOT EXISTS `index_list_status_user_id_sort_index` ON `list_status` (`user_id`, `sort_index`)",
            )
        },
    )
