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
import android.text.SpannableString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.anitrend.arch.recycler.action.contract.ISupportSelectionMode
import co.anitrend.arch.recycler.common.ClickableItem
import co.anitrend.arch.recycler.holder.SupportViewHolder
import co.anitrend.common.media.ui.R
import co.anitrend.common.media.ui.controller.extensions.openMediaListSheetFor
import co.anitrend.common.media.ui.controller.extensions.startMediaScreenFor
import co.anitrend.common.media.ui.databinding.MediaCompactItemBinding
import co.anitrend.core.android.extensions.dp
import co.anitrend.core.android.helpers.image.model.RequestImage
import co.anitrend.core.android.helpers.image.toMediaRequestImage
import co.anitrend.core.android.helpers.image.using
import co.anitrend.core.android.recycler.model.RecyclerItemBinding
import co.anitrend.core.android.settings.Settings
import co.anitrend.domain.media.entity.Media
import coil.request.Disposable
import coil.transform.RoundedCornersTransformation
import kotlinx.coroutines.flow.MutableStateFlow

internal class MediaCompactItem(
    private val entity: Media,
    private val settings: Settings
) : RecyclerItemBinding<MediaCompactItemBinding>(entity.id) {

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
        selectionMode: ISupportSelectionMode<Long>?
    ) {
        binding = MediaCompactItemBinding.bind(view)
        disposable = requireBinding().mediaImage.using(
            entity.image.toMediaRequestImage(
                RequestImage.Media.ImageType.POSTER
            ),
            listOf(
                RoundedCornersTransformation(
                    bottomLeft = 8f.dp,
                    bottomRight = 8f.dp,
                )
            )
        )
        requireBinding().mediaRatingWidget.setupUsingMedia(entity, settings)
        requireBinding().mediaSubTitleWidget.setUpSubTitle(entity)
        requireBinding().mediaStatusWidget.setBackgroundUsing(entity.status)
        requireBinding().mediaScheduleTitleWidget.setUpAiringSchedule(entity)
        requireBinding().mediaTitle.text = SpannableString(entity.title.userPreferred)
        requireBinding().mediaCardContainer.setOnClickListener {
            it.startMediaScreenFor(entity)
        }
        requireBinding().mediaCardContainer.setOnLongClickListener {
            it.openMediaListSheetFor(entity, settings)
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
    ) = resources.getInteger(R.integer.column_x3)

    companion object {
        internal fun LayoutInflater.createMediaItemViewHolder(
            viewGroup: ViewGroup
        ) = MediaCompactItemBinding.inflate(
            this, viewGroup, false
        ).let(::SupportViewHolder)
    }
}
