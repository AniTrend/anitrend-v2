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

import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Rule
import org.junit.Test

class AniTrendStoreMigrationTest {
    @get:Rule
    val helper =
        MigrationTestHelper(
            InstrumentationRegistry.getInstrumentation(),
            "co.anitrend.data.android.database.AniTrendStore",
            FrameworkSQLiteOpenHelperFactory(),
        )

    @Test
    fun migrate20To21() {
        helper.createDatabase(TEST_DB, 20).apply {
            execSQL(
                """
                INSERT INTO user (id, user_name, user_bio, user_site_url, user_donator_tier, user_donator_badge, user_is_following, user_is_follower, user_is_blocked, cover_large, cover_medium, cover_banner, created_at, updated_at)
                VALUES (1, 'viewer', NULL, 'https://anilist.co/user/viewer', NULL, NULL, 0, 0, 0, NULL, NULL, NULL, NULL, NULL)
            """,
            )
            execSQL("INSERT INTO user_statistic (user_id, id) VALUES (1, 1)")
            close()
        }

        helper.runMigrationsAndValidate(TEST_DB, 21, true, *MIGRATIONS)
    }

    private companion object {
        const val TEST_DB = "migration-test"
    }
}
