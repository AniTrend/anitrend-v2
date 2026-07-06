package co.anitrend.data.core.api.converter.request

import com.google.gson.Gson
import co.anitrend.data.core.api.model.GraphQLResponse
import co.anitrend.retrofit.graphql.annotation.processor.contract.AbstractGraphProcessor
import co.anitrend.retrofit.graphql.annotation.GraphQuery
import co.anitrend.retrofit.graphql.model.GraphQLDocumentRegistry
import co.anitrend.retrofit.graphql.model.request.QueryContainerBuilder
import io.mockk.every
import io.mockk.mockk
import kotlin.reflect.jvm.javaMethod
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import okio.Buffer
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

class AniGraphRequestConverterTest {
    private interface RegistryOnlyRemoteSource {
        @GraphQuery("RegistryOnlyQuery")
        @POST("/")
        suspend fun execute(
            @Body request: QueryContainerBuilder,
        ): Response<GraphQLResponse<Unit>>
    }

    @Test
    fun `convert omits operationName from graphql payload`() {
        val processor =
            mockk<AbstractGraphProcessor> {
                every { getQuery(any()) } returns "mutation SaveReview { SaveReview { id } }"
            }
        val converter =
            AniRequestConverter(
                methodAnnotations = emptyArray(),
                processor = processor,
                gson = Gson(),
            )

        val requestBody =
            converter.convert(
                QueryContainerBuilder().apply {
                    putVariables(mapOf("id" to 42, "body" to "demo"))
                },
            )

        val payload = requestBody.asString()

        assertTrue(payload.contains("\"query\""))
        assertTrue(payload.contains("\"variables\""))
        assertFalse(payload.contains("operationName"))
    }

    @Test
    fun `convert resolves query from registry before processor`() {
        val processor =
            mockk<AbstractGraphProcessor> {
                every { getQuery(any()) } throws IllegalStateException("Processor fallback should not be used")
            }
        val registry =
            mockk<GraphQLDocumentRegistry> {
                every { document("RegistryOnlyQuery") } returns
                    "query RegistryOnlyQuery { viewer { id } }"
            }
        val methodAnnotations =
            checkNotNull(RegistryOnlyRemoteSource::execute.javaMethod).annotations
        val converter =
            AniRequestConverter(
                methodAnnotations = methodAnnotations,
                processor = processor,
                gson = Gson(),
                registry = registry,
            )

        val requestBody =
            converter.convert(
                QueryContainerBuilder().apply {
                    putVariables(mapOf("id" to 42))
                },
            )

        val payload = requestBody.asString()

        assertTrue(payload.contains("RegistryOnlyQuery"))
        assertFalse(payload.contains("ProcessorFallback"))
    }

    private fun okhttp3.RequestBody.asString(): String {
        val buffer = Buffer()
        writeTo(buffer)
        return buffer.readUtf8()
    }
}
