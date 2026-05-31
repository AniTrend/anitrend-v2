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
package co.anitrend.data.user.usecase

import co.anitrend.data.user.GetAuthenticatedInteractor
import co.anitrend.data.user.GetProfileFeedInteractor
import co.anitrend.data.user.GetProfileOverviewInteractor
import co.anitrend.data.user.GetProfileInteractor
import co.anitrend.data.user.GetProfileStatisticInteractor
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
import co.anitrend.data.user.repository.UserRepository
import co.anitrend.domain.user.model.UserParam

internal interface UserInteractor {
    class Identifier(
        repository: UserIdentifierRepository,
    ) : GetUserInteractor(repository)

    class Profile(
        repository: UserProfileRepository,
    ) : GetProfileInteractor(repository)

    class Search(
        private val repository: UserRepository.Search,
    ) {
        suspend operator fun invoke(param: UserParam.Search) = repository.getPaged(param)
    }

    class Statistic(
        repository: UserProfileStatisticRepository,
    ) : GetProfileStatisticInteractor(repository)

    class Overview(
        repository: UserProfileOverviewRepository,
    ) : GetProfileOverviewInteractor(repository)

    class Feed(
        repository: UserProfileFeedRepository,
    ) : GetProfileFeedInteractor(repository)

    class Authenticated(
        repository: UserAuthenticatedRepository,
    ) : GetAuthenticatedInteractor(repository)

    class ToggleFollow(
        repository: UserFollowRepository,
    ) : ToggleFollowInteractor(repository)

    class Update(
        repository: UserUpdateRepository,
    ) : UpdateProfileInteractor(repository)
}
