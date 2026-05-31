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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import co.anitrend.android.core.compose.design.image.AniTrendImage
import co.anitrend.android.core.helpers.image.model.RequestImage
import co.anitrend.android.core.ui.AniTrendPreview
import co.anitrend.android.core.ui.theme.preview.DarkThemeProvider
import co.anitrend.android.core.ui.theme.preview.PreviewTheme
import co.anitrend.common.media.ui.compose.entity.MediaPreferenceData
import co.anitrend.common.media.ui.compose.item.MediaCompactItem
import co.anitrend.common.media.ui.compose.item.MediaPosterListItem
import co.anitrend.common.shared.ui.compose.DefaultScaffold
import co.anitrend.data.user.settings.IUserSettings
import co.anitrend.domain.character.entity.Character as DomainCharacter
import co.anitrend.domain.common.entity.shared.CoverImage
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.staff.entity.Staff
import co.anitrend.domain.studio.entity.Studio
import co.anitrend.domain.user.entity.User
import co.anitrend.navigation.CharacterRouter
import co.anitrend.navigation.ProfileRouter
import co.anitrend.navigation.StaffRouter
import co.anitrend.navigation.StudioRouter
import co.anitrend.navigation.model.common.IParam
import co.anitrend.search.R
import co.anitrend.search.component.viewmodel.SearchScope
import co.anitrend.search.component.viewmodel.SearchViewModel
import co.anitrend.search.component.viewmodel.UserPreviewState

@Composable
private fun SearchBarContent(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
        keyboardActions =
            KeyboardActions(
                onSearch = {
                    keyboardController?.hide()
                    onSearch(query)
                },
            ),
        leadingIcon = {
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = null,
            )
        },
        trailingIcon = {
            IconButton(
                onClick = {
                    keyboardController?.hide()
                    onSearch(query)
                },
            ) {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = stringResource(R.string.search_place_holder_text),
                )
            }
        },
        placeholder = {
            Text(text = stringResource(co.anitrend.search.R.string.search_place_holder_text))
        },
        modifier = modifier,
    )
}

@Composable
private fun SearchState(
    title: String,
    subtitle: String,
    onRetry: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            onRetry?.also {
                OutlinedButton(onClick = it, modifier = Modifier.padding(top = 8.dp)) {
                    Text(text = stringResource(co.anitrend.core.R.string.label_text_action_retry))
                }
            }
        }
    }
}

@Composable
private fun SearchScopeChips(
    scope: SearchScope,
    onScopeClick: (SearchScope) -> Unit,
) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
        SearchScope.entries.forEach { option ->
            FilterChip(
                selected = scope == option,
                onClick = { onScopeClick(option) },
                label = { Text(text = option.label()) },
            )
        }
    }
}

// ── Media sections (existing) ─────────────────────────────────────────────────────

@Composable
private fun SearchSection(
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
private fun SearchDrillDown(
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

// ── Section header helper ─────────────────────────────────────────────────────────

@Composable
private fun SectionHeader(
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

// ── User section ──────────────────────────────────────────────────────────────────

@Composable
private fun UserSearchSection(
    title: String,
    items: LazyPagingItems<User>,
    onUserClick: (IParam) -> Unit,
    onSeeAllClick: () -> Unit,
) {
    val refreshState = items.loadState.refresh
    Column(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
        SectionHeader(title = title, onSeeAllClick = onSeeAllClick)

        when {
            items.itemCount > 0 -> {
                val previewItems = remember(items.itemCount) { List(items.itemCount) { index -> items[index] }.filterNotNull().take(8) }
                LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp), contentPadding = PaddingValues(vertical = 4.dp)) {
                    items(items = previewItems, key = { user -> user.id }) { user ->
                        UserCompactRow(
                            user = user,
                            onClick = {
                                onUserClick(ProfileRouter.ProfileParam(userId = user.id, userName = user.name.toString()))
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
private fun UserDrillDown(
    items: LazyPagingItems<User>,
    onUserClick: (IParam) -> Unit,
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
                    key = items.itemKey { user -> user.id },
                    contentType = items.itemContentType { "search_user_row" },
                ) { index ->
                    val user = items[index] ?: return@items
                    UserListItem(
                        user = user,
                        onClick = {
                            onUserClick(ProfileRouter.ProfileParam(userId = user.id, userName = user.name.toString()))
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
private fun UserCompactRow(
    user: User,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val displayName = user.name.toString()
    Surface(
        modifier =
            modifier
                .width(138.dp)
                .clickable(onClick = onClick),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.18f),
        tonalElevation = 0.dp,
        shape = RoundedCornerShape(24.dp),
        border =
            BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f),
            ),
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.35f)),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = displayName.firstOrNull()?.uppercase() ?: "?",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                )
            }
            Text(
                text = displayName,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun UserListItem(
    user: User,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val displayName = user.name.toString()
    Surface(
        modifier =
            modifier
                .fillMaxWidth()
                .clickable(onClick = onClick),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.18f),
        shape = RoundedCornerShape(22.dp),
        border =
            BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f),
            ),
        tonalElevation = 0.dp,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier =
                    Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.35f)),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = displayName.firstOrNull()?.uppercase() ?: "?",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = displayName,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

// ── Studio section ────────────────────────────────────────────────────────────────

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
private fun StudioSearchSection(
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
private fun StudioDrillDown(
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
                .fillMaxWidth()
                .clickable(onClick = onClick),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.72f),
        shape = RoundedCornerShape(18.dp),
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
                        text = "Animation Studio",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
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
                .fillMaxWidth()
                .clickable(onClick = onClick),
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
                        text = "Animation Studio",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                if (item.favourites > 0) {
                    Text(
                        text = "${item.favourites} favourites",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}

// ── Character section ─────────────────────────────────────────────────────────────

private fun characterDisplayName(item: DomainCharacter): String =
    item.name?.userPreferred
        ?: item.name?.full
        ?: "Unknown"

@Composable
private fun CharacterSearchSection(
    title: String,
    items: LazyPagingItems<DomainCharacter>,
    onCharacterClick: (IParam) -> Unit,
    onSeeAllClick: () -> Unit,
) {
    val refreshState = items.loadState.refresh
    Column(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
        SectionHeader(title = title, onSeeAllClick = onSeeAllClick)

        when {
            items.itemCount > 0 -> {
                val previewItems = remember(items.itemCount) { List(items.itemCount) { index -> items[index] }.filterNotNull().take(6) }
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp), contentPadding = PaddingValues(vertical = 4.dp)) {
                    items(items = previewItems, key = { it.id }) { character ->
                        CharacterSearchCard(
                            item = character,
                            onClick = {
                                onCharacterClick(CharacterRouter.CharacterParam(id = character.id, name = characterDisplayName(character)))
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
private fun CharacterDrillDown(
    items: LazyPagingItems<DomainCharacter>,
    onCharacterClick: (IParam) -> Unit,
    modifier: Modifier = Modifier,
) {
    val refreshState = items.loadState.refresh
    when {
        items.itemCount > 0 -> {
            LazyColumn(
                modifier = modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 24.dp),
            ) {
                items(
                    count = items.itemCount,
                    key = items.itemKey { it.id },
                    contentType = items.itemContentType { "search_character_row" },
                ) { index ->
                    val character = items[index] ?: return@items
                    CharacterSearchListItem(
                        item = character,
                        onClick = {
                            onCharacterClick(CharacterRouter.CharacterParam(id = character.id, name = characterDisplayName(character)))
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
private fun CharacterSearchCard(
    item: DomainCharacter,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
) {
    val primaryName = characterDisplayName(item)

    Surface(
        modifier =
            modifier
                .width(138.dp)
                .then(
                    if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier,
                ),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.18f),
        tonalElevation = 0.dp,
        shape = RoundedCornerShape(24.dp),
        border =
            BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f),
            ),
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .aspectRatio(0.84f)
                        .clip(RoundedCornerShape(20.dp))
                        .background(MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.35f)),
            ) {
                item.image?.let { image ->
                    AniTrendImage(
                        image = image,
                        imageType = RequestImage.Media.ImageType.POSTER,
                        contentDescription = primaryName,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize(),
                    )
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = primaryName,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Composable
private fun CharacterSearchListItem(
    item: DomainCharacter,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
) {
    val displayName = characterDisplayName(item)

    Surface(
        modifier =
            modifier
                .fillMaxWidth()
                .then(
                    if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier,
                ),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.18f),
        shape = RoundedCornerShape(22.dp),
        border =
            BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f),
            ),
        tonalElevation = 0.dp,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier =
                    Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.35f)),
            ) {
                item.image?.let { image ->
                    AniTrendImage(
                        image = image,
                        imageType = RequestImage.Media.ImageType.POSTER,
                        contentDescription = displayName,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize(),
                    )
                }
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(3.dp),
            ) {
                Text(
                    text = displayName,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

// ── Staff section ─────────────────────────────────────────────────────────────────

private fun staffDisplayName(item: Staff): String =
    item.name?.userPreferred
        ?: item.name?.full
        ?: "Unknown"

private fun staffRoleLabel(item: Staff): String = item.primaryOccupations.firstOrNull() ?: "Staff"

@Composable
private fun StaffSearchSection(
    title: String,
    items: LazyPagingItems<Staff>,
    onStaffClick: (IParam) -> Unit,
    onSeeAllClick: () -> Unit,
) {
    val refreshState = items.loadState.refresh
    Column(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
        SectionHeader(title = title, onSeeAllClick = onSeeAllClick)

        when {
            items.itemCount > 0 -> {
                val previewItems = remember(items.itemCount) { List(items.itemCount) { index -> items[index] }.filterNotNull().take(4) }
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp), contentPadding = PaddingValues(vertical = 4.dp)) {
                    items(items = previewItems, key = { it.id }) { staff ->
                        StaffSearchCard(
                            item = staff,
                            onClick = {
                                onStaffClick(StaffRouter.StaffParam(id = staff.id, name = staffDisplayName(staff)))
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
private fun StaffDrillDown(
    items: LazyPagingItems<Staff>,
    onStaffClick: (IParam) -> Unit,
    modifier: Modifier = Modifier,
) {
    val refreshState = items.loadState.refresh
    when {
        items.itemCount > 0 -> {
            LazyColumn(
                modifier = modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 24.dp),
            ) {
                items(
                    count = items.itemCount,
                    key = items.itemKey { it.id },
                    contentType = items.itemContentType { "search_staff_row" },
                ) { index ->
                    val staffItem = items[index] ?: return@items
                    StaffSearchListItem(
                        item = staffItem,
                        onClick = {
                            onStaffClick(StaffRouter.StaffParam(id = staffItem.id, name = staffDisplayName(staffItem)))
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
private fun StaffSearchCard(
    item: Staff,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
) {
    val displayName = staffDisplayName(item)
    val roleLabel = staffRoleLabel(item)

    Surface(
        modifier =
            modifier
                .width(138.dp)
                .then(
                    if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier,
                ),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.18f),
        tonalElevation = 0.dp,
        shape = RoundedCornerShape(24.dp),
        border =
            BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f),
            ),
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .aspectRatio(0.84f)
                        .clip(RoundedCornerShape(20.dp))
                        .background(MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.35f)),
            ) {
                item.image?.let { image ->
                    AniTrendImage(
                        image = image,
                        imageType = RequestImage.Media.ImageType.POSTER,
                        contentDescription = displayName,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize(),
                    )
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = roleLabel,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = displayName,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Composable
private fun StaffSearchListItem(
    item: Staff,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
) {
    val displayName = staffDisplayName(item)
    val roleLabel = staffRoleLabel(item)

    Surface(
        modifier =
            modifier
                .fillMaxWidth()
                .then(
                    if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier,
                ),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.18f),
        shape = RoundedCornerShape(22.dp),
        border =
            BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f),
            ),
        tonalElevation = 0.dp,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (item.image?.medium != null || item.image?.large != null) {
                Box(
                    modifier =
                        Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.35f)),
                ) {
                    item.image?.let { image ->
                        AniTrendImage(
                            image = image,
                            imageType = RequestImage.Media.ImageType.POSTER,
                            contentDescription = displayName,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize(),
                        )
                    }
                }
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(3.dp),
            ) {
                Text(
                    text = roleLabel,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = displayName,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

// ── Shared avatar badge ───────────────────────────────────────────────────────────

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

// ── UserPreviewSection (kept for backward compat) ─────────────────────────────────

@Composable
private fun UserPreviewSection(state: UserPreviewState) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.label_search_user_preview_title),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
        )

        when (state) {
            UserPreviewState.Idle ->
                SearchState(
                    title = stringResource(R.string.label_search_user_preview_title),
                    subtitle = stringResource(R.string.message_search_user_preview_hint),
                )

            UserPreviewState.Loading ->
                SearchState(
                    title = stringResource(R.string.label_search_loading_title),
                    subtitle = stringResource(R.string.message_search_loading),
                )

            UserPreviewState.Empty ->
                SearchState(
                    title = stringResource(R.string.label_search_empty_title),
                    subtitle = stringResource(R.string.message_search_empty),
                )

            is UserPreviewState.Error ->
                SearchState(
                    title = stringResource(R.string.label_search_error_title),
                    subtitle = state.message ?: stringResource(R.string.message_search_error),
                )

            is UserPreviewState.Content ->
                UserPreviewRow(user = state.user)
        }
    }
}

@Composable
private fun UserPreviewRow(user: User) {
    SearchState(
        title = user.name.toString(),
        subtitle = stringResource(R.string.message_search_user_preview_ready),
    )
}

// ── Main screen content ───────────────────────────────────────────────────────────

@Composable
fun SearchScreenContent(
    settings: IUserSettings,
    viewModel: SearchViewModel,
    onBackClick: () -> Unit,
    onSeeAllClick: (SearchScope) -> Unit,
    onExitScope: () -> Unit,
    onMediaItemClick: (IParam) -> Unit,
    onUserClick: (IParam) -> Unit,
    onStudioClick: (IParam) -> Unit,
    onStaffClick: (IParam) -> Unit,
    onCharacterClick: (IParam) -> Unit,
) {
    val query = viewModel.query.collectAsStateWithLifecycle()
    val scope = viewModel.scope.collectAsStateWithLifecycle()
    val scoreFormat = settings.scoreFormat.flow.collectAsStateWithLifecycle(initialValue = settings.scoreFormat.value)

    val allMedia = viewModel.mediaAll.collectAsLazyPagingItems()
    val animeMedia = viewModel.mediaAnime.collectAsLazyPagingItems()
    val mangaMedia = viewModel.mediaManga.collectAsLazyPagingItems()
    val mediaPreferenceData = remember(scoreFormat.value) { MediaPreferenceData(scoreFormat = scoreFormat.value) }
    val homeScrollState = rememberScrollState()

    val userItems = viewModel.users.collectAsLazyPagingItems()
    val studioItems = viewModel.studios.collectAsLazyPagingItems()
    val staffItems = viewModel.staff.collectAsLazyPagingItems()
    val characterItems = viewModel.characters.collectAsLazyPagingItems()

    DefaultScaffold(onBackPress = if (scope.value == SearchScope.HOME) onBackClick else onExitScope) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            SearchBarContent(
                query = query.value,
                onQueryChange = viewModel::onQueryChange,
                onSearch = viewModel::submitSearch,
                modifier = Modifier.fillMaxWidth(),
            )

            SearchScopeChips(
                scope = scope.value,
                onScopeClick = viewModel::showScope,
            )

            when (scope.value) {
                SearchScope.HOME -> {
                    Column(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .verticalScroll(homeScrollState),
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                    ) {
                        SearchSection(
                            title = stringResource(R.string.label_search_media_all),
                            items = allMedia,
                            mediaPreferenceData = mediaPreferenceData,
                            onMediaItemClick = onMediaItemClick,
                            onSeeAllClick = { onSeeAllClick(SearchScope.ALL) },
                        )
                        SearchSection(
                            title = stringResource(R.string.label_search_media_anime),
                            items = animeMedia,
                            mediaPreferenceData = mediaPreferenceData,
                            onMediaItemClick = onMediaItemClick,
                            onSeeAllClick = { onSeeAllClick(SearchScope.ANIME) },
                        )
                        SearchSection(
                            title = stringResource(R.string.label_search_media_manga),
                            items = mangaMedia,
                            mediaPreferenceData = mediaPreferenceData,
                            onMediaItemClick = onMediaItemClick,
                            onSeeAllClick = { onSeeAllClick(SearchScope.MANGA) },
                        )
                        UserSearchSection(
                            title = stringResource(R.string.label_search_users),
                            items = userItems,
                            onUserClick = onUserClick,
                            onSeeAllClick = { onSeeAllClick(SearchScope.USERS) },
                        )
                        StudioSearchSection(
                            title = stringResource(R.string.label_search_studios),
                            items = studioItems,
                            onStudioClick = onStudioClick,
                            onSeeAllClick = { onSeeAllClick(SearchScope.STUDIOS) },
                        )
                        StaffSearchSection(
                            title = stringResource(R.string.label_search_staff),
                            items = staffItems,
                            onStaffClick = onStaffClick,
                            onSeeAllClick = { onSeeAllClick(SearchScope.STAFF) },
                        )
                        CharacterSearchSection(
                            title = stringResource(R.string.label_search_characters),
                            items = characterItems,
                            onCharacterClick = onCharacterClick,
                            onSeeAllClick = { onSeeAllClick(SearchScope.CHARACTERS) },
                        )
                    }
                }

                SearchScope.ALL ->
                    SearchDrillDown(
                        items = allMedia,
                        mediaPreferenceData = mediaPreferenceData,
                        onMediaItemClick = onMediaItemClick,
                        modifier = Modifier.weight(1f),
                    )

                SearchScope.ANIME ->
                    SearchDrillDown(
                        items = animeMedia,
                        mediaPreferenceData = mediaPreferenceData,
                        onMediaItemClick = onMediaItemClick,
                        modifier = Modifier.weight(1f),
                    )

                SearchScope.MANGA ->
                    SearchDrillDown(
                        items = mangaMedia,
                        mediaPreferenceData = mediaPreferenceData,
                        onMediaItemClick = onMediaItemClick,
                        modifier = Modifier.weight(1f),
                    )

                SearchScope.USERS ->
                    UserDrillDown(
                        items = userItems,
                        onUserClick = onUserClick,
                        modifier = Modifier.weight(1f),
                    )

                SearchScope.STUDIOS ->
                    StudioDrillDown(
                        items = studioItems,
                        onStudioClick = onStudioClick,
                        modifier = Modifier.weight(1f),
                    )

                SearchScope.STAFF ->
                    StaffDrillDown(
                        items = staffItems,
                        onStaffClick = onStaffClick,
                        modifier = Modifier.weight(1f),
                    )

                SearchScope.CHARACTERS ->
                    CharacterDrillDown(
                        items = characterItems,
                        onCharacterClick = onCharacterClick,
                        modifier = Modifier.weight(1f),
                    )
            }
        }
    }
}

private fun SearchScope.label(): String =
    when (this) {
        SearchScope.HOME -> "Home"
        SearchScope.ALL -> "All"
        SearchScope.ANIME -> "Anime"
        SearchScope.MANGA -> "Manga"
        SearchScope.USERS -> "Users"
        SearchScope.STUDIOS -> "Studios"
        SearchScope.STAFF -> "Staff"
        SearchScope.CHARACTERS -> "Characters"
    }

@AniTrendPreview.Default
@Composable
private fun SearchScreenPreview(
    @androidx.compose.ui.tooling.preview.PreviewParameter(DarkThemeProvider::class) darkTheme: Boolean,
) {
    PreviewTheme(darkTheme = darkTheme, wrapInSurface = true) {
        SearchState(
            title = "Search preview",
            subtitle = "Media sections will render from paging data.",
            modifier = Modifier.padding(16.dp),
        )
    }
}
