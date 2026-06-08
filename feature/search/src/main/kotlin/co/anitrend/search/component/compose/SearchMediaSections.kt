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
package co.anitrend.search.component.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import co.anitrend.common.media.ui.compose.entity.MediaPreferenceData
import co.anitrend.common.media.ui.compose.item.MediaCompactItem
import co.anitrend.common.media.ui.compose.item.MediaPosterListItem
import co.anitrend.domain.media.entity.Media
import co.anitrend.navigation.model.common.IParam
import co.anitrend.search.R

@Composable
internal fun SearchSection(
    title: String,
    items: LazyPagingItems<Media>,
    mediaPreferenceData: MediaPreferenceData,
    onMediaItemClick: (IParam) -> Unit,
    onSeeAllClick: () -> Unit,
) {
    val refreshState = items.loadState.refresh
    Column(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            TextButton(onClick = onSeeAllClick) {
                Text(text = stringResource(R.string.action_search_see_all))
            }
        }

        when {
            items.itemCount > 0 -> {
                val previewItems = remember(items.itemCount) { List(items.itemCount) { index -> items[index] }.filterNotNull().take(8) }
                LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp), contentPadding = PaddingValues(vertical = 4.dp)) {
                    items(items = previewItems, key = { media -> media.id }) { media ->
                        MediaCompactItem(
                            media = media,
                            mediaPreferenceData = mediaPreferenceData,
                            mediaItemClick = onMediaItemClick,
                            modifier = Modifier.width(148.dp).height(262.dp),
                        )
                    }
                }
            }

            refreshState is LoadState.Loading ->
                SearchState(
                    title = stringResource(R.string.label_search_loading_title),
                    subtitle = stringResource(R.string.message_search_loading),
                )

            refreshState is LoadState.Error ->
                SearchState(
                    title = stringResource(R.string.label_search_error_title),
                    subtitle = stringResource(R.string.message_search_error),
                    onRetry = items::retry,
                )

            else ->
                SearchState(
                    title = stringResource(R.string.label_search_empty_title),
                    subtitle = stringResource(R.string.message_search_empty),
                )
        }
    }
}

@Composable
internal fun SearchDrillDown(
    items: LazyPagingItems<Media>,
    mediaPreferenceData: MediaPreferenceData,
    onMediaItemClick: (IParam) -> Unit,
    modifier: Modifier = Modifier,
) {
    val refreshState = items.loadState.refresh
    when {
        items.itemCount > 0 -> {
            LazyColumn(
                modifier = modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 24.dp),
            ) {
                items(
                    count = items.itemCount,
                    key = items.itemKey { media -> media.id },
                    contentType = items.itemContentType { "search_media_row" },
                ) { index ->
                    val media = items[index] ?: return@items
                    MediaPosterListItem(
                        media = media,
                        mediaPreferenceData = mediaPreferenceData,
                        onMediaItemClick = onMediaItemClick,
                    )
                }
            }
        }

        refreshState is LoadState.Loading ->
            SearchState(
                title = stringResource(R.string.label_search_loading_title),
                subtitle = stringResource(R.string.message_search_loading),
            )

        refreshState is LoadState.Error ->
            SearchState(
                title = stringResource(R.string.label_search_error_title),
                subtitle = stringResource(R.string.message_search_error),
                onRetry = items::retry,
            )

        else ->
            SearchState(
                title = stringResource(R.string.label_search_empty_title),
                subtitle = stringResource(R.string.message_search_empty),
            )
    }
}

@Composable
internal fun SectionHeader(
    title: String,
    onSeeAllClick: () -> Unit,
) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        TextButton(onClick = onSeeAllClick) {
            Text(text = stringResource(R.string.action_search_see_all))
        }
    }
}
