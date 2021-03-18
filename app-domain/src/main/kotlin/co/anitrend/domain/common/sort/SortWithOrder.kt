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

package co.anitrend.domain.common.sort

import co.anitrend.domain.common.enums.contract.ISortable
import co.anitrend.domain.common.sort.contract.ISortWithOrder
import co.anitrend.domain.common.sort.order.SortOrder

/**
 * An object that holds information for a [ISortable] with a [SortOrder]
 */
data class SortWithOrder<Sortable : ISortable>(
    override val sortable: Sortable,
    override val order: SortOrder
) : ISortWithOrder<Sortable>