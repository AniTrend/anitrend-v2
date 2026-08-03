/*
 * Copyright (C) 2020 AniTrend
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
package co.anitrend.data.android.network.graphql

import co.anitrend.data.android.network.client.DeferrableNetworkClient
import co.anitrend.retrofit.graphql.model.GraphQLData
import co.anitrend.retrofit.graphql.model.GraphQLResponse
import co.anitrend.retrofit.graphql.model.GraphQLResponseError
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineDispatcher
import retrofit2.Response
import timber.log.Timber

/**
 * GraphQL network client
 *
 * @param gson Converter for decoding error bodies.
 * Using gson until kotlinx.serializer supports generics
 */
class GraphNetworkClient<R>(
    private val gson: Gson,
    override val dispatcher: CoroutineDispatcher,
) : DeferrableNetworkClient<GraphQLResponse<R>>() {
    private fun getGraphQLError(errorBodyString: String?): GraphQLResponse<R> {
        val body = requireNotNull(errorBodyString)
        val message = extractErrorMessage(body) ?: body
        return GraphQLResponse(
            data = GraphQLData.Absent,
            errors = listOf(GraphQLResponseError(message = message)),
        )
    }

    private fun extractErrorMessage(body: String): String? =
        runCatching {
            gson
                .fromJson(body, JsonObject::class.java)
                ?.getAsJsonArray("errors")
                ?.firstOrNull()
                ?.asJsonObject
                ?.get("message")
                ?.asString
        }.onFailure(Timber::w)
            .getOrNull()

    private fun Response<GraphQLResponse<R>>.responseErrors(): GraphQLResponse<R> {
        val errorBodyString = errorBody()?.string()
        return runCatching {
            getGraphQLError(errorBodyString)
        }.onFailure(Timber::w)
            .getOrElse { exception ->
                GraphQLResponse(
                    data = GraphQLData.Absent,
                    errors =
                        listOf(
                            GraphQLResponseError(
                                message = exception.message.orEmpty(),
                            ),
                        ),
                )
            }
    }

    /**
     * @return [Response.body] of the response
     *
     * @throws HttpException When the request was not successful
     */
    override fun Response<GraphQLResponse<R>>.bodyOrThrow(): GraphQLResponse<R> {
        if (!isSuccessful) {
            return responseErrors()
        }

        return requireNotNull(body()) {
            "Response<T>.bodyOrThrow() -> response body was null"
        }
    }
}
