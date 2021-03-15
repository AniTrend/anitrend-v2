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

package co.anitrend.data.cache.repository.contract

import co.anitrend.data.cache.model.CacheIdentity
import org.threeten.bp.Instant
import org.threeten.bp.temporal.TemporalAmount

internal interface ICacheStorePolicy {

    /**
     * Checks if the given [identity] has been requested before a certain time [instant]
     *
     * @param identity Unique identifier for the cache item
     * @param instant Specific point in time
     */
    suspend fun isRequestBefore(
        identity: CacheIdentity,
        instant: Instant
    ): Boolean

    /**
     * Checks if the given [identity] has expired using a [threshold]
     *
     * @param identity Unique identifier for the cache item
     * @param threshold Threshold expiry bias
     *
     * @see isRequestBefore
     */
    suspend fun isRequestExpired(
        identity: CacheIdentity,
        threshold: TemporalAmount
    ): Boolean

    /**
     * Checks if the given [identity] has been requested before, regardless of when
     *
     * @param identity Unique identifier for the cache item
     */
    suspend fun hasBeenRequested(identity: CacheIdentity): Boolean

    /**
     * Check if a resource with a given [identity] is permitted to refresh
     *
     * @param identity Unique identifier for the cache item
     * @param expiresAfter Expiry time fro the cached [identity]
     */
    suspend fun shouldRefresh(
        identity: CacheIdentity,
        expiresAfter: Instant
    ): Boolean

    /**
     * Updates the last request [timestamp] for the given [identity]
     */
    suspend fun updateLastRequest(
        identity: CacheIdentity,
        timestamp: Instant
    )

    /**
     * Invalidates cache records for the given [identity]
     */
    suspend fun invalidateLastRequest(identity: CacheIdentity)
}