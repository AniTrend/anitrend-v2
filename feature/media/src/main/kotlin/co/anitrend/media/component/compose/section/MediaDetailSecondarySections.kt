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
package co.anitrend.media.component.compose.section

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.paging.PagedList
import co.anitrend.android.core.extensions.toHumanReadableQuantity
import co.anitrend.android.core.helpers.date.AniTrendDateHelper
import co.anitrend.arch.domain.entities.LoadState
import co.anitrend.domain.common.entity.shared.FuzzyDate
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.media.entity.MediaPerson
import co.anitrend.domain.media.entity.MediaStats
import co.anitrend.domain.media.entity.MediaStudioEntry
import co.anitrend.domain.media.enums.MediaSeason
import co.anitrend.domain.media.enums.MediaStatus
import co.anitrend.domain.media.enums.MediaType
import co.anitrend.media.R
import co.anitrend.media.component.compose.people.previewCandidates
import co.anitrend.media.component.compose.people.selectStaffPreview
import co.anitrend.navigation.StudioRouter
import java.util.Locale
import org.koin.compose.koinInject
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import co.anitrend.common.media.ui.R as MediaUiR

private const val PRODUCTION_STAFF_PREVIEW_COUNT = 10
private const val PRODUCTION_STUDIO_PREVIEW_COUNT = 6
private const val DETAIL_TOKEN_LIMIT = 4

private enum class DatePrecision {
    NONE,
    YEAR,
    MONTH_YEAR,
    FULL,
}

private enum class ProductionGroupType(
    @param:StringRes val titleRes: Int,
) {
    ORIGINAL_CREATOR(R.string.label_media_production_original_creator),
    DIRECTOR(R.string.label_media_production_directors),
    WRITER(R.string.label_media_production_writers),
    PRODUCER(R.string.label_media_production_producers),
    OTHER(R.string.label_media_production_additional_credits),
}

private data class ProductionStaffGroup(
    val type: ProductionGroupType,
    val staff: List<MediaPerson.Staff>,
)

private data class ProductionCredit(
    val label: String,
    val subtitle: String? = null,
    val badge: String? = null,
    val onClick: (() -> Unit)? = null,
)

private data class ReleaseTimelineDetail(
    val label: String,
    val value: String,
)

private data class MediaMetric(
    val label: String,
    val value: String,
)

private fun String?.normalizedRole(): String =
    this
        .orEmpty()
        .lowercase(Locale.getDefault())
        .replace(Regex("[^a-z0-9]+"), " ")
        .trim()

private fun MediaPerson.Staff.productionGroup(): ProductionGroupType {
    val normalized = role.normalizedRole()

    return when {
        normalized.contains("original creator") ||
            normalized.contains("original story") ||
            normalized.contains("original work") ||
            normalized.contains("creator") ||
            normalized.contains("manga") ||
            normalized.contains("light novel") ||
            normalized.contains("web novel") ||
            normalized.contains("novel") ||
            normalized.contains("comic") -> {
            ProductionGroupType.ORIGINAL_CREATOR
        }

        normalized.contains("director") -> ProductionGroupType.DIRECTOR
        normalized.contains("writer") ||
            normalized.contains("screenplay") ||
            normalized.contains("script") ||
            normalized.contains("composition") ||
            normalized.contains("story") -> {
            ProductionGroupType.WRITER
        }

        normalized.contains("producer") ||
            normalized.contains("animation production") -> {
            ProductionGroupType.PRODUCER
        }

        else -> ProductionGroupType.OTHER
    }
}

private fun groupProductionStaff(staff: List<MediaPerson.Staff>): List<ProductionStaffGroup> {
    if (staff.isEmpty()) {
        return emptyList()
    }

    val grouped = linkedMapOf<ProductionGroupType, MutableList<MediaPerson.Staff>>()

    staff.forEach { item ->
        grouped.getOrPut(item.productionGroup()) { mutableListOf() }.add(item)
    }

    return buildList {
        listOf(
            ProductionGroupType.ORIGINAL_CREATOR,
            ProductionGroupType.DIRECTOR,
            ProductionGroupType.WRITER,
            ProductionGroupType.PRODUCER,
            ProductionGroupType.OTHER,
        ).forEach { groupType ->
            val items = grouped[groupType].orEmpty()
            if (items.isNotEmpty()) {
                add(
                    ProductionStaffGroup(
                        type = groupType,
                        staff = items,
                    ),
                )
            }
        }
    }
}

private fun FuzzyDate.precision(): DatePrecision =
    when {
        isDateNotSet() -> DatePrecision.NONE
        year > 0 && month > 0 && day > 0 -> DatePrecision.FULL
        year > 0 && month > 0 -> DatePrecision.MONTH_YEAR
        year > 0 -> DatePrecision.YEAR
        else -> DatePrecision.NONE
    }

private fun FuzzyDate.localizedTimelineDate(dateHelper: AniTrendDateHelper): String? =
    when (precision()) {
        DatePrecision.FULL -> dateHelper.convertToTextDate(this)?.toString()
        DatePrecision.MONTH_YEAR ->
            DateTimeFormatter.ofPattern("MMM yyyy", Locale.getDefault())
                .format(LocalDate.of(year, month, 1))
        DatePrecision.YEAR -> year.takeIf { it > 0 }?.toString()
        DatePrecision.NONE -> null
    }

@Composable
private fun MediaSeason.localizedSeasonLabel(): String =
    when (this) {
        MediaSeason.FALL -> stringResource(R.string.label_media_release_timeline_season_fall)
        MediaSeason.SPRING -> stringResource(R.string.label_media_release_timeline_season_spring)
        MediaSeason.SUMMER -> stringResource(R.string.label_media_release_timeline_season_summer)
        MediaSeason.WINTER -> stringResource(R.string.label_media_release_timeline_season_winter)
    }

@Composable
private fun MediaPerson.Staff.displayName(): String =
    listOf(
        name?.userPreferred,
        name?.full,
        name?.first,
        name?.native,
    ).mapNotNull { value ->
        value?.toString()?.trim()?.takeIf(String::isNotBlank)
    }.firstOrNull()
        ?: stringResource(R.string.label_media_people_staff_name_unknown)

@Composable
private fun MediaStudioEntry.displayName(): String = studio.name

@Composable
private fun MediaCompactToken(
    label: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    badge: String? = null,
    onClick: (() -> Unit)? = null,
    containerColor: Color = MaterialTheme.colorScheme.surface.copy(alpha = 0.72f),
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    borderColor: Color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f),
) {
    Surface(
        modifier =
            modifier
                .widthIn(min = 112.dp, max = 220.dp)
                .then(
                    if (onClick != null) {
                        Modifier.clickable(onClick = onClick)
                    } else {
                        Modifier
                    },
                ),
        color = containerColor,
        contentColor = contentColor,
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.dp, borderColor),
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Medium,
                )
                badge?.let {
                    MediaTokenBadge(label = it)
                }
            }
            if (!subtitle.isNullOrBlank()) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Composable
private fun MediaTokenBadge(
    label: String,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.48f),
    contentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
) {
    Surface(
        color = containerColor,
        contentColor = contentColor,
        shape = RoundedCornerShape(999.dp),
        modifier = modifier,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
        )
    }
}

@Composable
private fun MediaMetricCard(
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
private fun SectionRetryState(
    title: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        MediaHubSectionErrorState(title = title)
        OutlinedButton(
            onClick = onRetry,
            shape = RoundedCornerShape(20.dp),
        ) {
            Text(text = stringResource(co.anitrend.core.R.string.label_text_action_retry))
        }
    }
}

@Composable
internal fun MediaProductionSection(
    staff: PagedList<MediaPerson.Staff>?,
    staffLoadState: LoadState?,
    studios: List<MediaStudioEntry>?,
    studiosLoadState: LoadState?,
    onStaffClick: () -> Unit,
    onStudioClick: (StudioRouter.StudioParam) -> Unit,
    onRetryStaff: () -> Unit,
    onRetryStudios: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val staffPreview =
        remember(staff) {
            staff
                ?.previewCandidates(maxCount = PRODUCTION_STAFF_PREVIEW_COUNT)
                .orEmpty()
                .let { selectStaffPreview(it, maxCount = PRODUCTION_STAFF_PREVIEW_COUNT) }
        }
    val staffGroups = remember(staffPreview) { groupProductionStaff(staffPreview) }
    val studioCredits =
        remember(studios) {
            studios
                .orEmpty()
                .sortedWith(
                    compareByDescending<MediaStudioEntry> { it.isMain }
                        .thenBy { it.studio.name.lowercase(Locale.getDefault()) },
                ).take(PRODUCTION_STUDIO_PREVIEW_COUNT)
        }
    val hasStudios = studioCredits.isNotEmpty()
    val hasStaff = staffGroups.isNotEmpty()
    val showStudiosLoading = !hasStudios && (studiosLoadState == null || studiosLoadState is LoadState.Loading)
    val showStudiosError = !hasStudios && studiosLoadState is LoadState.Error
    val showStaffLoading = !hasStaff && (staffLoadState == null || staffLoadState is LoadState.Loading)
    val showStaffError = !hasStaff && staffLoadState is LoadState.Error

    MediaHubSection(
        title = stringResource(R.string.title_media_production_section),
        subtitle = stringResource(R.string.subtitle_media_production_section),
        modifier = modifier,
    ) {
        when {
            hasStudios -> {
                ProductionGroupBlock(
                    title = stringResource(R.string.label_media_production_studios_heading),
                    credits =
                        studioCredits.map { entry ->
                            ProductionCredit(
                                label = entry.displayName(),
                                badge =
                                    if (entry.isMain) {
                                        stringResource(R.string.label_media_production_studio_main_badge)
                                    } else {
                                        null
                                    },
                                onClick = {
                                    onStudioClick(
                                        StudioRouter.StudioParam(
                                            id = entry.studio.id,
                                            name = entry.studio.name,
                                        ),
                                    )
                                },
                            )
                        },
                )
            }

            showStudiosLoading -> {
                MediaHubSectionLoadingState(
                    title = stringResource(R.string.label_media_production_studios_loading),
                    message = stringResource(R.string.message_media_production_studios_loading),
                )
            }

            showStudiosError -> {
                SectionRetryState(
                    title = stringResource(R.string.label_media_production_studios_error_title),
                    onRetry = onRetryStudios,
                )
            }
        }

        if ((hasStudios || showStudiosLoading || showStudiosError) && (hasStaff || showStaffLoading || showStaffError)) {
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f))
        }

        when {
            hasStaff -> {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    staffGroups.forEach { group ->
                        ProductionGroupBlock(
                            title = stringResource(group.type.titleRes),
                            credits =
                                group.staff.map { staffItem ->
                                    ProductionCredit(
                                        label = staffItem.displayName(),
                                        subtitle = staffItem.role?.trim()?.takeIf(String::isNotBlank),
                                        onClick = onStaffClick,
                                    )
                                },
                        )
                    }
                }
            }

            showStaffLoading -> {
                MediaHubSectionLoadingState(
                    title = stringResource(R.string.label_media_production_staff_loading),
                    message = stringResource(R.string.message_media_production_staff_loading),
                )
            }

            showStaffError -> {
                SectionRetryState(
                    title = stringResource(R.string.label_media_production_staff_error_title),
                    onRetry = onRetryStaff,
                )
            }
        }

        if (!hasStudios && !hasStaff && !showStudiosLoading && !showStudiosError && !showStaffLoading && !showStaffError) {
            MediaHubSectionEmptyState(
                title = stringResource(R.string.label_media_production_empty_title),
                message = stringResource(R.string.message_media_production_empty),
            )
        }
    }
}

@Composable
private fun ProductionGroupBlock(
    title: String,
    credits: List<ProductionCredit>,
    modifier: Modifier = Modifier,
) {
    val previewCredits =
        remember(credits) {
            credits.take(DETAIL_TOKEN_LIMIT)
        }
    val remaining = credits.size - previewCredits.size

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.SemiBold,
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            previewCredits.forEach { credit ->
                MediaCompactToken(
                    label = credit.label,
                    subtitle = credit.subtitle,
                    badge = credit.badge,
                    onClick = credit.onClick,
                )
            }
            if (remaining > 0) {
                MediaCompactToken(
                    label = stringResource(R.string.label_media_production_more_count, remaining),
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.34f),
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    borderColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.28f),
                )
            }
        }
    }
}

@Composable
internal fun MediaReleaseTimelineSection(
    media: Media.Extended,
    modifier: Modifier = Modifier,
) {
    val dateHelper: AniTrendDateHelper = koinInject()
    val seasonLabel = media.season?.localizedSeasonLabel()
    val seasonYearLabel =
        seasonLabel?.let { label ->
            media.startDate.year.takeIf { it > 0 }?.let { year -> "$label $year" }
                ?: label
        }
    val startLabel = media.startDate.localizedTimelineDate(dateHelper)
    val endLabel = media.endDate.localizedTimelineDate(dateHelper)
    val startHeadlineLabel =
        when {
            media.startDate.precision() == DatePrecision.YEAR && seasonYearLabel != null -> seasonYearLabel
            startLabel != null -> startLabel
            else -> seasonYearLabel
        }
    val usesSeasonOnlyHeadline = startHeadlineLabel == seasonYearLabel && seasonYearLabel != null
    val headline =
        when (media.status) {
            MediaStatus.NOT_YET_RELEASED ->
                when {
                    startHeadlineLabel != null -> stringResource(R.string.label_media_release_timeline_starts, startHeadlineLabel)
                    seasonYearLabel != null -> stringResource(R.string.label_media_release_timeline_starts, seasonYearLabel)
                    else -> stringResource(R.string.label_media_release_timeline_upcoming)
                }

            MediaStatus.RELEASING ->
                when {
                    startHeadlineLabel != null -> stringResource(R.string.label_media_release_timeline_started, startHeadlineLabel)
                    seasonYearLabel != null -> stringResource(R.string.label_media_release_timeline_started, seasonYearLabel)
                    else -> stringResource(R.string.label_media_release_timeline_releasing)
                }

            MediaStatus.FINISHED ->
                when {
                    startHeadlineLabel != null && endLabel != null -> {
                        val verb =
                            if (media.category.type == MediaType.ANIME) {
                                R.string.label_media_release_timeline_aired_range
                            } else {
                                R.string.label_media_release_timeline_published_range
                            }
                        stringResource(verb, startHeadlineLabel, endLabel)
                    }

                    startHeadlineLabel != null ->
                        stringResource(
                            if (media.category.type == MediaType.ANIME) {
                                R.string.label_media_release_timeline_aired
                            } else {
                                R.string.label_media_release_timeline_published
                            },
                            startHeadlineLabel,
                        )

                    endLabel != null -> stringResource(R.string.label_media_release_timeline_ended, endLabel)
                    else -> stringResource(R.string.label_media_release_timeline_finished)
                }

            MediaStatus.CANCELLED ->
                when {
                    startHeadlineLabel != null -> stringResource(R.string.label_media_release_timeline_cancelled_after, startHeadlineLabel)
                    seasonYearLabel != null -> stringResource(R.string.label_media_release_timeline_cancelled_after, seasonYearLabel)
                    else -> stringResource(R.string.label_media_release_timeline_cancelled)
                }

            MediaStatus.HIATUS ->
                when {
                    startHeadlineLabel != null -> stringResource(R.string.label_media_release_timeline_hiatus_since, startHeadlineLabel)
                    seasonYearLabel != null -> stringResource(R.string.label_media_release_timeline_hiatus_since, seasonYearLabel)
                    else -> stringResource(R.string.label_media_release_timeline_hiatus)
                }

            null ->
                when {
                    startHeadlineLabel != null -> stringResource(R.string.label_media_release_timeline_started, startHeadlineLabel)
                    seasonYearLabel != null -> stringResource(R.string.label_media_release_timeline_starts, seasonYearLabel)
                    else -> null
                }
        }

    val timelineDetails =
        buildList {
            if (seasonYearLabel != null && !usesSeasonOnlyHeadline) {
                add(
                    ReleaseTimelineDetail(
                        label = stringResource(R.string.label_media_release_timeline_season),
                        value = seasonYearLabel,
                    ),
                )
            }

            if (endLabel != null && media.status != MediaStatus.FINISHED) {
                add(
                    ReleaseTimelineDetail(
                        label = stringResource(R.string.label_media_release_timeline_end),
                        value = endLabel,
                    ),
                )
            }
        }

    MediaHubSection(
        title = stringResource(R.string.title_media_release_timeline_section),
        subtitle = stringResource(R.string.subtitle_media_release_timeline_section),
        modifier = modifier,
    ) {
        when {
            headline != null -> {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = headline,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                    )

                    if (timelineDetails.isNotEmpty()) {
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            timelineDetails.forEach { detail ->
                                MediaCompactToken(
                                    label = detail.label,
                                    subtitle = detail.value,
                                )
                            }
                        }
                    }
                }
            }

            else -> {
                MediaHubSectionEmptyState(
                    title = stringResource(R.string.label_media_release_timeline_empty_title),
                    message = stringResource(R.string.message_media_release_timeline_empty),
                )
            }
        }
    }
}

@Composable
internal fun MediaStatsSection(
    media: Media.Extended,
    stats: MediaStats?,
    loadState: LoadState?,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val summaryMetrics =
        listOfNotNull(
            media.favourites.takeIf { it > 0 }?.let {
                MediaMetric(
                    label = stringResource(R.string.label_media_stats_favourites),
                    value = it.toHumanReadableQuantity(0),
                )
            },
            media.score.popularity?.takeIf { it > 0 }?.let {
                MediaMetric(
                    label = stringResource(R.string.label_media_stats_popularity),
                    value = it.toHumanReadableQuantity(0),
                )
            },
            media.score.trending?.takeIf { it > 0 }?.let {
                MediaMetric(
                    label = stringResource(R.string.label_media_stats_trending),
                    value = it.toHumanReadableQuantity(0),
                )
            },
        )

    val scoreDistribution =
        remember(stats) {
            stats
                ?.scoreDistribution
                .orEmpty()
                .sortedWith(
                    compareByDescending<MediaStats.ScoreDistribution> { it.amount }
                        .thenByDescending { it.score },
                ).take(DETAIL_TOKEN_LIMIT)
        }
    val statusDistribution =
        remember(stats) {
            stats
                ?.statusDistribution
                .orEmpty()
                .sortedWith(
                    compareByDescending<MediaStats.StatusDistribution> { it.amount }
                        .thenBy { it.status?.ordinal ?: Int.MAX_VALUE },
                ).take(DETAIL_TOKEN_LIMIT)
        }
    val hasSummaryMetrics = summaryMetrics.isNotEmpty()
    val hasDistributionData = scoreDistribution.isNotEmpty() || statusDistribution.isNotEmpty()
    val isLoading = (loadState == null || loadState is LoadState.Loading) && !hasSummaryMetrics && !hasDistributionData
    val isError = loadState is LoadState.Error && !hasSummaryMetrics && !hasDistributionData
    val hasNoContent = !hasSummaryMetrics && !hasDistributionData && !isLoading && !isError

    MediaHubSection(
        title = stringResource(R.string.title_media_stats_section),
        subtitle = stringResource(R.string.subtitle_media_stats_section),
        modifier = modifier,
    ) {
        if (hasSummaryMetrics) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                summaryMetrics.forEach { metric ->
                    MediaMetricCard(
                        label = metric.label,
                        value = metric.value,
                        modifier = Modifier.widthIn(min = 112.dp, max = 160.dp),
                    )
                }
            }
        }

        if (hasSummaryMetrics && hasDistributionData) {
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f))
        }

        when {
            isLoading -> {
                MediaHubSectionLoadingState(
                    title = stringResource(R.string.label_media_stats_loading),
                    message = stringResource(R.string.message_media_stats_loading),
                )
            }

            isError -> {
                SectionRetryState(
                    title = stringResource(R.string.label_media_stats_error_title),
                    onRetry = onRetry,
                )
            }
        }

        if (hasDistributionData) {
            if (scoreDistribution.isNotEmpty()) {
                DistributionBlock(
                    title = stringResource(R.string.label_media_stats_score_distribution),
                    entries =
                        scoreDistribution.map { distribution ->
                            MediaMetric(
                                label = distribution.score.toString(),
                                value = distribution.amount.toHumanReadableQuantity(0),
                            )
                        },
                )
            }

            if (scoreDistribution.isNotEmpty() && statusDistribution.isNotEmpty()) {
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f))
            }

            if (statusDistribution.isNotEmpty()) {
                DistributionBlock(
                    title = stringResource(R.string.label_media_stats_status_distribution),
                    entries =
                        statusDistribution.map { distribution ->
                            MediaMetric(
                                label =
                                    distribution.status?.alias?.toString()
                                        ?: stringResource(MediaUiR.string.label_media_status_unknown_value),
                                value = distribution.amount.toHumanReadableQuantity(0),
                            )
                        },
                )
            }
        }

        if (hasNoContent) {
            MediaHubSectionEmptyState(
                title = stringResource(R.string.label_media_stats_empty_title),
                message = stringResource(R.string.message_media_stats_empty),
            )
        }
    }
}

@Composable
private fun DistributionBlock(
    title: String,
    entries: List<MediaMetric>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.SemiBold,
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            entries.forEach { entry ->
                MediaCompactToken(
                    label = entry.label,
                    subtitle = entry.value,
                )
            }
        }
    }
}
