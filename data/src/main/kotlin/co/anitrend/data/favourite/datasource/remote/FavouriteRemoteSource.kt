/*
 * Copyright (C) 2025 AniTrend
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
package co.anitrend.data.favourite.datasource.remote

import co.anitrend.data.core.GRAPHQL
import co.anitrend.data.core.api.model.GraphQLResponse
import co.anitrend.data.graphql.anilist.ToggleAnimeFavouriteVariables
import co.anitrend.data.graphql.anilist.ToggleMangaFavouriteVariables
import co.anitrend.retrofit.graphql.model.GraphQLRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

internal interface FavouriteRemoteSource {
    @GRAPHQL
    @POST
    suspend fun toggleAnimeFavorite(
        @Body request: GraphQLRequest<ToggleAnimeFavouriteVariables>,
    ): Response<GraphQLResponse<Boolean>>

    @GRAPHQL
    @POST
    suspend fun toggleMangaFavorite(
        @Body request: GraphQLRequest<ToggleMangaFavouriteVariables>,
    ): Response<GraphQLResponse<Boolean>>
}
