/*
 * Copyright (C) 2020  AniTrend
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

package co.anitrend.data.api.helper.cache

import android.content.Context
import co.anitrend.arch.extension.network.contract.ISupportConnectivity
import co.anitrend.data.api.helper.cache.model.TimeSpecification
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Request
import timber.log.Timber
import java.io.File

internal object CacheHelper {

    private const val MAX_CACHE_SIZE = (1024 * 1024 * 25).toLong()

    fun createCache(context: Context, name: String, maxSize: Long = MAX_CACHE_SIZE): Cache {
        val destination = context.externalCacheDir ?: context.cacheDir
        val cacheLocation = File(destination, name)
        if (!cacheLocation.exists()) cacheLocation.mkdirs()
        return Cache(cacheLocation, maxSize)
    }

    /**
     * If we have internet connectivity, get the cache that was stored to the equivalent [cacheAge].
     * If the cache is older, then discard it, and indicate an error in fetching the response.
     *
     * [cacheAge] is responsible for this behavior.
     *
     * Otherwise get the cache that was stored to the equivalent [staleAge], and if the cache
     * is older then discard it and indicate an error in fetching the response.
     *
     * [staleAge] is responsible for this behavior.
     *
     * @param connectivity connectivity helper
     * @param cacheAge duration cache is valid for
     * @param staleAge duration offline cache is valid for
     * @param request request to build on
     */
    fun addCacheControl(
        connectivity: ISupportConnectivity,
        cacheAge: TimeSpecification,
        staleAge: TimeSpecification,
        request: Request
    ): Request.Builder {
        val host = request.url.host
        return if (connectivity.isConnected) {
            Timber.v(
                "Online cache control applied on request to host: $host"
            )
            // "public, max-age=MAX_CACHE_AGE"
            request.newBuilder().cacheControl(
                CacheControl.Builder()
                    .maxAge(
                        cacheAge.time,
                        cacheAge.unit
                    ).build()
            )
        }
        else {
            Timber.v(
                "Offline cache control applied on request to host: $host"
            )
            // "public, only-if-cached, max-stale=MAX_STALE_TIME"
            request.newBuilder().cacheControl(
                CacheControl.Builder()
                    .maxStale(
                        staleAge.time,
                        staleAge.unit
                    ).onlyIfCached()
                    .build()
            )
        }
    }
}