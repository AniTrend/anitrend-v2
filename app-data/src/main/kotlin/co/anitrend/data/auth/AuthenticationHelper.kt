/*
 * Copyright (C) 2019  AniTrend
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

package co.anitrend.data.auth

import android.net.Uri
import co.anitrend.data.BuildConfig
import co.anitrend.arch.data.auth.SupportAuthentication
import co.anitrend.arch.extension.preference.SupportPreference
import co.anitrend.data.auth.settings.IAuthenticationSettings
import co.anitrend.data.auth.settings.IAuthenticationSettings.Companion.INVALID_USER_ID
import okhttp3.Request

/**
 * Provides an api to handle authentication based processes
 */
class AuthenticationHelper(
    private val settings: IAuthenticationSettings
) : SupportAuthentication() {

    /**
     * Checks if the data source that contains the token is valid,
     * how to determine the validity of an existing token differs
     * with each token type
     */
    override fun isTokenValid(): Boolean {
        // use case doesn't support token validation checking
        return true
    }

    /**
     * Handle invalid token state by either renewing it or un-authenticates
     * the user locally if the token cannot be refreshed
     */
    override fun onInvalidToken() {
        with (settings) {
            authenticatedUserId = INVALID_USER_ID
            isAuthenticated = false
        }
    }

    /**
     * Handles complex task or dispatching of token refreshing to the an external work,
     * optionally the implementation can perform these operation internally
     */
    override fun refreshToken(): Boolean {
        // use case doesn't support token refreshing
        return false
    }

    /**
     * Facade to provide information on authentication status of the application,
     * on demand
     */
    override val isAuthenticated: Boolean
        get() = settings.isAuthenticated

    operator fun invoke(requestBuilder: Request.Builder) {
        if (settings.isAuthenticated)
            requestBuilder.addHeader(AUTHORIZATION, "")
    }


    companion object {

        const val CALLBACK_QUERY_KEY = "access_token"

        const val AUTHORIZATION = "Authorization"

        val AUTHENTICATION_URI = Uri.Builder()
                .scheme("https")
                .authority(BuildConfig.apiAuthUrl)
                .appendPath("authorize")
                .appendQueryParameter("client_id", BuildConfig.cliendId)
                .appendQueryParameter("response_type", "token")
                .build()

    }
}