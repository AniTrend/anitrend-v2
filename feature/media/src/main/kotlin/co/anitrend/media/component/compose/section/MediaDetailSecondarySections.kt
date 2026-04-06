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
import co.anitrend.arch.domain.entities.LoadState
import co.anitrend.domain.media.entity.MediaPerson
import co.anitrend.domain.media.entity.MediaStudioEntry
import co.anitrend.media.R
import co.anitrend.media.component.compose.people.previewCandidates
import co.anitrend.media.component.compose.people.selectStaffPreview
import co.anitrend.navigation.StudioRouter
import java.util.Locale

private const val PRODUCTION_STAFF_PREVIEW_COUNT = 10
private const val PRODUCTION_STUDIO_PREVIEW_COUNT = 6
private const val DETAIL_TOKEN_LIMIT = 4

private data class ProductionCredit(
    val label: String,
    val subtitle: String? = null,
    val badge: String? = null,
    val onClick: (() -> Unit)? = null,
)

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
    val staffGroups = remember(staffPreview) { groupStaffByRoleBucket(staffPreview) }
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
                            title = stringResource(group.group.titleRes),
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

