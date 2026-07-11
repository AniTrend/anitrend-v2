package co.anitrend.data.core.api.converter.request

import com.google.gson.Gson
import com.google.gson.JsonObject
import co.anitrend.retrofit.graphql.model.EmptyGraphQLVariables
import co.anitrend.retrofit.graphql.model.GraphQLRequest
import co.anitrend.retrofit.graphql.converter.GraphConverter
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue
import okio.Buffer

class AniGraphRequestConverterTest {
    @Test
    fun `convert serializes graphql request payload directly`() {
        val converter = AniRequestConverter(gson = Gson())
        val request =
            GraphQLRequest<EmptyGraphQLVariables>(
                query = "query Viewer {\n  viewer { id }\n}",
                operationName = "Viewer",
                variables = EmptyGraphQLVariables,
                extensions = emptyMap(),
            )

        val requestBody = converter.convert(request)
        val payload = requestBody.asString()
        val serialized = Gson().fromJson(payload, JsonObject::class.java)

        assertEquals("application/json", "${requestBody.contentType()?.type}/${requestBody.contentType()?.subtype}")
        assertEquals(request.query, serialized.get("query").asString)
        assertEquals(request.operationName, serialized.get("operationName").asString)
        assertTrue(payload.contains("\"variables\""))
    }

    @Test
    fun `serialize minifies query when shrink mode is enabled`() {
        val converter = AniRequestConverter(gson = Gson())
        val request =
            GraphQLRequest<EmptyGraphQLVariables>(
                query = "query Viewer {\n    viewer {\n        id\n    }\n}",
                operationName = "Viewer",
                variables = EmptyGraphQLVariables,
                extensions = emptyMap(),
            )

        val payload = converter.serialize(request, shrinkQuery = true)
        val serialized = Gson().fromJson(payload, JsonObject::class.java)

        assertTrue(serialized.get("query").asString.contains("query Viewer {"))
        assertTrue(serialized.get("query").asString.contains("viewer {"))
        assertTrue(serialized.get("query").asString.contains("id"))
        assertTrue(!serialized.get("query").asString.contains("\n"))
    }

    @Test
    fun `convert fails fast when query string is blank`() {
        val converter = AniRequestConverter(gson = Gson())
        val request =
            GraphQLRequest<EmptyGraphQLVariables>(
                query = "   ",
                operationName = "Viewer",
                variables = EmptyGraphQLVariables,
                extensions = emptyMap(),
            )

        val error =
            assertFailsWith<IllegalStateException> {
                converter.convert(request)
            }

        assertEquals("GraphQL request query is blank for operation: Viewer", error.message)
    }

    private fun okhttp3.RequestBody.asString(): String {
        val buffer = Buffer()
        writeTo(buffer)
        return buffer.readUtf8()
    }
}
