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
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import co.anitrend.android.core.extensions.toHumanReadableQuantity
import co.anitrend.domain.medialist.enums.MediaListStatus
import co.anitrend.domain.medialist.enums.ScoreFormat
import co.anitrend.domain.user.entity.attribute.MediaListInfo
import co.anitrend.domain.user.entity.attribute.option.UserProfileOption
import co.anitrend.profile.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

internal val SectionHorizontalPadding = 16.dp
internal val SectionShape = RoundedCornerShape(26.dp)

@Composable
internal fun ProfileSectionCard(
    title: String,
    subtitle: String? = null,
    trailingActionLabel: String? = null,
    onTrailingAction: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    Surface(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = SectionHorizontalPadding),
        shape = SectionShape,
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        tonalElevation = 0.dp,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.18f)),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            content = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.Top,
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                        )
                        if (!subtitle.isNullOrBlank()) {
                            Text(
                                text = subtitle,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                    if (!trailingActionLabel.isNullOrBlank() && onTrailingAction != null) {
                        TextButton(onClick = onTrailingAction) {
                            Text(text = trailingActionLabel)
                        }
                    }
                }
                content()
            },
        )
    }
}

@Composable
internal fun ProfileMetricGrid(metricItems: List<Pair<String, String>>) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        metricItems.forEach { (label, value) ->
            ProfileMetricCard(
                label = label,
                value = value,
                modifier = Modifier.widthIn(min = 132.dp, max = 180.dp),
            )
        }
    }
}

@Composable
internal fun ProfileMetricStrip(metricItems: List<Pair<String, String>>) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        metricItems.forEachIndexed { index, (label, value) ->
            if (index > 0) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.18f),
                ) {
                    Text(text = "", modifier = Modifier.padding(vertical = 0.5.dp))
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
    }
}

@Composable
private fun ProfileMetricCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.58f),
        shape = MaterialTheme.shapes.large,
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}

@Composable
internal fun MediaListChipRow(items: List<MediaListInfo>) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items.forEach { item ->
            ProfilePill(label = "${item.name} ${item.count.toHumanReadableQuantity(0)}")
        }
    }
}

@Composable
internal fun StatsChartBlock(
    title: String,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        content = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
            )
            content()
        },
    )
}

@Composable
internal fun ProfileMessageState(message: String) {
    Text(
        text = message,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

@Composable
internal fun ProfileRetryState(
    message: String = stringResource(R.string.message_profile_stats_unavailable),
    onRetry: () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        ProfileMessageState(message = message)
        OutlinedButton(onClick = onRetry) {
            Text(text = stringResource(co.anitrend.core.R.string.label_text_action_retry))
        }
    }
}

@Composable
internal fun ProfileQuietStateBanner(
    message: String,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
) {
    Surface(
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
        shape = MaterialTheme.shapes.large,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            if (!actionLabel.isNullOrBlank() && onAction != null) {
                TextButton(onClick = onAction, modifier = Modifier.align(Alignment.End)) {
                    Text(text = actionLabel)
                }
            }
        }
    }
}

@Composable
internal fun ProfilePill(
    label: String,
    containerColor: Color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.55f),
    contentColor: Color = MaterialTheme.colorScheme.onSecondaryContainer,
) {
    Surface(
        color = containerColor,
        contentColor = contentColor,
        shape = MaterialTheme.shapes.large,
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            style = MaterialTheme.typography.labelLarge,
        )
    }
}

@Composable
internal fun ProfileFactChip(
    label: String,
    value: String,
    accentColor: Color = MaterialTheme.colorScheme.primary,
) {
    Surface(
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.52f),
        shape = MaterialTheme.shapes.large,
        border = BorderStroke(1.dp, accentColor.copy(alpha = 0.12f)),
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = value,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Medium,
            )
        }
    }
}

@Composable
internal fun stringLabelAndValue(
    labelRes: Int,
    value: String,
): Pair<String, String> = stringResource(labelRes) to value

@Composable
internal fun statusSegmentColor(status: MediaListStatus?): Color =
    when (status) {
        MediaListStatus.COMPLETED -> MaterialTheme.colorScheme.primary
        MediaListStatus.PLANNING -> MaterialTheme.colorScheme.tertiary
        MediaListStatus.CURRENT -> MaterialTheme.colorScheme.secondary
        MediaListStatus.PAUSED -> MaterialTheme.colorScheme.secondaryContainer
        MediaListStatus.DROPPED -> MaterialTheme.colorScheme.error.copy(alpha = 0.82f)
        MediaListStatus.REPEATING -> MaterialTheme.colorScheme.primaryContainer
        null -> MaterialTheme.colorScheme.outline
    }

@Composable
internal fun profileAccentColor(option: UserProfileOption?): Color =
    when (option?.profileColor?.lowercase(Locale.ROOT)) {
        "blue" -> MaterialTheme.colorScheme.primary
        "purple" -> MaterialTheme.colorScheme.tertiary
        "pink" -> MaterialTheme.colorScheme.tertiaryContainer
        "orange" -> MaterialTheme.colorScheme.tertiary
        "red" -> MaterialTheme.colorScheme.error
        "green" -> MaterialTheme.colorScheme.secondary
        "grey" -> MaterialTheme.colorScheme.outline
        else -> MaterialTheme.colorScheme.primary
    }

@Composable
internal fun ScoreFormat.displayLabel(): String =
    when (this) {
        ScoreFormat.POINT_100 -> stringResource(R.string.label_profile_score_format_100_point)
        ScoreFormat.POINT_10_DECIMAL -> stringResource(R.string.label_profile_score_format_10_point_decimal)
        ScoreFormat.POINT_10 -> stringResource(R.string.label_profile_score_format_10_point)
        ScoreFormat.POINT_5 -> stringResource(R.string.label_profile_score_format_5_star)
        ScoreFormat.POINT_3 -> stringResource(R.string.label_profile_score_format_3_face)
    }

@Composable
internal fun MediaListStatus?.displayLabel(): String =
    when (this) {
        null -> stringResource(R.string.label_profile_status_unknown)
        else -> name.asReadableLabel()
    }

internal fun String.asReadableLabel(): String =
    lowercase(Locale.ROOT)
        .replace('_', ' ')
        .replaceFirstChar { it.titlecase(Locale.getDefault()) }

internal fun Float.displayScore(): String = String.format(Locale.US, "%.1f", this)

internal fun formatEpochDate(epochSecond: Long?): String? {
    if (epochSecond == null || epochSecond <= 0L) {
        return null
    }

    return SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(Date(epochSecond * 1000))
}
