/*
 * Copyright (C) 2025 AniTrend
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
package co.anitrend.settings.component.content.power

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BatterySaver
import androidx.compose.material.icons.outlined.Power
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import co.anitrend.android.core.compose.design.cards.AniTrendHintCard
import co.anitrend.android.core.ui.AniTrendPreview
import co.anitrend.android.core.ui.theme.preview.DarkThemeProvider
import co.anitrend.android.core.ui.theme.preview.PreviewTheme
import co.anitrend.data.settings.power.IPowerSettings
import co.anitrend.settings.R
import co.anitrend.settings.component.compose.SettingsSectionCard
import co.anitrend.settings.component.compose.SettingsToggleRow
import org.koin.compose.koinInject

@Composable
fun PowerScreen(
    modifier: Modifier = Modifier,
    settings: IPowerSettings = koinInject(),
) {
    var powerSaverEnabled by remember { mutableStateOf(settings.isPowerSaverOn.value) }
    var batteryOptimizationEnabled by remember { mutableStateOf(settings.isBatteryOptimizationOn.value) }

    PowerContent(
        modifier = modifier,
        powerSaverEnabled = powerSaverEnabled,
        batteryOptimizationEnabled = batteryOptimizationEnabled,
        onPowerSaverChange = {
            settings.isPowerSaverOn.value = it
            powerSaverEnabled = it
        },
        onBatteryOptimizationChange = {
            settings.isBatteryOptimizationOn.value = it
            batteryOptimizationEnabled = it
        },
    )
}

@Composable
private fun PowerContent(
    modifier: Modifier = Modifier,
    powerSaverEnabled: Boolean,
    batteryOptimizationEnabled: Boolean,
    onPowerSaverChange: (Boolean) -> Unit = {},
    onBatteryOptimizationChange: (Boolean) -> Unit = {},
) {
    val profileLabel =
        if (powerSaverEnabled) {
            stringResource(R.string.label_settings_state_power_saver_on)
        } else {
            stringResource(R.string.label_settings_state_default)
        }

    LazyColumn(modifier = modifier) {
        items(listOf(Unit)) {
            AniTrendHintCard(
                title = stringResource(R.string.preference_title_power_management),
                description = stringResource(R.string.preference_summary_power_management),
                icon = Icons.Outlined.BatterySaver,
                currentValue = profileLabel,
            )
        }
        items(listOf(Unit)) {
            SettingsSectionCard(
                title = stringResource(R.string.title_settings_power_battery_behavior),
                description = stringResource(R.string.preference_summary_power_management),
            ) {
                SettingsToggleRow(
                    title = stringResource(R.string.preference_title_power_saver),
                    summary = stringResource(R.string.preference_summary_power_saver),
                    icon = Icons.Outlined.BatterySaver,
                    checked = powerSaverEnabled,
                    onCheckedChange = onPowerSaverChange,
                )
                SettingsToggleRow(
                    title = stringResource(R.string.preference_title_battery_optimization),
                    summary = stringResource(R.string.preference_summary_battery_optimization),
                    icon = Icons.Outlined.Power,
                    checked = batteryOptimizationEnabled,
                    onCheckedChange = onBatteryOptimizationChange,
                )
            }
        }
    }
}

@AniTrendPreview.Default
@Composable
private fun PowerScreenPreview(
    @PreviewParameter(DarkThemeProvider::class) darkTheme: Boolean,
) {
    PreviewTheme(wrapInSurface = true, darkTheme = darkTheme) {
        PowerContent(
            powerSaverEnabled = true,
            batteryOptimizationEnabled = false,
        )
    }
}
