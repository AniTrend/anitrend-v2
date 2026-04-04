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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import co.anitrend.common.media.ui.compose.component.rank.MediaRankItem
import co.anitrend.common.media.ui.compose.component.rank.MediaRankSheet
import co.anitrend.domain.media.entity.attribute.rank.IMediaRank
import co.anitrend.domain.media.enums.MediaSort
import co.anitrend.navigation.model.sorting.Sorting
import co.anitrend.common.media.ui.R as MediaUiR

@Composable
fun MediaRankPreviewSection(
    ranks: List<IMediaRank>,
    onClick: (IMediaRank, List<Sorting<MediaSort>>) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (ranks.isEmpty()) {
        return
    }

    val previewRanks = remember(ranks) { selectRankingPreview(ranks) }
    var showAll by remember { mutableStateOf(false) }

    MediaHubSection(
        title = stringResource(MediaUiR.string.label_media_rank_section_title),
        subtitle = stringResource(MediaUiR.string.label_media_rank_sheet_subtitle),
        trailingActionLabel =
            if (ranks.size > previewRanks.size) {
                stringResource(MediaUiR.string.label_media_rank_section_show_all_rankings)
            } else {
                null
            },
        onTrailingAction =
            if (ranks.size > previewRanks.size) {
                { showAll = true }
            } else {
                null
            },
        modifier = modifier,
    ) {
        previewRanks.forEach { rank ->
            MediaRankItem(
                rank = rank,
                onClick = onClick,
            )
        }
    }

    if (showAll) {
        MediaRankSheet(
            ranks = ranks,
            onItemClick = onClick,
            onDismiss = { showAll = false },
        )
    }
}
