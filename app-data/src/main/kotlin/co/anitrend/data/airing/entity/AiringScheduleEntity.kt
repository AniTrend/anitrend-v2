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

package co.anitrend.data.airing.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import co.anitrend.data.media.entity.MediaEntity
import co.anitrend.data.core.common.Identity
import co.anitrend.support.query.builder.annotation.EntitySchema

@Entity(
    tableName = "airing_schedule",
    primaryKeys = ["id"],
    indices = [
        Index(value = ["media_id"])
    ],
    foreignKeys = [
        ForeignKey(
            entity = MediaEntity::class,
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
            childColumns = ["media_id"],
            parentColumns = ["id"]
        )
    ]
)
@EntitySchema
internal data class AiringScheduleEntity(
    @ColumnInfo(name ="airing_at") val airingAt: Long,
    @ColumnInfo(name ="episode") val episode: Int,
    @ColumnInfo(name ="media_id") val mediaId: Long,
    @ColumnInfo(name ="time_until_airing") val timeUntilAiring: Long,
    @ColumnInfo(name = "id") override val id: Long
) : Identity