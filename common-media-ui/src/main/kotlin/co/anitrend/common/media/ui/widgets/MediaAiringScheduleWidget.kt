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
import android.os.Build
import android.text.SpannableStringBuilder
import android.util.AttributeSet
import androidx.core.text.color
import androidx.core.widget.TextViewCompat
import co.anitrend.arch.extension.ext.getCompatColor
import co.anitrend.arch.extension.ext.gone
import co.anitrend.arch.extension.ext.visible
import co.anitrend.arch.ui.view.contract.CustomView
import co.anitrend.common.media.ui.R
import co.anitrend.core.android.getPrettyTime
import co.anitrend.core.android.helpers.image.toColorInt
import co.anitrend.domain.common.HexColor
import co.anitrend.domain.media.entity.Media
import com.google.android.material.textview.MaterialTextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

internal class MediaAiringScheduleWidget @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : MaterialTextView(context, attrs, defStyleAttr), CustomView, CoroutineScope by MainScope() {

    init { onInit(context, attrs, defStyleAttr) }

    private fun setDecoratedAiringText(media: Media.Category.Anime, color: HexColor?) {
        val decoratorColor = color?.toColorInt() ?: context.getCompatColor(R.color.primaryTextColor)
        val timeString = requireNotNull(media.schedule).getPrettyTime()
        val builder = SpannableStringBuilder().color(decoratorColor) {
            append(context.getString(R.string.label_episode_airing_in_time, requireNotNull(media.schedule).episode, timeString))
        }
        text = builder
    }

    fun setUpAiringSchedule(media: Media) {
        val mediaCategory = media.category
        if (mediaCategory !is Media.Category.Anime || mediaCategory.schedule == null) {
            gone()
            return
        }
        visible()
        setDecoratedAiringText(mediaCategory, media.image.color)
    }

    /**
     * Callable in view constructors to perform view inflation and attribute initialization
     *
     * @param context view context
     * @param attrs view attributes if applicable
     * @param styleAttr style attribute if applicable
     */
    override fun onInit(context: Context, attrs: AttributeSet?, styleAttr: Int?) {
        if (isInEditMode) {
            val builder = SpannableStringBuilder()
                .color(context.getCompatColor(R.color.orange_A400)) {
                    append(
                        context.getString(
                            R.string.label_episode_airing_in_time,
                            10,
                            "2 days from now"
                        )
                    )
                }
            text = builder
        }
        TextViewCompat.setTextAppearance(this, R.style.TextAppearance_MaterialComponents_Caption)
    }

    /**
     * Should be called on a view's detach from window to unbind or release object references
     * and cancel all running coroutine jobs if the current view
     *
     * Consider calling this in [android.view.View.onDetachedFromWindow]
     */
    override fun onViewRecycled() {
        super.onViewRecycled()
        cancel()
    }

    /**
     * This is called when the view is detached from a window.  At this point it
     * no longer has a surface for drawing.
     *
     * @see .onAttachedToWindow
     */
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        onViewRecycled()
    }
}