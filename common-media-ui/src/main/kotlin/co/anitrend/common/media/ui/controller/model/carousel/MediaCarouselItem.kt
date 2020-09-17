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
import co.anitrend.arch.recycler.action.contract.ISupportSelectionMode
import co.anitrend.arch.recycler.adapter.contract.ISupportAdapter.Companion.FULL_SPAN_SIZE
import co.anitrend.arch.recycler.common.ClickableItem
import co.anitrend.arch.recycler.holder.SupportViewHolder
import co.anitrend.arch.recycler.model.RecyclerItem
import co.anitrend.arch.ui.extension.setUpWith
import co.anitrend.arch.ui.view.widget.model.StateLayoutConfig
import co.anitrend.common.media.ui.R
import co.anitrend.common.media.ui.databinding.MediaCarouselItemBinding
import co.anitrend.common.media.ui.view.MediaItemAdapter
import co.anitrend.data.airing.model.query.AiringScheduleQuery
import co.anitrend.data.media.model.query.MediaQuery
import co.anitrend.domain.airing.enums.AiringSort
import co.anitrend.domain.common.entity.contract.IEntity
import co.anitrend.domain.common.graph.IGraphPayload
import co.anitrend.domain.media.entity.MediaCarousel
import co.anitrend.domain.media.entity.base.IMedia
import co.anitrend.domain.media.enums.MediaSort
import co.anitrend.domain.media.enums.MediaType
import kotlinx.coroutines.flow.MutableStateFlow
import timber.log.Timber

internal class MediaCarouselItem(
    val entity: IEntity?
) : RecyclerItem(entity?.id) {

    private var binding: MediaCarouselItemBinding? = null

    private fun setUpCarouselItems(view: View, mediaItems: List<IMedia>) {
        val mediaItemAdapter = MediaItemAdapter(
            view.resources,
            stateConfiguration = StateLayoutConfig()
        )

        val animator = object : DefaultItemAnimator() {
            override fun getSupportsChangeAnimations() = false
        }
        animator.supportsChangeAnimations = false

        binding?.mediaCarouselRecycler?.itemAnimator = animator
        binding?.mediaCarouselRecycler?.setUpWith(
            supportAdapter = mediaItemAdapter,
            recyclerLayoutManager = LinearLayoutManager(
                view.context, LinearLayoutManager.HORIZONTAL, false
            ).apply {
                // allow prefetching to speed up recycler performance
                isItemPrefetchEnabled = true
                initialPrefetchItemCount = 5
            }
        )
        mediaItemAdapter.submitList(mediaItems)
    }

    private fun setUpHeadings(view: View, carousel: MediaCarousel) {
        val query: IGraphPayload = when (carousel.carouselType) {
            MediaCarousel.CarouselType.AIRING_SOON -> {
                binding?.mediaCarouselTitle?.setText(R.string.label_carousel_airing_anime)
                binding?.mediaCarouselSubTitle?.setText(R.string.label_carousel_airing_anime_description)
                AiringScheduleQuery(
                    notYetAired = false,
                    sort = listOf(AiringSort.TIME)
                )
            }
            MediaCarousel.CarouselType.ALL_TIME_POPULAR -> {
                if (carousel.mediaType == MediaType.ANIME) {
                    binding?.mediaCarouselTitle?.setText(R.string.label_carousel_popular_anime)
                    binding?.mediaCarouselSubTitle?.setText(R.string.label_carousel_popular_anime_description)
                } else {
                    binding?.mediaCarouselTitle?.setText(R.string.label_carousel_popular_manga)
                    binding?.mediaCarouselSubTitle?.setText(R.string.label_carousel_popular_manga_description)
                }
                MediaQuery(
                    type = carousel.mediaType,
                    sort = listOf(MediaSort.POPULARITY)
                )
            }
            MediaCarousel.CarouselType.TRENDING_RIGHT_NOW -> {
                if (carousel.mediaType == MediaType.ANIME) {
                    binding?.mediaCarouselTitle?.setText(R.string.label_carousel_trending_anime)
                    binding?.mediaCarouselSubTitle?.setText(R.string.label_carousel_trending_anime_description)
                } else {
                    binding?.mediaCarouselTitle?.setText(R.string.label_carousel_trending_manga)
                    binding?.mediaCarouselSubTitle?.setText(R.string.label_carousel_trending_manga_description)

                }
                MediaQuery(
                    type = carousel.mediaType,
                    sort = listOf(MediaSort.TRENDING)
                )
            }
            MediaCarousel.CarouselType.POPULAR_THIS_SEASON -> {
                binding?.mediaCarouselTitle?.setText(R.string.label_carousel_popular_season_anime)
                binding?.mediaCarouselSubTitle?.setText(R.string.label_carousel_popular_season_anime_description)
                MediaQuery(
                    type = carousel.mediaType,
                    sort = listOf(MediaSort.POPULARITY),
                    season = carousel.mediaItems.first().season,
                    seasonYear = carousel.mediaItems.first().startDate.year,
                )
            }
            MediaCarousel.CarouselType.RECENTLY_ADDED -> {
                if (carousel.mediaType == MediaType.ANIME) {
                    binding?.mediaCarouselTitle?.setText(R.string.label_carousel_recently_added_anime)
                    binding?.mediaCarouselSubTitle?.setText(R.string.label_carousel_recently_added_anime_description)
                } else {
                    binding?.mediaCarouselTitle?.setText(R.string.label_carousel_recently_added_manga)
                    binding?.mediaCarouselSubTitle?.setText(R.string.label_carousel_recently_added_manga_description)
                }
                MediaQuery(
                        type = carousel.mediaType,
                    sort = listOf(MediaSort.ID)
                )
            }
            MediaCarousel.CarouselType.ANTICIPATED_NEXT_SEASON -> {
                binding?.mediaCarouselTitle?.setText(R.string.label_carousel_anticipated_anime)
                binding?.mediaCarouselSubTitle?.setText(R.string.label_carousel_anticipated_anime_description)
                MediaQuery(
                    type = carousel.mediaType,
                    sort = listOf(MediaSort.POPULARITY),
                    season = carousel.mediaItems.first().season,
                    seasonYear = carousel.mediaItems.first().startDate.year,
                )
            }
            MediaCarousel.CarouselType.POPULAR_MANHWA -> {
                binding?.mediaCarouselTitle?.setText(R.string.label_carousel_popular_manhwa)
                binding?.mediaCarouselSubTitle?.setText(R.string.label_carousel_popular_manhwa_description)
                MediaQuery(
                    type = carousel.mediaType,
                    sort = listOf(MediaSort.POPULARITY),
                    countryOfOrigin = carousel.mediaItems.first().origin

                )
            }
        }
        binding?.mediaCarouselAction?.setOnClickListener {
            // TODO: Opens media discover/search screen and provides media query as parameter for the appropriate action
            // Media.startActivity(view.context, NavPayload(MediaQuery.TAG, query))
            Toast.makeText(view.context, "Opens media discover/search screen", Toast.LENGTH_SHORT).show()
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
        if (entity == null) return
        binding = MediaCarouselItemBinding.bind(view)
        entity as MediaCarousel
        setUpHeadings(view, entity)
        setUpCarouselItems(view, entity.mediaItems)
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
        binding = null
    }

    companion object {
        internal fun LayoutInflater.createCarouselViewHolder(
            viewGroup: ViewGroup
        ) = MediaCarouselItemBinding.inflate(
            this, viewGroup, false
        ).let { SupportViewHolder(it.root) }
    }
}