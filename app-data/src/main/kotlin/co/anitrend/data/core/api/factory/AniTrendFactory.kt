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

package co.anitrend.data.core.api.factory

import co.anitrend.data.BuildConfig
import co.anitrend.data.android.network.agent.UserAgentInterceptor
import co.anitrend.data.auth.helper.contract.IAuthenticationHelper
import co.anitrend.data.core.api.factory.contract.IEndpointType
import co.anitrend.data.core.api.interceptor.GraphAuthenticator
import co.anitrend.data.core.api.interceptor.GraphClientInterceptor
import co.anitrend.data.core.extensions.defaultBuilder
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import org.koin.core.scope.Scope
import timber.log.Timber

internal class GraphApiFactory : IEndpointFactory {

    override val endpointType = object : IEndpointType {
        override val url: HttpUrl = BuildConfig.aniListApi.toHttpUrl()
    }

    override fun okHttpConfig(scope: Scope): OkHttpClient {
        val builder = scope.defaultBuilder(setOf(IAuthenticationHelper.AUTHORIZATION))
        Timber.d("Adding authenticator and request interceptors for request")
        val authenticatorHelper = scope.get<IAuthenticationHelper>()
        builder.addInterceptor(
            UserAgentInterceptor(deviceInfo = scope.get())
        ).addInterceptor(
            GraphClientInterceptor(authenticatorHelper)
        ).authenticator(
            GraphAuthenticator(authenticatorHelper)
        ).cookieJar(scope.get())
        return builder.build()
    }
}

internal class AuthenticationApiFactory : IEndpointFactory {

    override val endpointType = object : IEndpointType {
        override val url: HttpUrl = "https://${BuildConfig.aniListAuth}".toHttpUrl()
    }

    override fun okHttpConfig(scope: Scope): OkHttpClient {
        val builder = scope.defaultBuilder().addInterceptor(
            UserAgentInterceptor(deviceInfo = scope.get())
        )
        return builder.build()
    }
}