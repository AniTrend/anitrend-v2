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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import co.anitrend.android.core.ui.AniTrendPreview
import co.anitrend.android.core.ui.theme.preview.DarkThemeProvider
import co.anitrend.android.core.ui.theme.preview.PreviewTheme
import co.anitrend.common.media.ui.compose.extensions.MediaBrowseLayout
import co.anitrend.common.media.ui.compose.extensions.MediaBrowseListVariant
import co.anitrend.common.media.ui.compose.extensions.genreMetaLine
import co.anitrend.common.media.ui.compose.item.MediaPagedBrowseContent
import co.anitrend.common.media.ui.compose.item.MediaPagedBrowseKeys
import co.anitrend.common.media.ui.compose.preview.mediaPreviewItems
import co.anitrend.common.shared.ui.compose.DefaultScaffold
import co.anitrend.common.media.ui.compose.widget.airing.AiringScheduleText
import co.anitrend.common.media.ui.compose.widget.title.MediaMetaLineText
import co.anitrend.data.settings.customize.ICustomizationSettings
import co.anitrend.data.user.settings.IUserSettings
import co.anitrend.domain.media.entity.Media
import co.anitrend.media.discover.R
import co.anitrend.media.discover.component.content.viewmodel.MediaDiscoverViewModel
import co.anitrend.navigation.MediaDiscoverRouter
import co.anitrend.navigation.model.common.IParam
import kotlinx.coroutines.flow.flowOf

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

    DefaultScaffold(
        onBackPress = onBackPress,
        modifier = modifier,
        showBottomBar = showBottomBar,
        bottomBarActions = {
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
                    MediaPagedBrowseContent(
                        mediaItems = mediaItems,
                        browseLayout = preferredViewMode,
                        scoreFormat = scoreFormat,
                        onMediaItemClick = onMediaItemClick,
                        appendLoadingText = stringResource(R.string.message_media_discover_loading_more),
                        appendErrorTitle = stringResource(R.string.label_media_discover_error_title),
                        keys = MediaPagedBrowseKeys(prefix = "media_discover"),
                    ) { media, variant ->
                        MediaDiscoverSupportingContent(
                            media = media,
                            variant = variant,
                        )
                    }

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
private fun ColumnScope.MediaDiscoverSupportingContent(
    media: Media,
    variant: MediaBrowseListVariant,
) {
    MediaMetaLineText(
        media = media,
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.primary,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )

    AiringScheduleText(
        media = media,
        style = MaterialTheme.typography.bodySmall,
    )

    if (variant == MediaBrowseListVariant.SUMMARY) {
        media.genreMetaLine()?.also { genres ->
            Text(
                text = genres,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
            )
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

@AniTrendPreview.Default
@Composable
private fun MediaDiscoverContentPreview(
    @PreviewParameter(DarkThemeProvider::class) darkTheme: Boolean,
) {
    PreviewTheme(darkTheme = darkTheme, wrapInSurface = true) {
        val mediaItems = flowOf(PagingData.from(mediaPreviewItems.take(3))).collectAsLazyPagingItems()

        MediaPagedBrowseContent(
            mediaItems = mediaItems,
            browseLayout = MediaBrowseLayout.SUMMARY,
            scoreFormat = IUserSettings.DEFAULT_SCORE_FORMAT,
            onMediaItemClick = {},
            appendLoadingText = "Loading more discover results",
            appendErrorTitle = "Could not load more results",
            keys = MediaPagedBrowseKeys(prefix = "media_discover_preview"),
            modifier = Modifier.padding(8.dp).height(540.dp),
        ) { media, variant ->
            MediaDiscoverSupportingContent(
                media = media,
                variant = variant,
            )
        }
    }
}

@AniTrendPreview.Default
@Composable
private fun MediaDiscoverStatePreview(
    @PreviewParameter(DarkThemeProvider::class) darkTheme: Boolean,
) {
    PreviewTheme(darkTheme = darkTheme, wrapInSurface = true) {
        MediaDiscoverState(
            title = "No discover matches",
            subtitle = "Adjust the filters to broaden the result set.",
            modifier = Modifier.padding(24.dp),
        )
    }
}
