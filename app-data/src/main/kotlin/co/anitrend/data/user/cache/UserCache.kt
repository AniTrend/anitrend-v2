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

package co.anitrend.data.user.cache

import co.anitrend.data.cache.datasource.CacheLocalSource
import co.anitrend.data.cache.helper.instantInFuture
import co.anitrend.data.cache.helper.instantInPast
import co.anitrend.data.cache.model.CacheIdentity
import co.anitrend.data.cache.model.CacheRequest
import co.anitrend.data.cache.repository.CacheStorePolicy
import co.anitrend.domain.user.model.UserParam
import org.threeten.bp.Instant

internal sealed class UserCache : CacheStorePolicy() {

    class Profile(
        override val localSource: CacheLocalSource,
        override val request: CacheRequest = CacheRequest.USER
    ) : UserCache() {
        /**
         * Check if a resource with a given [identity] is permitted to refresh
         *
         * @param identity Unique identifier for the cache item
         * @param expiresAfter Expiry time fro the cached [identity]
         */
        override suspend fun shouldRefresh(
            identity: CacheIdentity,
            expiresAfter: Instant
        ): Boolean = isRequestBefore(identity, expiresAfter)

        class Identity(
            val param: UserParam.Profile,
            override val id: Long = param.id,
            override val key: String = "user_profile",
            override val expiresAt: Instant = instantInFuture(minutes = 5)
        ) : CacheIdentity
    }

    class Statistic(
        override val localSource: CacheLocalSource,
        override val request: CacheRequest = CacheRequest.STATISTIC
    ) : UserCache() {
        /**
         * Check if a resource with a given [identity] is permitted to refresh
         *
         * @param identity Unique identifier for the cache item
         * @param expiresAfter Expiry time fro the cached [identity]
         */
        override suspend fun shouldRefresh(
            identity: CacheIdentity,
            expiresAfter: Instant
        ): Boolean = isRequestBefore(identity, expiresAfter)

        class Identity(
            val param: UserParam.Statistic,
            override val id: Long = param.id,
            override val key: String = "user_profile_statistic",
            override val expiresAt: Instant = instantInFuture(hours = 6)
        ) : CacheIdentity
    }
}
