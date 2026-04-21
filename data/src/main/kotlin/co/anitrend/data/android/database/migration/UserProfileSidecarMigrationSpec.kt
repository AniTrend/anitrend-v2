/*
 * Copyright (C) 2026 AniTrend
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

import androidx.room.DeleteTable
import androidx.room.migration.AutoMigrationSpec

/**
 * Instructs Room that the `user_profile_overview` and `user_profile_feed` sidecar tables
 * (which stored JSON blobs) are intentionally dropped during the v18 → v19 auto-migration.
 * Their replacement tables are `user_profile_favourite_media`, `user_profile_activity`, and
 * `user_profile_review`.
 */
@DeleteTable.Entries(
    DeleteTable(tableName = "user_profile_overview"),
    DeleteTable(tableName = "user_profile_feed"),
)
internal class UserProfileSidecarMigrationSpec : AutoMigrationSpec
