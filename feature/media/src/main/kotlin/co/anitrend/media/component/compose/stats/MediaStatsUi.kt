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

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import co.anitrend.android.core.extensions.toHumanReadableQuantity
import co.anitrend.android.core.ui.AniTrendPreview
import co.anitrend.android.core.ui.theme.preview.DarkThemeProvider
import co.anitrend.android.core.ui.theme.preview.PreviewTheme
import co.anitrend.arch.domain.entities.LoadState
import co.anitrend.common.shared.ui.compose.DefaultScaffold
import co.anitrend.common.shared.ui.compose.chart.ChartLegendRow
import co.anitrend.common.shared.ui.compose.chart.ScoreDistributionAxisTick
import co.anitrend.common.shared.ui.compose.chart.ScoreDistributionBarEntry
import co.anitrend.common.shared.ui.compose.chart.ScoreDistributionChart
import co.anitrend.common.shared.ui.compose.chart.StatusDistributionBar
import co.anitrend.common.shared.ui.compose.chart.StatusDistributionSegment
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.media.entity.MediaStats
import co.anitrend.domain.medialist.enums.MediaListStatus
import co.anitrend.media.R
import co.anitrend.media.component.compose.MediaComposePreviewProvider
import co.anitrend.media.component.compose.section.MediaHubSection
import co.anitrend.media.component.compose.section.MediaHubSectionEmptyState
import co.anitrend.media.component.compose.section.MediaHubSectionErrorState
import co.anitrend.media.component.compose.section.MediaHubSectionRetryState
import co.anitrend.common.media.ui.R as MediaUiR

private data class StatsMetricItem(
    val label: String,
    val value: String,
)

@Composable
internal fun MediaStatsSection(
    media: Media.Extended,
    stats: MediaStats?,
    loadState: LoadState?,
    onRetry: () -> Unit,
    onSeeAllClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val summarySnapshot = remember(media) { media.toMediaStatsSummarySnapshot() }
    val scoreEntries = remember(stats) { stats.scoreChartEntries() }
    val statusEntries = remember(stats) { stats.statusChartEntries() }
    val hasSummaryMetrics = summarySnapshot.hasAnyValue()
    val hasDistributionData = scoreEntries.isNotEmpty() || statusEntries.isNotEmpty()
    val isDistributionLoading = (loadState == null || loadState is LoadState.Loading) && !hasDistributionData
    val isDistributionError = loadState is LoadState.Error && !hasDistributionData
    val showSeeMore = hasSummaryMetrics || hasDistributionData

    MediaHubSection(
        title = stringResource(R.string.title_media_stats_section),
        subtitle = stringResource(R.string.subtitle_media_stats_section),
        trailingActionLabel = if (showSeeMore) stringResource(R.string.action_media_stats_section_see_more) else null,
        onTrailingAction = if (showSeeMore) onSeeAllClick else null,
        modifier = modifier,
    ) {
        if (hasSummaryMetrics) {
            MediaStatsSummaryGrid(summarySnapshot = summarySnapshot)
        }

        if (hasSummaryMetrics && (hasDistributionData || isDistributionLoading || isDistributionError)) {
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f))
        }

        when {
            hasDistributionData -> {
                MediaStatsDistributions(
                    scoreEntries = scoreEntries,
                    statusEntries = statusEntries,
                    compact = true,
                )
            }

            isDistributionLoading -> {
                MediaStatsLoadingContent(
                    compact = true,
                    showSummarySkeleton = !hasSummaryMetrics,
                )
            }

            isDistributionError -> {
                MediaStatsRetryState(
                    title = stringResource(R.string.label_media_stats_error_title),
                    onRetry = onRetry,
                )
            }

            !hasSummaryMetrics -> {
                MediaHubSectionEmptyState(
                    title = stringResource(R.string.label_media_stats_empty_title),
                    message = stringResource(R.string.message_media_stats_empty),
                )
            }
        }
    }
}

@Composable
internal fun MediaStatsScreenContent(
    summarySnapshot: MediaStatsSummarySnapshot,
    stats: MediaStats?,
    loadState: LoadState?,
    mediaTitle: String?,
    onBackPress: () -> Unit,
    onRetry: () -> Unit,
) {
    val scoreEntries = remember(stats) { stats.scoreChartEntries() }
    val statusEntries = remember(stats) { stats.statusChartEntries() }
    val hasSummaryMetrics = summarySnapshot.hasAnyValue()
    val hasDistributionData = scoreEntries.isNotEmpty() || statusEntries.isNotEmpty()
    val isDistributionLoading = (loadState == null || loadState is LoadState.Loading) && !hasDistributionData
    val isDistributionError = loadState is LoadState.Error && !hasDistributionData

    StatsScreenScaffold(
        title = stringResource(R.string.title_media_stats_screen),
        subtitle = stringResource(R.string.subtitle_media_stats_screen),
        mediaTitle = mediaTitle,
        onBackPress = onBackPress,
    ) {
        when {
            hasSummaryMetrics || hasDistributionData || isDistributionLoading || isDistributionError -> {
                Column(
                    modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(18.dp),
                ) {
                    if (hasSummaryMetrics) {
                        MediaStatsSummaryGrid(summarySnapshot = summarySnapshot)
                    } else if (isDistributionLoading) {
                        SummaryMetricSkeletonGrid()
                    }

                    when {
                        hasDistributionData -> {
                            MediaStatsDistributions(
                                scoreEntries = scoreEntries,
                                statusEntries = statusEntries,
                                compact = false,
                            )
                        }

                        isDistributionLoading -> {
                            MediaStatsLoadingContent(
                                compact = false,
                                showSummarySkeleton = false,
                            )
                        }

                        isDistributionError -> {
                            MediaStatsRetryState(
                                title = stringResource(R.string.label_media_stats_error_title),
                                onRetry = onRetry,
                            )
                        }

                        else -> {
                            MediaStatsSummaryOnlyMessage()
                        }
                    }
                }
            }

            else -> {
                CenteredStatsState(
                    title = stringResource(R.string.label_media_stats_empty_title),
                    subtitle = stringResource(R.string.message_media_stats_empty),
                )
            }
        }
    }
}

@Composable
private fun StatsScreenScaffold(
    title: String,
    subtitle: String,
    mediaTitle: String?,
    onBackPress: () -> Unit,
    content: @Composable () -> Unit,
) {
    DefaultScaffold(onBackPress = onBackPress) { padding ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold,
                )
                mediaTitle
                    ?.takeIf(String::isNotBlank)
                    ?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium,
                        )
                    }
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            Box(modifier = Modifier.weight(1f)) {
                content()
            }
        }
    }
}

@Composable
private fun MediaStatsSummaryGrid(
    summarySnapshot: MediaStatsSummarySnapshot,
    modifier: Modifier = Modifier,
) {
    val items = statsMetricItems(summarySnapshot)
    if (items.isEmpty()) {
        return
    }

    FlowRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items.forEach { item ->
            StatsMetricCard(
                label = item.label,
                value = item.value,
                modifier = Modifier.widthIn(min = 132.dp, max = 180.dp),
            )
        }
    }
}

@Composable
private fun statsMetricItems(summarySnapshot: MediaStatsSummarySnapshot): List<StatsMetricItem> =
    buildList {
        summarySnapshot.averageScore?.let {
            add(
                StatsMetricItem(
                    label = stringResource(R.string.label_media_stats_average_score),
                    value = it.toString(),
                ),
            )
        }
        summarySnapshot.favourites?.let {
            add(
                StatsMetricItem(
                    label = stringResource(R.string.label_media_stats_favourites),
                    value = it.toHumanReadableQuantity(0),
                ),
            )
        }
        summarySnapshot.popularity?.let {
            add(
                StatsMetricItem(
                    label = stringResource(R.string.label_media_stats_popularity),
                    value = it.toHumanReadableQuantity(0),
                ),
            )
        }
        summarySnapshot.trendRank?.let {
            add(
                StatsMetricItem(
                    label = stringResource(R.string.label_media_stats_trending),
                    value = "#${it.toHumanReadableQuantity(0)}",
                ),
            )
        }
    }

@Composable
private fun StatsMetricCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.72f),
        contentColor = MaterialTheme.colorScheme.onSurface,
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.38f)),
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun MediaStatsDistributions(
    scoreEntries: List<ScoreDistributionChartEntry>,
    statusEntries: List<StatusDistributionChartEntry>,
    compact: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(if (compact) 14.dp else 18.dp),
    ) {
        if (scoreEntries.isNotEmpty()) {
            val axisValues = remember(scoreEntries, compact) { scoreEntries.scoreChartAxisValues(labelCount = if (compact) 2 else 3) }
            StatsChartBlock(title = stringResource(R.string.label_media_stats_score_distribution)) {
                ScoreDistributionChart(
                    entries = scoreEntries.asScoreBarEntries(),
                    compact = compact,
                    yAxisTicks = axisValues.asScoreAxisTicks(),
                )
            }
        }

        if (scoreEntries.isNotEmpty() && statusEntries.isNotEmpty()) {
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f))
        }

        if (statusEntries.isNotEmpty()) {
            val segments = statusEntries.asStatusSegments()
            StatsChartBlock(title = stringResource(R.string.label_media_stats_status_distribution)) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    StatusDistributionBar(segments = segments)
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        segments.forEach { segment ->
                            ChartLegendRow(
                                label = segment.label,
                                value = segment.value,
                                color = segment.color,
                                contentDescription = segment.contentDescription,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatsChartBlock(
    title: String,
    content: @Composable () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.SemiBold,
        )
        content()
    }
}

@Composable
private fun List<ScoreDistributionChartEntry>.asScoreBarEntries(): List<ScoreDistributionBarEntry> =
    map { entry ->
        ScoreDistributionBarEntry(
            label = entry.score.toString(),
            value = entry.amount,
            contentDescription =
                stringResource(
                    R.string.description_media_stats_score_distribution_bar,
                    entry.score,
                    entry.amount.toHumanReadableQuantity(0),
                ),
        )
    }

private fun List<Int>.asScoreAxisTicks(): List<ScoreDistributionAxisTick> =
    map { value ->
        ScoreDistributionAxisTick(
            label = value.toHumanReadableQuantity(0),
            value = value,
        )
    }

@Composable
private fun List<StatusDistributionChartEntry>.asStatusSegments(): List<StatusDistributionSegment> {
    val colors = MaterialTheme.colorScheme

    return map { entry ->
        val label = entry.status?.alias?.toString() ?: stringResource(MediaUiR.string.label_media_status_unknown_value)
        val value = entry.amount.toHumanReadableQuantity(0)
        StatusDistributionSegment(
            label = label,
            value = value,
            fraction = entry.fraction,
            color = statusSegmentColor(entry.status, colors),
            contentDescription =
                stringResource(
                    R.string.description_media_stats_status_distribution_segment,
                    label,
                    value,
                ),
        )
    }
}

private fun statusSegmentColor(
    status: MediaListStatus?,
    colors: androidx.compose.material3.ColorScheme,
): Color =
    when (status) {
        MediaListStatus.COMPLETED -> colors.primary
        MediaListStatus.PLANNING -> colors.tertiary
        MediaListStatus.CURRENT -> colors.secondary
        MediaListStatus.PAUSED -> colors.secondaryContainer
        MediaListStatus.DROPPED -> colors.error.copy(alpha = 0.82f)
        MediaListStatus.REPEATING -> colors.primaryContainer
        null -> colors.outline
    }

@Composable
private fun MediaStatsLoadingContent(
    compact: Boolean,
    showSummarySkeleton: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(if (compact) 14.dp else 18.dp),
    ) {
        if (showSummarySkeleton) {
            SummaryMetricSkeletonGrid()
        }
        StatsChartBlock(title = stringResource(R.string.label_media_stats_score_distribution)) {
            ScoreChartSkeleton(compact = compact)
        }
        StatsChartBlock(title = stringResource(R.string.label_media_stats_status_distribution)) {
            StatusDistributionSkeleton()
        }
    }
}

@Composable
private fun SummaryMetricSkeletonGrid() {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        repeat(4) {
            Surface(
                modifier = Modifier.widthIn(min = 132.dp, max = 180.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.24f),
                shape = RoundedCornerShape(20.dp),
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    PlaceholderLine(width = 64.dp, height = 10.dp)
                    PlaceholderLine(width = 72.dp, height = 20.dp)
                }
            }
        }
    }
}

@Composable
private fun ScoreChartSkeleton(compact: Boolean) {
    val chartHeight = if (compact) 120.dp else 188.dp
    val barHeights = listOf(0.16f, 0.24f, 0.32f, 0.44f, 0.58f, 0.72f, 0.86f, 1f, 0.82f, 0.56f)
    val axisLabelCount = if (compact) 2 else 3
    val axisLabelWidth = if (compact) 36.dp else 44.dp
    val axisSpacing = if (compact) 6.dp else 10.dp

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(axisSpacing),
            verticalAlignment = Alignment.Bottom,
        ) {
            Column(
                modifier = Modifier.height(chartHeight).width(axisLabelWidth),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.End,
            ) {
                repeat(axisLabelCount) {
                    PlaceholderLine(
                        width = if (compact) 22.dp else 28.dp,
                        height = 8.dp,
                    )
                }
            }

            Row(
                modifier = Modifier.weight(1f).height(chartHeight),
                horizontalArrangement = Arrangement.spacedBy(if (compact) 4.dp else 6.dp),
                verticalAlignment = Alignment.Bottom,
            ) {
                barHeights.forEach { fraction ->
                    Box(
                        modifier = Modifier.weight(1f).fillMaxSize(),
                        contentAlignment = Alignment.BottomCenter,
                    ) {
                        Surface(
                            modifier = Modifier.fillMaxWidth(if (compact) 0.72f else 0.62f).fillMaxHeight(fraction.coerceAtLeast(0.08f)),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.24f),
                            shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp, bottomStart = 4.dp, bottomEnd = 4.dp),
                        ) {}
                    }
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
        ) {
            Spacer(modifier = Modifier.width(axisLabelWidth + axisSpacing))
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(if (compact) 4.dp else 6.dp),
            ) {
                repeat(10) {
                    PlaceholderLine(
                        modifier = Modifier.weight(1f),
                        width = 0.dp,
                        height = 8.dp,
                    )
                }
            }
        }
    }
}

@Composable
private fun StatusDistributionSkeleton() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Surface(
            modifier = Modifier.fillMaxWidth().height(18.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.24f),
            shape = RoundedCornerShape(999.dp),
        ) {}
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            repeat(4) {
                PlaceholderLine(width = 88.dp, height = 12.dp)
            }
        }
    }
}

@Composable
private fun PlaceholderLine(
    width: androidx.compose.ui.unit.Dp,
    height: androidx.compose.ui.unit.Dp,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.then(if (width > 0.dp) Modifier.width(width) else Modifier).height(height),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.28f),
        shape = RoundedCornerShape(999.dp),
    ) {}
}

@Composable
private fun MediaStatsRetryState(
    title: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    MediaHubSectionRetryState(
        title = title,
        onRetry = onRetry,
        modifier = modifier,
    )
}

@Composable
private fun MediaStatsSummaryOnlyMessage() {
    Text(
        text = stringResource(R.string.message_media_stats_distribution_empty),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

@Composable
private fun CenteredStatsState(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

private val PreviewMediaStats =
    MediaStats(
        scoreDistribution =
            listOf(
                MediaStats.ScoreDistribution(amount = 4, score = 10),
                MediaStats.ScoreDistribution(amount = 8, score = 20),
                MediaStats.ScoreDistribution(amount = 14, score = 30),
                MediaStats.ScoreDistribution(amount = 22, score = 40),
                MediaStats.ScoreDistribution(amount = 36, score = 50),
                MediaStats.ScoreDistribution(amount = 48, score = 60),
                MediaStats.ScoreDistribution(amount = 66, score = 70),
                MediaStats.ScoreDistribution(amount = 104, score = 80),
                MediaStats.ScoreDistribution(amount = 84, score = 90),
                MediaStats.ScoreDistribution(amount = 52, score = 100),
            ),
        statusDistribution =
            listOf(
                MediaStats.StatusDistribution(amount = 816_000, status = MediaListStatus.COMPLETED),
                MediaStats.StatusDistribution(amount = 65_000, status = MediaListStatus.PLANNING),
                MediaStats.StatusDistribution(amount = 59_000, status = MediaListStatus.CURRENT),
                MediaStats.StatusDistribution(amount = 15_000, status = MediaListStatus.DROPPED),
            ),
    )

@AniTrendPreview.Default
@Composable
private fun MediaStatsSectionPreview(
    @PreviewParameter(MediaComposePreviewProvider::class) media: Media.Extended,
) {
    PreviewTheme(wrapInSurface = true) {
        MediaStatsSection(
            media = media,
            stats = PreviewMediaStats,
            loadState = null,
            onRetry = {},
            onSeeAllClick = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}

@AniTrendPreview.Default
@Composable
private fun MediaStatsScreenContentPreview(
    @PreviewParameter(DarkThemeProvider::class) darkTheme: Boolean,
) {
    PreviewTheme(darkTheme = darkTheme, wrapInSurface = true) {
        MediaStatsScreenContent(
            summarySnapshot =
                buildMediaStatsSummarySnapshot(
                    averageScore = 85,
                    favourites = 76_000,
                    popularity = 971_000,
                    trendRank = 23,
                ),
            stats = PreviewMediaStats,
            loadState = null,
            mediaTitle = "Solo Leveling",
            onBackPress = {},
            onRetry = {},
        )
    }
}
