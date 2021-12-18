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

    abstract class Sync<State: UiState<*>>(
        protected val repository: IMediaListRepository.Sync<State>
    ) : MediaListUseCase() {
        operator fun invoke(param: MediaListParam.Collection): State =
            repository.sync(param)
    }

    abstract class GetEntry<State: UiState<*>>(
        protected val repository: IMediaListRepository.Entry<State>
    ) : MediaListUseCase() {
        operator fun invoke(param: MediaListParam.Entry): State =
            repository.getEntry(param)
    }

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

    abstract class SaveEntry<State: UiState<*>>(
        protected val repository: IMediaListRepository.SaveEntry<State>
    ) : MediaListUseCase() {
        operator fun invoke(param: MediaListParam.SaveEntry): State =
            repository.saveEntry(param)
    }

    abstract class SaveEntries<State: UiState<*>>(
        protected val repository: IMediaListRepository.SaveEntries<State>
    ) : MediaListUseCase() {
        operator fun invoke(param: MediaListParam.SaveEntries): State =
            repository.saveEntries(param)
    }

    abstract class DeleteCustomList<State: UiState<*>>(
        protected val repository: IMediaListRepository.DeleteCustomList<State>
    ) : MediaListUseCase() {
        operator fun invoke(param: MediaListParam.DeleteCustomList): State =
            repository.deleteCustomList(param)
    }

    abstract class DeleteEntry<State: UiState<*>>(
        protected val repository: IMediaListRepository.DeleteEntry<State>
    ) : MediaListUseCase() {
        operator fun invoke(param: MediaListParam.DeleteEntry): State =
            repository.deleteEntry(param)
    }
}