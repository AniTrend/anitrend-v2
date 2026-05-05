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
        val cutoutD = with(density) { cutoutDiameter.toPx() }
        val cutoutM = with(density) { cutoutMargin.toPx() }
        val cutoutRCR = with(density) { cutoutRoundedCornerRadius.toPx() }
        val cutoutVO = with(density) { cutoutVerticalOffset.toPx() }

        val cradleDiameter = cutoutM * 2 + cutoutD
        val cradleRadius = cradleDiameter / 2f
        val roundedCornerOffset = progress * cutoutRCR
        val middle = width / 2f

        // Vertical offset interpolated so the cradle emerges from the edge.
        val verticalOffset = progress * cutoutVO + (1 - progress) * cradleRadius
        val verticalOffsetRatio = verticalOffset / cradleRadius

        if (verticalOffsetRatio >= 1.0f) {
            // Cutout lifted entirely above the edge — draw a straight line.
            path.lineTo(width, 0f)
            return
        }

        // Distance between the centres of the cutout circle and each rounded-corner
        // circle, resolved via Pythagoras.
        val distBetweenCentres = cradleRadius + roundedCornerOffset
        val distY = verticalOffset + roundedCornerOffset
        val distX =
            sqrt(
                (distBetweenCentres * distBetweenCentres - distY * distY).toDouble(),
            ).toFloat()

        val leftCornerCircleX = middle - distX
        val rightCornerCircleX = middle + distX

        val cornerRadiusArcLength =
            Math.toDegrees(atan((distX / distY).toDouble())).toFloat()
        val cutoutArcOffset = 90f - cornerRadiusArcLength

        // Top edge → left of the cutout rounded corner.
        path.lineTo(leftCornerCircleX - roundedCornerOffset, 0f)

        // Left rounded-corner arc.
        // Small circle centred at (leftCornerCircleX, roundedCornerOffset).
        // Walk CW from north (270°) downward-left.
        path.arcTo(
            rect =
                Rect(
                    leftCornerCircleX - roundedCornerOffset,
                    0f,
                    leftCornerCircleX + roundedCornerOffset,
                    roundedCornerOffset * 2,
                ),
            startAngleDegrees = 270f,
            sweepAngleDegrees = -cornerRadiusArcLength,
            forceMoveTo = false,
        )

        // Main cutout arc (concave cradle).
        // Circle centred at (middle, -verticalOffset), radius = cradleRadius.
        // Walk CW from the left tangent through the bottom to the right tangent.
        path.arcTo(
            rect =
                Rect(
                    middle - cradleRadius,
                    -cradleRadius - verticalOffset,
                    middle + cradleRadius,
                    cradleRadius - verticalOffset,
                ),
            startAngleDegrees = 90f + cornerRadiusArcLength,
            sweepAngleDegrees = 2f * cornerRadiusArcLength,
            forceMoveTo = false,
        )

        // Right rounded-corner arc.
        // Small circle centred at (rightCornerCircleX, roundedCornerOffset).
        // Walk CW from lower-right up to north (270°).
        path.arcTo(
            rect =
                Rect(
                    rightCornerCircleX - roundedCornerOffset,
                    0f,
                    rightCornerCircleX + roundedCornerOffset,
                    roundedCornerOffset * 2,
                ),
            startAngleDegrees = 270f - cornerRadiusArcLength,
            sweepAngleDegrees = -cornerRadiusArcLength,
            forceMoveTo = false,
        )

        // Continue top edge to top-right corner.
        path.lineTo(width, 0f)
    }
}
