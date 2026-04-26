package co.anitrend.example.testing

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

private enum class WorkerOutcome {
    SUCCESS,
    FAILURE,
}

private interface SyncInteractor {
    suspend fun sync(locale: String): Boolean
}

private class NewsWorkerRunner(
    private val locale: String,
    private val interactor: SyncInteractor,
) {
    suspend fun run(): WorkerOutcome {
        return if (interactor.sync(locale)) {
            WorkerOutcome.SUCCESS
        } else {
            WorkerOutcome.FAILURE
        }
    }
}

class NewsWorkerRunnerTest {

    private val interactor = mockk<SyncInteractor>()

    @Test
    fun `returns success when sync succeeds`() = runTest {
        val subject = NewsWorkerRunner(locale = "en", interactor = interactor)

        coEvery { interactor.sync("en") } returns true

        val result = subject.run()

        assertEquals(WorkerOutcome.SUCCESS, result)
        coVerify(exactly = 1) { interactor.sync("en") }
    }

    @Test
    fun `returns failure when sync fails`() = runTest {
        val subject = NewsWorkerRunner(locale = "en", interactor = interactor)

        coEvery { interactor.sync("en") } returns false

        val result = subject.run()

        assertEquals(WorkerOutcome.FAILURE, result)
        coVerify(exactly = 1) { interactor.sync("en") }
    }
}
