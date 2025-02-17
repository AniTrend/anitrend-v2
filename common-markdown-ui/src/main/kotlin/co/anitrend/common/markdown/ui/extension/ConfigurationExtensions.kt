/*
 * Copyright (C) 2021 AniTrend
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
package co.anitrend.common.markdown.ui.extension

import co.anitrend.common.markdown.ui.plugin.span.image.AbstractImageSpan
import io.noties.markwon.core.CoreProps

/**
 * @return list of spans
 */
fun AbstractImageSpan.onImage(): ArrayList<Any?>? {
    val spans = ArrayList<Any?>(2)
    val attributes = tag.attributes()

    val destination = attributes[AbstractImageSpan.SRC_ATTR]
    val styles = attributes[AbstractImageSpan.STYLE_ATTR]

    val imageSize =
        generateImageSize(
            styles,
            attributes[AbstractImageSpan.WIDTH_ATTR]?.toFloat(),
            attributes[AbstractImageSpan.HEIGHT_ATTR]?.toFloat(),
        )

    if (destination == null) {
        return null
    }

    addPropertiesToImage(destination, imageSize)

    val imageSpanFactory =
        configuration
            .spansFactory()
            .get(org.commonmark.node.Image::class.java)

    if (isClickable) {
        val linkSpanFactory =
            configuration
                .spansFactory()
                .get(org.commonmark.node.Link::class.java)

        CoreProps.LINK_DESTINATION.set(renderProps, destination)
        spans.add(linkSpanFactory?.getSpans(configuration, renderProps))
    }

    spans.add(imageSpanFactory?.getSpans(configuration, renderProps))

    return spans
}
