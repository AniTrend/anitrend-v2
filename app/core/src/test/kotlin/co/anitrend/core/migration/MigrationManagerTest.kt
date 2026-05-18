/*
 * Copyright (C) 2021 AniTrend
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
import android.content.SharedPreferences
import androidx.core.content.edit
import co.anitrend.android.core.R
import co.anitrend.android.core.settings.Settings
import co.anitrend.arch.extension.settings.SetSetting
import co.anitrend.core.migration.model.Migration
import co.anitrend.core.migration.model.Migrations
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.test.Test
import org.junit.jupiter.api.BeforeEach

class MigrationManagerTest {
    private val manager = MigrationManager(mockk())
    private val migrations = mutableListOf<Migration>()

    @BeforeEach
    fun setUp() {
        migrations +=
            listOf(
                object : Migration(20290, 20300) {
                    override fun invoke(
                        context: Context,
                        settings: Settings,
                    ) {
                    }
                },
                object : Migration(20310, 20320) {
                    override fun invoke(
                        context: Context,
                        settings: Settings,
                    ) {
                    }
                },
                object : Migration(20320, 20330) {
                    override fun invoke(
                        context: Context,
                        settings: Settings,
                    ) {
                    }
                },
            )
    }

    @Test
    fun `check possible migrations`() {
        val expected =
            listOf(
                object : Migration(20290, 20300) {
                    override fun invoke(
                        context: Context,
                        settings: Settings,
                    ) {
                    }
                },
                object : Migration(20310, 20320) {
                    override fun invoke(
                        context: Context,
                        settings: Settings,
                    ) {
                    }
                },
            )

        val actual =
            manager.possibleMigrations(
                20300,
                20310,
                migrations,
            )

        assertEquals(expected, actual)
    }

    @Test
    fun `should not migrate when downgraded`() {
        val expected = emptyList<Migration>()

        val actual =
            manager.possibleMigrations(
                2000000041,
                1000000,
                migrations,
            )

        assertEquals(expected, actual)
    }

    @Test
    fun `given legacy compose flag when migration runs then feature flag is copied and legacy preference removed`() {
        val context = mockk<Context>()
        val settings = mockk<Settings>(relaxed = true)
        val featureFlags = setSetting(initial = setOf("future_flag"))
        val editor = mockk<SharedPreferences.Editor>(relaxed = true)

        every { context.getString(R.string.settings_experimental_compose_ui) } returns "_experimentalComposeUi"
        every { settings.featureFlags } returns featureFlags
        every { settings.contains("_experimentalComposeUi") } returns true
        every { settings.getBoolean("_experimentalComposeUi", false) } returns true
        every { settings.edit() } returns editor
        every { editor.remove(any()) } returns editor
        every { editor.commit() } returns true

        Migrations.ALL.last().invoke(context, settings)

        assertEquals(setOf("experimental_compose_ui", "future_flag"), featureFlags.value)

        verify { editor.remove("_experimentalComposeUi") }
    }

    private fun setSetting(initial: Set<String>): SetSetting {
        var value = initial
        return mockk {
            every { this@mockk.value } answers { value }
            every { this@mockk.value = any() } answers {
                value = firstArg()
                Unit
            }
        }
    }
}
