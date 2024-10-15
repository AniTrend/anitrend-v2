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
package co.anitrend.core.android.recycler.selection

import android.view.ActionMode
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import co.anitrend.arch.extension.ext.replaceWith
import co.anitrend.arch.recycler.action.contract.ISupportSelectionMode
import co.anitrend.arch.recycler.action.decorator.ISelectionDecorator

class DefaultSelectionMode : ISupportSelectionMode<Long> {
    private val selections = mutableListOf<Long>()

    /**
     * Clears all selected items in the current context and
     * alternative stops the current action mode [mode]
     *
     * @param mode action mode currently in use
     */
    override fun clearSelection(mode: ActionMode?) {
        selections.clear()
        mode?.finish()
    }

    /**
     * Checks if item exists in the current selection
     */
    override fun containsItem(id: Long): Boolean {
        return selections.contains(id)
    }

    /**
     * Defines whether or not this current object can be consumed as a primary long click,
     * or if in action mode should start the action mode and also select or deselect the item.
     *
     * @param id item that has been clicked
     * @param decorator selection or deselection decorator
     * @param view the view holder parent of the clicked item
     *
     * @return true if in action mode to inform long click listener that the
     * we have consumed the event, otherwise false
     */
    override fun isLongSelectionClickable(
        view: View,
        decorator: ISelectionDecorator,
        id: Long,
    ): Boolean = false

    /**
     * Defines whether or not this current object can be consumed as a primary click action,
     * or if in action mode should be selected or deselected.
     *
     * @param id item that has been clicked
     * @param decorator selection or deselection decorator
     * @param view the view holder parent of the clicked item
     *
     * @return true if not currently in action mode otherwise false
     */
    override fun isSelectionClickable(
        view: View,
        decorator: ISelectionDecorator,
        id: Long,
    ): Boolean {
        if (id != RecyclerView.NO_ID) {
            val exists = containsItem(id)
            if (!exists) {
                selections.add(id)
            } else {
                selections.remove(id)
            }
            decorator.decorateUsing(view, !exists)
            return true
        }
        return false
    }

    /**
     * Selects all defined items and reflects changes on the action mode
     *
     * @param selection items which can be selected
     */
    override fun selectAllItems(selection: List<Long>) {
        selections.replaceWith(selection)
    }

    /**
     * @return list of selected items
     */
    override fun selectedItems(): List<Long> {
        return selections
    }
}
