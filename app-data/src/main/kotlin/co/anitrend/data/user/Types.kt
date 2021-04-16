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

package co.anitrend.data.user

import androidx.paging.PagedList
import co.anitrend.arch.data.state.DataState
import co.anitrend.data.android.controller.graphql.GraphQLController
import co.anitrend.data.user.entity.UserEntity
import co.anitrend.data.user.entity.statistic.UserWithStatisticEntity
import co.anitrend.data.user.model.container.UserModelContainer
import co.anitrend.domain.user.entity.User
import co.anitrend.domain.user.interactor.UserUseCase
import co.anitrend.domain.user.repository.IUserRepository

internal typealias UserPagedController = GraphQLController<UserModelContainer.Paged, List<UserEntity>>
internal typealias UserAuthController = GraphQLController<UserModelContainer.Viewer, UserEntity>
internal typealias UserProfileController = GraphQLController<UserModelContainer.Profile, UserEntity>
internal typealias UserController = GraphQLController<UserModelContainer.User, UserEntity>
internal typealias UserProfileStatisticController =
        GraphQLController<UserModelContainer.WithStatistic, UserWithStatisticEntity>

internal typealias UserIdentifierRepository = IUserRepository.User<DataState<User>>
internal typealias UserAuthenticatedRepository = IUserRepository.Authenticated<DataState<User>>
internal typealias UserSearchRepository = IUserRepository.Search<DataState<PagedList<User>>>
internal typealias UserProfileRepository = IUserRepository.Profile<DataState<User>>
internal typealias UserProfileStatisticRepository = IUserRepository.Statistic<DataState<User.WithStats>>
internal typealias UserFollowRepository = IUserRepository.ToggleFollow<DataState<User>>
internal typealias UserUpdateRepository = IUserRepository.Update<DataState<User>>

typealias GetUserInteractor = UserUseCase.GetUser<DataState<User>>
typealias GetProfilePagedInteractor = UserUseCase.GetPaged<DataState<PagedList<User>>>
typealias GetProfileInteractor = UserUseCase.GetProfile<DataState<User>>
typealias GetProfileStatisticInteractor = UserUseCase.Statistic<DataState<User.WithStats>>
typealias GetAuthenticatedInteractor = UserUseCase.GetAuthenticated<DataState<User>>
typealias ToggleFollowInteractor = UserUseCase.ToggleFollow<DataState<User>>
typealias UpdateProfileInteractor = UserUseCase.Update<DataState<User>>
