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
package co.anitrend.settings.component.content.filters

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import co.anitrend.android.core.compose.design.cards.AniTrendCautionCard
import co.anitrend.android.core.compose.design.cards.AniTrendHintCard
import co.anitrend.android.core.ui.AniTrendPreview
import co.anitrend.android.core.ui.theme.preview.DarkThemeProvider
import co.anitrend.android.core.ui.theme.preview.PreviewTheme
import co.anitrend.settings.R
import co.anitrend.settings.component.compose.SettingsSectionCard
import co.anitrend.settings.component.compose.SettingsValueRow

@Composable
fun FiltersScreen(modifier: Modifier = Modifier) {
    FiltersContent(modifier = modifier)
}

@Composable
private fun FiltersContent(modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        items(listOf(Unit)) {
            AniTrendHintCard(
                title = stringResource(R.string.title_settings_filters_overview),
                description = stringResource(R.string.summary_settings_filters_overview),
                icon = Icons.Outlined.FilterList,
            )
        }
        items(listOf(Unit)) {
            SettingsSectionCard(
                title = stringResource(R.string.title_settings_filters_planned_controls),
                description = stringResource(R.string.summary_settings_filters_planned_controls),
            ) {
                SettingsValueRow(
                    title = stringResource(R.string.title_settings_filters_muted_themes),
                    summary = stringResource(R.string.summary_settings_filters_muted_themes),
                    icon = Icons.Outlined.FilterList,
                    currentValue = stringResource(R.string.label_settings_filters_soon),
                    enabled = false,
                    onClick = {},
                )
                androidx.compose.material3.HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    color =
                        androidx.compose.material3.MaterialTheme.colorScheme.outlineVariant
                            .copy(alpha = 0.35f),
                )
                SettingsValueRow(
                    title = stringResource(R.string.title_settings_filters_content_sensitivity),
                    summary = stringResource(R.string.summary_settings_filters_content_sensitivity),
                    icon = Icons.Outlined.FilterList,
                    currentValue = stringResource(R.string.label_settings_filters_soon),
                    enabled = false,
                    onClick = {},
                )
            }
        }
        items(listOf(Unit)) {
            AniTrendCautionCard(
                title = stringResource(R.string.title_settings_filters_not_active),
                description = stringResource(R.string.summary_settings_filters_not_active),
            )
        }
    }
}

@AniTrendPreview.Light
@AniTrendPreview.Dark
@AniTrendPreview.Mobile
@Composable
private fun FiltersScreenPreview(
    @PreviewParameter(DarkThemeProvider::class) darkTheme: Boolean,
) {
    PreviewTheme(wrapInSurface = true, darkTheme = darkTheme) {
        FiltersContent()
    }
}
