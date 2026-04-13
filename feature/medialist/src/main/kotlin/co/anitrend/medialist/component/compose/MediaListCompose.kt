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
package co.anitrend.medialist.component.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
import co.anitrend.common.media.ui.compose.widget.airing.AiringScheduleText
import co.anitrend.common.media.ui.compose.widget.title.MediaMetaLineText
import co.anitrend.common.shared.ui.compose.DefaultBottomAppBar
import co.anitrend.data.settings.customize.ICustomizationSettings
import co.anitrend.data.user.settings.IUserSettings
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.media.enums.MediaType
import co.anitrend.domain.medialist.enums.MediaListStatus
import co.anitrend.domain.medialist.enums.ScoreFormat
import co.anitrend.domain.user.entity.attribute.MediaListInfo
import co.anitrend.medialist.R
import co.anitrend.medialist.component.container.viewmodel.UserViewModel
import co.anitrend.medialist.component.content.viewmodel.MediaListViewModel
import co.anitrend.navigation.MediaListRouter
import co.anitrend.navigation.model.common.IParam
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import androidx.paging.LoadState as PagingLoadState

@Composable
fun MediaListCompose(
    settings: ICustomizationSettings,
    userSettings: IUserSettings,
    userViewModel: UserViewModel,
    mediaViewModel: MediaListViewModel,
    onMediaItemClick: (IParam) -> Unit,
    modifier: Modifier = Modifier,
    onBackPress: (() -> Unit)? = null,
    showBottomBar: Boolean = true,
) {
    val sections by userViewModel.sectionListInfo.observeAsState(emptyList())
    val currentParam by mediaViewModel.params.collectAsStateWithLifecycle()
    val preferredViewMode by settings.preferredViewMode.flow.collectAsStateWithLifecycle(
        initialValue = settings.preferredViewMode.value,
    )
    val scoreFormat by userSettings.scoreFormat.flow.collectAsStateWithLifecycle(
        initialValue = IUserSettings.DEFAULT_SCORE_FORMAT,
    )
    val resolvedSections = remember(sections, currentParam.type) { sections.ifEmpty { fallbackSections(currentParam.type) } }
    val sectionItems =
        remember(resolvedSections) {
            resolvedSections.filter { !it.isCustomList } + resolvedSections.filter(MediaListInfo::isCustomList)
        }
    val selectedSection = remember(sectionItems, currentParam) { sectionItems.selectedSectionFor(currentParam) }

    Scaffold(
        modifier = modifier,
        bottomBar = {
            if (showBottomBar && onBackPress != null) {
                DefaultBottomAppBar(onBackPress = onBackPress)
            }
        },
    ) { padding ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            if (sectionItems.isNotEmpty()) {
                MediaListSectionRow(
                    sections = sectionItems,
                    selectedSection = selectedSection,
                    onSectionClick = mediaViewModel::selectSection,
                )
            }

            key(currentParam.sectionKey() ?: selectedSection.sectionKey()) {
                MediaListPagingContent(
                    mediaFlow = mediaViewModel.media,
                    browseLayout = preferredViewMode,
                    scoreFormat = scoreFormat,
                    selectedSection = selectedSection,
                    onMediaItemClick = onMediaItemClick,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
private fun MediaListPagingContent(
    mediaFlow: Flow<PagingData<Media>>,
    browseLayout: MediaBrowseLayout,
    scoreFormat: ScoreFormat,
    selectedSection: MediaListInfo,
    onMediaItemClick: (IParam) -> Unit,
    modifier: Modifier = Modifier,
) {
    val mediaItems = mediaFlow.collectAsLazyPagingItems()
    val refreshState = mediaItems.loadState.refresh

    Box(modifier = modifier) {
        when {
            refreshState is PagingLoadState.Loading ->
                MediaListState(
                    title = stringResource(R.string.label_media_list_loading_title),
                    subtitle = stringResource(R.string.message_media_list_loading, selectedSection.displayLabel()),
                )

            refreshState is PagingLoadState.Error ->
                MediaListRetryState(
                    title = stringResource(R.string.label_media_list_error_title),
                    onRetry = mediaItems::retry,
                )

            mediaItems.itemCount > 0 ->
                MediaPagedBrowseContent(
                    mediaItems = mediaItems,
                    browseLayout = browseLayout,
                    scoreFormat = scoreFormat,
                    onMediaItemClick = onMediaItemClick,
                    appendLoadingText = stringResource(R.string.message_media_list_loading_more),
                    appendErrorTitle = stringResource(R.string.label_media_list_error_title),
                    keys = MediaPagedBrowseKeys(prefix = "media_list"),
                ) { media, variant ->
                    MediaListSupportingContent(
                        media = media,
                        variant = variant,
                    )
                }

            else ->
                MediaListState(
                    title = stringResource(R.string.label_media_list_empty_title),
                    subtitle = stringResource(R.string.message_media_list_empty, selectedSection.displayLabel()),
                )
        }
    }
}

@Composable
private fun MediaListSectionRow(
    sections: List<MediaListInfo>,
    selectedSection: MediaListInfo,
    onSectionClick: (MediaListInfo) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(end = 4.dp),
    ) {
        items(
            count = sections.size,
            key = { index -> sections[index].sectionKey() },
            contentType = { "media_list_section_chip" },
        ) { index ->
            val section = sections[index]
            val isSelected = section.sectionKey() == selectedSection.sectionKey()

            FilterChip(
                selected = isSelected,
                onClick = { onSectionClick(section) },
                label = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Text(text = section.displayLabel())
                        Surface(
                            shape = CircleShape,
                            color =
                                if (isSelected) {
                                    MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.18f)
                                } else {
                                    MaterialTheme.colorScheme.surfaceVariant
                                },
                        ) {
                            Text(
                                text = section.count.toString(),
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            )
                        }
                    }
                },
            )
        }
    }
}

@Composable
private fun ColumnScope.MediaListSupportingContent(
    media: Media,
    variant: MediaBrowseListVariant,
) {
    MediaMetaLineText(
        media = media,
        style = MaterialTheme.typography.labelMedium,
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
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun MediaListState(
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
private fun MediaListRetryState(
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

private fun fallbackSections(type: MediaType): List<MediaListInfo> =
    MediaListStatus.entries.map { status ->
        MediaListInfo(
            isCustomList = false,
            mediaType = type,
            name = status.name,
            count = 0,
        )
    }

private fun List<MediaListInfo>.selectedSectionFor(param: MediaListRouter.MediaListParam): MediaListInfo =
    firstOrNull { it.sectionKey() == param.sectionKey() }
        ?: firstOrNull { !it.isCustomList && it.name == MediaListStatus.CURRENT.name }
        ?: first()

private fun MediaListInfo.sectionKey(): String =
    if (isCustomList) {
        "custom:$name"
    } else {
        "status:$name"
    }

private fun MediaListRouter.MediaListParam.sectionKey(): String? =
    when (val selectedStatus = status) {
        null ->
            if (customListName != null) {
                "custom:$customListName"
            } else {
                null
            }

        else -> "status:${selectedStatus.name}"
    }

private fun MediaListInfo.displayLabel(): String =
    if (isCustomList) {
        name
    } else {
        MediaListStatus.valueOf(name).alias.toString()
    }

private fun Media.statusMetaLine(): String? =
    buildList {
        format
            ?.alias
            ?.toString()
            ?.takeIf(String::isNotBlank)
            ?.let(::add)
        status?.let { add(it.name.lowercase().replaceFirstChar(Char::uppercaseChar)) }
        startDate
            .year
            .takeIf { it > 0 }
            ?.toString()
            ?.let(::add)
    }.takeIf(List<String>::isNotEmpty)?.joinToString(separator = " • ")

private val mediaListPreviewSections =
    listOf(
        MediaListInfo(
            isCustomList = false,
            mediaType = MediaType.ANIME,
            name = MediaListStatus.CURRENT.name,
            count = 12,
        ),
        MediaListInfo(
            isCustomList = false,
            mediaType = MediaType.ANIME,
            name = MediaListStatus.PLANNING.name,
            count = 8,
        ),
        MediaListInfo(
            isCustomList = true,
            mediaType = MediaType.ANIME,
            name = "Weekend binge",
            count = 3,
        ),
    )

@AniTrendPreview.Default
@Composable
private fun MediaListContentPreview(
    @PreviewParameter(DarkThemeProvider::class) darkTheme: Boolean,
) {
    PreviewTheme(darkTheme = darkTheme, wrapInSurface = true) {
        Column(
            modifier =
                Modifier
                    .padding(8.dp)
                    .height(620.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            MediaListSectionRow(
                sections = mediaListPreviewSections,
                selectedSection = mediaListPreviewSections.first(),
                onSectionClick = {},
            )
            MediaListPagingContent(
                mediaFlow = flowOf(PagingData.from(mediaPreviewItems)),
                browseLayout = MediaBrowseLayout.SUMMARY,
                scoreFormat = ScoreFormat.POINT_10_DECIMAL,
                selectedSection = mediaListPreviewSections.first(),
                onMediaItemClick = {},
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@AniTrendPreview.Default
@Composable
private fun MediaListStatePreview(
    @PreviewParameter(DarkThemeProvider::class) darkTheme: Boolean,
) {
    PreviewTheme(darkTheme = darkTheme, wrapInSurface = true) {
        MediaListState(
            title = "Your list is empty",
            subtitle = "Add something to Completed or Planning to populate this section.",
            modifier = Modifier.padding(24.dp),
        )
    }
}
