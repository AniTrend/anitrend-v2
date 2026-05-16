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
package co.anitrend.data.settings.feature

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class FeatureFlagsTest {
    @Test
    fun `given csv with whitespace blanks and duplicates when parsed then known flags are normalized`() {
        val csv = " experimental_compose_ui, ,EXPERIMENTAL_COMPOSE_UI,unknown "

        val flags = FeatureFlags.enabledFlags(csv)

        assertEquals(setOf(FeatureFlag.EXPERIMENTAL_COMPOSE_UI), flags)
        assertTrue(FeatureFlags.isEnabled(csv, FeatureFlag.EXPERIMENTAL_COMPOSE_UI))
    }

    @Test
    fun `given unknown future flag when enabling known flag then unknown token is preserved`() {
        val csv = "future_flag"

        val updated = FeatureFlags.setEnabled(csv, FeatureFlag.EXPERIMENTAL_COMPOSE_UI, true)

        assertEquals("experimental_compose_ui,future_flag", updated)
    }

    @Test
    fun `given mixed csv when disabling known flag then unknown token remains`() {
        val csv = "experimental_compose_ui,future_flag"

        val updated = FeatureFlags.setEnabled(csv, FeatureFlag.EXPERIMENTAL_COMPOSE_UI, false)

        assertEquals("future_flag", updated)
        assertFalse(FeatureFlags.isEnabled(updated, FeatureFlag.EXPERIMENTAL_COMPOSE_UI))
    }

    @Test
    fun `given incomplete legacy migration when old compose flag is enabled then csv is enabled without dropping unknown tokens`() {
        val updated = FeatureFlags.migrateLegacyComposeUi(
            csv = "future_flag",
            legacyEnabled = true,
            migrationComplete = false,
        )

        assertEquals("experimental_compose_ui,future_flag", updated)
    }

    @Test
    fun `given completed legacy migration when old compose flag remains enabled then csv is not re-enabled`() {
        val updated = FeatureFlags.migrateLegacyComposeUi(
            csv = FeatureFlags.EMPTY,
            legacyEnabled = true,
            migrationComplete = true,
        )

        assertEquals(FeatureFlags.EMPTY, updated)
    }
}
