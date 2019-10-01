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

package co.anitrend.data.model.query

import co.anitrend.data.extensions.toFuzzyDateInt
import co.anitrend.data.arch.common.model.paging.PageQuery
import co.anitrend.domain.common.model.FuzzyDate
import co.anitrend.domain.media.models.query.MediaQuery
import co.anitrend.domain.media.enums.MediaFormat
import co.anitrend.domain.media.enums.MediaSeason
import co.anitrend.domain.media.enums.MediaStatus
import co.anitrend.domain.media.enums.MediaType
import org.junit.Assert.*
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class QueryMappingTest {

    @Test
    fun `mapping of page query has correct keys and values`() {
        val pageQuery = PageQuery(
            page = 1,
            perPage = 15
        )

        val mappedQuery = pageQuery.toMap()

        assertNotNull("Conversion is not null", mappedQuery)

        assertTrue("Map contains key `page`", mappedQuery.containsKey("page"))
        assertTrue("Map contains key `perPage`", mappedQuery.containsKey("perPage"))

        assertEquals(1, mappedQuery["page"])
        assertEquals(15, mappedQuery["perPage"])
    }

    @Test
    fun `mapping of media query has correct keys and values`() {
        val mediaQuery = MediaQuery(
            type = MediaType.ANIME,
            endDate = FuzzyDate(
                year = 2018,
                month = 5
            ).toFuzzyDateInt(),
            season = MediaSeason.FALL,
            format_in = MediaFormat.values().asList(),
            onList = true,
            status_in = listOf(
                MediaStatus.FINISHED,
                MediaStatus.NOT_YET_RELEASED
            )
        )

        val mappedQuery = mediaQuery.toMap()

        assertNotNull("Conversion is not null", mappedQuery)

        assertTrue("Map contains key `type`", mappedQuery.containsKey("type"))
        assertTrue("Map contains key `endDate`", mappedQuery.containsKey("endDate"))
        assertTrue("Map contains key `season`", mappedQuery.containsKey("season"))
        assertTrue("Map contains key `format_in`", mappedQuery.containsKey("format_in"))
        assertTrue("Map contains key `onList`", mappedQuery.containsKey("onList"))
        assertTrue("Map contains key `status_in`", mappedQuery.containsKey("status_in"))

        assertEquals(MediaType.ANIME, mappedQuery["type"])
        assertEquals("20180500", mappedQuery["endDate"])
        assertEquals(MediaSeason.FALL, mappedQuery["season"])
        assertEquals(listOf(
            MediaFormat.MANGA, MediaFormat.MOVIE, MediaFormat.MUSIC,
            MediaFormat.NOVEL, MediaFormat.ONA, MediaFormat.ONE_SHOT,
            MediaFormat.OVA, MediaFormat.SPECIAL, MediaFormat.TV,
            MediaFormat.TV_SHORT
        ), mappedQuery["format_in"])
        assertEquals(true, mappedQuery["onList"])
        assertEquals(listOf(
            MediaStatus.FINISHED,
            MediaStatus.NOT_YET_RELEASED
        ), mappedQuery["status_in"])
    }
}