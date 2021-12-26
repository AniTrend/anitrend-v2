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

package co.anitrend.data.jikan.media.entity.projection

import androidx.room.Embedded
import androidx.room.Relation
import co.anitrend.data.jikan.author.entity.JikanAuthorEntity
import co.anitrend.data.jikan.licensor.entity.JikanLicensorEntity
import co.anitrend.data.jikan.media.entity.JikanEntity
import co.anitrend.data.jikan.producer.entity.JikanProducerEntity
import co.anitrend.data.jikan.studio.entity.JikanStudioEntity

data class JikanWithConnection(
    @Embedded val entity: JikanEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "jikan_id"
    )
    val authors: List<JikanAuthorEntity> = emptyList(),
    @Relation(
        parentColumn = "id",
        entityColumn = "jikan_id"
    )
    val producers: List<JikanProducerEntity> = emptyList(),
    @Relation(
        parentColumn = "id",
        entityColumn = "jikan_id"
    )
    val licensors: List<JikanLicensorEntity> = emptyList(),
    @Relation(
        parentColumn = "id",
        entityColumn = "jikan_id"
    )
    val studios: List<JikanStudioEntity> = emptyList()
)