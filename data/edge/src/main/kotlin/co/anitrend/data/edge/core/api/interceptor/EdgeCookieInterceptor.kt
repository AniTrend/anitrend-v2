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

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import timber.log.Timber

internal class EdgeCookieInterceptor(
    private val cookieJar: CookieJar,
) : Interceptor {
    private fun HttpUrl.getCsrfCookie(): Cookie? =
        cookieJar
            .loadForRequest(this)
            .filter {
                it.name == CSRF_KEY
            }.maxByOrNull { it.expiresAt }

    private fun Request.Builder.attachCsrfToken(url: HttpUrl): Request.Builder {
        url.getCsrfCookie()?.let { cookie ->
            header(HEADER_CSRF_TOKEN, cookie.value)
        } ?: Timber.d("No cookie matching CSRF_KEY found")
        return this
    }

    private fun Request.Builder.requestHeadOrigin(chain: Interceptor.Chain): Result<Unit> {
        method("HEAD", null)
        return chain.proceed(build()).use { response ->
            when (response.code) {
                404 -> Result.success(Unit)
                else -> Result.failure(Exception("Request failed with code ${response.code}"))
            }
        }
    }

    override fun intercept(chain: Interceptor.Chain): Response =
        synchronized(chain) {
            val request = chain.request()
            val response =
                chain.proceed(
                    request
                        .newBuilder()
                        .attachCsrfToken(request.url)
                        .build(),
                )
            if (response.code == 403) {
                runCatching(response::close).onFailure(Timber::e)
                Timber.d("Request failed with error ${response.code}")
                runCatching { request.newBuilder().requestHeadOrigin(chain) }.onFailure(Timber::e)
                return chain.proceed(
                    request
                        .newBuilder()
                        .attachCsrfToken(request.url)
                        .build(),
                )
            }
            Timber.d("Skipping CSRF, returning original response")
            return response
        }

    private companion object {
        const val CSRF_KEY = "csrftoken"
        const val HEADER_CSRF_TOKEN = "x-$CSRF_KEY"
    }
}
