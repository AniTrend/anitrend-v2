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
package co.anitrend.navigation.drawer.adapter

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import co.anitrend.arch.core.model.IStateLayoutConfig
import co.anitrend.arch.recycler.action.contract.ISupportSelectionMode
import co.anitrend.arch.recycler.adapter.SupportListAdapter
import co.anitrend.arch.recycler.adapter.contract.ISupportAdapter
import co.anitrend.arch.recycler.model.contract.IRecyclerItem
import co.anitrend.arch.theme.animator.contract.AbstractAnimator
import co.anitrend.navigation.drawer.controller.helpers.NavigationDiffUtil
import co.anitrend.navigation.drawer.controller.model.navigation.DividerNavigationItem
import co.anitrend.navigation.drawer.controller.model.navigation.DividerNavigationItem.Companion.createNavDividerViewHolder
import co.anitrend.navigation.drawer.controller.model.navigation.GroupNavigationItem
import co.anitrend.navigation.drawer.controller.model.navigation.GroupNavigationItem.Companion.createNavGroupViewHolder
import co.anitrend.navigation.drawer.controller.model.navigation.MenuNavigationItem
import co.anitrend.navigation.drawer.controller.model.navigation.MenuNavigationItem.Companion.createNavMenuViewHolder
import co.anitrend.navigation.drawer.model.navigation.Navigation
import co.anitrend.navigation.drawer.model.navigation.Navigation.Companion.toNavType

class NavigationAdapter(
    override val resources: Resources,
    override val stateConfiguration: IStateLayoutConfig,
    override val mapper: (Navigation) -> IRecyclerItem = { navigation ->
        when (navigation) {
            is Navigation.Menu -> MenuNavigationItem(navigation)
            is Navigation.Group -> GroupNavigationItem(navigation)
            is Navigation.Divider -> DividerNavigationItem(navigation)
        }
    },
    override val customSupportAnimator: AbstractAnimator? = null,
    override val supportAction: ISupportSelectionMode<Long>? = null,
) : SupportListAdapter<Navigation>(NavigationDiffUtil, true) {
    /**
     * Used to get stable ids for [androidx.recyclerview.widget.RecyclerView.Adapter] but only if
     * [androidx.recyclerview.widget.RecyclerView.Adapter.setHasStableIds] is set to true.
     *
     * The identifiable id of each item should unique, and if non exists
     * then this function should return [androidx.recyclerview.widget.RecyclerView.NO_ID]
     */
    override fun getStableIdFor(item: Navigation?): Long {
        return item?.hashCode()?.toLong() ?: super.getStableIdFor(item)
    }

    /**
     * Return the view type of the item at [position] for the purposes of view recycling.
     *
     * The default implementation of this method returns 0, making the assumption of
     * a single view type for the adapter. Unlike ListView adapters, types need not
     * be contiguous. Consider using id resources to uniquely identify item view types.
     *
     * @param position position to query
     * @return integer value identifying the type of the view needed to represent the item at
     * [position]. Type codes need not be contiguous.
     */
    override fun getItemViewType(position: Int): Int {
        return when (val viewType = super.getItemViewType(position)) {
            ISupportAdapter.DEFAULT_VIEW_TYPE -> {
                val item = getItem(position)
                item.toNavType() ?: viewType
            }
            else -> viewType
        }
    }

    /**
     * Should provide the required view holder, this function is a substitute for
     * [androidx.recyclerview.widget.RecyclerView.Adapter.onCreateViewHolder] which now
     * has extended functionality
     */
    override fun createDefaultViewHolder(
        parent: ViewGroup,
        viewType: Int,
        layoutInflater: LayoutInflater,
    ) = when (viewType) {
        Navigation.MENU -> layoutInflater.createNavMenuViewHolder(parent)
        Navigation.GROUP -> layoutInflater.createNavGroupViewHolder(parent)
        else -> layoutInflater.createNavDividerViewHolder(parent)
    }
}
