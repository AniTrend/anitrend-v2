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

package co.anitrend.data.arch.controller

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingRequestHelper
import co.anitrend.arch.data.common.ISupportPagingResponse
import co.anitrend.arch.data.common.ISupportResponse
import co.anitrend.arch.domain.entities.NetworkState
import co.anitrend.arch.extension.network.SupportConnectivity
import co.anitrend.data.arch.mapper.GraphQLMapper
import io.github.wax911.library.model.body.GraphContainer
import io.github.wax911.library.util.getError
import kotlinx.coroutines.Deferred
import retrofit2.Response
import timber.log.Timber

/**
 * AniTrend controller that handles complex logic of making requests, capturing errors,
 * notifying state observers and providing input to response mappers
 *
 * @see GraphQLMapper
 */
internal class GraphQLController<S, D> private constructor(
    private val responseMapper: GraphQLMapper<S, D>,
    private val supportConnectivity: SupportConnectivity
) : ISupportResponse<Deferred<Response<GraphContainer<S>>>, D>,
    ISupportPagingResponse<Deferred<Response<GraphContainer<S>>>> {

    private val moduleTag: String = javaClass.simpleName

    private suspend fun connectedRun(
        block: suspend () -> D?,
        networkState: MutableLiveData<NetworkState>
    ): D? {
        if (supportConnectivity.isConnected)
            return block()

        networkState.postValue(
            NetworkState.Error(
                heading = "No Internet Connection",
                message = "Please check your internet connection"
            )
        )
        return null
    }

    private suspend fun connectedRun(
        block: suspend () -> Unit,
        pagingRequestHelper: PagingRequestHelper.Request.Callback
    ) {
        if (supportConnectivity.isConnected)
            block()
        else
            pagingRequestHelper.recordFailure(
                Throwable("Please check your internet connection")
            )
    }

    /**
     * Response handler for coroutine contexts which need to observe [NetworkState]
     *
     * @param resource awaiting execution
     * @param networkState for the deferred result
     *
     * @return resource fetched if present
     */
    override suspend fun invoke(
        resource: Deferred<Response<GraphContainer<S>>>,
        networkState: MutableLiveData<NetworkState>
    ): D? {
        return connectedRun({
            networkState.postValue(NetworkState.Loading)
            val result = runCatching {
                val response = resource.await()
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody?.errors.isNullOrEmpty()) {
                        val result = if (responseBody != null) {
                            val mapped = responseMapper.onResponseMapFrom(responseBody)
                            responseMapper.onResponseDatabaseInsert(mapped)
                            mapped
                        } else null
                        networkState.postValue(NetworkState.Success)
                        result
                    } else {
                        val errors = responseBody?.errors
                        errors?.forEach {
                            Timber.tag(moduleTag).e("${it.message} | Status: ${it.status}")
                        }
                        networkState.postValue(
                            NetworkState.Error(
                                heading = "Request Unable to Complete Successfully",
                                message = errors?.firstOrNull()?.message
                            )
                        )
                        null
                    }
                } else {
                    val graphErrors = response.getError()
                    graphErrors?.forEach {
                        Timber.tag(moduleTag).e(
                            "Request failed: ${it.message} | Status: ${it.status} | Code: ${response.code()}"
                        )
                    }

                    networkState.postValue(
                        NetworkState.Error(
                            heading = "Server Request/Response Error",
                            message = graphErrors?.firstOrNull()?.message ?: response.message()
                        )
                    )
                    null
                }
            }

            result.getOrElse {
                it.printStackTrace()
                networkState.postValue(
                    NetworkState.Error(
                        heading = "Internal Application Error",
                        message = it.message
                    )
                )
                null
            }
        }, networkState)
    }

    /**
     * Response handler for coroutine contexts, mainly for paging
     *
     * @param resource awaiting execution
     * @param pagingRequestHelper optional paging request callback
     */
    override suspend fun invoke(
        resource: Deferred<Response<GraphContainer<S>>>,
        pagingRequestHelper: PagingRequestHelper.Request.Callback
    ) {
        connectedRun({
            val result = runCatching {
                val response = resource.await()
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        if (responseBody.errors.isNullOrEmpty()) {
                            val mapped = responseMapper.onResponseMapFrom(responseBody)
                            responseMapper.onResponseDatabaseInsert(mapped)
                        } else {
                            val errors = responseBody.errors
                            errors?.forEach {
                                Timber.tag(moduleTag).e("${it.message} | Status: ${it.status}")
                            }
                            pagingRequestHelper.recordFailure(
                                Throwable(errors?.first()?.message ?: "Unknown error occurred")
                            )
                        }
                    }
                    pagingRequestHelper.recordSuccess()
                } else {
                    val graphErrors = response.getError()
                    graphErrors?.forEach {
                        Timber.tag(moduleTag).e(
                            "Request failed: ${it.message} | Status: ${it.status} | Code: ${response.code()}"
                        )
                    }
                    pagingRequestHelper.recordFailure(
                        Throwable(graphErrors?.first()?.message ?: response.message())
                    )
                }
            }

            result.getOrElse {
                it.printStackTrace()
                Timber.tag(moduleTag).e(it)
                pagingRequestHelper.recordFailure(it)
            }
        }, pagingRequestHelper)
    }

    companion object {
        fun <S, D> newInstance(
            responseMapper: GraphQLMapper<S, D>,
            supportConnectivity: SupportConnectivity
        ) = GraphQLController(responseMapper, supportConnectivity)
    }
}