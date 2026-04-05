package co.anitrend.media.component.compose.section

import co.anitrend.android.core.helpers.date.AniTrendDateHelper
import co.anitrend.domain.common.entity.shared.FuzzyDate
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.media.entity.MediaRecommendationEntry
import co.anitrend.domain.media.entity.MediaRelationEntry
import co.anitrend.domain.media.entity.attribute.rank.MediaRank
import co.anitrend.domain.media.entity.attribute.score.MediaScore
import co.anitrend.domain.media.enums.MediaFormat
import co.anitrend.domain.media.enums.MediaRelation
import co.anitrend.domain.media.enums.MediaRankType
import co.anitrend.domain.media.enums.MediaSeason
import co.anitrend.domain.media.enums.MediaStatus
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
    fun `groupRelationsByBucket keeps bucket order and sends unknown relations to shared universe`() {
        val groups =
            groupRelationsByBucket(
                listOf(
                    relationEntry(id = 1L, relation = MediaRelation.SIDE_STORY, mediaId = 101L),
                    relationEntry(id = 2L, relation = MediaRelation.ADAPTATION, mediaId = 102L),
                    relationEntry(id = 3L, relation = MediaRelation.SEQUEL, mediaId = 103L),
                    relationEntry(id = 4L, relation = null, mediaId = 104L),
                    relationEntry(id = 5L, relation = MediaRelation.PREQUEL, mediaId = 105L),
                    relationEntry(id = 6L, relation = MediaRelation.CHARACTER, mediaId = 106L),
                    relationEntry(id = 7L, relation = MediaRelation.SOURCE, mediaId = 107L),
                    relationEntry(id = 8L, relation = MediaRelation.COMPILATION, mediaId = 108L),
                ),
            )

        assertEquals(
            listOf(
                MediaRelationBucket.STORY_CONTINUITY,
                MediaRelationBucket.SOURCE_AND_ADAPTATION,
                MediaRelationBucket.SIDE_PATHS,
                MediaRelationBucket.SHARED_UNIVERSE,
            ),
            groups.map { it.bucket },
        )
        assertEquals(
            listOf(
                listOf(3L, 5L),
                listOf(7L, 2L),
                listOf(1L, 8L),
                listOf(6L, 4L),
            ),
            groups.map { group -> group.entries.map { it.id } },
        )
    }

    @Test
    fun `buildReleaseMetadataEntries keeps premiere context and full dates`() {
        val media =
            extendedMedia(
                season = MediaSeason.SPRING,
                startDate = FuzzyDate(year = 2024, month = 4, day = 7),
                endDate = FuzzyDate(year = 2024, month = 6, day = 30),
            )

        val entries =
            buildReleaseMetadataEntries(
                media = media,
                dateHelper = AniTrendDateHelper(),
                premieredLabel = "Premiered",
                startedLabel = "Started",
                endedLabel = "Ended",
                seasonLabel = "Spring",
            )

        assertEquals(
            listOf(
                MetadataEntry("Premiered", "Spring 2024"),
                MetadataEntry("Started", "Apr 07, 2024"),
                MetadataEntry("Ended", "Jun 30, 2024"),
            ),
            entries,
        )
    }

    @Test
    fun `buildReleaseMetadataEntries omits duplicate started value for season only dates`() {
        val media =
            extendedMedia(
                season = MediaSeason.FALL,
                startDate = FuzzyDate(year = 2025, month = 0, day = 0),
            )

        val entries =
            buildReleaseMetadataEntries(
                media = media,
                dateHelper = AniTrendDateHelper(),
                premieredLabel = "Premiered",
                startedLabel = "Started",
                endedLabel = "Ended",
                seasonLabel = "Fall",
            )

        assertEquals(
            listOf(
                MetadataEntry("Premiered", "Fall 2025"),
            ),
            entries,
        )
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
        relation: MediaRelation?,
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

    private fun extendedMedia(
        season: MediaSeason? = null,
        startDate: FuzzyDate = FuzzyDate.empty(),
        endDate: FuzzyDate = FuzzyDate.empty(),
    ) = Media.Extended.empty().copy(
        id = 1L,
        status = MediaStatus.RELEASING,
        score = MediaScore.empty(),
        season = season,
        startDate = startDate,
        endDate = endDate,
    )
}
