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
package co.anitrend.data.user.source

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import co.anitrend.arch.data.source.contract.IDataSource
import co.anitrend.arch.extension.dispatchers.contract.ISupportDispatcher
import co.anitrend.arch.extension.util.DEFAULT_PAGE_SIZE
import co.anitrend.arch.request.callback.RequestCallback
import co.anitrend.data.android.cache.repository.contract.ICacheStorePolicy
import co.anitrend.data.android.cleaner.contract.IClearDataHelper
import co.anitrend.data.android.extensions.deferred
import co.anitrend.data.auth.settings.IAuthenticationSettings
import co.anitrend.data.android.controller.graphql.GraphQLController
import co.anitrend.data.common.extension.from
import co.anitrend.data.graphql.anilist.GetUser
import co.anitrend.data.graphql.anilist.GetUserProfile
import co.anitrend.data.graphql.anilist.GetUserProfileFeed
import co.anitrend.data.graphql.anilist.GetUserProfileOverview
import co.anitrend.data.graphql.anilist.GetUserViewer
import co.anitrend.data.graphql.anilist.GetUserWithStatistic
import co.anitrend.data.graphql.anilist.ListActivityOptionInput
import co.anitrend.data.graphql.anilist.MediaListOptionsInput
import co.anitrend.data.graphql.anilist.SaveToggleFollowUser
import co.anitrend.data.graphql.anilist.UpdateUserProfile
import co.anitrend.data.user.UserAuthController
import co.anitrend.data.user.UserController
import co.anitrend.data.user.UserPagedController
import co.anitrend.data.user.UserProfileFeedController
import co.anitrend.data.user.UserProfileOverviewController
import co.anitrend.data.user.UserProfileController
import co.anitrend.data.user.cache.UserCache
import co.anitrend.data.user.converter.UserEntityConverter
import co.anitrend.data.user.converter.UserProfileFeedConverter
import co.anitrend.data.user.converter.UserProfileOverviewConverter
import co.anitrend.data.user.converter.UserStatisticPayload
import co.anitrend.data.user.entity.filter.UserQueryFilter
import co.anitrend.data.status.datasource.local.StatusLocalSource
import co.anitrend.data.user.datasource.local.connection.UserProfileFavouriteMediaLocalSource
import co.anitrend.data.user.datasource.local.connection.UserProfileReviewLocalSource
import co.anitrend.data.user.datasource.local.UserLocalSource
import co.anitrend.data.user.datasource.remote.UserRemoteSource
import co.anitrend.data.user.model.container.UserModelContainer
import co.anitrend.data.user.converter.UserViewEntityConverter
import co.anitrend.data.user.source.contract.UserSource
import co.anitrend.domain.common.sort.order.SortOrder
import co.anitrend.domain.user.entity.User
import co.anitrend.domain.user.entity.profile.ProfileFeed
import co.anitrend.domain.user.entity.profile.ProfileOverview
import co.anitrend.domain.user.model.UserParam
import co.anitrend.retrofit.graphql.model.EmptyGraphQLVariables
import co.anitrend.retrofit.graphql.model.request.GraphQLOperationRequest
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
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
        override val dispatcher: ISupportDispatcher,
    ) : UserSource.Identifier() {
        override fun observable(): Flow<User> {
            val source =
                query.id?.let {
                    localSource.userByIdFlow(it)
                } ?: localSource.userByNameFlow(query.name)

            return source
                .flowOn(dispatcher.io)
                .filterNotNull()
                .map(converter::convertFrom)
                .distinctUntilChanged()
                .flowOn(dispatcher.computation)
        }

        override suspend fun getUser(callback: RequestCallback): Boolean {
            val deferred =
                deferred {
                    val request =
                        GetUser.request(
                            id = query.id?.toInt(),
                            userName = query.name,
                        )
                    remoteSource.getUserByName(request)
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
                localSource.clearByUserName(query.name)
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
        override val dispatcher: ISupportDispatcher,
    ) : UserSource.Viewer() {
        override fun observable(): Flow<User> {
            val userId = query.id

            return localSource
                .userAuthenticated(userId)
                .flowOn(dispatcher.io)
                .filterNotNull()
                .map(converter::convertFrom)
                .distinctUntilChanged()
                .flowOn(dispatcher.computation)
        }

        override suspend fun getProfile(callback: RequestCallback): Boolean {
            val deferred =
                deferred {
                    val request =
                        GraphQLOperationRequest<EmptyGraphQLVariables>(
                            query = GetUserViewer.document,
                            operationName = GetUserViewer.name,
                        )
                    remoteSource.getUserViewer(request)
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
                localSource.clearById(requireNotNull(query.id))
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
        override val dispatcher: ISupportDispatcher,
    ) : UserSource.Profile() {
        override fun observable(): Flow<User> {
            val result =
                if (query.id != null) {
                    localSource.userByIdWithOptionsFlow(requireNotNull(query.id))
                } else {
                    localSource.userByNameWithOptionsFlow(requireNotNull(query.name))
                }

            return result
                .flowOn(dispatcher.io)
                .filterNotNull()
                .map(converter::convertFrom)
                .distinctUntilChanged()
                .flowOn(dispatcher.computation)
        }

        override suspend fun getProfile(callback: RequestCallback): Boolean {
            val deferred =
                deferred {
                    val request =
                        GetUserProfile.request(
                            id = query.id?.toInt(),
                            userName = query.name,
                        )
                    remoteSource.getUserProfile(request)
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
                if (query.id != null) {
                    localSource.clearById(requireNotNull(query.id))
                } else {
                    localSource.clearByUserName(requireNotNull(query.name))
                }
            }
        }
    }

    class Paging(
        private val remoteSource: UserRemoteSource,
        private val localSource: UserLocalSource,
        private val controller: UserPagedController,
        private val converter: UserEntityConverter,
        private val clearDataHelper: IClearDataHelper,
        private val filter: UserQueryFilter.Search,
        private val dispatcher: ISupportDispatcher,
    ) : UserSource.Paging() {
        private val _pagingMediator =
            UserPagingSource(
                cacheIdentity = UserCache.Paged.Identity(UserParam.Search("")),
                remoteSource = remoteSource,
                localSource = localSource,
                controller = controller,
                clearDataHelper = clearDataHelper,
                filter = filter,
                query = UserParam.Search(""),
                dispatcher = dispatcher,
            )

        override val pagingMediator: IDataSource
            get() = _pagingMediator

        override fun invoke(param: UserParam.Search): Flow<PagingData<User>> {
            assignQuery(param)
            _pagingMediator.apply {
                cacheIdentity = UserCache.Paged.Identity(param)
                query = param
            }

            return Pager(
                config =
                    PagingConfig(
                        pageSize = DEFAULT_PAGE_SIZE,
                        initialLoadSize = DEFAULT_PAGE_SIZE,
                        prefetchDistance = DEFAULT_PAGE_SIZE,
                        enablePlaceholders = false,
                    ),
                remoteMediator = _pagingMediator,
                pagingSourceFactory = _pagingMediator.pagingSourceFactory(),
            ).flow.map { pagingData -> pagingData.map { entity -> converter.convertFrom(entity) } }
        }
    }

    class Statistic(
        private val remoteSource: UserRemoteSource,
        private val localSource: UserLocalSource,
        private val clearDataHelper: IClearDataHelper,
        private val controller: GraphQLController<UserModelContainer.WithStatistic, UserStatisticPayload>,
        private val converter: UserViewEntityConverter,
        override val cachePolicy: ICacheStorePolicy,
        override val dispatcher: ISupportDispatcher,
    ) : UserSource.Statistic() {
        override fun observable(): Flow<User.WithStats> =
            localSource
                .userByIdWithStatisticFlow(query.id)
                .flowOn(dispatcher.io)
                .filterNotNull()
                .map(converter::convertFrom)
                .map { it as User.WithStats }
                .distinctUntilChanged()
                .flowOn(dispatcher.computation)

        override suspend fun getProfileStatistic(callback: RequestCallback): Boolean {
            val deferred =
                deferred {
                    val request =
                        GetUserWithStatistic.request(
                            id = query.id.toInt(),
                            statisticsSort =
                                query.statisticsSort?.map {
                                    val baseName = (it.sortable as Enum<*>).name
                                    val enumName =
                                        if (it.order == SortOrder.DESC) baseName + "_DESC" else baseName
                                    co.anitrend.data.graphql.anilist.UserStatisticsSort
                                        .valueOf(enumName)
                                },
                        )
                    remoteSource.getUserWithStatistic(request)
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
                localSource.clearById(query.id)
            }
        }
    }

    class Overview(
        private val remoteSource: UserRemoteSource,
        private val favouriteMediaLocalSource: UserProfileFavouriteMediaLocalSource,
        private val statusLocalSource: StatusLocalSource,
        private val clearDataHelper: IClearDataHelper,
        private val controller: UserProfileOverviewController,
        override val cachePolicy: ICacheStorePolicy,
        override val dispatcher: ISupportDispatcher,
    ) : UserSource.Overview() {
        override fun observable(): Flow<ProfileOverview> =
            favouriteMediaLocalSource
                .entryByUserIdFlow(query.id)
                .combine(statusLocalSource.listStatusByUserIdFlow(query.id)) { favourites, activities ->
                    UserProfileOverviewConverter.toProfileOverview(favourites, activities)
                }.flowOn(dispatcher.io)
                .distinctUntilChanged()
                .flowOn(dispatcher.computation)

        override suspend fun getProfileOverview(callback: RequestCallback): Boolean {
            val deferred =
                deferred {
                    val request =
                        GetUserProfileOverview.request(
                            id = query.id.toInt(),
                        )
                    remoteSource.getUserProfileOverview(request)
                }

            return controller(deferred, callback) != null
        }

        override suspend fun clearDataSource(context: CoroutineDispatcher) {
            clearDataHelper(context) {
                cachePolicy.invalidateLastRequest(cacheIdentity)
                favouriteMediaLocalSource.clearByUserId(query.id)
                statusLocalSource.clearListStatusByUserId(query.id)
            }
        }
    }

    class Feed(
        private val remoteSource: UserRemoteSource,
        private val reviewLocalSource: UserProfileReviewLocalSource,
        private val statusLocalSource: StatusLocalSource,
        private val clearDataHelper: IClearDataHelper,
        private val controller: UserProfileFeedController,
        override val cachePolicy: ICacheStorePolicy,
        override val dispatcher: ISupportDispatcher,
    ) : UserSource.Feed() {
        override fun observable(): Flow<ProfileFeed> =
            reviewLocalSource
                .entryByUserIdFlow(query.id)
                .combine(statusLocalSource.listStatusByUserIdFlow(query.id)) { reviews, activities ->
                    UserProfileFeedConverter.toProfileFeed(reviews, activities)
                }.flowOn(dispatcher.io)
                .distinctUntilChanged()
                .flowOn(dispatcher.computation)

        override suspend fun getProfileFeed(callback: RequestCallback): Boolean {
            val deferred =
                deferred {
                    val request =
                        GetUserProfileFeed.request(
                            id = query.id.toInt(),
                        )
                    remoteSource.getUserProfileFeed(request)
                }

            return controller(deferred, callback) != null
        }

        override suspend fun clearDataSource(context: CoroutineDispatcher) {
            clearDataHelper(context) {
                cachePolicy.invalidateLastRequest(cacheIdentity)
                reviewLocalSource.clearByUserId(query.id)
                statusLocalSource.clearListStatusByUserId(query.id)
            }
        }
    }

    class ToggleFollow(
        private val remoteSource: UserRemoteSource,
        private val localSource: UserLocalSource,
        private val controller: UserController,
        private val converter: UserEntityConverter,
        override val dispatcher: ISupportDispatcher,
    ) : UserSource.ToggleFollow() {
        override fun observable(): Flow<User> =
            localSource
                .userByIdFlow(query.userId)
                .flowOn(dispatcher.io)
                .filterNotNull()
                .map(converter::convertFrom)
                .distinctUntilChanged()
                .flowOn(dispatcher.computation)

        override suspend fun toggleFollow(callback: RequestCallback) {
            val deferred =
                deferred {
                    val request =
                        SaveToggleFollowUser.request(
                            userId = query.userId.toInt(),
                        )
                    remoteSource.saveToggleFollow(request)
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
        override val dispatcher: ISupportDispatcher,
    ) : UserSource.Update() {
        override fun observable(): Flow<User> {
            val userId = settings.authenticatedUserId.value
            return localSource
                .userByIdFlow(userId)
                .flowOn(dispatcher.io)
                .filterNotNull()
                .map(converter::convertFrom)
                .distinctUntilChanged()
                .flowOn(dispatcher.computation)
        }

        override suspend fun updateProfile(callback: RequestCallback) {
            val deferred =
                deferred {
                    val request =
                        UpdateUserProfile.request(
                            about = query.about,
                            titleLanguage =
                                co.anitrend.data.graphql.anilist.UserTitleLanguage.valueOf(
                                    query.titleLanguage.name,
                                ),
                            activityMergeTime = query.activityMergeTime,
                            displayAdultContent = query.displayAdultContent,
                            airingNotifications = query.airingNotifications,
                            scoreFormat =
                                co.anitrend.data.graphql.anilist.ScoreFormat.valueOf(
                                    query.scoreFormat.name,
                                ),
                            timezone = query.timeZone,
                            rowOrder = query.rowOrder,
                            profileColor = query.profileColor,
                            disabledListActivity =
                                query.disabledListActivity?.map {
                                    ListActivityOptionInput(
                                        disabled = it.disabled,
                                        type =
                                            co.anitrend.data.graphql.anilist.MediaListStatus
                                                .valueOf(it.type.name),
                                    )
                                },
                            restrictMessagesToFollowing = query.restrictMessagesToFollowing,
                            notificationOptions =
                                query.notificationOptions.map {
                                    co.anitrend.data.graphql.anilist.NotificationOptionInput(
                                        enabled = it.enabled,
                                        type =
                                            co.anitrend.data.graphql.anilist.NotificationType
                                                .valueOf(it.type.name),
                                    )
                                },
                            animeListOptions =
                                MediaListOptionsInput(
                                    sectionOrder = query.animeListOptions.sectionOrder,
                                    splitCompletedSectionByFormat =
                                        query.animeListOptions.splitCompletedSectionByFormat,
                                    customLists = query.animeListOptions.customLists,
                                    advancedScoring = query.animeListOptions.advancedScoring,
                                    advancedScoringEnabled =
                                        query.animeListOptions.advancedScoringEnabled,
                                ),
                            mangaListOptions =
                                MediaListOptionsInput(
                                    sectionOrder = query.mangaListOptions.sectionOrder,
                                    splitCompletedSectionByFormat =
                                        query.mangaListOptions.splitCompletedSectionByFormat,
                                    customLists = query.mangaListOptions.customLists,
                                    advancedScoring = query.mangaListOptions.advancedScoring,
                                    advancedScoringEnabled =
                                        query.mangaListOptions.advancedScoringEnabled,
                                ),
                            staffNameLanguage =
                                query.staffNameLanguage?.let {
                                    co.anitrend.data.graphql.anilist.UserStaffNameLanguage
                                        .valueOf(it.name)
                                },
                        )
                    remoteSource.updateUserProfile(request)
                }

            controller(deferred, callback)
        }
    }
}
