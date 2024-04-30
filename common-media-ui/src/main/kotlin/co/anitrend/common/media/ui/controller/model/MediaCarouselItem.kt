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

package co.anitrend.common.media.ui.controller.model

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import co.anitrend.arch.extension.ext.gone
import co.anitrend.arch.recycler.action.contract.ISupportSelectionMode
import co.anitrend.arch.recycler.adapter.contract.ISupportAdapter.Companion.FULL_SPAN_SIZE
import co.anitrend.arch.recycler.common.ClickableItem
import co.anitrend.arch.recycler.holder.SupportViewHolder
import co.anitrend.arch.ui.view.widget.model.StateLayoutConfig
import co.anitrend.common.media.ui.R
import co.anitrend.common.media.ui.adapter.MediaCompactAdapter
import co.anitrend.common.media.ui.databinding.MediaCarouselItemBinding
import co.anitrend.common.shared.ui.extension.setUpWith
import co.anitrend.core.android.recycler.model.RecyclerItemBinding
import co.anitrend.core.android.settings.Settings
import co.anitrend.domain.airing.enums.AiringSort
import co.anitrend.domain.carousel.entity.MediaCarousel
import co.anitrend.domain.common.sort.order.SortOrder
import co.anitrend.domain.media.enums.MediaSort
import co.anitrend.domain.media.enums.MediaType
import co.anitrend.navigation.AiringRouter
import co.anitrend.navigation.MediaDiscoverRouter
import co.anitrend.navigation.extensions.asNavPayload
import co.anitrend.navigation.extensions.startActivity
import co.anitrend.navigation.model.common.IParam
import co.anitrend.navigation.model.sorting.Sorting
import kotlinx.coroutines.flow.MutableStateFlow
import org.threeten.bp.Instant

internal class MediaCarouselItem(
    private val entity: MediaCarousel,
    private val settings: Settings,
    private val viewPool: RecyclerView.RecycledViewPool
) : RecyclerItemBinding<MediaCarouselItemBinding>(entity.id) {

    private fun setUpCarouselItems(view: View) {
        val mediaItemAdapter = MediaCompactAdapter(
            settings = settings,
            resources = view.resources,
            customSupportAnimator = null,
            stateConfiguration = StateLayoutConfig()
        )
        requireBinding().mediaCarouselRecycler.gone()
        requireBinding().mediaCarouselRecycler.setUpWith(
            supportAdapter = mediaItemAdapter,
            recyclerViewPool = viewPool
        )

        mediaItemAdapter.submitList(entity.mediaItems)
    }

    private fun createQueryParam(): IParam = when (entity.carouselType) {
        MediaCarousel.CarouselType.AIRING_SOON -> {
            binding?.mediaCarouselTitle?.setText(R.string.label_carousel_airing_anime)
            binding?.mediaCarouselSubTitle?.setText(R.string.label_carousel_airing_anime_description)
            AiringRouter.AiringParam(
                airingAt_greater = (Instant.now().epochSecond).toInt(),
                sort = listOf(AiringSort.TIME,).map {
                    Sorting(it, SortOrder.ASC)
                },
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
            MediaDiscoverRouter.MediaDiscoverParam(
                type = entity.mediaType,
                sort = listOf(MediaSort.POPULARITY).map {
                    Sorting(it, SortOrder.DESC)
                },
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
            MediaDiscoverRouter.MediaDiscoverParam(
                type = entity.mediaType,
                sort = listOf(MediaSort.TRENDING).map {
                    Sorting(it, SortOrder.DESC)
                },
            )
        }
        MediaCarousel.CarouselType.POPULAR_THIS_SEASON -> {
            binding?.mediaCarouselTitle?.setText(R.string.label_carousel_popular_season_anime)
            binding?.mediaCarouselSubTitle?.setText(R.string.label_carousel_popular_season_anime_description)
            MediaDiscoverRouter.MediaDiscoverParam(
                type = entity.mediaType,
                sort = listOf(MediaSort.POPULARITY).map {
                    Sorting(it, SortOrder.DESC)
                },
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
            MediaDiscoverRouter.MediaDiscoverParam(
                type = entity.mediaType,
                sort = listOf(MediaSort.ID).map {
                    Sorting(it, SortOrder.DESC)
                },
            )
        }
        MediaCarousel.CarouselType.ANTICIPATED_NEXT_SEASON -> {
            binding?.mediaCarouselTitle?.setText(R.string.label_carousel_anticipated_anime)
            binding?.mediaCarouselSubTitle?.setText(R.string.label_carousel_anticipated_anime_description)
            MediaDiscoverRouter.MediaDiscoverParam(
                type = entity.mediaType,
                sort = listOf(MediaSort.POPULARITY).map {
                    Sorting(it, SortOrder.DESC)
                },
                season = entity.mediaItems.firstOrNull()?.season,
                seasonYear = entity.mediaItems.firstOrNull()?.startDate?.year,
            )
        }
        MediaCarousel.CarouselType.POPULAR_MANHWA -> {
            binding?.mediaCarouselTitle?.setText(R.string.label_carousel_popular_manhwa)
            binding?.mediaCarouselSubTitle?.setText(R.string.label_carousel_popular_manhwa_description)
            MediaDiscoverRouter.MediaDiscoverParam(
                type = entity.mediaType,
                sort = listOf(MediaSort.POPULARITY).map {
                    Sorting(it, SortOrder.DESC)
                },
                countryOfOrigin = entity.mediaItems.firstOrNull()?.countryCode,
            )
        }
    }

    private fun setUpHeadings() {
        val query: IParam = createQueryParam()
        requireBinding().mediaCarouselAction.setOnClickListener {
            when (query) {
                is MediaDiscoverRouter.MediaDiscoverParam -> {
                    MediaDiscoverRouter.startActivity(
                        context = it.context,
                        navPayload = query.asNavPayload()
                    )
                }
                is AiringRouter.AiringParam -> {
                    AiringRouter.startActivity(
                        context = it.context,
                        navPayload = query.asNavPayload()
                    )
                }
            }
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
        stateFlow: MutableStateFlow<ClickableItem>,
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
        ).let(::SupportViewHolder)
    }
}
