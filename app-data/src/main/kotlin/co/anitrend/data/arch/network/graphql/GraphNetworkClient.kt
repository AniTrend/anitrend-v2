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

package co.anitrend.data.arch.network.graphql

import co.anitrend.data.api.model.GraphQLResponse
import co.anitrend.data.arch.network.contract.NetworkClient
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import retrofit2.Response
import timber.log.Timber

/**
 * GraphQL network client
 *
 * @param gson Converter for decoding error bodies.
 * Using gson until kotlinx.serializer supports generics
 */
internal class GraphNetworkClient<R>(
    private val json: Json,
    override val dispatcher: CoroutineDispatcher
) : NetworkClient<GraphQLResponse<R>>() {

    private fun getGraphQLError(errorBodyString: String?): GraphQLResponse<R> {
        val body = requireNotNull(errorBodyString)
        return json.decodeFromString(body)
    }

    private fun Response<GraphQLResponse<R>>.responseErrors(): GraphQLResponse<R> {
        runCatching {
            val errorBodyString = errorBody()?.string()
            getGraphQLError(errorBodyString)
        }.onSuccess { error ->
            return error
        }.onFailure { exception ->
            Timber.tag(moduleTag).w(exception)
        }

        throw HttpException(this)
    }

    /**
     * @return [Response.body] of the response
     *
     * @throws HttpException When the request was not successful
     */
    override fun Response<GraphQLResponse<R>>.bodyOrThrow(): GraphQLResponse<R> {
        if (!isSuccessful)
            return responseErrors()

        return requireNotNull(body()) {
            "Response<T>.bodyOrThrow() -> response body was null"
        }
    }
}