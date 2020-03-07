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

package co.anitrend.data.tag.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import co.anitrend.arch.data.mapper.contract.ISupportMapperHelper
import co.anitrend.domain.common.entity.IEntity
import co.anitrend.domain.tag.entities.Tag

@Entity
data class TagEntity(
    @PrimaryKey
    override val id: Long,
    val name: String,
    val description: String?,
    val category: String?,
    val rank: Int?,
    val isGeneralSpoiler: Boolean,
    val isMediaSpoiler: Boolean,
    val isAdult: Boolean
) : IEntity {

    companion object : ISupportMapperHelper<TagEntity, Tag> {
        /**
         * Transforms the the [source] to the target type
         */
        override fun transform(source: TagEntity) =
            Tag(
                id = source.id,
                name = source.name,
                description = source.description,
                category = source.category,
                rank = source.rank ?: 0,
                isGeneralSpoiler = source.isGeneralSpoiler,
                isMediaSpoiler = source.isMediaSpoiler,
                isAdult = source.isAdult
            )
    }
}