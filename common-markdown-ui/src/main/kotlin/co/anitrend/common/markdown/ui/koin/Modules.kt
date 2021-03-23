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

package co.anitrend.common.markdown.ui.koin

import co.anitrend.common.markdown.R
import co.anitrend.common.markdown.ui.plugin.store.CoilStorePlugin
import co.anitrend.core.koin.helper.DynamicFeatureModuleHelper
import coil.request.ImageRequest
import coil.transform.RoundedCornersTransformation
import coil.transition.CrossfadeTransition
import io.noties.markwon.Markwon
import io.noties.markwon.html.HtmlPlugin
import io.noties.markwon.image.coil.CoilImagesPlugin
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

private val builderModule = module {
    factory {
        val context = androidContext()

        val radius = context.resources.getDimensionPixelSize(
            R.dimen.md_margin
        ).toFloat()

        val duration = context.resources.getInteger(
            R.integer.motion_duration_large
        )

        Markwon.builder(context)
            .usePlugin(HtmlPlugin.create())
            .usePlugin(
                CoilImagesPlugin.create(
                    CoilStorePlugin.create(
                        ImageRequest.Builder(context)
                            .transformations(RoundedCornersTransformation(radius))
                            .transition(CrossfadeTransition(duration))
                    ),
                    get()
                )
            )
    }
}

internal val moduleHelper = DynamicFeatureModuleHelper(listOf(builderModule))