package co.anitrend.core.android.ui.theme.preview

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.fragment.app.FragmentActivity
import co.anitrend.core.android.settings.helper.theme.contract.IThemeHelper
import co.anitrend.core.android.ui.theme.AniTrendTheme3

private val PreviewThemeHelper = object : IThemeHelper {

    override val dynamicColor: Boolean = false

    /**
     * Sets the default night mode based on the theme set in settings
     */
    override fun applyDynamicNightModeFromTheme() {}

    /**
     * Applies settings theme resource or provided [themeOverride] which overrides settings
     */
    override fun applyApplicationTheme(context: FragmentActivity, themeOverride: Int?) {}
}

class DarkThemeProvider: PreviewParameterProvider<Boolean> {
    override val values = sequenceOf(false, true)
}

@Composable
fun PreviewTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    themeHelper: IThemeHelper = PreviewThemeHelper,
    content: @Composable () -> Unit
) {
    AniTrendTheme3(
        darkTheme = darkTheme,
        themeHelper = themeHelper,
        content = content,
    )
}
