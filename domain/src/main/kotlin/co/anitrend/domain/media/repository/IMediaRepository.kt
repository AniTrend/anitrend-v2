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
package co.anitrend.domain.media.repository

import co.anitrend.arch.domain.state.UiState
import co.anitrend.domain.media.model.MediaParam

interface IMediaRepository {
    interface Detail<State : UiState<*>> : IMediaRepository {
        fun getMedia(param: MediaParam.Detail): State
    }

    interface Relations<State : UiState<*>> : IMediaRepository {
        fun getRelations(param: MediaParam.Relations): State
    }

    interface Recommendations<State : UiState<*>> : IMediaRepository {
        fun getRecommendations(param: MediaParam.Recommendations): State
    }

    interface Characters<State> : IMediaRepository {
        fun getCharacters(param: MediaParam.Characters): State
    }

    interface Staff<State> : IMediaRepository {
        fun getStaff(param: MediaParam.Staff): State
    }

    interface Studios<State : UiState<*>> : IMediaRepository {
        fun getStudios(param: MediaParam.Studios): State
    }

    interface Stats<State : UiState<*>> : IMediaRepository {
        fun getStats(param: MediaParam.Stats): State
    }

    interface Paged<State> : IMediaRepository {
        fun getPaged(param: MediaParam.Find): State
    }

    interface Network<State : UiState<*>> : IMediaRepository {
        fun getPaged(param: MediaParam.Find): State
    }
}
