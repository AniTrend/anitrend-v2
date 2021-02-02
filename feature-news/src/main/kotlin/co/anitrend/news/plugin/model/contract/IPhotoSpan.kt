/*
 * Copyright (C) 2021  AniTrend
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

package co.anitrend.news.plugin.model.contract

import co.anitrend.news.plugin.model.SizeMeasurementUnit
import io.noties.markwon.image.ImageSize

internal interface IPhotoSpan : ISpanConfiguration {

    val magnificationScale: Float
    val sizeMeasurementUnit: SizeMeasurementUnit

    fun addPropertiesToImage(source: String, imageSize: ImageSize)

    private fun findSizeInStyle(descriptor: String, styles: String?, value: Float?): Float? {
        if (value != null) return value
        val sizeStyle = styles?.split(';')
            ?.firstOrNull { it.contains(descriptor) }
        return runCatching {
            val size = sizeStyle?.run {
                val delimiterPosition = sizeStyle.indexOf(':').plus(1)
                val endPosition = sizeStyle.indexOf(SizeMeasurementUnit.PIXEL.attr).minus(1)
                val newValue = sizeStyle.subSequence(IntRange(delimiterPosition, endPosition))
                newValue.toString().toFloat()
            }
            size
        }.getOrNull()
    }

    fun generateImageSize(styles: String?, width: Float?, height: Float?): ImageSize {
        val autoMeasureHeight = findSizeInStyle(HEIGHT_ATTR, styles, height)
        val autoMeasureWidth = findSizeInStyle(WIDTH_ATTR, styles, width)

        // magnify the images, alternatively we could adjust
        // the magnification based on device dimens
        val heightDimension = autoMeasureHeight?.let {
            ImageSize.Dimension(
                it * magnificationScale,
                sizeMeasurementUnit.attr
            )
        }
        val widthDimension = autoMeasureWidth?.let {
            ImageSize.Dimension(
                it * magnificationScale,
                sizeMeasurementUnit.attr
            )
        }
        return ImageSize(
            widthDimension,
            heightDimension
        )
    }

    companion object {
        internal const val SRC_ATTR = "src"
        internal const val HEIGHT_ATTR = "height"
        internal const val WIDTH_ATTR = "width"
        internal const val STYLE_ATTR = "style"
    }
}