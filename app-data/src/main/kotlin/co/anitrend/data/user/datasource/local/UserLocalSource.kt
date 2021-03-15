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

package co.anitrend.data.user.datasource.local

import androidx.paging.DataSource
import androidx.room.*
import co.anitrend.data.arch.database.dao.ILocalSource
import co.anitrend.data.user.entity.UserEntity
import co.anitrend.data.user.entity.option.UserGeneralOptionEntity
import co.anitrend.data.user.entity.option.UserMediaOptionEntity
import co.anitrend.data.user.entity.statistic.UserWithStatisticEntity
import co.anitrend.data.user.entity.view.UserEntityView
import kotlinx.coroutines.flow.Flow

@Dao
internal abstract class UserLocalSource : ILocalSource<UserEntity> {

    /**
     * Count the number of entities
     */
    @Query("""
            select count(id) from user
        """)
    abstract override suspend fun count(): Int

    /**
     * Removes all records from table
     */
    @Query("""
        delete from user
        """)
    abstract override suspend fun clear()

    @Query("""
        delete from user
        where id = :id
        """)
    abstract suspend fun clearById(id: Long)

    @Query("""
        delete from user
        where user_name match :userName
        """)
    abstract suspend fun clearByMatch(userName: String)

    @Query("""
        select * from user
        where id = :id
    """)
    abstract suspend fun userById(id: Long): UserEntity?

    @Query("""
        select * from user
        where id in(:ids)
    """)
    abstract suspend fun userById(ids: List<Long>): List<UserEntity>

    @Query("""
        select * from user
        where id = :id
    """)
    @Transaction
    abstract suspend fun userByIdWithOptions(
        id: Long
    ): UserEntityView.WithOptions?

    @Query("""
        select * from user
        where id = :id
    """)
    abstract fun userByIdFlow(id: Long): Flow<UserEntity?>

    @Query("""
        select * from user
        where id = :id
    """)
    @Transaction
    abstract fun userByIdWithStatisticFlow(
        id: Long
    ): Flow<UserEntityView.WithStatistic?>

    @Query("""
        select * from user
        where user_is_following = :following
    """)
    abstract fun userFollowing(
        following: Boolean = true
    ): DataSource.Factory<Int, UserEntity>

    @Query("""
        select * from user
        where user_is_follower = :follower
    """)
    abstract fun userFollowers(
        follower: Boolean = true
    ): DataSource.Factory<Int, UserEntity>

    @Query("""
        select * from user
    """)
    abstract fun entryFactory(
    ): DataSource.Factory<Int, UserEntity>

    @Query("""
        select * from user
        where user_name match :searchTerm
    """)
    abstract fun entrySearchFactory(
        searchTerm: String
    ): DataSource.Factory<Int, UserEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun upsert(attribute: UserGeneralOptionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun upsert(attribute: UserMediaOptionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun upsert(attribute: UserWithStatisticEntity)

    @Transaction
    open suspend fun upsertWithOptions(
        userEntity: UserEntity,
        userGeneralOptionEntity: UserGeneralOptionEntity?,
        userMediaOptionEntity: UserMediaOptionEntity?
    ) {
        upsert(userEntity)
        if (userGeneralOptionEntity != null)
            upsert(userGeneralOptionEntity)
        if (userMediaOptionEntity != null)
            upsert(userMediaOptionEntity)
    }

    @Transaction
    open suspend fun upsertWithStatistic(
        userEntity: UserEntity,
        userWithStatisticEntity: UserWithStatisticEntity
    ) {
        upsert(userEntity)
        upsert(userWithStatisticEntity)
    }
}