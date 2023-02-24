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

package co.anitrend.data.auth.datasource.remote

import co.anitrend.data.auth.model.JsonWebToken
import co.anitrend.data.core.GRAPHQL
import co.anitrend.data.core.JSON
import co.anitrend.data.core.api.factory.contract.EndpointType
import co.anitrend.data.core.api.model.GraphQLResponse
import co.anitrend.data.user.model.container.UserModelContainer
import io.github.wax911.library.annotation.GraphQuery
import io.github.wax911.library.model.request.QueryContainerBuilder
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.Header
import retrofit2.http.POST

internal interface AuthRemoteSource {

    @JSON
    @POST("/token")
    suspend fun getAuthenticationToken(
        @Field("grant_type") grant_type: String,
        @Field("client_id") client_id: String,
        @Field("client_secret") client_secret: String,
        @Field("redirect_uri") redirect_uri: String,
        @Field("code") code: String
    ): Response<JsonWebToken>

    @GRAPHQL
    @POST(EndpointType.BASE_ENDPOINT_PATH)
    @GraphQuery("GetUserViewer")
    suspend fun getAuthenticatedUser(
        @Header("Authorization") authToken: String,
        @Body queryContainer: QueryContainerBuilder
    ): Response<GraphQLResponse<UserModelContainer.Viewer>>
}