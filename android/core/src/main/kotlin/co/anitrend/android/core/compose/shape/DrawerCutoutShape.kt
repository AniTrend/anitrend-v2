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
package co.anitrend.android.core.compose.shape

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import kotlin.math.atan
import kotlin.math.sqrt

/**
 * A Compose [Shape] that draws a rounded-top rectangle with an optional centered
 * semicircle cutout (cradle) on the top edge.
 *
 * This replicates the legacy [co.anitrend.android.core.components.edgetreatment.SemiCircleCutout]
 * edge treatment as a native Compose shape for use with drawer bottom sheets.
 *
 * When [cutoutProgress] is 0f the shape behaves like a normal rounded-top sheet
 * ([RoundedCornerShape] with [topCornerRadius]).  When [cutoutProgress] is 1f the
 * centered concave cradle is fully visible.
 *
 * @param topCornerRadius Radius for the top-left and top-right corners.
 * @param cutoutDiameter Diameter of the semicircle cutout.
 * @param cutoutMargin Extra horizontal margin added to each side of the cutout diameter.
 * @param cutoutRoundedCornerRadius Radius of the rounded transition where the cutout
 *   meets the straight top edge.
 * @param cutoutVerticalOffset Lifts the cutout upward relative to the circle centre.
 * @param cutoutProgress Interpolation value in `[0f, 1f]` controlling cutout visibility.
 */
class DrawerCutoutShape(
    private val topCornerRadius: Dp = 30.dp,
    private val cutoutDiameter: Dp = 32.dp,
    private val cutoutMargin: Dp = 8.dp,
    private val cutoutRoundedCornerRadius: Dp = 24.dp,
    private val cutoutVerticalOffset: Dp = 0.dp,
    private val cutoutProgress: Float = 0f,
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density,
    ): Outline {
        val progress = cutoutProgress.coerceIn(0f, 1f)
        val topCR = with(density) { topCornerRadius.toPx() }

        val path =
            Path().apply {
                // Start at bottom-left, walk clockwise.
                moveTo(0f, size.height)
                // Left edge → up.
                lineTo(0f, topCR)
                // Top-left rounded corner: CW from west (180°) to north (270°).
                arcTo(
                    rect = Rect(0f, 0f, topCR * 2, topCR * 2),
                    startAngleDegrees = 180f,
                    sweepAngleDegrees = 90f,
                    forceMoveTo = false,
                )

                if (progress == 0f) {
                    // Plain top edge — no cutout.
                    lineTo(size.width - topCR, 0f)
                } else {
                    drawCutout(
                        path = this,
                        width = size.width,
                        progress = progress,
                        density = density,
                    )
                }

                // Top-right rounded corner: CW from north (270°) to east (360°).
                arcTo(
                    rect = Rect(size.width - topCR * 2, 0f, size.width, topCR * 2),
                    startAngleDegrees = 270f,
                    sweepAngleDegrees = 90f,
                    forceMoveTo = false,
                )
                // Right edge → down.
                lineTo(size.width, size.height)
                // Bottom edge back to start.
                close()
            }

        return Outline.Generic(path)
    }

    private fun drawCutout(
        path: Path,
        width: Float,
        progress: Float,
        density: Density,
    ) {
        when (
            val geometry =
                calculateDrawerCutoutGeometry(
                    width = width,
                    cutoutDiameter = with(density) { cutoutDiameter.toPx() },
                    cutoutMargin = with(density) { cutoutMargin.toPx() },
                    cutoutRoundedCornerRadius = with(density) { cutoutRoundedCornerRadius.toPx() },
                    cutoutVerticalOffset = with(density) { cutoutVerticalOffset.toPx() },
                    cutoutProgress = progress,
                )
        ) {
            DrawerCutoutGeometry.Plain -> {
                // Cutout lifted entirely above the edge — draw a straight line.
                path.lineTo(width, 0f)
                return
            }
            is DrawerCutoutGeometry.Cutout -> drawCutout(path, width, geometry)
        }
    }

    private fun drawCutout(
        path: Path,
        width: Float,
        geometry: DrawerCutoutGeometry.Cutout,
    ) {
        if (geometry.verticalOffsetRatio >= 1.0f) {
            path.lineTo(width, 0f)
            return
        }

        // Top edge → left of the cutout rounded corner.
        path.lineTo(geometry.leftRoundedCornerCircleX - geometry.roundedCornerOffset, 0f)

        // Left rounded-corner arc.
        // Small circle centred at (leftRoundedCornerCircleX, roundedCornerOffset).
        path.arcTo(
            rect =
                Rect(
                    geometry.leftRoundedCornerCircleX - geometry.roundedCornerOffset,
                    0f,
                    geometry.leftRoundedCornerCircleX + geometry.roundedCornerOffset,
                    geometry.roundedCornerOffset * 2,
                ),
            startAngleDegrees = geometry.leftRoundedCornerStartAngle,
            sweepAngleDegrees = geometry.leftRoundedCornerSweep,
            forceMoveTo = false,
        )

        // Main cutout arc (concave cradle).
        // Circle centred at (middle, -verticalOffset), radius = cradleRadius.
        path.arcTo(
            rect =
                Rect(
                    geometry.middle - geometry.cradleRadius,
                    -geometry.cradleRadius - geometry.verticalOffset,
                    geometry.middle + geometry.cradleRadius,
                    geometry.cradleRadius - geometry.verticalOffset,
                ),
            startAngleDegrees = geometry.mainCutoutStartAngle,
            sweepAngleDegrees = geometry.mainCutoutSweep,
            forceMoveTo = false,
        )

        // Right rounded-corner arc.
        // Small circle centred at (rightRoundedCornerCircleX, roundedCornerOffset).
        path.arcTo(
            rect =
                Rect(
                    geometry.rightRoundedCornerCircleX - geometry.roundedCornerOffset,
                    0f,
                    geometry.rightRoundedCornerCircleX + geometry.roundedCornerOffset,
                    geometry.roundedCornerOffset * 2,
                ),
            startAngleDegrees = geometry.rightRoundedCornerStartAngle,
            sweepAngleDegrees = geometry.rightRoundedCornerSweep,
            forceMoveTo = false,
        )

        // Continue top edge to top-right corner.
        path.lineTo(width, 0f)
    }
}

internal sealed interface DrawerCutoutGeometry {
    data object Plain : DrawerCutoutGeometry

    data class Cutout(
        val cradleDiameter: Float,
        val cradleRadius: Float,
        val roundedCornerOffset: Float,
        val middle: Float,
        val verticalOffset: Float,
        val verticalOffsetRatio: Float,
        val leftRoundedCornerCircleX: Float,
        val rightRoundedCornerCircleX: Float,
        val leftRoundedCornerStartAngle: Float,
        val leftRoundedCornerSweep: Float,
        val mainCutoutStartAngle: Float,
        val mainCutoutSweep: Float,
        val rightRoundedCornerStartAngle: Float,
        val rightRoundedCornerSweep: Float,
    ) : DrawerCutoutGeometry
}

internal fun calculateDrawerCutoutGeometry(
    width: Float,
    cutoutDiameter: Float,
    cutoutMargin: Float,
    cutoutRoundedCornerRadius: Float,
    cutoutVerticalOffset: Float,
    cutoutProgress: Float,
): DrawerCutoutGeometry {
    if (cutoutDiameter == 0f) {
        return DrawerCutoutGeometry.Plain
    }

    val cradleDiameter = cutoutMargin * 2 + cutoutDiameter
    val cradleRadius = cradleDiameter / 2f
    val roundedCornerOffset = cutoutProgress * cutoutRoundedCornerRadius
    val middle = width / 2f
    val verticalOffset = cutoutProgress * cutoutVerticalOffset + (1 - cutoutProgress) * cradleRadius
    val verticalOffsetRatio = verticalOffset / cradleRadius

    if (verticalOffsetRatio >= 1.0f) {
        return DrawerCutoutGeometry.Plain
    }

    val distanceBetweenCenters = cradleRadius + roundedCornerOffset
    val distanceY = verticalOffset + roundedCornerOffset
    val distanceX =
        sqrt(
            (distanceBetweenCenters * distanceBetweenCenters - distanceY * distanceY).toDouble(),
        ).toFloat()
    val leftRoundedCornerCircleX = middle - distanceX
    val rightRoundedCornerCircleX = middle + distanceX
    val cornerRadiusArcLength =
        Math
            .toDegrees(
                atan((distanceX / distanceY).toDouble()),
            ).toFloat()
    val cutoutArcOffset = 90f - cornerRadiusArcLength

    return DrawerCutoutGeometry.Cutout(
        cradleDiameter = cradleDiameter,
        cradleRadius = cradleRadius,
        roundedCornerOffset = roundedCornerOffset,
        middle = middle,
        verticalOffset = verticalOffset,
        verticalOffsetRatio = verticalOffsetRatio,
        leftRoundedCornerCircleX = leftRoundedCornerCircleX,
        rightRoundedCornerCircleX = rightRoundedCornerCircleX,
        leftRoundedCornerStartAngle = 270f,
        leftRoundedCornerSweep = cornerRadiusArcLength,
        mainCutoutStartAngle = 180f - cutoutArcOffset,
        mainCutoutSweep = cutoutArcOffset * 2 - 180f,
        rightRoundedCornerStartAngle = 270f - cornerRadiusArcLength,
        rightRoundedCornerSweep = cornerRadiusArcLength,
    )
}
