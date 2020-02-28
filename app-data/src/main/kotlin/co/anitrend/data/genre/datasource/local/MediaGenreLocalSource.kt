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

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import co.anitrend.data.media.model.remote.MediaGenre
import co.anitrend.arch.data.dao.ISupportQuery

@Dao
interface MediaGenreLocalSource: ISupportQuery<MediaGenre> {

    @Query("select count(genre) from MediaGenre")
    suspend fun count(): Int

    @Query("select * from MediaGenre order by genre asc")
    suspend fun findAll(): List<MediaGenre>

    @Query("select * from MediaGenre order by genre asc")
    fun findAllLiveData(): LiveData<List<MediaGenre>>

    @Query("delete from MediaGenre")
    suspend fun deleteAll()
}