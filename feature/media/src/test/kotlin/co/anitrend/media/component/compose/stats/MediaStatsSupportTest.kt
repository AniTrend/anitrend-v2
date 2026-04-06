package co.anitrend.media.component.compose.stats

import co.anitrend.domain.media.entity.MediaStats
import co.anitrend.domain.medialist.enums.MediaListStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class MediaStatsSupportTest {

    @Test
    fun `buildMediaStatsSummarySnapshot keeps positive summary values only`() {
        val snapshot =
            buildMediaStatsSummarySnapshot(
                averageScore = 85,
                favourites = 76_000,
                popularity = 971_000,
                trendRank = 23,
            )

        assertEquals(85, snapshot.averageScore)
        assertEquals(76_000, snapshot.favourites)
        assertEquals(971_000, snapshot.popularity)
        assertEquals(23, snapshot.trendRank)
        assertTrue(snapshot.hasAnyValue())
    }

    @Test
    fun `buildMediaStatsSummarySnapshot drops zero and negative values`() {
        val snapshot =
            buildMediaStatsSummarySnapshot(
                averageScore = 0,
                favourites = -1,
                popularity = null,
                trendRank = 0,
            )

        assertEquals(null, snapshot.averageScore)
        assertEquals(null, snapshot.favourites)
        assertEquals(null, snapshot.popularity)
        assertEquals(null, snapshot.trendRank)
        assertFalse(snapshot.hasAnyValue())
    }

    @Test
    fun `scoreChartEntries sort by score and merge duplicate buckets`() {
        val entries =
            MediaStats(
                scoreDistribution =
                    listOf(
                        MediaStats.ScoreDistribution(amount = 14, score = 80),
                        MediaStats.ScoreDistribution(amount = 3, score = 10),
                        MediaStats.ScoreDistribution(amount = 0, score = 70),
                        MediaStats.ScoreDistribution(amount = 7, score = 80),
                        MediaStats.ScoreDistribution(amount = 6, score = 40),
                    ),
                statusDistribution = emptyList(),
            ).scoreChartEntries()

        assertEquals(
            listOf(
                ScoreDistributionChartEntry(score = 10, amount = 3),
                ScoreDistributionChartEntry(score = 40, amount = 6),
                ScoreDistributionChartEntry(score = 80, amount = 21),
            ),
            entries,
        )
    }

    @Test
    fun `scoreChartAxisValues return a compact two point scale`() {
        val axisValues =
            listOf(
                ScoreDistributionChartEntry(score = 10, amount = 3),
                ScoreDistributionChartEntry(score = 40, amount = 6),
                ScoreDistributionChartEntry(score = 80, amount = 104),
            ).scoreChartAxisValues(labelCount = 2)

        assertEquals(listOf(120, 0), axisValues)
    }

    @Test
    fun `scoreChartAxisValues return a full three point scale`() {
        val axisValues =
            listOf(
                ScoreDistributionChartEntry(score = 10, amount = 3),
                ScoreDistributionChartEntry(score = 40, amount = 6),
                ScoreDistributionChartEntry(score = 80, amount = 104),
            ).scoreChartAxisValues(labelCount = 3)

        assertEquals(listOf(120, 60, 0), axisValues)
    }

    @Test
    fun `statusChartEntries sort by amount and place unknown last on ties`() {
        val entries =
            MediaStats(
                scoreDistribution = emptyList(),
                statusDistribution =
                    listOf(
                        MediaStats.StatusDistribution(amount = 59, status = MediaListStatus.CURRENT),
                        MediaStats.StatusDistribution(amount = 816, status = MediaListStatus.COMPLETED),
                        MediaStats.StatusDistribution(amount = 59, status = null),
                        MediaStats.StatusDistribution(amount = 65, status = MediaListStatus.PLANNING),
                        MediaStats.StatusDistribution(amount = 15, status = MediaListStatus.DROPPED),
                        MediaStats.StatusDistribution(amount = 0, status = MediaListStatus.PAUSED),
                    ),
            ).statusChartEntries()

        assertEquals(
            listOf(
                MediaListStatus.COMPLETED,
                MediaListStatus.PLANNING,
                MediaListStatus.CURRENT,
                null,
                MediaListStatus.DROPPED,
            ),
            entries.map(StatusDistributionChartEntry::status),
        )
        assertEquals(listOf(816, 65, 59, 59, 15), entries.map(StatusDistributionChartEntry::amount))
        assertEquals(1.0, entries.sumOf { it.fraction.toDouble() }, 0.0001)
    }
}
