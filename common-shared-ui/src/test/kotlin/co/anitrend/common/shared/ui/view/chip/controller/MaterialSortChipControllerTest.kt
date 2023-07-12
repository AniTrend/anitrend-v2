/*
 * Copyright (C) 2022  AniTrend
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

import co.anitrend.common.shared.ui.view.chip.enums.CheckedState
import co.anitrend.domain.common.sort.order.SortOrder
import co.anitrend.domain.media.enums.MediaSort
import co.anitrend.navigation.model.sorting.Sorting
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MaterialSortChipControllerTest {

    @Test
    fun `controller checked state should be false with default instance`() {
        val controller = MaterialSortChipController()
        assertFalse(controller.isCheckedState())
    }

    @Test
    fun `controller checked state should be true when sorting of desc order`() {
        val controller = MaterialSortChipController()
        val sorting = Sorting(
            sortable = MediaSort.CHAPTERS,
            order = SortOrder.DESC
        )
        controller.updateSortOrder(sorting)
        assertTrue(controller.isCheckedState())
    }

    @Test
    fun `controller checked state should be true when sorting of asc order`() {
        val controller = MaterialSortChipController()
        val sorting = Sorting(
            sortable = MediaSort.CHAPTERS,
            order = SortOrder.ASC
        )
        controller.updateSortOrder(sorting)
        assertTrue(controller.isCheckedState())
    }

    @Test
    fun `controller checked shows progression from default to asc order`() {
        val controller = MaterialSortChipController()

        val previousState = controller.toggleCheckedState()

        assertEquals(CheckedState.NONE, previousState)

        assertEquals(CheckedState.ASC, controller.currentState())
    }

    @Test
    fun `controller checked shows progression from asc to desc order`() {
        val controller = MaterialSortChipController()

        controller.updateSortOrder(
            Sorting(
                sortable = MediaSort.CHAPTERS,
                order = SortOrder.ASC
            )
        )

        val previousState = controller.toggleCheckedState()

        assertEquals(CheckedState.ASC, previousState)

        assertEquals(CheckedState.DESC, controller.currentState())
    }

    @Test
    fun `controller checked shows progression from desc to none order`() {
        val controller = MaterialSortChipController()

        controller.updateSortOrder(
            Sorting(
                sortable = MediaSort.CHAPTERS,
                order = SortOrder.DESC
            )
        )

        val previousState = controller.toggleCheckedState()

        assertEquals(CheckedState.DESC, previousState)

        assertEquals(CheckedState.NONE, controller.currentState())
    }

    @Test
    fun `controller toggle state changes progress as expected`() {
        val controller = MaterialSortChipController()

        controller.updateSortOrder(
            Sorting(
                sortable = MediaSort.CHAPTERS,
                order = SortOrder.DESC
            )
        )

        val previousState = controller.toggleCheckedState()

        assertEquals(CheckedState.DESC, previousState)

        assertEquals(CheckedState.NONE, controller.currentState())

        val previousStateNone = controller.toggleCheckedState()

        assertEquals(CheckedState.NONE, previousStateNone)

        assertEquals(CheckedState.ASC, controller.currentState())

        val previousStateAsc = controller.toggleCheckedState()

        assertEquals(CheckedState.ASC, previousStateAsc)

        assertEquals(CheckedState.DESC, controller.currentState())
    }
}
