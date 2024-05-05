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

package co.anitrend.data.core.extensions


import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.test.Test

class CommonExtensionsKtTest {
    @Test
    fun `test hash for word action`() {
        val given = "Action"
        val actual = given.toHashId()
        val expected = 1955883606L
        assertEquals(expected, actual)
    }

    @Test
    fun `test hash for word adventure`() {
        val given = "Adventure"
        val actual = given.toHashId()
        val expected = 1309873904L
        assertEquals(expected, actual)
    }

    @Test
    fun `test hash for word comedy`() {
        val given = "Comedy"
        val actual = given.toHashId()
        val expected = 2024011449L
        assertEquals(expected, actual)
    }

    @Test
    fun `test hash for word drama`() {
        val given = "Drama"
        val actual = given.toHashId()
        val expected = 66292295L
        assertEquals(expected, actual)
    }
}
