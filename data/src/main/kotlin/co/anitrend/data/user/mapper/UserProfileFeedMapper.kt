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
import co.anitrend.data.user.datasource.local.sidecar.UserProfileFeedLocalSource
import co.anitrend.data.user.entity.sidecar.UserProfileFeedEntity
import co.anitrend.data.user.model.container.UserSidecarModelContainer

internal class UserProfileFeedMapper(
    private val localSource: UserProfileFeedLocalSource,
) : DefaultMapper<UserSidecarModelContainer.Feed, UserProfileFeedEntity>() {
    override suspend fun persist(data: UserProfileFeedEntity) {
        localSource.upsert(data)
    }

    override suspend fun onResponseMapFrom(source: UserSidecarModelContainer.Feed): UserProfileFeedEntity {
        val userId = requireNotNull(source.user?.id) { "Feed response missing user id" }

        return UserProfileFeedEntity(
            id = userId,
            reviews = source.reviewPage?.reviews.orEmpty(),
            listActivity = source.activityPage?.listActivity.orEmpty(),
        )
    }
}
