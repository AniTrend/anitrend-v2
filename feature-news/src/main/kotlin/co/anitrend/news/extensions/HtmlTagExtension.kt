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
import co.anitrend.news.plugin.model.ImageSpanConfiguration
import co.anitrend.news.plugin.model.YouTubeSpanConfiguration
import co.anitrend.news.plugin.model.contract.IPhotoSpan
import io.noties.markwon.Prop
import io.noties.markwon.core.CoreProps
import org.commonmark.node.Image
import org.commonmark.node.Link
import org.commonmark.node.Text

/**
 * @return list of spans
 */
internal fun ImageSpanConfiguration.onImage(): ArrayList<Any?>? {
    val spans = ArrayList<Any?>(2)
    val attributes = tag.attributes()

    val destination = attributes[IPhotoSpan.SRC_ATTR]
    val styles = attributes[IPhotoSpan.STYLE_ATTR]

    val imageSize = generateImageSize(
        styles,
        attributes[IPhotoSpan.WIDTH_ATTR]?.toFloat(),
        attributes[IPhotoSpan.HEIGHT_ATTR]?.toFloat()
    )

    if (destination == null)
        return null

    addPropertiesToImage(destination, imageSize)

    val imageSpanFactory = configuration
        .spansFactory()
        .get(Image::class.java)

    if (isClickable) {
        val linkSpanFactory = configuration
            .spansFactory()
            .get(Link::class.java)

        CoreProps.LINK_DESTINATION.set(renderProps, destination)
        spans.add(linkSpanFactory?.getSpans(configuration, renderProps))
    }

    spans.add(imageSpanFactory?.getSpans(configuration, renderProps))

    return spans
}

/**
 * @return list of spans
 */
internal fun YouTubeSpanConfiguration.onFrame(): ArrayList<Any?> {
    val spans = ArrayList<Any?>(2)
    val attributes = tag.attributes()

    val source = attributes[IPhotoSpan.SRC_ATTR]
    val styles = attributes[IPhotoSpan.STYLE_ATTR]
    if (source?.contains(YouTubeSpanConfiguration.LOOKUP_KEY) == true) {
        val imageSpanFactory = configuration
            .spansFactory()
            .get(Image::class.java)

        val linkSpanFactory = configuration
            .spansFactory()
            .get(Link::class.java)

        val imageSize = generateImageSize(
            styles,
            attributes[IPhotoSpan.WIDTH_ATTR]?.toFloat(),
            attributes[IPhotoSpan.HEIGHT_ATTR]?.toFloat()
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