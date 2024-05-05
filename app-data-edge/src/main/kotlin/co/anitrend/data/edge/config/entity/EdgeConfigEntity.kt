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
package co.anitrend.data.edge.config.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import co.anitrend.data.core.common.Identity
import co.anitrend.support.query.builder.annotation.EntitySchema

@Entity(
    tableName = "edge_config",
    primaryKeys = ["id"],
)
@EntitySchema
data class EdgeConfigEntity(
    @Embedded(prefix = "settings_") val settings: Settings,
    @Embedded(prefix = "image_") val defaultImage: Image,
    @ColumnInfo(name = "id") override val id: Long,
) : Identity {
    data class Settings(
        @ColumnInfo(name = "analytics_enabled") val analyticsEnabled: Boolean,
    )

    data class Image(
        @ColumnInfo(name = "banner") val banner: String,
        @ColumnInfo(name = "poster") val poster: String,
        @ColumnInfo(name = "loading") val loading: String,
        @ColumnInfo(name = "error") val error: String,
        @ColumnInfo(name = "info") val info: String,
        @ColumnInfo(name = "standard") val standard: String,
    )

    companion object {
        const val DEFAULT_ID: Long = 1
    }
}
