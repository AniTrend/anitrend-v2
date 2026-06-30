/*
 * Copyright (C) 2019 AniTrend
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
package co.anitrend.data.genre.datasource.remote

import co.anitrend.data.core.GRAPHQL
import co.anitrend.data.core.api.factory.contract.IEndpointType
import co.anitrend.data.core.api.model.GraphQLResponse
import co.anitrend.data.genre.model.GenreCollection
import co.anitrend.retrofit.graphql.model.EmptyGraphQLVariables
import co.anitrend.retrofit.graphql.model.GraphQLRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

internal interface MediaGenreRemoteSource {
    @GRAPHQL
    @POST(IEndpointType.BASE_ENDPOINT_PATH)
    suspend fun getMediaGenres(
        @Body request: GraphQLRequest<EmptyGraphQLVariables>,
    ): Response<GraphQLResponse<GenreCollection>>
}
