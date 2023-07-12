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

package co.anitrend.core.android.recycler.model

import android.content.res.Resources
import android.view.View
import androidx.viewbinding.ViewBinding
import co.anitrend.arch.recycler.action.decorator.ISelectionDecorator
import co.anitrend.arch.recycler.model.contract.IRecyclerItem
import co.anitrend.core.android.R
import co.anitrend.core.android.binding.IBindingView

abstract class RecyclerItemBinding<B : ViewBinding>(
    override val id: Long
) : IRecyclerItem, IBindingView<B> {

    override var binding: B? = null

    /**
     * Decorator that can be used to style this item when it is selected or unselected
     */
    override val decorator: ISelectionDecorator =
        object : ISelectionDecorator {
            // uses the default implementation of decorator
        }

    /**
     * If selection mode can be used, this will allow automatic styling of elements based
     * on selection state when the view item/s are drawn
     */
    override val supportsSelectionMode: Boolean = false

    /**
     * Called when the view needs to be recycled for reuse, clear any held references
     * to objects, stop any asynchronous work, e.t.c
     */
    override fun unbind(view: View) {
        binding = null
    }

    /**
     * Provides a preferred span size for the item, defaulted to [R.integer.single_list_size]
     *
     * @param spanCount current span count which may also be [INVALID_SPAN_COUNT]
     * @param position position of the current item
     * @param resources optionally useful for dynamic size check with different configurations
     */
    override fun getSpanSize(
        spanCount: Int,
        position: Int,
        resources: Resources
    )= resources.getInteger(co.anitrend.arch.theme.R.integer.single_list_size)
}
