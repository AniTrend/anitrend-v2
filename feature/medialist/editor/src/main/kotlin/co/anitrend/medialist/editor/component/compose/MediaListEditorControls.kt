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
package co.anitrend.medialist.editor.component.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import co.anitrend.common.media.ui.compose.component.IconScoreContent
import co.anitrend.domain.media.entity.attribute.score.IMediaRating
import co.anitrend.domain.medialist.enums.ScoreFormat
import co.anitrend.medialist.editor.R

@Composable
internal fun NumericStepperRow(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    onDecrement: () -> Unit,
    onIncrement: () -> Unit,
    modifier: Modifier = Modifier,
    valueSuffix: String? = null,
    supportingText: String? = null,
    keyboardType: KeyboardType = KeyboardType.Number,
    canDecrement: Boolean = true,
    canIncrement: Boolean = true,
    compact: Boolean = false,
) {
    val containerShape = RoundedCornerShape(if (compact) 20.dp else 24.dp)
    val displayShape = RoundedCornerShape(if (compact) 18.dp else 22.dp)
    val valueStyle =
        if (compact) {
            MaterialTheme.typography.headlineSmall
        } else {
            MaterialTheme.typography.displaySmall
        }.copy(textAlign = TextAlign.Center)

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(if (compact) 8.dp else 10.dp),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = label,
                style = if (compact) MaterialTheme.typography.titleSmall else MaterialTheme.typography.titleMedium,
            )
            if (!supportingText.isNullOrBlank()) {
                Text(
                    text = supportingText,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = containerShape,
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = if (compact) 0.16f else 0.14f),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
        ) {
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = if (compact) 10.dp else 12.dp, vertical = if (compact) 10.dp else 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                StepperIconButton(
                    onClick = onDecrement,
                    enabled = canDecrement,
                    compact = compact,
                    contentDescription = stringResource(R.string.description_media_list_editor_decrement_value, label),
                    icon = Icons.Filled.Remove,
                )

                Surface(
                    modifier = Modifier.weight(1f),
                    shape = displayShape,
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.32f)),
                ) {
                    Row(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = if (compact) 12.dp else 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.Center,
                        ) {
                            if (value.isBlank()) {
                                Text(
                                    text = "0",
                                    style = valueStyle,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.55f),
                                )
                            }
                            BasicTextField(
                                value = value,
                                onValueChange = onValueChange,
                                singleLine = true,
                                textStyle = valueStyle.copy(color = MaterialTheme.colorScheme.onSurface),
                                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .widthIn(min = 56.dp),
                            )
                        }

                        valueSuffix?.let {
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "/ $it",
                                style = if (compact) MaterialTheme.typography.titleSmall else MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }

                StepperIconButton(
                    onClick = onIncrement,
                    enabled = canIncrement,
                    compact = compact,
                    contentDescription = stringResource(R.string.description_media_list_editor_increment_value, label),
                    icon = Icons.Filled.Add,
                )
            }
        }
    }
}

@Composable
internal fun FormatAwareScoreControl(
    scoreFormat: ScoreFormat,
    scoreText: String,
    maxScore: String,
    onScoreChange: (String) -> Unit,
    onScoreIncrement: () -> Unit,
    onScoreDecrement: () -> Unit,
    onClearScore: () -> Unit,
    onDiscreteScoreSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = stringResource(R.string.label_media_list_editor_your_score),
                style = MaterialTheme.typography.titleMedium,
            )
            if (scoreText.isNotBlank()) {
                TextButton(onClick = onClearScore) {
                    Text(stringResource(R.string.action_media_list_editor_clear_score))
                }
            }
        }

        when (scoreFormat) {
            ScoreFormat.POINT_10,
            ScoreFormat.POINT_100,
            ScoreFormat.POINT_10_DECIMAL,
            ->
                NumericStepperRow(
                    label = scoreFormat.alias.toString(),
                    value = scoreText,
                    onValueChange = onScoreChange,
                    onDecrement = onScoreDecrement,
                    onIncrement = onScoreIncrement,
                    valueSuffix = maxScore,
                    keyboardType = if (scoreFormat == ScoreFormat.POINT_10_DECIMAL) KeyboardType.Decimal else KeyboardType.Number,
                    canDecrement = scoreText.isNotBlank(),
                    canIncrement = true,
                )

            ScoreFormat.POINT_3 ->
                DiscreteScoreSurface {
                    MoodScoreSelector(
                        selectedValue = scoreText.toIntOrNull() ?: 0,
                        onValueSelected = onDiscreteScoreSelected,
                    )
                }

            ScoreFormat.POINT_5 ->
                DiscreteScoreSurface {
                    StarScoreSelector(
                        selectedValue = scoreText.toIntOrNull() ?: 0,
                        onValueSelected = onDiscreteScoreSelected,
                    )
                }
        }
    }
}

@Composable
private fun DiscreteScoreSurface(content: @Composable ColumnScope.() -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.14f),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            content = content,
        )
    }
}

@Composable
private fun MoodScoreSelector(
    selectedValue: Int,
    onValueSelected: (Int) -> Unit,
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        FilterChip(
            selected = selectedValue == 0,
            onClick = { onValueSelected(0) },
            label = { Text(stringResource(R.string.label_media_list_editor_score_none)) },
        )

        val options =
            listOf(
                MoodOption(1, R.string.label_media_list_editor_score_low, IMediaRating.Mood.Sentiment.BAD),
                MoodOption(2, R.string.label_media_list_editor_score_okay, IMediaRating.Mood.Sentiment.NEUTRAL),
                MoodOption(3, R.string.label_media_list_editor_score_great, IMediaRating.Mood.Sentiment.GOOD),
            )
        options.forEach { option ->
            FilterChip(
                selected = selectedValue == option.value,
                onClick = { onValueSelected(option.value) },
                label = { Text(stringResource(option.labelRes)) },
                leadingIcon = {
                    IconScoreContent(
                        rating = IMediaRating.Mood(option.sentiment),
                        iconTint =
                            if (selectedValue == option.value) {
                                MaterialTheme.colorScheme.onSecondaryContainer
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            },
                        modifier = Modifier.size(18.dp),
                    )
                },
            )
        }
    }
}

@Composable
private fun StarScoreSelector(
    selectedValue: Int,
    onValueSelected: (Int) -> Unit,
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        FilterChip(
            selected = selectedValue == 0,
            onClick = { onValueSelected(0) },
            label = { Text(stringResource(R.string.label_media_list_editor_score_none)) },
        )

        (1..5).forEach { value ->
            FilterChip(
                selected = selectedValue == value,
                onClick = { onValueSelected(value) },
                label = { Text(value.toString()) },
                leadingIcon = {
                    Icon(
                        imageVector = if (selectedValue >= value) Icons.Filled.Star else Icons.Outlined.StarBorder,
                        contentDescription = null,
                    )
                },
            )
        }
    }
}

@Composable
private fun StepperIconButton(
    onClick: () -> Unit,
    enabled: Boolean,
    contentDescription: String,
    icon: ImageVector,
    compact: Boolean,
) {
    Surface(
        modifier = Modifier.size(if (compact) 44.dp else 48.dp),
        shape = CircleShape,
        color =
            if (enabled) {
                MaterialTheme.colorScheme.secondaryContainer
            } else {
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.72f)
            },
        tonalElevation = 0.dp,
    ) {
        androidx.compose.runtime.CompositionLocalProvider(
            LocalContentColor provides
                if (enabled) {
                    MaterialTheme.colorScheme.onSecondaryContainer
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                },
        ) {
            IconButton(
                onClick = onClick,
                enabled = enabled,
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = contentDescription,
                )
            }
        }
    }
}

private data class MoodOption(
    val value: Int,
    val labelRes: Int,
    val sentiment: IMediaRating.Mood.Sentiment,
)
