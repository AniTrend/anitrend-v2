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

    interface Sync<State: UiState<*>> : IMediaListRepository {
        fun sync(param: MediaListParam.Collection): State
    }

    interface Entry<State: UiState<*>> : IMediaListRepository {
        fun getEntry(param: MediaListParam.Entry): State
    }

    interface Paged<State: UiState<*>> : IMediaListRepository {
        fun getPaged(param: MediaListParam.Paged): State
    }

    interface Collection<State: UiState<*>> : IMediaListRepository {
        fun getCollection(param: MediaListParam.Collection): State
    }

    interface SaveEntries<State: UiState<*>> : IMediaListRepository {
        fun saveEntries(param: MediaListParam.SaveEntries): State
    }

    interface SaveEntry<State: UiState<*>> : IMediaListRepository {
        fun saveEntry(param: MediaListParam.SaveEntry): State
    }

    interface DeleteCustomList<State: UiState<*>> : IMediaListRepository {
        fun deleteCustomList(param: MediaListParam.DeleteCustomList): State
    }

    interface DeleteEntry<State: UiState<*>> : IMediaListRepository {
        fun deleteEntry(param: MediaListParam.DeleteEntry): State
    }
}