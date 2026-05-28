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
                "CREATE TABLE IF NOT EXISTS `media_stats_new` (`media_id` INTEGER NOT NULL, PRIMARY KEY(`media_id`))",
                "INSERT INTO `media_stats_new` (`media_id`) SELECT `media_id` FROM `media_stats`",
                "DROP TABLE IF EXISTS `media_stats`",
                "ALTER TABLE `media_stats_new` RENAME TO `media_stats`",
                "CREATE TABLE IF NOT EXISTS `media_score_distribution` (`amount` INTEGER NOT NULL, `score` INTEGER NOT NULL, `media_id` INTEGER NOT NULL, `id` INTEGER PRIMARY KEY AUTOINCREMENT, FOREIGN KEY(`media_id`) REFERENCES `media`(`id`) ON UPDATE CASCADE ON DELETE CASCADE )",
                "CREATE UNIQUE INDEX IF NOT EXISTS `index_media_score_distribution_media_id_score` ON `media_score_distribution` (`media_id`, `score`)",
                "CREATE INDEX IF NOT EXISTS `index_media_score_distribution_media_id` ON `media_score_distribution` (`media_id`)",
                "CREATE TABLE IF NOT EXISTS `media_status_distribution` (`amount` INTEGER NOT NULL, `status` TEXT, `media_id` INTEGER NOT NULL, `id` INTEGER PRIMARY KEY AUTOINCREMENT, FOREIGN KEY(`media_id`) REFERENCES `media`(`id`) ON UPDATE CASCADE ON DELETE CASCADE )",
                "CREATE UNIQUE INDEX IF NOT EXISTS `index_media_status_distribution_media_id_status` ON `media_status_distribution` (`media_id`, `status`)",
                "CREATE INDEX IF NOT EXISTS `index_media_status_distribution_media_id` ON `media_status_distribution` (`media_id`)",
                "DROP TABLE IF EXISTS `list_status`",
                "CREATE TABLE IF NOT EXISTS `user_profile_favourite_media` (`user_id` INTEGER NOT NULL, `media_id` INTEGER NOT NULL, `category` TEXT NOT NULL, `sort_index` INTEGER NOT NULL, `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, FOREIGN KEY(`user_id`) REFERENCES `user`(`id`) ON UPDATE CASCADE ON DELETE CASCADE , FOREIGN KEY(`media_id`) REFERENCES `media`(`id`) ON UPDATE CASCADE ON DELETE CASCADE )",
                "CREATE UNIQUE INDEX IF NOT EXISTS `index_user_profile_favourite_media_user_id_media_id_category` ON `user_profile_favourite_media` (`user_id`, `media_id`, `category`)",
                "CREATE INDEX IF NOT EXISTS `index_user_profile_favourite_media_user_id_sort_index` ON `user_profile_favourite_media` (`user_id`, `sort_index`)",
                "CREATE TABLE IF NOT EXISTS `user_profile_review` (`user_id` INTEGER NOT NULL, `review_id` INTEGER NOT NULL, `sort_index` INTEGER NOT NULL, `media_id` INTEGER NOT NULL, `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, FOREIGN KEY(`user_id`) REFERENCES `user`(`id`) ON UPDATE CASCADE ON DELETE CASCADE , FOREIGN KEY(`review_id`) REFERENCES `review`(`id`) ON UPDATE CASCADE ON DELETE CASCADE , FOREIGN KEY(`media_id`) REFERENCES `media`(`id`) ON UPDATE CASCADE ON DELETE CASCADE )",
                "CREATE UNIQUE INDEX IF NOT EXISTS `index_user_profile_review_user_id_review_id_media_id` ON `user_profile_review` (`user_id`, `review_id`, `media_id`)",
                "CREATE INDEX IF NOT EXISTS `index_user_profile_review_user_id_sort_index` ON `user_profile_review` (`user_id`, `sort_index`)",
                "CREATE TABLE IF NOT EXISTS `list_status` (`user_id` INTEGER NOT NULL, `created_at` INTEGER NOT NULL, `sort_index` INTEGER NOT NULL, `activity_status` TEXT, `progress` TEXT, `site_url` TEXT, `activity_type` TEXT, `media_id` INTEGER, `id` INTEGER NOT NULL, PRIMARY KEY(`id`), FOREIGN KEY(`user_id`) REFERENCES `user`(`id`) ON UPDATE CASCADE ON DELETE CASCADE , FOREIGN KEY(`media_id`) REFERENCES `media`(`id`) ON UPDATE CASCADE ON DELETE CASCADE )",
                "CREATE INDEX IF NOT EXISTS `index_list_status_user_id_sort_index` ON `list_status` (`user_id`, `sort_index`)",
                "CREATE INDEX IF NOT EXISTS `index_list_status_user_id_media_id` ON `list_status` (`user_id`, `media_id`)",
                "CREATE INDEX IF NOT EXISTS `index_list_status_media_id` ON `list_status` (`media_id`)",
            )
        },
        migrationOfStatements(20, 21) {
            arrayOf(
                "CREATE TABLE IF NOT EXISTS `user_statistic_new` (`statistic_anime_count` INTEGER, `statistic_anime_mean_score` REAL, `statistic_anime_standard_deviation` REAL, `statistic_anime_minutes_watched` INTEGER, `statistic_anime_episodes_watched` INTEGER, `statistic_manga_count` INTEGER, `statistic_manga_mean_score` REAL, `statistic_manga_standard_deviation` REAL, `statistic_manga_chapters_read` INTEGER, `statistic_manga_volumes_read` INTEGER, `user_id` INTEGER NOT NULL, `id` INTEGER NOT NULL, PRIMARY KEY(`id`), FOREIGN KEY(`user_id`) REFERENCES `user`(`id`) ON UPDATE CASCADE ON DELETE CASCADE )",
                "CREATE INDEX IF NOT EXISTS `index_user_statistic_new_user_id` ON `user_statistic_new` (`user_id`)",
                "INSERT INTO `user_statistic_new` (`user_id`, `id`) SELECT `user_id`, `id` FROM `user_statistic`",
                "DROP TABLE IF EXISTS `user_statistic`",
                "ALTER TABLE `user_statistic_new` RENAME TO `user_statistic`",
                "CREATE INDEX IF NOT EXISTS `index_user_statistic_user_id` ON `user_statistic` (`user_id`)",
                "CREATE TABLE IF NOT EXISTS `user_statistic_country` (`country` TEXT NOT NULL, `user_id` INTEGER NOT NULL, `media_type` TEXT NOT NULL, `count` INTEGER NOT NULL, `mean_score` REAL NOT NULL, `media_ids` TEXT NOT NULL, `tracked_amount` INTEGER NOT NULL, `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, FOREIGN KEY(`user_id`) REFERENCES `user`(`id`) ON UPDATE CASCADE ON DELETE CASCADE )",
                "CREATE UNIQUE INDEX IF NOT EXISTS `index_user_statistic_country_user_id_media_type_country` ON `user_statistic_country` (`user_id`, `media_type`, `country`)",
                "CREATE TABLE IF NOT EXISTS `user_statistic_format` (`format` TEXT NOT NULL, `user_id` INTEGER NOT NULL, `media_type` TEXT NOT NULL, `count` INTEGER NOT NULL, `mean_score` REAL NOT NULL, `media_ids` TEXT NOT NULL, `tracked_amount` INTEGER NOT NULL, `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, FOREIGN KEY(`user_id`) REFERENCES `user`(`id`) ON UPDATE CASCADE ON DELETE CASCADE )",
                "CREATE UNIQUE INDEX IF NOT EXISTS `index_user_statistic_format_user_id_media_type_format` ON `user_statistic_format` (`user_id`, `media_type`, `format`)",
                "CREATE TABLE IF NOT EXISTS `user_statistic_genre` (`genre` TEXT NOT NULL, `user_id` INTEGER NOT NULL, `media_type` TEXT NOT NULL, `count` INTEGER NOT NULL, `mean_score` REAL NOT NULL, `media_ids` TEXT NOT NULL, `tracked_amount` INTEGER NOT NULL, `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, FOREIGN KEY(`user_id`) REFERENCES `user`(`id`) ON UPDATE CASCADE ON DELETE CASCADE )",
                "CREATE UNIQUE INDEX IF NOT EXISTS `index_user_statistic_genre_user_id_media_type_genre` ON `user_statistic_genre` (`user_id`, `media_type`, `genre`)",
                "CREATE TABLE IF NOT EXISTS `user_statistic_length` (`length` TEXT, `user_id` INTEGER NOT NULL, `media_type` TEXT NOT NULL, `count` INTEGER NOT NULL, `mean_score` REAL NOT NULL, `media_ids` TEXT NOT NULL, `tracked_amount` INTEGER NOT NULL, `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, FOREIGN KEY(`user_id`) REFERENCES `user`(`id`) ON UPDATE CASCADE ON DELETE CASCADE )",
                "CREATE UNIQUE INDEX IF NOT EXISTS `index_user_statistic_length_user_id_media_type_length` ON `user_statistic_length` (`user_id`, `media_type`, `length`)",
                "CREATE TABLE IF NOT EXISTS `user_statistic_release_year` (`release_year` INTEGER NOT NULL, `user_id` INTEGER NOT NULL, `media_type` TEXT NOT NULL, `count` INTEGER NOT NULL, `mean_score` REAL NOT NULL, `media_ids` TEXT NOT NULL, `tracked_amount` INTEGER NOT NULL, `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, FOREIGN KEY(`user_id`) REFERENCES `user`(`id`) ON UPDATE CASCADE ON DELETE CASCADE )",
                "CREATE UNIQUE INDEX IF NOT EXISTS `index_user_statistic_release_year_user_id_media_type_release_year` ON `user_statistic_release_year` (`user_id`, `media_type`, `release_year`)",
                "CREATE TABLE IF NOT EXISTS `user_statistic_score` (`score` INTEGER NOT NULL, `user_id` INTEGER NOT NULL, `media_type` TEXT NOT NULL, `count` INTEGER NOT NULL, `mean_score` REAL NOT NULL, `media_ids` TEXT NOT NULL, `tracked_amount` INTEGER NOT NULL, `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, FOREIGN KEY(`user_id`) REFERENCES `user`(`id`) ON UPDATE CASCADE ON DELETE CASCADE )",
                "CREATE UNIQUE INDEX IF NOT EXISTS `index_user_statistic_score_user_id_media_type_score` ON `user_statistic_score` (`user_id`, `media_type`, `score`)",
                "CREATE TABLE IF NOT EXISTS `user_statistic_staff` (`staff_id` INTEGER NOT NULL, `user_id` INTEGER NOT NULL, `media_type` TEXT NOT NULL, `count` INTEGER NOT NULL, `mean_score` REAL NOT NULL, `media_ids` TEXT NOT NULL, `tracked_amount` INTEGER NOT NULL, `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, FOREIGN KEY(`user_id`) REFERENCES `user`(`id`) ON UPDATE CASCADE ON DELETE CASCADE , FOREIGN KEY(`staff_id`) REFERENCES `staff`(`id`) ON UPDATE CASCADE ON DELETE CASCADE )",
                "CREATE UNIQUE INDEX IF NOT EXISTS `index_user_statistic_staff_user_id_media_type_staff_id` ON `user_statistic_staff` (`user_id`, `media_type`, `staff_id`)",
                "CREATE TABLE IF NOT EXISTS `user_statistic_start_year` (`start_year` INTEGER NOT NULL, `user_id` INTEGER NOT NULL, `media_type` TEXT NOT NULL, `count` INTEGER NOT NULL, `mean_score` REAL NOT NULL, `media_ids` TEXT NOT NULL, `tracked_amount` INTEGER NOT NULL, `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, FOREIGN KEY(`user_id`) REFERENCES `user`(`id`) ON UPDATE CASCADE ON DELETE CASCADE )",
                "CREATE UNIQUE INDEX IF NOT EXISTS `index_user_statistic_start_year_user_id_media_type_start_year` ON `user_statistic_start_year` (`user_id`, `media_type`, `start_year`)",
                "CREATE TABLE IF NOT EXISTS `user_statistic_status` (`status` TEXT NOT NULL, `user_id` INTEGER NOT NULL, `media_type` TEXT NOT NULL, `count` INTEGER NOT NULL, `mean_score` REAL NOT NULL, `media_ids` TEXT NOT NULL, `tracked_amount` INTEGER NOT NULL, `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, FOREIGN KEY(`user_id`) REFERENCES `user`(`id`) ON UPDATE CASCADE ON DELETE CASCADE )",
                "CREATE UNIQUE INDEX IF NOT EXISTS `index_user_statistic_status_user_id_media_type_status` ON `user_statistic_status` (`user_id`, `media_type`, `status`)",
                "CREATE TABLE IF NOT EXISTS `user_statistic_studio` (`studio_id` INTEGER NOT NULL, `user_id` INTEGER NOT NULL, `media_type` TEXT NOT NULL, `count` INTEGER NOT NULL, `mean_score` REAL NOT NULL, `media_ids` TEXT NOT NULL, `tracked_amount` INTEGER NOT NULL, `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, FOREIGN KEY(`user_id`) REFERENCES `user`(`id`) ON UPDATE CASCADE ON DELETE CASCADE , FOREIGN KEY(`studio_id`) REFERENCES `studio`(`id`) ON UPDATE CASCADE ON DELETE CASCADE )",
                "CREATE UNIQUE INDEX IF NOT EXISTS `index_user_statistic_studio_user_id_media_type_studio_id` ON `user_statistic_studio` (`user_id`, `media_type`, `studio_id`)",
                "CREATE TABLE IF NOT EXISTS `user_statistic_tag` (`tag_id` INTEGER NOT NULL, `user_id` INTEGER NOT NULL, `media_type` TEXT NOT NULL, `count` INTEGER NOT NULL, `mean_score` REAL NOT NULL, `media_ids` TEXT NOT NULL, `tracked_amount` INTEGER NOT NULL, `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, FOREIGN KEY(`user_id`) REFERENCES `user`(`id`) ON UPDATE CASCADE ON DELETE CASCADE , FOREIGN KEY(`tag_id`) REFERENCES `tag`(`id`) ON UPDATE CASCADE ON DELETE CASCADE )",
                "CREATE UNIQUE INDEX IF NOT EXISTS `index_user_statistic_tag_user_id_media_type_tag_id` ON `user_statistic_tag` (`user_id`, `media_type`, `tag_id`)",
                "CREATE TABLE IF NOT EXISTS `user_statistic_voice_actor` (`staff_id` INTEGER NOT NULL, `user_id` INTEGER NOT NULL, `media_type` TEXT NOT NULL, `count` INTEGER NOT NULL, `mean_score` REAL NOT NULL, `media_ids` TEXT NOT NULL, `tracked_amount` INTEGER NOT NULL, `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, FOREIGN KEY(`user_id`) REFERENCES `user`(`id`) ON UPDATE CASCADE ON DELETE CASCADE , FOREIGN KEY(`staff_id`) REFERENCES `staff`(`id`) ON UPDATE CASCADE ON DELETE CASCADE )",
                "CREATE UNIQUE INDEX IF NOT EXISTS `index_user_statistic_voice_actor_user_id_media_type_staff_id` ON `user_statistic_voice_actor` (`user_id`, `media_type`, `staff_id`)",
            )
        },
        migrationOfStatements(21, 22) {
            arrayOf(
                "DROP TABLE IF EXISTS `edge_media_theme_song`",
                "CREATE TABLE IF NOT EXISTS `edge_anime_theme` (`media_id` TEXT NOT NULL, `theme_id` TEXT NOT NULL, `slug` TEXT, `type` TEXT NOT NULL, `sequence` INTEGER NOT NULL, `song_id` INTEGER, `song_title` TEXT NOT NULL, `id` TEXT NOT NULL, PRIMARY KEY(`id`), FOREIGN KEY(`media_id`) REFERENCES `edge_media`(`id`) ON UPDATE CASCADE ON DELETE CASCADE )",
                "CREATE UNIQUE INDEX IF NOT EXISTS `index_edge_anime_theme_media_id_theme_id` ON `edge_anime_theme` (`media_id`, `theme_id`)",
                "CREATE UNIQUE INDEX IF NOT EXISTS `index_edge_anime_theme_theme_id` ON `edge_anime_theme` (`theme_id`)",
                "CREATE INDEX IF NOT EXISTS `index_edge_anime_theme_media_id` ON `edge_anime_theme` (`media_id`)",
                "CREATE TABLE IF NOT EXISTS `edge_anime_theme_entry` (`theme_id` TEXT NOT NULL, `entry_id` TEXT NOT NULL, `episodes` TEXT, `notes` TEXT, `nsfw` INTEGER NOT NULL, `spoiler` INTEGER NOT NULL, `version` INTEGER NOT NULL, `id` TEXT NOT NULL, PRIMARY KEY(`id`), FOREIGN KEY(`theme_id`) REFERENCES `edge_anime_theme`(`theme_id`) ON UPDATE CASCADE ON DELETE CASCADE )",
                "CREATE UNIQUE INDEX IF NOT EXISTS `index_edge_anime_theme_entry_theme_id_entry_id` ON `edge_anime_theme_entry` (`theme_id`, `entry_id`)",
                "CREATE UNIQUE INDEX IF NOT EXISTS `index_edge_anime_theme_entry_entry_id` ON `edge_anime_theme_entry` (`entry_id`)",
                "CREATE INDEX IF NOT EXISTS `index_edge_anime_theme_entry_theme_id` ON `edge_anime_theme_entry` (`theme_id`)",
                "CREATE TABLE IF NOT EXISTS `edge_anime_theme_video` (`entry_id` TEXT NOT NULL, `video_id` TEXT NOT NULL, `link` TEXT NOT NULL, `resolution` INTEGER, `source` TEXT, `subbed` INTEGER NOT NULL, `lyrics` INTEGER NOT NULL, `nc` INTEGER NOT NULL, `uncen` INTEGER NOT NULL, `tags` TEXT, `overlap` TEXT, `audio_id` INTEGER, `audio_link` TEXT, `id` TEXT NOT NULL, PRIMARY KEY(`id`), FOREIGN KEY(`entry_id`) REFERENCES `edge_anime_theme_entry`(`entry_id`) ON UPDATE CASCADE ON DELETE CASCADE )",
                "CREATE UNIQUE INDEX IF NOT EXISTS `index_edge_anime_theme_video_entry_id_video_id` ON `edge_anime_theme_video` (`entry_id`, `video_id`)",
                "CREATE INDEX IF NOT EXISTS `index_edge_anime_theme_video_entry_id` ON `edge_anime_theme_video` (`entry_id`)",
            )
        },
        migrationOfStatements(22, 23) {
            arrayOf(
                "ALTER TABLE `media_studio_connection` ADD COLUMN `media_title` TEXT",
                "ALTER TABLE `media_studio_connection` ADD COLUMN `media_cover_image_large` TEXT",
                "ALTER TABLE `media_studio_connection` ADD COLUMN `media_cover_image_medium` TEXT",
                "ALTER TABLE `media_studio_connection` ADD COLUMN `media_format` TEXT",
                "ALTER TABLE `media_studio_connection` ADD COLUMN `media_start_year` INTEGER",
                "ALTER TABLE `media_studio_connection` ADD COLUMN `media_average_score` INTEGER",
            )
        },
    )
