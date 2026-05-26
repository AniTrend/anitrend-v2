package co.anitrend.data.core.api.converter.request

import com.google.gson.Gson
import io.github.wax911.library.annotation.processor.contract.AbstractGraphProcessor
import io.github.wax911.library.model.request.QueryContainerBuilder
import io.mockk.every
import io.mockk.mockk
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import okio.Buffer

class AniGraphRequestConverterTest {

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

    private fun okhttp3.RequestBody.asString(): String {
        val buffer = Buffer()
        writeTo(buffer)
        return buffer.readUtf8()
    }
}
