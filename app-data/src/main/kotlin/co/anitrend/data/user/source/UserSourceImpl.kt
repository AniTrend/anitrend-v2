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

package co.anitrend.data.user.source

import androidx.paging.PagedList
import co.anitrend.arch.extension.dispatchers.contract.ISupportDispatcher
import co.anitrend.arch.paging.legacy.FlowPagedListBuilder
import co.anitrend.arch.paging.legacy.util.PAGING_CONFIGURATION
import co.anitrend.arch.request.callback.RequestCallback
import co.anitrend.data.android.cache.repository.contract.ICacheStorePolicy
import co.anitrend.data.android.cleaner.contract.IClearDataHelper
import co.anitrend.data.android.extensions.deferred
import co.anitrend.data.auth.settings.IAuthenticationSettings
import co.anitrend.data.common.extension.from
import co.anitrend.data.user.UserAuthController
import co.anitrend.data.user.UserController
import co.anitrend.data.user.UserPagedController
import co.anitrend.data.user.UserProfileController
import co.anitrend.data.user.UserProfileStatisticController
import co.anitrend.data.user.converter.UserEntityConverter
import co.anitrend.data.user.converter.UserViewEntityConverter
import co.anitrend.data.user.datasource.local.UserLocalSource
import co.anitrend.data.user.datasource.remote.UserRemoteSource
import co.anitrend.data.user.source.contract.UserSource
import co.anitrend.data.util.GraphUtil.toQueryContainerBuilder
import co.anitrend.domain.user.entity.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

internal class UserSourceImpl {

    class Identifier(
        private val remoteSource: UserRemoteSource,
        private val localSource: UserLocalSource,
        private val clearDataHelper: IClearDataHelper,
        private val controller: UserController,
        private val converter: UserEntityConverter,
        override val cachePolicy: ICacheStorePolicy,
        override val dispatcher: ISupportDispatcher
    ) : UserSource.Identifier() {

        override fun observable(): Flow<User> {
            val source = query.param.id?.let {
                localSource.userByIdFlow(it)
            } ?: localSource.userByNameFlow(query.param.name)

            return source
                .flowOn(dispatcher.io)
                .filterNotNull()
                .map(converter::convertFrom)
                .distinctUntilChanged()
                .flowOn(dispatcher.computation)
        }

        override suspend fun getUser(callback: RequestCallback): Boolean {
            val deferred = deferred {
                val queryBuilder = query.toQueryContainerBuilder()
                remoteSource.getUserByName(queryBuilder)
            }

            val result = controller(deferred, callback)

            return result != null
        }

        /**
         * Clears data sources (databases, preferences, e.t.c)
         *
         * @param context Dispatcher context to run in
         */
        override suspend fun clearDataSource(context: CoroutineDispatcher) {
            clearDataHelper(context) {
                cachePolicy.invalidateLastRequest(cacheIdentity)
                localSource.clearByUserName(query.param.name)
            }
        }
    }

    class Viewer(
        private val remoteSource: UserRemoteSource,
        private val localSource: UserLocalSource,
        private val clearDataHelper: IClearDataHelper,
        private val controller: UserAuthController,
        private val converter: UserViewEntityConverter,
        override val settings: IAuthenticationSettings,
        override val cachePolicy: ICacheStorePolicy,
        override val dispatcher: ISupportDispatcher
    ) : UserSource.Viewer() {

        override fun observable(): Flow<User> {
            val userId = query.param.id

            return localSource.userAuthenticated(userId)
                .flowOn(dispatcher.io)
                .filterNotNull()
                .map(converter::convertFrom)
                .distinctUntilChanged()
                .flowOn(dispatcher.computation)
        }

        override suspend fun getProfile(callback: RequestCallback): Boolean {
            val deferred = deferred {
                val queryBuilder = query.toQueryContainerBuilder()
                remoteSource.getUserViewer(queryBuilder)
            }

            val result = controller(deferred, callback)

            return result != null
        }

        /**
         * Clears data sources (databases, preferences, e.t.c)
         *
         * @param context Dispatcher context to run in
         */
        override suspend fun clearDataSource(context: CoroutineDispatcher) {
            clearDataHelper(context) {
                cachePolicy.invalidateLastRequest(cacheIdentity)
                localSource.clearById(requireNotNull(query.param.id))
            }
        }
    }

    class Search(
        private val remoteSource: UserRemoteSource,
        private val localSource: UserLocalSource,
        private val clearDataHelper: IClearDataHelper,
        private val controller: UserPagedController,
        private val converter: UserEntityConverter,
        override val dispatcher: ISupportDispatcher
    ) : UserSource.Search() {

        override fun observable(): Flow<PagedList<User>> {
            val dataSourceFactory = localSource
                .entrySearchFactory(query.param.search)
                .map(converter::convertFrom)

            return FlowPagedListBuilder(
                dataSourceFactory,
                PAGING_CONFIGURATION,
                null,
                this
            ).buildFlow()
        }

        override suspend fun getUsers(callback: RequestCallback) {
            val deferred = deferred {
                val queryBuilder = query.toQueryContainerBuilder(
                    supportPagingHelper
                )
                remoteSource.getUserPaged(queryBuilder)
            }

            controller(deferred, callback) {
                supportPagingHelper.from(it.page)
                it
            }
        }

        /**
         * Clears data sources (databases, preferences, e.t.c)
         *
         * @param context Dispatcher context to run in
         */
        override suspend fun clearDataSource(context: CoroutineDispatcher) {
            clearDataHelper(context) {
                localSource.clearByMatch(
                    query.param.search
                )
            }
        }
    }

    class Profile(
        private val remoteSource: UserRemoteSource,
        private val localSource: UserLocalSource,
        private val clearDataHelper: IClearDataHelper,
        private val controller: UserProfileController,
        private val converter: UserViewEntityConverter,
        override val cachePolicy: ICacheStorePolicy,
        override val dispatcher: ISupportDispatcher
    ) : UserSource.Profile() {

        override fun observable(): Flow<User> {
            val result = if (query.param.id != null)
                localSource.userByIdWithOptionsFlow(requireNotNull(query.param.id))
            else
                localSource.userByNameWithOptionsFlow(requireNotNull(query.param.name))

            return result
                .flowOn(dispatcher.io)
                .filterNotNull()
                .map(converter::convertFrom)
                .distinctUntilChanged()
                .flowOn(dispatcher.computation)
        }

        override suspend fun getProfile(callback: RequestCallback): Boolean {
            val deferred = deferred {
                val queryBuilder = query.toQueryContainerBuilder()
                remoteSource.getUserProfile(queryBuilder)
            }

            val result = controller(deferred, callback)

            return result != null
        }

        /**
         * Clears data sources (databases, preferences, e.t.c)
         *
         * @param context Dispatcher context to run in
         */
        override suspend fun clearDataSource(context: CoroutineDispatcher) {
            clearDataHelper(context) {
                cachePolicy.invalidateLastRequest(cacheIdentity)
                if (query.param.id != null)
                    localSource.clearById(requireNotNull(query.param.id))
                else
                    localSource.clearByUserName(requireNotNull(query.param.name))
            }
        }
    }

    class Statistic(
        private val remoteSource: UserRemoteSource,
        private val localSource: UserLocalSource,
        private val clearDataHelper: IClearDataHelper,
        private val controller: UserProfileStatisticController,
        private val converter: UserViewEntityConverter,
        override val cachePolicy: ICacheStorePolicy,
        override val dispatcher: ISupportDispatcher
    ) : UserSource.Statistic() {

        override fun observable(): Flow<User.WithStats> {
            return localSource.userByIdWithStatisticFlow(query.param.id)
                .flowOn(dispatcher.io)
                .filterNotNull()
                .map(converter::convertFrom)
                .map { it as User.WithStats }
                .distinctUntilChanged()
                .flowOn(dispatcher.computation)
        }

        override suspend fun getProfileStatistic(callback: RequestCallback): Boolean {
            val deferred = deferred {
                val queryBuilder = query.toQueryContainerBuilder()
                remoteSource.getUserWithStatistic(queryBuilder)
            }

            val result = controller(deferred, callback)

            return result != null
        }

        /**
         * Clears data sources (databases, preferences, e.t.c)
         *
         * @param context Dispatcher context to run in
         */
        override suspend fun clearDataSource(context: CoroutineDispatcher) {
            clearDataHelper(context) {
                cachePolicy.invalidateLastRequest(cacheIdentity)
                localSource.clearById(query.param.id)
            }
        }
    }

    class ToggleFollow(
        private val remoteSource: UserRemoteSource,
        private val localSource: UserLocalSource,
        private val controller: UserController,
        private val converter: UserEntityConverter,
        override val dispatcher: ISupportDispatcher
    ) : UserSource.ToggleFollow() {

        override fun observable(): Flow<User> {
            return localSource.userByIdFlow(query.param.userId)
                .flowOn(dispatcher.io)
                .filterNotNull()
                .map(converter::convertFrom)
                .distinctUntilChanged()
                .flowOn(dispatcher.computation)
        }

        override suspend fun toggleFollow(callback: RequestCallback) {
            val deferred = deferred {
                val queryBuilder = query.toQueryContainerBuilder()
                remoteSource.saveToggleFollow(queryBuilder)
            }

            controller(deferred, callback)
        }
    }

    class Update(
        private val remoteSource: UserRemoteSource,
        private val localSource: UserLocalSource,
        private val controller: UserProfileController,
        private val converter: UserEntityConverter,
        private val settings: IAuthenticationSettings,
        override val dispatcher: ISupportDispatcher
    ) : UserSource.Update() {

        override fun observable(): Flow<User> {
            val userId = settings.authenticatedUserId.value
            return localSource.userByIdFlow(userId)
                .flowOn(dispatcher.io)
                .filterNotNull()
                .map(converter::convertFrom)
                .distinctUntilChanged()
                .flowOn(dispatcher.computation)
        }

        override suspend fun updateProfile(callback: RequestCallback) {
            val deferred = deferred {
                val queryBuilder = query.toQueryContainerBuilder()
                remoteSource.updateUserProfile(queryBuilder)
            }

            controller(deferred, callback)
        }
    }
}
