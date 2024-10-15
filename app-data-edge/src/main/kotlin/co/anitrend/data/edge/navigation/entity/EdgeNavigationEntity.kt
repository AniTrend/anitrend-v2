/*
 * Copyright (C) 2024 AniTrend
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
package co.anitrend.data.edge.navigation.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import co.anitrend.data.core.common.Identity
import co.anitrend.support.query.builder.annotation.EntitySchema

@Entity(
    tableName = "edge_navigation",
    primaryKeys = ["id"],
)
@EntitySchema
data class EdgeNavigationEntity(
    @ColumnInfo(name = "destination") val destination: String,
    @ColumnInfo(name = "label") val label: String,
    @ColumnInfo(name = "icon") val icon: String,
    @Embedded(prefix = "group_") val group: Group,
    @ColumnInfo(name = "id") override val id: Long,
) : Identity {
    data class Group(
        @ColumnInfo(name = "authenticated") val authenticated: Boolean,
        @ColumnInfo(name = "label") val label: String,
        @ColumnInfo(name = "id") val id: Long,
    )
}
