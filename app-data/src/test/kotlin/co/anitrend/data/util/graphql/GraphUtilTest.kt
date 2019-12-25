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
import co.anitrend.data.settings.ISortOrderSettings
import co.anitrend.domain.medialist.enums.MediaListSort
import co.anitrend.data.util.graphql.GraphUtil.applySortOrderUsing
import co.anitrend.domain.media.enums.MediaSort
import org.junit.Assert
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

    @Test
    fun `sort order utility on enum attaches sort order correctly`() {
        val expected = "ADDED_TIME_DESC"

        val sortType = MediaListSort.ADDED_TIME

        val settings = object : ISortOrderSettings {
            override var isSortOrderDescending: Boolean = true
        }

        val actual = sortType.applySortOrderUsing(settings)

        assertEquals(expected, actual)
    }


    @Test
    fun `sorting helper does not append postfix to ignored values`() {
        val given = object : ISortOrderSettings {
            override var isSortOrderDescending = true
        }

        val expected = "SEARCH_MATCH"

        val actual = MediaSort.SEARCH_MATCH.applySortOrderUsing(
            settings = given
        )

        assertEquals(expected, actual)
    }

    @Test
    fun `sorting helper appends postfix to non ignored values`() {
        val given = object : ISortOrderSettings {
            override var isSortOrderDescending = true
        }

        val expected = "PROGRESS_VOLUMES_DESC"

        val actual = MediaListSort.PROGRESS_VOLUMES.applySortOrderUsing(
            settings = given
        )

        assertEquals(expected, actual)
    }

    @Test
    fun `sorting helper does not append postfix when preference order is not set to descending order`() {
        val given = object : ISortOrderSettings {
            override var isSortOrderDescending = false
        }

        val expected = "PROGRESS_VOLUMES"

        val actual = MediaListSort.PROGRESS_VOLUMES.applySortOrderUsing(
            settings = given
        )

        assertEquals(expected, actual)
    }
}
