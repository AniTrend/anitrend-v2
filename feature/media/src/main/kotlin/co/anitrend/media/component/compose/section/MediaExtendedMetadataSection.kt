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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import co.anitrend.android.core.helpers.date.AniTrendDateHelper
import co.anitrend.domain.common.entity.shared.FuzzyDate
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.media.entity.attribute.theme.MediaTheme
import co.anitrend.domain.media.enums.MediaSeason
import co.anitrend.media.R
import java.util.Locale
import org.koin.compose.koinInject
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import co.anitrend.common.media.ui.R as MediaUiR

internal data class MetadataEntry(
    val label: String,
    val value: String,
)

private data class MetadataLinkEntry(
    val label: String,
    val url: String,
)

private enum class MetadataDatePrecision {
    NONE,
    YEAR,
    MONTH_YEAR,
    FULL,
}

private fun FuzzyDate.precision(): MetadataDatePrecision =
    when {
        isDateNotSet() -> MetadataDatePrecision.NONE
        year > 0 && month > 0 && day > 0 -> MetadataDatePrecision.FULL
        year > 0 && month > 0 -> MetadataDatePrecision.MONTH_YEAR
        year > 0 -> MetadataDatePrecision.YEAR
        else -> MetadataDatePrecision.NONE
    }

private fun FuzzyDate.localizedMetadataDate(dateHelper: AniTrendDateHelper): String? =
    when (precision()) {
        MetadataDatePrecision.FULL -> dateHelper.convertToTextDate(this)?.toString()
        MetadataDatePrecision.MONTH_YEAR ->
            DateTimeFormatter
                .ofPattern("MMM yyyy", Locale.getDefault())
                .format(LocalDate.of(year, month, 1))
        MetadataDatePrecision.YEAR -> year.takeIf { it > 0 }?.toString()
        MetadataDatePrecision.NONE -> null
    }

@Composable
private fun MediaSeason.localizedSeasonLabel(): String =
    when (this) {
        MediaSeason.FALL -> stringResource(R.string.label_media_release_timeline_season_fall)
        MediaSeason.SPRING -> stringResource(R.string.label_media_release_timeline_season_spring)
        MediaSeason.SUMMER -> stringResource(R.string.label_media_release_timeline_season_summer)
        MediaSeason.WINTER -> stringResource(R.string.label_media_release_timeline_season_winter)
    }

internal fun buildReleaseMetadataEntries(
    media: Media.Extended,
    dateHelper: AniTrendDateHelper,
    premieredLabel: String,
    startedLabel: String,
    endedLabel: String,
    seasonLabel: String?,
): List<MetadataEntry> {
    val premieredValue =
        seasonLabel?.let { label ->
            media.startDate.year
                .takeIf { it > 0 }
                ?.let { year -> "$label $year" }
                ?: label
        }
    val startedValue = media.startDate.localizedMetadataDate(dateHelper)
    val endedValue = media.endDate.localizedMetadataDate(dateHelper)
    val duplicateStartedValue =
        premieredValue != null &&
            (startedValue == premieredValue || media.startDate.precision() == MetadataDatePrecision.YEAR)

    return buildList {
        premieredValue?.let { add(MetadataEntry(premieredLabel, it)) }
        startedValue
            ?.takeUnless { duplicateStartedValue }
            ?.let { add(MetadataEntry(startedLabel, it)) }
        endedValue?.let { add(MetadataEntry(endedLabel, it)) }
    }
}

@Composable
private fun MetadataRow(
    entry: MetadataEntry,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier.fillMaxWidth(),
    ) {
        Text(
            text = entry.label,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.weight(0.36f),
        )
        Text(
            text = entry.value,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.weight(0.64f),
        )
    }
}

@Composable
private fun MetadataChip(
    label: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.72f),
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        shape = MaterialTheme.shapes.medium,
        modifier = modifier,
    ) {
        Text(
            text = label,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
        )
    }
}

@Composable
private fun MetadataGroup(
    title: String,
    values: List<String>,
    collapsedCount: Int = Int.MAX_VALUE,
    modifier: Modifier = Modifier,
) {
    var isExpanded by rememberSaveable(title, values.size) {
        mutableStateOf(false)
    }
    val canExpand = values.size > collapsedCount
    val visibleValues = if (canExpand && !isExpanded) values.take(collapsedCount) else values

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.labelLarge,
            )
            Spacer(modifier = Modifier.weight(1f))
            if (canExpand) {
                TextButton(
                    onClick = { isExpanded = !isExpanded },
                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 0.dp),
                ) {
                    Text(
                        text =
                            stringResource(
                                if (isExpanded) {
                                    R.string.action_media_extended_details_show_less
                                } else {
                                    R.string.action_media_extended_details_show_more
                                },
                            ),
                        style = MaterialTheme.typography.labelMedium,
                    )
                }
            }
        }
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            visibleValues.forEach { value ->
                MetadataChip(label = value)
            }
        }
    }
}

@Composable
private fun MetadataLinkGroup(
    title: String,
    links: List<MetadataLinkEntry>,
    onLinkClick: (String) -> Unit,
    collapsedCount: Int = Int.MAX_VALUE,
    modifier: Modifier = Modifier,
) {
    var isExpanded by rememberSaveable(title, links.size) {
        mutableStateOf(false)
    }
    val canExpand = links.size > collapsedCount
    val visibleLinks = if (canExpand && !isExpanded) links.take(collapsedCount) else links

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.labelLarge,
            )
            Spacer(modifier = Modifier.weight(1f))
            if (canExpand) {
                TextButton(
                    onClick = { isExpanded = !isExpanded },
                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 0.dp),
                ) {
                    Text(
                        text =
                            stringResource(
                                if (isExpanded) {
                                    R.string.action_media_extended_details_show_less
                                } else {
                                    R.string.action_media_extended_details_show_more
                                },
                            ),
                        style = MaterialTheme.typography.labelMedium,
                    )
                }
            }
        }
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            visibleLinks.forEach { link ->
                TextButton(
                    onClick = { onLinkClick(link.url) },
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp),
                ) {
                    Text(
                        text = link.label,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.labelMedium,
                    )
                }
            }
        }
    }
}

@Composable
fun MediaExtendedMetadataSection(
    media: Media.Extended,
    modifier: Modifier = Modifier,
    themes: List<MediaTheme> = emptyList(),
    onExternalLinkClick: (String) -> Unit = {},
) {
    val dateHelper: AniTrendDateHelper = koinInject()
    val detailRows =
        buildList {
            media.ageRating
                ?.takeIf(String::isNotBlank)
                ?.let { add(MetadataEntry(stringResource(R.string.label_media_extended_details_age_rating), it)) }
            media.source
                ?.alias
                ?.toString()
                ?.takeIf(String::isNotBlank)
                ?.let { add(MetadataEntry(stringResource(R.string.label_media_extended_details_source), it)) }
            media.twitterTag
                ?.toString()
                ?.trim()
                ?.takeIf(String::isNotBlank)
                ?.let {
                    val hashtag = if (it.startsWith("#")) it else "#$it"
                    add(MetadataEntry(stringResource(R.string.label_media_extended_details_twitter_tag), hashtag))
                }
            addAll(
                buildReleaseMetadataEntries(
                    media = media,
                    dateHelper = dateHelper,
                    premieredLabel = stringResource(MediaUiR.string.label_media_status_premiered),
                    startedLabel = stringResource(MediaUiR.string.label_media_status_started),
                    endedLabel = stringResource(R.string.label_media_extended_details_ended),
                    seasonLabel = media.season?.localizedSeasonLabel(),
                ),
            )
        }

    val synonyms =
        media.synonyms
            .map(CharSequence::toString)
            .map(String::trim)
            .filter(String::isNotBlank)
            .distinct()

    val externalLinks =
        buildList {
            media.siteUrl.aniList?.takeIf(String::isNotBlank)?.let {
                add(MetadataLinkEntry(label = "AniList", url = it))
            }
            media.siteUrl.myAnimeList?.takeIf(String::isNotBlank)?.let {
                add(MetadataLinkEntry(label = "MyAnimeList", url = it))
            }
            media.externalLinks.forEach { link ->
                val url = link.url.toString().trim()
                if (link.isDisabled == true || url.isBlank()) {
                    return@forEach
                }

                add(
                    MetadataLinkEntry(
                        label = link.site.toString(),
                        url = url,
                    ),
                )
            }
        }.distinctBy { it.label to it.url }

    if (detailRows.isEmpty() && synonyms.isEmpty() && themes.isEmpty() && externalLinks.isEmpty()) {
        return
    }

    MediaHubSection(
        title = stringResource(R.string.label_media_extended_details_section_title),
        subtitle = stringResource(R.string.subtitle_media_extended_details_section),
        modifier = modifier,
    ) {
        if (detailRows.isNotEmpty()) {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                detailRows.forEach { entry ->
                    MetadataRow(entry = entry)
                }
            }
        }

        if (detailRows.isNotEmpty() && (synonyms.isNotEmpty() || themes.isNotEmpty() || externalLinks.isNotEmpty())) {
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f))
        }

        if (synonyms.isNotEmpty()) {
            MetadataGroup(
                title = stringResource(R.string.label_media_extended_details_synonyms),
                values = synonyms,
                collapsedCount = 2,
            )
        }

        if (themes.isNotEmpty()) {
            MediaThemePreviewBlock(
                themes = themes,
                title = stringResource(R.string.label_media_extended_details_themes),
            )
        }

        if (externalLinks.isNotEmpty()) {
            MetadataLinkGroup(
                title = stringResource(R.string.label_media_extended_details_links),
                links = externalLinks,
                collapsedCount = 4,
                onLinkClick = onExternalLinkClick,
            )
        }
    }
}
