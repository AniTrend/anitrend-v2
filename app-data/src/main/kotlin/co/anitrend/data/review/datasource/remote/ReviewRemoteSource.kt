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

package co.anitrend.data.review.datasource.remote

import co.anitrend.data.core.GRAPHQL
import co.anitrend.data.core.api.factory.contract.EndpointType
import co.anitrend.data.core.api.model.GraphQLResponse
import co.anitrend.data.review.model.container.ReviewContainerModel
import io.github.wax911.library.annotation.GraphQuery
import io.github.wax911.library.model.request.QueryContainerBuilder
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

internal interface ReviewRemoteSource {

    @GRAPHQL
    @POST(EndpointType.BASE_ENDPOINT_PATH)
    @GraphQuery("GetReview")
    suspend fun getReview(
        @Body queryContainer: QueryContainerBuilder
    ): Response<GraphQLResponse<ReviewContainerModel.Entry>>

    @GRAPHQL
    @POST(EndpointType.BASE_ENDPOINT_PATH)
    @GraphQuery("GetReviewPaged")
    suspend fun getReviewPaged(
        @Body queryContainer: QueryContainerBuilder
    ): Response<GraphQLResponse<ReviewContainerModel.Paged>>

    @GRAPHQL
    @POST(EndpointType.BASE_ENDPOINT_PATH)
    @GraphQuery("SaveReview")
    suspend fun saveReview(
        @Body queryContainer: QueryContainerBuilder
    ): Response<GraphQLResponse<ReviewContainerModel.SavedEntry>>

    @GRAPHQL
    @POST(EndpointType.BASE_ENDPOINT_PATH)
    @GraphQuery("RateReview")
    suspend fun rateReview(
        @Body queryContainer: QueryContainerBuilder
    ): Response<GraphQLResponse<ReviewContainerModel.RatedEntry>>

    @GRAPHQL
    @POST(EndpointType.BASE_ENDPOINT_PATH)
    @GraphQuery("DeleteReview")
    suspend fun deleteReview(
        @Body queryContainer: QueryContainerBuilder
    ): Response<GraphQLResponse<ReviewContainerModel.DeletedEntry>>
}