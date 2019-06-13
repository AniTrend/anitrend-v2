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

package co.anitrend.data.arch.mapper

import io.github.wax911.library.model.body.GraphContainer
import io.github.wax911.library.util.getError
import io.wax911.support.data.mapper.SupportDataMapper
import io.wax911.support.data.mapper.contract.IMapperHelper
import io.wax911.support.data.model.NetworkState
import io.wax911.support.data.model.contract.SupportStateContract
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

/**
 * GraphQLMapper specific mapper, assures that all requests respond with [GraphContainer] as the root tree object.
 * Making it easier for us to implement error logging and provide better error messages
 *
 * @see SupportDataMapper
 */
abstract class GraphQLMapper<S, D> (
    parentCoroutineJob: Job? = null
): SupportDataMapper<GraphContainer<S>, D>(parentCoroutineJob), IMapperHelper<Response<GraphContainer<S>>> {

    /**
     * Response handler for coroutine contexts which need to observe
     * the live data of [NetworkState]
     *
     * Unless when if using [androidx.paging.PagingRequestHelper.Request.Callback]
     * then you can ignore the return type
     *
     * @param deferred an deferred result awaiting execution
     * @return network state of the deferred result
     */
    override suspend fun handleResponse(deferred: Deferred<Response<GraphContainer<S>>>): NetworkState {
        val response = deferred.await()

        if (response.isSuccessful && response.body() != null) {
            val mapped = onResponseMapFrom(response.body()!!)
            onResponseDatabaseInsert(mapped)

            return NetworkState.LOADED
        }

        val graphErrors = response.getError()
        if (!graphErrors.isNullOrEmpty()) {
            graphErrors.forEach {
                Timber.tag(moduleTag).e("${it.message} | Status: ${it.status}")
            }
            return NetworkState(
                status = SupportStateContract.ERROR,
                message = graphErrors.first().message,
                code = graphErrors.first().status
            )
        }

        return NetworkState.error("Unable to process exception")
    }
}