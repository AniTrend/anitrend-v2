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
package co.anitrend.media.discover.component.compose

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import co.anitrend.android.core.compose.design.BackIconButton
import co.anitrend.android.core.compose.design.image.AniTrendImage
import co.anitrend.android.core.helpers.image.model.RequestImage
import co.anitrend.common.media.ui.compose.component.MediaRating
import co.anitrend.common.media.ui.compose.entity.MediaPreferenceData
import co.anitrend.common.media.ui.compose.item.MediaCompactItem
import co.anitrend.common.media.ui.compose.widget.airing.AiringScheduleText
import co.anitrend.common.media.ui.compose.widget.title.MediaSubTitleText
import co.anitrend.data.settings.customize.ICustomizationSettings
import co.anitrend.data.settings.customize.common.PreferredViewMode
import co.anitrend.data.user.settings.IUserSettings
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.medialist.enums.ScoreFormat
import co.anitrend.media.discover.R
import co.anitrend.media.discover.component.content.viewmodel.MediaDiscoverViewModel
import co.anitrend.navigation.MediaDiscoverRouter
import co.anitrend.navigation.MediaListEditorRouter
import co.anitrend.navigation.MediaRouter
import co.anitrend.navigation.model.common.IParam

@Composable
fun MediaDiscoverCompose(
    settings: ICustomizationSettings,
    userSettings: IUserSettings,
    onFilterClick: (MediaDiscoverRouter.MediaDiscoverParam) -> Unit,
    onMediaItemClick: (IParam) -> Unit,
    viewModel: MediaDiscoverViewModel,
    modifier: Modifier = Modifier,
    onBackPress: (() -> Unit)? = null,
    onViewModeClick: (() -> Unit)? = null,
    showBottomBar: Boolean = true,
) {
    val params by viewModel.params.collectAsStateWithLifecycle()
    val preferredViewMode by settings.preferredViewMode.flow.collectAsStateWithLifecycle(
        initialValue = settings.preferredViewMode.value,
    )
    val scoreFormat by userSettings.scoreFormat.flow.collectAsStateWithLifecycle(
        initialValue = IUserSettings.DEFAULT_SCORE_FORMAT,
    )
    val mediaItems = viewModel.media.collectAsLazyPagingItems()
    val refreshState = mediaItems.loadState.refresh

    Scaffold(
        modifier = modifier,
        bottomBar = {
            if (showBottomBar) {
                BottomAppBar(
                    actions = {
                        onBackPress?.also { BackIconButton(onBackClick = it) }
                        IconButton(onClick = { onFilterClick(params) }) {
                            Icon(
                                imageVector = Icons.Default.FilterList,
                                contentDescription = stringResource(R.string.action_media_discover_filter),
                            )
                        }
                        onViewModeClick?.also { action ->
                            IconButton(onClick = action) {
                                Icon(
                                    imageVector = Icons.Default.GridView,
                                    contentDescription = stringResource(R.string.action_media_discover_change_view),
                                )
                            }
                        }
                    },
                )
            }
        },
    ) { padding ->
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp, vertical = 16.dp),
        ) {
            when {
                mediaItems.itemCount > 0 ->
                    MediaDiscoverResults(
                        mediaItems = mediaItems,
                        preferredViewMode = preferredViewMode,
                        scoreFormat = scoreFormat,
                        onMediaItemClick = onMediaItemClick,
                    )

                refreshState is LoadState.Loading ->
                    MediaDiscoverState(
                        title = stringResource(R.string.label_media_discover_loading_title),
                        subtitle = stringResource(R.string.message_media_discover_loading),
                    )

                refreshState is LoadState.Error ->
                    MediaDiscoverRetryState(
                        title = stringResource(R.string.label_media_discover_error_title),
                        onRetry = mediaItems::retry,
                    )

                else ->
                    MediaDiscoverState(
                        title = stringResource(R.string.label_media_discover_empty_title),
                        subtitle = stringResource(R.string.message_media_discover_empty),
                    )
            }
        }
    }
}

@Composable
private fun MediaDiscoverResults(
    mediaItems: LazyPagingItems<Media>,
    preferredViewMode: PreferredViewMode,
    scoreFormat: ScoreFormat,
    onMediaItemClick: (IParam) -> Unit,
    modifier: Modifier = Modifier,
) {
    val mediaPreferenceData = remember(scoreFormat) { MediaPreferenceData(scoreFormat = scoreFormat) }

    if (preferredViewMode.isListLayout()) {
        MediaDiscoverList(
            mediaItems = mediaItems,
            preferredViewMode = preferredViewMode,
            mediaPreferenceData = mediaPreferenceData,
            onMediaItemClick = onMediaItemClick,
            modifier = modifier,
        )
    } else {
        MediaDiscoverGrid(
            mediaItems = mediaItems,
            preferredViewMode = preferredViewMode,
            mediaPreferenceData = mediaPreferenceData,
            onMediaItemClick = onMediaItemClick,
            modifier = modifier,
        )
    }
}

@Composable
private fun MediaDiscoverGrid(
    mediaItems: LazyPagingItems<Media>,
    preferredViewMode: PreferredViewMode,
    mediaPreferenceData: MediaPreferenceData,
    onMediaItemClick: (IParam) -> Unit,
    modifier: Modifier = Modifier,
) {
    val columns = preferredViewMode.gridColumns()

    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(count = mediaItems.itemCount) { index ->
            val media = mediaItems[index] ?: return@items

            MediaCompactItem(
                media = media,
                mediaPreferenceData = mediaPreferenceData,
                mediaItemClick = onMediaItemClick,
                modifier =
                    if (columns == 2) {
                        Modifier
                            .fillMaxWidth()
                            .aspectRatio(0.62f)
                    } else {
                        Modifier
                            .fillMaxWidth()
                            .aspectRatio(0.55f)
                    },
            )
        }

        when (mediaItems.loadState.append) {
            is LoadState.Loading -> {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Text(
                        text = stringResource(R.string.message_media_discover_loading_more),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 12.dp),
                    )
                }
            }

            is LoadState.Error -> {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    MediaDiscoverRetryState(
                        title = stringResource(R.string.label_media_discover_error_title),
                        onRetry = mediaItems::retry,
                    )
                }
            }

            else -> Unit
        }
    }
}

@Composable
private fun MediaDiscoverList(
    mediaItems: LazyPagingItems<Media>,
    preferredViewMode: PreferredViewMode,
    mediaPreferenceData: MediaPreferenceData,
    onMediaItemClick: (IParam) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        items(count = mediaItems.itemCount) { index ->
            val media = mediaItems[index] ?: return@items

            when (preferredViewMode) {
                PreferredViewMode.SUMMARY ->
                    MediaDiscoverSummaryItem(
                        media = media,
                        mediaPreferenceData = mediaPreferenceData,
                        onMediaItemClick = onMediaItemClick,
                    )

                else ->
                    MediaDiscoverDetailedItem(
                        media = media,
                        mediaPreferenceData = mediaPreferenceData,
                        onMediaItemClick = onMediaItemClick,
                    )
            }
        }

        when (mediaItems.loadState.append) {
            is LoadState.Loading -> {
                item {
                    Text(
                        text = stringResource(R.string.message_media_discover_loading_more),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 12.dp),
                    )
                }
            }

            is LoadState.Error -> {
                item {
                    MediaDiscoverRetryState(
                        title = stringResource(R.string.label_media_discover_error_title),
                        onRetry = mediaItems::retry,
                    )
                }
            }

            else -> Unit
        }
    }
}

@Composable
private fun MediaDiscoverDetailedItem(
    media: Media,
    mediaPreferenceData: MediaPreferenceData,
    onMediaItemClick: (IParam) -> Unit,
    modifier: Modifier = Modifier,
) {
    MediaDiscoverListItem(
        media = media,
        mediaPreferenceData = mediaPreferenceData,
        onMediaItemClick = onMediaItemClick,
        modifier = modifier,
        showGenres = false,
    )
}

@Composable
private fun MediaDiscoverSummaryItem(
    media: Media,
    mediaPreferenceData: MediaPreferenceData,
    onMediaItemClick: (IParam) -> Unit,
    modifier: Modifier = Modifier,
) {
    MediaDiscoverListItem(
        media = media,
        mediaPreferenceData = mediaPreferenceData,
        onMediaItemClick = onMediaItemClick,
        modifier = modifier,
        showGenres = true,
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MediaDiscoverListItem(
    media: Media,
    mediaPreferenceData: MediaPreferenceData,
    onMediaItemClick: (IParam) -> Unit,
    modifier: Modifier = Modifier,
    showGenres: Boolean,
) {
    Surface(
        modifier =
            modifier
                .fillMaxWidth()
                .combinedClickable(
                    onClick = {
                        onMediaItemClick(
                            MediaRouter.MediaParam(
                                id = media.id,
                                type = media.category.type,
                            ),
                        )
                    },
                    onLongClick = {
                        onMediaItemClick(
                            MediaListEditorRouter.MediaListEditorParam(
                                mediaId = media.id,
                                mediaType = media.category.type,
                                scoreFormat = mediaPreferenceData.scoreFormat,
                            ),
                        )
                    },
                ),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.18f),
        contentColor = MaterialTheme.colorScheme.onSurface,
        shape = androidx.compose.foundation.shape.RoundedCornerShape(24.dp),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.Top,
        ) {
            Box(
                modifier =
                    Modifier
                        .width(112.dp)
                        .height(160.dp),
            ) {
                AniTrendImage(
                    image = media.image,
                    imageType = RequestImage.Media.ImageType.POSTER,
                    modifier = Modifier.fillMaxSize().clip(androidx.compose.foundation.shape.RoundedCornerShape(18.dp)),
                    onClick = {},
                )
                MediaRating(
                    media = media,
                    scoreFormat = mediaPreferenceData.scoreFormat,
                    modifier = Modifier.padding(8.dp),
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = media.displayTitle().orEmpty(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )

                media.secondaryTitle()?.also { secondaryTitle ->
                    Text(
                        text = secondaryTitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }

                media.metaLine()?.also { metadata ->
                    Text(
                        text = metadata,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }

                MediaSubTitleText(
                    media = media,
                    style = MaterialTheme.typography.bodyMedium,
                )

                AiringScheduleText(
                    media = media,
                    style = MaterialTheme.typography.bodySmall,
                )

                if (showGenres && media.genres.isNotEmpty()) {
                    Text(
                        text = media.genres.joinToString(separator = " • "),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
    }
}

@Composable
private fun MediaDiscoverState(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun MediaDiscoverRetryState(
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

private fun PreferredViewMode.isListLayout(): Boolean =
    this == PreferredViewMode.DETAILED || this == PreferredViewMode.SUMMARY

private fun PreferredViewMode.gridColumns(): Int =
    when (this) {
        PreferredViewMode.COMFORTABLE -> 2
        PreferredViewMode.COMPACT -> 3
        PreferredViewMode.DETAILED,
        PreferredViewMode.SUMMARY,
        -> 1
    }

private fun Media.displayTitle(): String? =
    title.userPreferred
        ?.toString()
        ?.trim()
        ?.takeIf(String::isNotBlank)
        ?: listOf(title.english, title.romaji, title.native)
            .mapNotNull { it?.toString()?.trim()?.takeIf(String::isNotBlank) }
            .firstOrNull()

private fun Media.secondaryTitle(): String? {
    val preferred = title.userPreferred?.toString()?.trim()

    return listOf(title.english, title.romaji, title.native)
        .mapNotNull { it?.toString()?.trim()?.takeIf(String::isNotBlank) }
        .firstOrNull { it != preferred }
}

private fun Media.metaLine(): String? =
    buildList {
        format?.alias?.toString()?.takeIf(String::isNotBlank)?.let(::add)
        season?.alias?.toString()?.takeIf(String::isNotBlank)?.let(::add)
        startDate?.year?.takeIf { it > 0 }?.toString()?.let(::add)
    }.takeIf(List<String>::isNotEmpty)?.joinToString(separator = " • ")
