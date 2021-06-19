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

package co.anitrend.data.medialist.datasource.remote

import co.anitrend.data.core.api.factory.contract.EndpointType
import co.anitrend.data.core.api.model.GraphQLResponse
import co.anitrend.data.core.GRAPHQL
import co.anitrend.data.medialist.model.container.MediaListContainerModel
import io.github.wax911.library.annotation.GraphQuery
import io.github.wax911.library.model.request.QueryContainerBuilder
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

internal interface MediaListRemoteSource {

    @GRAPHQL
    @POST(EndpointType.BASE_ENDPOINT_PATH)
    @GraphQuery("GetMediaListPaged")
    suspend fun getMediaListPaged(
        @Body queryContainer: QueryContainerBuilder
    ): Response<GraphQLResponse<MediaListContainerModel.Paged>>

    @GRAPHQL
    @POST(EndpointType.BASE_ENDPOINT_PATH)
    @GraphQuery("GetMediaListCollection")
    suspend fun getMediaListCollection(
        @Body queryContainer: QueryContainerBuilder
    ): Response<GraphQLResponse<MediaListContainerModel.Collection>>

    @GRAPHQL
    @POST(EndpointType.BASE_ENDPOINT_PATH)
    @GraphQuery("GetMediaListEntry")
    suspend fun getMediaListEntry(
        @Body queryContainer: QueryContainerBuilder
    ): Response<GraphQLResponse<MediaListContainerModel.Entry>>

    @GRAPHQL
    @POST(EndpointType.BASE_ENDPOINT_PATH)
    @GraphQuery("SaveMediaListEntries")
    suspend fun saveMediaListEntries(
        @Body queryContainer: QueryContainerBuilder
    ): Response<GraphQLResponse<MediaListContainerModel.SavedEntries>>

    @GRAPHQL
    @POST(EndpointType.BASE_ENDPOINT_PATH)
    @GraphQuery("SaveMediaListEntry")
    suspend fun saveMediaListEntry(
        @Body queryContainer: QueryContainerBuilder
    ): Response<GraphQLResponse<MediaListContainerModel.SavedEntry>>

    @GRAPHQL
    @POST(EndpointType.BASE_ENDPOINT_PATH)
    @GraphQuery("DeleteMediaListEntry")
    suspend fun deleteMediaListEntry(
        @Body queryContainer: QueryContainerBuilder
    ): Response<GraphQLResponse<MediaListContainerModel.DeletedEntry>>

    @GRAPHQL
    @POST(EndpointType.BASE_ENDPOINT_PATH)
    @GraphQuery("DeleteCustomList")
    suspend fun deleteCustomList(
        @Body queryContainer: QueryContainerBuilder
    ): Response<GraphQLResponse<MediaListContainerModel.DeletedCustomList>>
}