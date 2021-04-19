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

package co.anitrend.data.auth.helper.contract

import okhttp3.Request

interface IAuthenticationHelper {

    /**
     * Facade to provide information on authentication status of the application,
     * on demand
     */
    val isAuthenticated: Boolean

    /**
     * Invalidates any properties related to authentication state
     */
    suspend fun invalidateAuthenticationState()

    /**
     * Injects authorization properties into the ongoing request
     *
     * @param requestBuilder The current ongoing request builder
     */
    operator fun invoke(requestBuilder: Request.Builder)

    companion object {
        const val AUTHORIZATION = "Authorization"
    }
}