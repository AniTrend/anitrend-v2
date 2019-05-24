package co.anitrend.data.api

import io.github.wax911.library.util.getError
import io.wax911.support.core.controller.SupportRequestClient
import io.wax911.support.core.wrapper.RequestResult
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import retrofit2.Call
import timber.log.Timber

/**
 * Network client executor helper class, dispatches network calls & registers on going calls.
 * The class can cancel all on-going calls through [NetworkClient.cancel]
 */
class NetworkClient: SupportRequestClient() {

    /**
     * Executes the given retrofit call and returns a result. This function call
     * will require that you execute it in a async context to avoid exceptions
     * caused by running network calls on the main thread
     *
     * @param call retrofit call to execute
     */
    override fun <T> executeUsing(call: Call<T>): RequestResult<T?> {
        try {
            callList.add(call)
            val response = call.execute()

            if (!response.isSuccessful)
                response.getError()

            return RequestResult(
                response.code(),
                response.body(),
                response.headers(),
                response.errorBody()
            )
        } catch (e: Exception) {
            Timber.e(e)
            return RequestResult()
        }
    }

    /**
     * Executes the given retrofit call and returns a deferred result. This function call
     * will require that you call .await() to kick of the execution
     *
     * @param call retrofit call to execute
     */
    override fun <T> executeUsingAsync(call: Call<T>): Deferred<RequestResult<T?>> = async {
        return@async executeUsing(call)
    }

    companion object {
        const val HEADER_RATE_LIMIT_RETRY_AFTER = "Retry-After"
        const val HEADER_RATE_LIMIT_ = "X-RateLimit-Limit"
        const val HEADER_RATE_REMAINING_ = "X-RateLimit-Remaining"
        const val HEADER_RATE_LIMIT_RESET_ = "X-RateLimit-Reset"
    }
}