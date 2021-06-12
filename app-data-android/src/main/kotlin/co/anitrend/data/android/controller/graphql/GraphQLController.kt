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

package co.anitrend.data.android.controller.graphql

import co.anitrend.arch.data.common.ISupportResponse
import co.anitrend.arch.data.request.callback.RequestCallback
import co.anitrend.arch.domain.entities.RequestError
import co.anitrend.data.core.api.model.GraphQLResponse
import co.anitrend.data.android.controller.strategy.contract.ControllerStrategy
import co.anitrend.data.android.mapper.DefaultMapper
import co.anitrend.data.android.network.client.DeferrableNetworkClient
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.withContext
import retrofit2.Response
import timber.log.Timber

/**
 * AniTrend controller that handles complex logic of making requests, capturing errors,
 * notifying state observers and providing input to response mappers
 */
class GraphQLController<S, out D>(
    private val mapper: DefaultMapper<S, D>,
    private val strategy: ControllerStrategy<D>,
    private val dispatcher: CoroutineDispatcher,
    private val client: DeferrableNetworkClient<GraphQLResponse<S>>
) : ISupportResponse<Deferred<Response<GraphQLResponse<S>>>, D> {

    /**
     * Response handler for coroutine contexts which need to observe [LoadState]
     *
     * @param resource awaiting execution
     * @param requestCallback for the deferred result
     * @param interceptor allows you to access certain network model fields 
     * which are otherwise unaccessable from the domain/entity level
     *
     * @return resource fetched if present
     */
    suspend operator fun invoke(
        resource: Deferred<Response<GraphQLResponse<S>>>,
        requestCallback: RequestCallback,
        interceptor: (S) -> S
    ) = strategy(requestCallback) {

        val response = client.fetch(resource)

        val error = response.errors?.onEach {
            Timber.w(it.toString())
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

        response.data?.let {
            val data = interceptor(it)
            val mapped = mapper.onResponseMapFrom(data)
            withContext(dispatcher) {
                mapper.onResponseDatabaseInsert(mapped)
            }
            mapped
        }
    }

    /**
     * Response handler for coroutine contexts which need to observe [LoadState]
     *
     * @param resource awaiting execution
     * @param requestCallback for the deferred result
     *
     * @return resource fetched if present
     */
    override suspend fun invoke(
        resource: Deferred<Response<GraphQLResponse<S>>>,
        requestCallback: RequestCallback
    ) = invoke(resource, requestCallback) { it }
}