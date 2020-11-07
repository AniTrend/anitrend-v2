/*
 * Copyright (C) 2019  AniTrend
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

package co.anitrend.data.auth.datasource.local

import androidx.room.Dao
import androidx.room.Query
import co.anitrend.data.arch.database.dao.ILocalSource
import co.anitrend.data.auth.entity.AuthEntity

@Dao
internal abstract class AuthLocalSource : ILocalSource<AuthEntity?> {

    @Query("""
        select count(id) from auth
        """)
    abstract override suspend fun count(): Int

    @Query("""
        delete from auth
        """)
    abstract override suspend fun clear()

    @Query("""
        delete from auth
        where user_id = :userId
        """)
    abstract fun clearByUserId(userId: Long)

    @Query("""
            select * from auth
            where user_id = :userId
        """)
    abstract fun byUserId(userId: Long): AuthEntity?

    @Query("""
            select user_id from auth
        """)
    abstract suspend fun userIds(): List<Long>
}