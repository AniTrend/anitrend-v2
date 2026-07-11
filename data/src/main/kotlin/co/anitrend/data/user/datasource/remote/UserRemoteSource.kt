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
import co.anitrend.data.core.api.model.GraphQLResponse
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
import co.anitrend.retrofit.graphql.model.GraphQLRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

internal interface UserRemoteSource {
    @POST(IEndpointType.BASE_ENDPOINT_PATH)
    suspend fun getUserByName(
        @Body request: GraphQLRequest<GetUserVariables>,
    ): Response<GraphQLResponse<UserModelContainer.User>>

    @POST(IEndpointType.BASE_ENDPOINT_PATH)
    suspend fun getUserPaged(
        @Body request: GraphQLRequest<GetUserPagedVariables>,
    ): Response<GraphQLResponse<UserModelContainer.Paged>>

    @POST(IEndpointType.BASE_ENDPOINT_PATH)
    suspend fun getUserViewer(
        @Body request: GraphQLRequest<EmptyGraphQLVariables>,
    ): Response<GraphQLResponse<UserModelContainer.Viewer>>

    @POST(IEndpointType.BASE_ENDPOINT_PATH)
    suspend fun getUserProfile(
        @Body request: GraphQLRequest<GetUserProfileVariables>,
    ): Response<GraphQLResponse<UserModelContainer.Profile>>

    @POST(IEndpointType.BASE_ENDPOINT_PATH)
    suspend fun getUserWithStatistic(
        @Body request: GraphQLRequest<GetUserWithStatisticVariables>,
    ): Response<GraphQLResponse<UserModelContainer.WithStatistic>>

    @POST(IEndpointType.BASE_ENDPOINT_PATH)
    suspend fun getUserProfileOverview(
        @Body request: GraphQLRequest<GetUserProfileOverviewVariables>,
    ): Response<GraphQLResponse<UserSidecarModelContainer.Overview>>

    @POST(IEndpointType.BASE_ENDPOINT_PATH)
    suspend fun getUserProfileFeed(
        @Body request: GraphQLRequest<GetUserProfileFeedVariables>,
    ): Response<GraphQLResponse<UserSidecarModelContainer.Feed>>

    @POST(IEndpointType.BASE_ENDPOINT_PATH)
    suspend fun saveToggleFollow(
        @Body request: GraphQLRequest<SaveToggleFollowUserVariables>,
    ): Response<GraphQLResponse<UserModelContainer.User>>

    @POST(IEndpointType.BASE_ENDPOINT_PATH)
    suspend fun updateUserProfile(
        @Body request: GraphQLRequest<UpdateUserProfileVariables>,
    ): Response<GraphQLResponse<UserModelContainer.Profile>>

    @POST(IEndpointType.BASE_ENDPOINT_PATH)
    suspend fun getAnimeFavourites(
        @Body request: GraphQLRequest<GetAnimeFavouritesVariables>,
    ): Response<GraphQLResponse<UserSidecarModelContainer.AnimeFavourites>>

    @POST(IEndpointType.BASE_ENDPOINT_PATH)
    suspend fun getMangaFavourites(
        @Body request: GraphQLRequest<GetMangaFavouritesVariables>,
    ): Response<GraphQLResponse<UserSidecarModelContainer.MangaFavourites>>
}
