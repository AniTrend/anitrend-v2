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

import co.anitrend.data.common.extension.toFuzzyDateInt
import co.anitrend.data.common.model.date.FuzzyDateModel
import co.anitrend.data.common.model.paging.query.PageQuery
import co.anitrend.data.media.model.query.MediaQuery
import co.anitrend.domain.media.enums.MediaFormat
import co.anitrend.domain.media.enums.MediaSeason
import co.anitrend.domain.media.enums.MediaStatus
import co.anitrend.domain.media.enums.MediaType
import co.anitrend.domain.media.model.MediaParam
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import kotlin.test.Test

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

        assertNotNull(mappedQuery, "Conversion is not null")

        assertTrue(mappedQuery.containsKey("page"), "Map contains key `page`")
        assertTrue(mappedQuery.containsKey("perPage"), "Map contains key `perPage`")

        assertEquals(1, mappedQuery["page"])
        assertEquals(15, mappedQuery["perPage"])
    }

    @Test
    fun `mapping of media query has correct keys and values`() {
        val mediaQuery = MediaQuery.Find(
            MediaParam.Find() builder {
                type = MediaType.ANIME
                endDate = FuzzyDateModel.empty().copy(
                    year = 2018,
                    month = 5
                ).toFuzzyDateInt()
                season = MediaSeason.FALL
                format_in = MediaFormat.entries
                onList = true
                status_in = listOf(
                    MediaStatus.FINISHED,
                    MediaStatus.NOT_YET_RELEASED
                )
            }
        )

        val mappedQuery = mediaQuery.toMap()

        assertNotNull(mappedQuery, "Conversion is not null")

        assertTrue(mappedQuery.containsKey("type"), "Map contains key `type`")
        assertTrue(mappedQuery.containsKey("endDate"), "Map contains key `endDate`")
        assertTrue(mappedQuery.containsKey("season"), "Map contains key `season`")
        assertTrue(mappedQuery.containsKey("format_in"), "Map contains key `format_in`")
        assertTrue(mappedQuery.containsKey("onList"), "Map contains key `onList`")
        assertTrue(mappedQuery.containsKey("status_in"), "Map contains key `status_in`")

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
