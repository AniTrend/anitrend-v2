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
import android.text.style.ParagraphStyle
import io.noties.markwon.MarkwonVisitor
import io.noties.markwon.SpannableBuilder
import io.noties.markwon.html.CssInlineStyleParser
import io.noties.markwon.html.HtmlTag
import io.noties.markwon.html.MarkwonHtmlRenderer
import io.noties.markwon.html.TagHandler
import timber.log.Timber

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
        val spans = ArrayList<ParagraphStyle>(2)
        val attributes = tag.attributes()
        val style = attributes["style"]

        if (tag.isBlock)
            visitChildren(visitor, renderer, tag.asBlock)

        if (style != null) {
            val properties = CssInlineStyleParser.create().parse(style)
            properties.forEach { property ->
                when (property.key()) {
                    TEXT_ALIGN -> {
                        if (property.value() == CENTER_ALIGN)
                            spans.add(AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER))
                        else if (property.value() == RIGHT_ALIGN)
                            spans.add(AlignmentSpan.Standard(Layout.Alignment.ALIGN_OPPOSITE))
                    }
                    MARGIN_LEFT -> {
                        val attribute = property.value()
                        if (attribute.contains("px")) {
                            val end = attribute.length - 2
                            val size = property.value().substring(0, end).toIntOrNull()
                            if (size != null)
                                spans.add(LeadingMarginSpan.Standard(size))
                        } else Timber.v("$MARGIN_LEFT has unknown unit $property")
                    }
                    else -> Timber.v("Not sure how to handle $property")
                }
            }
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
        private const val CENTER_ALIGN = "center"
        private const val RIGHT_ALIGN = "right"
        private const val TEXT_ALIGN = "text-align"
        private const val MARGIN_LEFT = "margin-left"

        fun create() = ParagraphTagHandler()
    }
}