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

package co.anitrend.data.jikan.studio.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import co.anitrend.data.jikan.contract.JikanEntityAttribute
import co.anitrend.data.jikan.media.entity.JikanEntity

@Entity(
    tableName = "jikan_studio",
    primaryKeys = ["id"],
    indices = [
        Index(value = ["jikan_id"])
    ],
    foreignKeys = [
        ForeignKey(
            entity = JikanEntity::class,
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
            childColumns = ["jikan_id"],
            parentColumns = ["id"],
            deferred = true
        )
    ]
)
data class JikanStudioEntity(
    @ColumnInfo(name = "jikan_id") override val jikanId: Long,
    @ColumnInfo(name = "type") override val type: String?,
    @ColumnInfo(name = "name") override val name: String?,
    @ColumnInfo(name = "id") override val id: Long
) : JikanEntityAttribute