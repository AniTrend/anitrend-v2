package co.anitrend.profile.component.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.anitrend.android.core.extensions.toHumanReadableQuantity
import co.anitrend.android.core.ui.theme.preview.PreviewTheme
import co.anitrend.common.shared.ui.compose.chart.ChartLegendRow
import co.anitrend.common.shared.ui.compose.chart.StatusDistributionBar
import co.anitrend.domain.media.enums.MediaType
import co.anitrend.domain.user.entity.attribute.option.UserMediaListTypeOptions
import co.anitrend.domain.user.entity.attribute.statistic.Statistic
import co.anitrend.domain.user.entity.profile.ProfileOverview
import co.anitrend.profile.R
import co.anitrend.profile.component.model.ProfileSectionState

@Composable
internal fun ProfileLibraryTab(
    details: ProfileDetails?,
    overviewState: ProfileSectionState<ProfileOverview>,
    statsState: ProfileSectionState<Statistic>,
    selectedTab: ProfileMediaTab,
    onTabSelected: (ProfileMediaTab) -> Unit,
    onMediaSelected: (Long, MediaType?) -> Unit,
    onOverviewRetry: () -> Unit,
    onStatsRetry: () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        ProfileMediaTabSelector(
            selectedTab = selectedTab,
            onTabSelected = onTabSelected,
        )

        if (details == null) {
            ProfileLibraryStateSection(
                title = stringResource(R.string.title_profile_section_collection_totals),
                message = stringResource(R.string.message_profile_library_unavailable),
            )
        } else {
            ProfileLibraryCollectionSection(
                details = details,
                statistic = statsState.state,
                selectedTab = selectedTab,
            )
            ProfileLibraryOrganisationSection(
                details = details,
                selectedTab = selectedTab,
            )
        }

        ProfileLibraryStatusSection(
            state = statsState,
            onRetry = onStatsRetry,
        )
        ProfileLibraryRecentUpdatesSection(
            state = overviewState,
            selectedTab = selectedTab,
            onMediaSelected = onMediaSelected,
            onRetry = onOverviewRetry,
        )
    }
}

@Composable
private fun ProfileLibraryCollectionSection(
    details: ProfileDetails,
    statistic: Statistic?,
    selectedTab: ProfileMediaTab,
) {
    val listSections = remember(details, selectedTab) { details.mediaListSections(selectedTab) }
    val metricItems = libraryCollectionMetricItems(details, statistic, selectedTab, listSections.custom.size)

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        ProfileSectionHeader(
            title = stringResource(R.string.title_profile_section_collection_totals),
            modifier = Modifier.padding(horizontal = SectionHorizontalPadding),
        )
        ProfileCompactMetricStrip(
            metricItems = metricItems,
            modifier = Modifier.padding(horizontal = SectionHorizontalPadding),
        )
    }
}

@Composable
private fun ProfileLibraryOrganisationSection(
    details: ProfileDetails,
    selectedTab: ProfileMediaTab,
) {
    val listSections = remember(details, selectedTab) { details.mediaListSections(selectedTab) }
    val listOptions = remember(details, selectedTab) { details.listTypeOptions(selectedTab) }
    val settingItems = librarySettingMetricItems(details)
    val showSectionOrder = listOptions.sectionOrder.isNotEmpty()
    val showLists = listSections.primary.isNotEmpty() || listSections.custom.isNotEmpty()

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        ProfileSectionHeader(
            title = stringResource(R.string.title_profile_section_collection_organisation),
            modifier = Modifier.padding(horizontal = SectionHorizontalPadding),
        )

        ProfileContainedSectionSurface(
            modifier = Modifier.padding(horizontal = SectionHorizontalPadding),
        ) {
            ProfileMetricStrip(metricItems = settingItems)

            if (showSectionOrder || showLists) {
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.28f))
            }

            if (showSectionOrder) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    LibrarySectionLabel(label = stringResource(R.string.label_profile_library_section_order))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        listOptions.sectionOrder.forEach { section ->
                            ProfilePill(label = section.toString())
                        }
                    }
                }
            }

            if (showLists) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    if (listSections.primary.isNotEmpty()) {
                        LibrarySectionLabel(label = stringResource(R.string.label_profile_library_primary_sections))
                        MediaListChipRow(items = listSections.primary)
                    }
                    if (listSections.custom.isNotEmpty()) {
                        LibrarySectionLabel(label = stringResource(R.string.label_profile_metric_custom_lists))
                        MediaListChipRow(items = listSections.custom)
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileLibraryStatusSection(
    state: ProfileSectionState<Statistic>,
    onRetry: () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        ProfileSectionHeader(
            title = stringResource(R.string.title_profile_section_library_status),
            modifier = Modifier.padding(horizontal = SectionHorizontalPadding),
        )

        when (state) {
            ProfileSectionState.Loading ->
                ProfileCompactStateSurface(
                    message = stringResource(R.string.message_profile_stats_loading),
                    modifier = Modifier.padding(horizontal = SectionHorizontalPadding),
                )

            is ProfileSectionState.Error ->
                ProfileCompactStateSurface(
                    message = stringResource(R.string.message_profile_stats_unavailable),
                    modifier = Modifier.padding(horizontal = SectionHorizontalPadding),
                    actionLabel = stringResource(co.anitrend.core.R.string.label_text_action_retry),
                    onAction = onRetry,
                )

            ProfileSectionState.Empty ->
                ProfileCompactStateSurface(
                    message = stringResource(R.string.message_profile_stats_unavailable),
                    modifier = Modifier.padding(horizontal = SectionHorizontalPadding),
                )

            is ProfileSectionState.Content ->
                LibraryStatusContent(
                    statistic = state.data,
                )

            is ProfileSectionState.Partial ->
                LibraryStatusContent(
                    statistic = state.data,
                )
        }
    }
}

@Composable
private fun LibraryStatusContent(
    statistic: Statistic,
) {
    val statusEntries = remember(statistic) { statistic.statusEntries() }

    if (statusEntries.isEmpty()) {
        ProfileCompactStateSurface(
            message = stringResource(R.string.message_profile_stats_unavailable),
            modifier = Modifier.padding(horizontal = SectionHorizontalPadding),
        )
        return
    }

    val segments = statusEntries.toStatusSegments()

    ProfileContainedSectionSurface(
        modifier = Modifier.padding(horizontal = SectionHorizontalPadding),
    ) {
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

@Composable
private fun ProfileLibraryRecentUpdatesSection(
    state: ProfileSectionState<ProfileOverview>,
    selectedTab: ProfileMediaTab,
    onMediaSelected: (Long, MediaType?) -> Unit,
    onRetry: () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        ProfileSectionHeader(
            title = stringResource(R.string.title_profile_section_recently_updated),
            modifier = Modifier.padding(horizontal = SectionHorizontalPadding),
        )

        when (state) {
            ProfileSectionState.Loading ->
                ProfileCompactStateSurface(
                    message = stringResource(R.string.message_profile_overview_loading),
                    modifier = Modifier.padding(horizontal = SectionHorizontalPadding),
                )

            is ProfileSectionState.Error ->
                ProfileCompactStateSurface(
                    message = stringResource(R.string.message_profile_library_recent_updates_empty),
                    modifier = Modifier.padding(horizontal = SectionHorizontalPadding),
                    actionLabel = stringResource(co.anitrend.core.R.string.label_text_action_retry),
                    onAction = onRetry,
                )

            ProfileSectionState.Empty ->
                ProfileCompactStateSurface(
                    message = stringResource(R.string.message_profile_library_recent_updates_empty),
                    modifier = Modifier.padding(horizontal = SectionHorizontalPadding),
                )

            is ProfileSectionState.Content ->
                LibraryRecentUpdatesContent(
                    overview = state.data,
                    selectedTab = selectedTab,
                    onMediaSelected = onMediaSelected,
                )

            is ProfileSectionState.Partial ->
                LibraryRecentUpdatesContent(
                    overview = state.data,
                    selectedTab = selectedTab,
                    onMediaSelected = onMediaSelected,
                )
        }
    }
}

@Composable
private fun LibraryRecentUpdatesContent(
    overview: ProfileOverview,
    selectedTab: ProfileMediaTab,
    onMediaSelected: (Long, MediaType?) -> Unit,
) {
    val recentUpdates = remember(overview, selectedTab) { overview.recentLibraryActivity(selectedTab) }

    if (recentUpdates.isEmpty()) {
        ProfileCompactStateSurface(
            message = stringResource(R.string.message_profile_library_recent_updates_empty),
            modifier = Modifier.padding(horizontal = SectionHorizontalPadding),
        )
        return
    }

    Column(
        modifier = Modifier.padding(horizontal = SectionHorizontalPadding),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        recentUpdates.forEach { activity ->
            ProfilePosterActivityRow(
                item = activity,
                onMediaSelected = onMediaSelected,
            )
        }
    }
}

@Composable
private fun ProfileLibraryStateSection(
    title: String,
    message: String,
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        ProfileSectionHeader(
            title = title,
            modifier = Modifier.padding(horizontal = SectionHorizontalPadding),
        )
        ProfileCompactStateSurface(
            message = message,
            modifier = Modifier.padding(horizontal = SectionHorizontalPadding),
        )
    }
}

@Composable
private fun LibrarySectionLabel(
    label: String,
) {
    Text(
        text = label,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

@Composable
private fun libraryCollectionMetricItems(
    details: ProfileDetails,
    statistic: Statistic?,
    selectedTab: ProfileMediaTab,
    customListCount: Int,
): List<Pair<String, String>> =
    buildList {
        val listSections = details.mediaListSections(selectedTab)
        val fallbackEntryCount = listSections.primary.sumOf { it.count } + listSections.custom.sumOf { it.count }

        add(
            stringLabelAndValue(
                labelRes = R.string.label_profile_metric_entries,
                value = (statistic?.count ?: fallbackEntryCount).toHumanReadableQuantity(0),
            ),
        )
        add(
            stringLabelAndValue(
                labelRes = R.string.label_profile_metric_custom_lists,
                value = customListCount.toString(),
            ),
        )

        when (statistic) {
            is Statistic.Anime -> {
                add(
                    stringLabelAndValue(
                        labelRes = R.string.label_profile_metric_episodes_watched,
                        value = statistic.episodesWatched.toHumanReadableQuantity(0),
                    ),
                )
                add(
                    stringLabelAndValue(
                        labelRes = R.string.label_profile_metric_minutes_watched,
                        value = statistic.minutesWatched.toHumanReadableQuantity(0),
                    ),
                )
            }

            is Statistic.Manga -> {
                add(
                    stringLabelAndValue(
                        labelRes = R.string.label_profile_metric_chapters_read,
                        value = statistic.chaptersRead.toHumanReadableQuantity(0),
                    ),
                )
                add(
                    stringLabelAndValue(
                        labelRes = R.string.label_profile_metric_volumes_read,
                        value = statistic.volumesRead.toHumanReadableQuantity(0),
                    ),
                )
            }

            null -> Unit
        }
    }

@Composable
private fun librarySettingMetricItems(
    details: ProfileDetails,
): List<Pair<String, String>> =
    buildList {
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

private fun ProfileDetails.listTypeOptions(
    tab: ProfileMediaTab,
): UserMediaListTypeOptions =
    when (tab) {
        ProfileMediaTab.Anime -> listOption.animeList
        ProfileMediaTab.Manga -> listOption.mangaList
    }

@Preview(name = "Profile Library Tab")
@Composable
private fun ProfileLibraryTabPreview() {
    val previewUser = previewProfileUser()

    PreviewTheme(darkTheme = true, wrapInSurface = true) {
        ProfileLibraryTab(
            details = requireNotNull(previewUser.profileDetailsOrNull()),
            overviewState = ProfileSectionState.Content(previewProfileOverview()),
            statsState = ProfileSectionState.Content(requireNotNull(previewUser.statisticFor(ProfileMediaTab.Anime))),
            selectedTab = ProfileMediaTab.Anime,
            onTabSelected = {},
            onMediaSelected = { _, _ -> },
            onOverviewRetry = {},
            onStatsRetry = {},
        )
    }
}
