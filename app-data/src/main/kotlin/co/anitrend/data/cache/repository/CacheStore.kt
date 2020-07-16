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

package co.anitrend.data.cache.repository

import co.anitrend.data.cache.datasource.local.CacheDao
import co.anitrend.data.cache.entity.CacheEntity
import co.anitrend.data.cache.helper.inPast
import co.anitrend.data.cache.model.CacheRequest
import co.anitrend.data.cache.repository.contract.CacheStorePolicy
import org.threeten.bp.Instant
import org.threeten.bp.temporal.TemporalAmount

internal abstract class CacheStore(
    private val request: CacheRequest,
    private val dao: CacheDao
) : CacheStorePolicy {

    private suspend fun getRequestInstant(
        entityId: Long
    ) = dao.getCacheLog(request, entityId)?.timestamp

    /**
     * Checks if the given [entityId] has been requested before a certain time [instant]
     */
    override suspend fun isRequestBefore(
        entityId: Long,
        instant: Instant
    ) = getRequestInstant(entityId)?.isBefore(instant) ?: true

    /**
     * Checks if the given [entityId] has expired using a [threshold]
     *
     * @see isRequestBefore
     */
    override suspend fun isRequestExpired(
        entityId: Long,
        threshold: TemporalAmount
    ) = isRequestBefore(entityId, threshold.inPast())

    /**
     * Checks if the given [entityId] has been requested before, regardless of when
     */
    override suspend fun hasBeenRequested(
        entityId: Long
    ) = dao.countMatching(request, entityId) > 0
    
    suspend fun updateLastRequest(entityId: Long, timestamp: Instant = Instant.now()) {
        dao.upsert(
            CacheEntity(
                request = request,
                cacheItemId = entityId,
                timestamp = timestamp
            )
        )
    }

    suspend fun invalidateLastRequest(entityId: Long) =
        updateLastRequest(entityId, Instant.EPOCH)
}