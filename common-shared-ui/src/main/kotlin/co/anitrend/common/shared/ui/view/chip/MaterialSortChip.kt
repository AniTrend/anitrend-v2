/*
 * Copyright (C) 2021  AniTrend
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

package co.anitrend.common.shared.ui.view.chip

import android.content.Context
import android.util.AttributeSet
import co.anitrend.arch.extension.ext.UNSAFE
import co.anitrend.arch.extension.ext.getCompatDrawable
import co.anitrend.arch.ui.view.contract.CustomView
import co.anitrend.common.shared.R
import co.anitrend.common.shared.ui.view.chip.controller.MaterialSortChipController
import co.anitrend.common.shared.ui.view.chip.controller.contract.IMaterialSortChipController
import co.anitrend.common.shared.ui.view.chip.enums.CheckedState
import co.anitrend.domain.common.enums.contract.ISortable
import co.anitrend.domain.common.sort.order.SortOrder
import co.anitrend.navigation.model.sorting.Sorting
import com.airbnb.paris.extensions.style
import com.google.android.material.chip.Chip

class MaterialSortChip @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = com.google.android.material.R.attr.chipStyle
) : Chip(context, attrs, defStyleAttr), CustomView {

    private val controller: IMaterialSortChipController by lazy(UNSAFE) {
        MaterialSortChipController()
    }

    val sortOrder: SortOrder
        get() = controller.asSortOrder()

    init {
        onInit(context, attrs, defStyleAttr)
    }

    /**
     * Updates internal sorting state for the current chip
     *
     * @see Sorting
     * @see ISortable
     */
    fun <T : ISortable> updateSortOrder(sorting: Sorting<T>) {
        controller.updateSortOrder(sorting)
        isChecked = true
    }

    private fun updateCheckedIconOn(checked: Boolean) {
        checkedIcon = if (checked) {
            when (controller.toggleCheckedState()) {
                CheckedState.NONE -> null
                CheckedState.ASC ->
                    context.getCompatDrawable(
                        R.drawable.ic_arrow_up,
                        co.anitrend.core.android.R.color.white_1000
                    )
                CheckedState.DESC ->
                    context.getCompatDrawable(
                        R.drawable.ic_arrow_down,
                        co.anitrend.core.android.R.color.white_1000
                    )
            }
        } else null
    }

    override fun setChecked(checked: Boolean) {
        updateCheckedIconOn(checked)
        super.setChecked(checked)
    }

    /**
     * Callable in view constructors to perform view inflation and attribute initialization
     *
     * @param context view context
     * @param attrs view attributes if applicable
     * @param styleAttr style attribute if applicable
     */
    override fun onInit(context: Context, attrs: AttributeSet?, styleAttr: Int?) {
        style(co.anitrend.core.android.R.style.AppTheme_Material_Chip_Action)
        setChipBackgroundColorResource(
            co.anitrend.core.android.R.color.selector_chip_background
        )
        setOnCheckedChangeListener { _, _ ->
            isChecked = controller.isCheckedState()
        }
        isCheckable = true
    }

    /**
     * Should be called on a view's detach from window to unbind or release object references
     * and cancel all running coroutine jobs if the current view
     *
     * Consider calling this in [android.view.View.onDetachedFromWindow]
     */
    override fun onViewRecycled() {
        super.onViewRecycled()
        setOnCheckedChangeListener(null)
    }

    /**
     * This is called when the view is detached from a window.  At this point it
     * no longer has a surface for drawing.
     *
     * @see .onAttachedToWindow
     */
    override fun onDetachedFromWindow() {
        onViewRecycled()
        super.onDetachedFromWindow()
    }
}
