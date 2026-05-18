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
    fun `given flags with duplicates when parsed then known flags are normalized`() {
        val flags = setOf(" experimental_compose_ui ", "EXPERIMENTAL_COMPOSE_UI", "unknown")

        val enabledFlags = FeatureFlags.enabledFlags(flags)

        assertEquals(setOf(FeatureFlag.EXPERIMENTAL_COMPOSE_UI), enabledFlags)
        assertTrue(FeatureFlags.isEnabled(flags, FeatureFlag.EXPERIMENTAL_COMPOSE_UI))
    }

    @Test
    fun `given unknown future flag when enabling known flag then unknown token is preserved`() {
        val flags = setOf("future_flag")

        val updated = FeatureFlags.setEnabled(flags, FeatureFlag.EXPERIMENTAL_COMPOSE_UI, true)

        assertEquals(setOf("experimental_compose_ui", "future_flag"), updated)
    }

    @Test
    fun `given mixed flags when disabling known flag then unknown token remains`() {
        val flags = setOf("experimental_compose_ui", "future_flag")

        val updated = FeatureFlags.setEnabled(flags, FeatureFlag.EXPERIMENTAL_COMPOSE_UI, false)

        assertEquals(setOf("future_flag"), updated)
        assertFalse(FeatureFlags.isEnabled(updated, FeatureFlag.EXPERIMENTAL_COMPOSE_UI))
    }
}
