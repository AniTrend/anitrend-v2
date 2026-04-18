package co.anitrend.profile.component.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.anitrend.android.core.extensions.toHumanReadableQuantity
import co.anitrend.android.core.ui.theme.preview.PreviewTheme
import co.anitrend.common.shared.ui.compose.chart.ChartLegendRow
import co.anitrend.common.shared.ui.compose.chart.ScoreDistributionChart
import co.anitrend.common.shared.ui.compose.chart.StatusDistributionBar
import co.anitrend.domain.user.entity.attribute.statistic.Statistic
import co.anitrend.profile.R
import co.anitrend.profile.component.model.ProfileSectionState

@Composable
internal fun ProfileStatsTab(
    state: ProfileSectionState<Statistic>,
    selectedTab: ProfileMediaTab,
    onTabSelected: (ProfileMediaTab) -> Unit,
    onRetry: () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        ProfileMediaTabSelector(
            selectedTab = selectedTab,
            onTabSelected = onTabSelected,
        )

        when (state) {
            ProfileSectionState.Loading ->
                StatsStateSection(
                    message = stringResource(R.string.message_profile_stats_loading),
                    onRetry = null,
                )

            is ProfileSectionState.Error ->
                StatsStateSection(
                    message = stringResource(R.string.message_profile_stats_unavailable),
                    onRetry = onRetry,
                )

            ProfileSectionState.Empty ->
                StatsStateSection(
                    message = stringResource(R.string.message_profile_stats_unavailable),
                    onRetry = null,
                )

            is ProfileSectionState.Content -> StatsTabContent(statistic = state.data)

            is ProfileSectionState.Partial -> StatsTabContent(statistic = state.data)
        }
    }
}

@Composable
private fun StatsStateSection(
    message: String,
    onRetry: (() -> Unit)?,
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        ProfileSectionHeader(
            title = stringResource(R.string.title_profile_section_stats_overview),
            modifier = Modifier.padding(horizontal = SectionHorizontalPadding),
        )
        ProfileCompactStateSurface(
            message = message,
            modifier = Modifier.padding(horizontal = SectionHorizontalPadding),
            actionLabel = onRetry?.let { stringResource(co.anitrend.core.R.string.label_text_action_retry) },
            onAction = onRetry,
        )
    }
}

@Composable
private fun StatsTabContent(
    statistic: Statistic,
) {
    ProfileStatsMetricStripSection(statistic = statistic)
    ProfileStatsHeroChartSection(statistic = statistic)

    statistic.secondaryChart()?.let { chart ->
        ProfileStatsSupportingChartSection(
            statistic = statistic,
            chart = chart,
        )
    }

    val topGenres = remember(statistic) { topGenreEntries(statistic).take(5) }
    if (topGenres.isNotEmpty()) {
        ProfileStatsTasteGroupsSection(topGenres = topGenres)
    }
}

@Composable
private fun ProfileStatsMetricStripSection(
    statistic: Statistic,
) {
    val metrics = statsMetricItems(statistic).take(5)

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        ProfileSectionHeader(
            title = stringResource(R.string.title_profile_section_stats_metric_strip),
            modifier = Modifier.padding(horizontal = SectionHorizontalPadding),
        )
        ProfileCompactMetricStrip(
            metricItems = metrics,
            modifier = Modifier.padding(horizontal = SectionHorizontalPadding),
        )
    }
}

@Composable
private fun ProfileStatsHeroChartSection(
    statistic: Statistic,
) {
    val heroChart = remember(statistic) { statistic.preferredHeroChart() }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        ProfileSectionHeader(
            title = stringResource(R.string.title_profile_section_stats_hero_chart),
            modifier = Modifier.padding(horizontal = SectionHorizontalPadding),
        )

        ProfileContainedSectionSurface(
            modifier = Modifier.padding(horizontal = SectionHorizontalPadding),
        ) {
            when (heroChart) {
                ProfileStatsChart.ScoreDistribution -> ScoreChartContent(statistic = statistic, compact = false)
                ProfileStatsChart.StatusDistribution -> StatusChartContent(statistic = statistic)
                null -> ProfileMessageState(message = stringResource(R.string.message_profile_stats_unavailable))
            }
        }
    }
}

@Composable
private fun ProfileStatsSupportingChartSection(
    statistic: Statistic,
    chart: ProfileStatsChart,
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        ProfileSectionHeader(
            title = stringResource(R.string.title_profile_section_stats_supporting_chart),
            modifier = Modifier.padding(horizontal = SectionHorizontalPadding),
        )

        ProfileContainedSectionSurface(
            modifier = Modifier.padding(horizontal = SectionHorizontalPadding),
        ) {
            when (chart) {
                ProfileStatsChart.ScoreDistribution -> ScoreChartContent(statistic = statistic, compact = true)
                ProfileStatsChart.StatusDistribution -> StatusChartContent(statistic = statistic)
            }
        }
    }
}

@Composable
private fun ScoreChartContent(
    statistic: Statistic,
    compact: Boolean,
) {
    val scoreEntries = remember(statistic) { statistic.scoreEntries() }

    if (scoreEntries.isEmpty()) {
        ProfileMessageState(message = stringResource(R.string.message_profile_stats_unavailable))
        return
    }

    StatsChartBlock(title = stringResource(R.string.label_profile_chart_score_distribution)) {
        ScoreDistributionChart(
            entries = scoreEntries.toScoreBarEntries(),
            compact = compact,
            yAxisTicks = scoreEntries.axisValues(labelCount = if (compact) 2 else 3).toAxisTicks(),
        )
    }
}

@Composable
private fun StatusChartContent(
    statistic: Statistic,
) {
    val statusEntries = remember(statistic) { statistic.statusEntries() }

    if (statusEntries.isEmpty()) {
        ProfileMessageState(message = stringResource(R.string.message_profile_stats_unavailable))
        return
    }

    val segments = statusEntries.toStatusSegments()

    StatsChartBlock(title = stringResource(R.string.label_profile_chart_status_distribution)) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            StatusDistributionBar(segments = segments)
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                segments.forEach { segment ->
                    ChartLegendRow(
                        label = segment.label,
                        value = segment.value,
                        color = segment.color,
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfileStatsTasteGroupsSection(
    topGenres: List<Pair<String, Int>>,
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        ProfileSectionHeader(
            title = stringResource(R.string.title_profile_section_stats_taste_groups),
            modifier = Modifier.padding(horizontal = SectionHorizontalPadding),
        )

        ProfileContainedSectionSurface(
            modifier = Modifier.padding(horizontal = SectionHorizontalPadding),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                topGenres.forEachIndexed { index, (name, count) ->
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.58f),
                        shape = MaterialTheme.shapes.large,
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 14.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            Text(
                                text = (index + 1).toString(),
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary,
                            )
                            Text(
                                text = name,
                                modifier = Modifier.weight(1f),
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium,
                            )
                            ProfilePill(label = count.toHumanReadableQuantity(0))
                        }
                    }
                }
            }
        }
    }
}

@Preview(name = "Profile Stats Tab")
@Composable
private fun ProfileStatsTabPreview() {
    val previewUser = previewProfileUser()

    PreviewTheme(darkTheme = true, wrapInSurface = true) {
        ProfileStatsTab(
            state = ProfileSectionState.Content(requireNotNull(previewUser.statisticFor(ProfileMediaTab.Anime))),
            selectedTab = ProfileMediaTab.Anime,
            onTabSelected = {},
            onRetry = {},
        )
    }
}
