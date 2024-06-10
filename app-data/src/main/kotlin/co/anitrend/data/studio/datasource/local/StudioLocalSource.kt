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

package co.anitrend.data.studio.datasource.local

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import co.anitrend.data.android.source.local.AbstractLocalSource
import co.anitrend.data.studio.entity.StudioEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal abstract class StudioLocalSource : AbstractLocalSource<StudioEntity>() {

    @Query("""
        select count(id) from studio
    """)
    abstract override suspend fun count(): Int

    @Query("""
        delete from studio
    """)
    abstract override suspend fun clear()

    @Query("""
        delete from studio
        where id = :id
        """)
    abstract suspend fun clearById(id: Long)

    @Query("""
        select * from studio
        where id = :id
    """)
    abstract fun studioByIdFlow(id: Long): Flow<StudioEntity?>

    @Query("""
        select * from studio
    """)
    abstract fun allStudioFactory(): DataSource.Factory<Int, StudioEntity>

    @Query("""
        select * from studio
        where name match :term
    """)
    abstract fun searchStudioFactory(
        term: String
    ): DataSource.Factory<Int, StudioEntity>
}
