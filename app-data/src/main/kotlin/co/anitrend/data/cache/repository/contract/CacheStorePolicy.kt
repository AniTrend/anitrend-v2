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

import org.threeten.bp.Instant
import org.threeten.bp.temporal.TemporalAmount

internal interface CacheStorePolicy {

    /**
     * Checks if the given [entityId] has been requested before a certain time [instant]
     */
    suspend fun isRequestBefore(entityId: Long, instant: Instant): Boolean

    /**
     * Checks if the given [entityId] has expired using a [threshold]
     *
     * @see isRequestBefore
     */
    suspend fun isRequestExpired(entityId: Long, threshold: TemporalAmount): Boolean

    /**
     * Checks if the given [entityId] has been requested before, regardless of when
     */
    suspend fun hasBeenRequested(entityId: Long): Boolean

    /**
     * Check if a resource with a given [entityId] is permitted to refresh
     */
    suspend fun shouldRefresh(
        entityId: Long,
        expiresAfter: Instant
    ): Boolean
}