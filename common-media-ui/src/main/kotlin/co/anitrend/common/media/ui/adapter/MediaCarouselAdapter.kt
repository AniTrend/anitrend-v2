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
import co.anitrend.arch.recycler.adapter.SupportListAdapter
import co.anitrend.arch.recycler.model.contract.IRecyclerItem
import co.anitrend.arch.theme.animator.ScaleAnimator
import co.anitrend.arch.theme.animator.contract.AbstractAnimator
import co.anitrend.common.media.ui.controller.helpers.CarouselDiffUtil
import co.anitrend.common.media.ui.controller.model.MediaCarouselItem
import co.anitrend.common.media.ui.controller.model.MediaCarouselItem.Companion.createCarouselViewHolder
import co.anitrend.core.android.settings.Settings
import co.anitrend.domain.carousel.entity.MediaCarousel

class MediaCarouselAdapter(
    settings: Settings,
    viewPool: RecycledViewPool,
    override val resources: Resources,
    override val stateConfiguration: IStateLayoutConfig,
    override var customSupportAnimator: AbstractAnimator? = ScaleAnimator(),
    override val mapper: (MediaCarousel) -> IRecyclerItem = {
        MediaCarouselItem(it, settings, viewPool)
    },
    override val supportAction: ISupportSelectionMode<Long>? = null
) : SupportListAdapter<MediaCarousel>(CarouselDiffUtil) {

    /**
     * Should provide the required view holder, this function is a substitute for
     * [androidx.recyclerview.widget.RecyclerView.Adapter.onCreateViewHolder] which now
     * has extended functionality
     */
    override fun createDefaultViewHolder(
        parent: ViewGroup,
        viewType: Int,
        layoutInflater: LayoutInflater
    ) = layoutInflater.createCarouselViewHolder(parent)
}