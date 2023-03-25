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

package co.anitrend.common.review.ui.adapter

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import co.anitrend.arch.core.model.IStateLayoutConfig
import co.anitrend.arch.recycler.action.contract.ISupportSelectionMode
import co.anitrend.arch.recycler.adapter.SupportPagedListAdapter
import co.anitrend.arch.recycler.adapter.contract.ISupportAdapter
import co.anitrend.arch.recycler.model.contract.IRecyclerItem
import co.anitrend.arch.theme.animator.ScaleAnimator
import co.anitrend.arch.theme.animator.contract.AbstractAnimator
import co.anitrend.common.review.ui.controller.helpers.ReviewDiffUtil
import co.anitrend.common.review.ui.controller.model.ReviewCompactItem
import co.anitrend.common.review.ui.controller.model.ReviewCompactItem.Companion.createReviewCompatItemViewHolder
import co.anitrend.common.review.ui.controller.model.ReviewDetailedItem
import co.anitrend.common.review.ui.controller.model.ReviewDetailedItem.Companion.createReviewDetailItemViewHolder
import co.anitrend.core.android.settings.Settings
import co.anitrend.data.settings.customize.common.PreferredViewMode
import co.anitrend.domain.review.entity.Review

class ReviewPagedAdapter(
    private val settings: Settings,
    private val preferredViewMode: PreferredViewMode,
    override val resources: Resources,
    override val stateConfiguration: IStateLayoutConfig,
    override var customSupportAnimator: AbstractAnimator? = ScaleAnimator(),
    override val mapper: (Review) -> IRecyclerItem = {
        when (preferredViewMode) {
            PreferredViewMode.DETAILED -> ReviewDetailedItem(it, settings)
            PreferredViewMode.COMPACT -> ReviewCompactItem(it)
            else -> throw UnsupportedOperationException(
                "${preferredViewMode.name} is not supported by this adapter"
            )
        }
    },
    override val supportAction: ISupportSelectionMode<Long>? = null,
) : SupportPagedListAdapter<Review>(ReviewDiffUtil) {

    /**
     * Should provide the required view holder, this function is a substitute for
     * [androidx.recyclerview.widget.RecyclerView.Adapter.onCreateViewHolder] which now
     * has extended functionality
     */
    override fun createDefaultViewHolder(
        parent: ViewGroup,
        viewType: Int,
        layoutInflater: LayoutInflater
    ) = when (viewType) {
        PreferredViewMode.DETAILED.ordinal ->
            layoutInflater.createReviewDetailItemViewHolder(parent)
        else -> layoutInflater.createReviewCompatItemViewHolder(parent)
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
            ISupportAdapter.DEFAULT_VIEW_TYPE -> preferredViewMode.ordinal
            else -> viewType
        }
    }
}