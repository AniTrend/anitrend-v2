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
package co.anitrend.common.news.ui.adapter

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import co.anitrend.arch.core.model.IStateLayoutConfig
import co.anitrend.arch.recycler.action.contract.ISupportSelectionMode
import co.anitrend.arch.recycler.model.contract.IRecyclerItem
import co.anitrend.arch.recycler.paging.legacy.adapter.SupportPagedListAdapter
import co.anitrend.arch.theme.animator.ScaleAnimator
import co.anitrend.arch.theme.animator.contract.AbstractAnimator
import co.anitrend.common.news.ui.controller.helpers.NewsDiffUtil
import co.anitrend.common.news.ui.controller.model.NewsItem
import co.anitrend.common.news.ui.controller.model.NewsItem.Companion.createViewHolder
import co.anitrend.domain.news.entity.News

class NewsPagedAdapter(
    override val resources: Resources,
    override val stateConfiguration: IStateLayoutConfig,
    override val customSupportAnimator: AbstractAnimator? = ScaleAnimator(),
    override val mapper: (News) -> IRecyclerItem = { NewsItem(it) },
    override val supportAction: ISupportSelectionMode<Long>? = null,
) : SupportPagedListAdapter<News>(NewsDiffUtil) {
    /**
     * Should provide the required view holder, this function is a substitute for
     * [androidx.recyclerview.widget.RecyclerView.Adapter.onCreateViewHolder] which now
     * has extended functionality
     */
    override fun createDefaultViewHolder(
        parent: ViewGroup,
        viewType: Int,
        layoutInflater: LayoutInflater,
    ) = layoutInflater.createViewHolder(parent)
}
