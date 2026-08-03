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
package co.anitrend.data.user.datasource.remote

import co.anitrend.data.core.api.factory.contract.IEndpointType
import co.anitrend.retrofit.graphql.model.GraphQLResponse
import co.anitrend.data.graphql.anilist.GetAnimeFavouritesVariables
import co.anitrend.data.graphql.anilist.GetMangaFavouritesVariables
import co.anitrend.data.graphql.anilist.GetUserPagedVariables
import co.anitrend.data.graphql.anilist.GetUserProfileFeedVariables
import co.anitrend.data.graphql.anilist.GetUserProfileOverviewVariables
import co.anitrend.data.graphql.anilist.GetUserProfileVariables
import co.anitrend.data.graphql.anilist.GetUserVariables
import co.anitrend.data.graphql.anilist.GetUserWithStatisticVariables
import co.anitrend.data.graphql.anilist.SaveToggleFollowUserVariables
import co.anitrend.data.graphql.anilist.UpdateUserProfileVariables
import co.anitrend.data.user.model.container.UserModelContainer
import co.anitrend.data.user.model.container.UserSidecarModelContainer
import co.anitrend.retrofit.graphql.model.EmptyGraphQLVariables
import co.anitrend.retrofit.graphql.model.request.GraphQLOperationRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

internal interface UserRemoteSource {
    @POST(IEndpointType.BASE_ENDPOINT_PATH)
    suspend fun getUserByName(
        @Body request: GraphQLOperationRequest<GetUserVariables>,
    ): Response<GraphQLResponse<UserModelContainer.User>>

    @POST(IEndpointType.BASE_ENDPOINT_PATH)
    suspend fun getUserPaged(
        @Body request: GraphQLOperationRequest<GetUserPagedVariables>,
    ): Response<GraphQLResponse<UserModelContainer.Paged>>

    @POST(IEndpointType.BASE_ENDPOINT_PATH)
    suspend fun getUserViewer(
        @Body request: GraphQLOperationRequest<EmptyGraphQLVariables>,
    ): Response<GraphQLResponse<UserModelContainer.Viewer>>

    @POST(IEndpointType.BASE_ENDPOINT_PATH)
    suspend fun getUserProfile(
        @Body request: GraphQLOperationRequest<GetUserProfileVariables>,
    ): Response<GraphQLResponse<UserModelContainer.Profile>>

    @POST(IEndpointType.BASE_ENDPOINT_PATH)
    suspend fun getUserWithStatistic(
        @Body request: GraphQLOperationRequest<GetUserWithStatisticVariables>,
    ): Response<GraphQLResponse<UserModelContainer.WithStatistic>>

    @POST(IEndpointType.BASE_ENDPOINT_PATH)
    suspend fun getUserProfileOverview(
        @Body request: GraphQLOperationRequest<GetUserProfileOverviewVariables>,
    ): Response<GraphQLResponse<UserSidecarModelContainer.Overview>>

    @POST(IEndpointType.BASE_ENDPOINT_PATH)
    suspend fun getUserProfileFeed(
        @Body request: GraphQLOperationRequest<GetUserProfileFeedVariables>,
    ): Response<GraphQLResponse<UserSidecarModelContainer.Feed>>

    @POST(IEndpointType.BASE_ENDPOINT_PATH)
    suspend fun saveToggleFollow(
        @Body request: GraphQLOperationRequest<SaveToggleFollowUserVariables>,
    ): Response<GraphQLResponse<UserModelContainer.User>>

    @POST(IEndpointType.BASE_ENDPOINT_PATH)
    suspend fun updateUserProfile(
        @Body request: GraphQLOperationRequest<UpdateUserProfileVariables>,
    ): Response<GraphQLResponse<UserModelContainer.Profile>>

    @POST(IEndpointType.BASE_ENDPOINT_PATH)
    suspend fun getAnimeFavourites(
        @Body request: GraphQLOperationRequest<GetAnimeFavouritesVariables>,
    ): Response<GraphQLResponse<UserSidecarModelContainer.AnimeFavourites>>

    @POST(IEndpointType.BASE_ENDPOINT_PATH)
    suspend fun getMangaFavourites(
        @Body request: GraphQLOperationRequest<GetMangaFavouritesVariables>,
    ): Response<GraphQLResponse<UserSidecarModelContainer.MangaFavourites>>
}
