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
package co.anitrend.settings.component.content.developer

import co.anitrend.arch.extension.settings.BooleanSetting
import co.anitrend.data.settings.developer.IDeveloperSettings
import co.anitrend.data.settings.refresh.IRefreshBehaviourSettings
import io.mockk.every
import io.mockk.mockk
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DeveloperScreenStateTest {
    @Test
    fun `given developer toggle changes when updating screen state then checked state mirrors immediately`() {
        val automaticHeapDump = booleanSetting(initial = false)
        val showLeakLauncher = booleanSetting(initial = false)
        val clearDataOnSwipeRefresh = booleanSetting(initial = false)
        val developerSettings =
            mockk<IDeveloperSettings> {
                every { this@mockk.automaticHeapDump } returns automaticHeapDump
                every { this@mockk.showLeakLauncher } returns showLeakLauncher
            }
        val refreshBehaviourSettings =
            mockk<IRefreshBehaviourSettings> {
                every { this@mockk.clearDataOnSwipeRefresh } returns clearDataOnSwipeRefresh
            }

        val updatedState =
            DeveloperScreenState(
                automaticHeapDump = false,
                showLeakLauncher = false,
                clearDataOnSwipeRefresh = false,
            ).updateAutomaticHeapDump(true, developerSettings)
                .updateShowLeakLauncher(true, developerSettings)
                .updateClearDataOnSwipeRefresh(true, refreshBehaviourSettings)

        assertTrue(updatedState.automaticHeapDump)
        assertTrue(updatedState.showLeakLauncher)
        assertTrue(updatedState.clearDataOnSwipeRefresh)
        assertEquals(true, automaticHeapDump.value)
        assertEquals(true, showLeakLauncher.value)
        assertEquals(true, clearDataOnSwipeRefresh.value)
    }

    private fun booleanSetting(initial: Boolean): BooleanSetting {
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
