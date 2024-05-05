/*
 * Copyright (C) 2024 AniTrend
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
package co.anitrend.navigation.drawer.action

import android.view.View
import android.widget.ImageView
import androidx.core.view.marginTop
import androidx.core.view.updatePadding
import co.anitrend.core.android.animations.normalize
import co.anitrend.core.android.components.sheet.action.contract.OnSlideAction
import com.google.android.material.shape.MaterialShapeDrawable

/**
 * A slide action which rotates a view counterclockwise by 180 degrees between the hidden state
 * and the half expanded state.
 */
internal class HalfClockwiseRotateSlideAction(
    private val view: View,
) : OnSlideAction {
    override fun onSlide(
        sheet: View,
        slideOffset: Float,
    ) {
        view.rotation =
            slideOffset.normalize(
                -1F,
                0F,
                0F,
                180F,
            )
    }
}

/**
 * Rotate the given [view] counter-clockwise by 180 degrees.
 */
internal class HalfCounterClockwiseRotateSlideAction(
    private val view: View,
) : OnSandwichSlideAction {
    override fun onSlide(slideOffset: Float) {
        view.rotation =
            slideOffset.normalize(
                0F,
                1F,
                180F,
                0F,
            )
    }
}

/**
 * A slide action which acts on the nav drawer between the half expanded state and
 * expanded state by:
 * - Scaling the user avatar
 * - Translating the foreground sheet
 * - Removing the foreground sheets rounded corners/edge treatment
 */
class ForegroundSheetTransformSlideAction(
    private val foregroundView: View,
    private val foregroundShapeDrawable: MaterialShapeDrawable,
    private val profileImageView: ImageView,
) : OnSlideAction {
    private val foregroundMarginTop = foregroundView.marginTop
    private var systemTopInset: Int = 0
    private val foregroundZ = foregroundView.z
    private val profileImageOriginalZ = profileImageView.z

    private fun getPaddingTop(): Int {
        // This view's tag might not be set immediately as it needs to wait for insets to be
        // applied. Lazily evaluate to ensure we get a value, even if we've already started slide
        // changes.
        if (systemTopInset == 0) {
            systemTopInset = foregroundView.getTag(co.anitrend.core.android.R.id.tag_system_window_inset_top) as? Int? ?: 0
        }
        return systemTopInset
    }

    override fun onSlide(
        sheet: View,
        slideOffset: Float,
    ) {
        val progress = slideOffset.normalize(0F, 0.25F, 1F, 0F)
        profileImageView.scaleX = progress
        profileImageView.scaleY = progress
        foregroundShapeDrawable.interpolation = progress

        foregroundView.translationY = -(1 - progress) * foregroundMarginTop
        val topPaddingProgress = slideOffset.normalize(0F, 0.9F, 0F, 1F)
        foregroundView.updatePadding(top = (getPaddingTop() * topPaddingProgress).toInt())

        // Modify the z ordering of the profileImage to make it easier to click when half-expanded.
        // Reset the z order if the sheet is expanding so the profile image slides under the
        // foreground sheet.
        if (slideOffset > 0 && foregroundZ <= profileImageView.z) {
            profileImageView.z = profileImageOriginalZ
        } else if (slideOffset <= 0 && foregroundZ >= profileImageView.z) {
            profileImageView.z = foregroundZ + 1
        }
    }
}

/**
 * Change the alpha of [view] when a bottom sheet is slid.
 *
 * @param reverse Setting reverse to true will cause the view's alpha to approach 0.0 as the sheet
 *  slides up. The default behavior, false, causes the view's alpha to approach 1.0 as the sheet
 *  slides up.
 */
class AlphaSlideAction(
    private val view: View,
    private val reverse: Boolean = false,
) : OnSlideAction {
    override fun onSlide(
        sheet: View,
        slideOffset: Float,
    ) {
        val alpha = slideOffset.normalize(-1F, 0F, 0F, 1F)
        view.alpha = if (!reverse) alpha else 1F - alpha
    }
}
