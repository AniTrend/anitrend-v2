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

import androidx.annotation.VisibleForTesting
import co.anitrend.news.plugin.model.contract.IPhotoSpan
import io.noties.markwon.MarkwonConfiguration
import io.noties.markwon.RenderProps
import io.noties.markwon.core.CoreProps
import io.noties.markwon.html.HtmlTag
import io.noties.markwon.image.ImageProps
import io.noties.markwon.image.ImageSize

internal data class YouTubeSpanConfiguration(
    val configuration: MarkwonConfiguration,
    val renderProps: RenderProps,
    val tag: HtmlTag,
    override val magnificationScale: Float,
    override val sizeMeasurementUnit: SizeMeasurementUnit,
    override val isClickable: Boolean = true
) : IPhotoSpan {

    @VisibleForTesting
    fun getVideoId(src: String) = src.split('/').last()

    @VisibleForTesting
    fun getVideoUrl(src: String): String {
        val videoId = getVideoId(src)
        return "https://youtube.com/watch?v=$videoId"
    }

    @VisibleForTesting
    fun createImageLink(src: String): String {
        val videoId = getVideoId(src)
        return "https://img.youtube.com/vi/$videoId/hqdefault.jpg"
    }

    override fun addPropertiesToImage(source: String, imageSize: ImageSize) {
        ImageProps.DESTINATION.set(renderProps, createImageLink(source))
        ImageProps.IMAGE_SIZE.set(renderProps, imageSize)
        ImageProps.REPLACEMENT_TEXT_IS_LINK.set(renderProps, false)
        CoreProps.LINK_DESTINATION.set(renderProps, getVideoUrl(source))
    }

    companion object {
        internal const val LOOKUP_KEY = "youtube"
        internal const val IFRAME_TAG = "iframe"
    }
}