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

package co.anitrend.core.android.helpers.date

import co.anitrend.domain.common.entity.shared.FuzzyDate
import org.junit.Assert.*
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see [Testing documentation](http://d.android.com/tools/testing).
 */
class AniTrendDateHelperTest : KoinTest {

    private val dateHelper by inject<AniTrendDateHelper>()

    @Test
    fun test_convert_from_fuzzy_date_to_formatted_date() {
        val given = FuzzyDate(2021, 7, 4)
        val expected = "Jul 04, 2021"
        val actual = dateHelper.convertToTextDate(given)

        assertEquals(expected, actual)
    }
}