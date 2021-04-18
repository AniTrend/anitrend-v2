/*
 * Copyright (C) 2019  AniTrend
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

package co.anitrend.domain.common.entity.shared

import co.anitrend.domain.common.entity.contract.IEntityName

/**
 * Name attributes for various models such as characters and staff/actors
 */
data class CoverName(
    override val middle: String?,
    override val alternativeSpoiler: List<String>,
    override val alternative: List<String>,
    override val first: String?,
    override val full: String?,
    override val last: String?,
    override val native: String?
) : IEntityName