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

package co.anitrend.data.user.usecase

import co.anitrend.arch.data.repository.contract.ISupportRepository
import co.anitrend.data.user.GetAuthenticatedInteractor
import co.anitrend.data.user.GetProfileInteractor
import co.anitrend.data.user.GetProfileStatisticInteractor
import co.anitrend.data.user.GetUserInteractor
import co.anitrend.data.user.GetUserPagedInteractor
import co.anitrend.data.user.ToggleFollowInteractor
import co.anitrend.data.user.UpdateProfileInteractor
import co.anitrend.data.user.UserAuthenticatedRepository
import co.anitrend.data.user.UserFollowRepository
import co.anitrend.data.user.UserIdentifierRepository
import co.anitrend.data.user.UserProfileRepository
import co.anitrend.data.user.UserProfileStatisticRepository
import co.anitrend.data.user.UserSearchRepository
import co.anitrend.data.user.UserUpdateRepository

internal interface UserInteractor {

    class Identifier(
        repository: UserIdentifierRepository
    ) : GetUserInteractor(repository) {
        /**
         * Informs underlying repositories or related components running background operations to stop
         */
        override fun onCleared() {
            repository as ISupportRepository
            repository.onCleared()
        }

    }

    class Paged(
        repository: UserSearchRepository
    ) : GetUserPagedInteractor(repository) {
        /**
         * Informs underlying repositories or related components running background operations to stop
         */
        override fun onCleared() {
            repository as ISupportRepository
            repository.onCleared()
        }
    }

    class Profile(
        repository: UserProfileRepository
    ) : GetProfileInteractor(repository) {
        /**
         * Informs underlying repositories or related components running background operations to stop
         */
        override fun onCleared() {
            repository as ISupportRepository
            repository.onCleared()
        }
    }

    class Statistic(
        repository: UserProfileStatisticRepository
    ) : GetProfileStatisticInteractor(repository) {
        /**
         * Informs underlying repositories or related components running background operations to stop
         */
        override fun onCleared() {
            repository as ISupportRepository
            repository.onCleared()
        }
    }

    class Authenticated(
        repository: UserAuthenticatedRepository
    ) : GetAuthenticatedInteractor(repository) {
        /**
         * Informs underlying repositories or related components running background operations to stop
         */
        override fun onCleared() {
            repository as ISupportRepository
            repository.onCleared()
        }
    }

    class ToggleFollow(
        repository: UserFollowRepository
    ) : ToggleFollowInteractor(repository) {
        /**
         * Informs underlying repositories or related components running background operations to stop
         */
        override fun onCleared() {
            repository as ISupportRepository
            repository.onCleared()
        }
    }

    class Update(
        repository: UserUpdateRepository
    ) : UpdateProfileInteractor(repository) {
        /**
         * Informs underlying repositories or related components running background operations to stop
         */
        override fun onCleared() {
            repository as ISupportRepository
            repository.onCleared()
        }
    }
}
