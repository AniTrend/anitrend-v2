/*
 * Copyright (C) 2020  AniTrend
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

package co.anitrend.data.news

import androidx.paging.PagedList
import co.anitrend.arch.data.state.DataState
import co.anitrend.data.arch.controller.core.DefaultController
import co.anitrend.data.news.entity.NewsEntity
import co.anitrend.data.news.model.page.NewsPageModel
import co.anitrend.data.news.model.query.NewsQuery
import co.anitrend.domain.news.entity.News
import co.anitrend.domain.news.interactor.NewsUseCase
import co.anitrend.domain.news.repository.NewsRepository

internal typealias NewPagedController = DefaultController<NewsPageModel, List<NewsEntity>>
internal typealias NewsPagedRepository = NewsRepository<DataState<PagedList<News>>, NewsQuery>

typealias NewsInteractor = NewsUseCase<DataState<PagedList<News>>, NewsQuery>