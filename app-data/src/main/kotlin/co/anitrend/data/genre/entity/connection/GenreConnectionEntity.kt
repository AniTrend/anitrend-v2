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

package co.anitrend.data.genre.entity.connection

import androidx.room.*
import co.anitrend.data.core.common.Identity
import co.anitrend.data.genre.entity.GenreEntity
import co.anitrend.data.media.entity.MediaEntity
import co.anitrend.support.query.builder.annotation.EntitySchema

@Entity(
    tableName = "genre_connection",
    indices = [
        Index(value = ["genre_id", "media_id"], unique = true),
        Index(value = ["genre_id"]),
        Index(value = ["media_id"]),
    ],
    foreignKeys = [
        ForeignKey(
            entity = MediaEntity::class,
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
            childColumns = ["media_id"],
            parentColumns = ["id"]
        ),
        ForeignKey(
            entity = GenreEntity::class,
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
            childColumns = ["genre_id"],
            parentColumns = ["id"]
        )
    ]
)
@EntitySchema
internal data class GenreConnectionEntity(
    @ColumnInfo(name = "media_id") val mediaId: Long,
    @ColumnInfo(name = "genre_id") val genreId: Long,
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) override val id: Long = 0
) : Identity