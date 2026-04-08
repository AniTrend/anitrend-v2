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
package co.anitrend.media.component.schedule

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import co.anitrend.common.media.ui.compose.widget.airing.AiringScheduleText
import co.anitrend.common.shared.ui.compose.sheet.ListBottomSheet
import co.anitrend.domain.media.entity.Media
import co.anitrend.media.R
import co.anitrend.media.component.viewmodel.MediaScheduleViewModel

@Composable
private fun ScheduleList(
    list: LazyPagingItems<Media>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp),
    itemSpacing: Int = 8,
) {
    LazyColumn(
        contentPadding = contentPadding,
        modifier = modifier,
    ) {
        items(count = list.itemCount) { index ->
            val media = list[index] ?: return@items
            Column(modifier = Modifier.fillMaxWidth().padding(vertical = itemSpacing.dp)) {
                // Title: Episode X • in Y time
                AiringScheduleText(
                    media = media,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            HorizontalDivider()
        }
    }
}

@Composable
fun MediaScheduleSheet(
    mediaId: Long,
    onDismiss: () -> Unit,
    viewModel: MediaScheduleViewModel,
) {
    val schedule = remember(mediaId) { viewModel.schedule(mediaId) }.collectAsLazyPagingItems()
    val refreshState = schedule.loadState.refresh
    val items = schedule.itemSnapshotList.items

    ListBottomSheet(onDismiss = onDismiss) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(R.string.title_media_schedule_sheet),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            )
            when {
                items.isNotEmpty() -> ScheduleList(list = schedule)

                refreshState is LoadState.Loading -> {
                    Text(
                        text = stringResource(R.string.message_media_schedule_loading),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(16.dp),
                    )
                }

                refreshState is LoadState.Error -> {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = stringResource(R.string.label_media_schedule_error_title),
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier.padding(bottom = 8.dp),
                        )
                        OutlinedButton(onClick = schedule::retry) {
                            Text(text = stringResource(co.anitrend.core.R.string.label_text_action_retry))
                        }
                    }
                }

                else -> {
                    Text(
                        text = stringResource(R.string.message_media_schedule_empty),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(16.dp),
                    )
                }
            }
        }
    }
}
