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
import co.anitrend.data.genre.entity.GenreEntity
import co.anitrend.data.media.entity.MediaEntity
import co.anitrend.data.core.common.Identity
import co.anitrend.support.query.builder.annotation.EntitySchema

@Entity(
    tableName = "genre_connection",
    indices = [
        Index(
            value = ["media_id", "genre"],
            unique = true
        ),
    ],
    foreignKeys = [
        ForeignKey(
            entity = MediaEntity::class,
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
            childColumns = ["media_id"],
            parentColumns = ["id"],
            deferred = true
        ),
        ForeignKey(
            entity = GenreEntity::class,
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
            childColumns = ["genre"],
            parentColumns = ["genre"],
            deferred = true
        )
    ]
)
@EntitySchema
internal data class GenreConnectionEntity(
    @ColumnInfo(name = "media_id") val mediaId: Long,
    @ColumnInfo(name = "genre") val genre: String,
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) override val id: Long = 0
) : Identity