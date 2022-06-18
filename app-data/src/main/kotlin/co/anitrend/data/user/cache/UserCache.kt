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

import co.anitrend.data.android.cache.datasource.CacheLocalSource
import co.anitrend.data.android.cache.helper.instantInPast
import co.anitrend.data.android.cache.model.CacheIdentity
import co.anitrend.data.android.cache.model.CacheRequest
import co.anitrend.data.android.cache.repository.CacheStorePolicy
import co.anitrend.data.core.extensions.toHashId
import co.anitrend.domain.user.model.UserParam
import org.threeten.bp.Instant

internal sealed class UserCache : CacheStorePolicy() {

    class Identifier(
        override val localSource: CacheLocalSource,
        override val request: CacheRequest = CacheRequest.USER_ID
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
            val param: UserParam.Identifier,
            override val id: Long = param.let {
                it.id ?: requireNotNull(it.name).toHashId()
            },
            override val key: String = "user_id",
            override val expiresAt: Instant = instantInPast(minutes = 5)
        ) : CacheIdentity
    }

    class Viewer(
        override val localSource: CacheLocalSource,
        override val request: CacheRequest = CacheRequest.VIEWER
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
            override val id: Long,
            override val key: String = "viewer",
            override val expiresAt: Instant = instantInPast(minutes = 5)
        ) : CacheIdentity
    }

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
            override val id: Long = when {
                param.id != null -> requireNotNull(param.id)
                else -> requireNotNull(param.name?.toHashId())
            },
            override val key: String = "user_profile",
            override val expiresAt: Instant = instantInPast(minutes = 5)
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
            override val expiresAt: Instant = instantInPast(hours = 1)
        ) : CacheIdentity
    }
}
