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
import android.text.SpannableString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import co.anitrend.arch.recycler.action.contract.ISupportSelectionMode
import co.anitrend.arch.recycler.common.ClickType
import co.anitrend.arch.recycler.common.ClickableItem
import co.anitrend.arch.recycler.common.DefaultClickableItem
import co.anitrend.arch.recycler.holder.SupportViewHolder
import co.anitrend.arch.recycler.model.RecyclerItem
import co.anitrend.common.media.ui.R
import co.anitrend.common.media.ui.databinding.MediaItemBinding
import co.anitrend.core.android.helpers.image.model.MediaRequestImage
import co.anitrend.core.android.helpers.image.using
import co.anitrend.core.android.recycler.model.RecyclerItemBinding
import co.anitrend.domain.media.entity.base.IMedia
import co.anitrend.domain.media.entity.base.IMediaExtendedWithMediaList
import coil.request.Disposable
import kotlinx.coroutines.flow.MutableStateFlow

internal class MediaItem(
    val entity: IMedia
) : RecyclerItemBinding<MediaItemBinding>(entity.id) {

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
        stateFlow: MutableStateFlow<ClickableItem?>,
        selectionMode: ISupportSelectionMode<Long>?
    ) {
        binding = MediaItemBinding.bind(view)
        disposable = requireBinding().mediaImage.using(
            MediaRequestImage(entity.image, MediaRequestImage.ImageType.POSTER)
        )
        entity as IMediaExtendedWithMediaList
        requireBinding().mediaRatingWidget.setupUsingMedia(entity, true)
        requireBinding().mediaSubTitleWidget.setUpSubTitle(entity)
        requireBinding().mediaStatusWidget.setBackgroundUsing(entity.status)
        requireBinding().mediaScheduleTitleWidget.setUpAiringSchedule(entity)
        requireBinding().mediaTitle.text = SpannableString(entity.title.userPreferred)
        requireBinding().mediaCardContainer.setOnClickListener {
            Toast.makeText(view.context, "Opens media screen", Toast.LENGTH_SHORT).show()
        }
        requireBinding().mediaCardContainer.setOnLongClickListener {
            stateFlow.value =
                DefaultClickableItem(
                    clickType = ClickType.LONG,
                    data = entity,
                    view = view
                )
            Toast.makeText(view.context, "Opens media list bottom dialog", Toast.LENGTH_SHORT).show()
            true
        }
    }

    /**
     * Called when the view needs to be recycled for reuse, clear any held references
     * to objects, stop any asynchronous work, e.t.c
     */
    override fun unbind(view: View) {
        binding?.mediaCardContainer?.setOnLongClickListener(null)
        binding?.mediaCardContainer?.setOnClickListener(null)
        disposable?.dispose()
        disposable = null
        super.unbind(view)
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
    ) = resources.getInteger(R.integer.grid_list_x3)

    companion object {
        internal fun LayoutInflater.createMediaItemViewHolder(
            viewGroup: ViewGroup
        ) = MediaItemBinding.inflate(
            this, viewGroup, false
        ).let { SupportViewHolder(it.root) }
    }
}