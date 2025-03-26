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
package co.anitrend.android.core.ui.tokens

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight

internal object TypefaceTokens {
    val Brand =
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
}
