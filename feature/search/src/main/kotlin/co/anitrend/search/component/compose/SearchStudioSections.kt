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

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import co.anitrend.android.core.compose.design.image.AniTrendImage
import co.anitrend.android.core.helpers.image.model.RequestImage
import co.anitrend.domain.common.entity.shared.CoverImage
import co.anitrend.domain.studio.entity.Studio
import co.anitrend.navigation.StudioRouter
import co.anitrend.navigation.model.common.IParam
import co.anitrend.search.R

private data class StudioSearchItemUiModel(
    val id: Long,
    val name: String,
    val isAnimationStudio: Boolean,
    val favourites: Int,
    val siteUrl: String?,
    val image: CoverImage?,
)

private fun Studio.toSearchUiModel() =
    when (this) {
        is Studio.Core -> StudioSearchItemUiModel(id, name, isAnimationStudio, favourites, siteUrl, image)
        is Studio.Extended -> StudioSearchItemUiModel(id, name, isAnimationStudio, favourites, siteUrl, image)
    }

@Composable
internal fun StudioSearchSection(
    title: String,
    items: LazyPagingItems<Studio>,
    onStudioClick: (IParam) -> Unit,
    onSeeAllClick: () -> Unit,
) {
    val refreshState = items.loadState.refresh
    Column(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
        SectionHeader(title = title, onSeeAllClick = onSeeAllClick)

        when {
            items.itemCount > 0 -> {
                val previewItems =
                    remember(items.itemCount) {
                        List(items.itemCount) { index -> items[index]?.toSearchUiModel() }.filterNotNull().take(8)
                    }
                LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp), contentPadding = PaddingValues(vertical = 4.dp)) {
                    items(items = previewItems, key = { it.id }) { studio ->
                        StudioSearchCompactRow(
                            item = studio,
                            onClick = {
                                onStudioClick(StudioRouter.StudioParam(id = studio.id, name = studio.name))
                            },
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
internal fun StudioDrillDown(
    items: LazyPagingItems<Studio>,
    onStudioClick: (IParam) -> Unit,
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
                    key = items.itemKey { it.id },
                    contentType = items.itemContentType { "search_studio_row" },
                ) { index ->
                    val studio = items[index] ?: return@items
                    val uiModel = studio.toSearchUiModel()
                    StudioSearchListRow(
                        item = uiModel,
                        onClick = {
                            onStudioClick(StudioRouter.StudioParam(id = uiModel.id, name = uiModel.name))
                        },
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
private fun StudioSearchCompactRow(
    item: StudioSearchItemUiModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier =
            modifier
                .width(148.dp),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.72f),
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.24f)),
        onClick = onClick,
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AvatarImageBadge(
                image = item.image,
                name = item.name,
                modifier = Modifier.size(42.dp),
            )
            Text(
                text = item.name,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Medium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
            )
            if (item.isAnimationStudio) {
                Text(
                    text = stringResource(R.string.label_search_studio_animation),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Composable
private fun StudioSearchListRow(
    item: StudioSearchItemUiModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier =
            modifier
                .fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.72f),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.24f)),
        onClick = onClick,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            AvatarImageBadge(
                image = item.image,
                name = item.name,
                modifier = Modifier.size(42.dp),
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                if (item.isAnimationStudio) {
                    Text(
                        text = stringResource(R.string.label_search_studio_animation),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                if (item.favourites > 0) {
                    Text(
                        text = stringResource(R.string.label_search_studio_favourites, item.favourites),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}

@Composable
private fun AvatarImageBadge(
    image: CoverImage?,
    name: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f)),
    ) {
        if (image != null) {
            AniTrendImage(
                image = image,
                imageType = RequestImage.Media.ImageType.POSTER,
                contentDescription = name,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}
