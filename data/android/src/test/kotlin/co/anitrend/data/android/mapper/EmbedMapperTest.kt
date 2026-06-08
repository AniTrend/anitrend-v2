package co.anitrend.data.android.mapper

import co.anitrend.arch.data.converter.SupportConverter
import co.anitrend.data.android.source.local.AbstractLocalSource
import io.mockk.mockk
import kotlin.test.Test
import kotlin.test.assertEquals

class EmbedMapperTest {
    @Test
    fun `persistEmbedded should not drop entities buffered during ongoing persist`() {
        val mapper = TestEmbedMapper()

        runSuspend {
            mapper.onEmbedded(listOf(1))
            mapper.persistEmbedded()
            mapper.persistEmbedded()
        }

        assertEquals(listOf(1, 2), mapper.persisted)
    }
}

private class TestEmbedMapper : EmbedMapper<Int, Int>() {
    val persisted = mutableListOf<Int>()
    private var shouldBufferAnotherDuringPersist = true

    override val localSource: AbstractLocalSource<Int> = FakeLocalSource()

    override val converter: SupportConverter<Int, Int> = mockk(relaxed = true)

    override suspend fun onResponseMapFrom(source: List<Int>): List<Int> = source

    override suspend fun persist(data: List<Int>) {
        persisted.addAll(data)

        if (shouldBufferAnotherDuringPersist) {
            shouldBufferAnotherDuringPersist = false
            onEmbedded(2)
        }
    }
}

private class FakeLocalSource : AbstractLocalSource<Int>() {
    override suspend fun count(): Int = 0

    override suspend fun clear() = Unit

    override suspend fun insert(attribute: Int): Long = 0L

    override suspend fun insert(attribute: List<Int>): List<Long> = emptyList()

    override suspend fun update(attribute: Int) = Unit

    override suspend fun update(attribute: List<Int>) = Unit

    override suspend fun delete(attribute: Int) = Unit

    override suspend fun delete(attribute: List<Int>) = Unit

    override suspend fun upsert(attribute: Int) = Unit

    override suspend fun upsert(attribute: List<Int>) = Unit
}

private fun runSuspend(block: suspend () -> Unit) {
    kotlinx.coroutines.runBlocking { block() }
}
