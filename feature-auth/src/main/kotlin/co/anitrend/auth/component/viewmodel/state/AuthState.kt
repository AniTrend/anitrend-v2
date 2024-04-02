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

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import co.anitrend.arch.domain.entities.LoadState
import co.anitrend.arch.domain.entities.RequestError
import co.anitrend.auth.model.Authentication
import co.anitrend.core.component.viewmodel.state.AniTrendViewModelState
import co.anitrend.data.auth.AuthUserInteractor
import co.anitrend.domain.account.model.AccountParam
import co.anitrend.domain.user.entity.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import timber.log.Timber

class AuthState(
    private val interactor: AuthUserInteractor
) : AniTrendViewModelState<User>() {

    val authenticationFlow =
        MutableStateFlow<Authentication>(Authentication.Idle)

    override val loadState: LiveData<LoadState> = authenticationFlow
        .map { state ->
            Timber.v("Authentication flow state changed: $state")
            when (state) {
                is Authentication.Authenticate -> {
                    LoadState.Loading()
                }

                is Authentication.Error ->
                    LoadState.Error(
                        RequestError(
                            topic = state.title,
                            description = state.message,
                        ),
                    )

                else -> LoadState.Idle()
            }
        }.asLiveData(viewModelScope.coroutineContext)

    operator fun invoke(authentication: Authentication) {
        when (authentication) {
            is Authentication.Authenticate -> {
                val result = interactor.getAuthenticatedUser(
                    AccountParam.SignIn(
                        accessToken = authentication.accessToken,
                        tokenType = authentication.tokenType,
                        expiresIn = authentication.expiresIn
                    )
                )
                state.postValue(result)
            }
            else -> {
                Timber.i("AuthState.invoke triggered with param: $authentication")
            }
        }
    }
}
