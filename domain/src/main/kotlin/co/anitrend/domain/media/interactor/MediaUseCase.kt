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
package co.anitrend.domain.media.interactor

import co.anitrend.arch.domain.state.UiState
import co.anitrend.domain.media.model.MediaParam
import co.anitrend.domain.media.repository.IMediaRepository

sealed class MediaUseCase {
    abstract class GetDetail<State : UiState<*>>(
        protected val repository: IMediaRepository.Detail<State>,
    ) : MediaUseCase() {
        operator fun invoke(param: MediaParam.Detail) = repository.getMedia(param)
    }

    abstract class GetRelations<State : UiState<*>>(
        protected val repository: IMediaRepository.Relations<State>,
    ) : MediaUseCase() {
        operator fun invoke(param: MediaParam.Relations) = repository.getRelations(param)
    }

    abstract class GetRecommendations<State : UiState<*>>(
        protected val repository: IMediaRepository.Recommendations<State>,
    ) : MediaUseCase() {
        operator fun invoke(param: MediaParam.Recommendations) = repository.getRecommendations(param)
    }

    abstract class GetCharacters<State : UiState<*>>(
        protected val repository: IMediaRepository.Characters<State>,
    ) : MediaUseCase() {
        operator fun invoke(param: MediaParam.Characters) = repository.getCharacters(param)
    }

    abstract class GetStaff<State : UiState<*>>(
        protected val repository: IMediaRepository.Staff<State>,
    ) : MediaUseCase() {
        operator fun invoke(param: MediaParam.Staff) = repository.getStaff(param)
    }

    abstract class GetPaged<State : UiState<*>>(
        protected val repository: IMediaRepository.Paged<State>,
    ) : MediaUseCase() {
        operator fun invoke(param: MediaParam.Find) = repository.getPaged(param)
    }

    abstract class GetByNetwork<State : UiState<*>>(
        protected val repository: IMediaRepository.Network<State>,
    ) : MediaUseCase() {
        operator fun invoke(param: MediaParam.Find) = repository.getPaged(param)
    }
}
