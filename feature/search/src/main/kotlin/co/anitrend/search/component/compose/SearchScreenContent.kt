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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import co.anitrend.common.media.ui.compose.entity.MediaPreferenceData
import co.anitrend.common.shared.ui.compose.DefaultScaffold
import co.anitrend.data.user.settings.IUserSettings
import co.anitrend.navigation.model.common.IParam
import co.anitrend.search.R
import co.anitrend.search.component.viewmodel.SearchScope
import co.anitrend.search.component.viewmodel.SearchViewModel

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
    val hasSubmittedSearch = viewModel.hasSubmittedSearch.collectAsStateWithLifecycle()
    val hasSubmittedQuery = viewModel.hasSubmittedQuery.collectAsStateWithLifecycle()
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
                // TODO: Replace Column+verticalScroll with LazyColumn for better
                //       scroll performance when many sections are visible. The search
                //       bar and scope chips should use stickyHeader {} items.
                SearchScope.HOME -> {
                    if (!hasSubmittedSearch.value) {
                        SearchState(
                            title = stringResource(R.string.label_search_idle_title),
                            subtitle = stringResource(R.string.message_search_idle),
                        )
                        return@Column
                    }

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
                        if (hasSubmittedQuery.value) {
                            CharacterSearchSection(
                                title = stringResource(R.string.label_search_characters),
                                items = characterItems,
                                onCharacterClick = onCharacterClick,
                                onSeeAllClick = { onSeeAllClick(SearchScope.CHARACTERS) },
                            )
                            StaffSearchSection(
                                title = stringResource(R.string.label_search_staff),
                                items = staffItems,
                                onStaffClick = onStaffClick,
                                onSeeAllClick = { onSeeAllClick(SearchScope.STAFF) },
                            )
                            StudioSearchSection(
                                title = stringResource(R.string.label_search_studios),
                                items = studioItems,
                                onStudioClick = onStudioClick,
                                onSeeAllClick = { onSeeAllClick(SearchScope.STUDIOS) },
                            )
                            UserSearchSection(
                                title = stringResource(R.string.label_search_users),
                                items = userItems,
                                onUserClick = onUserClick,
                                onSeeAllClick = { onSeeAllClick(SearchScope.USERS) },
                            )
                        }
                    }
                }

                SearchScope.ALL ->
                    if (!hasSubmittedSearch.value) {
                        SearchState(
                            title = stringResource(R.string.label_search_idle_title),
                            subtitle = stringResource(R.string.message_search_idle),
                            modifier = Modifier.weight(1f),
                        )
                    } else {
                        SearchDrillDown(
                            items = allMedia,
                            mediaPreferenceData = mediaPreferenceData,
                            onMediaItemClick = onMediaItemClick,
                            modifier = Modifier.weight(1f),
                        )
                    }

                SearchScope.ANIME ->
                    if (!hasSubmittedSearch.value) {
                        SearchState(
                            title = stringResource(R.string.label_search_idle_title),
                            subtitle = stringResource(R.string.message_search_idle),
                            modifier = Modifier.weight(1f),
                        )
                    } else {
                        SearchDrillDown(
                            items = animeMedia,
                            mediaPreferenceData = mediaPreferenceData,
                            onMediaItemClick = onMediaItemClick,
                            modifier = Modifier.weight(1f),
                        )
                    }

                SearchScope.MANGA ->
                    if (!hasSubmittedSearch.value) {
                        SearchState(
                            title = stringResource(R.string.label_search_idle_title),
                            subtitle = stringResource(R.string.message_search_idle),
                            modifier = Modifier.weight(1f),
                        )
                    } else {
                        SearchDrillDown(
                            items = mangaMedia,
                            mediaPreferenceData = mediaPreferenceData,
                            onMediaItemClick = onMediaItemClick,
                            modifier = Modifier.weight(1f),
                        )
                    }

                SearchScope.USERS ->
                    if (!hasSubmittedQuery.value) {
                        SearchState(
                            title = stringResource(R.string.label_search_idle_title),
                            subtitle = stringResource(R.string.message_search_idle),
                            modifier = Modifier.weight(1f),
                        )
                    } else {
                        UserDrillDown(
                            items = userItems,
                            onUserClick = onUserClick,
                            modifier = Modifier.weight(1f),
                        )
                    }

                SearchScope.STUDIOS ->
                    if (!hasSubmittedQuery.value) {
                        SearchState(
                            title = stringResource(R.string.label_search_idle_title),
                            subtitle = stringResource(R.string.message_search_idle),
                            modifier = Modifier.weight(1f),
                        )
                    } else {
                        StudioDrillDown(
                            items = studioItems,
                            onStudioClick = onStudioClick,
                            modifier = Modifier.weight(1f),
                        )
                    }

                SearchScope.STAFF ->
                    if (!hasSubmittedQuery.value) {
                        SearchState(
                            title = stringResource(R.string.label_search_idle_title),
                            subtitle = stringResource(R.string.message_search_idle),
                            modifier = Modifier.weight(1f),
                        )
                    } else {
                        StaffDrillDown(
                            items = staffItems,
                            onStaffClick = onStaffClick,
                            modifier = Modifier.weight(1f),
                        )
                    }

                SearchScope.CHARACTERS ->
                    if (!hasSubmittedQuery.value) {
                        SearchState(
                            title = stringResource(R.string.label_search_idle_title),
                            subtitle = stringResource(R.string.message_search_idle),
                            modifier = Modifier.weight(1f),
                        )
                    } else {
                        CharacterDrillDown(
                            items = characterItems,
                            onCharacterClick = onCharacterClick,
                            modifier = Modifier.weight(1f),
                        )
                    }
            }
        }
    }
}
