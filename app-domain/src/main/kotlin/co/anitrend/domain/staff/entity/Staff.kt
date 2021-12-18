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

package co.anitrend.domain.staff.entity

import co.anitrend.domain.common.entity.shared.CoverImage
import co.anitrend.domain.common.entity.shared.CoverName
import co.anitrend.domain.common.entity.shared.FuzzyDate
import co.anitrend.domain.staff.entity.contract.IStaff
import co.anitrend.domain.staff.enums.StaffLanguage

sealed class Staff : IStaff {

    abstract val age: Int?
    abstract val dateOfBirth: FuzzyDate?
    abstract val dateOfDeath: FuzzyDate?
    abstract val gender: String?
    abstract val homeTown: String?
    abstract val bloodType: String?
    abstract val primaryOccupations: List<String>
    abstract val yearsActive: ActiveYearPeriod

    data class ActiveYearPeriod(
        val start: Int?,
        val end: Int?
    )

    data class Core(
        override val age: Int?,
        override val dateOfBirth: FuzzyDate?,
        override val dateOfDeath: FuzzyDate?,
        override val gender: String?,
        override val homeTown: String?,
        override val bloodType: String?,
        override val primaryOccupations: List<String>,
        override val yearsActive: ActiveYearPeriod,
        override val description: String?,
        override val favourites: Int,
        override val image: CoverImage?,
        override val isFavourite: Boolean,
        override val isFavouriteBlocked: Boolean,
        override val language: StaffLanguage?,
        override val name: CoverName?,
        override val siteUrl: String?,
        override val id: Long
    ) : Staff()

    data class Extended(
        override val age: Int?,
        override val dateOfBirth: FuzzyDate?,
        override val dateOfDeath: FuzzyDate?,
        override val gender: String?,
        override val homeTown: String?,
        override val bloodType: String?,
        override val primaryOccupations: List<String>,
        override val yearsActive: ActiveYearPeriod,
        override val description: String?,
        override val favourites: Int,
        override val image: CoverImage?,
        override val isFavourite: Boolean,
        override val isFavouriteBlocked: Boolean,
        override val language: StaffLanguage?,
        override val name: CoverName?,
        override val siteUrl: String?,
        override val id: Long
    ) : Staff()
}
