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

package co.anitrend.news.extensions

import android.text.Layout
import android.text.style.AlignmentSpan
import co.anitrend.common.markdown.ui.plugin.span.image.AbstractImageSpan
import co.anitrend.news.plugin.span.YouTubeSpanConfiguration
import io.noties.markwon.Prop
import org.commonmark.node.Image
import org.commonmark.node.Link
import org.commonmark.node.Text

/**
 * @return list of spans
 */
internal fun AbstractImageSpan.onFrame(): ArrayList<Any?> {
    val spans = ArrayList<Any?>(2)
    val attributes = tag.attributes()

    val source = attributes[AbstractImageSpan.SRC_ATTR]
    val styles = attributes[AbstractImageSpan.STYLE_ATTR]
    if (source?.contains(YouTubeSpanConfiguration.LOOKUP_KEY) == true) {
        val imageSpanFactory = configuration
            .spansFactory()
            .get(Image::class.java)

        val linkSpanFactory = configuration
            .spansFactory()
            .get(Link::class.java)

        val imageSize = generateImageSize(
            styles,
            attributes[AbstractImageSpan.WIDTH_ATTR]?.toFloat(),
            attributes[AbstractImageSpan.HEIGHT_ATTR]?.toFloat()
        )

        addPropertiesToImage(source, imageSize)
        spans.add(imageSpanFactory?.getSpans(configuration, renderProps))
        spans.add(linkSpanFactory?.getSpans(configuration, renderProps))
        spans.add(AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER))
    } else {
        // return some sort of unsupported span
        val textSpan = configuration
            .spansFactory()
            .get(Text::class.java)

        renderProps.set(
            Prop.of("text-literal"),
            "Unsupported embedded element"
        )
        spans.add(textSpan?.getSpans(configuration, renderProps))
    }

    return spans
}