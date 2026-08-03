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
package co.anitrend.data.character.datasource.remote

import co.anitrend.data.character.model.container.CharacterContainer
import co.anitrend.data.core.api.factory.contract.IEndpointType
import co.anitrend.retrofit.graphql.model.GraphQLResponse
import co.anitrend.data.graphql.anilist.GetCharacterPagedVariables
import co.anitrend.retrofit.graphql.model.request.GraphQLOperationRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

internal interface CharacterRemoteSource {
    @POST(IEndpointType.BASE_ENDPOINT_PATH)
    suspend fun getCharacterPaged(
        @Body request: GraphQLOperationRequest<GetCharacterPagedVariables>,
    ): Response<GraphQLResponse<CharacterContainer.Paged>>
}
