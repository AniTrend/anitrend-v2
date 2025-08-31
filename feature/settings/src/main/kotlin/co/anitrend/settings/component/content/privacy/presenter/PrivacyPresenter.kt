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
package co.anitrend.settings.component.content.privacy.presenter

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PrivacyTip
import co.anitrend.android.core.settings.Settings
import co.anitrend.core.presenter.CorePresenter
import co.anitrend.data.settings.privacy.IPrivacySettings
import co.anitrend.settings.R
import co.anitrend.settings.component.builder.contract.IPreferenceBuilder
import co.anitrend.settings.model.SettingItem

class PrivacyPresenter(
    context: Context,
    private val preferenceBuilder: IPreferenceBuilder,
    settings: Settings,
) : CorePresenter(context, settings) {
    fun getItems(): List<SettingItem> {
        preferenceBuilder.clear()

        val privacy = settings as IPrivacySettings

        // Intro hint
        preferenceBuilder.add(
            entries =
                listOf(
                    SettingItem.HintCard(
                        id = "privacy_hint",
                        title = context.getString(R.string.preference_title_privacy),
                        description = context.getString(R.string.preference_summary_privacy),
                        icon = Icons.Outlined.PrivacyTip,
                        onClick = {},
                    ),
                ),
        )

        // Section header and toggles
        preferenceBuilder.add(
            entries =
                listOf(
                    SettingItem.SwitchSetting(
                        id = "privacy_analytics",
                        title = context.getString(R.string.preference_title_privacy_analytics_config),
                        summary = context.getString(R.string.preference_summary_privacy_analytics_config),
                        icon = Icons.Outlined.PrivacyTip,
                        onValueChange = { privacy.isAnalyticsEnabled.value = it },
                        onClick = { privacy.isAnalyticsEnabled.value },
                    ),
                    SettingItem.SwitchSetting(
                        id = "privacy_crashlytics",
                        title = context.getString(R.string.preference_title_privacy_crash_analytics_config),
                        summary = context.getString(R.string.preference_summary_privacy_crash_analytics_config),
                        icon = Icons.Outlined.PrivacyTip,
                        onValueChange = { privacy.isCrashlyticsEnabled.value = it },
                        onClick = { privacy.isCrashlyticsEnabled.value },
                    ),
                ),
        )

        return preferenceBuilder.build()
    }
}
