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

import co.anitrend.arch.extension.network.SupportConnectivity
import co.anitrend.data.api.helper.cache.model.TimeSpecification
import okhttp3.CacheControl
import okhttp3.Request
import timber.log.Timber

internal object CacheHelper {
    internal const val MAX_CACHE_SIZE = (1024 * 1024 * 25).toLong()

    private val moduleTag = javaClass.simpleName

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
    internal fun addCacheControl(
        connectivity: SupportConnectivity,
        cacheAge: TimeSpecification,
        staleAge: TimeSpecification,
        request: Request
    ): Request.Builder {
        val host = request.url.host
        return if (connectivity.isConnected) {
            Timber.tag(moduleTag).v(
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
            Timber.tag(moduleTag).v(
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