/*
 * Copyright (C) 2021 AniTrend
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
package co.anitrend.common.markdown.ui.animator

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.StateListAnimator
import android.view.View
import android.view.View.SCALE_X
import android.view.View.SCALE_Y
import android.view.animation.AccelerateDecelerateInterpolator

/** Plays a subtle push animation when [view] is pressed. */
class PushOnPressAnimator(private val view: View) : StateListAnimator() {
    init {
        addState(
            intArrayOf(android.R.attr.state_pressed),
            createAnimator(toScale = 0.95f),
        )
        addState(
            intArrayOf(-android.R.attr.state_pressed),
            createAnimator(toScale = 1f),
        )
    }

    private fun createAnimator(toScale: Float): Animator {
        val scaleX = PropertyValuesHolder.ofFloat(SCALE_X, toScale)
        val scaleY = PropertyValuesHolder.ofFloat(SCALE_Y, toScale)
        return ObjectAnimator.ofPropertyValuesHolder(view, scaleX, scaleY).apply {
            duration = 80
            interpolator = AccelerateDecelerateInterpolator()
        }
    }
}
