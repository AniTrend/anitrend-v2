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
package co.anitrend.common.media.ui.compose.component.rank

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import co.anitrend.android.core.ui.AniTrendPreview
import co.anitrend.android.core.ui.theme.preview.PreviewTheme
import co.anitrend.arch.extension.ext.capitalizeWords
import co.anitrend.core.extensions.CHARACTER_SEPARATOR
import co.anitrend.domain.common.sort.order.SortOrder
import co.anitrend.domain.media.entity.attribute.rank.IMediaRank
import co.anitrend.domain.media.enums.MediaRankType
import co.anitrend.domain.media.enums.MediaSort
import co.anitrend.navigation.model.sorting.Sorting

@Composable
fun MediaRankItem(
    rank: IMediaRank,
    onClick: (IMediaRank, List<Sorting<MediaSort>>) -> Unit,
    modifier: Modifier = Modifier,
) {
    val content =
        remember(rank.id) {
            buildString {
                append(rank.context.toString().capitalizeWords())
                rank.season?.let { append(" $CHARACTER_SEPARATOR ${it.alias}") }
                rank.year?.let { append(" $CHARACTER_SEPARATOR $it") }
                append(" $CHARACTER_SEPARATOR ${rank.format.alias}")
            }
        }
    Row(
        modifier =
            modifier.clickable(
                enabled = true,
                onClick = {
                    val sorting =
                        when (rank.type) {
                            MediaRankType.RATED ->
                                Sorting(
                                    sortable = MediaSort.SCORE,
                                    order = SortOrder.DESC,
                                )
                            else ->
                                Sorting(
                                    sortable = MediaSort.POPULARITY,
                                    order = SortOrder.DESC,
                                )
                        }
                    onClick(rank, listOf(sorting))
                },
                role = Role.Button,
                onClickLabel = "null",
            ),
    ) {
        Text(
            text = content,
            style = MaterialTheme.typography.bodyMedium,
            modifier =
                Modifier
                    .align(Alignment.CenterVertically)
                    .weight(1f),
        )
        Box {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Numbers,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                )
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                    text = "${rank.rank}",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}

@AniTrendPreview.Light
@AniTrendPreview.Dark
@Composable
private fun MediaRankItemPreview(
    @PreviewParameter(MediaRankPreviewProvider::class) rank: IMediaRank,
) {
    PreviewTheme(wrapInSurface = true) {
        MediaRankItem(
            rank = rank,
            onClick = { _, _ -> },
            modifier = Modifier.padding(8.dp),
        )
    }
}
