/*
 * Copyright (C) 2021 AniTrend
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
/*
 * Copyright (C) 2021 AniTrend
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
package co.anitrend.data.status.datasource.local

import androidx.room.Dao
import androidx.room.Query
import co.anitrend.data.android.source.local.AbstractLocalSource
import co.anitrend.data.status.entity.StatusEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal abstract class StatusLocalSource : AbstractLocalSource<StatusEntity.ListStatus>() {
    @Query("SELECT COUNT(id) FROM list_status")
    abstract override suspend fun count(): Int

    @Query("DELETE FROM list_status")
    abstract override suspend fun clear()

    @Query(
        """
        SELECT * FROM list_status
        WHERE user_id = :userId
        ORDER BY sort_index ASC
        """,
    )
    abstract fun listStatusByUserIdFlow(userId: Long): Flow<List<StatusEntity.ListStatus>>

    @Query(
        """
        DELETE FROM list_status
        WHERE user_id = :userId
        """,
    )
    abstract suspend fun clearListStatusByUserId(userId: Long)
}
