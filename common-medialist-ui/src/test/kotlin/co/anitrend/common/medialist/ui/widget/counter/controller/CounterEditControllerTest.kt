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

package co.anitrend.common.medialist.ui.widget.counter.controller

import co.anitrend.common.medialist.ui.widget.counter.model.CounterEditModel
import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.test.Test

class CounterEditControllerTest {

    @Test
    fun `increment when near model maximum`() {
        val given = CounterEditModel(7, 8)
        val controller = CounterEditController(given)
        controller.incrementCount()
        val expected = 8
        val actual = given.current
        assertEquals(expected, actual)
    }

    @Test
    fun `decrement when approaching zero`() {
        val given = CounterEditModel(1, 8)
        val controller = CounterEditController(given)
        controller.decrementCount()
        val expected = 0
        val actual = given.current
        assertEquals(expected, actual)
    }

    @Test
    fun `increment when equal maximum`() {
        val given = CounterEditModel(8, 8)
        val controller = CounterEditController(given)
        controller.incrementCount()
        val expected = 8
        val actual = given.current
        assertEquals(expected, actual)

    }

    @Test
    fun `decrement when on zero`() {
        val given = CounterEditModel(0, 8)
        val controller = CounterEditController(given)
        controller.decrementCount()
        val expected = 0
        val actual = given.current
        assertEquals(expected, actual)
    }

    @Test
    fun `increment when maximum is zero`() {
        val given = CounterEditModel(5, 0)
        val controller = CounterEditController(given)
        controller.incrementCount()
        val expected = 6
        val actual = given.current
        assertEquals(expected, actual)
    }

    @Test
    fun `decrement when maximum is zero`() {
        val given = CounterEditModel(5, 0)
        val controller = CounterEditController(given)
        controller.decrementCount()
        val expected = 4
        val actual = given.current
        assertEquals(expected, actual)
    }
}
