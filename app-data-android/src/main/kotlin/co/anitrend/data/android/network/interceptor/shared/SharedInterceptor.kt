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

package co.anitrend.data.android.network.interceptor.shared

import co.anitrend.data.core.device.IDeviceInfo
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Interceptor to add user agent to requests
 */
class SharedInterceptor(
    private val deviceInfo: IDeviceInfo
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val builder = request.newBuilder()
        builder
            .header(ACCEPT_ENCODING, "*")
            .header(ACCEPT_LANGUAGE, "*")
            .header(USER_AGENT, deviceInfo.userAgent)
            .header(CONNECTION, "keep-alive")
            .header(HOST, request.url.host)

        return chain.proceed(builder.build())
    }

    private companion object {
        const val ACCEPT_ENCODING = "Accept-Encoding"
        const val ACCEPT_LANGUAGE = "Accept-Language"
        const val CONNECTION = "Connection"
        const val USER_AGENT = "User-Agent"
        const val HOST = "Host"
    }
}
