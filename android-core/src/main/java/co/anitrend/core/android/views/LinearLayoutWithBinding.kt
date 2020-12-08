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

package co.anitrend.core.android.views

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.viewbinding.ViewBinding
import co.anitrend.arch.ui.view.contract.CustomView
import co.anitrend.core.android.binding.IBindingView
import co.anitrend.data.arch.AniTrendExperimentalFeature

/**
 * Linear layout with view binding support
 * > **N.B.** View binding seems to cause a lot of data mix-ups within a view holder item
 */
@AniTrendExperimentalFeature
abstract class LinearLayoutWithBinding<V : ViewBinding> @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayoutCompat(context, attrs, defStyleAttr), CustomView, IBindingView<V> {

    override var binding: V? = null

    override fun onInit(context: Context, attrs: AttributeSet?, styleAttr: Int?) {
        binding = createBinding()
    }

    protected abstract fun createBinding(): V

    /**
     * Should be called on a view's detach from window to unbind or release object references
     * and cancel all running coroutine jobs if the current view
     *
     * Consider calling this in [android.view.View.onDetachedFromWindow]
     */
    override fun onViewRecycled() {
        super.onViewRecycled()
        binding = null
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        // TODO: Avoid setting binding to null in onDetach otherwise views don't get updated in a recycler
        //onViewRecycled()
    }
}