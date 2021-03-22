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

import co.anitrend.data.cache.datasource.CacheLocalSource
import co.anitrend.data.cache.entity.CacheEntity
import co.anitrend.data.cache.helper.inPast
import co.anitrend.data.cache.model.CacheIdentity
import co.anitrend.data.cache.model.CacheRequest
import co.anitrend.data.cache.repository.contract.ICacheStorePolicy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.threeten.bp.Instant
import org.threeten.bp.temporal.TemporalAmount

internal abstract class CacheStorePolicy : ICacheStorePolicy {

    protected abstract val localSource: CacheLocalSource
    protected abstract val request: CacheRequest

    private suspend fun getRequestInstant(
        entityId: Long
    ) = withContext(Dispatchers.IO) {
        localSource.getCacheLog(request, entityId)?.timestamp
    }

    /**
     * Checks if the given [identity] has been requested before a certain time [instant]
     */
    override suspend fun isRequestBefore(
        identity: CacheIdentity,
        instant: Instant
    ) = getRequestInstant(identity.id)?.isBefore(instant) ?: true

    /**
     * Checks if the given [identity] has expired using a [threshold]
     *
     * @see isRequestBefore
     */
    override suspend fun isRequestExpired(
        identity: CacheIdentity,
        threshold: TemporalAmount
    ): Boolean = isRequestBefore(identity, threshold.inPast())

    /**
     * Checks if the given [identity] has been requested before, regardless of when
     */
    override suspend fun hasBeenRequested(
        identity: CacheIdentity
    ) = withContext(Dispatchers.IO) {
        localSource.countMatching(request, identity.id) > 0
    }

    /**
     * Updates the last request [timestamp] for the given [identity]
     */
    override suspend fun updateLastRequest(identity: CacheIdentity, timestamp: Instant) {
        withContext(Dispatchers.IO) {
            localSource.upsert(
                CacheEntity(
                    request = request,
                    cacheItemId = identity.id,
                    timestamp = timestamp
                )
            )
        }
    }

    /**
     * Invalidates cache records for the given [identity]
     */
    override suspend fun invalidateLastRequest(identity: CacheIdentity) =
        updateLastRequest(identity, Instant.EPOCH)
}