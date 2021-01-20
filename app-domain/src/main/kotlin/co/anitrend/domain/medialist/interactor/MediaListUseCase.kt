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
import co.anitrend.domain.common.graph.IGraphPayload
import co.anitrend.domain.medialist.repository.MediaListRepository

sealed class MediaListUseCase : IUseCase {

    abstract class GetPaged<State: UiState<*>, Query: IGraphPayload>(
        protected val repository: MediaListRepository.Paged<State>
    ) : MediaListUseCase() {
        operator fun invoke(query: Query): State =
            repository.getMediaListPaged(query)
    }

    abstract class GetCollection<State: UiState<*>, Query: IGraphPayload>(
        protected val repository: MediaListRepository.Collection<State>
    ) : MediaListUseCase() {
        operator fun invoke(query: Query): State =
            repository.getMediaListCollection(query)
    }

    abstract class Save<State: UiState<*>, Mutation: IGraphPayload>(
        protected val repository: MediaListRepository.Save<State>
    ) : MediaListUseCase() {
        operator fun invoke(mutation: Mutation): State =
            repository.saveMediaList(mutation)
    }

    abstract class Delete<State: UiState<*>, Mutation: IGraphPayload>(
        protected val repository: MediaListRepository.Delete<State>
    ) : MediaListUseCase() {
        operator fun invoke(mutation: Mutation): State =
            repository.deleteMediaList(mutation)
    }
}