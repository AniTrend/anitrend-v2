/*
 * Copyright (C) 2025 AniTrend
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
package co.anitrend.data.android.network.interceptor.app

import co.anitrend.arch.extension.ext.empty
import co.anitrend.data.core.app.IAppInfo
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.atomic.AtomicReference

class AppInterceptor(
    private val appInfo: IAppInfo,
) : Interceptor {
    private val atomicRequestId = AtomicReference(String.empty())

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val builder = request.newBuilder()
        builder.header(APP_NAME, appInfo.label)
        builder.header(APP_VERSION, appInfo.version)
        builder.header(APP_BUILD, appInfo.build)
        builder.header(APP_CODE, appInfo.code)
        builder.header(APP_SOURCE, appInfo.source)
        builder.header(APP_LOCALE, appInfo.locale)
        builder.header(APP_BUILD_TYPE, appInfo.buildType)

        atomicRequestId.get().also { requestId ->
            if (requestId.isNotBlank()) {
                builder.header(APP_REQUEST_ID, requestId)
            }
        }

        return chain.proceed(builder.build()).also { response ->
            response.header(APP_REQUEST_ID)?.let(atomicRequestId::set)
        }
    }

    private companion object {
        const val APP_NAME = "x-app-name"
        const val APP_VERSION = "x-app-version"
        const val APP_BUILD = "x-app-build"
        const val APP_CODE = "x-app-code"
        const val APP_SOURCE = "x-app-source"
        const val APP_LOCALE = "x-app-locale"
        const val APP_BUILD_TYPE = "x-app-build-type"
        const val APP_REQUEST_ID = "x-request-id"
    }
}
