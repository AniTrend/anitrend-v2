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

package co.anitrend.common.shared.ui.plugin

import co.anitrend.common.markdown.ui.plugin.decorator.ParagraphTagHandler
import co.anitrend.common.markdown.ui.plugin.decorator.CenterTagHandler
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.MarkwonPlugin
import io.noties.markwon.html.HtmlPlugin

internal class MarkdownPlugin private constructor(): AbstractMarkwonPlugin() {

    override fun configure(registry: MarkwonPlugin.Registry) {
        registry.require(HtmlPlugin::class.java) { plugin ->
            //plugin.addHandler(ImageTagHandler.create())
            plugin.addHandler(ParagraphTagHandler.create())
            plugin.addHandler(CenterTagHandler.create())
        }
    }

    companion object {
        fun create() = MarkdownPlugin()
    }
}