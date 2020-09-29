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

import co.anitrend.data.cache.helper.instantInPast
import org.threeten.bp.Instant
import org.threeten.bp.temporal.TemporalAmount

internal interface ICacheStorePolicy {

    /**
     * Checks if the given [entityId] has been requested before a certain time [instant]
     *
     * @param entityId Unique identifier for the cache item
     * @param instant Specific point in time
     */
    suspend fun isRequestBefore(entityId: Long, instant: Instant): Boolean

    /**
     * Checks if the given [entityId] has expired using a [threshold]
     *
     * @param entityId Unique identifier for the cache item
     * @param threshold Threshold expirey bias
     *
     * @see isRequestBefore
     */
    suspend fun isRequestExpired(entityId: Long, threshold: TemporalAmount): Boolean

    /**
     * Checks if the given [entityId] has been requested before, regardless of when
     *
     * @param entityId Unique identifier for the cache item
     */
    suspend fun hasBeenRequested(entityId: Long): Boolean

    /**
     * Check if a resource with a given [entityId] is permitted to refresh
     *
     * @param entityId Unique identifier for the cache item
     * @param expiresAfter defaults to 2 hours
     */
    suspend fun shouldRefresh(
        entityId: Long,
        expiresAfter: Instant = instantInPast(hours = 2)
    ): Boolean

    /**
     * Updates the last request [timestamp] for the given [entityId]
     */
    suspend fun updateLastRequest(
        entityId: Long,
        timestamp: Instant = Instant.now()
    )

    /**
     * Invalidates cache records for the given [entityId]
     */
    suspend fun invalidateLastRequest(entityId: Long)
}