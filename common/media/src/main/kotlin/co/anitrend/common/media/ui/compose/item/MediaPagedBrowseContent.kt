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
package co.anitrend.common.media.ui.compose.item

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import co.anitrend.android.core.ui.AniTrendPreview
import co.anitrend.android.core.ui.theme.preview.DarkThemeProvider
import co.anitrend.android.core.ui.theme.preview.PreviewTheme
import co.anitrend.common.media.ui.compose.extensions.MediaBrowseLayout
import co.anitrend.common.media.ui.compose.entity.MediaPreferenceData
import co.anitrend.common.media.ui.compose.extensions.MediaBrowseListVariant
import co.anitrend.common.media.ui.compose.extensions.genreMetaLine
import co.anitrend.common.media.ui.compose.extensions.gridColumns
import co.anitrend.common.media.ui.compose.extensions.listVariantOrNull
import co.anitrend.common.media.ui.compose.preview.mediaPreviewItems
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.medialist.enums.ScoreFormat
import co.anitrend.navigation.model.common.IParam
import kotlinx.coroutines.flow.flowOf

data class MediaPagedBrowseKeys(
    val prefix: String,
) {
    val gridItemContentType: String
        get() = "${prefix}_grid_item"
    val summaryItemContentType: String
        get() = "${prefix}_summary_item"
    val detailedItemContentType: String
        get() = "${prefix}_detailed_item"
    val gridAppendLoadingKey: String
        get() = "${prefix}_grid_append_loading"
    val gridAppendErrorKey: String
        get() = "${prefix}_grid_append_error"
    val listAppendLoadingKey: String
        get() = "${prefix}_list_append_loading"
    val listAppendErrorKey: String
        get() = "${prefix}_list_append_error"
}

@Composable
fun MediaPagedBrowseContent(
    mediaItems: LazyPagingItems<Media>,
    browseLayout: MediaBrowseLayout,
    scoreFormat: ScoreFormat,
    onMediaItemClick: (IParam) -> Unit,
    appendLoadingText: String,
    appendErrorTitle: String,
    keys: MediaPagedBrowseKeys,
    modifier: Modifier = Modifier,
    listSupportingContent: @Composable ColumnScope.(media: Media, variant: MediaBrowseListVariant) -> Unit,
) {
    val mediaPreferenceData = remember(scoreFormat) { MediaPreferenceData(scoreFormat = scoreFormat) }

    when (val listVariant = browseLayout.listVariantOrNull()) {
        null ->
            MediaPagedBrowseGrid(
                mediaItems = mediaItems,
                browseLayout = browseLayout,
                mediaPreferenceData = mediaPreferenceData,
                onMediaItemClick = onMediaItemClick,
                appendLoadingText = appendLoadingText,
                appendErrorTitle = appendErrorTitle,
                keys = keys,
                modifier = modifier,
            )

        else ->
            MediaPagedBrowseList(
                mediaItems = mediaItems,
                mediaPreferenceData = mediaPreferenceData,
                listVariant = listVariant,
                onMediaItemClick = onMediaItemClick,
                appendLoadingText = appendLoadingText,
                appendErrorTitle = appendErrorTitle,
                keys = keys,
                modifier = modifier,
                listSupportingContent = listSupportingContent,
            )
    }
}

@Composable
private fun MediaPagedBrowseGrid(
    mediaItems: LazyPagingItems<Media>,
    browseLayout: MediaBrowseLayout,
    mediaPreferenceData: MediaPreferenceData,
    onMediaItemClick: (IParam) -> Unit,
    appendLoadingText: String,
    appendErrorTitle: String,
    keys: MediaPagedBrowseKeys,
    modifier: Modifier = Modifier,
) {
    val columns = browseLayout.gridColumns()

    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(
            count = mediaItems.itemCount,
            key = mediaItems.itemKey { media -> media.id },
            contentType = mediaItems.itemContentType { keys.gridItemContentType },
        ) { index ->
            val media = mediaItems[index] ?: return@items

            MediaCompactItem(
                media = media,
                mediaPreferenceData = mediaPreferenceData,
                mediaItemClick = onMediaItemClick,
                modifier =
                    if (columns == 2) {
                        Modifier.fillMaxWidth().aspectRatio(0.62f)
                    } else {
                        Modifier.fillMaxWidth().aspectRatio(0.55f)
                    },
            )
        }

        when (mediaItems.loadState.append) {
            is LoadState.Loading -> {
                item(
                    key = keys.gridAppendLoadingKey,
                    span = { GridItemSpan(maxLineSpan) },
                    contentType = keys.gridAppendLoadingKey,
                ) {
                    MediaPagedAppendLoadingText(text = appendLoadingText)
                }
            }

            is LoadState.Error -> {
                item(
                    key = keys.gridAppendErrorKey,
                    span = { GridItemSpan(maxLineSpan) },
                    contentType = keys.gridAppendErrorKey,
                ) {
                    MediaPagedAppendRetryState(
                        title = appendErrorTitle,
                        onRetry = mediaItems::retry,
                    )
                }
            }

            else -> Unit
        }
    }
}

@Composable
private fun MediaPagedBrowseList(
    mediaItems: LazyPagingItems<Media>,
    mediaPreferenceData: MediaPreferenceData,
    listVariant: MediaBrowseListVariant,
    onMediaItemClick: (IParam) -> Unit,
    appendLoadingText: String,
    appendErrorTitle: String,
    keys: MediaPagedBrowseKeys,
    modifier: Modifier = Modifier,
    listSupportingContent: @Composable ColumnScope.(media: Media, variant: MediaBrowseListVariant) -> Unit,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        items(
            count = mediaItems.itemCount,
            key = mediaItems.itemKey { media -> media.id },
            contentType =
                mediaItems.itemContentType {
                    when (listVariant) {
                        MediaBrowseListVariant.SUMMARY -> keys.summaryItemContentType
                        MediaBrowseListVariant.DETAILED -> keys.detailedItemContentType
                    }
                },
        ) { index ->
            val media = mediaItems[index] ?: return@items

            MediaPosterListItem(
                media = media,
                mediaPreferenceData = mediaPreferenceData,
                onMediaItemClick = onMediaItemClick,
            ) {
                listSupportingContent(media, listVariant)
            }
        }

        when (mediaItems.loadState.append) {
            is LoadState.Loading -> {
                item(
                    key = keys.listAppendLoadingKey,
                    contentType = keys.listAppendLoadingKey,
                ) {
                    MediaPagedAppendLoadingText(text = appendLoadingText)
                }
            }

            is LoadState.Error -> {
                item(
                    key = keys.listAppendErrorKey,
                    contentType = keys.listAppendErrorKey,
                ) {
                    MediaPagedAppendRetryState(
                        title = appendErrorTitle,
                        onRetry = mediaItems::retry,
                    )
                }
            }

            else -> Unit
        }
    }
}

@Composable
private fun MediaPagedAppendLoadingText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(vertical = 12.dp),
    )
}

@Composable
private fun MediaPagedAppendRetryState(
    title: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
            OutlinedButton(onClick = onRetry) {
                Text(text = stringResource(co.anitrend.core.R.string.label_text_action_retry))
            }
        }
    }
}

@Composable
private fun MediaPagedBrowseGridPreviewContent(
    browseLayout: MediaBrowseLayout,
    darkTheme: Boolean,
) {
    PreviewTheme(darkTheme = darkTheme, wrapInSurface = true) {
        val mediaItems = flowOf(PagingData.from(mediaPreviewItems)).collectAsLazyPagingItems()

        MediaPagedBrowseContent(
            mediaItems = mediaItems,
            browseLayout = browseLayout,
            scoreFormat = ScoreFormat.POINT_10_DECIMAL,
            onMediaItemClick = {},
            appendLoadingText = "Loading more titles",
            appendErrorTitle = "Could not load more titles for ${browseLayout.name.lowercase()}",
            keys = MediaPagedBrowseKeys(prefix = "preview_${browseLayout.name.lowercase()}"),
            modifier = Modifier.padding(8.dp).height(540.dp),
            listSupportingContent = { _, _ -> },
        )
    }
}

@Composable
private fun MediaPagedBrowseListPreviewContent(
    browseLayout: MediaBrowseLayout,
    darkTheme: Boolean,
) {
    PreviewTheme(darkTheme = darkTheme, wrapInSurface = true) {
        val mediaItems = flowOf(PagingData.from(mediaPreviewItems.take(3))).collectAsLazyPagingItems()

        MediaPagedBrowseContent(
            mediaItems = mediaItems,
            browseLayout = browseLayout,
            scoreFormat = ScoreFormat.POINT_10_DECIMAL,
            onMediaItemClick = {},
            appendLoadingText = "Loading more titles",
            appendErrorTitle = "Could not load more titles for ${browseLayout.name.lowercase()}",
            keys = MediaPagedBrowseKeys(prefix = "preview_${browseLayout.name.lowercase()}"),
            modifier = Modifier.padding(8.dp).height(540.dp),
        ) { media, _ ->
            media.genreMetaLine()?.also { genres ->
                Text(
                    text = genres,
                    style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                )
            }
        }
    }
}

@AniTrendPreview.Default
@Composable
private fun MediaPagedBrowseComfortablePreview(
    @PreviewParameter(DarkThemeProvider::class) darkTheme: Boolean,
) {
    MediaPagedBrowseGridPreviewContent(
        browseLayout = MediaBrowseLayout.COMFORTABLE,
        darkTheme = darkTheme,
    )
}

@AniTrendPreview.Default
@Composable
private fun MediaPagedBrowseCompactPreview(
    @PreviewParameter(DarkThemeProvider::class) darkTheme: Boolean,
) {
    MediaPagedBrowseGridPreviewContent(
        browseLayout = MediaBrowseLayout.COMPACT,
        darkTheme = darkTheme,
    )
}

@AniTrendPreview.Default
@Composable
private fun MediaPagedBrowseSummaryPreview(
    @PreviewParameter(DarkThemeProvider::class) darkTheme: Boolean,
) {
    MediaPagedBrowseListPreviewContent(
        browseLayout = MediaBrowseLayout.SUMMARY,
        darkTheme = darkTheme,
    )
}

@AniTrendPreview.Default
@Composable
private fun MediaPagedBrowseDetailedPreview(
    @PreviewParameter(DarkThemeProvider::class) darkTheme: Boolean,
) {
    MediaPagedBrowseListPreviewContent(
        browseLayout = MediaBrowseLayout.DETAILED,
        darkTheme = darkTheme,
    )
}
