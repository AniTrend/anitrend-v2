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
package co.anitrend.search.component.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import co.anitrend.data.media.GetPagingMediaInteractor
import co.anitrend.data.user.GetSearchUserInteractor
import co.anitrend.domain.user.entity.User
import co.anitrend.domain.user.model.UserParam
import co.anitrend.arch.domain.entities.LoadState
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.media.enums.MediaType
import co.anitrend.domain.media.model.MediaParam
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted

class SearchViewModel(
    private val mediaInteractor: GetPagingMediaInteractor,
    private val userSearchInteractor: GetSearchUserInteractor,
) : ViewModel() {
    private val mutableQuery = MutableStateFlow("")
    val query: StateFlow<String> = mutableQuery.asStateFlow()

    private val submittedQuery = MutableStateFlow("")

    private val mutableScope = MutableStateFlow(SearchScope.HOME)
    val scope: StateFlow<SearchScope> = mutableScope.asStateFlow()

    val mediaAll: Flow<PagingData<Media>> = buildMediaFlow(type = null)
    val mediaAnime: Flow<PagingData<Media>> = buildMediaFlow(type = MediaType.ANIME)
    val mediaManga: Flow<PagingData<Media>> = buildMediaFlow(type = MediaType.MANGA)
    val userPreviewState: StateFlow<UserPreviewState> =
        submittedQuery
            .map(String::trim)
            .distinctUntilChanged()
            .flatMapLatest(::buildUserPreviewFlow)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = UserPreviewState.Idle,
            )

    init {
        query
            .map(String::trim)
            .debounce(400)
            .onEach { debounced ->
                submittedQuery.value = debounced
            }.launchIn(viewModelScope)
    }

    fun onQueryChange(value: String) {
        mutableQuery.value = value
    }

    fun submitSearch(value: String = query.value) {
        val trimmed = value.trim()
        mutableQuery.value = trimmed
        submittedQuery.value = trimmed
    }

    fun showHome() {
        mutableScope.value = SearchScope.HOME
    }

    fun showScope(scope: SearchScope) {
        mutableScope.value = scope
    }

    private fun buildMediaFlow(type: MediaType?): Flow<PagingData<Media>> =
        submittedQuery
            .map(String::trim)
            .distinctUntilChanged()
            .flatMapLatest { searchQuery ->
                mediaInteractor(
                    MediaParam.Find(
                        search = searchQuery.ifBlank { null },
                        type = type,
                    ),
                )
            }.cachedIn(viewModelScope)

    private suspend fun buildUserPreviewFlow(searchQuery: String): Flow<UserPreviewState> {
        if (searchQuery.isBlank()) {
            return MutableStateFlow(UserPreviewState.Idle)
        }

        val dataState =
            userSearchInteractor(
                UserParam.Search(
                    search = searchQuery,
                ),
            )

        return combine(
            dataState.model
                .map<User, User?> { it }
                .onStart { emit(null) },
            dataState.loadState,
        ) { user, loadState ->
            when {
                user != null -> UserPreviewState.Content(user)
                loadState is LoadState.Loading -> UserPreviewState.Loading
                loadState is LoadState.Error -> UserPreviewState.Error(loadState.details.message)
                loadState is LoadState.Success -> UserPreviewState.Empty
                else -> UserPreviewState.Idle
            }
        }
    }
}

sealed interface UserPreviewState {
    data object Idle : UserPreviewState

    data object Loading : UserPreviewState

    data object Empty : UserPreviewState

    data class Content(
        val user: User,
    ) : UserPreviewState

    data class Error(
        val message: String?,
    ) : UserPreviewState
}

enum class SearchScope {
    HOME,
    ALL,
    ANIME,
    MANGA,
}
