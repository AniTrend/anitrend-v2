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
package co.anitrend.data.studio.datasource.remote

import co.anitrend.data.core.GRAPHQL
import co.anitrend.data.core.api.factory.contract.IEndpointType
import co.anitrend.data.core.api.model.GraphQLResponse
import co.anitrend.data.graphql.anilist.GetStudioDetailVariables
import co.anitrend.data.graphql.anilist.GetStudioPagedVariables
import co.anitrend.data.studio.model.remote.StudioDetailContainer
import co.anitrend.data.studio.model.remote.StudioPagedContainer
import co.anitrend.retrofit.graphql.model.GraphQLRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

internal interface StudioRemoteSource {
    @GRAPHQL
    @POST(IEndpointType.BASE_ENDPOINT_PATH)
    suspend fun getStudioDetail(
        @Body request: GraphQLRequest<GetStudioDetailVariables>,
    ): Response<GraphQLResponse<StudioDetailContainer>>

    @GRAPHQL
    @POST(IEndpointType.BASE_ENDPOINT_PATH)
    suspend fun getStudioPaged(
        @Body request: GraphQLRequest<GetStudioPagedVariables>,
    ): Response<GraphQLResponse<StudioPagedContainer>>
}
