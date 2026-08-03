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
package co.anitrend.data.medialist.datasource.remote

import co.anitrend.data.core.api.factory.contract.IEndpointType
import co.anitrend.retrofit.graphql.model.GraphQLResponse
import co.anitrend.data.graphql.anilist.DeleteCustomListVariables
import co.anitrend.data.graphql.anilist.DeleteMediaListItemVariables
import co.anitrend.data.graphql.anilist.GetMediaListCollectionVariables
import co.anitrend.data.graphql.anilist.GetMediaListEntryVariables
import co.anitrend.data.graphql.anilist.GetMediaListPagedVariables
import co.anitrend.data.graphql.anilist.SaveMediaListEntriesVariables
import co.anitrend.data.graphql.anilist.SaveMediaListEntryVariables
import co.anitrend.data.medialist.model.container.MediaListContainerModel
import co.anitrend.retrofit.graphql.model.request.GraphQLOperationRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

internal interface MediaListRemoteSource {
    @POST(IEndpointType.BASE_ENDPOINT_PATH)
    suspend fun getMediaListPaged(
        @Body request: GraphQLOperationRequest<GetMediaListPagedVariables>,
    ): Response<GraphQLResponse<MediaListContainerModel.Paged>>

    @POST(IEndpointType.BASE_ENDPOINT_PATH)
    suspend fun getMediaListCollection(
        @Body request: GraphQLOperationRequest<GetMediaListCollectionVariables>,
    ): Response<GraphQLResponse<MediaListContainerModel.Collection>>

    @POST(IEndpointType.BASE_ENDPOINT_PATH)
    suspend fun getMediaListEntry(
        @Body request: GraphQLOperationRequest<GetMediaListEntryVariables>,
    ): Response<GraphQLResponse<MediaListContainerModel.Entry>>

    @POST(IEndpointType.BASE_ENDPOINT_PATH)
    suspend fun saveMediaListEntries(
        @Body request: GraphQLOperationRequest<SaveMediaListEntriesVariables>,
    ): Response<GraphQLResponse<MediaListContainerModel.SavedEntries>>

    @POST(IEndpointType.BASE_ENDPOINT_PATH)
    suspend fun saveMediaListEntry(
        @Body request: GraphQLOperationRequest<SaveMediaListEntryVariables>,
    ): Response<GraphQLResponse<MediaListContainerModel.SavedEntry>>

    @POST(IEndpointType.BASE_ENDPOINT_PATH)
    suspend fun deleteMediaListEntry(
        @Body request: GraphQLOperationRequest<DeleteMediaListItemVariables>,
    ): Response<GraphQLResponse<MediaListContainerModel.DeletedEntry>>

    @POST(IEndpointType.BASE_ENDPOINT_PATH)
    suspend fun deleteCustomList(
        @Body request: GraphQLOperationRequest<DeleteCustomListVariables>,
    ): Response<GraphQLResponse<MediaListContainerModel.DeletedCustomList>>
}
