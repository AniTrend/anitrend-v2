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

package co.anitrend.data.airing.datasource.remote

import co.anitrend.data.airing.model.container.AiringScheduleModelContainer
import co.anitrend.data.core.api.factory.contract.EndpointType
import co.anitrend.data.core.api.model.GraphQLResponse
import co.anitrend.data.core.GRAPHQL
import io.github.wax911.library.annotation.GraphQuery
import io.github.wax911.library.model.request.QueryContainerBuilder
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

internal interface AiringRemoteSource {

    @GRAPHQL
    @POST(EndpointType.BASE_ENDPOINT_PATH)
    @GraphQuery("GetAiringPaged")
    suspend fun getAiringPaged(
        @Body queryContainer: QueryContainerBuilder
    ): Response<GraphQLResponse<AiringScheduleModelContainer.Paged>>
}