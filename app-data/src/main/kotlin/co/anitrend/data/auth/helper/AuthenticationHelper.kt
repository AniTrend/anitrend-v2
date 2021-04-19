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

import co.anitrend.data.android.cache.datasource.CacheLocalSource
import co.anitrend.data.android.cache.model.CacheRequest
import co.anitrend.data.auth.datasource.local.AuthLocalSource
import co.anitrend.data.auth.helper.contract.IAuthenticationHelper
import co.anitrend.data.auth.settings.IAuthenticationSettings
import co.anitrend.data.medialist.datasource.local.MediaListLocalSource
import co.anitrend.data.user.settings.IUserSettings
import co.anitrend.domain.account.model.AccountParam
import okhttp3.Request
import timber.log.Timber

/**
 * Provides an api to handle authentication based processes
 */
internal class AuthenticationHelper(
    private val authSettings: IAuthenticationSettings,
    private val userSettings: IUserSettings,
    private val localSource: AuthLocalSource,
    private val mediaListLocalSource: MediaListLocalSource,
    private val cacheLocalSource: CacheLocalSource,
) : IAuthenticationHelper {

    /**
     * Invalidates any properties related to authentication state
     */
    override suspend fun invalidateAuthenticationState() {
        val setting = authSettings.authenticatedUserId
        val param = AccountParam.SignOut(setting.value)

        userSettings.invalidateSettings()
        authSettings.invalidateAuthenticationSettings()

        localSource.clearByUserId(param.userId)
        mediaListLocalSource.clearByUserId(param.userId)
        cacheLocalSource.clearByType(CacheRequest.MEDIA_LIST)
        cacheLocalSource.clearByType(CacheRequest.USER)
    }

    /**
     * Facade to provide information on authentication status of the application,
     * on demand
     */
    override val isAuthenticated: Boolean
        get() = authSettings.isAuthenticated.value

    /**
     * Injects authorization properties into the ongoing request
     *
     * @param requestBuilder The current ongoing request builder
     */
    override operator fun invoke(requestBuilder: Request.Builder) {
        val entity = localSource.byUserId(
            authSettings.authenticatedUserId.value
        )
        if (entity != null)
            requestBuilder.addHeader(IAuthenticationHelper.AUTHORIZATION, entity.accessToken)
        else
            Timber.w(
                "Settings indicates that user is authenticated, but no authentication token for the user can be found"
            )
    }
}