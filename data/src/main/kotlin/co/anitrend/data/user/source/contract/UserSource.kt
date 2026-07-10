/*
 * Copyright (C) 2021 AniTrend
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
package co.anitrend.data.user.source.contract

import androidx.paging.PagingData
import co.anitrend.arch.data.source.contract.IDataSource
import co.anitrend.arch.request.callback.RequestCallback
import co.anitrend.arch.request.model.Request
import co.anitrend.data.android.cache.extensions.invoke
import co.anitrend.data.android.cache.model.CacheIdentity
import co.anitrend.data.android.cache.repository.contract.ICacheStorePolicy
import co.anitrend.data.android.extensions.invoke
import co.anitrend.data.android.source.AbstractCoreDataSource
import co.anitrend.data.auth.settings.IAuthenticationSettings
import co.anitrend.data.user.cache.UserCache
import co.anitrend.domain.user.entity.User
import co.anitrend.domain.user.entity.profile.ProfileFeed
import co.anitrend.domain.user.entity.profile.ProfileOverview
import co.anitrend.domain.user.model.UserParam
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

internal class UserSource {
    abstract class Identifier : AbstractCoreDataSource() {
        protected lateinit var cacheIdentity: CacheIdentity

        protected abstract val cachePolicy: ICacheStorePolicy

        protected lateinit var query: UserParam.Identifier

        protected abstract fun observable(): Flow<User>

        protected abstract suspend fun getUser(callback: RequestCallback): Boolean

        operator fun invoke(param: UserParam.Identifier): Flow<User> {
            query = param
            cacheIdentity = UserCache.Identifier.Identity(query)
            cachePolicy(
                scope = scope,
                requestHelper = requestHelper,
                cacheIdentity = cacheIdentity,
                block = ::getUser,
            )
            return observable()
        }
    }

    abstract class Viewer : AbstractCoreDataSource() {
        protected lateinit var cacheIdentity: CacheIdentity

        protected abstract val settings: IAuthenticationSettings

        protected abstract val cachePolicy: ICacheStorePolicy

        protected val query: UserParam.Viewer
            get() {
                val userId = settings.authenticatedUserId.value
                return UserParam.Viewer(userId)
            }

        protected abstract fun observable(): Flow<User>

        protected abstract suspend fun getProfile(callback: RequestCallback): Boolean

        operator fun invoke(): Flow<User> {
            if (settings.isAuthenticated.value) {
                require(query.id != IAuthenticationSettings.INVALID_USER_ID) {
                    "User id for supplied query is invalid"
                }
                cacheIdentity = UserCache.Viewer.Identity(query.id)
                cachePolicy(
                    scope = scope,
                    requestHelper = requestHelper,
                    cacheIdentity = cacheIdentity,
                    block = ::getProfile,
                )
            }
            return observable()
        }
    }

    abstract class Profile : AbstractCoreDataSource() {
        protected lateinit var query: UserParam.Profile

        protected lateinit var cacheIdentity: CacheIdentity

        protected abstract val cachePolicy: ICacheStorePolicy

        protected abstract fun observable(): Flow<User>

        protected abstract suspend fun getProfile(callback: RequestCallback): Boolean

        suspend operator fun invoke(param: UserParam.Profile): Flow<User> {
            query = param
            cacheIdentity = UserCache.Profile.Identity(param)
            cachePolicy(
                scope = scope,
                requestHelper = requestHelper,
                cacheIdentity = cacheIdentity,
                block = ::getProfile,
            )
            return observable()
        }
    }

    abstract class Paging {
        protected lateinit var query: UserParam.Search

        abstract operator fun invoke(param: UserParam.Search): Flow<PagingData<User>>

        abstract val pagingMediator: IDataSource

        protected fun assignQuery(param: UserParam.Search) {
            query = param
        }
    }

    abstract class Statistic : AbstractCoreDataSource() {
        protected lateinit var query: UserParam.Statistic

        protected lateinit var cacheIdentity: CacheIdentity

        protected abstract val cachePolicy: ICacheStorePolicy

        protected abstract fun observable(): Flow<User.WithStats>

        protected abstract suspend fun getProfileStatistic(callback: RequestCallback): Boolean

        suspend operator fun invoke(param: UserParam.Statistic): Flow<User.WithStats> {
            query = param
            cacheIdentity = UserCache.Statistic.Identity(param)
            cachePolicy(
                scope = scope,
                requestHelper = requestHelper,
                cacheIdentity = cacheIdentity,
                block = ::getProfileStatistic,
            )
            return observable()
        }
    }

    abstract class Overview : AbstractCoreDataSource() {
        protected lateinit var query: UserParam.Overview

        protected lateinit var cacheIdentity: CacheIdentity

        protected abstract val cachePolicy: ICacheStorePolicy

        protected abstract fun observable(): Flow<ProfileOverview>

        protected abstract suspend fun getProfileOverview(callback: RequestCallback): Boolean

        suspend operator fun invoke(param: UserParam.Overview): Flow<ProfileOverview> {
            query = param
            cacheIdentity = UserCache.Overview.Identity(param)
            cachePolicy(
                scope = scope,
                requestHelper = requestHelper,
                cacheIdentity = cacheIdentity,
                block = ::getProfileOverview,
            )
            return observable()
        }
    }

    abstract class Feed : AbstractCoreDataSource() {
        protected lateinit var query: UserParam.Feed

        protected lateinit var cacheIdentity: CacheIdentity

        protected abstract val cachePolicy: ICacheStorePolicy

        protected abstract fun observable(): Flow<ProfileFeed>

        protected abstract suspend fun getProfileFeed(callback: RequestCallback): Boolean

        suspend operator fun invoke(param: UserParam.Feed): Flow<ProfileFeed> {
            query = param
            cacheIdentity = UserCache.Feed.Identity(param)
            cachePolicy(
                scope = scope,
                requestHelper = requestHelper,
                cacheIdentity = cacheIdentity,
                block = ::getProfileFeed,
            )
            return observable()
        }
    }

    abstract class ToggleFollow : AbstractCoreDataSource() {
        protected lateinit var query: UserParam.ToggleFollow

        protected abstract fun observable(): Flow<User>

        protected abstract suspend fun toggleFollow(callback: RequestCallback)

        operator fun invoke(param: UserParam.ToggleFollow): Flow<User> {
            query = param
            invoke(block = ::toggleFollow)
            return observable()
        }

        /**
         * Clears data sources (databases, preferences, e.t.c)
         *
         * @param context Dispatcher context to run in
         */
        override suspend fun clearDataSource(context: CoroutineDispatcher) {
            // not supported
        }
    }

    abstract class Update : AbstractCoreDataSource() {
        protected lateinit var query: UserParam.Update

        protected abstract fun observable(): Flow<User>

        protected abstract suspend fun updateProfile(callback: RequestCallback)

        operator fun invoke(param: UserParam.Update): Flow<User> {
            query = param
            invoke(block = ::updateProfile)
            return observable()
        }

        /**
         * Clears data sources (databases, preferences, e.t.c)
         *
         * @param context Dispatcher context to run in
         */
        override suspend fun clearDataSource(context: CoroutineDispatcher) {
            // not supported
        }
    }
}
