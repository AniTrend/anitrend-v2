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
import androidx.recyclerview.widget.RecyclerView.RecycledViewPool
import co.anitrend.arch.core.model.IStateLayoutConfig
import co.anitrend.arch.recycler.action.contract.ISupportSelectionMode
import co.anitrend.arch.recycler.adapter.SupportPagedListAdapter
import co.anitrend.arch.recycler.adapter.contract.ISupportAdapter.Companion.DEFAULT_VIEW_TYPE
import co.anitrend.arch.recycler.holder.SupportViewHolder
import co.anitrend.arch.recycler.model.contract.IRecyclerItem
import co.anitrend.arch.theme.animator.ScaleAnimator
import co.anitrend.arch.theme.animator.contract.AbstractAnimator
import co.anitrend.common.media.ui.controller.helpers.MediaDiffUtil
import co.anitrend.common.media.ui.controller.model.MediaComfortableItem
import co.anitrend.common.media.ui.controller.model.MediaComfortableItem.Companion.createGridViewHolder
import co.anitrend.common.media.ui.controller.model.MediaCompactItem
import co.anitrend.common.media.ui.controller.model.MediaCompactItem.Companion.createMediaItemViewHolder
import co.anitrend.common.media.ui.controller.model.MediaDetailedItem
import co.anitrend.common.media.ui.controller.model.MediaDetailedItem.Companion.createDetailViewHolder
import co.anitrend.common.media.ui.controller.model.MediaSummaryItem
import co.anitrend.common.media.ui.controller.model.MediaSummaryItem.Companion.createListViewHolder
import co.anitrend.core.android.settings.Settings
import co.anitrend.data.settings.customize.common.PreferredViewMode
import co.anitrend.domain.media.entity.Media

/**
 * Adapter for paged list
 */
class MediaPagedAdapter(
    viewPool: RecycledViewPool,
    private val settings: Settings,
    override val resources: Resources,
    override val stateConfiguration: IStateLayoutConfig,
    override var customSupportAnimator: AbstractAnimator? = ScaleAnimator(),
    override val mapper: (Media) -> IRecyclerItem = {
        when (settings.preferredViewMode.value) {
            PreferredViewMode.SUMMARY -> MediaSummaryItem(it, settings, viewPool)
            PreferredViewMode.DETAILED -> MediaDetailedItem(it, settings)
            PreferredViewMode.COMFORTABLE -> MediaComfortableItem(it, settings)
            PreferredViewMode.COMPACT -> MediaCompactItem(it, settings)
        }
    },
    override val supportAction: ISupportSelectionMode<Long>? = null
) : SupportPagedListAdapter<Media>(MediaDiffUtil) {

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
            PreferredViewMode.DETAILED.ordinal ->
                layoutInflater.createDetailViewHolder(parent)
            PreferredViewMode.SUMMARY.ordinal ->
                layoutInflater.createListViewHolder(parent)
            PreferredViewMode.COMFORTABLE.ordinal ->
                layoutInflater.createGridViewHolder(parent)
            else -> layoutInflater.createMediaItemViewHolder(parent)
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