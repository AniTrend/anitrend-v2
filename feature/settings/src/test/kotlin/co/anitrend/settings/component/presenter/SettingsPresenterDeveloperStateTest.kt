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
package co.anitrend.settings.component.presenter

import android.content.Context
import co.anitrend.android.core.settings.Settings
import co.anitrend.arch.extension.settings.BooleanSetting
import co.anitrend.arch.extension.settings.StringSetting
import co.anitrend.data.settings.feature.FeatureFlag
import co.anitrend.data.settings.feature.FeatureFlags
import co.anitrend.navigation.SettingsRouter
import co.anitrend.settings.component.builder.contract.IPreferenceBuilder
import co.anitrend.settings.model.SettingItem
import io.mockk.every
import io.mockk.mockk
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SettingsPresenterDeveloperStateTest {
    @Test
    fun `given reactive developer state when creating items then switch checked values reflect provided state`() {
        val automaticHeapDump = booleanSetting(initial = false)
        val showLeakLauncher = booleanSetting(initial = false)
        val clearDataOnSwipeRefresh = booleanSetting(initial = false)

        val presenter =
            settingsPresenter(
                automaticHeapDump = automaticHeapDump,
                showLeakLauncher = showLeakLauncher,
                clearDataOnSwipeRefresh = clearDataOnSwipeRefresh,
            )

        val items =
            presenter.getDeveloperSettingsItems(
                state =
                    SettingsPresenter.DeveloperSettingsState(
                        automaticHeapDump = true,
                        showLeakLauncher = true,
                        clearDataOnSwipeRefresh = true,
                    ),
                navigateTo = {},
            )

        val heapDumpToggle = items.switchSetting(id = "heap_dump")
        val leakLauncherToggle = items.switchSetting(id = "show_leak_launcher")
        val refreshToggle = items.switchSetting(id = "clear_db_on_refresh")

        assertTrue(heapDumpToggle.onClick())
        assertTrue(leakLauncherToggle.onClick())
        assertTrue(refreshToggle.onClick())
    }

    @Test
    fun `given developer switch when toggled then underlying settings value updates`() {
        val automaticHeapDump = booleanSetting(initial = false)
        val showLeakLauncher = booleanSetting(initial = false)
        val clearDataOnSwipeRefresh = booleanSetting(initial = false)

        val presenter =
            settingsPresenter(
                automaticHeapDump = automaticHeapDump,
                showLeakLauncher = showLeakLauncher,
                clearDataOnSwipeRefresh = clearDataOnSwipeRefresh,
            )

        val items =
            presenter.getDeveloperSettingsItems(
                state =
                    SettingsPresenter.DeveloperSettingsState(
                        automaticHeapDump = false,
                        showLeakLauncher = false,
                        clearDataOnSwipeRefresh = false,
                    ),
                navigateTo = {},
            )

        val heapDumpToggle = items.switchSetting(id = "heap_dump")
        val leakLauncherToggle = items.switchSetting(id = "show_leak_launcher")
        val refreshToggle = items.switchSetting(id = "clear_db_on_refresh")

        heapDumpToggle.onValueChange(true)
        leakLauncherToggle.onValueChange(true)
        refreshToggle.onValueChange(true)

        assertEquals(true, automaticHeapDump.value)
        assertEquals(true, showLeakLauncher.value)
        assertEquals(true, clearDataOnSwipeRefresh.value)
    }

    @Test
    fun `given developer feature flags row when clicked then navigates to feature flags destination`() {
        val presenter = settingsPresenter()
        var destination: SettingsRouter.Destination? = null

        val items =
            presenter.getDeveloperSettingsItems(
                state =
                    SettingsPresenter.DeveloperSettingsState(
                        automaticHeapDump = false,
                        showLeakLauncher = false,
                        clearDataOnSwipeRefresh = false,
                    ),
                navigateTo = { destination = it },
            )

        items.clickableSetting(id = "feature_flags").onClick()

        assertEquals(SettingsRouter.Destination.FEATURE_FLAGS, destination)
    }

    @Test
    fun `given feature flag setting when toggled then csv preference updates without dropping unknown tokens`() {
        val featureFlags = stringSetting(initial = "future_flag")
        val presenter = settingsPresenter(featureFlags = featureFlags)

        val items =
            presenter.getFeatureFlagSettingsItems(
                state =
                    SettingsPresenter.FeatureFlagSettingsState(
                        featureFlags = featureFlags.value,
                    ),
            )

        val composeToggle = items.switchSetting(id = FeatureFlag.EXPERIMENTAL_COMPOSE_UI.key)
        composeToggle.onValueChange(true)

        assertEquals("experimental_compose_ui,future_flag", featureFlags.value)
    }

    private fun settingsPresenter(
        automaticHeapDump: BooleanSetting = booleanSetting(initial = false),
        showLeakLauncher: BooleanSetting = booleanSetting(initial = false),
        clearDataOnSwipeRefresh: BooleanSetting = booleanSetting(initial = false),
        featureFlags: StringSetting = stringSetting(initial = FeatureFlags.EMPTY),
    ): SettingsPresenter {
        val context = mockk<Context>()
        every { context.getString(any<Int>()) } answers { firstArg<Int>().toString() }
        every { context.getString(any<Int>(), *anyVararg()) } answers { firstArg<Int>().toString() }

        val settings = mockk<Settings>()
        every { settings.automaticHeapDump } returns automaticHeapDump
        every { settings.showLeakLauncher } returns showLeakLauncher
        every { settings.clearDataOnSwipeRefresh } returns clearDataOnSwipeRefresh
        every { settings.featureFlags } returns featureFlags

        return SettingsPresenter(
            context = context,
            settings = settings,
            preferenceBuilder = mockk<IPreferenceBuilder>(relaxed = true),
        )
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

    private fun List<SettingItem>.switchSetting(id: String): SettingItem.SwitchSetting =
        filterIsInstance<SettingItem.SwitchSetting>().first { it.id == id }

    private fun List<SettingItem>.clickableSetting(id: String): SettingItem.ClickableSetting =
        filterIsInstance<SettingItem.ClickableSetting>().first { it.id == id }
}
