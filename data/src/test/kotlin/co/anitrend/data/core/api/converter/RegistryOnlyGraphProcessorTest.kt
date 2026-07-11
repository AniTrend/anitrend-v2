package co.anitrend.data.core.api.converter

import co.anitrend.retrofit.graphql.annotation.GraphQuery
import io.mockk.every
import io.mockk.mockk
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertFailsWith

class RegistryOnlyGraphProcessorTest {
    private val subject = RegistryOnlyGraphProcessor()

    @Test
    fun `processor exposes runtime safe logger and fragment patcher`() {
        assertNotNull(subject.logger)
        assertNotNull(subject.fragmentPatcher)
    }

    @Test
    fun `processor returns null when no legacy asset annotation is requested`() {
        assertNull(subject.getQuery(emptyArray()))
    }

    @Test
    fun `processor rejects legacy asset fallback requests`() {
        val graphQuery =
            mockk<GraphQuery> {
                every { value } returns "legacy-operation.graphql"
            }

        assertFailsWith<IllegalStateException> {
            subject.getQuery(arrayOf(graphQuery))
        }
    }
}
