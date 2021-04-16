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

package co.anitrend.data.user.datasource.remote

import co.anitrend.data.core.api.factory.contract.EndpointType
import co.anitrend.data.core.api.model.GraphQLResponse
import co.anitrend.data.core.GRAPHQL
import co.anitrend.data.user.model.container.UserModelContainer
import io.github.wax911.library.annotation.GraphQuery
import io.github.wax911.library.model.request.QueryContainerBuilder
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

internal interface UserRemoteSource {

    @GRAPHQL
    @GraphQuery("GetUserByName")
    @POST(EndpointType.BASE_ENDPOINT_PATH)
    suspend fun getUserByName(
        @Body queryContainer: QueryContainerBuilder
    ): Response<GraphQLResponse<UserModelContainer.User>>

    @GRAPHQL
    @GraphQuery("GetUserPaged")
    @POST(EndpointType.BASE_ENDPOINT_PATH)
    suspend fun getUserPaged(
        @Body queryContainer: QueryContainerBuilder
    ): Response<GraphQLResponse<UserModelContainer.Paged>>

    @GRAPHQL
    @GraphQuery("GetUserViewer")
    @POST(EndpointType.BASE_ENDPOINT_PATH)
    suspend fun getUserViewer(
        @Body queryContainer: QueryContainerBuilder
    ): Response<GraphQLResponse<UserModelContainer.Viewer>>

    @GRAPHQL
    @GraphQuery("GetUserProfile")
    @POST(EndpointType.BASE_ENDPOINT_PATH)
    suspend fun getUserProfile(
        @Body queryContainer: QueryContainerBuilder
    ): Response<GraphQLResponse<UserModelContainer.Profile>>

    @GRAPHQL
    @GraphQuery("GetUserWithStatistic")
    @POST(EndpointType.BASE_ENDPOINT_PATH)
    suspend fun getUserWithStatistic(
        @Body queryContainer: QueryContainerBuilder
    ): Response<GraphQLResponse<UserModelContainer.WithStatistic>>

    @GRAPHQL
    @GraphQuery("SaveToggleFollowUser")
    @POST(EndpointType.BASE_ENDPOINT_PATH)
    suspend fun saveToggleFollow(
        @Body queryContainer: QueryContainerBuilder
    ): Response<GraphQLResponse<UserModelContainer.User>>

    @GRAPHQL
    @GraphQuery("UpdateUserProfile")
    @POST(EndpointType.BASE_ENDPOINT_PATH)
    suspend fun updateUserProfile(
        @Body queryContainer: QueryContainerBuilder
    ): Response<GraphQLResponse<UserModelContainer.Profile>>
}