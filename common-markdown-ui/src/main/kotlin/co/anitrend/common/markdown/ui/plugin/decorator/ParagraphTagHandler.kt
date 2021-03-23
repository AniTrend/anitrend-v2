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

package co.anitrend.common.markdown.ui.plugin.decorator

import android.text.Layout
import android.text.style.AlignmentSpan
import android.text.style.LeadingMarginSpan
import io.noties.markwon.MarkwonVisitor
import io.noties.markwon.SpannableBuilder
import io.noties.markwon.html.HtmlTag
import io.noties.markwon.html.MarkwonHtmlRenderer
import io.noties.markwon.html.TagHandler

/**
 * Handles attributes on paragraph tags
 *
 * <p style="text-align: center;"></p>
 * <p style="text-align: left;"></p>
 * <p style="text-align: right;"></p>
 * <p style="margin-left %%px;"></p>
 * <p class="p%"></p>
 */
class ParagraphTagHandler private constructor(): TagHandler() {

    override fun handle(
        visitor: MarkwonVisitor,
        renderer: MarkwonHtmlRenderer,
        tag: HtmlTag
    ) {
        val spans = ArrayList<Any>(2)
        val attributes = tag.attributes()
        val style = attributes["style"]
        @Suppress("UNUSED_VARIABLE") val styleClass = attributes["class"]

        if (tag.isBlock)
            visitChildren(visitor, renderer, tag.asBlock)

        if (style == CENTER_ALIGN)
            spans.add(AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER))
        else if (style == RIGHT_ALIGN)
            spans.add(AlignmentSpan.Standard(Layout.Alignment.ALIGN_OPPOSITE))

        if (style?.startsWith(MARGIN_PREFIX) == true) {
            // e.g. margin-left: 30px;
            val length = style.length
            val start = length - 5
            val end = length - 3

            val paddingSize = style.substring(start, end).toInt()
            spans.add(LeadingMarginSpan.Standard(paddingSize))
        }

        if (spans.isNotEmpty())
            SpannableBuilder.setSpans(
                visitor.builder(),
                spans.toArray(),
                tag.start(),
                tag.end()
            )

    }

    override fun supportedTags(): List<String> = listOf("p")

    companion object {
        private const val CENTER_ALIGN = "text-align: center;"
        private const val RIGHT_ALIGN = "text-align: right;"
        private const val MARGIN_PREFIX = "margin-left"

        fun create() = ParagraphTagHandler()
    }
}