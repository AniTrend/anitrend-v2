/*
 * Copyright (C) 2026 AniTrend
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
package co.anitrend.data.user.mapper

import co.anitrend.data.android.mapper.DefaultMapper
import co.anitrend.data.user.datasource.local.sidecar.UserProfileOverviewLocalSource
import co.anitrend.data.user.entity.sidecar.UserProfileOverviewEntity
import co.anitrend.data.user.model.container.UserSidecarModelContainer

internal class UserProfileOverviewMapper(
    private val localSource: UserProfileOverviewLocalSource,
) : DefaultMapper<UserSidecarModelContainer.Overview, UserProfileOverviewEntity>() {
    override suspend fun persist(data: UserProfileOverviewEntity) {
        localSource.upsert(data)
    }

    override suspend fun onResponseMapFrom(source: UserSidecarModelContainer.Overview): UserProfileOverviewEntity {
        val userId = requireNotNull(source.user?.id) { "Overview response missing user id" }
        return UserProfileOverviewEntity(
            id = userId,
            animeFavourites =
                source.user.favourites
                    ?.anime
                    ?.edges
                    .orEmpty()
                    .sortedBy { it.favouriteOrder ?: Int.MAX_VALUE }
                    .mapNotNull { it.node },
            mangaFavourites =
                source.user.favourites
                    ?.manga
                    ?.edges
                    .orEmpty()
                    .sortedBy { it.favouriteOrder ?: Int.MAX_VALUE }
                    .mapNotNull { it.node },
            recentActivity = source.page?.activities.orEmpty(),
        )
    }
}
