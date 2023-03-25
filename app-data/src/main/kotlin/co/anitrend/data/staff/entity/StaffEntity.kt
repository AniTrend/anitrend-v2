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

package co.anitrend.data.staff.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import co.anitrend.data.common.FuzzyDateInt
import co.anitrend.data.core.common.Identity
import co.anitrend.domain.staff.enums.StaffLanguage
import co.anitrend.support.query.builder.annotation.EntitySchema

@Entity(
    tableName = "staff",
    primaryKeys = ["id"]
)
@EntitySchema
internal data class StaffEntity(
    @Embedded(prefix = "attribute_") val attribute: Attribute?,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "favourites") val favourites: Int,
    @Embedded(prefix = "image_") val image: CoverImage,
    @ColumnInfo(name = "is_favourite") val isFavourite: Boolean,
    @ColumnInfo(name = "language") val language: StaffLanguage?,
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
        @ColumnInfo(name = "original") var original: String? = null,
        @ColumnInfo(name = "user_preferred") val userPreferred: String? = null,
    )

    data class Attribute(
        @ColumnInfo(name = "age") val age: Int? = null,
        @ColumnInfo(name = "date_of_birth") val dateOfBirth: FuzzyDateInt? = null,
        @ColumnInfo(name = "date_of_death") val dateOfDeath: FuzzyDateInt? = null,
        @ColumnInfo(name = "gender") val gender: String? = null,
        @ColumnInfo(name = "blood_type") val bloodType: String?,
        @ColumnInfo(name = "home_town") val homeTown: String? = null,
        @ColumnInfo(name = "primary_occupations") val primaryOccupations: List<String>?= null,
        @ColumnInfo(name = "year_active_start") val yearActiveStart: Int? = null,
        @ColumnInfo(name = "year_active_end") val yearActiveEnd: Int? = null,
    )
}