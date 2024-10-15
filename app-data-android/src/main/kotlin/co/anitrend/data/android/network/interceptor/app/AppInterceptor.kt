/*
 * Copyright (C) 2024 AniTrend
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

import co.anitrend.data.core.app.IAppInfo
import okhttp3.Interceptor
import okhttp3.Response

class AppInterceptor(
    private val appInfo: IAppInfo,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        builder.header(APP_NAME, appInfo.label)
        builder.header(APP_VERSION, appInfo.version)
        builder.header(APP_CODE, appInfo.code)
        builder.header(APP_SOURCE, appInfo.source)
        builder.header(APP_LOCALE, appInfo.locale)
        builder.header(APP_BUILD_TYPE, appInfo.buildType)
        return chain.proceed(builder.build())
    }

    private companion object {
        const val APP_NAME = "x-app-name"
        const val APP_VERSION = "x-app-version"
        const val APP_CODE = "x-app-code"
        const val APP_SOURCE = "x-app-source"
        const val APP_LOCALE = "x-app-locale"
        const val APP_BUILD_TYPE = "x-app-build-type"
    }
}
