package co.anitrend.media.component.compose.section

import co.anitrend.domain.common.entity.shared.CoverImage
import co.anitrend.domain.media.entity.MediaStudioEntry
import co.anitrend.domain.studio.entity.Studio
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class MediaStudiosSectionSupportTest {

    @Test
    fun `toMediaStudioUiModels sorts by main animation favourites then name`() {
        val studios =
            listOf(
                studio(id = 1L, name = "Wit Studio", isAnimationStudio = true, favourites = 900),
                studio(id = 2L, name = "Bones", isMain = true, isAnimationStudio = true, favourites = 500),
                studio(id = 3L, name = "A-1 Pictures", isAnimationStudio = true, favourites = 900),
                studio(id = 4L, name = "Aniplex", favourites = 9_000),
            )

        val preview = studios.toMediaStudioUiModels()

        assertEquals(listOf(2L, 3L, 1L, 4L), preview.map { it.id })
    }

    @Test
    fun `toMediaStudiosPreviewUiState features main studio and limits supporting rows`() {
        val studios =
            listOf(
                studio(id = 1L, name = "Bones", isMain = true, isAnimationStudio = true, favourites = 16_400),
                studio(id = 2L, name = "TOHO animation", isAnimationStudio = true, favourites = 12_100),
                studio(id = 3L, name = "Shueisha", favourites = 8_400),
                studio(id = 4L, name = "Dentsu", favourites = 6_200),
                studio(id = 5L, name = "Movic", favourites = 5_100),
            ).toMediaStudioUiModels()

        val preview = studios.toMediaStudiosPreviewUiState()

        assertEquals(1L, preview.featuredStudio?.id)
        assertEquals(listOf(2L, 3L, 4L), preview.supportingStudios.map { it.id })
        assertEquals(5, preview.totalCount)
    }

    @Test
    fun `toMediaStudiosPreviewUiState falls back to compact rows when no main studio exists`() {
        val studios =
            listOf(
                studio(id = 1L, name = "CloverWorks", isAnimationStudio = true, favourites = 14_000),
                studio(id = 2L, name = "Aniplex", favourites = 13_500),
                studio(id = 3L, name = "Kodansha", favourites = 7_200),
                studio(id = 4L, name = "TMS Entertainment", isAnimationStudio = true, favourites = 10_300),
                studio(id = 5L, name = "Dentsu", favourites = 4_000),
            ).toMediaStudioUiModels()

        val preview = studios.toMediaStudiosPreviewUiState()

        assertNull(preview.featuredStudio)
        assertEquals(listOf(1L, 4L, 2L, 3L), preview.supportingStudios.map { it.id })
        assertEquals(5, preview.totalCount)
    }

    @Test
    fun `toMediaStudioUiModels keeps network enrichment labels`() {
        val preview =
            listOf(
                studio(
                    id = 1L,
                    name = "Bones",
                    image = CoverImage(large = "https://cdn.example/logo.png", medium = "https://cdn.example/logo.png"),
                    networkMatch =
                        MediaStudioEntry.StudioNetworkMatch(
                            networkId = 9L,
                            name = "Bones",
                            category = "network",
                            originCountry = "jp",
                            logoPath = null,
                            isPrimary = true,
                            similarity = 1f,
                        ),
                ),
            ).toMediaStudioUiModels()

        assertEquals("Network", preview.first().networkCategory)
        assertEquals("JP", preview.first().networkOriginCountry)
        assertEquals("https://cdn.example/logo.png", preview.first().image?.large)
    }

    private fun studio(
        id: Long,
        name: String,
        isMain: Boolean = false,
        isAnimationStudio: Boolean = false,
        favourites: Int = 0,
        image: CoverImage? = null,
        networkMatch: MediaStudioEntry.StudioNetworkMatch? = null,
    ) =
        MediaStudioEntry(
            studio =
                Studio.Core(
                    favourites = favourites,
                    isFavourite = false,
                    isFavouriteBlocked = false,
                    name = name,
                    image = image,
                    isAnimationStudio = isAnimationStudio,
                    siteUrl = null,
                    id = id,
                ),
            isMain = isMain,
            networkMatch = networkMatch,
            id = id,
        )
}
