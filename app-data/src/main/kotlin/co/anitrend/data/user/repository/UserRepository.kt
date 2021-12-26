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

import co.anitrend.arch.data.repository.SupportRepository
import co.anitrend.arch.data.state.DataState.Companion.create
import co.anitrend.arch.extension.coroutine.ISupportCoroutine
import co.anitrend.data.user.*
import co.anitrend.data.user.UserAuthenticatedRepository
import co.anitrend.data.user.UserFollowRepository
import co.anitrend.data.user.UserProfileRepository
import co.anitrend.data.user.UserProfileStatisticRepository
import co.anitrend.data.user.UserSearchRepository
import co.anitrend.data.user.UserUpdateRepository
import co.anitrend.data.user.source.contract.UserSource
import co.anitrend.domain.user.model.UserParam

internal sealed class UserRepository(
    source: ISupportCoroutine
) : SupportRepository(source) {

    class Identifier(
        private val source: UserSource.Identifier
    ) : UserRepository(source), UserIdentifierRepository {
        override fun getUser(
            param: UserParam.Identifier
        ) = source create source(param)
    }

    class Authenticated(
        private val source: UserSource.Authenticated
    ) : UserRepository(source), UserAuthenticatedRepository {
        override fun getProfile() = source create source()
    }

    class Search(
        private val source: UserSource.Search
    ) : UserRepository(source), UserSearchRepository {
        override fun getPaged(
            param: UserParam.Search
        ) = source create source(param)
    }

    class Profile(
        private val source: UserSource.Profile
    ) : UserRepository(source), UserProfileRepository {
        override fun getProfile(
            param: UserParam.Profile
        ) = source create source(param)
    }

    class Statistic(
        private val source: UserSource.Statistic
    ) : UserRepository(source), UserProfileStatisticRepository {
        override fun getProfileStatistic(
            param: UserParam.Statistic
        ) = source create source(param)
    }

    class ToggleFollow(
        private val source: UserSource.ToggleFollow
    ) : UserRepository(source), UserFollowRepository {
        override fun toggleFollow(
            param: UserParam.ToggleFollow
        ) = source create source(param)
    }

    class Update(
        private val source: UserSource.Update
    ) : UserRepository(source), UserUpdateRepository {
        override fun updateProfile(
            param: UserParam.Update
        ) = source create source(param)
    }
}