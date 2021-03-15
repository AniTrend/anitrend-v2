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

package co.anitrend.data.user.koin

import co.anitrend.data.api.contract.EndpointType
import co.anitrend.data.arch.extension.api
import co.anitrend.data.arch.extension.db
import co.anitrend.data.arch.extension.graphQLController
import co.anitrend.data.auth.mapper.AuthMapper
import co.anitrend.data.user.*
import co.anitrend.data.user.cache.UserCache
import co.anitrend.data.user.converter.UserEntityConverter
import co.anitrend.data.user.converter.UserGeneralOptionModelConverter
import co.anitrend.data.user.converter.UserMediaOptionModelConverter
import co.anitrend.data.user.converter.UserModelConverter
import co.anitrend.data.user.converter.UserViewEntityConverter
import co.anitrend.data.user.mapper.UserMapper
import co.anitrend.data.user.repository.UserRepository
import co.anitrend.data.user.source.UserSourceImpl
import co.anitrend.data.user.source.contract.UserSource
import co.anitrend.data.user.usecase.UserInteractor
import org.koin.dsl.module

private val sourceModule = module {
    factory<UserSource.Authenticated> {
        UserSourceImpl.Authenticated(
            remoteSource = api(EndpointType.GRAPH_QL),
            localSource = db().userDao(),
            clearDataHelper = get(),
            controller = graphQLController(
                mapper = get<AuthMapper>()
            ),
            converter = get(),
            settings = get(),
            cachePolicy = get<UserCache.Profile>(),
            dispatcher = get()
        )
    }
    factory<UserSource.Search> {
        UserSourceImpl.Search(
            remoteSource = api(EndpointType.GRAPH_QL),
            localSource = db().userDao(),
            clearDataHelper = get(),
            settings = get(),
            controller = graphQLController(
                mapper = get<UserMapper.Paged>()
            ),
            converter = get(),
            dispatcher = get()
        )
    }
    factory<UserSource.Profile> {
        UserSourceImpl.Profile(
            remoteSource = api(EndpointType.GRAPH_QL),
            localSource = db().userDao(),
            clearDataHelper = get(),
            controller = graphQLController(
                mapper = get<UserMapper.Profile>()
            ),
            converter = get(),
            cachePolicy = get<UserCache.Profile>(),
            dispatcher = get()
        )
    }
    factory<UserSource.Statistic> {
        UserSourceImpl.Statistic(
            remoteSource = api(EndpointType.GRAPH_QL),
            localSource = db().userDao(),
            clearDataHelper = get(),
            controller = graphQLController(
                mapper = get<UserMapper.Statistic>()
            ),
            converter = get(),
            cachePolicy = get<UserCache.Statistic>(),
            dispatcher = get()
        )
    }
    factory<UserSource.ToggleFollow> {
        UserSourceImpl.ToggleFollow(
            remoteSource = api(EndpointType.GRAPH_QL),
            localSource = db().userDao(),
            controller = graphQLController(
                mapper = get<UserMapper.User>()
            ),
            converter = get(),
            dispatcher = get()
        )
    }
    factory<UserSource.Update> {
        UserSourceImpl.Update(
            remoteSource = api(EndpointType.GRAPH_QL),
            localSource = db().userDao(),
            controller = graphQLController(
                mapper = get<UserMapper.Profile>()
            ),
            converter = get(),
            settings = get(),
            dispatcher = get()
        )
    }
}

private val cacheModule = module {
    factory {
        UserCache.Profile(
            localSource = db().cacheDao()
        )
    }
    factory {
        UserCache.Statistic(
            localSource = db().cacheDao()
        )
    }
}

private val converterModule = module {
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
}

private val mapperModule = module {
    factory {
        UserMapper.Paged(
            localSource = db().userDao(),
            converter = get(),
        )
    }
    factory {
        UserMapper.Profile(
            generalOptionConverter = get(),
            mediaOptionConverter = get(),
            localSource = db().userDao(),
            converter = get(),
        )
    }
    factory {
        UserMapper.User(
            localSource = db().userDao(),
            converter = get(),
        )
    }
}

private val useCaseModule = module {
    factory<GetProfilePagedInteractor> {
        UserInteractor.Paged(
            repository = get()
        )
    }
    factory<GetProfileInteractor> {
        UserInteractor.Profile(
            repository = get()
        )
    }
    factory<GetAuthenticatedInteractor> {
        UserInteractor.Authenticated(
            repository = get()
        )
    }
    factory<ToggleFollowInteractor> {
        UserInteractor.ToggleFollow(
            repository = get()
        )
    }
    factory<UpdateProfileInteractor> {
        UserInteractor.Update(
            repository = get()
        )
    }
}

private val repositoryModule = module {
    factory<UserAuthenticatedRepository> {
        UserRepository.Authenticated(
            source = get()
        )
    }
    factory<UserSearchRepository> {
        UserRepository.Search(
            source = get()
        )
    }
    factory<UserProfileRepository> {
        UserRepository.Profile(
            source = get()
        )
    }
    factory<UserFollowRepository> {
        UserRepository.ToggleFollow(
            source = get()
        )
    }
    factory<UserUpdateRepository> {
        UserRepository.Update(
            source = get()
        )
    }
}

internal val userModules = listOf(
    converterModule,
    sourceModule,
    cacheModule,
    mapperModule,
    useCaseModule,
    repositoryModule
)