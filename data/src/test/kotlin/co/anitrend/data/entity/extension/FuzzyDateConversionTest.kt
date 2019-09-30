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

package co.anitrend.data.entity.extension

import co.anitrend.data.extensions.toFuzzyDate
import co.anitrend.domain.entities.common.FuzzyDate
import co.anitrend.domain.common.FuzzyDateInt
import org.junit.Assert
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class FuzzyDateConversionTest {

    @Test
    fun `should produce unknown fuzzy date fields`() {
        /** should produce fuzzy date of [FuzzyDate.UNKNOWN] */
        val fuzzyDateInt : FuzzyDateInt = "0"

        val fuzzyDate = fuzzyDateInt.toFuzzyDate()

        Assert.assertEquals(FuzzyDate.UNKNOWN, fuzzyDate.day)
        Assert.assertEquals(FuzzyDate.UNKNOWN, fuzzyDate.month)
        Assert.assertEquals(FuzzyDate.UNKNOWN, fuzzyDate.year)
    }

    @Test
    fun `should produce fuzzy date with an unknown day field`() {
        val fuzzyDateInt : FuzzyDateInt = "19760500"

        val fuzzyDate = fuzzyDateInt.toFuzzyDate()

        Assert.assertEquals(FuzzyDate.UNKNOWN, fuzzyDate.day)
        Assert.assertEquals(5, fuzzyDate.month)
        Assert.assertEquals(1976, fuzzyDate.year)
    }

    @Test
    fun `should produce fuzzy date with an unknown month field`() {
        val fuzzyDateInt : FuzzyDateInt = "18430010"

        val fuzzyDate = fuzzyDateInt.toFuzzyDate()

        Assert.assertEquals(10, fuzzyDate.day)
        Assert.assertEquals(FuzzyDate.UNKNOWN, fuzzyDate.month)
        Assert.assertEquals(1843, fuzzyDate.year)
    }

    @Test
    fun `should produce fuzzy date with an unknown year field`() {
        val fuzzyDateInt : FuzzyDateInt = "00000810"

        val fuzzyDate = fuzzyDateInt.toFuzzyDate()

        Assert.assertEquals(10, fuzzyDate.day)
        Assert.assertEquals(8, fuzzyDate.month)
        Assert.assertEquals(FuzzyDate.UNKNOWN, fuzzyDate.year)
    }

    @Test
    fun `should produce fuzzy date with no unknown fields`() {
        val fuzzyDateInt : FuzzyDateInt = "20111117"

        val fuzzyDate = fuzzyDateInt.toFuzzyDate()

        Assert.assertEquals(17, fuzzyDate.day)
        Assert.assertEquals(11, fuzzyDate.month)
        Assert.assertEquals(2011, fuzzyDate.year)
    }
}