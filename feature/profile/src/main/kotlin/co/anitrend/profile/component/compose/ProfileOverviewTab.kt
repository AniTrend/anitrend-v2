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
package co.anitrend.profile.component.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import co.anitrend.common.shared.ui.compose.chart.ChartLegendRow
import co.anitrend.common.shared.ui.compose.chart.ScoreDistributionChart
import co.anitrend.common.shared.ui.compose.chart.StatusDistributionBar
import co.anitrend.domain.media.enums.MediaType
import co.anitrend.domain.user.entity.User
import co.anitrend.domain.user.entity.attribute.statistic.Statistic
import co.anitrend.domain.user.entity.profile.ProfileOverview
import co.anitrend.profile.R
import co.anitrend.profile.component.model.ProfileSectionState

@Composable
internal fun ProfileOverviewTab(
    user: User,
    displayUser: User,
    details: ProfileDetails?,
    overviewState: ProfileSectionState<ProfileOverview>,
    statsState: ProfileSectionState<Statistic>,
    onOpenStats: () -> Unit,
    onMediaSelected: (Long, MediaType?) -> Unit,
) {
    val overview = overviewState.state
    val favourites = remember(overview) { overview?.favouritesRail().orEmpty() }
    val recentItems = remember(overview) { overview?.recentActivityPreview(limit = 3).orEmpty() }
    val libraryPulse = remember(details, displayUser) { details?.libraryPulseSummary(displayUser) }

    Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
        if (favourites.isNotEmpty()) {
            ProfileFavouritesRailSection(
                favourites = favourites,
                onMediaSelected = onMediaSelected,
            )
        }

        if (recentItems.isNotEmpty()) {
            ProfileRecentSection(
                items = recentItems,
                onMediaSelected = onMediaSelected,
            )
        }

        libraryPulse?.let { summary ->
            ProfileLibraryPulseSection(summary = summary)
        }

        ProfileStatsPreviewSection(
            state = statsState,
            onOpenStats = onOpenStats,
        )

        AboutProfileSection(
            about = user.status.about?.toString(),
            previousNames = details?.previousNames.orEmpty(),
        )
    }
}

@Composable
private fun ProfileFavouritesRailSection(
    favourites: List<ProfileOverview.MediaPreview>,
    onMediaSelected: (Long, MediaType?) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        ProfileSectionHeader(
            title = stringResource(R.string.title_profile_section_favourites_showcase),
            modifier = Modifier.padding(horizontal = SectionHorizontalPadding),
        )

        LazyRow(
            contentPadding = PaddingValues(horizontal = SectionHorizontalPadding),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(favourites, key = ProfileOverview.MediaPreview::id) { media ->
                ProfilePosterRailItem(
                    media = media,
                    onMediaSelected = onMediaSelected,
                )
            }
        }
    }
}

@Composable
private fun ProfileRecentSection(
    items: List<ProfileOverview.ListActivityPreview>,
    onMediaSelected: (Long, MediaType?) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        ProfileSectionHeader(
            title = stringResource(R.string.title_profile_section_recent_activity),
            modifier = Modifier.padding(horizontal = SectionHorizontalPadding),
        )

        Column(
            modifier = Modifier.padding(horizontal = SectionHorizontalPadding),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            items.forEach { item ->
                ProfilePosterActivityRow(
                    item = item,
                    onMediaSelected = onMediaSelected,
                )
            }
        }
    }
}

@Composable
private fun ProfileLibraryPulseSection(summary: ProfileLibraryPulseSummary) {
    val metricItems =
        listOf(
            stringResource(R.string.label_profile_metric_anime_total) to summary.animeTotal.toString(),
            stringResource(R.string.label_profile_metric_manga_total) to summary.mangaTotal.toString(),
            stringResource(R.string.label_profile_metric_progress_footprint) to summary.progressFootprint,
            stringResource(R.string.label_profile_metric_collection_bias) to
                summary.dominantStatus.ifBlank {
                    stringResource(R.string.label_profile_row_order_default)
                },
        )

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        ProfileSectionHeader(
            title = stringResource(R.string.title_profile_section_library_pulse),
            modifier = Modifier.padding(horizontal = SectionHorizontalPadding),
        )

        ProfileCompactMetricStrip(
            metricItems = metricItems,
            modifier = Modifier.padding(horizontal = SectionHorizontalPadding),
        )
    }
}

@Composable
private fun ProfileStatsPreviewSection(
    state: ProfileSectionState<Statistic>,
    onOpenStats: () -> Unit,
) {
    val statistic = state.state
    val hasChart = remember(statistic) { statistic?.preferredHeroChart() != null }

    if (statistic == null || !hasChart) {
        ProfileCompactCtaRow(
            title = stringResource(R.string.title_profile_section_stats_preview),
            actionLabel = stringResource(R.string.action_profile_open_stats),
            onAction = onOpenStats,
            modifier = Modifier.padding(horizontal = SectionHorizontalPadding),
        )
        return
    }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        ProfileSectionHeader(
            title = stringResource(R.string.title_profile_section_stats_preview),
            modifier = Modifier.padding(horizontal = SectionHorizontalPadding),
            actionLabel = stringResource(R.string.action_profile_open_stats),
            onAction = onOpenStats,
        )

        Surface(
            modifier = Modifier.padding(horizontal = SectionHorizontalPadding),
            shape = SectionShape,
            color = MaterialTheme.colorScheme.surfaceContainerLow,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.18f)),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                StatsPreviewChart(statistic = statistic)
            }
        }
    }
}

@Composable
private fun StatsPreviewChart(statistic: Statistic) {
    val scoreEntries = remember(statistic) { statistic.scoreEntries() }
    val statusEntries = remember(statistic) { statistic.statusEntries() }

    when {
        scoreEntries.isNotEmpty() -> {
            Text(
                text = stringResource(R.string.label_profile_chart_score_distribution),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
            )
            ScoreDistributionChart(
                entries = scoreEntries.toScoreBarEntries(),
                compact = true,
                yAxisTicks = scoreEntries.axisValues(labelCount = 2).toAxisTicks(),
            )
        }

        statusEntries.isNotEmpty() -> {
            val segments = statusEntries.toStatusSegments()

            Text(
                text = stringResource(R.string.label_profile_chart_status_distribution),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
            )
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
}
