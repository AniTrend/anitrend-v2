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

import co.anitrend.common.markdown.ui.extension.onImage
import co.anitrend.common.markdown.ui.plugin.span.size.SizeMeasurementUnit
import co.anitrend.news.plugin.span.RssImageSpanConfiguration
import io.noties.markwon.MarkwonConfiguration
import io.noties.markwon.RenderProps
import io.noties.markwon.html.HtmlTag
import io.noties.markwon.html.tag.SimpleTagHandler

internal class RssImageTagHandler private constructor() : SimpleTagHandler() {

    override fun getSpans(
        configuration: MarkwonConfiguration,
        renderProps: RenderProps,
        tag: HtmlTag
    ): Any? {
        val imageSpanConfiguration = RssImageSpanConfiguration(
            magnificationScale = 1.5f,
            sizeMeasurementUnit = SizeMeasurementUnit.PIXEL,
            configuration = configuration,
            renderProps = renderProps,
            tag = tag,
            cutoffImageSize = 80,
            isClickable = true
        )

        return imageSpanConfiguration.onImage()?.toArray()
    }

    override fun supportedTags() = listOf("img")

    companion object {
        fun create() = RssImageTagHandler()
    }
}