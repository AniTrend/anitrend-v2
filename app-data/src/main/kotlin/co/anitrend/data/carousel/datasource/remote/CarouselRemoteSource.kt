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

package co.anitrend.data.carousel.datasource.remote

import co.anitrend.data.carousel.model.CarouselModel
import co.anitrend.data.core.GRAPHQL
import co.anitrend.data.core.api.factory.contract.EndpointType
import co.anitrend.data.core.api.model.GraphQLResponse
import io.github.wax911.library.annotation.GraphQuery
import io.github.wax911.library.model.request.QueryContainerBuilder
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

internal interface CarouselRemoteSource {

    @GRAPHQL
    @POST(EndpointType.BASE_ENDPOINT_PATH)
    @GraphQuery("Carousel")
    suspend fun getCarousel(
        @Body queryContainer: QueryContainerBuilder
    ): Response<GraphQLResponse<CarouselModel.Core>>

    @GRAPHQL
    @POST(EndpointType.BASE_ENDPOINT_PATH)
    @GraphQuery("CarouselAnime")
    suspend fun getCarouselAnime(
        @Body queryContainer: QueryContainerBuilder
    ): Response<GraphQLResponse<CarouselModel.Anime>>

    @GRAPHQL
    @POST(EndpointType.BASE_ENDPOINT_PATH)
    @GraphQuery("CarouselManga")
    suspend fun getCarouselManga(
        @Body queryContainer: QueryContainerBuilder
    ): Response<GraphQLResponse<CarouselModel.Manga>>
}