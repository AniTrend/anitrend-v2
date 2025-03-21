/*
 * Copyright (C) 2020 AniTrend
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
package co.anitrend.news.component.screen.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.anitrend.arch.domain.entities.LoadState
import co.anitrend.arch.extension.ext.empty
import co.anitrend.navigation.NewsRouter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.jsoup.Jsoup

class NewsScreenViewModel : ViewModel() {
    private val _documentHtml = MutableStateFlow(String.empty())
    val documentHtml: Flow<String> = _documentHtml

    private val _loadState = MutableLiveData<LoadState>()
    val loadState: LiveData<LoadState> = _loadState

    private suspend fun buildHtml(param: NewsRouter.NewsParam) {
        val template =
            """
            <p><h3>${param.title}</h3></p>
            <h5>${param.subTitle}</h5>
            """.trimIndent()
        val document = Jsoup.parse("$template${param.content}")
        _documentHtml.emit(document.html())
        _loadState.postValue(LoadState.Success())
    }

    operator fun invoke(param: NewsRouter.NewsParam) {
        _loadState.postValue(LoadState.Loading())
        viewModelScope.launch {
            buildHtml(param)
        }
    }
}
