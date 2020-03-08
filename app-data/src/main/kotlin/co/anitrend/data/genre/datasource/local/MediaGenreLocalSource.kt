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

package co.anitrend.data.genre.datasource.local

import androidx.room.Dao
import androidx.room.Query
import co.anitrend.data.arch.common.dao.ILocalSource
import co.anitrend.data.genre.entity.GenreEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MediaGenreLocalSource: ILocalSource<GenreEntity> {

    @Query("""
        select count(id) from GenreEntity
        """
    )
    override suspend fun count(): Int

    @Query("""
            delete from GenreEntity
        """
    )
    override suspend fun clear()

    @Query("""
        select * from GenreEntity order by genre asc
        """
    )
    suspend fun findAll(): List<GenreEntity>

    @Query("""
        select * from GenreEntity order by genre asc
        """
    )
    fun findAllX(): Flow<List<GenreEntity>>
}