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

package co.anitrend.data.auth.helper

import co.anitrend.data.auth.datasource.local.AuthLocalSource
import co.anitrend.data.auth.settings.IAuthenticationSettings
import co.anitrend.data.auth.settings.IAuthenticationSettings.Companion.INVALID_USER_ID
import okhttp3.Request
import timber.log.Timber

/**
 * Provides an api to handle authentication based processes
 */
internal class AuthenticationHelper(
    private val settings: IAuthenticationSettings,
    private val localSource: AuthLocalSource
) {

    private val moduleTag = javaClass.simpleName

    /**
     * Handle invalid token state by either renewing it or un-authenticates
     * the user locally if the token cannot be refreshed
     */
    fun onInvalidToken() {
        with (settings) {
            localSource.clearByUserId(authenticatedUserId.value)
            authenticatedUserId.value = INVALID_USER_ID
            isAuthenticated.value = false
        }
    }

    /**
     * Facade to provide information on authentication status of the application,
     * on demand
     */
    val isAuthenticated: Boolean
        get() = settings.isAuthenticated.value

    operator fun invoke(requestBuilder: Request.Builder) {
        val entity = localSource.byUserId(
            settings.authenticatedUserId.value
        )
        if (entity != null)
            requestBuilder.addHeader(AUTHORIZATION, entity.accessToken)
        else
            Timber.tag(moduleTag).w(
                "Settings indicates that user is authenticated, but no authentication token for the user can be found"
            )
    }

    companion object {
        const val AUTHORIZATION = "Authorization"
    }
}