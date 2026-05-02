package co.anitrend.example.testing

import app.cash.turbine.test
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

private interface ScoreRepository {
    fun observeScores(userId: Long): Flow<List<Int>>
}

private class ScoreStreamUseCase(
    private val repository: ScoreRepository,
) {
    fun invoke(userId: Long): Flow<List<Int>> = repository.observeScores(userId)
}

class ScoreStreamUseCaseTest {

    private val repository = mockk<ScoreRepository>()

    @Test
    fun `emits the latest scores for the requested user`() = runTest {
        val userId = 42L
        val expected = listOf(80, 90, 95)
        val subject = ScoreStreamUseCase(repository)

        every { repository.observeScores(userId) } returns flowOf(expected)

        subject.invoke(userId).test {
            assertEquals(expected, awaitItem())
            awaitComplete()
        }

        verify(exactly = 1) { repository.observeScores(userId) }
    }
}
