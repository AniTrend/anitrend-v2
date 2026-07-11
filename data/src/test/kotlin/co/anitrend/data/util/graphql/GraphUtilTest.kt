/*
 * Copyright (C) 2019 AniTrend
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

import co.anitrend.data.util.GraphUtil.applySortOrderUsing
import co.anitrend.domain.common.sort.SortWithOrder
import co.anitrend.domain.common.sort.order.SortOrder
import co.anitrend.domain.media.enums.MediaSort
import co.anitrend.domain.medialist.enums.MediaListSort
import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.test.Test

class GraphUtilTest {
    @Test
    fun `sort order utility on enum attaches sort order correctly`() {
        val expected = "ADDED_TIME_DESC"

        val sortWithOrder = SortWithOrder(MediaListSort.ADDED_TIME, SortOrder.DESC)

        val actual = sortWithOrder.applySortOrderUsing()

        assertEquals(expected, actual)
    }

    @Test
    fun `sorting helper does not append postfix to ignored values`() {
        val expected = "SEARCH_MATCH"

        val sortWithOrder = SortWithOrder(MediaSort.SEARCH_MATCH, SortOrder.DESC)

        val actual = sortWithOrder.applySortOrderUsing()

        assertEquals(expected, actual)
    }

    @Test
    fun `sorting helper appends postfix to non ignored values`() {
        val expected = "PROGRESS_VOLUMES_DESC"

        val sortWithOrder = SortWithOrder(MediaListSort.PROGRESS_VOLUMES, SortOrder.DESC)

        val actual = sortWithOrder.applySortOrderUsing()

        assertEquals(expected, actual)
    }

    @Test
    fun `sorting helper does not append postfix when preference order is not set to descending order`() {
        val expected = "PROGRESS_VOLUMES"

        val sortWithOrder = SortWithOrder(MediaListSort.PROGRESS_VOLUMES, SortOrder.ASC)

        val actual = sortWithOrder.applySortOrderUsing()

        assertEquals(expected, actual)
    }

}
