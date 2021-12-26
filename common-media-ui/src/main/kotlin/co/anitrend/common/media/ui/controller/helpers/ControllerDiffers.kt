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

package co.anitrend.common.media.ui.controller.helpers

import androidx.recyclerview.widget.DiffUtil
import co.anitrend.domain.carousel.entity.MediaCarousel
import co.anitrend.domain.media.entity.Media


internal object MediaDiffUtil : DiffUtil.ItemCallback<Media>() {
    override fun areItemsTheSame(
        oldItem: Media,
        newItem: Media
    ): Boolean {
        return oldItem.id == newItem.id && oldItem.mediaList?.id == newItem.mediaList?.id
    }

    override fun areContentsTheSame(
        oldItem: Media,
        newItem: Media
    ): Boolean {
        return oldItem.hashCode() == newItem.hashCode()
    }

    /**
     * When [.areItemsTheSame] returns `true` for two items and
     * [.areContentsTheSame] returns false for them, this method is called to
     * get a payload about the change.
     *
     * For example, if you are using DiffUtil with [RecyclerView], you can return the
     * particular field that changed in the item and your
     * [ItemAnimator][RecyclerView.ItemAnimator] can use that
     * information to run the correct animation.
     *
     * Default implementation returns `null`.
     *
     * @see Callback.getChangePayload
     */
    override fun getChangePayload(oldItem: Media, newItem: Media): Any? {
        return if (oldItem.mediaList == newItem.mediaList)
            super.getChangePayload(oldItem, newItem)
        else newItem
    }
}

internal object CarouselDiffUtil : DiffUtil.ItemCallback<MediaCarousel>() {
    override fun areItemsTheSame(
        oldItem: MediaCarousel,
        newItem: MediaCarousel
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: MediaCarousel,
        newItem: MediaCarousel
    ): Boolean {
        return oldItem.hashCode() == newItem.hashCode()
    }

    /**
     * When [.areItemsTheSame] returns `true` for two items and
     * [.areContentsTheSame] returns false for them, this method is called to
     * get a payload about the change.
     *
     * For example, if you are using DiffUtil with [RecyclerView], you can return the
     * particular field that changed in the item and your
     * [ItemAnimator][RecyclerView.ItemAnimator] can use that
     * information to run the correct animation.
     *
     * Default implementation returns `null`.
     *
     * @see Callback.getChangePayload
     */
    override fun getChangePayload(oldItem: MediaCarousel, newItem: MediaCarousel): Any {
        return newItem
    }
}