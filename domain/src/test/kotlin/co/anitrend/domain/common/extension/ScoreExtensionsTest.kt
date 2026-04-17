/*
 * Copyright (C) 2026 AniTrend
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

import co.anitrend.domain.medialist.enums.ScoreFormat
import org.junit.Assert.assertEquals
import org.junit.Test

class ScoreExtensionsTest {
    @Test
    fun `formats score for point 10 decimal`() {
        assertEquals("8.9/10", 89.asFormattedScore(ScoreFormat.POINT_10_DECIMAL))
    }

    @Test
    fun `formats score for point 5`() {
        assertEquals("4/5", 80.asFormattedScore(ScoreFormat.POINT_5))
    }

    @Test
    fun `formats score for point 3`() {
        assertEquals("2/3", 65.asFormattedScore(ScoreFormat.POINT_3))
    }
}

