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

import co.anitrend.data.arch.common.model.paging.query.PageQuery
import co.anitrend.data.arch.database.settings.ISortOrderSettings
import co.anitrend.data.base.MockQuery
import co.anitrend.data.util.graphql.GraphUtil.applySortOrderUsing
import co.anitrend.data.util.graphql.GraphUtil.toQueryContainerBuilder
import co.anitrend.domain.media.enums.MediaSort
import co.anitrend.domain.medialist.enums.MediaListSort
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test


class GraphUtilTest {
    private val settings = mockk<ISortOrderSettings>()

    @Test
    fun `default query has required map keys and values`() {
        val actual = PageQuery(
            page = 1
        ).toQueryContainerBuilder()

        assertTrue(actual.containsKey("page"))
        assertEquals(1, actual.getVariable("page"))
        assertTrue(actual.containsKey("perPage"))
        assertEquals(GraphUtil.PAGING_LIMIT, actual.getVariable("perPage"))
    }

    @Test
    fun `sort order utility on enum attaches sort order correctly`() {
        val expected = "ADDED_TIME_DESC"

        val sortType = MediaListSort.ADDED_TIME

        every { settings.isSortOrderDescending.value } returns true

        val actual = sortType.applySortOrderUsing(settings)

        assertEquals(expected, actual)
    }


    @Test
    fun `sorting helper does not append postfix to ignored values`() {
        every { settings.isSortOrderDescending.value } returns false

        val expected = "SEARCH_MATCH"

        val actual = MediaSort.SEARCH_MATCH.applySortOrderUsing(
            settings = settings
        )

        assertEquals(expected, actual)
    }

    @Test
    fun `sorting helper appends postfix to non ignored values`() {
        every { settings.isSortOrderDescending.value } returns true

        val expected = "PROGRESS_VOLUMES_DESC"

        val actual = MediaListSort.PROGRESS_VOLUMES.applySortOrderUsing(
            settings = settings
        )

        assertEquals(expected, actual)
    }

    @Test
    fun `sorting helper does not append postfix when preference order is not set to descending order`() {
        every { settings.isSortOrderDescending.value } returns false

        val expected = "PROGRESS_VOLUMES"

        val actual = MediaListSort.PROGRESS_VOLUMES.applySortOrderUsing(
            settings = settings
        )

        assertEquals(expected, actual)
    }

    @Test
    fun `mock query with sorting parameter produces mapped sorting order using sort order settings as descending`() {
        every { settings.isSortOrderDescending.value } returns true

        val input = listOf(MediaSort.SEARCH_MATCH, MediaSort.POPULARITY, MediaSort.TYPE)
        val mockQuery = MockQuery(sort=input)

        val expected = listOf("SEARCH_MATCH", "POPULARITY_DESC", "TYPE_DESC")

        val queryContainerBuilder = mockQuery.toQueryContainerBuilder(settings = settings)
        val actual = queryContainerBuilder.getVariable("sort")

        assertEquals(expected, actual)
    }

    @Test
    fun `mock query with sorting parameter produces mapped sorting order using sort order settings as ascending`() {
        every { settings.isSortOrderDescending.value } returns false

        val input = listOf(MediaSort.SEARCH_MATCH, MediaSort.POPULARITY, MediaSort.TYPE)
        val mockQuery = MockQuery(sort=input)

        val expected = listOf("SEARCH_MATCH", "POPULARITY", "TYPE")

        val queryContainerBuilder = mockQuery.toQueryContainerBuilder(settings = settings)
        val actual = queryContainerBuilder.getVariable("sort")

        assertEquals(expected, actual)
    }
}
