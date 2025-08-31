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
package co.anitrend.data.edge.theme.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import co.anitrend.data.core.common.IEntityId
import co.anitrend.data.edge.media.entity.EdgeMediaEntity
import co.anitrend.support.query.builder.annotation.EntitySchema

@Entity(
    tableName = "edge_media_theme_song",
    indices = [
        Index(value = ["media_id", "theme_id"], unique = true),
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
data class EdgeThemeEntity(
    @ColumnInfo(name = "media_id") val mediaId: String,
    @ColumnInfo(name = "theme_id") val themeId: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "audio") val audio: String?,
    @ColumnInfo(name = "video") val video: String,
    @Embedded(prefix = "meta_") val meta: ThemeMeta,
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") override val id: Long = 0,
) : IEntityId<Long> {
    data class ThemeMeta(
        @ColumnInfo(name = "number") val number: Int,
        @ColumnInfo(name = "type") val type: String,
        @ColumnInfo(name = "version") val version: Int,
    )
}
