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
package co.anitrend.core.android.widget.viewpager

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.viewpager.widget.ViewPager
import java.lang.reflect.Field

/** [BottomSheetViewPager](https://github.com/kafumi/android-bottomsheet-viewpager)
 *
 * BottomSheetBehavior supports only single scrollable child view.
 * If there is a ViewPager and it has multiple scrollable views,
 * user can not scroll them as expected.
 *
 * @author kafumi
 */
class BottomSheetViewPager
    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
    ) : ViewPager(context, attrs) {
        // Need to access package-private `position` field of `ViewPager.LayoutParams` to determine
        // which child view is the view for currently selected item from `ViewPager.getCurrentItem()`.
        private val positionField: Field =
            LayoutParams::class.java.getDeclaredField(
                "position",
            ).also { it.isAccessible = true }

        init {
            addOnPageChangeListener(
                object : SimpleOnPageChangeListener() {
                    override fun onPageSelected(position: Int) {
                        // Need to call requestLayout() when selected page is changed so that
                        // `BottomSheetBehavior` calls `findScrollingChild()` and recognizes the new page
                        // as the "scrollable child".
                        requestLayout()
                    }
                },
            )
        }

        override fun getChildAt(index: Int): View {
            // Swap index 0 and `currentItem`
            val currentView = getCurrentView() ?: return super.getChildAt(index)
            return if (index == 0) {
                currentView
            } else {
                var view = super.getChildAt(index)
                if (view == currentView) {
                    view = super.getChildAt(0)
                }
                return view
            }
        }

        private fun getCurrentView(): View? {
            for (i in 0 until childCount) {
                val child = super.getChildAt(i)
                val lp = child.layoutParams as? LayoutParams
                if (lp != null) {
                    val position = positionField.getInt(lp)
                    if (!lp.isDecor && currentItem == position) {
                        return child
                    }
                }
            }
            return null
        }
    }
