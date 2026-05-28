package co.anitrend.data.studio.mapper

import co.anitrend.data.common.model.date.FuzzyDateModel
import co.anitrend.data.media.model.MediaModel
import co.anitrend.data.media.model.connection.MediaConnection
import co.anitrend.data.media.model.edge.MediaEdge
import co.anitrend.data.studio.datasource.local.StudioLocalSource
import co.anitrend.data.studio.datasource.local.connection.MediaStudioConnectionLocalSource
import co.anitrend.data.studio.entity.StudioEntity
import co.anitrend.data.studio.model.remote.StudioDetailContainer
import co.anitrend.domain.media.enums.MediaFormat
import co.anitrend.domain.media.enums.MediaType
import io.mockk.coVerifySequence
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class StudioDetailMapperTest {
    private val studioLocalSource = mockk<StudioLocalSource>(relaxed = true)
    private val connectionLocalSource = mockk<MediaStudioConnectionLocalSource>(relaxed = true)
    private val mapper = StudioDetailMapper(studioLocalSource = studioLocalSource, connectionLocalSource = connectionLocalSource)

    @Test
    fun `persist upserts studio and refreshes studio media connections`() {
        val persistence =
            StudioDetailPersistenceData(
                studio =
                    StudioEntity(
                        favourites = 7,
                        isAnimationStudio = true,
                        isFavourite = false,
                        isFavouriteBlocked = false,
                        name = "Wit Studio",
                        siteUrl = "https://anilist.co/studio/99",
                        id = 99L,
                    ),
                mediaConnections = emptyList(),
            )

        runBlocking { mapper.onResponseDatabaseInsert(persistence) }

        coVerifySequence {
            studioLocalSource.upsert(persistence.studio)
            connectionLocalSource.clearByStudioId(99L)
            connectionLocalSource.upsert(emptyList())
        }
    }

    @Test
    fun `onResponseMapFrom maps studio media fields used by detail source`() {
        val source =
            StudioDetailContainer(
                studio =
                    StudioDetailContainer.Studio(
                        favourites = 15,
                        isAnimationStudio = true,
                        isFavourite = false,
                        isFavouriteBlocked = null,
                        name = "Orange",
                        siteUrl = "https://anilist.co/studio/42",
                        id = 42L,
                        media =
                            MediaConnection.Studio(
                                edges =
                                    listOf(
                                        MediaEdge.Studio(
                                            isMainStudio = true,
                                            id = 6L,
                                            node =
                                                MediaModel.Core(
                                                    isReviewBlocked = false,
                                                    type = MediaType.ANIME,
                                                    favourites = 10,
                                                    siteUrl = "https://anilist.co/anime/6",
                                                    id = 6L,
                                                    title = MediaModel.Title(userPreferred = "Beastars"),
                                                    coverImage = MediaModel.CoverImage(large = "large.jpg", medium = "medium.jpg"),
                                                    format = MediaFormat.TV,
                                                    startDate = FuzzyDateModel(year = 2019),
                                                    averageScore = 82,
                                                ),
                                        ),
                                    ),
                                pageInfo = null,
                            ),
                    ),
            )

        val result = runBlocking { mapper.onResponseMapFrom(source) }
        val mapped = result.mediaConnections.single()

        assertEquals("Beastars", mapped.mediaTitle)
        assertEquals("large.jpg", mapped.mediaCoverImageLarge)
        assertEquals("medium.jpg", mapped.mediaCoverImageMedium)
        assertEquals(MediaFormat.TV.name, mapped.mediaFormat)
        assertEquals(2019, mapped.mediaStartYear)
        assertEquals(82, mapped.mediaAverageScore)
        assertEquals(true, mapped.isMain)
        assertEquals(0, mapped.sortIndex)
    }
}
