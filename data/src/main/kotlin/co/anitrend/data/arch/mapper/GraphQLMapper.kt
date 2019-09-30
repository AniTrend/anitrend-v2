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

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingRequestHelper
import co.anitrend.arch.data.common.ISupportPagingResponse
import co.anitrend.arch.data.common.ISupportResponse
import co.anitrend.arch.data.mapper.SupportResponseMapper
import io.github.wax911.library.model.body.GraphContainer
import io.github.wax911.library.util.getError
import co.anitrend.arch.domain.entities.NetworkState
import kotlinx.coroutines.Deferred
import retrofit2.Response
import timber.log.Timber

/**
 * GraphQLMapper specific mapper, assures that all requests respond with [GraphContainer] as the root tree object.
 * Making it easier for us to implement error logging and provide better error messages
 */
abstract class GraphQLMapper<S, D> :
    SupportResponseMapper<GraphContainer<S>, D>(),
    ISupportResponse<Deferred<Response<GraphContainer<S>>>, D>,
    ISupportPagingResponse<Deferred<Response<GraphContainer<S>>>> {

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
        val result = runCatching {
            val response = resource.await()
            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody?.errors.isNullOrEmpty()) {
                    val result = if (responseBody != null) {
                        val mapped = onResponseMapFrom(responseBody)
                        onResponseDatabaseInsert(mapped)
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

        return result.getOrElse {
            it.printStackTrace()
            networkState.postValue(
                NetworkState.Error(
                    heading = "Internal Application Error",
                    message = it.message
                )
            )
            null
        }
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
        val result = runCatching {
            val response = resource.await()
            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null) {
                    if (responseBody.errors.isNullOrEmpty()) {
                        val mapped = onResponseMapFrom(responseBody)
                        onResponseDatabaseInsert(mapped)
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
    }
}