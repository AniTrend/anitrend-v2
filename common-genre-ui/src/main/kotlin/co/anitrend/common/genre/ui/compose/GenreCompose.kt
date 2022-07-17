/*
 * Copyright (C) 2021  AniTrend
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

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import co.anitrend.arch.extension.ext.getLayoutInflater
import co.anitrend.common.genre.databinding.GenreItemBinding
import co.anitrend.core.android.views.text.TextDrawable
import co.anitrend.domain.genre.entity.Genre

@Composable
private fun GenreItem(
    modifier: Modifier = Modifier,
    genre: Genre
) {
    AndroidView(
        factory = { context ->
            val binding = GenreItemBinding.inflate(context.getLayoutInflater())
            binding.genre.text = genre.name
            binding.genre.chipIcon = TextDrawable(context, genre.emoji)
            binding.root
        },
        modifier = modifier
    )
}

@Composable
fun GenresListComponent(
    genres: List<Genre>,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(88.dp),
        modifier = modifier,
        contentPadding = PaddingValues(8.dp)
    ) {
        items(genres.size) { index ->
            GenreItem(
                modifier = Modifier,
                genre = genres[index]
            )
        }
    }
}