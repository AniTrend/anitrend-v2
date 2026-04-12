/*
 * Copyright (C) 2021 AniTrend
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
package co.anitrend.news.component.content.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import co.anitrend.data.edge.news.NewsPagedInteractor
import co.anitrend.domain.news.entity.News
import co.anitrend.domain.news.model.NewsParam
import kotlinx.coroutines.flow.Flow

class NewsContentViewModel(
    private val interactor: NewsPagedInteractor,
) : ViewModel() {
    fun news(param: NewsParam): Flow<PagingData<News>> = interactor(param).cachedIn(viewModelScope)
}
