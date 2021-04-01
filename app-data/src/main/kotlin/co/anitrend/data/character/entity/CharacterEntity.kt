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

package co.anitrend.data.character.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import co.anitrend.data.core.common.Identity

@Entity(
    tableName = "character",
    primaryKeys = ["id"]
)
internal data class CharacterEntity(
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "favourites") val favourites: Int,
    @Embedded(prefix = "image_") val image: CoverImage,
    @ColumnInfo(name = "isFavourite") val isFavourite: Boolean,
    @Embedded(prefix = "name_") val name: Name,
    @ColumnInfo(name = "site_url") val siteUrl: String,
    @ColumnInfo(name = "updated_at") val updatedAt: Long?,
    @ColumnInfo(name = "id") override val id: Long
) : Identity {

    data class CoverImage(
        @ColumnInfo(name = "large") val large: String? = null,
        @ColumnInfo(name = "medium") val medium: String? = null
    )

    data class Name(
        @ColumnInfo(name = "alternative") var alternatives: List<String>? = null,
        @ColumnInfo(name = "first") var first: String? = null,
        @ColumnInfo(name = "full") var full: String? = null,
        @ColumnInfo(name = "last") val last: String? = null,
        @ColumnInfo(name = "original") var original: String? = null
    )
}
