package co.anitrend.core.api.interceptor

import okhttp3.Interceptor
import okhttp3.Response

/**
 * ClientInterceptor interceptor add headers dynamically adds accept headers.
 * The context in which an [Interceptor] may be  parallel or asynchronous depending
 * on the dispatching caller, as such take care to assure thread safety
 */
class ClientInterceptor: Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val builder = request.newBuilder()

        builder.apply {
            addHeader("Content-Type", "application/json")
        }

        return chain.proceed(builder.build())
    }
}