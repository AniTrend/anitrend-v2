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

package co.anitrend.data.tag.entity.connection

import androidx.room.*
import co.anitrend.data.media.entity.MediaEntity
import co.anitrend.data.shared.common.Identity
import co.anitrend.data.tag.entity.TagEntity
import co.anitrend.support.query.builder.annotation.EntitySchema

@Entity(
    tableName = "tag_connection",
    indices = [
        Index(value = ["tag_id"]),
        Index(value = ["media_id"])
    ],
    foreignKeys = [
        ForeignKey(
            entity = TagEntity::class,
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
            childColumns = ["tag_id"],
            parentColumns = ["id"]
        ),
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
internal data class TagConnectionEntity(
    @ColumnInfo(name = "rank") val rank: Int,
    @ColumnInfo(name = "is_media_spoiler") val isMediaSpoiler: Boolean,
    @ColumnInfo(name = "media_id") val mediaId: Long,
    @ColumnInfo(name = "tag_id") val tagId: Long,
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) override val id: Long = 0
) : Identity