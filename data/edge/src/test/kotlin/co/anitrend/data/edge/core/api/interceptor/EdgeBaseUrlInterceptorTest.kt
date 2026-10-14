package co.anitrend.data.edge.core.api.interceptor

import okhttp3.HttpUrl.Companion.toHttpUrl
import kotlin.test.Test
import kotlin.test.assertEquals

class EdgeBaseUrlInterceptorTest {
    @Test
    fun `configured host rewrites scheme and port but preserves GraphQL path and query`() {
        val requestUrl = "https://api.anitrend.co/graphql?query=GetConfig".toHttpUrl()

        val rewritten = rewriteEdgeApiUrl(requestUrl, "http://10.0.2.2:8800/")

        assertEquals("http://10.0.2.2:8800/graphql?query=GetConfig", rewritten.toString())
    }

    @Test
    fun `missing or invalid override preserves the compiled endpoint`() {
        val requestUrl = "https://api.anitrend.co/graphql".toHttpUrl()

        assertEquals(requestUrl, rewriteEdgeApiUrl(requestUrl, null))
        assertEquals(requestUrl, rewriteEdgeApiUrl(requestUrl, "not a URL"))
    }
}
