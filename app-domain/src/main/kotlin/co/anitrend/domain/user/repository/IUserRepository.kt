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

package co.anitrend.domain.user.repository

import co.anitrend.arch.domain.state.UiState
import co.anitrend.domain.user.model.UserParam

interface IUserRepository {

    interface Authenticated<State: UiState<*>> : IUserRepository {
        fun getProfile(): State
    }

    interface Profile<State: UiState<*>> : IUserRepository {
        fun getProfile(param: UserParam.Profile): State
    }

    interface Search<State: UiState<*>> {
        fun getPaged(param: UserParam.Search): State
    }

    interface ToggleFollow<State : UiState<*>> : IUserRepository {
        fun toggleFollow(param: UserParam.ToggleFollow): State
    }

    interface Update<State : UiState<*>> : IUserRepository {
        fun updateProfile(param: UserParam.Update): State
    }

    interface Statistic<State : UiState<*>> : IUserRepository {
        fun getProfileStatistic(param: UserParam.Statistic): State
    }
}