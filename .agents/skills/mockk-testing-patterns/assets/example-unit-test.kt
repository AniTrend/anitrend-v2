package co.anitrend.example.testing

import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

private interface EpisodeRepository {
    suspend fun featuredIds(): List<Long>
}

private interface EpisodeStreamProvider {
    fun observeEpisodes(ids: List<Long>): Flow<List<String>>
}

private class FeaturedEpisodesInteractor(
    private val repository: EpisodeRepository,
    private val streamProvider: EpisodeStreamProvider,
) {
    suspend fun invoke(): Flow<List<String>> {
        val ids = repository.featuredIds()
        return streamProvider.observeEpisodes(ids)
    }
}

class FeaturedEpisodesInteractorTest {

    private val repository = mockk<EpisodeRepository>()
    private val streamProvider = mockk<EpisodeStreamProvider>()

    @Test
    fun `emits featured episodes from repository ids`() = runTest {
        val expectedIds = listOf(11L, 12L)
        val expectedTitles = listOf("Episode 11", "Episode 12")
        val subject = FeaturedEpisodesInteractor(repository, streamProvider)

        coEvery { repository.featuredIds() } returns expectedIds
        every { streamProvider.observeEpisodes(expectedIds) } returns flowOf(expectedTitles)

        subject.invoke().test {
            assertEquals(expectedTitles, awaitItem())
            awaitComplete()
        }

        coVerify(exactly = 1) { repository.featuredIds() }
        verify(exactly = 1) { streamProvider.observeEpisodes(expectedIds) }
        confirmVerified(repository, streamProvider)
    }
}
