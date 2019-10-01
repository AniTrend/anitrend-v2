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
import co.anitrend.data.auth.model.JsonWebToken
import co.anitrend.arch.data.dao.ISupportQuery

@Dao
interface JsonWebTokenDao: ISupportQuery<JsonWebToken?> {

    @Query("select count(id) from JsonWebToken")
    fun count(): Int

    @Query("select * from JsonWebToken order by id asc limit 1")
    fun findLatest(): JsonWebToken?

    @Query("select * from JsonWebToken limit :limit offset :offset")
    fun findAll(offset: Int, limit: Int): List<JsonWebToken>?

    @Query("delete from JsonWebToken")
    fun clearTable()
}