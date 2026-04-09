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
package co.anitrend.common.media.ui.compose.widget.title

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import co.anitrend.android.core.ui.AniTrendPreview
import co.anitrend.android.core.ui.theme.preview.DarkThemeProvider
import co.anitrend.android.core.ui.theme.preview.PreviewTheme
import co.anitrend.domain.common.entity.shared.FuzzyDate
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.media.entity.attribute.image.MediaImage
import co.anitrend.domain.media.enums.MediaFormat


private fun Media.metaLine(): String? =
    buildList {
        format?.alias?.toString()?.takeIf(String::isNotBlank)?.let(::add)
        season?.alias?.toString()?.takeIf(String::isNotBlank)?.let(::add)
        startDate.year.takeIf { it > 0 }?.toString()?.let(::add)
    }.takeIf(List<String>::isNotEmpty)?.joinToString(separator = " • ")


/**
 * Displays subtitle text in the following format for anime and manga respectively
 * > TV • Summer • 2018
 *
 * > Novel • 2018
 */
@Composable
fun MediaMetaLineText(
    media: Media,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
    color: Color = MaterialTheme.colorScheme.primary,
    maxLines: Int = 1,
    overflow: TextOverflow = TextOverflow.Ellipsis,
) {
    media.metaLine()?.also {
        Text(
            text = it,
            style = style,
            color = color,
            maxLines = maxLines,
            overflow = overflow,
            modifier = modifier,
        )
    }
}

@AniTrendPreview.Default
@Composable
private fun MediaMetaLineTextPreview(
    @PreviewParameter(DarkThemeProvider::class) darkTheme: Boolean,
) {
    PreviewTheme(darkTheme = darkTheme, wrapInSurface = true) {
        MediaMetaLineText(
            media =
                Media.Core.empty().copy(
                    image = MediaImage.empty().copy(color = "#e4a15d"),
                    startDate = FuzzyDate.empty().copy(2018),
                    format = MediaFormat.TV,
                    category =
                        Media.Category.Anime
                            .empty()
                            .copy(25),
                ),
        )
    }
}
