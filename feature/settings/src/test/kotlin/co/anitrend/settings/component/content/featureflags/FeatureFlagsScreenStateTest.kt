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
package co.anitrend.settings.component.content.featureflags

import co.anitrend.arch.extension.settings.StringSetting
import co.anitrend.data.settings.feature.FeatureFlag
import co.anitrend.data.settings.feature.FeatureFlags
import co.anitrend.data.settings.feature.IFeatureFlagSetting
import io.mockk.every
import io.mockk.mockk
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FeatureFlagsScreenStateTest {
    @Test
    fun `given feature flag toggle change when updating screen state then checked state mirrors immediately`() {
        val featureFlagsSetting = stringSetting(initial = "future_flag")
        val featureFlagSetting =
            mockk<IFeatureFlagSetting> {
                every { this@mockk.featureFlags } returns featureFlagsSetting
            }

        val updatedState =
            FeatureFlagsScreenState(featureFlags = featureFlagsSetting.value)
                .updateExperimentalComposeUi(true, featureFlagSetting)

        assertTrue(
            FeatureFlags.isEnabled(
                csv = updatedState.featureFlags,
                flag = FeatureFlag.EXPERIMENTAL_COMPOSE_UI,
            ),
        )
        assertEquals(updatedState.featureFlags, featureFlagsSetting.value)
    }

    private fun stringSetting(initial: String): StringSetting {
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
