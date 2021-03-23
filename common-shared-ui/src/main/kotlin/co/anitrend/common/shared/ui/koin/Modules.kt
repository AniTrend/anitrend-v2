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

package co.anitrend.common.shared.ui.koin

import co.anitrend.arch.extension.ext.getCompatColor
import co.anitrend.common.shared.R
import co.anitrend.core.android.koin.MarkdownFlavour
import co.anitrend.common.shared.ui.plugin.MarkdownPlugin
import co.anitrend.core.koin.helper.DynamicFeatureModuleHelper
import co.anitrend.support.markdown.center.CenterPlugin
import co.anitrend.support.markdown.ephasis.EmphasisPlugin
import co.anitrend.support.markdown.heading.HeadingPlugin
import co.anitrend.support.markdown.image.ImagePlugin
import co.anitrend.support.markdown.link.LinkifyPlugin
import co.anitrend.support.markdown.spoiler.SpoilerPlugin
import co.anitrend.support.markdown.webm.WebMPlugin
import co.anitrend.support.markdown.youtube.YouTubePlugin
import io.noties.markwon.Markwon
import io.noties.markwon.core.CorePlugin
import io.noties.markwon.ext.strikethrough.StrikethroughPlugin
import io.noties.markwon.ext.tasklist.TaskListPlugin
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

private val coreModule = module {
    single(named(MarkdownFlavour.ANILIST)) {
        val context = androidContext()
        val accent = context.getCompatColor(R.color.colorAccent)
        val builder = get<Markwon.Builder>()
        builder.usePlugin(MarkdownPlugin.create())
            .usePlugin(CorePlugin.create())
            .usePlugin(LinkifyPlugin.create())
            .usePlugin(HeadingPlugin.create())
            .usePlugin(EmphasisPlugin.create())
            .usePlugin(CenterPlugin.create())
            .usePlugin(ImagePlugin.create())
            .usePlugin(WebMPlugin.create())
            .usePlugin(YouTubePlugin.create())
            .usePlugin(SpoilerPlugin.create(accent))
            .usePlugin(StrikethroughPlugin.create())
            .usePlugin(TaskListPlugin.create(context))
            .build()
    }
}

internal val moduleHelper = DynamicFeatureModuleHelper(
    listOf(coreModule)
)