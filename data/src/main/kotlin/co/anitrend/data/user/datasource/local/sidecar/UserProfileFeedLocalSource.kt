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
package co.anitrend.data.user.datasource.local.sidecar

import androidx.room.Dao
import androidx.room.Query
import co.anitrend.data.android.source.local.AbstractLocalSource
import co.anitrend.data.user.entity.sidecar.UserProfileFeedEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal abstract class UserProfileFeedLocalSource : AbstractLocalSource<UserProfileFeedEntity>() {
    @Query(
        """
        select count(user_id) from user_profile_feed
        """,
    )
    abstract override suspend fun count(): Int

    @Query(
        """
        delete from user_profile_feed
        """,
    )
    abstract override suspend fun clear()

    @Query(
        """
        select * from user_profile_feed
        where user_id = :userId
        limit 1
        """,
    )
    abstract fun entryByUserIdFlow(userId: Long): Flow<UserProfileFeedEntity?>

    @Query(
        """
        delete from user_profile_feed
        where user_id = :userId
        """,
    )
    abstract suspend fun clearByUserId(userId: Long)
}
