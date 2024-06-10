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

package co.anitrend.data.user.repository

import co.anitrend.arch.data.state.DataState.Companion.create
import co.anitrend.data.user.UserAuthenticatedRepository
import co.anitrend.data.user.UserFollowRepository
import co.anitrend.data.user.UserIdentifierRepository
import co.anitrend.data.user.UserProfileRepository
import co.anitrend.data.user.UserProfileStatisticRepository
import co.anitrend.data.user.UserSearchRepository
import co.anitrend.data.user.UserUpdateRepository
import co.anitrend.data.user.source.contract.UserSource
import co.anitrend.domain.user.model.UserParam

internal interface UserRepository {

    class Identifier(
        private val source: UserSource.Identifier
    ) : UserRepository, UserIdentifierRepository {
        override suspend fun getUser(
            param: UserParam.Identifier
        ) = source create source(param)
    }

    class Authenticated(
        private val source: UserSource.Viewer
    ) : UserRepository, UserAuthenticatedRepository {
        override suspend fun getProfile() = source create source()
    }

    class Search(
        private val source: UserSource.Search
    ) : UserRepository, UserSearchRepository {
        override suspend fun getPaged(
            param: UserParam.Search
        ) = source create source(param)
    }

    class Profile(
        private val source: UserSource.Profile
    ) : UserRepository, UserProfileRepository {
        override suspend fun getProfile(
            param: UserParam.Profile
        ) = source create source(param)
    }

    class Statistic(
        private val source: UserSource.Statistic
    ) : UserRepository, UserProfileStatisticRepository {
        override suspend fun getProfileStatistic(
            param: UserParam.Statistic
        ) = source create source(param)
    }

    class ToggleFollow(
        private val source: UserSource.ToggleFollow
    ) : UserRepository, UserFollowRepository {
        override suspend fun toggleFollow(
            param: UserParam.ToggleFollow
        ) = source create source(param)
    }

    class Update(
        private val source: UserSource.Update
    ) : UserRepository, UserUpdateRepository {
        override suspend fun updateProfile(
            param: UserParam.Update
        ) = source create source(param)
    }
}
