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
package co.anitrend.data.user.datasource.local.connection

import androidx.room.Dao
import androidx.room.Query
import co.anitrend.data.android.source.local.AbstractLocalSource
import co.anitrend.data.user.entity.connection.UserProfileReviewEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal abstract class UserProfileReviewLocalSource :
    AbstractLocalSource<UserProfileReviewEntity>() {
    @Query("SELECT COUNT(id) FROM user_profile_review")
    abstract override suspend fun count(): Int

    @Query("DELETE FROM user_profile_review")
    abstract override suspend fun clear()

    @Query(
        """
        SELECT * FROM user_profile_review
        WHERE user_id = :userId
        ORDER BY sort_index ASC
        """,
    )
    abstract fun entryByUserIdFlow(userId: Long): Flow<List<UserProfileReviewEntity>>

    @Query(
        """
        DELETE FROM user_profile_review
        WHERE user_id = :userId
        """,
    )
    abstract suspend fun clearByUserId(userId: Long)
}
