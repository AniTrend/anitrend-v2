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
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.media.enums.MediaType
import co.anitrend.domain.media.model.MediaParam
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

class SearchViewModel(
    private val mediaInteractor: GetPagingMediaInteractor,
) : ViewModel() {
    private val mutableQuery = MutableStateFlow("")
    val query: StateFlow<String> = mutableQuery.asStateFlow()

    private val submittedQuery = MutableStateFlow("")

    private val mutableScope = MutableStateFlow(SearchScope.HOME)
    val scope: StateFlow<SearchScope> = mutableScope.asStateFlow()

    val mediaAll: Flow<PagingData<Media>> = buildMediaFlow(type = null)
    val mediaAnime: Flow<PagingData<Media>> = buildMediaFlow(type = MediaType.ANIME)
    val mediaManga: Flow<PagingData<Media>> = buildMediaFlow(type = MediaType.MANGA)

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
}

enum class SearchScope {
    HOME,
    ALL,
    ANIME,
    MANGA,
}
