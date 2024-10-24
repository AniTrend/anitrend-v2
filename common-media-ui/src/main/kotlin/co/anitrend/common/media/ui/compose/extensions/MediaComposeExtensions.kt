/*
 * Copyright (C) 2022  AniTrend
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

package co.anitrend.common.media.ui.compose.extensions

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import co.anitrend.core.android.helpers.color.asColorInt
import co.anitrend.domain.common.entity.contract.IMediaCover

@Composable
fun IMediaCover.rememberAccentColor(): Color {
    val defaultColor = MaterialTheme.colorScheme.onBackground
    val surface = MaterialTheme.colorScheme.background
    val accent = remember(color) {
        color
            ?.asColorInt(surface.toArgb())
            ?.let(::Color)
            ?: defaultColor
    }
    return accent
}
