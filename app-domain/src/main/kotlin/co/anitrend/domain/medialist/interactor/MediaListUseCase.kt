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

package co.anitrend.domain.medialist.interactor

import co.anitrend.arch.domain.common.IUseCase
import co.anitrend.arch.domain.state.UiState
import co.anitrend.domain.medialist.model.MediaListParam
import co.anitrend.domain.medialist.repository.IMediaListRepository

sealed class MediaListUseCase : IUseCase {

    abstract class GetPaged<State: UiState<*>>(
        protected val repository: IMediaListRepository.Paged<State>
    ) : MediaListUseCase() {
        operator fun invoke(param: MediaListParam.Paged): State =
            repository.getPaged(param)
    }

    abstract class GetCollection<State: UiState<*>>(
        protected val repository: IMediaListRepository.Collection<State>
    ) : MediaListUseCase() {
        operator fun invoke(param: MediaListParam.Collection): State =
            repository.getCollection(param)
    }

    abstract class Save<State: UiState<*>>(
        protected val repository: IMediaListRepository.Save<State>
    ) : MediaListUseCase() {
        operator fun invoke(param: MediaListParam.Save): State =
            repository.save(param)
    }

    abstract class Delete<State: UiState<*>>(
        protected val repository: IMediaListRepository.Delete<State>
    ) : MediaListUseCase() {
        operator fun invoke(param: MediaListParam.Delete): State =
            repository.delete(param)
    }
}