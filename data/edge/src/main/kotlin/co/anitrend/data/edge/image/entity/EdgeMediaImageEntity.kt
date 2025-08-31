/*
 * Copyright (C) 2025 AniTrend
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
package co.anitrend.data.edge.image.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import co.anitrend.data.core.common.IEntityId
import co.anitrend.data.edge.media.entity.EdgeMediaEntity
import co.anitrend.support.query.builder.annotation.EntitySchema

@Entity(
    tableName = "edge_media_image",
    indices = [
        Index(value = ["media_id", "url", "type"], unique = true),
        Index(value = ["media_id"]),
    ],
    foreignKeys = [
        ForeignKey(
            entity = EdgeMediaEntity::class,
            parentColumns = ["id"],
            childColumns = ["media_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        ),
    ],
)
@EntitySchema
data class EdgeMediaImageEntity(
    @ColumnInfo(name = "media_id") val mediaId: String,
    @ColumnInfo(name = "type") val type: ImageType,
    @ColumnInfo(name = "url") val url: String,
    @ColumnInfo(name = "height") val height: Int,
    @ColumnInfo(name = "width") val width: Int,
    @ColumnInfo(name = "locale") val locale: String?,
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") override val id: Long = 0,
) : IEntityId<Long> {
    enum class ImageType {
        BACKDROP,
        LOGO,
        POSTER,
    }
}
