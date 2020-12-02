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

package co.anitrend.common.media.ui.adapter

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import co.anitrend.arch.core.model.IStateLayoutConfig
import co.anitrend.arch.recycler.action.contract.ISupportSelectionMode
import co.anitrend.arch.recycler.adapter.SupportPagedListAdapter
import co.anitrend.arch.recycler.adapter.contract.ISupportAdapter.Companion.DEFAULT_VIEW_TYPE
import co.anitrend.arch.recycler.holder.SupportViewHolder
import co.anitrend.arch.recycler.model.contract.IRecyclerItem
import co.anitrend.arch.theme.animator.ScaleAnimator
import co.anitrend.arch.theme.animator.contract.AbstractAnimator
import co.anitrend.common.media.ui.controller.helpers.MediaDiffUtil
import co.anitrend.common.media.ui.controller.model.MediaDetailItem
import co.anitrend.common.media.ui.controller.model.MediaDetailItem.Companion.createDetailViewHolder
import co.anitrend.common.media.ui.controller.model.MediaGridItem
import co.anitrend.common.media.ui.controller.model.MediaGridItem.Companion.createGridViewHolder
import co.anitrend.common.media.ui.controller.model.MediaListItem
import co.anitrend.common.media.ui.controller.model.MediaListItem.Companion.createListViewHolder
import co.anitrend.core.settings.common.customize.ICustomizationSettings
import co.anitrend.core.settings.common.customize.common.PreferredViewMode
import co.anitrend.domain.media.entity.Media

/**
 * Adapter for paged list
 */
class MediaPagedAdapter(
    private val settings: ICustomizationSettings,
    override val resources: Resources,
    override val stateConfiguration: IStateLayoutConfig,
    override var customSupportAnimator: AbstractAnimator? = ScaleAnimator(),
    override val mapper: (Media) -> IRecyclerItem = {
        when (settings.preferredViewMode.value) {
            PreferredViewMode.DETAILED_LIST -> MediaDetailItem(it)
            PreferredViewMode.SUMMARY_LIST -> MediaListItem(it)
            PreferredViewMode.GRID_LIST -> MediaGridItem(it)
        }
    }
) : SupportPagedListAdapter<Media>(MediaDiffUtil) {

    /**
     * Assigned if the current adapter needs to support action mode
     * TODO: Might add a selection mode later to allow multi-select for batch operations
     */
    override var supportAction: ISupportSelectionMode<Long>? = null

    /**
     * Used to get stable ids for [androidx.recyclerview.widget.RecyclerView.Adapter] but only if
     * [androidx.recyclerview.widget.RecyclerView.Adapter.setHasStableIds] is set to true.
     *
     * The identifiable id of each item should unique, and if non exists
     * then this function should return [androidx.recyclerview.widget.RecyclerView.NO_ID]
     */
    override fun getStableIdFor(item: Media?): Long {
        return item?.id ?: RecyclerView.NO_ID
    }

    /**
     * Should provide the required view holder, this function is a substitute for
     * [androidx.recyclerview.widget.RecyclerView.Adapter.onCreateViewHolder] which now
     * has extended functionality
     */
    override fun createDefaultViewHolder(
        parent: ViewGroup,
        viewType: Int,
        layoutInflater: LayoutInflater
    ): SupportViewHolder {
        return when (viewType) {
            PreferredViewMode.DETAILED_LIST.ordinal ->
                layoutInflater.createDetailViewHolder(parent)
            PreferredViewMode.SUMMARY_LIST.ordinal ->
                layoutInflater.createListViewHolder(parent)
            else ->
                layoutInflater.createGridViewHolder(parent)
        }
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
            DEFAULT_VIEW_TYPE -> settings.preferredViewMode.value.ordinal
            else -> viewType
        }
    }
}