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
package co.anitrend.data.review.datasource.remote

import co.anitrend.data.core.GRAPHQL
import co.anitrend.data.core.api.model.GraphQLResponse
import co.anitrend.data.graphql.anilist.DeleteReviewVariables
import co.anitrend.data.graphql.anilist.GetReviewPagedVariables
import co.anitrend.data.graphql.anilist.GetReviewVariables
import co.anitrend.data.graphql.anilist.RateReviewVariables
import co.anitrend.data.graphql.anilist.SaveReviewVariables
import co.anitrend.data.review.model.container.ReviewContainerModel
import co.anitrend.retrofit.graphql.model.GraphQLRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

internal interface ReviewRemoteSource {
    @GRAPHQL
    @POST
    suspend fun getReview(
        @Body request: GraphQLRequest<GetReviewVariables>,
    ): Response<GraphQLResponse<ReviewContainerModel.Entry>>

    @GRAPHQL
    @POST
    suspend fun getReviewPaged(
        @Body request: GraphQLRequest<GetReviewPagedVariables>,
    ): Response<GraphQLResponse<ReviewContainerModel.Paged>>

    @GRAPHQL
    @POST
    suspend fun saveReview(
        @Body request: GraphQLRequest<SaveReviewVariables>,
    ): Response<GraphQLResponse<ReviewContainerModel.SavedEntry>>

    @GRAPHQL
    @POST
    suspend fun rateReview(
        @Body request: GraphQLRequest<RateReviewVariables>,
    ): Response<GraphQLResponse<ReviewContainerModel.RatedEntry>>

    @GRAPHQL
    @POST
    suspend fun deleteReview(
        @Body request: GraphQLRequest<DeleteReviewVariables>,
    ): Response<GraphQLResponse<ReviewContainerModel.DeletedEntry>>
}
