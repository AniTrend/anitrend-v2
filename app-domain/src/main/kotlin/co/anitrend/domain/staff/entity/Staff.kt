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
import co.anitrend.domain.staff.entity.contract.IStaff
import co.anitrend.domain.staff.enums.StaffLanguage

sealed class Staff : IStaff {

    data class Core(
        override val description: String?,
        override val favourites: Int,
        override val image: CoverImage?,
        override val isFavourite: Boolean,
        override val language: StaffLanguage?,
        override val name: CoverName?,
        override val siteUrl: String?,
        override val id: Long
    ) : Staff()

    data class Extended(
        override val description: String?,
        override val favourites: Int,
        override val image: CoverImage?,
        override val isFavourite: Boolean,
        override val language: StaffLanguage?,
        override val name: CoverName?,
        override val siteUrl: String?,
        override val id: Long
    ) : Staff()
}
