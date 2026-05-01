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
package co.anitrend.common.tag.ui.controller.helpers

import androidx.recyclerview.widget.DiffUtil
import co.anitrend.domain.tag.entity.Tag

internal object TagDiffUtil : DiffUtil.ItemCallback<Tag>() {
    override fun areItemsTheSame(
        oldItem: Tag,
        newItem: Tag,
    ) = oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: Tag,
        newItem: Tag,
    ) =
        when {
            oldItem is Tag.Core && newItem is Tag.Core -> oldItem == newItem
            oldItem is Tag.Extended && newItem is Tag.Extended -> oldItem == newItem
            else -> false
        }
}
