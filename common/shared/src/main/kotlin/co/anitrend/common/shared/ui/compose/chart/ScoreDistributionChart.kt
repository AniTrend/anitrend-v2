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
package co.anitrend.common.shared.ui.compose.chart

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import co.anitrend.android.core.extensions.rememberSystemAnimDuration
import co.anitrend.android.core.ui.AniTrendPreview
import co.anitrend.android.core.ui.theme.preview.DarkThemeProvider
import co.anitrend.android.core.ui.theme.preview.PreviewTheme

data class ScoreDistributionBarEntry(
    val label: String,
    val value: Int,
    val contentDescription: String? = null,
)

data class ScoreDistributionAxisTick(
    val label: String,
    val value: Int,
)

@Composable
fun ScoreDistributionChart(
    entries: List<ScoreDistributionBarEntry>,
    modifier: Modifier = Modifier,
    compact: Boolean = false,
    yAxisTicks: List<ScoreDistributionAxisTick> = emptyList(),
    barColor: Color = MaterialTheme.colorScheme.primary,
    guideColor: Color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.16f),
    baselineColor: Color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.32f),
) {
    if (entries.isEmpty()) {
        return
    }

    val maxValue = entries.maxOf(ScoreDistributionBarEntry::value).coerceAtLeast(1)
    val sortedAxisTicks = yAxisTicks.sortedByDescending(ScoreDistributionAxisTick::value)
    val scaleMax = maxOf(maxValue, sortedAxisTicks.maxOfOrNull(ScoreDistributionAxisTick::value) ?: 0).coerceAtLeast(1)
    val animationDuration = rememberSystemAnimDuration()
    val chartHeight = if (compact) 120.dp else 188.dp
    val barShape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp, bottomStart = 4.dp, bottomEnd = 4.dp)
    val axisLabelWidth = if (compact) 36.dp else 44.dp
    val axisSpacing = if (compact) 6.dp else 10.dp
    val hasYAxis = sortedAxisTicks.isNotEmpty()

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(if (hasYAxis) axisSpacing else 0.dp),
            verticalAlignment = Alignment.Bottom,
        ) {
            if (hasYAxis) {
                Column(
                    modifier = Modifier.height(chartHeight).width(axisLabelWidth),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.End,
                ) {
                    sortedAxisTicks.forEach { tick ->
                        Text(
                            text = tick.label,
                            style = if (compact) MaterialTheme.typography.labelSmall else MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.End,
                            maxLines = 1,
                        )
                    }
                }
            }

            Row(
                modifier =
                    Modifier
                        .weight(1f)
                        .height(chartHeight)
                        .drawBehind {
                            val strokeWidth = 1.dp.toPx()
                            val gridStep = size.height / 4f
                            for (index in 1 until 4) {
                                drawLine(
                                    color = guideColor,
                                    start = Offset(x = 0f, y = size.height - (gridStep * index)),
                                    end = Offset(x = size.width, y = size.height - (gridStep * index)),
                                    strokeWidth = strokeWidth,
                                )
                            }
                            drawLine(
                                color = baselineColor,
                                start = Offset(x = 0f, y = size.height),
                                end = Offset(x = size.width, y = size.height),
                                strokeWidth = strokeWidth,
                            )
                        },
                horizontalArrangement = Arrangement.spacedBy(if (compact) 4.dp else 6.dp),
                verticalAlignment = Alignment.Bottom,
            ) {
                entries.forEach { entry ->
                    val targetFraction = (entry.value / scaleMax.toFloat()).coerceIn(0f, 1f)
                    val animatedFraction by animateFloatAsState(
                        targetValue = targetFraction,
                        animationSpec = tween(durationMillis = animationDuration, easing = FastOutSlowInEasing),
                        label = "score_distribution_${entry.label}",
                    )

                    Column(
                        modifier = Modifier.weight(1f).fillMaxHeight(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom,
                    ) {
                        Box(
                            modifier = Modifier.weight(1f).fillMaxWidth(),
                            contentAlignment = Alignment.BottomCenter,
                        ) {
                            Box(
                                modifier =
                                    Modifier
                                        .fillMaxWidth(if (compact) 0.72f else 0.62f)
                                        .fillMaxHeight(animatedFraction)
                                        .heightIn(min = if (entry.value > 0) 10.dp else 0.dp)
                                        .widthIn(min = 10.dp)
                                        .clip(barShape)
                                        .background(barColor)
                                        .semantics {
                                            contentDescription = entry.contentDescription ?: "${entry.label}: ${entry.value}"
                                        },
                            )
                        }
                    }
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
        ) {
            if (hasYAxis) {
                Spacer(modifier = Modifier.width(axisLabelWidth + axisSpacing))
            }

            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(if (compact) 4.dp else 6.dp),
                verticalAlignment = Alignment.Top,
            ) {
                entries.forEach { entry ->
                    Text(
                        text = entry.label,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }
    }
}

private val PreviewAxisTicks =
    listOf(
        ScoreDistributionAxisTick(label = "120", value = 120),
        ScoreDistributionAxisTick(label = "0", value = 0),
    )

private val PreviewScoreEntries =
    listOf(
        ScoreDistributionBarEntry(label = "10", value = 4),
        ScoreDistributionBarEntry(label = "20", value = 8),
        ScoreDistributionBarEntry(label = "30", value = 14),
        ScoreDistributionBarEntry(label = "40", value = 22),
        ScoreDistributionBarEntry(label = "50", value = 36),
        ScoreDistributionBarEntry(label = "60", value = 48),
        ScoreDistributionBarEntry(label = "70", value = 66),
        ScoreDistributionBarEntry(label = "80", value = 104),
        ScoreDistributionBarEntry(label = "90", value = 84),
        ScoreDistributionBarEntry(label = "100", value = 52),
    )

@AniTrendPreview.Default
@Composable
private fun ScoreDistributionChartPreview(
    @PreviewParameter(DarkThemeProvider::class) darkTheme: Boolean,
) {
    PreviewTheme(darkTheme = darkTheme, wrapInSurface = true) {
        Box(modifier = Modifier.padding(16.dp)) {
            ScoreDistributionChart(
                entries = PreviewScoreEntries,
                compact = true,
                yAxisTicks = PreviewAxisTicks,
            )
        }
    }
}
