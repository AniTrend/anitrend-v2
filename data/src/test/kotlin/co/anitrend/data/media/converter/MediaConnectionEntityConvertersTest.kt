/*
 * Copyright (C) 2026 AniTrend
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package co.anitrend.data.media.converter

import co.anitrend.data.media.entity.MediaStatsEntity
import co.anitrend.data.media.entity.stats.MediaScoreDistributionEntity
import co.anitrend.data.media.entity.stats.MediaStatusDistributionEntity
import co.anitrend.data.media.entity.view.MediaStatsEntityView
import co.anitrend.data.media.entity.connection.MediaRelationConnectionEntity
import co.anitrend.data.recommendation.converter.MediaRecommendationConnectionEntityConverter
import co.anitrend.data.recommendation.entity.connection.MediaRecommendationConnectionEntity
import co.anitrend.domain.airing.entity.AiringSchedule
import co.anitrend.domain.common.entity.shared.FuzzyDate
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.media.entity.attribute.image.MediaImage
import co.anitrend.domain.media.entity.attribute.score.MediaScore
import co.anitrend.domain.media.entity.attribute.title.MediaTitle
import co.anitrend.domain.media.enums.MediaFormat
import co.anitrend.domain.media.enums.MediaRelation
import co.anitrend.domain.media.enums.MediaStatus
import co.anitrend.domain.medialist.entity.MediaList
import co.anitrend.domain.medialist.entity.contract.MediaListPrivacy
import co.anitrend.domain.medialist.enums.MediaListStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class MediaConnectionEntityConvertersTest {
    @Test
    fun `given cached recommendation target when converting then preview media state is preserved`() {
        val media =
            Media.Core.empty().copy(
                id = 42L,
                title =
                    MediaTitle(
                        romaji = "Tensei Example",
                        english = "Example Reborn",
                        native = "転生サンプル",
                        userPreferred = "Example Reborn",
                    ),
                image =
                    MediaImage(
                        color = "#112233",
                        extraLarge = "extra",
                        large = "large",
                        medium = "medium",
                        banner = null,
                    ),
                category =
                    Media.Category.Anime(
                        episodes = 24,
                        duration = 24,
                        broadcast = null,
                        premiered = null,
                        schedule =
                            AiringSchedule(
                                airingAt = 1_900_000_000L,
                                episode = 13,
                                mediaId = 42L,
                                timeUntilAiring = 600L,
                                id = 77L,
                            ),
                    ),
                format = MediaFormat.TV,
                status = MediaStatus.RELEASING,
                score = MediaScore(mean = 88, average = 90, personal = 9f, popularity = null, trending = null),
                startDate = FuzzyDate(year = 2024, month = 1, day = 7),
                isFavourite = true,
                mediaList =
                    MediaList.Core.empty().copy(
                        mediaId = 42L,
                        score = 9f,
                        status = MediaListStatus.CURRENT,
                        privacy = MediaListPrivacy.empty().copy(notes = "Queued for weekly watch"),
                    ),
            )

        val recommendationEntity =
            MediaRecommendationConnectionEntity(
                mediaId = 10L,
                entryId = 91L,
                rating = 42,
                userName = "Yuki",
                userRating = "RATE_UP",
                sortIndex = 0,
                target = media.toConnectionPreviewEntity(),
            )

        val relationEntity =
            MediaRelationConnectionEntity(
                mediaId = 10L,
                entryId = 92L,
                relation = "SEQUEL",
                sortIndex = 1,
                target = media.toConnectionPreviewEntity(),
            )

        val recommendation = MediaRecommendationConnectionEntityConverter().convertFrom(recommendationEntity)
        val relation = MediaRelationConnectionEntityConverter().convertFrom(relationEntity)

        assertEquals(91L, recommendation.id)
        assertEquals(42L, recommendation.media.id)
        assertEquals("Example Reborn", recommendation.media.title.userPreferred)
        assertEquals(MediaListStatus.CURRENT, recommendation.media.mediaList?.status)
        assertEquals("Queued for weekly watch", recommendation.media.mediaList?.privacy?.notes)
        assertEquals(true, recommendation.media.isFavourite)
        assertEquals(24, (recommendation.media.category as Media.Category.Anime).episodes)
        assertEquals(MediaRelation.SEQUEL, relation.relation)
        assertEquals(92L, relation.id)
    }

    @Test
    fun `given cached stats with unknown status labels when converting then invalid labels are ignored`() {
        val entity =
            MediaStatsEntityView(
                stats = MediaStatsEntity(id = 42L),
                scoreDistribution =
                    listOf(
                        MediaScoreDistributionEntity(amount = 100, score = 90, mediaId = 42L),
                        MediaScoreDistributionEntity(amount = 20, score = 70, mediaId = 42L),
                    ),
                statusDistribution =
                    listOf(
                        MediaStatusDistributionEntity(amount = 60, status = "CURRENT", mediaId = 42L),
                        MediaStatusDistributionEntity(amount = 15, status = null, mediaId = 42L),
                        MediaStatusDistributionEntity(amount = 5, status = "NOT_A_REAL_STATUS", mediaId = 42L),
                    ),
            )

        val result = MediaStatsEntityConverter().convertFrom(entity)

        assertEquals(2, result.scoreDistribution.size)
        assertEquals(90, result.scoreDistribution.first().score)
        assertEquals(MediaListStatus.CURRENT, result.statusDistribution.first().status)
        assertNull(result.statusDistribution[1].status)
        assertNull(result.statusDistribution[2].status)
    }
}
