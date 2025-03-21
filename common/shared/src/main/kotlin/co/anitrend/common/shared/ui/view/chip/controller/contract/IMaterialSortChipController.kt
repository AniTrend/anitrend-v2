/*
 * Copyright (C) 2022 AniTrend
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
package co.anitrend.common.shared.ui.view.chip.controller.contract

import co.anitrend.common.shared.ui.view.chip.enums.CheckedState
import co.anitrend.domain.common.sort.order.SortOrder
import co.anitrend.navigation.model.sorting.Sorting

interface IMaterialSortChipController {
    fun currentState(): CheckedState

    fun asSortOrder(): SortOrder

    fun isCheckedState(): Boolean

    fun updateSortOrder(sorting: Sorting<*>)

    fun toggleCheckedState(): CheckedState
}
