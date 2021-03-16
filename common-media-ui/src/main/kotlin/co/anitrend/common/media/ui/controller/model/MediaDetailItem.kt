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

import android.text.SpannableString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import co.anitrend.arch.recycler.action.contract.ISupportSelectionMode
import co.anitrend.arch.recycler.common.ClickableItem
import co.anitrend.arch.recycler.holder.SupportViewHolder
import co.anitrend.common.media.ui.databinding.MediaDetailItemBinding
import co.anitrend.common.media.ui.databinding.MediaListItemBinding
import co.anitrend.core.android.R
import co.anitrend.core.android.helpers.image.model.MediaRequestImage
import co.anitrend.core.android.helpers.image.using
import co.anitrend.core.android.recycler.model.RecyclerItemBinding
import co.anitrend.data.user.settings.IUserSettings
import co.anitrend.domain.media.entity.Media
import co.anitrend.navigation.MediaRouter
import co.anitrend.navigation.extensions.asNavPayload
import co.anitrend.navigation.extensions.startActivity
import coil.request.Disposable
import coil.transform.RoundedCornersTransformation
import kotlinx.coroutines.flow.MutableStateFlow

internal data class MediaDetailItem(
    private val entity: Media,
    private val settings: IUserSettings
) : RecyclerItemBinding<MediaDetailItemBinding>(entity.id) {

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
        binding = MediaDetailItemBinding.bind(view)
        val radius = view.resources.getDimensionPixelSize(R.dimen.lg_margin).toFloat()
        disposable = requireBinding().mediaImage.using(
            MediaRequestImage(entity.image, MediaRequestImage.ImageType.POSTER),
            listOf(
                RoundedCornersTransformation(
                    topRight = radius,
                    bottomRight = radius
                )
            )
        )
        requireBinding().mediaRatingWidget.setupUsingMedia(
            media = entity,
            settings = settings,
            tintColor = R.color.colorOnBackground
        )
        requireBinding().mediaSubTitleWidget.setUpSubTitle(entity)
        requireBinding().mediaStatusWidget.setBackgroundUsing(entity.status)
        requireBinding().mediaScheduleTitleWidget.setUpAiringSchedule(entity)
        requireBinding().mediaTitle.text = SpannableString(entity.title.userPreferred)
        requireBinding().mediaCardContainer.setOnClickListener {
            MediaRouter.startActivity(
                context = it.context,
                navPayload = MediaRouter.Param(
                    id = entity.id,
                    type = entity.category.type
                ).asNavPayload()
            )
        }
        requireBinding().mediaCardContainer.setOnLongClickListener {
            Toast.makeText(view.context, "Opens media list bottom dialog", Toast.LENGTH_SHORT)
                .show()
            true
        }
    }

    /**
     * Called when the view needs to be recycled for reuse, clear any held references
     * to objects, stop any asynchronous work, e.t.c
     */
    override fun unbind(view: View) {
        disposable?.dispose()
        disposable = null
        super.unbind(view)
    }

    companion object {
        internal fun LayoutInflater.createDetailViewHolder(
            viewGroup: ViewGroup
        ) = MediaDetailItemBinding.inflate(
            this, viewGroup, false
        ).let { SupportViewHolder(it.root) }
    }
}