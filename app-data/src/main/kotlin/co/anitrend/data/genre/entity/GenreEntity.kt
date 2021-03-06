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

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import co.anitrend.data.shared.common.Identity

@Entity(
    tableName = "genre",
    indices = [
        Index(
            value = ["genre"],
            unique = true
        )
    ]
)
internal data class GenreEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") override val id: Long = 0,
    @ColumnInfo(name = "genre") val genre: String
) : Identity