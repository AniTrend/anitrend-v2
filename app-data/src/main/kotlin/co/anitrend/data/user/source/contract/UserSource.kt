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

package co.anitrend.data.user.source.contract

import androidx.paging.PagedList
import co.anitrend.arch.data.request.callback.RequestCallback
import co.anitrend.arch.data.request.model.Request
import co.anitrend.arch.data.source.core.SupportCoreDataSource
import co.anitrend.arch.data.source.paging.SupportPagingDataSource
import co.anitrend.data.android.cache.extensions.invoke
import co.anitrend.data.android.cache.model.CacheIdentity
import co.anitrend.data.android.cache.repository.contract.ICacheStorePolicy
import co.anitrend.data.android.extensions.invoke
import co.anitrend.data.auth.settings.IAuthenticationSettings
import co.anitrend.data.user.cache.UserCache
import co.anitrend.data.user.model.mutation.UserMutation
import co.anitrend.data.user.model.query.UserQuery
import co.anitrend.domain.user.entity.User
import co.anitrend.domain.user.model.UserParam
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

internal class UserSource {

    abstract class Identifier : SupportCoreDataSource() {

        protected lateinit var cacheIdentity: CacheIdentity

        protected abstract val cachePolicy: ICacheStorePolicy

        protected lateinit var query: UserQuery.Identifier

        protected abstract fun observable(): Flow<User>

        protected abstract suspend fun getUser(callback: RequestCallback): Boolean

        operator fun invoke(param: UserParam.Identifier): Flow<User> {
            query = UserQuery.Identifier(param)
            cacheIdentity = UserCache.Identifier.Identity(query.param)
            cachePolicy(
                scope = scope,
                requestHelper = requestHelper,
                cacheIdentity = cacheIdentity,
                block = ::getUser
            )
            return observable()
        }
    }

    abstract class Viewer : SupportCoreDataSource() {

        protected lateinit var cacheIdentity: CacheIdentity

        protected abstract val settings: IAuthenticationSettings

        protected abstract val cachePolicy: ICacheStorePolicy

        protected val query: UserQuery.Viewer
            get() {
                val userId = settings.authenticatedUserId.value
                return UserQuery.Viewer(
                    UserParam.Viewer(userId)
                )
            }

        protected abstract fun observable(): Flow<User>

        protected abstract suspend fun getProfile(callback: RequestCallback): Boolean

        operator fun invoke(): Flow<User> {
            require(query.isUserIdValid()) {
                "User id for supplied query is invalid"
            }
            cacheIdentity = UserCache.Viewer.Identity(query.param.id)
            cachePolicy(
                scope = scope,
                requestHelper = requestHelper,
                cacheIdentity = cacheIdentity,
                block = ::getProfile
            )
            return observable()
        }
    }

    abstract class Search : SupportPagingDataSource<User>() {

        protected lateinit var query: UserQuery.Search

        protected abstract fun observable(): Flow<PagedList<User>>

        protected abstract suspend fun getUsers(callback: RequestCallback)

        operator fun invoke(param: UserParam.Search): Flow<PagedList<User>> {
            query = UserQuery.Search(param)
            return observable()
        }

        /**
         * Called when the item at the end of the PagedList has been loaded, and access has
         * occurred within [Config.prefetchDistance] of it.
         *
         * No more data will be appended to the PagedList after this item.
         *
         * @param itemAtEnd The first item of PagedList
         */
        override fun onItemAtEndLoaded(itemAtEnd: User) {
            invoke(
                paging = supportPagingHelper,
                requestType = Request.Type.AFTER,
                block = ::getUsers
            )
        }

        /**
         * Called when the item at the front of the PagedList has been loaded, and access has
         * occurred within [Config.prefetchDistance] of it.
         *
         * No more data will be prepended to the PagedList before this item.
         *
         * @param itemAtFront The first item of PagedList
         */
        override fun onItemAtFrontLoaded(itemAtFront: User) {
            /*invoke(
                paging = supportPagingHelper,
                requestType = Request.Type.BEFORE,
                block = ::getUsers
            )*/
        }

        /**
         * Called when zero items are returned from an initial load of the PagedList's data source.
         */
        override fun onZeroItemsLoaded() {
            invoke(
                paging = supportPagingHelper,
                block = ::getUsers
            )
        }
    }

    abstract class Profile : SupportCoreDataSource() {

        protected lateinit var query: UserQuery.Profile

        protected lateinit var cacheIdentity: CacheIdentity

        protected abstract val cachePolicy: ICacheStorePolicy

        protected abstract fun observable(): Flow<User>

        protected abstract suspend fun getProfile(callback: RequestCallback): Boolean

        operator fun invoke(param: UserParam.Profile): Flow<User> {
            query = UserQuery.Profile(param)
            cacheIdentity = UserCache.Profile.Identity(param)
            cachePolicy(
                scope = scope,
                requestHelper = requestHelper,
                cacheIdentity = cacheIdentity,
                block = ::getProfile
            )
            return observable()
        }
    }

    abstract class Statistic : SupportCoreDataSource() {

        protected lateinit var query: UserQuery.Statistic

        protected lateinit var cacheIdentity: CacheIdentity

        protected abstract val cachePolicy: ICacheStorePolicy

        protected abstract fun observable(): Flow<User.WithStats>

        protected abstract suspend fun getProfileStatistic(callback: RequestCallback): Boolean

        operator fun invoke(param: UserParam.Statistic): Flow<User.WithStats> {
            query = UserQuery.Statistic(param)
            cacheIdentity = UserCache.Statistic.Identity(param)
            cachePolicy(
                scope = scope,
                requestHelper = requestHelper,
                cacheIdentity = cacheIdentity,
                block = ::getProfileStatistic
            )
            return observable()
        }
    }

    abstract class ToggleFollow : SupportCoreDataSource() {

        protected lateinit var query: UserMutation.ToggleFollow

        protected abstract fun observable(): Flow<User>

        protected abstract suspend fun toggleFollow(callback: RequestCallback)

        operator fun invoke(param: UserParam.ToggleFollow): Flow<User> {
            query = UserMutation.ToggleFollow(param)
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

    abstract class Update : SupportCoreDataSource() {

        protected lateinit var query: UserMutation.Update

        protected abstract fun observable(): Flow<User>

        protected abstract suspend fun updateProfile(callback: RequestCallback)

        operator fun invoke(param: UserParam.Update): Flow<User> {
            query = UserMutation.Update(param)
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
