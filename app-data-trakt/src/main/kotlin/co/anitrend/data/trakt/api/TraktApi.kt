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

package co.anitrend.data.trakt.api

import co.anitrend.data.core.extensions.koinOf
import co.anitrend.data.trakt.BuildConfig
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.uwetrottmann.trakt5.TraktV2
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

internal class TraktApi(
    apiKey: String,
    clientSecret: String,
    redirectUri: String,
    isStaging: Boolean = false,
    private val cache: Cache
) : TraktV2(apiKey, clientSecret, redirectUri, isStaging) {

    /**
     * Adds [TraktV2Interceptor] as an application interceptor and [TraktV2Authenticator] as an authenticator.
     */
    override fun setOkHttpClientDefaults(builder: OkHttpClient.Builder) {
        super.setOkHttpClientDefaults(builder)
        builder
            .readTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .cache(cache)

        if (BuildConfig.DEBUG) {
            builder.addInterceptor(koinOf<HttpLoggingInterceptor>())
                .addInterceptor(koinOf<ChuckerInterceptor>())
        }
    }
}