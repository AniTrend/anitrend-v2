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

package co.anitrend.data.media.entity.reference

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "media_genre_ref",
    primaryKeys = ["media_id", "media_genre_id"]
)
internal data class MediaGenreRefEntity(
    @ColumnInfo(name = "media_id") val mediaId: Long,
    @ColumnInfo(name = "media_genre_id") val mediaGenreId: String
)