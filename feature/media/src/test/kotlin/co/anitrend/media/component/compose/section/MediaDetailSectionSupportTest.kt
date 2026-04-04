package co.anitrend.media.component.compose.section

import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.media.entity.MediaRecommendationEntry
import co.anitrend.domain.media.entity.MediaRelationEntry
import co.anitrend.domain.media.entity.attribute.rank.MediaRank
import co.anitrend.domain.media.enums.MediaFormat
import co.anitrend.domain.media.enums.MediaRelation
import co.anitrend.domain.media.enums.MediaRankType
import co.anitrend.domain.media.enums.MediaSeason
import co.anitrend.domain.tag.entity.Tag
import co.anitrend.domain.recommendation.enums.RecommendationRating
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
    fun `selectRelationPreview prioritizes sequel and prequel before broader relation types`() {
        val relations =
            listOf(
                relationEntry(id = 1L, relation = MediaRelation.CHARACTER, mediaId = 101L),
                relationEntry(id = 2L, relation = MediaRelation.PREQUEL, mediaId = 102L),
                relationEntry(id = 3L, relation = MediaRelation.SEQUEL, mediaId = 103L),
                relationEntry(id = 4L, relation = MediaRelation.SIDE_STORY, mediaId = 104L),
                relationEntry(id = 5L, relation = MediaRelation.ADAPTATION, mediaId = 105L),
            )

        val preview = selectRelationPreview(relations, maxCount = 4)

        assertEquals(listOf(3L, 2L, 5L, 4L), preview.map { it.id })
    }

    @Test
    fun `selectRecommendationPreview keeps first distinct recommended titles only`() {
        val recommendations =
            listOf(
                recommendationEntry(id = 1L, mediaId = 301L),
                recommendationEntry(id = 2L, mediaId = 302L),
                recommendationEntry(id = 3L, mediaId = 301L),
                recommendationEntry(id = 4L, mediaId = 303L),
            )

        val preview = selectRecommendationPreview(recommendations, maxCount = 3)

        assertEquals(listOf(1L, 2L, 4L), preview.map { it.id })
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

    private fun relationEntry(
        id: Long,
        relation: MediaRelation,
        mediaId: Long,
    ) =
        MediaRelationEntry(
            relation = relation,
            media = mediaItem(mediaId),
            id = id,
        )

    private fun recommendationEntry(
        id: Long,
        mediaId: Long,
    ) =
        MediaRecommendationEntry(
            media = mediaItem(mediaId),
            rating = id.toInt(),
            userName = "User $id",
            userRating = RecommendationRating.NO_RATING,
            id = id,
        )

    private fun mediaItem(id: Long) = Media.Core.empty().copy(id = id)
}
