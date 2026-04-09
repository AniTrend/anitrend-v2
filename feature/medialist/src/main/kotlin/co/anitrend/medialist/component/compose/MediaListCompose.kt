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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import androidx.paging.LoadState as PagingLoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import co.anitrend.android.core.compose.design.image.AniTrendImage
import co.anitrend.android.core.helpers.image.model.RequestImage
import co.anitrend.common.media.ui.compose.component.MediaRating
import co.anitrend.common.media.ui.compose.entity.MediaPreferenceData
import co.anitrend.common.media.ui.compose.item.MediaCompactItem
import co.anitrend.common.media.ui.compose.widget.airing.AiringScheduleText
import co.anitrend.common.media.ui.compose.widget.title.MediaMetaLineText
import co.anitrend.data.settings.customize.ICustomizationSettings
import co.anitrend.data.settings.customize.common.PreferredViewMode
import co.anitrend.data.user.settings.IUserSettings
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.medialist.enums.MediaListStatus
import co.anitrend.domain.medialist.enums.ScoreFormat
import co.anitrend.domain.user.entity.attribute.MediaListInfo
import co.anitrend.medialist.R
import co.anitrend.medialist.component.container.viewmodel.UserViewModel
import co.anitrend.medialist.component.content.viewmodel.MediaListViewModel
import co.anitrend.navigation.MediaListEditorRouter
import co.anitrend.navigation.MediaRouter
import co.anitrend.navigation.model.common.IParam
import kotlinx.coroutines.flow.Flow

@Composable
fun MediaListCompose(
    settings: ICustomizationSettings,
    userSettings: IUserSettings,
    userViewModel: UserViewModel,
    mediaViewModel: MediaListViewModel,
    onMediaItemClick: (IParam) -> Unit,
    modifier: Modifier = Modifier,
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
    val sectionItems = remember(resolvedSections) {
        resolvedSections.filter { !it.isCustomList } + resolvedSections.filter(MediaListInfo::isCustomList)
    }
    val selectedSection = remember(sectionItems, currentParam) { sectionItems.selectedSectionFor(currentParam) }

    Column(
        modifier = modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 16.dp),
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
                preferredViewMode = preferredViewMode,
                scoreFormat = scoreFormat,
                selectedSection = selectedSection,
                onMediaItemClick = onMediaItemClick,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun MediaListPagingContent(
    mediaFlow: Flow<PagingData<Media>>,
    preferredViewMode: PreferredViewMode,
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
                MediaListResults(
                    mediaItems = mediaItems,
                    preferredViewMode = preferredViewMode,
                    scoreFormat = scoreFormat,
                    onMediaItemClick = onMediaItemClick,
                )

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
private fun MediaListResults(
    mediaItems: LazyPagingItems<Media>,
    preferredViewMode: PreferredViewMode,
    scoreFormat: ScoreFormat,
    onMediaItemClick: (IParam) -> Unit,
    modifier: Modifier = Modifier,
) {
    val mediaPreferenceData = remember(scoreFormat) { MediaPreferenceData(scoreFormat = scoreFormat) }

    if (preferredViewMode.isListLayout()) {
        MediaListList(
            mediaItems = mediaItems,
            mediaPreferenceData = mediaPreferenceData,
            preferredViewMode = preferredViewMode,
            onMediaItemClick = onMediaItemClick,
            modifier = modifier,
        )
    } else {
        MediaListGrid(
            mediaItems = mediaItems,
            mediaPreferenceData = mediaPreferenceData,
            preferredViewMode = preferredViewMode,
            onMediaItemClick = onMediaItemClick,
            modifier = modifier,
        )
    }
}

@Composable
private fun MediaListGrid(
    mediaItems: LazyPagingItems<Media>,
    mediaPreferenceData: MediaPreferenceData,
    preferredViewMode: PreferredViewMode,
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
        items(
            count = mediaItems.itemCount,
            key = mediaItems.itemKey { media -> media.id },
            contentType = mediaItems.itemContentType { "media_list_grid_item" },
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
            is PagingLoadState.Loading -> {
                item(
                    key = "media_list_grid_append_loading",
                    span = { GridItemSpan(maxLineSpan) },
                    contentType = "media_list_grid_append_loading",
                ) {
                    Text(
                        text = stringResource(R.string.message_media_list_loading_more),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 12.dp),
                    )
                }
            }

            is PagingLoadState.Error -> {
                item(
                    key = "media_list_grid_append_error",
                    span = { GridItemSpan(maxLineSpan) },
                    contentType = "media_list_grid_append_error",
                ) {
                    MediaListRetryState(
                        title = stringResource(R.string.label_media_list_error_title),
                        onRetry = mediaItems::retry,
                    )
                }
            }

            else -> Unit
        }
    }
}

@Composable
private fun MediaListList(
    mediaItems: LazyPagingItems<Media>,
    mediaPreferenceData: MediaPreferenceData,
    preferredViewMode: PreferredViewMode,
    onMediaItemClick: (IParam) -> Unit,
    modifier: Modifier = Modifier,
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
                    when (preferredViewMode) {
                        PreferredViewMode.SUMMARY -> "media_list_summary_item"
                        else -> "media_list_detailed_item"
                    }
                },
        ) { index ->
            val media = mediaItems[index] ?: return@items

            when (preferredViewMode) {
                PreferredViewMode.SUMMARY ->
                    MediaListSummaryItem(
                        media = media,
                        mediaPreferenceData = mediaPreferenceData,
                        onMediaItemClick = onMediaItemClick,
                    )

                else ->
                    MediaListDetailedItem(
                        media = media,
                        mediaPreferenceData = mediaPreferenceData,
                        onMediaItemClick = onMediaItemClick,
                    )
            }
        }

        when (mediaItems.loadState.append) {
            is PagingLoadState.Loading -> {
                item(
                    key = "media_list_list_append_loading",
                    contentType = "media_list_list_append_loading",
                ) {
                    Text(
                        text = stringResource(R.string.message_media_list_loading_more),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 12.dp),
                    )
                }
            }

            is PagingLoadState.Error -> {
                item(
                    key = "media_list_list_append_error",
                    contentType = "media_list_list_append_error",
                ) {
                    MediaListRetryState(
                        title = stringResource(R.string.label_media_list_error_title),
                        onRetry = mediaItems::retry,
                    )
                }
            }

            else -> Unit
        }
    }
}

@Composable
private fun MediaListDetailedItem(
    media: Media,
    mediaPreferenceData: MediaPreferenceData,
    onMediaItemClick: (IParam) -> Unit,
    modifier: Modifier = Modifier,
) {
    MediaListListItem(
        media = media,
        mediaPreferenceData = mediaPreferenceData,
        onMediaItemClick = onMediaItemClick,
        modifier = modifier,
        showGenres = false,
    )
}

@Composable
private fun MediaListSummaryItem(
    media: Media,
    mediaPreferenceData: MediaPreferenceData,
    onMediaItemClick: (IParam) -> Unit,
    modifier: Modifier = Modifier,
) {
    MediaListListItem(
        media = media,
        mediaPreferenceData = mediaPreferenceData,
        onMediaItemClick = onMediaItemClick,
        modifier = modifier,
        showGenres = true,
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MediaListListItem(
    media: Media,
    mediaPreferenceData: MediaPreferenceData,
    onMediaItemClick: (IParam) -> Unit,
    modifier: Modifier = Modifier,
    showGenres: Boolean,
) {
    Surface(
        modifier =
            modifier.fillMaxWidth().combinedClickable(
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
        shape = RoundedCornerShape(24.dp),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.Top,
        ) {
            Box(
                modifier = Modifier.width(112.dp).height(160.dp),
            ) {
                AniTrendImage(
                    image = media.image,
                    imageType = RequestImage.Media.ImageType.POSTER,
                    modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(18.dp)),
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

                MediaMetaLineText(
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

private fun fallbackSections(type: co.anitrend.domain.media.enums.MediaType): List<MediaListInfo> =
    MediaListStatus.entries.map { status ->
        MediaListInfo(
            isCustomList = false,
            mediaType = type,
            name = status.name,
            count = 0,
        )
    }

private fun List<MediaListInfo>.selectedSectionFor(param: co.anitrend.navigation.MediaListRouter.MediaListParam): MediaListInfo =
    firstOrNull { it.sectionKey() == param.sectionKey() }
        ?: firstOrNull { !it.isCustomList && it.name == MediaListStatus.CURRENT.name }
        ?: first()

private fun MediaListInfo.sectionKey(): String =
    if (isCustomList) {
        "custom:$name"
    } else {
        "status:$name"
    }

private fun co.anitrend.navigation.MediaListRouter.MediaListParam.sectionKey(): String? =
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
        status?.let { add(it.name.lowercase().replaceFirstChar(Char::uppercaseChar)) }
        startDate?.year?.takeIf { it > 0 }?.toString()?.let(::add)
    }.takeIf(List<String>::isNotEmpty)?.joinToString(separator = " • ")
