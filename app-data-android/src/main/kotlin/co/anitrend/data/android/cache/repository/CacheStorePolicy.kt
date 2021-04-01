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

package co.anitrend.data.android.cache.repository

import co.anitrend.data.android.cache.datasource.CacheLocalSource
import co.anitrend.data.android.cache.entity.CacheEntity
import co.anitrend.data.android.cache.helper.inPast
import co.anitrend.data.android.cache.model.CacheIdentity
import co.anitrend.data.android.cache.model.CacheRequest
import co.anitrend.data.android.cache.repository.contract.ICacheStorePolicy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.threeten.bp.Instant
import org.threeten.bp.temporal.TemporalAmount
import timber.log.Timber

abstract class CacheStorePolicy : ICacheStorePolicy {

    protected abstract val localSource: CacheLocalSource
    protected abstract val request: CacheRequest

    private suspend fun getRequestInstant(
        entityId: Long
    ): Instant? = withContext(Dispatchers.IO) {
        val instant = localSource.getCacheLog(request, entityId)
        Timber.v("getRequestInstant(entityId: $entityId) -> $instant")
        instant?.timestamp
    }

    /**
     * Checks if the given [identity] has been requested before a certain time [instant]
     */
    override suspend fun isRequestBefore(
        identity: CacheIdentity,
        instant: Instant
    ): Boolean {
        val lastRequestInstant = getRequestInstant(identity.id)
        val isBefore = lastRequestInstant?.isBefore(instant)
        Timber.v("isRequestBefore(identity: $identity, instant: $instant) -> $isBefore")
        return isBefore ?: true
    }

    /**
     * Checks if the given [identity] has expired using a [threshold]
     *
     * @see isRequestBefore
     */
    override suspend fun isRequestExpired(
        identity: CacheIdentity,
        threshold: TemporalAmount
    ): Boolean {
        Timber.v("isRequestExpired(identity: $identity, threshold: $threshold)")
        return isRequestBefore(identity, threshold.inPast())
    }

    /**
     * Checks if the given [identity] has been requested before, regardless of when
     */
    override suspend fun hasBeenRequested(
        identity: CacheIdentity
    ): Boolean = withContext(Dispatchers.IO) {
        val requestCount = localSource.countMatching(request, identity.id)
        val exists = requestCount > 0
        Timber.v("hasBeenRequested(identity: $identity) -> $exists")
        exists
    }

    /**
     * Updates the last request [timestamp] for the given [identity]
     */
    override suspend fun updateLastRequest(identity: CacheIdentity, timestamp: Instant) {
        withContext(Dispatchers.IO) {
            val entity = CacheEntity(
                request = request,
                cacheItemId = identity.id,
                timestamp = timestamp
            )
            Timber.v("updateLastRequest(identity: $identity, timestamp: $timestamp)")
            localSource.insertOrUpdate(entity)
        }
    }

    /**
     * Invalidates cache records for the given [identity]
     */
    override suspend fun invalidateLastRequest(identity: CacheIdentity) {
        Timber.v("invalidateLastRequest(identity: $identity)")
        updateLastRequest(identity, Instant.EPOCH)
    }
}