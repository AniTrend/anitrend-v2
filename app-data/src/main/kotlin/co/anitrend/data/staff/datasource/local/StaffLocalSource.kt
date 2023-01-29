/*
 * Copyright (C) 2021  AniTrend
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

package co.anitrend.data.staff.datasource.local

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import co.anitrend.data.android.source.AbstractLocalSource
import co.anitrend.data.staff.entity.StaffEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal abstract class StaffLocalSource : AbstractLocalSource<StaffEntity>() {

    @Query("""
        select count(id) from staff
    """)
    abstract override suspend fun count(): Int

    @Query("""
        delete from staff
    """)
    abstract override suspend fun clear()

    @Query("""
        delete from staff
        where id = :id
        """)
    abstract suspend fun clearById(id: Long)

    @Query("""
        select * from staff
        where id = :id
    """)
    abstract suspend fun staffById(id: Long): StaffEntity

    @Query("""
        select * from staff
        where id = :id
    """)
    abstract fun staffByIdFlow(id: Long): Flow<StaffEntity?>

    @Query("""
        select * from staff
    """)
    abstract fun allStaffFactory(): DataSource.Factory<Int, StaffEntity>

    @Query("""
        select * from staff
        where name_first match :term 
        or name_full match :term 
        or name_last match :term 
        or name_original match :term
        or name_alternative match :term
    """)
    abstract fun searchStaffFactory(
        term: String
    ): DataSource.Factory<Int, StaffEntity>
}