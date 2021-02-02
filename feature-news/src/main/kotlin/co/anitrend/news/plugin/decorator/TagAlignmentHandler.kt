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

package co.anitrend.news.plugin.decorator

import android.text.Layout
import android.text.style.AlignmentSpan
import io.noties.markwon.MarkwonVisitor
import io.noties.markwon.SpannableBuilder
import io.noties.markwon.html.HtmlTag
import io.noties.markwon.html.MarkwonHtmlRenderer
import io.noties.markwon.html.TagHandler


internal class TagAlignmentHandler private constructor(): TagHandler() {

    override fun handle(
        visitor: MarkwonVisitor,
        renderer: MarkwonHtmlRenderer,
        tag: HtmlTag
    ) {
        if (tag.isBlock)
            visitChildren(visitor, renderer, tag.asBlock)

        SpannableBuilder.setSpans(
            visitor.builder(),
            AlignmentSpan.Standard(
                Layout.Alignment.ALIGN_CENTER
            ),
            tag.start(),
            tag.end()
        )
    }

    override fun supportedTags(): List<String> = listOf("center")

    companion object {
        fun create() = TagAlignmentHandler()
    }
}