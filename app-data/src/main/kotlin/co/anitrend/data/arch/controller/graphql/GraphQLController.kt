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
import co.anitrend.arch.extension.dispatchers.SupportDispatchers
import co.anitrend.data.api.model.GraphQLResponse
import co.anitrend.data.arch.controller.strategy.contract.ControllerStrategy
import co.anitrend.data.arch.extension.fetchBodyWithRetry
import co.anitrend.data.arch.mapper.DefaultMapper
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.withContext
import retrofit2.Response
import timber.log.Timber

/**
 * AniTrend controller that handles complex logic of making requests, capturing errors,
 * notifying state observers and providing input to response mappers
 *
 * @see DefaultMapper
 */
internal class GraphQLController<S, out D> private constructor(
    private val mapper: DefaultMapper<S, D>,
    private val strategy: ControllerStrategy<D>,
    private val dispatchers: SupportDispatchers
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
        /**
         * Suppressing this because: https://discuss.kotlinlang.org/t/warning-inappropriate-blocking-method-call-with-coroutines-how-to-fix/16903
         */
        @Suppress("BlockingMethodInNonBlockingContext")
        val response = resource.fetchBodyWithRetry(dispatchers.io)

        val error = response.errors?.also { errors ->
            errors.forEach {
                Timber.tag(moduleTag).e(it.toString())
            }
        }?.firstOrNull()

        // Only throw if data is null while we have an error
        if (error != null && response.data == null)
            throw RequestError(
                topic = "Error with request",
                description = error.message,
                throwable = null
            )

        response.data?.let { data ->
            val mapped = mapper.onResponseMapFrom(data)
            withContext(dispatchers.io) {
                mapper.onResponseDatabaseInsert(mapped)
            }
            mapped
        }
    }

    companion object {
        private val moduleTag = GraphQLController::class.java.simpleName

        fun <S, D> newInstance(
            mapper: DefaultMapper<S, D>,
            strategy: ControllerStrategy<D>,
            dispatchers: SupportDispatchers
        ) = GraphQLController(
            mapper, strategy, dispatchers
        )
    }
}