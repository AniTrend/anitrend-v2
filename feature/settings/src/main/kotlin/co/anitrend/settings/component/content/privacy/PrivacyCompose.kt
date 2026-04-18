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
package co.anitrend.settings.component.content.privacy

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PrivacyTip
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
import co.anitrend.data.settings.privacy.IPrivacySettings
import co.anitrend.settings.R
import co.anitrend.settings.component.compose.SettingsSectionCard
import co.anitrend.settings.component.compose.SettingsToggleRow
import org.koin.compose.koinInject

@Composable
fun PrivacyScreen(
    modifier: Modifier = Modifier,
    settings: IPrivacySettings = koinInject(),
) {
    var analyticsEnabled by remember { mutableStateOf(settings.isAnalyticsEnabled.value) }
    var crashReportingEnabled by remember { mutableStateOf(settings.isCrashlyticsEnabled.value) }

    PrivacyContent(
        modifier = modifier,
        analyticsEnabled = analyticsEnabled,
        crashReportingEnabled = crashReportingEnabled,
        onAnalyticsChange = {
            settings.isAnalyticsEnabled.value = it
            analyticsEnabled = it
        },
        onCrashReportingChange = {
            settings.isCrashlyticsEnabled.value = it
            crashReportingEnabled = it
        },
    )
}

@Composable
private fun PrivacyContent(
    modifier: Modifier = Modifier,
    analyticsEnabled: Boolean,
    crashReportingEnabled: Boolean,
    onAnalyticsChange: (Boolean) -> Unit = {},
    onCrashReportingChange: (Boolean) -> Unit = {},
) {
    val postureLabel =
        when {
            analyticsEnabled && crashReportingEnabled -> stringResource(R.string.label_settings_state_enabled)
            !analyticsEnabled && !crashReportingEnabled -> stringResource(R.string.label_settings_state_minimal)
            else -> stringResource(R.string.label_settings_state_custom)
        }

    LazyColumn(modifier = modifier) {
        items(listOf(Unit)) {
            AniTrendHintCard(
                title = stringResource(R.string.preference_title_privacy),
                description = stringResource(R.string.preference_summary_privacy),
                icon = Icons.Outlined.PrivacyTip,
                currentValue = postureLabel,
            )
        }
        items(listOf(Unit)) {
            SettingsSectionCard(
                title = stringResource(R.string.title_settings_privacy_data_sharing),
                description = stringResource(R.string.preference_summary_privacy),
            ) {
                SettingsToggleRow(
                    title = stringResource(R.string.preference_title_privacy_analytics_config),
                    summary = stringResource(R.string.preference_summary_privacy_analytics_config),
                    icon = Icons.Outlined.PrivacyTip,
                    checked = analyticsEnabled,
                    onCheckedChange = onAnalyticsChange,
                )
                SettingsToggleRow(
                    title = stringResource(R.string.preference_title_privacy_crash_analytics_config),
                    summary = stringResource(R.string.preference_summary_privacy_crash_analytics_config),
                    icon = Icons.Outlined.PrivacyTip,
                    checked = crashReportingEnabled,
                    onCheckedChange = onCrashReportingChange,
                )
            }
        }
    }
}

@AniTrendPreview.Default
@Composable
private fun PrivacyScreenPreview(
    @PreviewParameter(DarkThemeProvider::class) darkTheme: Boolean,
) {
    PreviewTheme(wrapInSurface = true, darkTheme = darkTheme) {
        PrivacyContent(
            analyticsEnabled = false,
            crashReportingEnabled = true,
        )
    }
}
