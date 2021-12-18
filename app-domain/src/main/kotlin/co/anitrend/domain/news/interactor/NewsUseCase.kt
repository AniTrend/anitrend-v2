/*
 * Copyright (C) 2021  AniTrend
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

package co.anitrend.domain.news.interactor

import co.anitrend.arch.domain.common.IUseCase
import co.anitrend.arch.domain.state.UiState
import co.anitrend.domain.news.model.NewsParam
import co.anitrend.domain.news.repository.INewsRepository

sealed class NewsUseCase : IUseCase {

    abstract class GetPaged<State : UiState<*>>(
        protected val repository: INewsRepository.Paged<State>
    ) : NewsUseCase() {
        operator fun invoke(param: NewsParam) =
            repository.getPagedNews(param)
    }

    abstract class Sync<State : UiState<*>>(
        protected val repository: INewsRepository.Sync<State>
    ) : NewsUseCase() {
        operator fun invoke(param: NewsParam) =
            repository.sync(param)
    }
}