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
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import co.anitrend.arch.domain.entities.LoadState
import co.anitrend.data.character.GetSearchCharacterInteractor
import co.anitrend.data.media.GetPagingMediaInteractor
import co.anitrend.data.staff.GetPagingStaffInteractor
import co.anitrend.data.studio.GetSearchStudioInteractor
import co.anitrend.data.user.GetSearchUserInteractor
import co.anitrend.domain.character.entity.Character
import co.anitrend.domain.character.model.CharacterParam
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.media.enums.MediaType
import co.anitrend.domain.media.model.MediaParam
import co.anitrend.domain.staff.entity.Staff
import co.anitrend.domain.staff.model.StaffParam
import co.anitrend.domain.studio.entity.Studio
import co.anitrend.domain.studio.model.StudioParam
import co.anitrend.domain.user.entity.User
import co.anitrend.domain.user.model.UserParam
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

class SearchViewModel(
    private val mediaInteractor: GetPagingMediaInteractor,
    private val userSearchInteractor: GetSearchUserInteractor,
    private val studioInteractor: GetSearchStudioInteractor,
    private val staffInteractor: GetPagingStaffInteractor,
    private val characterInteractor: GetSearchCharacterInteractor,
) : ViewModel() {
    private val mutableQuery = MutableStateFlow("")
    val query: StateFlow<String> = mutableQuery.asStateFlow()

    internal val submittedQuery = MutableStateFlow("")

    private val mutableScope = MutableStateFlow(SearchScope.HOME)
    val scope: StateFlow<SearchScope> = mutableScope.asStateFlow()

    val mediaAll: Flow<PagingData<Media>> = buildMediaFlow(type = null)
    val mediaAnime: Flow<PagingData<Media>> = buildMediaFlow(type = MediaType.ANIME)
    val mediaManga: Flow<PagingData<Media>> = buildMediaFlow(type = MediaType.MANGA)

    val studios: Flow<PagingData<Studio>> = buildStudioFlow()
    val staff: Flow<PagingData<Staff>> = buildStaffFlow()
    val characters: Flow<PagingData<Character>> = buildCharacterFlow()
    val users: Flow<PagingData<User>> = buildUserFlow()

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

    private fun buildStudioFlow(): Flow<PagingData<Studio>> =
        submittedQuery
            .map(String::trim)
            .distinctUntilChanged()
            .flatMapLatest { searchQuery ->
                studioInteractor.getStudioPaged(
                    StudioParam.Find(
                        search = searchQuery.ifBlank { null },
                    ),
                )
            }.cachedIn(viewModelScope)

    private fun buildStaffFlow(): Flow<PagingData<Staff>> =
        submittedQuery
            .map(String::trim)
            .distinctUntilChanged()
            .flatMapLatest { searchQuery ->
                staffInteractor(
                    StaffParam.Paged(
                        search = searchQuery.ifBlank { null },
                    ),
                )
            }.cachedIn(viewModelScope)

    private fun buildCharacterFlow(): Flow<PagingData<Character>> =
        submittedQuery
            .map(String::trim)
            .distinctUntilChanged()
            .flatMapLatest { searchQuery ->
                characterInteractor(
                    CharacterParam.Find(
                        search = searchQuery.ifBlank { null },
                    ),
                )
            }.cachedIn(viewModelScope)

    @OptIn(ExperimentalPagingApi::class)
    private fun buildUserFlow(): Flow<PagingData<User>> =
        submittedQuery
            .map(String::trim)
            .distinctUntilChanged()
            .flatMapLatest { searchQuery ->
                if (searchQuery.isBlank()) {
                    flowOf(PagingData.empty())
                } else {
                    val dataState =
                        userSearchInteractor(
                            UserParam.Search(search = searchQuery),
                        )
                    flow {
                        dataState.model.collect { user ->
                            emit(PagingData.from(listOfNotNull(user)))
                        }
                    }
                }
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
    USERS,
    STUDIOS,
    STAFF,
    CHARACTERS,
}
