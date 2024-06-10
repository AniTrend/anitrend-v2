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

package co.anitrend.media.carousel.component.content

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import co.anitrend.arch.recycler.adapter.SupportAdapter
import co.anitrend.arch.ui.view.widget.model.StateLayoutConfig
import co.anitrend.core.component.content.list.AniTrendListContent
import co.anitrend.domain.carousel.entity.MediaCarousel
import co.anitrend.domain.carousel.model.CarouselParam
import co.anitrend.media.carousel.component.content.controller.CarouselContentController
import co.anitrend.media.carousel.component.viewmodel.CarouselViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CarouselContent(
    private val controller: CarouselContentController,
    override val stateConfig: StateLayoutConfig,
    override val supportViewAdapter: SupportAdapter<MediaCarousel>,
    override val defaultSpanSize: Int = co.anitrend.arch.theme.R.integer.single_list_size
) : AniTrendListContent<MediaCarousel>() {

    private val viewModel by viewModel<CarouselViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val animator = object : DefaultItemAnimator() {
            override fun canReuseUpdatedViewHolder(
                viewHolder: RecyclerView.ViewHolder
            ) = false
        }
        animator.supportsChangeAnimations = false
        listPresenter.recyclerView.itemAnimator = animator
    }

    /**
     * Stub to trigger the loading of data, by default this is only called
     * when [supportViewAdapter] has no data in its underlying source.
     *
     * This is called when the fragment reaches it's [onStart] state
     *
     * @see initializeComponents
     */
    override fun onFetchDataInitialize() {
        val mediaCarouselQuery = CarouselParam.Find(
            season = controller.season,
            seasonYear = controller.year,
            nextSeasonYear = controller.nextSeasonYear,
            nextSeason = controller.nextSeason,
            currentTime = controller.currentTimeAsEpoch(),
            pageSize = controller.pageSize(resources,4)
        )
        viewModel(mediaCarouselQuery)
    }

    /**
     * Invoke view model observer to watch for changes, this will be called
     * called in [onViewCreated]
     */
    override fun setUpViewModelObserver() {
        viewModelState().model.observe(viewLifecycleOwner) {
            onPostModelChange(it)
        }
    }

    /**
     * Proxy for a view model state if one exists
     */
    override fun viewModelState() = viewModel.state
}
