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

package co.anitrend.news.plugin.model

import co.anitrend.news.plugin.model.contract.IPhotoSpan
import io.noties.markwon.MarkwonConfiguration
import io.noties.markwon.RenderProps
import io.noties.markwon.html.HtmlTag
import io.noties.markwon.image.ImageProps
import io.noties.markwon.image.ImageSize

internal data class ImageSpanConfiguration(
    val configuration: MarkwonConfiguration,
    val renderProps: RenderProps,
    val tag: HtmlTag,
    val cutoffImageSize: Int,
    override val magnificationScale: Float,
    override val sizeMeasurementUnit: SizeMeasurementUnit,
    override val isClickable: Boolean
) : IPhotoSpan {

    override fun addPropertiesToImage(source: String, imageSize: ImageSize) {
        ImageProps.DESTINATION.set(renderProps, source)
        ImageProps.IMAGE_SIZE.set(renderProps, imageSize)
        // No documentation is provided for this.. welp
        ImageProps.REPLACEMENT_TEXT_IS_LINK.set(renderProps, isClickable)
    }

    companion object {
        private const val TITLE_ATTR = "title"
        private const val ALT_ATTR = "alt"
    }
}