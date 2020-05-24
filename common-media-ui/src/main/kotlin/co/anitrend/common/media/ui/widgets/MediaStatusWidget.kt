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
import android.widget.FrameLayout
import co.anitrend.arch.extension.getCompatDrawable
import co.anitrend.arch.ui.view.contract.CustomView
import co.anitrend.common.media.ui.R
import co.anitrend.domain.media.enums.MediaStatus

internal class MediaStatusWidget @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), CustomView {

    init { onInit(context, attrs, defStyleAttr) }

    /**
     * Callable in view constructors to perform view inflation and attribute initialization
     *
     * @param context view context
     * @param attrs view attributes if applicable
     * @param styleAttr style attribute if applicable
     */
    override fun onInit(context: Context, attrs: AttributeSet?, styleAttr: Int?) {
        if (isInEditMode)
            background = context.getCompatDrawable(R.drawable.dashed_background)
    }

    /**
     * Sets the background of the view using a color from [mediaStatus]
     */
    fun setBackgroundUsing(mediaStatus: MediaStatus?) {
        val colorTint = when (mediaStatus) {
            MediaStatus.CANCELLED -> R.color.red_A700
            MediaStatus.FINISHED -> R.color.green_A700
            MediaStatus.NOT_YET_RELEASED -> R.color.orange_A700
            MediaStatus.RELEASING -> R.color.blue_A700
            else -> null
        }
        colorTint?.also {
            background = context.getCompatDrawable(
                R.drawable.dashed_background, it
            )
        }
    }
}