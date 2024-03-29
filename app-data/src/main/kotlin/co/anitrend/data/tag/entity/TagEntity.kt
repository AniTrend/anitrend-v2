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

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import co.anitrend.data.core.common.Identity
import co.anitrend.support.query.builder.annotation.EntitySchema

@Entity(
    tableName = "tag",
    primaryKeys = ["id"],
    indices = [
        Index(
            value = ["name"],
            unique = true
        ),
        Index(
            value = ["category"],
            unique = false
        )
    ]
)
@EntitySchema
internal data class TagEntity(
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "category") val category: String?,
    @ColumnInfo(name = "is_general_spoiler") val isGeneralSpoiler: Boolean,
    @ColumnInfo(name = "is_adult") val isAdult: Boolean,
    @ColumnInfo(name = "id") override val id: Long
) : Identity