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

package co.anitrend.news.plugin.tag

import co.anitrend.news.extensions.onFrame
import co.anitrend.common.markdown.ui.plugin.span.size.SizeMeasurementUnit
import co.anitrend.news.plugin.span.YouTubeSpanConfiguration
import io.noties.markwon.MarkwonConfiguration
import io.noties.markwon.RenderProps
import io.noties.markwon.html.HtmlTag
import io.noties.markwon.html.tag.SimpleTagHandler

/**
 * Allows us to handle iframes and extract images from it, we will only target youtube iframes
 * to return a clickable image which should trigger a youtube handling activity
 *
 * <iframe src="https://www.youtube.com/embed/luWcue3t2OU" width="640" height="360" />
 */
internal class FrameTagHandler private constructor() : SimpleTagHandler() {

    override fun getSpans(
        configuration: MarkwonConfiguration,
        renderProps: RenderProps,
        tag: HtmlTag
    ): Any? {
        val imageSpanConfiguration = YouTubeSpanConfiguration(
            magnificationScale = 1.8f,
            sizeMeasurementUnit = SizeMeasurementUnit.PIXEL,
            configuration = configuration,
            renderProps = renderProps,
            tag = tag
        )

        return imageSpanConfiguration.onFrame().toArray()
    }

    override fun supportedTags() = listOf(
        YouTubeSpanConfiguration.IFRAME_TAG
    )

    companion object {
        fun create() = FrameTagHandler()
    }
}