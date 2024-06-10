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

package co.anitrend.data.common.extension

import co.anitrend.data.common.model.date.FuzzyDateModel
import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.test.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class FuzzyDateIntConversionTest {

    @Test
    fun `should produce fuzzy date int unknown`() {
        /** new instance of the fuzzy date object should return all fields defaulted to [FuzzyDate.UNKNOWN] */
        val fuzzyDate = FuzzyDateModel.empty()

        assertEquals("0", fuzzyDate.toFuzzyDateInt())
    }

    @Test
    fun `should produce fuzzy date int with no day postfix`() {
        val fuzzyDate = FuzzyDateModel.empty().copy(
            year = 1976,
            month = 5
        )

        assertEquals("19760500", fuzzyDate.toFuzzyDateInt())
    }

    @Test
    fun `should produce fuzzy date int with no month`() {
        val fuzzyDate = FuzzyDateModel.empty().copy(
            year = 1843,
            day = 10
        )

        assertEquals("18430010", fuzzyDate.toFuzzyDateInt())
    }

    @Test
    fun `should produce fuzzy date int with no year`() {
        val fuzzyDate = FuzzyDateModel.empty().copy(
            month = 8,
            day = 10
        )

        assertEquals("00000810", fuzzyDate.toFuzzyDateInt())
    }

    @Test
    fun `should produce full fuzzy date int`() {
        val fuzzyDate = FuzzyDateModel(2011, 11, 17)

        assertEquals("20111117", fuzzyDate.toFuzzyDateInt())
    }
}
