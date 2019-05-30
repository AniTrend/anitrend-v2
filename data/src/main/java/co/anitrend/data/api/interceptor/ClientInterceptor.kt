package co.anitrend.data.api.interceptor

import okhttp3.Interceptor
import okhttp3.Response

/**
 * ClientInterceptor interceptor add headers dynamically adds accept headers.
 * The context in which an [Interceptor] may be  parallel or asynchronous depending
 * on the dispatching caller, as such take care to assure thread safety
 */
class ClientInterceptor: Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        val request = original.newBuilder()
            .header("Content-Type", "application/json")
            .header("Accept", "application/json")
            .method(original.method(), original.body())
            .build()

        return chain.proceed(request)
    }
}