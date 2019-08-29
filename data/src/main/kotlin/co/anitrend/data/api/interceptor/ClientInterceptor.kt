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

package co.anitrend.data.api.interceptor

import co.anitrend.data.api.converter.request.AniRequestConverter
import okhttp3.Interceptor
import okhttp3.Response

/**
 * ClientInterceptor interceptor add headers dynamically adds accept headers.
 * The context in which an [Interceptor] may be  parallel or asynchronous depending
 * on the dispatching caller, as such take care to assure thread safety
 */
class ClientInterceptor: Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        val request = original.newBuilder()
            .header("Content-Type", AniRequestConverter.MIME_TYPE)
            .header("Accept", AniRequestConverter.MIME_TYPE)
            .method(original.method, original.body)
            .build()

        return chain.proceed(request)
    }
}