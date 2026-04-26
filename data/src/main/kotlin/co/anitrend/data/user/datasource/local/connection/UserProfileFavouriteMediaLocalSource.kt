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
import androidx.room.Transaction
import co.anitrend.data.android.source.local.AbstractLocalSource
import co.anitrend.data.user.entity.connection.UserProfileFavouriteMediaEntity
import co.anitrend.data.user.entity.view.UserProfileFavouriteMediaEntityView
import kotlinx.coroutines.flow.Flow

@Dao
internal abstract class UserProfileFavouriteMediaLocalSource : AbstractLocalSource<UserProfileFavouriteMediaEntity>() {
    @Query("SELECT COUNT(id) FROM user_profile_favourite_media")
    abstract override suspend fun count(): Int

    @Query("DELETE FROM user_profile_favourite_media")
    abstract override suspend fun clear()

    @Query(
        """
        SELECT * FROM user_profile_favourite_media
        WHERE user_id = :userId
        ORDER BY category ASC, sort_index ASC
        """,
    )
    @Transaction
    abstract fun entryByUserIdFlow(userId: Long): Flow<List<UserProfileFavouriteMediaEntityView>>

    @Query(
        """
        DELETE FROM user_profile_favourite_media
        WHERE user_id = :userId
        """,
    )
    abstract suspend fun clearByUserId(userId: Long)
}
