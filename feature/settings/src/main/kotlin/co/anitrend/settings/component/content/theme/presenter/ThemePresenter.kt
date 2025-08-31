package co.anitrend.settings.component.content.theme.presenter

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ColorLens
import co.anitrend.android.core.R
import co.anitrend.android.core.settings.Settings
import co.anitrend.android.core.settings.common.theme.IThemeSettings
import co.anitrend.android.core.settings.helper.theme.model.AniTrendTheme
import co.anitrend.core.presenter.CorePresenter
import co.anitrend.settings.component.builder.contract.IPreferenceBuilder
import co.anitrend.settings.model.SettingItem

class ThemePresenter(
    context: Context,
    private val preferenceBuilder: IPreferenceBuilder,
    settings: Settings,
) : CorePresenter(context, settings) {

    private fun themeLabel(theme: AniTrendTheme): String = when (theme) {
        AniTrendTheme.SYSTEM -> context.getString(R.string.global_label_system)
        AniTrendTheme.DYNAMIC -> "Dynamic"
        AniTrendTheme.AMOLED -> "Black"
        AniTrendTheme.LIGHT -> "Light"
        AniTrendTheme.DARK -> "Dark"
    }

    fun getItems(): List<SettingItem> {
        preferenceBuilder.clear()

        val themeSettings = settings as IThemeSettings
        val options = AniTrendTheme.entries.toList()
        val selected = themeSettings.theme.value

        // Intro
        preferenceBuilder.add(
            entries = listOf(
                SettingItem.HintCard(
                    id = "theme_hint",
                    title = context.getString(co.anitrend.settings.R.string.preference_title_theme),
                    description = context.getString(co.anitrend.settings.R.string.preference_summary_theme),
                    icon = Icons.Outlined.ColorLens,
                    onClick = {},
                )
            )
        )

        // Choices
        preferenceBuilder.add(
            category = SettingItem.CategoryHeader(
                id = "theme_header",
                title = context.getString(co.anitrend.settings.R.string.preference_title_theme),
            ),
            entries = listOf(
                SettingItem.DialogSetting(
                    id = "theme_choice",
                    title = context.getString(co.anitrend.settings.R.string.preference_title_theme),
                    summary = context.getString(co.anitrend.settings.R.string.preference_summary_theme),
                    icon = Icons.Outlined.ColorLens,
                    options = options,
                    selectedOption = { selected },
                    onOptionSelected = { theme -> themeSettings.theme.value = theme },
                    displayText = { theme -> themeLabel(theme) },
                )
            ),
        )

        return preferenceBuilder.build()
    }
}
