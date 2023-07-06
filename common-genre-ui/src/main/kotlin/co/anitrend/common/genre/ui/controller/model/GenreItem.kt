/*
 * Copyright (C) 2021  AniTrend
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

package co.anitrend.common.genre.ui.controller.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.anitrend.arch.recycler.action.contract.ISupportSelectionMode
import co.anitrend.arch.recycler.common.ClickableItem
import co.anitrend.arch.recycler.holder.SupportViewHolder
import co.anitrend.common.genre.databinding.GenreItemBinding
import co.anitrend.core.android.extensions.asChoice
import co.anitrend.core.android.helpers.color.asColorInt
import co.anitrend.core.android.recycler.model.RecyclerItemBinding
import co.anitrend.core.android.views.text.TextDrawable
import co.anitrend.domain.genre.entity.Genre
import co.anitrend.navigation.MediaDiscoverRouter
import co.anitrend.navigation.extensions.asNavPayload
import co.anitrend.navigation.extensions.startActivity
import kotlinx.coroutines.flow.MutableStateFlow

internal class GenreItem(
    private val entity: Genre,
    override val supportsSelectionMode: Boolean = true
) : RecyclerItemBinding<GenreItemBinding>(entity.id) {

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
        binding = GenreItemBinding.bind(view)
        val isSelectable = selectionMode != null
        requireBinding().genre.chipIcon = TextDrawable(view.context, entity.emoji)

        if (entity is Genre.Extended && entity.background != null) {
            val background = entity.background!!.asColorInt(view.context)
            requireBinding().genre.setTextColor(background)
        }

        if (isSelectable)
            requireBinding().genre.asChoice()

        requireBinding().genre.text = entity.name

        requireBinding().genre.setOnClickListener {
            if (!isSelectable) {
                MediaDiscoverRouter.startActivity(
                    context = it.context,
                    navPayload = MediaDiscoverRouter.Param(
                        genre = entity.name
                    ).asNavPayload()
                )
            } else {
                selectionMode?.isSelectionClickable(it, decorator, id)
            }
        }
    }

    /**
     * Called when the view needs to be recycled for reuse, clear any held references
     * to objects, stop any asynchronous work, e.t.c
     */
    override fun unbind(view: View) {
        binding?.genre?.setOnClickListener(null)
        super.unbind(view)
    }

    companion object {
        internal fun LayoutInflater.createViewHolder(
            viewGroup: ViewGroup
        ) = GenreItemBinding.inflate(
            this, viewGroup, false
        ).let(::SupportViewHolder)
    }
}
