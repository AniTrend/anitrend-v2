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

package co.anitrend.domain.user.interactor

import co.anitrend.arch.domain.state.UiState
import co.anitrend.domain.user.model.UserParam
import co.anitrend.domain.user.repository.IUserRepository

sealed class UserUseCase {

    abstract class GetUser<State: UiState<*>>(
        protected val repository: IUserRepository.User<State>
    ) : UserUseCase() {
        suspend operator fun invoke(param: UserParam.Identifier) = repository.getUser(param)
    }

    abstract class GetAuthenticated<State: UiState<*>>(
        protected val repository: IUserRepository.Authenticated<State>
    ) : UserUseCase() {
        suspend operator fun invoke() = repository.getProfile()
    }

    abstract class GetPaged<State: UiState<*>>(
        protected val repository: IUserRepository.Search<State>
    ) : UserUseCase() {
        suspend operator fun invoke(param: UserParam.Search) = repository.getPaged(param)
    }

    abstract class GetProfile<State: UiState<*>>(
        protected val repository: IUserRepository.Profile<State>
    ) : UserUseCase() {
        suspend operator fun invoke(param: UserParam.Profile) = repository.getProfile(param)
    }

    abstract class ToggleFollow<State: UiState<*>>(
        protected val repository: IUserRepository.ToggleFollow<State>
    ) : UserUseCase() {
        suspend operator fun invoke(param: UserParam.ToggleFollow) = repository.toggleFollow(param)
    }

    abstract class Update<State: UiState<*>>(
        protected val repository: IUserRepository.Update<State>
    ) : UserUseCase() {
        suspend operator fun invoke(param: UserParam.Update) = repository.updateProfile(param)
    }

    abstract class Statistic<State: UiState<*>>(
        protected val repository: IUserRepository.Statistic<State>
    ) : UserUseCase() {
        suspend operator fun invoke(param: UserParam.Statistic) = repository.getProfileStatistic(param)
    }
}
