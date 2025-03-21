/*
 * Copyright (C) 2025 AniTrend
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
package co.anitrend.core.android.compose.design.pageindicator

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import co.anitrend.core.android.ui.AniTrendPreview
import co.anitrend.core.android.ui.theme.preview.DarkThemeProvider
import co.anitrend.core.android.ui.theme.preview.PreviewTheme

private fun DrawScope.drawJoiningDots(
    leftDotCenterX: Float,
    dotCenterY: Float,
    rightDotCenterX: Float,
    dotRadius: Float,
    dotGap: Float,
    joiningFraction: Float,
    color: Color,
) {
    val path = Path()
    val halfDotRadius = dotRadius / 2
    val dotTopY = dotCenterY - dotRadius
    val dotBottomY = dotCenterY + dotRadius

    // Left dot
    path.moveTo(leftDotCenterX, dotBottomY)

    // Left dot arc
    path.arcTo(
        rect =
            Rect(
                left = leftDotCenterX - dotRadius,
                top = dotTopY,
                right = leftDotCenterX + dotRadius,
                bottom = dotBottomY,
            ),
        startAngleDegrees = 90f,
        sweepAngleDegrees = 180f,
        forceMoveTo = true,
    )

    // Extend to right
    val endX1 = leftDotCenterX + dotRadius + (joiningFraction * dotGap)
    path.cubicTo(
        x1 = leftDotCenterX + halfDotRadius,
        y1 = dotTopY,
        x2 = endX1,
        y2 = dotCenterY - halfDotRadius,
        x3 = endX1,
        y3 = dotCenterY,
    )

    // Back to bottom
    path.cubicTo(
        x1 = endX1,
        y1 = dotCenterY + halfDotRadius,
        x2 = leftDotCenterX + halfDotRadius,
        y2 = dotBottomY,
        x3 = leftDotCenterX,
        y3 = dotBottomY,
    )

    // Draw right dot with similar approach
    val rightDotPath = Path()
    rightDotPath.moveTo(rightDotCenterX, dotBottomY)

    rightDotPath.arcTo(
        rect =
            Rect(
                left = rightDotCenterX - dotRadius,
                top = dotTopY,
                right = rightDotCenterX + dotRadius,
                bottom = dotBottomY,
            ),
        startAngleDegrees = 90f,
        sweepAngleDegrees = -180f,
        forceMoveTo = true,
    )

    val endX2 = rightDotCenterX - dotRadius - (joiningFraction * dotGap)
    rightDotPath.cubicTo(
        x1 = rightDotCenterX - halfDotRadius,
        y1 = dotTopY,
        x2 = endX2,
        y2 = dotCenterY - halfDotRadius,
        x3 = endX2,
        y3 = dotCenterY,
    )

    rightDotPath.cubicTo(
        x1 = endX2,
        y1 = dotCenterY + halfDotRadius,
        x2 = rightDotCenterX - halfDotRadius,
        y2 = dotBottomY,
        x3 = rightDotCenterX,
        y3 = dotBottomY,
    )

    val combinedPath = Path()
    combinedPath.op(path, rightDotPath, PathOperation.Union)
    drawPath(path = combinedPath, color = color)
}

private fun DrawScope.drawCurvedJoining(
    leftDotCenterX: Float,
    dotCenterY: Float,
    rightDotCenterX: Float,
    dotRadius: Float,
    dotGap: Float,
    joiningFraction: Float,
    color: Color,
) {
    // Convert fraction to better visual effect
    val adjustedFraction = (joiningFraction - 0.2f) * 1.25f
    val path = Path()
    val dotTopY = dotCenterY - dotRadius
    val dotBottomY = dotCenterY + dotRadius

    // Start at bottom left
    path.moveTo(leftDotCenterX, dotBottomY)

    // Arc to top left
    path.arcTo(
        rect =
            Rect(
                left = leftDotCenterX - dotRadius,
                top = dotTopY,
                right = leftDotCenterX + dotRadius,
                bottom = dotBottomY,
            ),
        startAngleDegrees = 90f,
        sweepAngleDegrees = 180f,
        forceMoveTo = true,
    )

    // Connect to top middle with curve
    val middleX = leftDotCenterX + dotRadius + (dotGap / 2)
    val topY = dotCenterY - (adjustedFraction * dotRadius)

    path.cubicTo(
        x1 = middleX - (adjustedFraction * dotRadius),
        y1 = dotTopY,
        x2 = middleX - ((1 - adjustedFraction) * dotRadius),
        y2 = topY,
        x3 = middleX,
        y3 = topY,
    )

    // Continue to top right
    path.cubicTo(
        x1 = middleX + ((1 - adjustedFraction) * dotRadius),
        y1 = topY,
        x2 = middleX + (adjustedFraction * dotRadius),
        y2 = dotTopY,
        x3 = rightDotCenterX,
        y3 = dotTopY,
    )

    // Arc to bottom right
    path.arcTo(
        rect =
            Rect(
                left = rightDotCenterX - dotRadius,
                top = dotTopY,
                right = rightDotCenterX + dotRadius,
                bottom = dotBottomY,
            ),
        startAngleDegrees = 270f,
        sweepAngleDegrees = 180f,
        forceMoveTo = false,
    )

    // Connect to bottom middle with curve
    val bottomY = dotCenterY + (adjustedFraction * dotRadius)

    path.cubicTo(
        x1 = middleX + (adjustedFraction * dotRadius),
        y1 = dotBottomY,
        x2 = middleX + ((1 - adjustedFraction) * dotRadius),
        y2 = bottomY,
        x3 = middleX,
        y3 = bottomY,
    )

    // Finish back to start point
    path.cubicTo(
        x1 = middleX - ((1 - adjustedFraction) * dotRadius),
        y1 = bottomY,
        x2 = middleX - (adjustedFraction * dotRadius),
        y2 = dotBottomY,
        x3 = leftDotCenterX,
        y3 = dotBottomY,
    )

    drawPath(path = path, color = color)
}

@Composable
fun PageIndicator(
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    dotDiameter: Dp = 8.dp,
    dotGap: Dp = 12.dp,
    animationDuration: Int = 400,
    pageIndicatorColor: Color,
    currentPageIndicatorColor: Color,
) {
    if (pagerState.pageCount <= 1) return

    val density = LocalDensity.current
    val dotDiameterPx = with(density) { dotDiameter.toPx() }
    val dotRadiusPx = dotDiameterPx / 2
    val dotGapPx = with(density) { dotGap.toPx() }
    val totalWidth = pagerState.pageCount * dotDiameterPx + (pagerState.pageCount - 1) * dotGapPx

    // Derive the continuous visual page value
    val visualPage = pagerState.currentPage + pagerState.currentPageOffsetFraction

    // Animate the selected dot's offset smoothly
    val animatedDotOffset by animateFloatAsState(
        targetValue = visualPage,
        animationSpec =
            tween(
                durationMillis = animationDuration,
                easing = FastOutSlowInEasing,
            ),
    )

    // Calculate positions
    Canvas(
        modifier =
            modifier
                .width(with(density) { totalWidth.toDp() })
                .height(dotDiameter),
    ) {
        val dotRadius = dotDiameter.toPx() / 2
        val dotCenterY = size.height / 2

        // Draw unselected dots
        for (i in 0 until pagerState.pageCount) {
            val dotCenterX = dotRadius + i * (dotDiameter.toPx() + dotGapPx)

            if (i != pagerState.currentPage) {
                drawCircle(
                    color = pageIndicatorColor,
                    radius = dotRadius,
                    center = Offset(dotCenterX, dotCenterY),
                )
            }

            // Draw joining line between dots during transitions
            if (i < pagerState.pageCount - 1) {
                val nextDotCenterX = dotCenterX + dotDiameter.toPx() + dotGapPx
                val joiningFraction =
                    when {
                        animatedDotOffset <= i -> 0f
                        animatedDotOffset >= i + 1 -> 1f
                        else -> animatedDotOffset - i
                    }

                if (joiningFraction > 0) {
                    when {
                        joiningFraction <= 0.5f -> {
                            drawJoiningDots(
                                dotCenterX,
                                dotCenterY,
                                nextDotCenterX,
                                dotRadius,
                                dotGapPx,
                                joiningFraction,
                                pageIndicatorColor,
                            )
                        }

                        joiningFraction < 1f -> {
                            drawCurvedJoining(
                                dotCenterX,
                                dotCenterY,
                                nextDotCenterX,
                                dotRadius,
                                dotGapPx,
                                joiningFraction,
                                pageIndicatorColor,
                            )
                        }

                        else -> {
                            drawRoundRect(
                                color = pageIndicatorColor,
                                topLeft = Offset(dotCenterX - dotRadius, dotCenterY - dotRadius),
                                size = Size(nextDotCenterX - dotCenterX + dotRadius * 2, dotRadius * 2),
                                cornerRadius = CornerRadius(dotRadius, dotRadius),
                            )
                        }
                    }
                }
            }
        }

        val selectedDotX = dotRadiusPx + animatedDotOffset * (dotDiameterPx + dotGapPx)
        drawCircle(
            color = currentPageIndicatorColor,
            radius = dotRadiusPx,
            center = Offset(selectedDotX, dotCenterY),
        )
    }
}

@Composable
@AniTrendPreview.Default
private fun PageIndicatorPreview(
    @PreviewParameter(DarkThemeProvider::class) darkTheme: Boolean,
) {
    PreviewTheme(wrapInSurface = true, darkTheme = darkTheme) {
        PageIndicator(
            pagerState = rememberPagerState(2, pageCount = { 5 }),
            modifier = Modifier.padding(16.dp),
            dotDiameter = 8.dp,
            dotGap = 12.dp,
            animationDuration = 400,
            pageIndicatorColor = MaterialTheme.colorScheme.onSurface,
            currentPageIndicatorColor = MaterialTheme.colorScheme.primary,
        )
    }
}
