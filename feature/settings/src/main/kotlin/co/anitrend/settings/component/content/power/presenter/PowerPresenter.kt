package co.anitrend.settings.component.content.power.presenter

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BatterySaver
import co.anitrend.android.core.settings.Settings
import co.anitrend.core.presenter.CorePresenter
import co.anitrend.data.settings.power.IPowerSettings
import co.anitrend.settings.R
import co.anitrend.settings.component.builder.contract.IPreferenceBuilder
import co.anitrend.settings.model.SettingItem

class PowerPresenter(
    context: Context,
    private val preferenceBuilder: IPreferenceBuilder,
    settings: Settings,
): CorePresenter(context, settings) {

    fun getItems(): List<SettingItem> {
        preferenceBuilder.clear()

        val power = settings as IPowerSettings

        // Intro hint
        preferenceBuilder.add(
            entries = listOf(
                SettingItem.HintCard(
                    id = "power_hint",
                    title = context.getString(R.string.preference_title_power_management),
                    description = context.getString(R.string.preference_summary_power_management),
                    icon = Icons.Outlined.BatterySaver,
                    onClick = {},
                )
            )
        )

        // Section header and toggles
        preferenceBuilder.add(
            entries = listOf(
                SettingItem.SwitchSetting(
                    id = "power_saver",
                    title = context.getString(R.string.preference_title_power_saver),
                    summary = context.getString(R.string.preference_summary_power_saver),
                    icon = Icons.Outlined.BatterySaver,
                    onValueChange = { power.isPowerSaverOn.value = it },
                    onClick = { power.isPowerSaverOn.value },
                ),
                SettingItem.SwitchSetting(
                    id = "battery_optimization",
                    title = context.getString(R.string.preference_title_battery_optimization),
                    summary = context.getString(R.string.preference_summary_battery_optimization),
                    icon = Icons.Outlined.BatterySaver,
                    onValueChange = { power.isBatteryOptimizationOn.value = it },
                    onClick = { power.isBatteryOptimizationOn.value },
                ),
            ),
        )

        return preferenceBuilder.build()
    }
}
