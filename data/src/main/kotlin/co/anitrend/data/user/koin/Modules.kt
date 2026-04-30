/*
 * Copyright (C) 2020 AniTrend
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
package co.anitrend.data.user.koin

import co.anitrend.data.android.extensions.cacheLocalSource
import co.anitrend.data.android.extensions.graphQLController
import co.anitrend.data.android.extensions.offline
import co.anitrend.data.android.mapper.EmbedMapper
import co.anitrend.data.auth.mapper.AuthMapper
import co.anitrend.data.core.extensions.aniListApi
import co.anitrend.data.core.extensions.store
import co.anitrend.data.core.extensions.transaction
import co.anitrend.data.review.mapper.ReviewMapper
import co.anitrend.data.status.mapper.StatusMapper
import co.anitrend.data.user.GetAuthenticatedInteractor
import co.anitrend.data.user.GetProfileFeedInteractor
import co.anitrend.data.user.GetProfileInteractor
import co.anitrend.data.user.GetProfileOverviewInteractor
import co.anitrend.data.user.GetProfileStatisticInteractor
import co.anitrend.data.user.UserProfileStatisticController
import co.anitrend.data.user.mapper.UserProfileConnectionMapper
import co.anitrend.data.user.GetUserInteractor
import co.anitrend.data.user.ToggleFollowInteractor
import co.anitrend.data.user.UpdateProfileInteractor
import co.anitrend.data.user.UserAuthenticatedRepository
import co.anitrend.data.user.UserFollowRepository
import co.anitrend.data.user.UserIdentifierRepository
import co.anitrend.data.user.UserProfileFeedRepository
import co.anitrend.data.user.UserProfileOverviewRepository
import co.anitrend.data.user.UserProfileRepository
import co.anitrend.data.user.UserProfileStatisticRepository
import co.anitrend.data.user.UserUpdateRepository
import co.anitrend.data.user.cache.UserCache
import co.anitrend.data.user.converter.UserEntityConverter
import co.anitrend.data.user.converter.UserGeneralOptionModelConverter
import co.anitrend.data.user.converter.UserMediaOptionModelConverter
import co.anitrend.data.user.converter.UserModelConverter
import co.anitrend.data.user.converter.UserStatisticModelConverter
import co.anitrend.data.user.converter.UserViewEntityConverter
import co.anitrend.data.user.mapper.UserMapper
import co.anitrend.data.user.mapper.UserProfileFeedMapper
import co.anitrend.data.user.mapper.UserProfileWriter
import co.anitrend.data.user.mapper.UserProfileWriterContract
import co.anitrend.data.user.mapper.UserProfileOverviewMapper
import co.anitrend.data.user.mapper.UserProfileFeedWriter
import co.anitrend.data.user.mapper.UserProfileFeedWriterContract
import co.anitrend.data.user.mapper.UserProfileOverviewWriter
import co.anitrend.data.user.mapper.UserProfileOverviewWriterContract
import co.anitrend.data.user.mapper.UserStatisticPersistenceWriter
import co.anitrend.data.user.mapper.UserStatisticPersistenceWriterContract
import co.anitrend.data.user.mapper.UserStatisticWriter
import co.anitrend.data.user.mapper.UserStatisticWriterContract
import co.anitrend.data.user.repository.UserRepository
import co.anitrend.data.user.source.UserSourceImpl
import co.anitrend.data.user.source.contract.UserSource
import co.anitrend.data.user.usecase.UserInteractor
import org.koin.dsl.module

private val sourceModule =
    module {
        factory<UserSource.Identifier> {
            UserSourceImpl.Identifier(
                remoteSource = aniListApi(),
                localSource = store().userDao(),
                clearDataHelper = get(),
                controller =
                    graphQLController(
                        mapper = get<UserMapper.User>(),
                        strategy = offline(),
                    ),
                converter = get(),
                cachePolicy = get<UserCache.Identifier>(),
                dispatcher = get(),
            )
        }
        factory<UserSource.Viewer> {
            UserSourceImpl.Viewer(
                remoteSource = aniListApi(),
                localSource = store().userDao(),
                clearDataHelper = get(),
                controller =
                    graphQLController(
                        mapper = get<AuthMapper>(),
                        strategy = offline(),
                    ),
                converter = get(),
                settings = get(),
                cachePolicy = get<UserCache.Viewer>(),
                dispatcher = get(),
            )
        }
        factory<UserSource.Profile> {
            UserSourceImpl.Profile(
                remoteSource = aniListApi(),
                localSource = store().userDao(),
                clearDataHelper = get(),
                controller =
                    graphQLController(
                        mapper = get<UserMapper.Profile>(),
                        strategy = offline(),
                    ),
                converter = get(),
                cachePolicy = get<UserCache.Profile>(),
                dispatcher = get(),
            )
        }
        factory<UserSource.Statistic> {
            val controller: UserProfileStatisticController =
                graphQLController(
                    mapper = get<UserMapper.Statistic>(),
                    strategy = offline(),
                )

            UserSourceImpl.Statistic(
                remoteSource = aniListApi(),
                localSource = store().userDao(),
                clearDataHelper = get(),
                controller = controller,
                converter = get(),
                cachePolicy = get<UserCache.Statistic>(),
                dispatcher = get(),
            )
        }
        factory<UserSource.Overview> {
            UserSourceImpl.Overview(
                remoteSource = aniListApi(),
                favouriteMediaLocalSource = store().userProfileFavouriteMediaDao(),
                statusLocalSource = store().statusDao(),
                clearDataHelper = get(),
                controller =
                    graphQLController(
                        mapper = get<UserProfileOverviewMapper>(),
                        strategy = offline(),
                    ),
                cachePolicy = get<UserCache.Overview>(),
                dispatcher = get(),
            )
        }
        factory<UserSource.Feed> {
            UserSourceImpl.Feed(
                remoteSource = aniListApi(),
                reviewLocalSource = store().userProfileReviewDao(),
                statusLocalSource = store().statusDao(),
                clearDataHelper = get(),
                controller =
                    graphQLController(
                        mapper = get<UserProfileFeedMapper>(),
                        strategy = offline(),
                    ),
                cachePolicy = get<UserCache.Feed>(),
                dispatcher = get(),
            )
        }
        factory<UserSource.ToggleFollow> {
            UserSourceImpl.ToggleFollow(
                remoteSource = aniListApi(),
                localSource = store().userDao(),
                controller =
                    graphQLController(
                        mapper = get<UserMapper.User>(),
                    ),
                converter = get(),
                dispatcher = get(),
            )
        }
        factory<UserSource.Update> {
            UserSourceImpl.Update(
                remoteSource = aniListApi(),
                localSource = store().userDao(),
                controller =
                    graphQLController(
                        mapper = get<UserMapper.Profile>(),
                    ),
                converter = get(),
                settings = get(),
                dispatcher = get(),
            )
        }
    }

private val cacheModule =
    module {
        factory {
            UserCache.Viewer(
                localSource = cacheLocalSource(),
            )
        }
        factory {
            UserCache.Identifier(
                localSource = cacheLocalSource(),
            )
        }
        factory {
            UserCache.Profile(
                localSource = cacheLocalSource(),
            )
        }
        factory {
            UserCache.Statistic(
                localSource = cacheLocalSource(),
            )
        }
        factory {
            UserCache.Overview(
                localSource = cacheLocalSource(),
            )
        }
        factory {
            UserCache.Feed(
                localSource = cacheLocalSource(),
            )
        }
    }

private val converterModule =
    module {
        factory {
            UserEntityConverter()
        }
        factory {
            UserModelConverter()
        }
        factory {
            UserGeneralOptionModelConverter()
        }
        factory {
            UserMediaOptionModelConverter()
        }
        factory {
            UserViewEntityConverter()
        }
        factory {
            UserStatisticModelConverter()
        }
    }

private val mapperModule =
    module {
        factory {
            UserMapper.Profile(
                generalOptionMapper = get(),
                mediaOptionMapper = get(),
                previousNameMapper = get(),
                writer = get(),
                localSource = store().userDao(),
                transactionRunner = transaction(),
                converter = get(),
            )
        }
        factory<UserProfileWriterContract> {
            UserProfileWriter(
                localSource = store().userDao(),
                generalOptionMapper = get(),
                mediaOptionMapper = get(),
                previousNameMapper = get(),
            )
        }
        factory {
            UserMapper.User(
                localSource = store().userDao(),
                converter = get(),
            )
        }
        factory {
            UserMapper.Embed(
                localSource = store().userDao(),
                converter = get(),
            )
        }
        factory {
            UserMapper.MediaOptionEmbed(
                localSource = store().userMediaOptionDao(),
                converter = get(),
            )
        }
        factory {
            UserMapper.GeneralOptionEmbed(
                localSource = store().userGeneralOptionDao(),
                converter = get(),
            )
        }
        factory {
            UserMapper.PreviousNameEmbed(
                localSource = store().userPreviousNameDao(),
            )
        }
        factory {
            UserMapper.NotificationEmbed(
                localSource = store().userNotificationDao(),
            )
        }
        factory {
            UserMapper.StatisticEmbed(
                localSource = store().userStatisticDao(),
            )
        }
        factory {
            UserMapper.Statistic(
                userMapper = get(),
                writer = get(),
                converter = get(),
                transactionRunner = transaction(),
            )
        }
        factory<UserStatisticPersistenceWriterContract> {
            UserStatisticPersistenceWriter(
                userPersistence = get<UserMapper.Embed>(),
                statisticWriter = get(),
            )
        }
        factory<UserStatisticWriterContract> {
            UserStatisticWriter(
                localSource = store().userStatisticDao(),
            )
        }
        factory<UserProfileOverviewWriterContract> {
            UserProfileOverviewWriter(
                mediaPersistence = get<EmbedMapper<co.anitrend.data.media.model.MediaModel, co.anitrend.data.media.entity.MediaEntity>>(),
                favouritePersistence = get<UserProfileConnectionMapper.FavouriteEmbed>(),
                statusPersistence = get<StatusMapper.Activity.Embed>(),
            )
        }
        factory<UserProfileFeedWriterContract> {
            UserProfileFeedWriter(
                mediaPersistence = get<EmbedMapper<co.anitrend.data.media.model.MediaModel, co.anitrend.data.media.entity.MediaEntity>>(),
                reviewPreviewPersistence = get<ReviewMapper.PreviewEmbed>(),
                reviewConnectionPersistence = get<UserProfileConnectionMapper.ReviewEmbed>(),
                statusPersistence = get<StatusMapper.Activity.Embed>(),
            )
        }
        factory {
            UserProfileOverviewMapper(
                favouriteEmbedMapper = get(),
                statusEmbedMapper = get(),
                mediaEmbedMapper = get(),
                writer = get(),
                transactionRunner = transaction(),
            )
        }
        factory {
            UserProfileFeedMapper(
                reviewConnectionMapper = get(),
                reviewPreviewMapper = get(),
                statusEmbedMapper = get(),
                mediaEmbedMapper = get(),
                writer = get(),
                transactionRunner = transaction(),
            )
        }
        factory {
            UserProfileConnectionMapper.FavouriteEmbed(
                localSource = store().userProfileFavouriteMediaDao(),
            )
        }
        factory {
            UserProfileConnectionMapper.ReviewEmbed(
                localSource = store().userProfileReviewDao(),
            )
        }
        factory {
            StatusMapper.Activity.Embed(
                localSource = store().statusDao(),
            )
        }
        factory {
            ReviewMapper.PreviewEmbed(
                localSource = store().reviewDao(),
            )
        }
    }

private val useCaseModule =
    module {
        factory<GetUserInteractor> {
            UserInteractor.Identifier(
                repository = get(),
            )
        }
        factory<GetProfileInteractor> {
            UserInteractor.Profile(
                repository = get(),
            )
        }
        factory<GetAuthenticatedInteractor> {
            UserInteractor.Authenticated(
                repository = get(),
            )
        }
        factory<GetProfileStatisticInteractor> {
            UserInteractor.Statistic(
                repository = get(),
            )
        }
        factory<GetProfileOverviewInteractor> {
            UserInteractor.Overview(
                repository = get(),
            )
        }
        factory<GetProfileFeedInteractor> {
            UserInteractor.Feed(
                repository = get(),
            )
        }
        factory<ToggleFollowInteractor> {
            UserInteractor.ToggleFollow(
                repository = get(),
            )
        }
        factory<UpdateProfileInteractor> {
            UserInteractor.Update(
                repository = get(),
            )
        }
    }

private val repositoryModule =
    module {
        factory<UserIdentifierRepository> {
            UserRepository.Identifier(
                source = get(),
            )
        }
        factory<UserAuthenticatedRepository> {
            UserRepository.Authenticated(
                source = get(),
            )
        }
        factory<UserProfileRepository> {
            UserRepository.Profile(
                source = get(),
            )
        }
        factory<UserProfileStatisticRepository> {
            UserRepository.Statistic(
                source = get(),
            )
        }
        factory<UserProfileOverviewRepository> {
            UserRepository.Overview(
                source = get(),
            )
        }
        factory<UserProfileFeedRepository> {
            UserRepository.Feed(
                source = get(),
            )
        }
        factory<UserFollowRepository> {
            UserRepository.ToggleFollow(
                source = get(),
            )
        }
        factory<UserUpdateRepository> {
            UserRepository.Update(
                source = get(),
            )
        }
    }

internal val userModules =
    module {
        includes(
            converterModule,
            sourceModule,
            cacheModule,
            mapperModule,
            useCaseModule,
            repositoryModule,
        )
    }
