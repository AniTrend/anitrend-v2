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
package co.anitrend.settings.component.content.sync.presenter

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AppsOutage
import androidx.compose.material.icons.outlined.Checklist
import androidx.compose.material.icons.outlined.SupervisedUserCircle
import androidx.compose.material.icons.outlined.Sync
import co.anitrend.android.core.settings.Settings
import co.anitrend.core.presenter.CorePresenter
import co.anitrend.data.settings.sync.ISyncSettings
import co.anitrend.settings.R
import co.anitrend.settings.component.builder.contract.IPreferenceBuilder
import co.anitrend.settings.model.SettingItem
import kotlin.math.max

class SynchronizationPresenter(
    context: Context,
    private val preferenceBuilder: IPreferenceBuilder,
    settings: Settings,
) : CorePresenter(context, settings) {

    private fun labelForSeconds(seconds: Int): String {
        val minutes = seconds / 60
        return when {
            minutes < 60 -> context.getString(R.string.label_settings_sync_every_minutes, minutes)
            minutes % 60 == 0 -> {
                val hours = minutes / 60
                if (hours == 1) context.getString(R.string.label_settings_sync_every_hour, hours)
                else context.getString(R.string.label_settings_sync_every_hours, hours)
            }
            else -> {
                val hours = minutes / 60
                val rem = minutes % 60
                context.getString(R.string.label_settings_sync_every_hr_min, hours, rem)
            }
        }
    }

    private fun <T> coerceToOptions(value: T, options: List<T>): T =
        options.firstOrNull { it == value } ?: options.first()

    fun getItems(): List<SettingItem> {
        preferenceBuilder.clear()

        val syncSettings = settings as ISyncSettings

        // Options in seconds
        val metaOptions = listOf(900, 1800, 3600, 7200, 14400, 43200) // 15m -> 12h
        val listOptions = listOf(900, 1800, 3600, 7200, 14400, 43200) // 15m -> 12h
        val userOptions = listOf(300, 900, 1800, 3600, 7200)          // 5m -> 2h

        // Current selections (respect minimums)
        val selectedMeta = coerceToOptions(max(syncSettings.metaSyncInterval.value, ISyncSettings.MINIMUM_INTERVAL), metaOptions)
        val selectedList = coerceToOptions(max(syncSettings.listSyncInterval.value, ISyncSettings.MINIMUM_INTERVAL), listOptions)
        val selectedUser = coerceToOptions(syncSettings.userSyncInterval.value, userOptions)

        // Hint
        preferenceBuilder.add(
            entries = listOf(
                SettingItem.HintCard(
                    id = "sync_hint",
                    title = context.getString(R.string.preference_title_sync),
                    description = context.getString(R.string.preference_summary_sync),
                    icon = Icons.Outlined.Sync,
                    onClick = { /* TODO: deeplink to docs/help if available */ },
                ),
            ),
        )

        // Dialog settings
        preferenceBuilder.add(
            entries = listOf(
                SettingItem.DialogSetting(
                    id = "sync_meta_interval",
                    title = context.getString(R.string.title_settings_sync_metadata_interval),
                    summary = context.getString(R.string.summary_settings_sync_metadata_interval),
                    icon = Icons.Outlined.AppsOutage,
                    options = metaOptions,
                    selectedOption = { selectedMeta },
                    onOptionSelected = { option -> syncSettings.metaSyncInterval.value = option },
                    displayText = { seconds -> labelForSeconds(seconds) },
                ),
                SettingItem.DialogSetting(
                    id = "sync_list_interval",
                    title = context.getString(R.string.title_settings_sync_lists_interval),
                    summary = context.getString(R.string.summary_settings_sync_lists_interval),
                    icon = Icons.Outlined.Checklist,
                    options = listOptions,
                    selectedOption = { selectedList },
                    onOptionSelected = { option -> syncSettings.listSyncInterval.value = option },
                    displayText = { seconds -> labelForSeconds(seconds) },
                ),
                SettingItem.DialogSetting(
                    id = "sync_user_interval",
                    title = context.getString(R.string.title_settings_sync_user_interval),
                    summary = context.getString(R.string.summary_settings_sync_user_interval),
                    icon = Icons.Outlined.SupervisedUserCircle,
                    options = userOptions,
                    selectedOption = { selectedUser },
                    onOptionSelected = { option -> syncSettings.userSyncInterval.value = option },
                    displayText = { seconds -> labelForSeconds(seconds) },
                ),
            ),
        )

        return preferenceBuilder.build()
    }
}
