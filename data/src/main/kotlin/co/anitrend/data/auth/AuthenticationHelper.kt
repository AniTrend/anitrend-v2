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
import co.anitrend.data.auth.model.JsonWebToken
import co.anitrend.data.dao.query.JsonWebTokenDao
import co.anitrend.data.util.Settings
import co.anitrend.arch.data.auth.SupportAuthentication
import co.anitrend.arch.extension.empty
import co.anitrend.arch.extension.util.SupportConnectivityHelper
import okhttp3.Request
import timber.log.Timber

/**
 * Provides an api to handle authentication based processes
 */
class AuthenticationHelper(
    private val connectivityHelper: SupportConnectivityHelper,
    private val jsonWebTokenDao: JsonWebTokenDao,
    private val settings: Settings
): SupportAuthentication<Uri, Request.Builder>() {

    /**
     * Facade to provide information on authentication status of the application,
     * on demand
     */
    override val isAuthenticated: Boolean
        get() = settings.isAuthenticated

    /**
     * Checks if the data source that contains the token is valid
     */
    override fun isTokenValid(): Boolean {
        val token = jsonWebTokenDao.findLatest()
        return token == null
    }

    /**
     * Handles complex task or dispatching of token refreshing to the an external work,
     * optionally the implementation can perform these operation internally
     */
    override fun refreshToken(): Boolean {
        // anilist doesn't support refreshing of tokens
        if (!isTokenValid())
            return false
        return true
    }

    /**
     * Performs core operation of applying authentication credentials
     * at runtime
     *
     * @param resource object that need to be manipulated
     */
    override fun invoke(resource: Request.Builder) {
        assert(isAuthenticated)
        when (refreshToken()) {
            true -> {
                val latestToken = jsonWebTokenDao.findLatest()
                val tokenHeader = latestToken?.getTokenKey() ?: String.empty()
                resource.addHeader(
                    AUTHORIZATION,
                    tokenHeader
                )
            }
            false -> onInvalidToken()
        }
    }

    /**
     * If using Oauth 2 then once the user has approved your client they will be redirected to your redirect URI,
     * included in the URL fragment will be an access_token parameter that includes the JWT access token
     * used to make requests on their behalf.
     *
     * Otherwise this could just be an authentication result that should be handled and complete the authentication
     * process on the users behalf
     *
     * @param authPayload payload from an authenticating source
     * @return True if the operation was successful, false otherwise
     */
    override suspend fun handleCallbackUri(authPayload: Uri): Boolean {
        val jwt = authPayload.getQueryParameter(CALLBACK_QUERY_KEY)
        return if (!jwt.isNullOrBlank()) {
            val jsonWebToken = jsonWebTokenDao.findLatest()
            val jsonWebTokenId = jsonWebToken?.id ?: 1
            jsonWebTokenDao.insert(JsonWebToken(jsonWebTokenId, jwt))
            true
        } else false
    }

    /**
     * Handle invalid token state by either renewing it or un-authenticates
     * the user locally if the token cannot be refreshed
     */
    override fun onInvalidToken() {
        if (connectivityHelper.isConnected) {
            settings.authenticatedUserId = Settings.INVALID_USER_ID
            settings.isAuthenticated = false
            jsonWebTokenDao.clearTable()
            Timber.tag(moduleTag).e("Authentication token is null, application is logging user out!")
        }
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