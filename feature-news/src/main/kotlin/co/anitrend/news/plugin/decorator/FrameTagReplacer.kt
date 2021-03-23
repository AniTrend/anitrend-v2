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

import android.annotation.SuppressLint
import io.noties.markwon.html.HtmlEmptyTagReplacement
import io.noties.markwon.html.HtmlTag

internal class FrameTagReplacer private constructor() : HtmlEmptyTagReplacement() {

    /**
     * @return replacement for supplied startTag or null if no replacement should occur (which will
     * lead to `Inline` tag have start &amp; end the same value, thus not applicable for applying a Span)
     */
    @SuppressLint("DefaultLocale")
    override fun replace(tag: HtmlTag): String? {
        return when (tag.name().toLowerCase()) {
            in handlingTags -> frameReplacement
            else -> super.replace(tag)
        }
    }

    companion object {
        private const val frameReplacement = "\u00a0" // non-breakable space
        private val handlingTags = listOf("iframe")

        fun create() = FrameTagReplacer()
    }
}