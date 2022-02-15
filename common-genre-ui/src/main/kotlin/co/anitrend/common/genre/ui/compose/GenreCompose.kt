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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import co.anitrend.arch.extension.ext.getLayoutInflater
import co.anitrend.common.genre.databinding.GenreItemBinding
import co.anitrend.core.android.views.text.TextDrawable
import co.anitrend.domain.genre.entity.Genre

@Composable
private fun GenreItem(genre: Genre) {
    AndroidView(
        factory = {
            val binding = GenreItemBinding.inflate(it.getLayoutInflater())
            binding.genre.text = genre.name
            binding.genre.chipIcon = TextDrawable(it, genre.emoji)
            binding.root
        },
        modifier = Modifier
    )
}

@Composable
fun GenresListComponent(
    genres: List<Genre>,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        cells = GridCells.Adaptive(88.dp),
        modifier = modifier,
        contentPadding = PaddingValues(8.dp),
        content = {
            items(genres) { genre ->
                GenreItem(genre)
            }
        }
    )
}