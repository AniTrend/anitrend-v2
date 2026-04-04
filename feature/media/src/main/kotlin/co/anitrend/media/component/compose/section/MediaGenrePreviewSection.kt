/*
 * Copyright (C) 2026 AniTrend
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
package co.anitrend.media.component.compose.section

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import co.anitrend.android.core.helpers.color.asColorInt
import co.anitrend.domain.genre.entity.Genre
import co.anitrend.media.R
import co.anitrend.navigation.MediaDiscoverRouter

@Composable
private fun Genre.Extended.rememberAccentColor(): Color {
    val surface = MaterialTheme.colorScheme.surface.toArgb()
    val defaultAccent = MaterialTheme.colorScheme.primary

    return remember(this, surface, defaultAccent) {
        background
            ?.let { value -> runCatching { Color(value.asColorInt(surface)) }.getOrNull() }
            ?: defaultAccent
    }
}

@Composable
private fun GenreChip(
    genre: Genre.Extended,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val accent = genre.rememberAccentColor()

    Surface(
        color = accent.copy(alpha = 0.14f).compositeOver(MaterialTheme.colorScheme.surfaceVariant),
        contentColor = MaterialTheme.colorScheme.onSurface,
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, accent.copy(alpha = 0.36f)),
        modifier =
            modifier.clickable(onClick = onClick),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            genre.emoji?.takeIf(String::isNotBlank)?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.titleSmall,
                )
            }
            Text(
                text = genre.name,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Medium,
            )
        }
    }
}

@Composable
fun MediaGenrePreviewSection(
    genres: List<Genre.Extended>,
    onMediaDiscoverableItemClick: (MediaDiscoverRouter.MediaDiscoverParam) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (genres.isEmpty()) {
        return
    }

    MediaHubSection(
        title = stringResource(R.string.label_media_genre_section_title),
        subtitle = stringResource(R.string.subtitle_media_genre_section),
        modifier = modifier,
    ) {
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            genres.forEach { genre ->
                GenreChip(
                    genre = genre,
                    onClick = {
                        onMediaDiscoverableItemClick(
                            MediaDiscoverRouter.MediaDiscoverParam(genre = genre.name),
                        )
                    },
                )
            }
        }
    }
}
