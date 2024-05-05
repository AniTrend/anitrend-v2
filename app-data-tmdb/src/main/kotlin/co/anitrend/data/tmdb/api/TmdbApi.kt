/*
 * Copyright (C) 2021 AniTrend
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
package co.anitrend.data.tmdb.api

import co.anitrend.data.core.extensions.koinOf
import co.anitrend.data.tmdb.BuildConfig
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.uwetrottmann.tmdb2.Tmdb
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

internal class TmdbApi(
    apiKey: String,
    private val cache: Cache,
) : Tmdb(apiKey) {
    /**
     * Adds an interceptor to add the api key query parameter and to log requests.
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
