/*
 * Copyright (C) 2021  AniTrend
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

package co.anitrend.core.migration

import android.content.Context
import co.anitrend.core.android.settings.Settings
import co.anitrend.core.migration.model.Migration
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test

class MigrationManagerTest {

    private val manager = MigrationManager(mockk())
    private val migrations = mutableListOf<Migration>()

    @BeforeEach
    fun setUp() {
        migrations += listOf(
            object : Migration(20290, 20300){
                override fun invoke(context: Context, settings: Settings) {

                }
            },
            object : Migration(20310, 20320){
                override fun invoke(context: Context, settings: Settings) {

                }
            },
            object : Migration(20320, 20330){
                override fun invoke(context: Context, settings: Settings) {

                }
            }
        )
    }

    @Test
    fun `check possible migrations`() {
        val expected = listOf(
            object : Migration(20290, 20300){
                override fun invoke(context: Context, settings: Settings) {

                }
            },
            object : Migration(20310, 20320){
                override fun invoke(context: Context, settings: Settings) {

                }
            }
        )

        val actual = manager.possibleMigrations(
            20300,
            20310,
            migrations
        )

        assertEquals(expected, actual)
    }
}
