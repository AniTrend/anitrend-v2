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

package co.anitrend.data.media.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import co.anitrend.arch.data.mapper.contract.ISupportMapperHelper
import co.anitrend.domain.common.entity.IEntity
import co.anitrend.domain.media.entities.Media

@Entity
internal data class MediaEntity(
    @PrimaryKey
    override val id: Long
) : IEntity {

    companion object : ISupportMapperHelper<MediaEntity, Media> {
        /**
         * Transforms the the [source] to the target type
         */
        override fun transform(source: MediaEntity): Media {
            TODO("Not yet implemented")
        }
    }
}