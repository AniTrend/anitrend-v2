/*
 * Copyright (C) 2020 AniTrend
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
package co.anitrend.common.media.ui.widget.airing

import android.content.Context
import android.text.SpannableStringBuilder
import android.util.AttributeSet
import androidx.core.text.color
import androidx.core.widget.TextViewCompat
import co.anitrend.arch.extension.ext.getCompatColor
import co.anitrend.arch.extension.ext.gone
import co.anitrend.arch.extension.ext.visible
import co.anitrend.arch.ui.view.contract.CustomView
import co.anitrend.common.media.ui.R
import co.anitrend.common.media.ui.widget.airing.controller.MediaAiringScheduleController
import co.anitrend.core.android.asPrettyTime
import co.anitrend.domain.media.entity.Media
import com.google.android.material.textview.MaterialTextView

class MediaAiringScheduleWidget
    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
    ) : MaterialTextView(context, attrs, defStyleAttr), CustomView {
        init {
            onInit(context, attrs, defStyleAttr)
        }

        private fun setDecoratedAiringText(controller: MediaAiringScheduleController) {
            val schedule = controller.getSchedule()
            val decoratorColor = controller.getColor(context)
            val builder =
                SpannableStringBuilder()
                    .color(decoratorColor) {
                        append(
                            context.getString(
                                R.string.label_episode_airing_in_time,
                                schedule.episode,
                                schedule.asPrettyTime(),
                            ),
                        )
                    }
            text = builder
        }

        fun setUpAiringSchedule(media: Media) {
            val controller = MediaAiringScheduleController(media)
            if (controller.shouldHideWidget()) {
                gone()
                return
            }
            visible()
            setDecoratedAiringText(controller)
        }

        /**
         * Callable in view constructors to perform view inflation and attribute initialization
         *
         * @param context view context
         * @param attrs view attributes if applicable
         * @param styleAttr style attribute if applicable
         */
        override fun onInit(
            context: Context,
            attrs: AttributeSet?,
            styleAttr: Int?,
        ) {
            if (isInEditMode) {
                val builder =
                    SpannableStringBuilder()
                        .color(context.getCompatColor(co.anitrend.core.android.R.color.orange_A400)) {
                            append(
                                context.getString(
                                    R.string.label_episode_airing_in_time,
                                    10,
                                    "2 days from now",
                                ),
                            )
                        }
                text = builder
            }
            TextViewCompat.setTextAppearance(this, com.google.android.material.R.style.TextAppearance_MaterialComponents_Caption)
        }
    }
