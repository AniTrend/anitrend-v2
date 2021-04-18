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
import co.anitrend.data.common.FuzzyDateInt
import co.anitrend.data.core.common.Identity

@Entity(
    tableName = "character",
    primaryKeys = ["id"]
)
internal data class CharacterEntity(
    @ColumnInfo(name = "age") val age: Int?,
    @ColumnInfo(name = "date_of_birth") val dateOfBirth: FuzzyDateInt?,
    @ColumnInfo(name = "gender") val gender: String?,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "favourites") val favourites: Int,
    @Embedded(prefix = "image_") val image: CoverImage,
    @ColumnInfo(name = "isFavourite") val isFavourite: Boolean,
    @ColumnInfo(name = "isFavouriteBlocked") val isFavouriteBlocked: Boolean,
    @Embedded(prefix = "name_") val name: Name,
    @ColumnInfo(name = "site_url") val siteUrl: String,
    @ColumnInfo(name = "id") override val id: Long
) : Identity {

    data class CoverImage(
        @ColumnInfo(name = "large") val large: String? = null,
        @ColumnInfo(name = "medium") val medium: String? = null
    )

    data class Name(
        @ColumnInfo(name = "alternative") val alternatives: List<String>? = null,
        @ColumnInfo(name = "alternative_spoiler") val alternativeSpoiler: List<String>? = null,
        @ColumnInfo(name = "first") val first: String? = null,
        @ColumnInfo(name = "full") val full: String? = null,
        @ColumnInfo(name = "last") val last: String? = null,
        @ColumnInfo(name = "middle") val middle: String? = null,
        @ColumnInfo(name = "original") val original: String? = null
    )
}
