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

package co.anitrend.data.arch.controller.graphql

import co.anitrend.arch.data.common.ISupportResponse
import co.anitrend.arch.data.request.callback.RequestCallback
import co.anitrend.arch.data.request.error.RequestError
import co.anitrend.arch.domain.entities.NetworkState
import co.anitrend.data.api.model.GraphQLResponse
import co.anitrend.data.arch.controller.strategy.contract.ControllerStrategy
import co.anitrend.data.arch.mapper.DefaultMapper
import co.anitrend.data.arch.network.contract.NetworkClient
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.withContext
import retrofit2.Response
import timber.log.Timber

/**
 * AniTrend controller that handles complex logic of making requests, capturing errors,
 * notifying state observers and providing input to response mappers
 */
internal class GraphQLController<S, out D>(
    private val mapper: DefaultMapper<S, D>,
    private val strategy: ControllerStrategy<D>,
    private val dispatcher: CoroutineDispatcher,
    private val client: NetworkClient<GraphQLResponse<S>>
) : ISupportResponse<Deferred<Response<GraphQLResponse<S>>>, D> {

    /**
     * Response handler for coroutine contexts which need to observe [NetworkState]
     *
     * @param resource awaiting execution
     * @param requestCallback for the deferred result
     *
     * @return resource fetched if present
     */
    override suspend fun invoke(
        resource: Deferred<Response<GraphQLResponse<S>>>,
        requestCallback: RequestCallback
    ) = strategy(requestCallback) {

        val response = client.fetch(resource)

        val error = response.errors?.onEach {
            Timber.tag(moduleTag).w(it.toString())
        }?.firstOrNull()

        /**
         * Only throw if data is null while we have an error, [strategy] will catch
         * the exception and emit a network state for the user to see
         */
        if (error != null && response.data == null)
            throw RequestError(
                topic = "Error with request",
                description = error.message,
                throwable = null
            )

        response.data?.let { data ->
            val mapped = mapper.onResponseMapFrom(data)
            withContext(dispatcher) {
                mapper.onResponseDatabaseInsert(mapped)
            }
            mapped
        }
    }


    companion object {
        private val moduleTag = GraphQLController::class.java.simpleName
    }
}