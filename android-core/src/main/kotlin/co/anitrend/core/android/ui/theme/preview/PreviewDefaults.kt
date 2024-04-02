package co.anitrend.core.android.ui.theme.preview

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.fragment.app.FragmentActivity
import co.anitrend.core.android.settings.helper.theme.contract.IThemeHelper
import co.anitrend.core.android.ui.theme.AniTrendTheme3

private val PreviewThemeHelper = object : IThemeHelper {
    /**
     * Sets the default night mode based on the theme set in settings
     */
    override fun applyDynamicNightModeFromTheme() {}

    /**
     * Applies settings theme resource or provided [themeOverride] which overrides settings
     */
    override fun applyApplicationTheme(context: FragmentActivity, themeOverride: Int?) {}
}

@Composable
fun PreviewTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    AniTrendTheme3(
        darkTheme = darkTheme,
        themeHelper = PreviewThemeHelper,
        dynamicColor = dynamicColor,
        content = content,
    )
}
