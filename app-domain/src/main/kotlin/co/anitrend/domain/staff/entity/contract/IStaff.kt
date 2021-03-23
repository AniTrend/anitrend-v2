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

package co.anitrend.domain.staff.entity.contract

import co.anitrend.domain.common.entity.contract.IEntity
import co.anitrend.domain.common.entity.contract.IFavourable
import co.anitrend.domain.common.entity.contract.ISynopsis
import co.anitrend.domain.common.entity.shared.CoverImage
import co.anitrend.domain.common.entity.shared.CoverName
import co.anitrend.domain.staff.enums.StaffLanguage

interface IStaff : IEntity, IFavourable, ISynopsis {
    val image: CoverImage?
    val language: StaffLanguage?
    val name: CoverName?
    val siteUrl: String?
    val updatedAt: Long?
}