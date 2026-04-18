package co.anitrend.profile.component.compose

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import co.anitrend.android.core.compose.design.image.AniTrendImage
import co.anitrend.android.core.extensions.toHumanReadableQuantity
import co.anitrend.android.core.helpers.image.model.RequestImage
import co.anitrend.common.markdown.ui.compose.MarkdownText
import co.anitrend.common.shared.ui.compose.chart.ChartLegendRow
import co.anitrend.common.shared.ui.compose.chart.ScoreDistributionAxisTick
import co.anitrend.common.shared.ui.compose.chart.ScoreDistributionBarEntry
import co.anitrend.common.shared.ui.compose.chart.ScoreDistributionChart
import co.anitrend.common.shared.ui.compose.chart.StatusDistributionBar
import co.anitrend.common.shared.ui.compose.chart.StatusDistributionSegment
import co.anitrend.domain.user.entity.User
import co.anitrend.domain.user.entity.attribute.MediaListInfo
import co.anitrend.domain.user.entity.attribute.statistic.MediaStatistic
import co.anitrend.domain.user.entity.attribute.statistic.Statistic
import co.anitrend.domain.user.entity.profile.ProfileOverview
import co.anitrend.profile.R
import co.anitrend.profile.component.model.ProfileSectionState

@Composable
internal fun ProfileMediaTabSelector(
    selectedTab: ProfileMediaTab,
    onTabSelected: (ProfileMediaTab) -> Unit,
) {
    ProfileCompactSegmentedControl(
        items = ProfileMediaTab.entries,
        selectedItem = selectedTab,
        labelFor = { tab ->
            when (tab) {
                ProfileMediaTab.Anime -> stringResource(R.string.label_profile_tab_anime)
                ProfileMediaTab.Manga -> stringResource(R.string.label_profile_tab_manga)
            }
        },
        onItemSelected = onTabSelected,
    )
}

@Composable
internal fun <T> ProfileCompactSegmentedControl(
    items: Iterable<T>,
    selectedItem: T,
    labelFor: @Composable (T) -> String,
    onItemSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = SectionHorizontalPadding),
        shape = SectionShape,
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.18f)),
        tonalElevation = 0.dp,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items.forEach { item ->
                val isSelected = item == selectedItem

                Surface(
                    modifier =
                        Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(18.dp))
                            .clickable { onItemSelected(item) },
                    shape = RoundedCornerShape(18.dp),
                    color = if (isSelected) MaterialTheme.colorScheme.surfaceContainerHighest else Color.Transparent,
                    tonalElevation = if (isSelected) 2.dp else 0.dp,
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 10.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = labelFor(item),
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                            color = if (isSelected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        }
    }
}

@Composable
internal fun ProfileSectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
        )
        if (!actionLabel.isNullOrBlank() && onAction != null) {
            TextButton(onClick = onAction) {
                Text(text = actionLabel)
            }
        }
    }
}

@Composable
internal fun ProfilePosterRailItem(
    media: ProfileOverview.MediaPreview,
    onMediaSelected: (Long, co.anitrend.domain.media.enums.MediaType?) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .width(124.dp)
                .then(
                    if (media.type != null) {
                        Modifier.clickable { onMediaSelected(media.id, media.type) }
                    } else {
                        Modifier
                    },
                ),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(176.dp),
        ) {
            AniTrendImage(
                image = media.image,
                imageType = RequestImage.Media.ImageType.POSTER,
                modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(18.dp)),
                onClick = {},
            )
        }
        Text(
            text = media.displayTitleText(),
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Medium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
internal fun ProfilePosterActivityRow(
    item: ProfileOverview.ListActivityPreview,
    onMediaSelected: (Long, co.anitrend.domain.media.enums.MediaType?) -> Unit,
    modifier: Modifier = Modifier,
) {
    val media = item.media
    val progressToken = item.progress?.toString()?.takeIf(String::isNotBlank)

    Surface(
        modifier =
            modifier
                .fillMaxWidth()
                .then(
                    if (media?.type != null) {
                        Modifier.clickable { onMediaSelected(media.id, media.type) }
                    } else {
                        Modifier
                    },
                ),
        shape = RoundedCornerShape(22.dp),
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.16f)),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            media?.let {
                Box(modifier = Modifier.width(64.dp).height(92.dp)) {
                    AniTrendImage(
                        image = it.image,
                        imageType = RequestImage.Media.ImageType.POSTER,
                        modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(14.dp)),
                        onClick = {},
                    )
                }
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Text(
                    text = media?.displayTitleText().orEmpty(),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                item.activitySummary().takeIf(String::isNotBlank)?.let { summary ->
                    Text(
                        text = summary,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    progressToken?.let { token ->
                        ProfilePill(
                            label = token,
                            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.72f),
                            contentColor = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                    media?.secondaryContext()?.takeIf(String::isNotBlank)?.let { context ->
                        Text(
                            text = context,
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                    formatEpochDate(item.createdAt)?.let { dateLabel ->
                        Text(
                            text = dateLabel,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        }
    }
}

@Composable
internal fun ProfileCompactMetricStrip(
    metricItems: List<Pair<String, String>>,
    modifier: Modifier = Modifier,
) {
    ProfileContainedSectionSurface(modifier = modifier) {
        ProfileMetricStrip(metricItems = metricItems)
    }
}

@Composable
internal fun ProfileContainedSectionSurface(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.16f)),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            content = content,
        )
    }
}

@Composable
internal fun ProfileCompactStateSurface(
    message: String,
    modifier: Modifier = Modifier,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
) {
    ProfileContainedSectionSurface(modifier = modifier) {
        ProfileMessageState(message = message)
        if (!actionLabel.isNullOrBlank() && onAction != null) {
            TextButton(
                onClick = onAction,
                modifier = Modifier.align(Alignment.End),
            ) {
                Text(text = actionLabel)
            }
        }
    }
}

@Composable
internal fun ProfileCompactCtaRow(
    title: String,
    actionLabel: String,
    onAction: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.16f)),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Medium,
            )
            TextButton(onClick = onAction) {
                Text(text = actionLabel)
            }
        }
    }
}

@Composable
internal fun ProfileEditorialMarkdownBlock(
    title: String,
    markdown: String?,
    expanded: Boolean,
    onExpandedChange: (() -> Unit)?,
    modifier: Modifier = Modifier,
    footer: @Composable ColumnScope.() -> Unit = {},
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        ProfileSectionHeader(
            title = title,
            actionLabel =
                if (!markdown.isNullOrBlank() && onExpandedChange != null) {
                    if (expanded) stringResource(R.string.action_profile_show_less) else stringResource(R.string.action_profile_show_more)
                } else {
                    null
                },
            onAction = onExpandedChange,
        )

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = SectionShape,
            color = MaterialTheme.colorScheme.surfaceContainerLow,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.18f)),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                if (markdown.isNullOrBlank()) {
                    Text(
                        text = stringResource(R.string.message_profile_about_empty),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                } else {
                    Box(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .clip(MaterialTheme.shapes.extraLarge)
                                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.6f))
                                .animateContentSize()
                                .then(
                                    if (expanded) {
                                        Modifier
                                    } else {
                                        Modifier.heightIn(max = 240.dp)
                                    },
                                )
                                .padding(16.dp),
                    ) {
                        MarkdownText(content = markdown)
                    }
                }

                footer()
            }
        }
    }
}

@Composable
internal fun ProfileLibrarySnapshotSection(
    details: ProfileDetails,
    selectedTab: ProfileMediaTab,
    statistic: Statistic?,
) {
    val listSections = remember(details, selectedTab) { details.mediaListSections(selectedTab) }
    val metricItems =
        buildLibraryMetricItems(
            details = details,
            statistic = statistic,
            listSections = listSections,
        )

    ProfileSectionCard(
        title = stringResource(R.string.title_profile_section_library_snapshot),
        subtitle = stringResource(R.string.subtitle_profile_section_library_snapshot),
    ) {
        ProfileMetricGrid(metricItems = metricItems)

        if (listSections.primary.isNotEmpty() || listSections.custom.isNotEmpty()) {
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.28f))
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                if (listSections.primary.isNotEmpty()) {
                    Text(
                        text = stringResource(R.string.title_profile_section_library_snapshot),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    MediaListChipRow(items = listSections.primary)
                }
                if (listSections.custom.isNotEmpty()) {
                    Text(
                        text = stringResource(R.string.label_profile_metric_custom_lists),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    MediaListChipRow(items = listSections.custom)
                }
            }
        }
    }
}

@Composable
internal fun ProfileStatsOverviewSection(
    state: ProfileSectionState<Statistic>,
    onRetry: () -> Unit,
) {
    ProfileSectionCard(
        title = stringResource(R.string.title_profile_section_stats_overview),
        subtitle = stringResource(R.string.subtitle_profile_section_stats_overview),
    ) {
        when (state) {
            is ProfileSectionState.Content -> StatsOverviewContent(statistic = state.data)
            is ProfileSectionState.Partial -> StatsOverviewContent(statistic = state.data)
            ProfileSectionState.Loading -> ProfileMessageState(
                message = stringResource(R.string.message_profile_stats_loading),
            )
            is ProfileSectionState.Error -> ProfileRetryState(onRetry = onRetry)
            ProfileSectionState.Empty -> ProfileMessageState(
                message = stringResource(R.string.message_profile_stats_unavailable),
            )
        }
    }
}

@Composable
private fun StatsOverviewContent(
    statistic: Statistic,
) {
    val scoreEntries = remember(statistic) { statistic.scoreEntries() }
    val statusEntries = remember(statistic) { statistic.statusEntries() }
    val metrics = statsMetricItems(statistic)
    val topGenres = remember(statistic) { topGenreEntries(statistic).take(5) }

    ProfileMetricGrid(metricItems = metrics)

    if (scoreEntries.isNotEmpty()) {
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.28f))
        StatsChartBlock(title = stringResource(R.string.label_profile_chart_score_distribution)) {
            ScoreDistributionChart(
                entries = scoreEntries.toScoreBarEntries(),
                compact = true,
                yAxisTicks = scoreEntries.axisValues(labelCount = 2).toAxisTicks(),
            )
        }
    }

    if (statusEntries.isNotEmpty()) {
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.28f))
        StatsChartBlock(title = stringResource(R.string.label_profile_chart_status_distribution)) {
            val segments = statusEntries.toStatusSegments()
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
                        )
                    }
                }
            }
        }
    }

    if (topGenres.isNotEmpty()) {
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.28f))
        StatsChartBlock(title = stringResource(R.string.label_profile_chart_top_genres)) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                topGenres.forEach { (name, count) ->
                    ProfilePill(label = "$name ${count.toHumanReadableQuantity(0)}")
                }
            }
        }
    }
}

@Composable
internal fun AboutProfileSection(
    about: String?,
    previousNames: List<User.PreviousName> = emptyList(),
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    var historyExpanded by rememberSaveable { mutableStateOf(false) }

    ProfileEditorialMarkdownBlock(
        title = stringResource(R.string.title_profile_section_about),
        markdown = about,
        expanded = expanded,
        onExpandedChange = if (!about.isNullOrBlank()) ({ expanded = !expanded }) else null,
        modifier = Modifier.padding(horizontal = SectionHorizontalPadding),
        footer = {
            if (previousNames.isNotEmpty()) {
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.24f))
                PreviousNamesDisclosure(
                    previousNames = previousNames,
                    expanded = historyExpanded,
                    onExpandedChange = { historyExpanded = !historyExpanded },
                )
            }
        },
    )
}

@Composable
private fun PreviousNamesDisclosure(
    previousNames: List<User.PreviousName>,
    expanded: Boolean,
    onExpandedChange: () -> Unit,
) {
    val visibleNames = if (expanded) previousNames else previousNames.take(3)

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.label_profile_previous_names_count, previousNames.size),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Medium,
            )
            TextButton(onClick = onExpandedChange) {
                Text(
                    text =
                        if (expanded) {
                            stringResource(R.string.action_profile_show_less)
                        } else {
                            stringResource(R.string.action_profile_show_more)
                        },
                )
            }
        }

        visibleNames.forEach { previousName ->
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.54f),
                shape = MaterialTheme.shapes.large,
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                ) {
                    Text(
                        text = previousName.name?.toString().orEmpty(),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Medium,
                    )
                    formatEpochDate(previousName.updatedAt ?: previousName.createdAt)?.let { dateLabel ->
                        Text(
                            text = dateLabel,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun buildLibraryMetricItems(
    details: ProfileDetails,
    statistic: Statistic?,
    listSections: ProfileMediaListSections,
): List<Pair<String, String>> =
    buildList {
        add(
            stringLabelAndValue(
                labelRes = R.string.label_profile_metric_entries,
                value =
                    statistic
                        ?.count
                        ?.toHumanReadableQuantity(0)
                        ?: (listSections.primary.sumOf(MediaListInfo::count) + listSections.custom.sumOf(MediaListInfo::count))
                            .toHumanReadableQuantity(0),
            ),
        )
        add(
            stringLabelAndValue(
                labelRes = R.string.label_profile_metric_custom_lists,
                value = listSections.custom.size.toString(),
            ),
        )
        add(
            stringLabelAndValue(
                labelRes = R.string.label_profile_fact_score_format,
                value = details.listOption.scoreFormat.displayLabel(),
            ),
        )
        add(
            stringLabelAndValue(
                labelRes = R.string.label_profile_metric_row_order,
                value = details.listOption.rowOrder?.toString().orEmpty().ifBlank { stringResource(R.string.label_profile_row_order_default) },
            ),
        )
    }

@Composable
internal fun statsMetricItems(
    statistic: Statistic,
): List<Pair<String, String>> =
    buildList {
        add(stringLabelAndValue(R.string.label_profile_metric_mean_score, statistic.meanScore.displayScore()))
        add(
            stringLabelAndValue(
                R.string.label_profile_metric_standard_deviation,
                statistic.standardDeviation.displayScore(),
            ),
        )
        add(stringLabelAndValue(R.string.label_profile_metric_entries, statistic.count.toHumanReadableQuantity(0)))
        when (statistic) {
            is Statistic.Anime -> {
                add(
                    stringLabelAndValue(
                        R.string.label_profile_metric_episodes_watched,
                        statistic.episodesWatched.toHumanReadableQuantity(0),
                    ),
                )
                add(
                    stringLabelAndValue(
                        R.string.label_profile_metric_minutes_watched,
                        statistic.minutesWatched.toHumanReadableQuantity(0),
                    ),
                )
            }
            is Statistic.Manga -> {
                add(
                    stringLabelAndValue(
                        R.string.label_profile_metric_chapters_read,
                        statistic.chaptersRead.toHumanReadableQuantity(0),
                    ),
                )
                add(
                    stringLabelAndValue(
                        R.string.label_profile_metric_volumes_read,
                        statistic.volumesRead.toHumanReadableQuantity(0),
                    ),
                )
            }
        }
    }

internal fun topGenreEntries(
    statistic: Statistic,
): List<Pair<String, Int>> =
    when (statistic) {
        is Statistic.Anime ->
            statistic.genres
                .orEmpty()
                .filterIsInstance<MediaStatistic.Anime.Genre>()
                .sortedByDescending(MediaStatistic.Anime.Genre::count)
                .map { it.genre to it.count }
        is Statistic.Manga ->
            statistic.genres
                .orEmpty()
                .filterIsInstance<MediaStatistic.Manga.Genre>()
                .sortedByDescending(MediaStatistic.Manga.Genre::count)
                .map { it.genre to it.count }
    }

internal fun List<ProfileScoreEntry>.toScoreBarEntries(): List<ScoreDistributionBarEntry> =
    map { entry ->
        ScoreDistributionBarEntry(
            label = entry.score.toString(),
            value = entry.amount,
            contentDescription = "${entry.score}: ${entry.amount.toHumanReadableQuantity(0)}",
        )
    }

internal fun List<Int>.toAxisTicks(): List<ScoreDistributionAxisTick> =
    map { value ->
        ScoreDistributionAxisTick(
            label = value.toHumanReadableQuantity(0),
            value = value,
        )
    }

@Composable
internal fun List<ProfileStatusEntry>.toStatusSegments(): List<StatusDistributionSegment> =
    map { entry ->
        val label = entry.status.displayLabel()
        val value = entry.amount.toHumanReadableQuantity(0)
        StatusDistributionSegment(
            label = label,
            value = value,
            fraction = entry.fraction,
            color = statusSegmentColor(entry.status),
        )
    }
