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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import co.anitrend.android.core.ui.AniTrendPreview
import co.anitrend.android.core.ui.theme.preview.DarkThemeProvider
import co.anitrend.android.core.ui.theme.preview.PreviewTheme
import co.anitrend.common.media.ui.R
import co.anitrend.domain.media.entity.attribute.rank.IMediaRank
import co.anitrend.domain.media.enums.MediaSort
import co.anitrend.navigation.model.sorting.Sorting

@Composable
fun MediaRankSectionHeader(modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
        Icon(
            painter = painterResource(co.anitrend.common.media.ui.R.drawable.ic_trophy),
            contentDescription = null,
            modifier = Modifier.align(alignment = Alignment.CenterVertically),
            tint = colorResource(co.anitrend.android.core.R.color.orange_700),
        )
        Spacer(modifier = Modifier.padding(end = 16.dp))
        Text(
            text = stringResource(R.string.label_media_rank_section_title),
            modifier =
                Modifier
                    .weight(1f)
                    .align(alignment = Alignment.CenterVertically),
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.titleLarge,
        )
    }
}

@Composable
fun MediaRankSection(
    ranks: List<IMediaRank>,
    onClick: (IMediaRank, List<Sorting<MediaSort>>) -> Unit,
    modifier: Modifier = Modifier,
) {
    var showAll by remember { mutableStateOf(false) }
    OutlinedCard(
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
        shape = CardDefaults.outlinedShape,
        modifier = modifier,
    ) {
        Column(modifier = Modifier.padding(all = 16.dp)) {
            MediaRankSectionHeader()
            Spacer(modifier = Modifier.padding(bottom = 8.dp))
            ranks.take(2).forEachIndexed { index, rank ->
                MediaRankItem(
                    rank = ranks[index],
                    onClick = onClick,
                    modifier = Modifier.padding(8.dp),
                )
            }
            if (ranks.size > 2) {
                TextButton(
                    onClick = {
                        showAll = true
                    },
                    content = {
                        Text(
                            text = stringResource(R.string.label_media_rank_section_show_all_rankings),
                            textAlign = TextAlign.Start,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    },
                    modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
                )
            }
        }
    }

    if (showAll) {
        MediaRankSheet(
            ranks = ranks,
            onItemClick = { rank, sorting -> onClick(rank, sorting) },
            onDismiss = { showAll = false },
        )
    }
}

@AniTrendPreview.Default
@Composable
private fun MediaRankSectionPreview(
    @PreviewParameter(DarkThemeProvider::class) darkTheme: Boolean,
) {
    PreviewTheme(darkTheme = darkTheme, wrapInSurface = true) {
        MediaRankSection(
            ranks = MediaRankPreviewProvider().values.toList(),
            onClick = { _, _ -> },
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
        )
    }
}
