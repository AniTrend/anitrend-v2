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

package co.anitrend.data.genre.entity

import androidx.room.*
import co.anitrend.data.core.common.Identity
import co.anitrend.support.query.builder.annotation.EntitySchema

@Entity(
    tableName = "genre",
    primaryKeys = ["id"],
    indices = [
        Index(
            value = ["genre", "emoji"],
            unique = true
        )
    ]
)
@EntitySchema
internal data class GenreEntity(
    @ColumnInfo(name = "id") override val id: Long,
    @ColumnInfo(name = "genre") val genre: String,
    @ColumnInfo(name = "emoji") val emoji: String
) : Identity {

    @DatabaseView(
        value = """
            select g.*, c.media_id 
            from genre g
            inner join genre_connection c
            on g.id = c.genre_id
        """,
        viewName = "view_genre_extended"
    )
    data class Extended(
        @ColumnInfo(name = "genre") val genre: String,
        @ColumnInfo(name = "emoji") val emoji: String,
        @ColumnInfo(name = "media_id") val mediaId: Long,
        @ColumnInfo(name = "id") override val id: Long
    ) : Identity
}