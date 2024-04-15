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

package co.anitrend.auth.component.viewmodel

import android.content.Context
import co.anitrend.auth.R
import co.anitrend.auth.component.viewmodel.state.AuthState
import co.anitrend.auth.model.Authentication
import co.anitrend.core.component.viewmodel.AniTrendViewModel
import co.anitrend.navigation.AuthRouter
import timber.log.Timber

class AuthViewModel(
    val state: AuthState
) : AniTrendViewModel(state) {

    fun onIntentData(context: Context, param: AuthRouter.AuthParam?) {
        if (param == null) {
            Timber.d("AuthRouter.Param is null, no new intent data available. Skipping checks")
            return
        }

        runCatching {
            state.authenticationFlow.value = Authentication.Authenticating(
                requireNotNull(param.accessToken),
                requireNotNull(param.tokenType),
                requireNotNull(param.expiresIn)
            )
        }.onFailure {
            state.authenticationFlow.value = Authentication.Error(
                title = param.errorTitle
                    ?: context.getString(R.string.auth_error_default_title),
                message = param.errorDescription
                    ?: context.getString(R.string.auth_error_default_message)
            )
        }
    }
}
