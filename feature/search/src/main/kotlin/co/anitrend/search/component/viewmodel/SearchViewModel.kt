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
import co.anitrend.data.character.GetSearchCharacterInteractor
import co.anitrend.data.media.GetPagingMediaInteractor
import co.anitrend.data.staff.GetPagingStaffInteractor
import co.anitrend.data.studio.GetSearchStudioInteractor
import co.anitrend.data.user.GetSearchUserInteractor
import co.anitrend.domain.character.entity.Character
import co.anitrend.domain.character.model.CharacterParam
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.media.enums.MediaFormat
import co.anitrend.domain.media.enums.MediaSeason
import co.anitrend.domain.media.enums.MediaStatus
import co.anitrend.domain.media.enums.MediaType
import co.anitrend.domain.media.model.MediaParam
import co.anitrend.domain.staff.entity.Staff
import co.anitrend.domain.staff.model.StaffParam
import co.anitrend.domain.studio.entity.Studio
import co.anitrend.domain.studio.model.StudioParam
import co.anitrend.domain.user.entity.User
import co.anitrend.domain.user.model.UserParam
import co.anitrend.navigation.SearchRouter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
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

    internal val submittedSearch = MutableStateFlow(SearchSubmission())

    private val mutableScope = MutableStateFlow(SearchScope.HOME)
    val scope: StateFlow<SearchScope> = mutableScope.asStateFlow()

    internal val hasSubmittedSearch: StateFlow<Boolean> =
        submittedSearch
            .map(SearchSubmission::hasAnyCriteria)
            .stateIn(
                scope = viewModelScope,
                started = kotlinx.coroutines.flow.SharingStarted.Eagerly,
                initialValue = false,
            )

    internal val hasSubmittedQuery: StateFlow<Boolean> =
        submittedSearch
            .map { it.query.isNotBlank() }
            .stateIn(
                scope = viewModelScope,
                started = kotlinx.coroutines.flow.SharingStarted.Eagerly,
                initialValue = false,
            )

    val mediaAll: Flow<PagingData<Media>> = buildMediaFlow(type = null)
    val mediaAnime: Flow<PagingData<Media>> = buildMediaFlow(type = MediaType.ANIME)
    val mediaManga: Flow<PagingData<Media>> = buildMediaFlow(type = MediaType.MANGA)

    val studios: Flow<PagingData<Studio>> = buildStudioFlow()
    val staff: Flow<PagingData<Staff>> = buildStaffFlow()
    val characters: Flow<PagingData<Character>> = buildCharacterFlow()
    val users: Flow<PagingData<User>> = buildUserFlow()

    fun onQueryChange(value: String) {
        mutableQuery.value = value
    }

    fun submitSearch(value: String = query.value) {
        val trimmed = value.trim()
        mutableQuery.value = trimmed
        submittedSearch.value =
            submittedSearch.value.copy(
                query = trimmed,
            )
    }

    fun initialize(param: SearchRouter.SearchParam) {
        val initialQuery = param.query?.trim().orEmpty()
        mutableQuery.value = initialQuery
        submittedSearch.value = SearchSubmission.from(param, initialQuery)
        mutableScope.value = param.destination.toSearchScope()
    }

    fun showHome() {
        mutableScope.value = SearchScope.HOME
    }

    fun showScope(scope: SearchScope) {
        mutableScope.value = scope
    }

    private fun buildMediaFlow(type: MediaType?): Flow<PagingData<Media>> =
        submittedSearch
            .flatMapLatest { submission ->
                if (!submission.hasMediaCriteria()) {
                    flowOf(PagingData.empty())
                } else {
                    mediaInteractor(
                        submission.toMediaParam(type = type),
                    )
                }
            }.cachedIn(viewModelScope)

    private fun buildStudioFlow(): Flow<PagingData<Studio>> =
        submittedSearch
            .map { it.query }
            .distinctUntilChanged()
            .flatMapLatest { searchQuery ->
                if (searchQuery.isBlank()) {
                    flowOf(PagingData.empty())
                } else {
                    studioInteractor.getStudioPaged(
                        StudioParam.Find(
                            search = searchQuery,
                        ),
                    )
                }
            }.cachedIn(viewModelScope)

    private fun buildStaffFlow(): Flow<PagingData<Staff>> =
        submittedSearch
            .map { it.query }
            .distinctUntilChanged()
            .flatMapLatest { searchQuery ->
                if (searchQuery.isBlank()) {
                    flowOf(PagingData.empty())
                } else {
                    staffInteractor(
                        StaffParam.Paged(
                            search = searchQuery,
                        ),
                    )
                }
            }.cachedIn(viewModelScope)

    private fun buildCharacterFlow(): Flow<PagingData<Character>> =
        submittedSearch
            .map { it.query }
            .distinctUntilChanged()
            .flatMapLatest { searchQuery ->
                if (searchQuery.isBlank()) {
                    flowOf(PagingData.empty())
                } else {
                    characterInteractor(
                        CharacterParam.Find(
                            search = searchQuery,
                        ),
                    )
                }
            }.cachedIn(viewModelScope)

    private fun buildUserFlow(): Flow<PagingData<User>> =
        submittedSearch
            .map { it.query }
            .distinctUntilChanged()
            .flatMapLatest { searchQuery ->
                if (searchQuery.isBlank()) {
                    flowOf(PagingData.empty())
                } else {
                    userSearchInteractor.getPaged(
                        UserParam.Search(search = searchQuery),
                    )
                }
            }.cachedIn(viewModelScope)
}

internal data class SearchSubmission(
    val query: String = "",
    val genres: List<String> = emptyList(),
    val year: Int? = null,
    val season: MediaSeason? = null,
    val format: MediaFormat? = null,
    val status: MediaStatus? = null,
) {
    fun hasAnyCriteria(): Boolean =
        query.isNotBlank() ||
            genres.isNotEmpty() ||
            year != null ||
            season != null ||
            format != null ||
            status != null

    fun hasMediaCriteria(): Boolean = hasAnyCriteria()

    fun toMediaParam(type: MediaType?): MediaParam.Find =
        MediaParam.Find(
            search = query.ifBlank { null },
            genre_in = genres.ifEmpty { null },
            seasonYear = year,
            season = season,
            format = format,
            status = status,
            type = type,
        )

    companion object {
        fun from(
            param: SearchRouter.SearchParam,
            initialQuery: String = param.query?.trim().orEmpty(),
        ): SearchSubmission =
            SearchSubmission(
                query = initialQuery,
                genres = param.genres.orEmpty(),
                year = param.year,
                season = param.season,
                format = param.format,
                status = param.status,
            )
    }
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

private fun SearchRouter.Destination.toSearchScope(): SearchScope =
    when (this) {
        SearchRouter.Destination.HOME -> SearchScope.HOME
        SearchRouter.Destination.ALL -> SearchScope.ALL
        SearchRouter.Destination.ANIME -> SearchScope.ANIME
        SearchRouter.Destination.MANGA -> SearchScope.MANGA
    }
