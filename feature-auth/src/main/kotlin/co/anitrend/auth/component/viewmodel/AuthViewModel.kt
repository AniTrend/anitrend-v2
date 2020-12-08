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
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.anitrend.auth.R
import co.anitrend.auth.component.viewmodel.state.AuthState
import co.anitrend.auth.model.Authentication
import co.anitrend.data.auth.helper.*
import timber.log.Timber

class AuthViewModel(
    val state: AuthState
) : ViewModel() {

    init {
        state.context = viewModelScope.coroutineContext
    }

    fun onIntentData(context: Context, data: Uri?) {
        if (data == null)
            return

        // APP_URL/callback#access_token=TOKEN_HERE&token_type=TOKEN_TYPE&expires_in=EXPIRES_IN_HERE
        // Why are we even using fragments :not_like:
        val uri = Uri.parse(data.toString().replaceFirst('#', '?'))

        runCatching {
            state.authenticationFlow.value = Authentication.Authenticating(
                requireNotNull(uri.getQueryParameter(CALLBACK_QUERY_TOKEN_KEY)) {
                    "$CALLBACK_QUERY_TOKEN_KEY was not found in -> $uri"
                },
                requireNotNull(uri.getQueryParameter(CALLBACK_QUERY_TOKEN_TYPE_KEY)) {
                    "$CALLBACK_QUERY_TOKEN_TYPE_KEY was not found in -> $uri"
                },
                requireNotNull(uri.getQueryParameter(CALLBACK_QUERY_TOKEN_EXPIRES_IN_KEY)) {
                    "$CALLBACK_QUERY_TOKEN_EXPIRES_IN_KEY was not found in -> $uri"
                }.toLong()
            )
        }.onFailure {
            state.authenticationFlow.value = Authentication.Error(
                title = uri.getQueryParameter(CALLBACK_QUERY_ERROR_KEY)
                    ?: context.getString(R.string.auth_error_default_title),
                message = uri.getQueryParameter(CALLBACK_QUERY_ERROR_DESCRIPTION_KEY)
                    ?: context.getString(R.string.auth_error_default_message)
            )
            Timber.tag(moduleTag).w(it)
        }
    }

    /**
     * This method will be called when this ViewModel is no longer used and will be destroyed.
     *
     * It is useful when ViewModel observes some data and you need to clear this subscription to
     * prevent a leak of this ViewModel.
     */
    override fun onCleared() {
        state.onCleared()
        super.onCleared()
    }

    companion object {
        private val moduleTag = AuthViewModel::class.java.simpleName
    }
}