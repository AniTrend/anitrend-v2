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

package co.anitrend.data.source.datasource.local

import androidx.room.Dao
import androidx.room.Query
import co.anitrend.data.arch.database.dao.ILocalSource
import co.anitrend.data.source.entity.SourceEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal interface SourceLocal : ILocalSource<SourceEntity> {

    @Query("""
        select count(anilist) from source_entity
    """)
    override suspend fun count(): Int

    @Query("""
        delete from source_entity
    """)
    override suspend fun clear()

    @Query("""
        delete from source_entity
        where anilist = :anilist
    """)
    suspend fun clear(anilist: Long)

    @Query("""
        select count(anilist)
        from source_entity
        where anilist = :anilist
    """)
    suspend fun count(anilist: Long): Int

    @Query("""
        select * from source_entity
        where anilist = :anilist
    """)
    fun withAniListIdX(
        anilist: Long
    ): Flow<SourceEntity?>

    @Query("""
        select * from source_entity
        where anilist = :anilist
    """)
    suspend fun withAniListId(
        anilist: Long
    ): SourceEntity?
}