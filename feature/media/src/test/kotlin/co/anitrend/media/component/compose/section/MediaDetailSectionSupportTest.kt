package co.anitrend.media.component.compose.section

import co.anitrend.domain.media.entity.attribute.rank.MediaRank
import co.anitrend.domain.media.enums.MediaFormat
import co.anitrend.domain.media.enums.MediaRankType
import co.anitrend.domain.media.enums.MediaSeason
import co.anitrend.domain.tag.entity.Tag
import kotlin.test.Test
import kotlin.test.assertEquals

class MediaDetailSectionSupportTest {

    @Test
    fun `selectRankingPreview prioritizes all time anchors before remaining ranks`() {
        val ranks =
            listOf(
                MediaRank(
                    id = 1L,
                    allTime = false,
                    context = "Highest Rated",
                    format = MediaFormat.TV,
                    rank = 3,
                    season = MediaSeason.SPRING,
                    type = MediaRankType.RATED,
                    year = 2024,
                ),
                MediaRank(
                    id = 2L,
                    allTime = true,
                    context = "Highest Rated",
                    format = MediaFormat.TV,
                    rank = 10,
                    season = null,
                    type = MediaRankType.RATED,
                    year = null,
                ),
                MediaRank(
                    id = 3L,
                    allTime = false,
                    context = "Most Popular",
                    format = MediaFormat.TV,
                    rank = 2,
                    season = MediaSeason.WINTER,
                    type = MediaRankType.POPULAR,
                    year = 2025,
                ),
                MediaRank(
                    id = 4L,
                    allTime = true,
                    context = "Most Popular",
                    format = MediaFormat.TV,
                    rank = 15,
                    season = null,
                    type = MediaRankType.POPULAR,
                    year = null,
                ),
            )

        val preview = selectRankingPreview(ranks)

        assertEquals(listOf(2L, 4L, 3L), preview.map { it.id })
    }

    @Test
    fun `partitionMediaTags groups spoiler levels and counts correctly`() {
        val tags =
            listOf(
                Tag.Extended(
                    id = 1L,
                    rank = 88,
                    isMediaSpoiler = false,
                    background = "#8AA4FF",
                    name = "Found Family",
                    description = null,
                    category = "Themes",
                    isGeneralSpoiler = false,
                    isAdult = false,
                ),
                Tag.Extended(
                    id = 2L,
                    rank = 64,
                    isMediaSpoiler = false,
                    background = null,
                    name = "Late-game betrayal",
                    description = null,
                    category = "Plot",
                    isGeneralSpoiler = true,
                    isAdult = false,
                ),
                Tag.Extended(
                    id = 3L,
                    rank = 52,
                    isMediaSpoiler = true,
                    background = null,
                    name = "Character death",
                    description = null,
                    category = "Plot",
                    isGeneralSpoiler = false,
                    isAdult = false,
                ),
            )

        val partition = partitionMediaTags(tags)

        assertEquals(listOf(1L), partition.safeTags.map { it.id })
        assertEquals(listOf(2L, 3L), partition.spoilerTags.map { it.id })
        assertEquals(1, partition.generalSpoilerCount)
        assertEquals(1, partition.mediaSpoilerCount)
    }
}
