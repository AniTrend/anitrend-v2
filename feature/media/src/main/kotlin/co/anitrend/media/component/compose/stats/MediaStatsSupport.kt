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
package co.anitrend.media.component.compose.stats

import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.media.entity.MediaStats
import co.anitrend.domain.medialist.enums.MediaListStatus
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow
import kotlin.math.roundToInt

internal data class MediaStatsSummarySnapshot(
    val averageScore: Int? = null,
    val favourites: Int? = null,
    val popularity: Int? = null,
    val trendRank: Int? = null,
) {
    fun hasAnyValue(): Boolean =
        averageScore != null || favourites != null || popularity != null || trendRank != null
}

internal data class ScoreDistributionChartEntry(
    val score: Int,
    val amount: Int,
)

internal data class StatusDistributionChartEntry(
    val status: MediaListStatus?,
    val amount: Int,
    val fraction: Float,
)

internal fun buildMediaStatsSummarySnapshot(
    averageScore: Int?,
    favourites: Int?,
    popularity: Int?,
    trendRank: Int?,
): MediaStatsSummarySnapshot =
    MediaStatsSummarySnapshot(
        averageScore = averageScore?.takeIf { it > 0 },
        favourites = favourites?.takeIf { it > 0 },
        popularity = popularity?.takeIf { it > 0 },
        trendRank = trendRank?.takeIf { it > 0 },
    )

internal fun Media.Extended.toMediaStatsSummarySnapshot(): MediaStatsSummarySnapshot =
    buildMediaStatsSummarySnapshot(
        averageScore = score.mean,
        favourites = favourites,
        popularity = score.popularity,
        trendRank = score.trending,
    )

internal fun MediaStats?.scoreChartEntries(): List<ScoreDistributionChartEntry> =
    this
        ?.scoreDistribution
        .orEmpty()
        .asSequence()
        .filter { it.amount > 0 }
        .groupBy(MediaStats.ScoreDistribution::score)
        .map { (score, entries) ->
            ScoreDistributionChartEntry(
                score = score,
                amount = entries.sumOf(MediaStats.ScoreDistribution::amount),
            )
        }.sortedBy(ScoreDistributionChartEntry::score)

internal fun List<ScoreDistributionChartEntry>.scoreChartAxisValues(labelCount: Int): List<Int> {
    if (isEmpty() || labelCount < 2) {
        return emptyList()
    }

    val maxAmount = maxOf(ScoreDistributionChartEntry::amount)
    if (maxAmount <= 0) {
        return emptyList()
    }

    val segmentCount = labelCount - 1
    val step = niceAxisStep(ceil(maxAmount / segmentCount.toDouble()).toInt())

    return (segmentCount downTo 0).map { index -> step * index }
}

internal fun MediaStats?.statusChartEntries(): List<StatusDistributionChartEntry> {
    val aggregatedEntries =
        this
            ?.statusDistribution
            .orEmpty()
            .asSequence()
            .filter { it.amount > 0 }
            .groupBy(MediaStats.StatusDistribution::status)
            .map { (status, entries) ->
                status to entries.sumOf(MediaStats.StatusDistribution::amount)
            }.sortedWith(
                compareByDescending<Pair<MediaListStatus?, Int>> { it.second }
                    .thenBy { it.first?.ordinal ?: Int.MAX_VALUE },
            )

    val totalAmount = aggregatedEntries.sumOf(Pair<MediaListStatus?, Int>::second)

    if (totalAmount <= 0) {
        return emptyList()
    }

    return aggregatedEntries.map { (status, amount) ->
        StatusDistributionChartEntry(
            status = status,
            amount = amount,
            fraction = amount / totalAmount.toFloat(),
        )
    }
}

private fun niceAxisStep(value: Int): Int {
    if (value <= 0) {
        return 0
    }

    if (value < 10) {
        return value
    }

    val magnitude = 10.0.pow(floor(log10(value.toDouble()))).toInt()
    val normalized = value.toDouble() / magnitude
    val multiplier =
        when {
            normalized <= 1.0 -> 1.0
            normalized <= 1.2 -> 1.2
            normalized <= 1.5 -> 1.5
            normalized <= 2.0 -> 2.0
            normalized <= 2.5 -> 2.5
            normalized <= 3.0 -> 3.0
            normalized <= 4.0 -> 4.0
            normalized <= 5.0 -> 5.0
            normalized <= 6.0 -> 6.0
            normalized <= 8.0 -> 8.0
            normalized <= 9.0 -> 9.0
            else -> 10.0
        }

    return (multiplier * magnitude).roundToInt()
}
