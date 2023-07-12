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

package co.anitrend.common.episode.ui.adapter

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import co.anitrend.arch.core.model.IStateLayoutConfig
import co.anitrend.arch.recycler.action.contract.ISupportSelectionMode
import co.anitrend.arch.recycler.model.contract.IRecyclerItem
import co.anitrend.arch.recycler.paging.legacy.adapter.SupportPagedListAdapter
import co.anitrend.arch.theme.animator.ScaleAnimator
import co.anitrend.arch.theme.animator.contract.AbstractAnimator
import co.anitrend.common.episode.ui.controller.helpers.EpisodeDiffUtil
import co.anitrend.common.episode.ui.controller.model.EpisodeItem
import co.anitrend.common.episode.ui.controller.model.EpisodeItem.Companion.createViewHolder
import co.anitrend.domain.episode.entity.Episode

class EpisodePagedAdapter(
    override val resources: Resources,
    override val stateConfiguration: IStateLayoutConfig,
    override val customSupportAnimator: AbstractAnimator? = ScaleAnimator(),
    override val mapper: (Episode) -> IRecyclerItem = { EpisodeItem(it) },
    override val supportAction: ISupportSelectionMode<Long>? = null
) : SupportPagedListAdapter<Episode>(EpisodeDiffUtil) {

    /**
     * Should provide the required view holder, this function is a substitute for
     * [androidx.recyclerview.widget.RecyclerView.Adapter.onCreateViewHolder] which now
     * has extended functionality
     */
    override fun createDefaultViewHolder(
        parent: ViewGroup,
        viewType: Int,
        layoutInflater: LayoutInflater
    ) = layoutInflater.createViewHolder(parent)
}
