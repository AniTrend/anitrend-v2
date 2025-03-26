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
package co.anitrend.settings.component.content.locale

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Translate
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.core.os.LocaleListCompat
import co.anitrend.android.core.R
import co.anitrend.android.core.compose.design.cards.AniTrendHintCard
import co.anitrend.android.core.compose.design.category.AniTrendCategoryHeader
import co.anitrend.android.core.compose.design.choice.AniTrendSingleChoiceItem
import co.anitrend.android.core.settings.common.locale.ILocaleSettings
import co.anitrend.android.core.settings.helper.locale.model.AniTrendLocale
import co.anitrend.android.core.settings.helper.locale.model.AniTrendLocale.Companion.asAniTrendLocale
import co.anitrend.android.core.settings.helper.locale.model.AniTrendLocale.Companion.asDisplayName
import co.anitrend.android.core.settings.helper.locale.model.AniTrendLocale.Companion.asLocale
import co.anitrend.android.core.ui.AniTrendPreview
import co.anitrend.android.core.ui.theme.preview.DarkThemeProvider
import co.anitrend.android.core.ui.theme.preview.PreviewTheme
import org.koin.compose.koinInject
import java.util.Locale

@Composable
private fun rememberLocaleList(key: AniTrendLocale): List<Locale> =
    remember(key) {
        val defaultLocaleListCompat = LocaleListCompat.getDefault()
        (0..defaultLocaleListCompat.size())
            .mapNotNull { defaultLocaleListCompat[it] }
    }

@Composable
private fun rememberSupportLocaleList(key: AniTrendLocale): List<Locale> =
    remember(key) {
        AniTrendLocale.entries.map {
            it.asLocale()
        }
    }

@Composable
fun LocaleScreen(
    modifier: Modifier = Modifier,
    settings: ILocaleSettings = koinInject(),
) {
    var selectedLocale by remember { mutableStateOf(settings.locale.value) }
    val systemLocaleList = rememberLocaleList(selectedLocale)
    val supportedLocales = rememberSupportLocaleList(selectedLocale)

    val suggestedLocales =
        remember(selectedLocale) {
            systemLocaleList.filter { supported ->
                supportedLocales.any { desired ->
                    LocaleListCompat.matchesLanguageAndScript(
                        supported,
                        desired,
                    )
                }
            }
        }

    val allLocales = remember(selectedLocale) { supportedLocales - suggestedLocales.toSet() }

    LocaleContent(
        selectedLocale = selectedLocale.asLocale(),
        suggestedLocales = suggestedLocales,
        allLocales = allLocales,
        onLocaleChange = {
            settings.locale.value = it.asAniTrendLocale()
            selectedLocale = it.asAniTrendLocale()
        },
    )
}

@Composable
private fun LocaleContent(
    modifier: Modifier = Modifier,
    suggestedLocales: List<Locale>,
    allLocales: List<Locale>,
    selectedLocale: Locale,
    onLocaleChange: (Locale) -> Unit = {},
) {
    LazyColumn(modifier = modifier) {
        item {
            AniTrendHintCard(
                title = "Language and region",
                description = "Help us add more languages and regions. Tap to learn more.",
                icon = Icons.Outlined.Translate,
                onClick = {
                },
            )
        }

        if (suggestedLocales.isNotEmpty()) {
            item {
                AniTrendCategoryHeader(
                    text = "Suggested",
                )
            }
            if (!suggestedLocales.contains(Locale.getDefault())) {
                item {
                    val defaultLocale = AniTrendLocale.AUTOMATIC.asLocale()
                    AniTrendSingleChoiceItem(
                        text = stringResource(R.string.global_label_system),
                        selected = defaultLocale == selectedLocale,
                        onOptionSelected = {
                            onLocaleChange(defaultLocale)
                        },
                    )
                }
            }
            for (locale in suggestedLocales) {
                item {
                    AniTrendSingleChoiceItem(
                        text = locale.asDisplayName(),
                        selected = locale == selectedLocale,
                        onOptionSelected = {
                            onLocaleChange(locale)
                        },
                    )
                }
            }
        }

        if (allLocales.isNotEmpty()) {
            item {
                AniTrendCategoryHeader(
                    text = "Supported",
                )
            }
            for (locale in allLocales) {
                item {
                    AniTrendSingleChoiceItem(
                        text = locale.asDisplayName(),
                        selected = locale == selectedLocale,
                        onOptionSelected = {
                            onLocaleChange(locale)
                        },
                    )
                }
            }
        }
    }
}

@AniTrendPreview.Default
@Composable
private fun LocaleScreenPreview(
    @PreviewParameter(DarkThemeProvider::class) darkTheme: Boolean,
) {
    PreviewTheme(wrapInSurface = true, darkTheme = darkTheme) {
        LocaleContent(
            suggestedLocales = listOf(Locale.ENGLISH, Locale.JAPANESE),
            allLocales = listOf(Locale.CHINESE, Locale.FRENCH, Locale.forLanguageTag("ES")),
            selectedLocale = Locale.ENGLISH,
            onLocaleChange = {},
        )
    }
}
