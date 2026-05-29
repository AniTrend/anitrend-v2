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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import co.anitrend.android.core.ui.AniTrendPreview
import co.anitrend.android.core.ui.theme.preview.DarkThemeProvider
import co.anitrend.android.core.ui.theme.preview.PreviewTheme
import co.anitrend.common.media.ui.compose.entity.MediaPreferenceData
import co.anitrend.common.media.ui.compose.item.MediaCompactItem
import co.anitrend.common.media.ui.compose.item.MediaPosterListItem
import co.anitrend.common.shared.ui.compose.DefaultScaffold
import co.anitrend.data.user.settings.IUserSettings
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.user.entity.User
import co.anitrend.navigation.model.common.IParam
import co.anitrend.search.R
import co.anitrend.search.component.viewmodel.SearchScope
import co.anitrend.search.component.viewmodel.SearchViewModel
import co.anitrend.search.component.viewmodel.UserPreviewState
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions

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
) {
    val refreshState = items.loadState.refresh
    when {
        items.itemCount > 0 -> {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp), contentPadding = PaddingValues(bottom = 24.dp)) {
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
private fun UserPreviewSection(
    state: UserPreviewState,
) {
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
                UserPreviewRow(
                    user = state.user,
                )
        }
    }
}

@Composable
private fun UserPreviewRow(
    user: User,
) {
    SearchState(
        title = user.name.toString(),
        subtitle = stringResource(R.string.message_search_user_preview_ready),
    )
}

@Composable
fun SearchScreenContent(
    settings: IUserSettings,
    viewModel: SearchViewModel,
    onBackClick: () -> Unit,
    onSeeAllClick: (SearchScope) -> Unit,
    onExitScope: () -> Unit,
    onMediaItemClick: (IParam) -> Unit,
) {
    val query = viewModel.query.collectAsStateWithLifecycle()
    val scope = viewModel.scope.collectAsStateWithLifecycle()
    val scoreFormat = settings.scoreFormat.flow.collectAsStateWithLifecycle(initialValue = settings.scoreFormat.value)
    val userPreviewState = viewModel.userPreviewState.collectAsStateWithLifecycle()

    val allMedia = viewModel.mediaAll.collectAsLazyPagingItems()
    val animeMedia = viewModel.mediaAnime.collectAsLazyPagingItems()
    val mangaMedia = viewModel.mediaManga.collectAsLazyPagingItems()
    val mediaPreferenceData = remember(scoreFormat.value) { MediaPreferenceData(scoreFormat = scoreFormat.value) }

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
                    UserPreviewSection(state = userPreviewState.value)
                }

                SearchScope.ALL ->
                    SearchDrillDown(
                        items = allMedia,
                        mediaPreferenceData = mediaPreferenceData,
                        onMediaItemClick = onMediaItemClick,
                    )

                SearchScope.ANIME ->
                    SearchDrillDown(
                        items = animeMedia,
                        mediaPreferenceData = mediaPreferenceData,
                        onMediaItemClick = onMediaItemClick,
                    )

                SearchScope.MANGA ->
                    SearchDrillDown(
                        items = mangaMedia,
                        mediaPreferenceData = mediaPreferenceData,
                        onMediaItemClick = onMediaItemClick,
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
