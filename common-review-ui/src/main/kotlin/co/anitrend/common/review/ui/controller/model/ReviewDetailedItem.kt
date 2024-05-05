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
package co.anitrend.common.review.ui.controller.model

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.anitrend.arch.recycler.action.contract.ISupportSelectionMode
import co.anitrend.arch.recycler.common.ClickableItem
import co.anitrend.arch.recycler.holder.SupportViewHolder
import co.anitrend.common.review.databinding.ReviewDetailedItemBinding
import co.anitrend.core.android.R
import co.anitrend.core.android.helpers.image.model.RequestImage
import co.anitrend.core.android.helpers.image.toMediaRequestImage
import co.anitrend.core.android.helpers.image.using
import co.anitrend.core.android.recycler.model.RecyclerItemBinding
import co.anitrend.core.android.settings.Settings
import co.anitrend.domain.review.entity.Review
import coil.request.Disposable
import kotlinx.coroutines.flow.MutableStateFlow

internal class ReviewDetailedItem(
    private val entity: Review,
    private val settings: Settings,
) : RecyclerItemBinding<ReviewDetailedItemBinding>(entity.id) {
    private var disposable: Disposable? = null

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
        selectionMode: ISupportSelectionMode<Long>?,
    ) {
        binding = ReviewDetailedItemBinding.bind(view)
        entity as Review.Extended
        disposable =
            requireBinding().reviewMediaBanner.using(
                entity.media.image.toMediaRequestImage(RequestImage.Media.ImageType.BANNER),
            )
        requireBinding().reviewTextBody.text = entity.summary
        requireBinding().reviewAvatarWidget.updateUsing(entity)
        requireBinding().reviewRatingWidget.updateUsing(entity)
        requireBinding().reviewActionWidget.updateUsing(entity, settings)
        requireBinding().root.setOnClickListener {
            // TODO: Open review reader as a bottom sheet or as a full activity?
            // A full activity might be better to support things like deep linking
        }
    }

    /**
     * Called when the view needs to be recycled for reuse, clear any held references
     * to objects, stop any asynchronous work, e.t.c
     */
    override fun unbind(view: View) {
        binding?.root?.setOnClickListener(null)
        binding?.reviewActionWidget?.onViewRecycled()
        disposable?.dispose()
        disposable = null
        super.unbind(view)
    }

    /**
     * Provides a preferred span size for the item, defaulted to [R.integer.single_list_size]
     *
     * @param spanCount current span count which may also be [INVALID_SPAN_COUNT]
     * @param position position of the current item
     * @param resources optionally useful for dynamic size check with different configurations
     */
    override fun getSpanSize(
        spanCount: Int,
        position: Int,
        resources: Resources,
    ) = resources.getInteger(R.integer.column_x1)

    companion object {
        internal fun LayoutInflater.createReviewDetailItemViewHolder(viewGroup: ViewGroup) =
            ReviewDetailedItemBinding.inflate(
                this,
                viewGroup,
                false,
            ).let(::SupportViewHolder)
    }
}
