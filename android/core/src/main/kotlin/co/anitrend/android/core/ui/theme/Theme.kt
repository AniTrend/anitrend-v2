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
package co.anitrend.android.core.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.fragment.app.FragmentActivity
import co.anitrend.android.core.settings.helper.theme.contract.IThemeHelper
import co.anitrend.android.core.ui.color.md_theme_dark_background
import co.anitrend.android.core.ui.color.md_theme_dark_error
import co.anitrend.android.core.ui.color.md_theme_dark_errorContainer
import co.anitrend.android.core.ui.color.md_theme_dark_inverseOnSurface
import co.anitrend.android.core.ui.color.md_theme_dark_inversePrimary
import co.anitrend.android.core.ui.color.md_theme_dark_inverseSurface
import co.anitrend.android.core.ui.color.md_theme_dark_onBackground
import co.anitrend.android.core.ui.color.md_theme_dark_onError
import co.anitrend.android.core.ui.color.md_theme_dark_onErrorContainer
import co.anitrend.android.core.ui.color.md_theme_dark_onPrimary
import co.anitrend.android.core.ui.color.md_theme_dark_onPrimaryContainer
import co.anitrend.android.core.ui.color.md_theme_dark_onSecondary
import co.anitrend.android.core.ui.color.md_theme_dark_onSecondaryContainer
import co.anitrend.android.core.ui.color.md_theme_dark_onSurface
import co.anitrend.android.core.ui.color.md_theme_dark_onSurfaceVariant
import co.anitrend.android.core.ui.color.md_theme_dark_onTertiary
import co.anitrend.android.core.ui.color.md_theme_dark_onTertiaryContainer
import co.anitrend.android.core.ui.color.md_theme_dark_outline
import co.anitrend.android.core.ui.color.md_theme_dark_outlineVariant
import co.anitrend.android.core.ui.color.md_theme_dark_primary
import co.anitrend.android.core.ui.color.md_theme_dark_primaryContainer
import co.anitrend.android.core.ui.color.md_theme_dark_scrim
import co.anitrend.android.core.ui.color.md_theme_dark_secondary
import co.anitrend.android.core.ui.color.md_theme_dark_secondaryContainer
import co.anitrend.android.core.ui.color.md_theme_dark_surface
import co.anitrend.android.core.ui.color.md_theme_dark_surfaceTint
import co.anitrend.android.core.ui.color.md_theme_dark_surfaceVariant
import co.anitrend.android.core.ui.color.md_theme_dark_tertiary
import co.anitrend.android.core.ui.color.md_theme_dark_tertiaryContainer
import co.anitrend.android.core.ui.color.md_theme_light_background
import co.anitrend.android.core.ui.color.md_theme_light_error
import co.anitrend.android.core.ui.color.md_theme_light_errorContainer
import co.anitrend.android.core.ui.color.md_theme_light_inverseOnSurface
import co.anitrend.android.core.ui.color.md_theme_light_inversePrimary
import co.anitrend.android.core.ui.color.md_theme_light_inverseSurface
import co.anitrend.android.core.ui.color.md_theme_light_onBackground
import co.anitrend.android.core.ui.color.md_theme_light_onError
import co.anitrend.android.core.ui.color.md_theme_light_onErrorContainer
import co.anitrend.android.core.ui.color.md_theme_light_onPrimary
import co.anitrend.android.core.ui.color.md_theme_light_onPrimaryContainer
import co.anitrend.android.core.ui.color.md_theme_light_onSecondary
import co.anitrend.android.core.ui.color.md_theme_light_onSecondaryContainer
import co.anitrend.android.core.ui.color.md_theme_light_onSurface
import co.anitrend.android.core.ui.color.md_theme_light_onSurfaceVariant
import co.anitrend.android.core.ui.color.md_theme_light_onTertiary
import co.anitrend.android.core.ui.color.md_theme_light_onTertiaryContainer
import co.anitrend.android.core.ui.color.md_theme_light_outline
import co.anitrend.android.core.ui.color.md_theme_light_outlineVariant
import co.anitrend.android.core.ui.color.md_theme_light_primary
import co.anitrend.android.core.ui.color.md_theme_light_primaryContainer
import co.anitrend.android.core.ui.color.md_theme_light_scrim
import co.anitrend.android.core.ui.color.md_theme_light_secondary
import co.anitrend.android.core.ui.color.md_theme_light_secondaryContainer
import co.anitrend.android.core.ui.color.md_theme_light_surface
import co.anitrend.android.core.ui.color.md_theme_light_surfaceTint
import co.anitrend.android.core.ui.color.md_theme_light_surfaceVariant
import co.anitrend.android.core.ui.color.md_theme_light_tertiary
import co.anitrend.android.core.ui.color.md_theme_light_tertiaryContainer
import co.anitrend.android.core.ui.typography.AniTrendTypography
import com.google.android.material.color.MaterialColors
import org.koin.compose.koinInject

private val LightColorScheme =
    lightColorScheme(
        primary = md_theme_light_primary,
        onPrimary = md_theme_light_onPrimary,
        primaryContainer = md_theme_light_primaryContainer,
        onPrimaryContainer = md_theme_light_onPrimaryContainer,
        secondary = md_theme_light_secondary,
        onSecondary = md_theme_light_onSecondary,
        secondaryContainer = md_theme_light_secondaryContainer,
        onSecondaryContainer = md_theme_light_onSecondaryContainer,
        tertiary = md_theme_light_tertiary,
        onTertiary = md_theme_light_onTertiary,
        tertiaryContainer = md_theme_light_tertiaryContainer,
        onTertiaryContainer = md_theme_light_onTertiaryContainer,
        error = md_theme_light_error,
        onError = md_theme_light_onError,
        errorContainer = md_theme_light_errorContainer,
        onErrorContainer = md_theme_light_onErrorContainer,
        outline = md_theme_light_outline,
        background = md_theme_light_background,
        onBackground = md_theme_light_onBackground,
        surface = md_theme_light_surface,
        onSurface = md_theme_light_onSurface,
        surfaceVariant = md_theme_light_surfaceVariant,
        onSurfaceVariant = md_theme_light_onSurfaceVariant,
        inverseSurface = md_theme_light_inverseSurface,
        inverseOnSurface = md_theme_light_inverseOnSurface,
        inversePrimary = md_theme_light_inversePrimary,
        surfaceTint = md_theme_light_surfaceTint,
        outlineVariant = md_theme_light_outlineVariant,
        scrim = md_theme_light_scrim,
    )

private val DarkColorScheme =
    darkColorScheme(
        primary = md_theme_dark_primary,
        onPrimary = md_theme_dark_onPrimary,
        primaryContainer = md_theme_dark_primaryContainer,
        onPrimaryContainer = md_theme_dark_onPrimaryContainer,
        secondary = md_theme_dark_secondary,
        onSecondary = md_theme_dark_onSecondary,
        secondaryContainer = md_theme_dark_secondaryContainer,
        onSecondaryContainer = md_theme_dark_onSecondaryContainer,
        tertiary = md_theme_dark_tertiary,
        onTertiary = md_theme_dark_onTertiary,
        tertiaryContainer = md_theme_dark_tertiaryContainer,
        onTertiaryContainer = md_theme_dark_onTertiaryContainer,
        error = md_theme_dark_error,
        onError = md_theme_dark_onError,
        errorContainer = md_theme_dark_errorContainer,
        onErrorContainer = md_theme_dark_onErrorContainer,
        outline = md_theme_dark_outline,
        background = md_theme_dark_background,
        onBackground = md_theme_dark_onBackground,
        surface = md_theme_dark_surface,
        onSurface = md_theme_dark_onSurface,
        surfaceVariant = md_theme_dark_surfaceVariant,
        onSurfaceVariant = md_theme_dark_onSurfaceVariant,
        inverseSurface = md_theme_dark_inverseSurface,
        inverseOnSurface = md_theme_dark_inverseOnSurface,
        inversePrimary = md_theme_dark_inversePrimary,
        surfaceTint = md_theme_dark_surfaceTint,
        outlineVariant = md_theme_dark_outlineVariant,
        scrim = md_theme_dark_scrim,
    )

private val AniTrendShapes =
    Shapes(
        extraSmall = ShapeDefaults.ExtraLarge,
        small = ShapeDefaults.ExtraLarge,
        medium = ShapeDefaults.ExtraLarge,
        large = ShapeDefaults.ExtraLarge,
        extraLarge = ShapeDefaults.ExtraLarge,
    )

fun Color.applyOpacity(enabled: Boolean): Color = if (enabled) this else this.copy(alpha = 0.62f)

@Composable
@ReadOnlyComposable
fun Color.harmonizeWith(other: Color) = Color(MaterialColors.harmonize(this.toArgb(), other.toArgb()))

@Composable
@ReadOnlyComposable
fun Color.harmonizeWithPrimary(): Color = this.harmonizeWith(other = MaterialTheme.colorScheme.primary)

@Composable
fun AniTrendTheme3(
    darkTheme: Boolean = isSystemInDarkTheme(),
    themeHelper: IThemeHelper = koinInject(),
    content: @Composable () -> Unit,
) {
    val view = LocalView.current
    val colorScheme =
        when {
            themeHelper.dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                if (darkTheme) dynamicDarkColorScheme(view.context) else dynamicLightColorScheme(view.context)
            }
            darkTheme -> DarkColorScheme
            else -> LightColorScheme
        }
    if (!view.isInEditMode) {
        SideEffect {
            themeHelper.applyApplicationTheme(view.context as FragmentActivity)
            themeHelper.applyDynamicNightModeFromTheme()
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        shapes = AniTrendShapes,
        typography = AniTrendTypography,
        content = content,
    )
}
