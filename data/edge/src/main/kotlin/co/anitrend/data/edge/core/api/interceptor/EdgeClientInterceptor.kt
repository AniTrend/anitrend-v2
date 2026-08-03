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
package co.anitrend.data.edge.core.api.interceptor

import okhttp3.Interceptor
import okhttp3.Response

internal class EdgeClientInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val contentLength = request.body?.contentLength() ?: 0
        val requestBuilder = request.newBuilder()

        requestBuilder
            .header(CONTENT_TYPE, GRAPHQL_MIME_TYPE)
            .header(ACCEPT, ACCEPT_TYPE)
            .header(CONTENT_LENGTH, contentLength.toString())
            .method(request.method, request.body)

        val response = chain.proceed(requestBuilder.build())

        return response
    }

    companion object {
        private const val ACCEPT = "Accept"
        private const val CONTENT_LENGTH = "Content-Length"
        private const val CONTENT_TYPE = "Content-Type"

        private const val GRAPHQL_MIME_TYPE = "application/graphql"
        private const val ACCEPT_TYPE = "application/json"
    }
}
