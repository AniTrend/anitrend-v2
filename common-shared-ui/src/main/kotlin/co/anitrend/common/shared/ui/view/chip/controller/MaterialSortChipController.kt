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
package co.anitrend.common.shared.ui.view.chip.controller

import co.anitrend.common.shared.ui.view.chip.controller.contract.IMaterialSortChipController
import co.anitrend.common.shared.ui.view.chip.enums.CheckedState
import co.anitrend.domain.common.sort.order.SortOrder
import co.anitrend.navigation.model.sorting.Sorting

internal class MaterialSortChipController : IMaterialSortChipController {
    private var state = CheckedState.NONE

    override fun currentState() = state

    override fun asSortOrder() =
        when (state) {
            CheckedState.ASC -> SortOrder.ASC
            else -> SortOrder.DESC
        }

    override fun isCheckedState() =
        when (state) {
            CheckedState.NONE -> false
            CheckedState.DESC -> true
            CheckedState.ASC -> true
        }

    override fun updateSortOrder(sorting: Sorting<*>) {
        state =
            when (sorting.order) {
                SortOrder.ASC -> CheckedState.ASC
                SortOrder.DESC -> CheckedState.DESC
            }
    }

    override fun toggleCheckedState(): CheckedState {
        val previousState = currentState()
        state =
            when (previousState) {
                CheckedState.NONE -> CheckedState.ASC
                CheckedState.ASC -> CheckedState.DESC
                CheckedState.DESC -> CheckedState.NONE
            }

        return previousState
    }
}
