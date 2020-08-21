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

package co.anitrend.domain.common.extension

import co.anitrend.domain.common.entity.shared.FuzzyDate
import org.junit.Assert
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class FuzzyDateLikeConversionTest {

    @Test
    fun `should produce unknown fuzzy date like pattern`() {
        /** new instance of the fuzzy date object should return all fields defaulted to [FuzzyDate.UNKNOWN] */
        val fuzzyDate = FuzzyDate()

        Assert.assertEquals("0", fuzzyDate.toFuzzyDateLike())
    }

    @Test
    fun `should produce fuzzy date like pattern with missing day`() {
        val fuzzyDate = FuzzyDate(
            year = 1976,
            month = 5
        )

        Assert.assertEquals("197605%", fuzzyDate.toFuzzyDateLike())
    }

    @Test
    fun `should produce fuzzy date like pattern with missing month`() {
        val fuzzyDate = FuzzyDate(
            year = 1843,
            day = 10
        )

        Assert.assertEquals("1843%10", fuzzyDate.toFuzzyDateLike())

        val fuzzyDateYear = FuzzyDate(
            year = 1843
        )

        Assert.assertEquals("1843%", fuzzyDateYear.toFuzzyDateLike())
    }

    @Test
    fun `should produce fuzzy date like pattern with missing year`() {
        val fuzzyDate = FuzzyDate(
            month = 8,
            day = 10
        )

        Assert.assertEquals("%0810", fuzzyDate.toFuzzyDateLike())
    }

    @Test
    fun `should produce fuzzy date like pattern with no missing parts`() {
        val fuzzyDate = FuzzyDate(2011, 11, 17)

        Assert.assertEquals("20111117", fuzzyDate.toFuzzyDateLike())
    }
}