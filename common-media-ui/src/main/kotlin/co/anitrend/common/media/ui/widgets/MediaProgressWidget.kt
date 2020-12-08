/*
 * Copyright (C) 2020  AniTrend
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

package co.anitrend.common.media.ui.widgets

import android.content.Context
import android.util.AttributeSet
import co.anitrend.arch.extension.ext.getLayoutInflater
import co.anitrend.arch.ui.view.contract.CustomView
import co.anitrend.common.media.ui.databinding.MediaProgressWidgetBinding
import co.anitrend.core.android.views.FrameLayoutWithBinding
import co.anitrend.data.arch.AniTrendExperimentalFeature

@OptIn(AniTrendExperimentalFeature::class)
internal class MediaProgressWidget @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayoutWithBinding<MediaProgressWidgetBinding>(
    context, attrs, defStyleAttr
), CustomView {

    init { onInit(context, attrs, defStyleAttr) }

    override fun createBinding() =
        MediaProgressWidgetBinding.inflate(context.getLayoutInflater(), this, true)
}