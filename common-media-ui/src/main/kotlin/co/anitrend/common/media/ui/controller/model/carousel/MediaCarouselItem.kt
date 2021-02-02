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

package co.anitrend.common.media.ui.controller.model.carousel

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.anitrend.arch.domain.entities.NetworkState
import co.anitrend.arch.recycler.action.contract.ISupportSelectionMode
import co.anitrend.arch.recycler.adapter.contract.ISupportAdapter.Companion.FULL_SPAN_SIZE
import co.anitrend.arch.recycler.common.ClickableItem
import co.anitrend.arch.recycler.holder.SupportViewHolder
import co.anitrend.arch.ui.extension.setUpWith
import co.anitrend.arch.ui.view.widget.model.StateLayoutConfig
import co.anitrend.common.media.ui.R
import co.anitrend.common.media.ui.adapter.MediaItemAdapter
import co.anitrend.common.media.ui.databinding.MediaCarouselItemBinding
import co.anitrend.core.android.recycler.model.RecyclerItemBinding
import co.anitrend.data.airing.model.query.AiringScheduleQuery
import co.anitrend.data.media.model.query.MediaQuery
import co.anitrend.domain.airing.enums.AiringSort
import co.anitrend.domain.common.graph.IGraphPayload
import co.anitrend.domain.carousel.entity.MediaCarousel
import co.anitrend.domain.media.entity.Media
import co.anitrend.domain.media.entity.contract.IMedia
import co.anitrend.domain.media.enums.MediaSort
import co.anitrend.domain.media.enums.MediaType
import co.anitrend.navigation.MediaRouter
import co.anitrend.navigation.extensions.startActivity
import co.anitrend.navigation.model.NavPayload
import kotlinx.coroutines.flow.MutableStateFlow

internal class MediaCarouselItem(
    private val entity: MediaCarousel,
    private val viewPool: RecyclerView.RecycledViewPool
) : RecyclerItemBinding<MediaCarouselItemBinding>(entity.id) {

    private fun setUpCarouselItems(view: View) {
        val mediaItemAdapter = MediaItemAdapter(
            view.resources,
            stateConfiguration = StateLayoutConfig()
        )

        val layoutManager = LinearLayoutManager(
            view.context,
            if (entity.mediaItems.isEmpty())
                RecyclerView.VERTICAL
            else RecyclerView.HORIZONTAL,
            false
        )

        with(layoutManager) {
            // allow prefetching to speed up recycler performance
            isItemPrefetchEnabled = true
            initialPrefetchItemCount = 5
            // If the view types are not the same across RecyclerView then it may lead to performance degradation.
            recycleChildrenOnDetach = true
        }

        val animator = object : DefaultItemAnimator() {
            override fun getSupportsChangeAnimations() = false
        }
        animator.supportsChangeAnimations = false

        requireBinding().mediaCarouselRecycler.setRecycledViewPool(viewPool)
        requireBinding().mediaCarouselRecycler.itemAnimator = animator
        requireBinding().mediaCarouselRecycler.setUpWith(
            supportAdapter = mediaItemAdapter,
            recyclerLayoutManager = layoutManager
        )
        if (entity.mediaItems.isEmpty())
            mediaItemAdapter.networkState = NetworkState.Loading
        else
            mediaItemAdapter.submitList(entity.mediaItems)
    }

    private fun setUpHeadings() {
        val query: IGraphPayload = when (entity.carouselType) {
            MediaCarousel.CarouselType.AIRING_SOON -> {
                binding?.mediaCarouselTitle?.setText(R.string.label_carousel_airing_anime)
                binding?.mediaCarouselSubTitle?.setText(R.string.label_carousel_airing_anime_description)
                AiringScheduleQuery(
                    notYetAired = false,
                    sort = listOf(AiringSort.TIME)
                )
            }
            MediaCarousel.CarouselType.ALL_TIME_POPULAR -> {
                if (entity.mediaType == MediaType.ANIME) {
                    binding?.mediaCarouselTitle?.setText(R.string.label_carousel_popular_anime)
                    binding?.mediaCarouselSubTitle?.setText(R.string.label_carousel_popular_anime_description)
                } else {
                    binding?.mediaCarouselTitle?.setText(R.string.label_carousel_popular_manga)
                    binding?.mediaCarouselSubTitle?.setText(R.string.label_carousel_popular_manga_description)
                }
                MediaQuery(
                    type = entity.mediaType,
                    sort = listOf(MediaSort.POPULARITY)
                )
            }
            MediaCarousel.CarouselType.TRENDING_RIGHT_NOW -> {
                if (entity.mediaType == MediaType.ANIME) {
                    binding?.mediaCarouselTitle?.setText(R.string.label_carousel_trending_anime)
                    binding?.mediaCarouselSubTitle?.setText(R.string.label_carousel_trending_anime_description)
                } else {
                    binding?.mediaCarouselTitle?.setText(R.string.label_carousel_trending_manga)
                    binding?.mediaCarouselSubTitle?.setText(R.string.label_carousel_trending_manga_description)

                }
                MediaQuery(
                    type = entity.mediaType,
                    sort = listOf(MediaSort.TRENDING)
                )
            }
            MediaCarousel.CarouselType.POPULAR_THIS_SEASON -> {
                binding?.mediaCarouselTitle?.setText(R.string.label_carousel_popular_season_anime)
                binding?.mediaCarouselSubTitle?.setText(R.string.label_carousel_popular_season_anime_description)
                MediaQuery(
                    type = entity.mediaType,
                    sort = listOf(MediaSort.POPULARITY),
                    season = entity.mediaItems.firstOrNull()?.season,
                    seasonYear = entity.mediaItems.firstOrNull()?.startDate?.year,
                )
            }
            MediaCarousel.CarouselType.RECENTLY_ADDED -> {
                if (entity.mediaType == MediaType.ANIME) {
                    binding?.mediaCarouselTitle?.setText(R.string.label_carousel_recently_added_anime)
                    binding?.mediaCarouselSubTitle?.setText(R.string.label_carousel_recently_added_anime_description)
                } else {
                    binding?.mediaCarouselTitle?.setText(R.string.label_carousel_recently_added_manga)
                    binding?.mediaCarouselSubTitle?.setText(R.string.label_carousel_recently_added_manga_description)
                }
                MediaQuery(
                    type = entity.mediaType,
                    sort = listOf(MediaSort.ID)
                )
            }
            MediaCarousel.CarouselType.ANTICIPATED_NEXT_SEASON -> {
                binding?.mediaCarouselTitle?.setText(R.string.label_carousel_anticipated_anime)
                binding?.mediaCarouselSubTitle?.setText(R.string.label_carousel_anticipated_anime_description)
                MediaQuery(
                    type = entity.mediaType,
                    sort = listOf(MediaSort.POPULARITY),
                    season = entity.mediaItems.firstOrNull()?.season,
                    seasonYear = entity.mediaItems.firstOrNull()?.startDate?.year,
                )
            }
            MediaCarousel.CarouselType.POPULAR_MANHWA -> {
                binding?.mediaCarouselTitle?.setText(R.string.label_carousel_popular_manhwa)
                binding?.mediaCarouselSubTitle?.setText(R.string.label_carousel_popular_manhwa_description)
                MediaQuery(
                    type = entity.mediaType,
                    sort = listOf(MediaSort.POPULARITY),
                    countryOfOrigin = entity.mediaItems.firstOrNull()?.countryCode
                )
            }
        }
        requireBinding().mediaCarouselAction.setOnClickListener {
            // TODO: Opens media discover/search screen and provides media query as parameter for the appropriate action
            when (query) {
                is MediaQuery -> {
                    MediaRouter.startActivity(
                        context = it.context,
                        navPayload = NavPayload(
                            MediaRouter.Param.KEY,
                            query
                        )
                    )
                }
                is AiringScheduleQuery -> {
                    Toast.makeText(it.context, "Opens airing schedule screen", Toast.LENGTH_SHORT).show()
                }
            }
            // Media.startActivity(view.context, NavPayload(MediaQuery.TAG, query))
        }
    }

    /**
     * Called when the [view] needs to be setup, this could be to set click listeners,
     * assign text, load images, e.t.c
     *
     * @param view view that was inflated
     * @param position current position
     * @param payloads optional payloads which maybe empty
     * @param stateFlow observable to broadcast click events
     * @param selectionMode action mode helper or null if none was provided
     */
    override fun bind(
        view: View,
        position: Int,
        payloads: List<Any>,
        stateFlow: MutableStateFlow<ClickableItem?>,
        selectionMode: ISupportSelectionMode<Long>?
    ) {
        binding = MediaCarouselItemBinding.bind(view)
        setUpHeadings()
        setUpCarouselItems(view)
    }

    /**
     * Provides a preferred span size for the item
     *
     * @param spanCount current span count which may also be [INVALID_SPAN_COUNT]
     * @param position position of the current item
     * @param resources optionally useful for dynamic size check with different configurations
     */
    override fun getSpanSize(
        spanCount: Int,
        position: Int,
        resources: Resources
    ): Int = FULL_SPAN_SIZE

    /**
     * Called when the view needs to be recycled for reuse, clear any held references
     * to objects, stop any asynchronous work, e.t.c
     */
    override fun unbind(view: View) {
        binding?.mediaCarouselAction?.setOnClickListener(null)
        super.unbind(view)
    }

    companion object {
        internal fun LayoutInflater.createCarouselViewHolder(
            viewGroup: ViewGroup
        ) = MediaCarouselItemBinding.inflate(
            this, viewGroup, false
        ).let { SupportViewHolder(it.root) }
    }
}