package co.anitrend.data.core.api.converter.request

import com.google.gson.Gson
import co.anitrend.retrofit.graphql.annotation.processor.contract.AbstractGraphProcessor
import co.anitrend.retrofit.graphql.model.EmptyGraphQLVariables
import co.anitrend.retrofit.graphql.model.GraphQLRequest
import io.mockk.every
import io.mockk.mockk
import kotlin.test.Test
import kotlin.test.assertTrue
import okio.Buffer

class AniGraphRequestConverterTest {
    @Test
    fun `convert serializes graphql request payload directly`() {
        val processor =
            mockk<AbstractGraphProcessor> {
                every { getQuery(any()) } throws IllegalStateException("Processor fallback should not be used")
            }
        val converter =
            AniRequestConverter(
                methodAnnotations = emptyArray(),
                processor = processor,
                gson = Gson(),
            )

        val requestBody =
            converter.convert(
                GraphQLRequest<EmptyGraphQLVariables>(
                    query = "query Viewer { viewer { id } }",
                    operationName = "Viewer",
                    variables = EmptyGraphQLVariables,
                    extensions = emptyMap(),
                ),
            )

        val payload = requestBody.asString()

        assertTrue(payload.contains("\"query\""))
        assertTrue(payload.contains("Viewer"))
        assertTrue(payload.contains("\"variables\""))
        assertTrue(payload.contains("\"operationName\":\"Viewer\""))
    }

    private fun okhttp3.RequestBody.asString(): String {
        val buffer = Buffer()
        writeTo(buffer)
        return buffer.readUtf8()
    }
}
