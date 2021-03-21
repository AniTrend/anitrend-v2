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
import androidx.room.DatabaseView
import androidx.room.Entity
import androidx.room.Index
import co.anitrend.data.shared.common.Identity
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
) : Identity {

    @DatabaseView(
        value = """
            select t.*, c.is_media_spoiler, c.rank, c.media_id 
            from tag t
            inner join tag_connection c 
            on t.id = c.tag_id
        """,
        viewName = "view_tag_extended"
    )
    data class Extended(
        @ColumnInfo(name = "name") val name: String,
        @ColumnInfo(name = "description") val description: String?,
        @ColumnInfo(name = "category") val category: String?,
        @ColumnInfo(name = "rank") val rank: Int,
        @ColumnInfo(name = "is_media_spoiler") val isMediaSpoiler: Boolean,
        @ColumnInfo(name = "is_general_spoiler") val isGeneralSpoiler: Boolean,
        @ColumnInfo(name = "is_adult") val isAdult: Boolean,
        @ColumnInfo(name = "media_id") val mediaId: Long,
        @ColumnInfo(name = "id") override val id: Long
    ) : Identity
}