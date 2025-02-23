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
package co.anitrend.common.genre.ui.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ElevatedSuggestionChip
import androidx.compose.material3.Icon
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import co.anitrend.core.android.views.text.TextDrawable
import co.anitrend.domain.genre.entity.Genre
import co.anitrend.navigation.MediaDiscoverRouter
import com.google.accompanist.drawablepainter.rememberDrawablePainter

@Composable
private fun GenreItem(
    genre: Genre,
    onMediaDiscoverableItemClick: (MediaDiscoverRouter.MediaDiscoverParam) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    ElevatedSuggestionChip(
        icon = {
            Icon(
                painter = rememberDrawablePainter(TextDrawable(context, genre.emoji)),
                contentDescription = null,
            )
        },
        label = { Text(text = genre.name) },
        onClick = {
            onMediaDiscoverableItemClick(
                MediaDiscoverRouter.MediaDiscoverParam(genre = genre.name),
            )
        },
        shape = SuggestionChipDefaults.shape,
        modifier = modifier,
    )
}

@Composable
fun GenresListComponent(
    genres: List<Genre>,
    onMediaDiscoverableItemClick: (MediaDiscoverRouter.MediaDiscoverParam) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        state = rememberLazyListState(),
        contentPadding = PaddingValues(all = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        modifier = modifier,
    ) {
        items(
            count = genres.size,
            key = { genres[it].id },
            contentType = { "Genre" },
        ) { index ->
            GenreItem(
                genre = genres[index],
                onMediaDiscoverableItemClick = onMediaDiscoverableItemClick,
            )
        }
    }
}
