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
package co.anitrend.core.android.behaviour

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import co.anitrend.core.android.R

/**
 * Behavior will automatically sets up a [BottomOffsetBehavior.ViewOffsetHelper] on a [View].
 */
class BottomOffsetBehavior(
    private val context: Context,
    private val attributeSet: AttributeSet,
) : CoordinatorLayout.Behavior<View>() {
    // TODO: Offsets don't seem to be getting applied on the bottom, not entirely sure what could be missing

    private lateinit var viewOffsetHelper: ViewOffsetHelper

    /**
     * Called when the parent CoordinatorLayout is about the lay out the given child view.
     *
     * This method can be used to perform custom or modified layout of a child view
     * in place of the default child layout behavior. The Behavior's implementation can
     * delegate to the standard CoordinatorLayout measurement behavior by calling
     * `parent.onLayoutChild` [CoordinatorLayout.onLayoutChild].
     *
     * If a Behavior implements [onDependentViewChanged] to change the position of a view
     * in response to a dependent view changing, it should also implement `onLayoutChild`
     * in such a way that respects those dependent views. [onLayoutChild] will always
     * be called for a dependent view *after* its dependency has been laid out.
     *
     * @param parent the parent CoordinatorLayout
     * @param child child view to lay out
     * @param layoutDirection the resolved layout direction for the CoordinatorLayout, such as
     * [ViewCompat.LAYOUT_DIRECTION_LTR] or [ViewCompat.LAYOUT_DIRECTION_RTL].
     *
     * @return true if the Behavior performed layout of the child view, false to request
     * default layout behavior
     */
    override fun onLayoutChild(
        parent: CoordinatorLayout,
        child: View,
        layoutDirection: Int,
    ): Boolean {
        parent.onLayoutChild(child, layoutDirection)

        if (!::viewOffsetHelper.isInitialized) {
            viewOffsetHelper = ViewOffsetHelper(child)
        }

        viewOffsetHelper.doOnInit()
        viewOffsetHelper.applyOffsets()

        return setBottomOffset(
            context.resources.getDimensionPixelSize(
                R.dimen.design_bottom_app_bar_height,
            ),
        )
    }

    private fun setBottomOffset(offset: Int): Boolean {
        val offsetHelper = viewOffsetHelper
        if (::viewOffsetHelper.isInitialized) {
            return offsetHelper.setBottomOffset(offset)
        }

        return false
    }

    private class ViewOffsetHelper(private val view: View) {
        private var layoutBottom = 0
        private var offsetBottom = 0

        fun doOnInit() {
            layoutBottom = view.bottom
        }

        /**
         * Applies top and bottom offsets on the [view]
         */
        fun applyOffsets() {
            val offset = offsetBottom - (view.bottom - layoutBottom)
            ViewCompat.offsetTopAndBottom(view, offset)
        }

        /**
         * Set the top and bottom offset for this [ViewOffsetHelper]'s view.
         *
         * @param offset the offset in px.
         * @return true if the offset has changed
         */
        fun setBottomOffset(offset: Int): Boolean {
            if (offsetBottom != offset) {
                offsetBottom = offset
                applyOffsets()
                return true
            }
            return false
        }
    }
}
