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
package co.anitrend.settings.component.content.theme

import android.os.Build
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ColorLens
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import co.anitrend.android.core.compose.design.cards.AniTrendCautionCard
import co.anitrend.android.core.compose.design.cards.AniTrendHintCard
import co.anitrend.android.core.compose.design.choice.AniTrendSingleChoiceItem
import co.anitrend.android.core.settings.common.theme.IThemeSettings
import co.anitrend.android.core.settings.helper.theme.model.AniTrendTheme
import co.anitrend.android.core.ui.AniTrendPreview
import co.anitrend.android.core.ui.theme.preview.DarkThemeProvider
import co.anitrend.android.core.ui.theme.preview.PreviewTheme
import co.anitrend.settings.R
import co.anitrend.settings.component.compose.SettingsSectionCard
import org.koin.compose.koinInject

@Composable
fun ThemeScreen(
    modifier: Modifier = Modifier,
    settings: IThemeSettings = koinInject(),
) {
    var selectedTheme by remember { mutableStateOf(settings.theme.value) }
    ThemeContent(
        modifier = modifier,
        selectedTheme = selectedTheme,
        onThemeSelected = {
            settings.theme.value = it
            selectedTheme = it
        },
    )
}

@Composable
private fun ThemeContent(
    modifier: Modifier = Modifier,
    selectedTheme: AniTrendTheme,
    onThemeSelected: (AniTrendTheme) -> Unit,
) {
    val isDynamicSupported = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    LazyColumn(modifier = modifier) {
        items(listOf(Unit)) {
            AniTrendHintCard(
                title = stringResource(R.string.title_settings_theme_color_appearance),
                description = stringResource(R.string.summary_settings_theme_color_appearance),
                icon = Icons.Outlined.ColorLens,
                currentValue = themeLabel(selectedTheme),
            )
        }
        items(listOf(Unit)) {
            SettingsSectionCard(
                title = stringResource(R.string.title_settings_theme_current),
                description = themeLabel(selectedTheme),
            ) {}
        }
        items(listOf(Unit)) {
            SettingsSectionCard(
                title = stringResource(R.string.title_settings_theme_choose),
                description = stringResource(R.string.summary_settings_theme_choose),
            ) {
                AniTrendTheme.entries.forEach { theme ->
                    val enabled = theme != AniTrendTheme.DYNAMIC || isDynamicSupported
                    AniTrendSingleChoiceItem(
                        text = themeLabel(theme),
                        selected = theme == selectedTheme,
                        onOptionSelected = {
                            if (enabled) {
                                onThemeSelected(theme)
                            }
                        },
                    )
                }
            }
        }
        if (!isDynamicSupported) {
            items(listOf(Unit)) {
                AniTrendCautionCard(
                    title = stringResource(R.string.title_settings_theme_dynamic_unavailable),
                    description = stringResource(R.string.summary_settings_theme_dynamic_unavailable),
                )
            }
        }
    }
}

@Composable
private fun themeLabel(theme: AniTrendTheme): String =
    when (theme) {
        AniTrendTheme.SYSTEM -> stringResource(co.anitrend.android.core.R.string.global_label_system)
        AniTrendTheme.DYNAMIC -> stringResource(R.string.label_settings_theme_dynamic)
        AniTrendTheme.AMOLED -> stringResource(R.string.label_settings_theme_black)
        AniTrendTheme.LIGHT -> stringResource(R.string.label_settings_theme_light)
        AniTrendTheme.DARK -> stringResource(R.string.label_settings_theme_dark)
    }

@AniTrendPreview.Light
@AniTrendPreview.Dark
@AniTrendPreview.Mobile
@Composable
private fun ThemeScreenPreview(
    @PreviewParameter(DarkThemeProvider::class) darkTheme: Boolean,
) {
    PreviewTheme(wrapInSurface = true, darkTheme = darkTheme) {
        ThemeContent(
            selectedTheme = AniTrendTheme.AMOLED,
            onThemeSelected = {},
        )
    }
}
