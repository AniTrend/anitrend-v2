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
package co.anitrend.core.android.recycler.decorator

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import co.anitrend.core.android.extensions.dp
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import timber.log.Timber

class DefaultSpacingDecorator(
    private val itemSpacing: Int = 2.dp,
    private val edgeSpacing: Int = 2.dp,
) : RecyclerView.ItemDecoration() {
    private fun isVerticalOrientation(recyclerView: RecyclerView): Boolean {
        return when (val layoutManager = recyclerView.layoutManager) {
            is FlexboxLayoutManager -> layoutManager.flexDirection == FlexDirection.COLUMN
            is LinearLayoutManager -> layoutManager.orientation == LinearLayoutManager.VERTICAL
            is GridLayoutManager -> layoutManager.orientation == GridLayoutManager.VERTICAL
            is StaggeredGridLayoutManager -> layoutManager.orientation == StaggeredGridLayoutManager.VERTICAL
            null -> {
                Timber.v("Recycler does not have a layout manager attached to it yet, returning true")
                true
            }
            else -> throw NotImplementedError("Not sure how to handle $layoutManager")
        }
    }

    /**
     * Retrieve any offsets for the given item. Each field of `outRect` specifies
     * the number of pixels that the item view should be inset by, similar to padding or margin.
     * The default implementation sets the bounds of outRect to 0 and returns.
     *
     * If this ItemDecoration does not affect the positioning of item views, it should set
     * all four fields of `outRect` (left, top, right, bottom) to zero
     * before returning.
     *
     * If you need to access Adapter for additional data, you can call
     * [RecyclerView.getChildAdapterPosition] to get the adapter position of the
     * View.
     *
     * @param outRect Rect to receive the output.
     * @param view The child view to decorate
     * @param parent RecyclerView this ItemDecoration is decorating
     * @param state The current state of RecyclerView.
     */
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        /** Credits: https://stackoverflow.com/a/64375667/1725347 */
        val count = parent.adapter?.itemCount ?: 0
        val position = parent.getChildAdapterPosition(view)
        val leading = if (position == 0) edgeSpacing else itemSpacing
        val trailing = if (position == count - 1) edgeSpacing else 0
        with(outRect) {
            if (isVerticalOrientation(parent)) {
                top = leading
                bottom = trailing
            } else {
                left = leading
                right = trailing
            }
        }
    }
}
