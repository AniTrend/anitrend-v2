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
package co.anitrend.data.media.datasource.remote

import co.anitrend.data.core.api.factory.contract.IEndpointType
import co.anitrend.retrofit.graphql.model.GraphQLResponse
import co.anitrend.data.graphql.anilist.GetMediaDetailVariables
import co.anitrend.data.graphql.anilist.GetMediaPagedVariables
import co.anitrend.data.graphql.anilist.GetMediaStatsVariables
import co.anitrend.data.graphql.anilist.GetMediaWithCharacterVariables
import co.anitrend.data.graphql.anilist.GetMediaWithRelationVariables
import co.anitrend.data.graphql.anilist.GetMediaWithStaffVariables
import co.anitrend.data.graphql.anilist.GetMediaWithStudioVariables
import co.anitrend.data.graphql.anilist.GetMediaWithSuggestionVariables
import co.anitrend.data.media.model.container.MediaConnectionModelContainer
import co.anitrend.data.media.model.container.MediaModelContainer
import co.anitrend.data.media.model.container.MediaSidecarModelContainer
import co.anitrend.data.media.model.container.MediaPeopleModelContainer
import co.anitrend.retrofit.graphql.model.request.GraphQLOperationRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

internal interface MediaRemoteSource {
    @POST(IEndpointType.BASE_ENDPOINT_PATH)
    suspend fun getMediaPaged(
        @Body request: GraphQLOperationRequest<GetMediaPagedVariables>,
    ): Response<GraphQLResponse<MediaModelContainer.Paged>>

    @POST(IEndpointType.BASE_ENDPOINT_PATH)
    suspend fun getMediaDetail(
        @Body request: GraphQLOperationRequest<GetMediaDetailVariables>,
    ): Response<GraphQLResponse<MediaModelContainer.Detail>>

    @POST(IEndpointType.BASE_ENDPOINT_PATH)
    suspend fun getMediaCharacters(
        @Body request: GraphQLOperationRequest<GetMediaWithCharacterVariables>,
    ): Response<GraphQLResponse<MediaPeopleModelContainer.Characters>>

    @POST(IEndpointType.BASE_ENDPOINT_PATH)
    suspend fun getMediaStaff(
        @Body request: GraphQLOperationRequest<GetMediaWithStaffVariables>,
    ): Response<GraphQLResponse<MediaPeopleModelContainer.Staff>>

    @POST(IEndpointType.BASE_ENDPOINT_PATH)
    suspend fun getMediaRelations(
        @Body request: GraphQLOperationRequest<GetMediaWithRelationVariables>,
    ): Response<GraphQLResponse<MediaConnectionModelContainer.Relations>>

    @POST(IEndpointType.BASE_ENDPOINT_PATH)
    suspend fun getMediaRecommendations(
        @Body request: GraphQLOperationRequest<GetMediaWithSuggestionVariables>,
    ): Response<GraphQLResponse<MediaConnectionModelContainer.Recommendations>>

    @POST(IEndpointType.BASE_ENDPOINT_PATH)
    suspend fun getMediaStudios(
        @Body request: GraphQLOperationRequest<GetMediaWithStudioVariables>,
    ): Response<GraphQLResponse<MediaSidecarModelContainer.Studios>>

    @POST(IEndpointType.BASE_ENDPOINT_PATH)
    suspend fun getMediaStats(
        @Body request: GraphQLOperationRequest<GetMediaStatsVariables>,
    ): Response<GraphQLResponse<MediaSidecarModelContainer.Stats>>
}
