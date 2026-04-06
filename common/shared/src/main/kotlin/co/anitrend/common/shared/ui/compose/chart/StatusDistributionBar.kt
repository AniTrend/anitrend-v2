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
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import co.anitrend.android.core.extensions.rememberSystemAnimDuration
import co.anitrend.android.core.ui.AniTrendPreview
import co.anitrend.android.core.ui.theme.preview.DarkThemeProvider
import co.anitrend.android.core.ui.theme.preview.PreviewTheme

data class StatusDistributionSegment(
    val label: String,
    val value: String,
    val fraction: Float,
    val color: Color,
    val contentDescription: String? = null,
)

@Composable
fun StatusDistributionBar(
    segments: List<StatusDistributionSegment>,
    modifier: Modifier = Modifier,
    barHeight: androidx.compose.ui.unit.Dp = 18.dp,
    trackColor: Color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.26f),
) {
    if (segments.isEmpty()) {
        return
    }

    val animationDuration = rememberSystemAnimDuration()

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = trackColor,
        shape = RoundedCornerShape(999.dp),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(barHeight)
                    .clip(RoundedCornerShape(999.dp)),
            horizontalArrangement = Arrangement.spacedBy(1.dp),
        ) {
            segments.forEach { segment ->
                val animatedFraction by animateFloatAsState(
                    targetValue = segment.fraction.coerceAtLeast(0f),
                    animationSpec = tween(durationMillis = animationDuration, easing = FastOutSlowInEasing),
                    label = "status_distribution_${segment.label}",
                )

                Box(
                    modifier =
                        Modifier
                            .fillMaxHeight()
                            .weight(animatedFraction.coerceAtLeast(0.0001f))
                            .background(segment.color)
                            .semantics {
                                contentDescription = segment.contentDescription ?: "${segment.label}: ${segment.value}"
                            },
                )
            }
        }
    }
}

private val PreviewStatusSegments =
    listOf(
        StatusDistributionSegment(
            label = "Completed",
            value = "816K",
            fraction = 0.72f,
            color = Color(0xFF9AA4FF),
        ),
        StatusDistributionSegment(
            label = "Planning",
            value = "65K",
            fraction = 0.14f,
            color = Color(0xFFF5C38D),
        ),
        StatusDistributionSegment(
            label = "Current",
            value = "59K",
            fraction = 0.09f,
            color = Color(0xFF74D3AE),
        ),
        StatusDistributionSegment(
            label = "Dropped",
            value = "15K",
            fraction = 0.05f,
            color = Color(0xFFF18D89),
        ),
    )

@AniTrendPreview.Default
@Composable
private fun StatusDistributionBarPreview(
    @PreviewParameter(DarkThemeProvider::class) darkTheme: Boolean,
) {
    PreviewTheme(darkTheme = darkTheme, wrapInSurface = true) {
        FlowRow(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            StatusDistributionBar(
                segments = PreviewStatusSegments,
                modifier = Modifier.fillMaxWidth(),
            )
            PreviewStatusSegments.forEach { segment ->
                ChartLegendRow(
                    label = segment.label,
                    value = segment.value,
                    color = segment.color,
                )
            }
        }
    }
}
