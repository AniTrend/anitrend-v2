/*
 * Copyright (C) 2021 AniTrend
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
package co.anitrend.core.android.compose

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.anitrend.arch.extension.ext.getCompatColor
import co.anitrend.arch.theme.extensions.isEnvironmentNightMode

object AniTrendTheme {
    private val defaultFontFamily =
        FontFamily(
            Font(
                resId = co.anitrend.arch.theme.R.font.product_sans_bold,
                style = FontStyle.Normal,
                weight = FontWeight.Bold,
            ),
            Font(
                resId = co.anitrend.arch.theme.R.font.product_sans_bold_italic,
                style = FontStyle.Italic,
                weight = FontWeight.Bold,
            ),
            Font(
                resId = co.anitrend.arch.theme.R.font.product_sans_italic,
                style = FontStyle.Italic,
                weight = FontWeight.Normal,
            ),
            Font(
                resId = co.anitrend.arch.theme.R.font.product_sans_regular,
                style = FontStyle.Normal,
                weight = FontWeight.Normal,
            ),
        )

    /**
     * Shapes mirroring [co.anitrend.core.android.R.style.ShapeAppearance_MaterialComponents_SmallComponent],
     * [co.anitrend.core.android.R.style.ShapeAppearance_MaterialComponents_MediumComponent] and
     * [co.anitrend.core.android.R.style.ShapeAppearance_MaterialComponents_LargeComponent]
     */
    val shapes =
        Shapes(
            small = RoundedCornerShape(8.dp),
            medium = RoundedCornerShape(16.dp),
            large = RoundedCornerShape(16.dp),
        )

    val colors: Colors
        @Composable get() {
            val context = LocalContext.current
            return Colors(
                primary = Color(context.getCompatColor(co.anitrend.arch.theme.R.color.primaryColor)),
                primaryVariant = Color(context.getCompatColor(co.anitrend.arch.theme.R.color.primaryDarkColor)),
                secondary = Color(context.getCompatColor(co.anitrend.arch.theme.R.color.secondaryColor)),
                secondaryVariant = Color(context.getCompatColor(co.anitrend.arch.theme.R.color.secondaryDarkColor)),
                background = Color(context.getCompatColor(co.anitrend.arch.theme.R.color.colorBackground)),
                surface = Color(context.getCompatColor(co.anitrend.arch.theme.R.color.colorSurface)),
                error = Color(context.getCompatColor(co.anitrend.arch.theme.R.color.colorError)),
                onPrimary = Color(context.getCompatColor(co.anitrend.arch.theme.R.color.colorOnPrimary)),
                onSecondary = Color(context.getCompatColor(co.anitrend.arch.theme.R.color.colorOnSecondary)),
                onBackground = Color(context.getCompatColor(co.anitrend.arch.theme.R.color.colorOnBackground)),
                onSurface = Color(context.getCompatColor(co.anitrend.arch.theme.R.color.colorOnSurface)),
                onError = Color(context.getCompatColor(co.anitrend.arch.theme.R.color.colorOnError)),
                isLight = !context.isEnvironmentNightMode(),
            )
        }

    val typography: Typography
        @Composable get() {
            val context = LocalContext.current
            return Typography(
                defaultFontFamily = defaultFontFamily,
                h1 =
                    TextStyle(
                        fontWeight = FontWeight.Light,
                        fontSize = 96.sp,
                        letterSpacing = (-1.5).sp,
                        color = context.primaryTextColor(),
                    ),
                h2 =
                    TextStyle(
                        fontWeight = FontWeight.Light,
                        fontSize = 60.sp,
                        letterSpacing = (-0.5).sp,
                        color = context.primaryTextColor(),
                    ),
                h3 =
                    TextStyle(
                        fontWeight = FontWeight.Normal,
                        fontSize = 48.sp,
                        letterSpacing = 0.sp,
                        color = context.primaryTextColor(),
                    ),
                h4 =
                    TextStyle(
                        fontWeight = FontWeight.Normal,
                        fontSize = 34.sp,
                        letterSpacing = 0.25.sp,
                        color = context.primaryTextColor(),
                    ),
                h5 =
                    TextStyle(
                        fontWeight = FontWeight.Normal,
                        fontSize = 24.sp,
                        letterSpacing = 0.sp,
                        color = context.primaryTextColor(),
                    ),
                h6 =
                    TextStyle(
                        fontWeight = FontWeight.Medium,
                        fontSize = 20.sp,
                        letterSpacing = 0.15.sp,
                        color = context.primaryTextColor(),
                    ),
                subtitle1 =
                    TextStyle(
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp,
                        letterSpacing = 0.15.sp,
                        color = context.secondaryTextColor(),
                    ),
                subtitle2 =
                    TextStyle(
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        letterSpacing = 0.1.sp,
                        color = context.secondaryTextColor(),
                    ),
                body1 =
                    TextStyle(
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp,
                        letterSpacing = 0.5.sp,
                        color = context.primaryTextColor(),
                    ),
                body2 =
                    TextStyle(
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        letterSpacing = 0.25.sp,
                        color = context.primaryTextColor(),
                    ),
                button =
                    TextStyle(
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        letterSpacing = 1.25.sp,
                        color = context.primaryTextColor(),
                    ),
                caption =
                    TextStyle(
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp,
                        letterSpacing = 0.4.sp,
                        color = context.secondaryTextColor(),
                    ),
                overline =
                    TextStyle(
                        fontWeight = FontWeight.Normal,
                        fontSize = 10.sp,
                        letterSpacing = 1.5.sp,
                        color = context.secondaryTextColor(),
                    ),
            )
        }
}

@Deprecated(
    message = "Use AniTrendTheme3 instead",
    replaceWith =
        ReplaceWith(
            "AniTrend3()",
            "co.anitrend.core.android.ui.theme",
        ),
    level = DeprecationLevel.ERROR,
)
@Composable
fun AniTrendTheme(
    colors: Colors = AniTrendTheme.colors,
    typography: Typography = AniTrendTheme.typography,
    shapes: Shapes = AniTrendTheme.shapes,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colors = colors,
        content = content,
        typography = typography,
        shapes = shapes,
    )
}
