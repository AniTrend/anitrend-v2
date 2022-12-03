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

package co.anitrend.data.jikan.media.datasource.remote

import co.anitrend.data.core.JSON
import co.anitrend.data.jikan.media.model.anime.JikanMediaModel
import co.anitrend.data.jikan.model.JikanWrapper
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

internal interface JikanRemoteSource {

    @JSON
    @GET("{type}/{id}/moreinfo")
    suspend fun getExtraInfo(
        @Path("id") id: Long,
        @Path("type") type: String
    ): Response<JikanWrapper<JikanMediaModel.MoreInfo>>

    @JSON
    @GET("anime/{id}")
    suspend fun getAnimeDetails(
        @Path("id") id: Long
    ): Response<JikanWrapper<JikanMediaModel.Anime>>

    @JSON
    @GET("manga/{id}")
    suspend fun getMangaDetails(
        @Path("id") id: Long
    ): Response<JikanWrapper<JikanMediaModel.Manga>>
}
