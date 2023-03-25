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

package co.anitrend.auth.component.viewmodel.state

import co.anitrend.auth.model.Authentication
import co.anitrend.core.component.viewmodel.state.AniTrendViewModelState
import co.anitrend.data.auth.AuthUserInteractor
import co.anitrend.domain.account.model.AccountParam
import co.anitrend.domain.user.entity.User
import kotlinx.coroutines.flow.MutableStateFlow

class AuthState(
    override val interactor: AuthUserInteractor
) : AniTrendViewModelState<User>() {

    val authenticationFlow =
        MutableStateFlow<Authentication>(Authentication.Idle)

    operator fun invoke(authenticating: Authentication.Authenticating) {
        val result = interactor.getAuthenticatedUser(
            AccountParam.SignIn(
                accessToken = authenticating.accessToken,
                tokenType = authenticating.tokenType,
                expiresIn = authenticating.expiresIn
            )
        )
        state.postValue(result)
    }
}