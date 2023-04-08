package co.anitrend.data.edge.core.api.interceptor

import io.github.wax911.library.converter.GraphConverter
import okhttp3.Interceptor
import okhttp3.Response

internal class EdgeClientInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        val contentLength = original.body?.contentLength() ?: 0
        val requestBuilder = original.newBuilder()

        requestBuilder.header(CONTENT_TYPE, GraphConverter.MimeType)
            .header(ACCEPT, ACCEPT_TYPE)
            .header(CONTENT_LENGTH, contentLength.toString())
            .method(original.method, original.body)

        return chain.proceed(requestBuilder.build())
    }

    companion object {
        private const val ACCEPT = "Accept"
        private const val CONTENT_LENGTH = "Content-Length"
        private const val CONTENT_TYPE = "Content-Type"

        private const val ACCEPT_TYPE = "application/json"
    }
}
