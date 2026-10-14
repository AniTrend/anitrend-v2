/*
 * Copyright (C) 2026 AniTrend
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
package co.anitrend.data.edge.core.api.interceptor

import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.Response

internal class EdgeBaseUrlInterceptor(
    private val baseUrl: () -> String?,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val rewrittenUrl = rewriteEdgeApiUrl(request.url, baseUrl())
        if (rewrittenUrl == request.url) return chain.proceed(request)

        return chain.proceed(request.newBuilder().url(rewrittenUrl).build())
    }
}

internal fun rewriteEdgeApiUrl(
    requestUrl: HttpUrl,
    baseUrl: String?,
): HttpUrl {
    val overrideUrl = baseUrl?.toHttpUrlOrNull() ?: return requestUrl
    return requestUrl
        .newBuilder()
        .scheme(overrideUrl.scheme)
        .host(overrideUrl.host)
        .port(overrideUrl.port)
        .build()
}
