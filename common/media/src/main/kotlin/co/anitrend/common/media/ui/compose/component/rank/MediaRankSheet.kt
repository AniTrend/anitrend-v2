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

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import co.anitrend.android.core.ui.AniTrendPreview
import co.anitrend.android.core.ui.theme.preview.DarkThemeProvider
import co.anitrend.android.core.ui.theme.preview.PreviewTheme
import co.anitrend.common.media.ui.compose.widget.ranking.RankingItems
import co.anitrend.common.shared.ui.compose.sheet.ListBottomSheet
import co.anitrend.domain.media.entity.attribute.rank.IMediaRank
import co.anitrend.domain.media.enums.MediaSort
import co.anitrend.navigation.model.sorting.Sorting

@Composable
fun MediaRankSheet(
    ranks: List<IMediaRank>,
    onItemClick: (IMediaRank, List<Sorting<MediaSort>>) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showSheet by remember { mutableStateOf(true) }
    if (showSheet) {
        ListBottomSheet(
            onDismiss = {
                showSheet = false
                onDismiss()
            },
            modifier = modifier,
        ) {
            MediaRankSheetContent(
                ranks = ranks,
                onItemClick = onItemClick,
            )
        }
    }
}

@Composable
private fun MediaRankSheetContent(
    ranks: List<IMediaRank>,
    onItemClick: (IMediaRank, List<Sorting<MediaSort>>) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        MediaRankSectionHeader(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
        )
        Spacer(modifier = Modifier.padding(bottom = 8.dp))
        RankingItems(
            rankings = ranks,
            accentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp),
            onClick = onItemClick,
        )
    }
}

@AniTrendPreview.Default
@Composable
private fun MediaRankSheetPreview(
    @PreviewParameter(DarkThemeProvider::class) darkTheme: Boolean,
) {
    PreviewTheme(wrapInSurface = true, darkTheme = darkTheme) {
        MediaRankSheetContent(
            ranks = MediaRankPreviewProvider().values.toList(),
            onItemClick = { _, _ -> },
        )
    }
}
