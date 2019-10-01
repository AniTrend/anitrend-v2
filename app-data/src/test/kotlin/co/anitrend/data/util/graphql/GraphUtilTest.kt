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

package co.anitrend.data.util.graphql

import co.anitrend.data.arch.common.model.paging.PageQuery
import org.junit.Test

import org.junit.Assert.*

class GraphUtilTest {

    @Test
    fun `default query has required map keys and values`() {
        val input = PageQuery(
            page = 1
        )

        val actual = GraphUtil.getDefaultQuery(input)

        assertTrue(actual.containsKey("page"))
        assertEquals(1, actual.getVariable("page"))
        assertTrue(actual.containsKey("perPage"))
        assertEquals(GraphUtil.PAGING_LIMIT, actual.getVariable("perPage"))
    }
}