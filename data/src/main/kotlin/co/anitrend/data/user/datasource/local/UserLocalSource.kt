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
package co.anitrend.data.user.datasource.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Transaction
import androidx.sqlite.db.SupportSQLiteQuery
import co.anitrend.data.android.source.local.AbstractLocalSource
import co.anitrend.data.user.entity.UserEntity
import co.anitrend.data.user.entity.view.UserEntityView
import kotlinx.coroutines.flow.Flow

@Dao
internal abstract class UserLocalSource : AbstractLocalSource<UserEntity>() {
    /**
     * Count the number of entities
     */
    @Query(
        """
            select count(id) from user
        """,
    )
    abstract override suspend fun count(): Int

    /**
     * Removes all records from table
     */
    @Query(
        """
        delete from user
        """,
    )
    abstract override suspend fun clear()

    @Query(
        """
        delete from user
        where id = :id
        """,
    )
    abstract suspend fun clearById(id: Long)

    @Query(
        """
        delete from user
        where user_name = :userName
        """,
    )
    abstract suspend fun clearByUserName(userName: String)

    @Query(
        """
        delete from user
        where user_name match :userName
        """,
    )
    abstract suspend fun clearByMatch(userName: String)

    @Query(
        """
        select * from user
        where id = :id
    """,
    )
    abstract suspend fun userById(id: Long): UserEntity?

    @Query(
        """
        select * from user
        where id in(:ids)
    """,
    )
    abstract suspend fun userById(ids: List<Long>): List<UserEntity>

    @Query(
        """
        select * from user
        where id = :id
    """,
    )
    abstract fun userByIdFlow(id: Long): Flow<UserEntity?>

    @Query(
        """
        select * from user
        where user_name = :userName
    """,
    )
    abstract fun userByNameFlow(userName: String): Flow<UserEntity?>

    @RawQuery(observedEntities = [UserEntity::class])
    abstract fun rawPagingSource(query: SupportSQLiteQuery): PagingSource<Int, UserEntity>

    @Query(
        """
        select * from user
        where id = :userId
    """,
    )
    @Transaction
    abstract fun userAuthenticated(userId: Long): Flow<UserEntityView.Authenticated?>

    @Query(
        """
        select * from user
        where user_name = :userName
    """,
    )
    @Transaction
    abstract fun userByNameWithOptionsFlow(userName: String): Flow<UserEntityView.WithOptions?>

    @Query(
        """
        select * from user
        where id = :id
    """,
    )
    @Transaction
    abstract fun userByIdWithOptionsFlow(id: Long): Flow<UserEntityView.WithOptions?>

    @Query(
        """
        select * from user
        where id = :id
    """,
    )
    @Transaction
    abstract fun userByIdWithStatisticFlow(id: Long): Flow<UserEntityView.WithStatistic?>
}
