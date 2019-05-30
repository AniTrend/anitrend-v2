package co.anitrend.data.arch.mapper

import androidx.lifecycle.MutableLiveData
import io.github.wax911.library.model.body.GraphContainer
import io.github.wax911.library.util.getError
import io.wax911.support.data.model.NetworkState
import io.wax911.support.data.model.contract.SupportStateType
import io.wax911.support.data.source.mapper.SupportDataMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

/**
 * GraphQLMapper specific mapper, extends and overrides the response callback as we can
 * assure that all requests respond with [GraphContainer] as the root tree object
 * this makes it easier for us to implement error logging and provide better error messages
 *
 * @see SupportDataMapper
 */
abstract class GraphQLMapper<S, D> (
    parentCoroutineJob: Job? = null,
    networkState: MutableLiveData<NetworkState>
): SupportDataMapper<GraphContainer<S>?, D>(parentCoroutineJob, networkState) {

    /**
     * Synchronous network request handler for coroutine contexts which need cannot observe
     * the live data of [networkState]
     *
     * @param call GraphQL network request which needs to be executed
     */
    suspend fun executeUsing(call: Call<GraphContainer<S>>): NetworkState {
        try {
            val response = call.execute()
            if (response.isSuccessful) {
                withContext(Dispatchers.IO) {
                    val mapped = onResponseMapFrom(response)
                    onResponseDatabaseInsert(mapped)
                }
                return NetworkState.LOADED
            }

            val graphErrors = response.getError()
            if (!graphErrors.isNullOrEmpty()) {
                graphErrors.forEach {
                    Timber.tag(TAG).e("${it.message} | Status: ${it.status}")
                }
                return NetworkState(
                    status = SupportStateType.ERROR,
                    message = graphErrors.first().message,
                    code = graphErrors.first().status
                )
            }
        } catch (e: Exception) {
            Timber.tag(TAG).e(e)
        }
        return NetworkState.error(
            msg = "Unable to complete request"
        )
    }

    override val responseCallback = object : Callback<GraphContainer<S>?> {
        /**
         * Invoked when a network exception occurred talking to the server or when an unexpected
         * exception occurred creating the request or processing the response.
         */
        override fun onFailure(call: Call<GraphContainer<S>?>, throwable: Throwable) {
            networkState.postValue(
                NetworkState.error(
                    msg = "Unable to complete request"
                )
            )
            Timber.tag(TAG).e(throwable)
        }

        /**
         * Invoked for a received HTTP response.
         *
         *
         * Note: An HTTP response may still indicate an application-level failure such as a 404 or 500.
         * Call [Response.isSuccessful] to determine if the response indicates success.
         */
        override fun onResponse(call: Call<GraphContainer<S>?>, response: Response<GraphContainer<S>?>) {
            when (response.isSuccessful) {
                true -> {
                    launch {
                        val mapped = onResponseMapFrom(response)
                        onResponseDatabaseInsert(mapped)
                        withContext(Dispatchers.Main) {
                            networkState.postValue(
                                NetworkState.LOADED
                            )
                        }
                    }
                }
                false -> {
                    val graphErrors = response.getError()
                    if (!graphErrors.isNullOrEmpty()) {
                        networkState.postValue(
                            NetworkState(
                                status = SupportStateType.ERROR,
                                message = graphErrors.first().message,
                                code = graphErrors.first().status
                            )
                        )
                        graphErrors.forEach {
                            Timber.tag(TAG).e("${it.message} | Status: ${it.status}")
                        }
                    }
                    else {
                        val message = response.message()
                        Timber.tag(TAG).e(message)
                        networkState.postValue(
                            NetworkState(
                                status = SupportStateType.ERROR,
                                message = message,
                                code = response.code()
                            )
                        )
                    }
                }
            }
        }

    }
}