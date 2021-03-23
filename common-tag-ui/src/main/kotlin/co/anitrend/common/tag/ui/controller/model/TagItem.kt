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

package co.anitrend.common.tag.ui.controller.model

import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.core.text.bold
import co.anitrend.arch.recycler.action.contract.ISupportSelectionMode
import co.anitrend.arch.recycler.common.ClickableItem
import co.anitrend.arch.recycler.holder.SupportViewHolder
import co.anitrend.common.tag.R
import co.anitrend.common.tag.databinding.TagItemBinding
import co.anitrend.core.android.getCompatDrawable
import co.anitrend.core.android.helpers.color.asColorInt
import co.anitrend.core.android.recycler.model.RecyclerItemBinding
import co.anitrend.core.extensions.CHARACTER_SEPARATOR
import co.anitrend.domain.tag.entity.Tag
import co.anitrend.navigation.MediaDiscoverRouter
import co.anitrend.navigation.extensions.asNavPayload
import co.anitrend.navigation.extensions.startActivity
import kotlinx.coroutines.flow.MutableStateFlow

internal class TagItem(
    private val entity: Tag
) : RecyclerItemBinding<TagItemBinding>(entity.id) {
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
        binding = TagItemBinding.bind(view)
        val builder = SpannableStringBuilder(entity.name)

        if (entity is Tag.Extended) {
            @ColorInt val colorTint = entity.background?.asColorInt(view.context) ?: 0
            requireBinding().tag.chipIcon =
                view.context.getCompatDrawable(R.drawable.ic_info, colorTint)

            builder.append(" $CHARACTER_SEPARATOR ").bold {
                "${entity.rank}%"
            }
        }
        requireBinding().tag.text = builder

        requireBinding().tag.setOnClickListener {
            MediaDiscoverRouter.startActivity(
                context = it.context,
                navPayload = MediaDiscoverRouter.Param(
                    tag = entity.name
                ).asNavPayload()
            )
        }
    }

    /**
     * Called when the view needs to be recycled for reuse, clear any held references
     * to objects, stop any asynchronous work, e.t.c
     */
    override fun unbind(view: View) {
        binding?.tag?.setOnClickListener(null)
        super.unbind(view)
    }

    companion object {
        internal fun LayoutInflater.createViewHolder(
            viewGroup: ViewGroup
        ) = TagItemBinding.inflate(
            this, viewGroup, false
        ).let { SupportViewHolder(it.root) }
    }
}