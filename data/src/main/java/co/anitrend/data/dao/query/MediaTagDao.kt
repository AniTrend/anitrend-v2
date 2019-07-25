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

package co.anitrend.data.dao.query

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import co.anitrend.data.model.response.core.media.MediaTag
import io.wax911.support.data.dao.ISupportQuery

@Dao
interface MediaTagDao: ISupportQuery<MediaTag> {

    @Query("select count(id) from MediaTag")
    suspend fun count(): Int

    @Query("select * from MediaTag where id = :id order by name asc")
    suspend fun find(id: Long): List<MediaTag>?

    @Query("select * from MediaTag order by name asc")
    suspend fun findAll(): List<MediaTag>?

    @Query("delete from MediaTag")
    suspend fun deleteAll()

    @Query("select * from MediaTag where id = :id order by name asc")
    fun findLiveData(id: Long): LiveData<List<MediaTag>>?

    @Query("select * from MediaTag order by name asc")
    fun findAllLiveData(): LiveData<List<MediaTag>?>
}