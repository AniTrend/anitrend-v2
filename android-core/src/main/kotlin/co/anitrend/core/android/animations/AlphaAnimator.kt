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
package co.anitrend.core.android.animations

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.LinearInterpolator
import co.anitrend.arch.theme.animator.contract.AbstractAnimator

class AlphaAnimator(
    val from: Float = .85f,
    val to: Float = 1f,
) : AbstractAnimator() {
    override val interpolator = LinearInterpolator()

    override fun getAnimators(view: View): Array<Animator> {
        val animator =
            ObjectAnimator.ofFloat(
                view,
                PROPERTY_NAME,
                from,
                to,
            )
        return arrayOf(animator)
    }

    companion object {
        private const val PROPERTY_NAME = "alpha"
    }
}
