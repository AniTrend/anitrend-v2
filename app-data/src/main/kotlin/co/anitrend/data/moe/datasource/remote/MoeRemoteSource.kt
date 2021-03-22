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

package co.anitrend.data.moe.datasource.remote

import co.anitrend.data.arch.JSON
import co.anitrend.data.moe.model.local.MoeSourceQuery
import co.anitrend.data.moe.model.remote.MoeModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

internal interface MoeRemoteSource {

    @JSON
    @GET("api/ids")
    suspend fun getFromSource(
        @Query("source") source: String,
        @Query("id") id: Long
    ) : Response<MoeModel?>
}