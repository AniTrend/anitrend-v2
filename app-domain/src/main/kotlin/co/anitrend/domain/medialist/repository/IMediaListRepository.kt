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

package co.anitrend.domain.medialist.repository

import co.anitrend.arch.domain.state.UiState
import co.anitrend.domain.medialist.model.MediaListParam

interface IMediaListRepository {

    interface Paged<State: UiState<*>> : IMediaListRepository {
        fun getPaged(param: MediaListParam.Paged): State
    }

    interface Collection<State: UiState<*>> : IMediaListRepository {
        fun getCollection(param: MediaListParam.Collection): State
    }

    interface Save<State: UiState<*>> : IMediaListRepository {
        fun save(param: MediaListParam.Save): State
    }

    interface Delete<State: UiState<*>> : IMediaListRepository {
        fun delete(param: MediaListParam.Delete): State
    }

    interface UpdateMany<State: UiState<*>> : IMediaListRepository {
        fun updateMany(param: MediaListParam.UpdateMany): State
    }
}