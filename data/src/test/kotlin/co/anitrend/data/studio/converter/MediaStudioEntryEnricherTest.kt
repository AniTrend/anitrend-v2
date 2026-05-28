package co.anitrend.data.studio.converter

import co.anitrend.data.edge.network.entity.EdgeNetworkEntity
import co.anitrend.domain.media.entity.MediaStudioEntry
import co.anitrend.domain.studio.entity.Studio
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class MediaStudioEntryEnricherTest {

    @Test
    fun `enrich adds matched network image to studio`() {
        val result =
            MediaStudioEntryEnricher().enrich(
                entries =
                    listOf(
                        MediaStudioEntry(
                            studio =
                                Studio.Core(
                                    favourites = 0,
                                    isFavourite = false,
                                    isFavouriteBlocked = false,
                                    name = "Bones",
                                    image = null,
                                    isAnimationStudio = true,
                                    siteUrl = null,
                                    id = 1L,
                                ),
                            mediaTitle = "",
                            mediaCoverImage = null,
                            mediaFormat = null,
                            mediaStartYear = null,
                            mediaAverageScore = null,
                            isMain = true,
                            id = 11L,
                        ),
                    ),
                networks =
                    listOf(
                        EdgeNetworkEntity(
                            mediaId = "media-1",
                            networkId = 99L,
                            name = "Bones",
                            category = "network",
                            isPrimary = true,
                            logoPath = "/logo.png",
                            originCountry = "JP",
                        ),
                    ),
            )

        val match = assertNotNull(result.first().networkMatch)
        assertEquals("https://image.tmdb.org/t/p/original/logo.png", match.logoPath)
        assertEquals("https://image.tmdb.org/t/p/original/logo.png", result.first().studio.image?.large)
        assertEquals("https://image.tmdb.org/t/p/original/logo.png", result.first().studio.image?.medium)
    }
}
